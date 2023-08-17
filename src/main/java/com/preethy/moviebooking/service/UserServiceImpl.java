package com.preethy.moviebooking.service;

import com.preethy.moviebooking.collections.UserInfo;
import com.preethy.moviebooking.exception.UserRegistrationError;
import com.preethy.moviebooking.payload.ResetRequest;
import com.preethy.moviebooking.payload.SignupRequest;
import com.preethy.moviebooking.repositories.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserInfoRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public ResponseEntity<?> registerUser(SignupRequest userModel) throws UserRegistrationError {

        if (userRepository.existsByUsername(userModel.getUsername())) {
            return new ResponseEntity<>("Username is already exists!", HttpStatus.BAD_REQUEST);
        }

        if (userRepository.existsByEmail(userModel.getEmail())) {
            return new ResponseEntity<>("Email is already exists!", HttpStatus.BAD_REQUEST);
        }

        UserInfo byUserName = userRepository.findByUsername(userModel.getUsername());

        if (byUserName == null) {
            UserInfo user = UserInfo.builder()
                    .username(userModel.getUsername())
                    .email(userModel.getEmail())
                    .password(passwordEncoder.encode(userModel.getPassword()))
                    .securityAnswer(userModel.getSecurityAnswer())
                    .securityQuestion(userModel.getSecurityQuestion())
                    .role(userModel.getRole())
                    .build();

            userRepository.save(user);

            return ResponseEntity.ok().body("{\"message\": \"User registered successfully\"}");
        } else {
            throw new UserRegistrationError("User already exist");
        }

    }

    @Override
    public String getrole(String username) {

        UserInfo userObj = userRepository.findByUsername(username);
        return userObj.getRole();
    }

    @Override
    public Optional<UserInfo> getUserByUsername(String username) {
        Optional<UserInfo> user = Optional.ofNullable(userRepository.findByUsername(username));
        if (user.isEmpty()) {
            throw new RuntimeException("username is not present");
        }
        return user;
    }

    @Override
    public ResponseEntity<?> updatePassword(String username, ResetRequest resetRequest) {
        if (userRepository.findByUsername(resetRequest.getUsername()) == null) {
            return new ResponseEntity<>("Username doesn't exists!", HttpStatus.BAD_REQUEST);
        }
        Optional<UserInfo> userdata = Optional.ofNullable(userRepository.findByUsername(resetRequest.getUsername()));
        if (userdata.isPresent() && resetRequest.getSecQuestion().equals(userdata.get().getSecurityQuestion())
                && resetRequest.getSecAnswer().equals(userdata.get().getSecurityAnswer())) {
            userdata.get().setPassword(resetRequest.getNewPassword());
            userRepository.save(userdata.get());
            return new ResponseEntity<>("changed password successfully!", HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>("could not update password(cause:sec ques not match)!", HttpStatus.BAD_REQUEST);

    }

}
