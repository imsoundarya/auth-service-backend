package com.preethy.moviebooking.controller;

import com.preethy.moviebooking.collections.UserInfo;
import com.preethy.moviebooking.exception.UserRegistrationError;
import com.preethy.moviebooking.jwt.service.JwtTokenService;
import com.preethy.moviebooking.jwt.service.UserInfoUserDetailsService;
import com.preethy.moviebooking.payload.JwtResponse;
import com.preethy.moviebooking.payload.LoginRequest;
import com.preethy.moviebooking.payload.ResetRequest;
import com.preethy.moviebooking.payload.SignupRequest;
import com.preethy.moviebooking.repositories.UserInfoRepository;
import com.preethy.moviebooking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1.0/auth")
@CrossOrigin("*")
public class AuthController {


    @Autowired
    private UserService userService;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private JwtTokenService tokenService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserInfoUserDetailsService userDetailsService;


    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signUpRequest) throws UserRegistrationError {
        ResponseEntity<?> response = userService.registerUser(signUpRequest);

        if (response.getStatusCode().equals(HttpStatus.OK)) {
            return ResponseEntity.ok().body(response.getBody());
        } else {
            return ResponseEntity.badRequest().body(response.getBody());
        }
    }


    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {

        var authenticationToken = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                loginRequest.getPassword());

        var authentication = authenticationManager.authenticate(authenticationToken);

        String token;
        if (authentication.isAuthenticated()) {
            token = tokenService.generateToken(loginRequest.getUsername());
        } else {
            throw new UsernameNotFoundException("Invalid user credentials !");
        }
        UserInfo userInfo = userInfoRepository.findByUsername(loginRequest.getUsername());

        return ResponseEntity.ok(new JwtResponse(token, userInfo.getId(),
                userInfo.getUsername(), userInfo.getEmail(),
                userInfo.getRole()));

    }


    @PostMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String token) {


        String username = tokenService.extractUsername(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (tokenService.validateToken(token, userDetails)) {
            Map<String, String> userInfo = new HashMap<>();
            String role = userService.getrole(username);
            userInfo.put(username, role);
            return ResponseEntity.status(HttpStatus.OK).body(userInfo);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
    }

    @GetMapping("/getRole")
    public String getRole(@RequestHeader("Authorization") String token) {
        String username = tokenService.extractUsername(token);
        return userService.getrole(username);
    }

    @PatchMapping("/forgot/{username}/updatePassword")
    public ResponseEntity<?> forgotPassword(@PathVariable(name = "username") String username,
                                            @RequestBody ResetRequest resetRequest) {
        return userService.updatePassword(username, resetRequest);

    }

}
