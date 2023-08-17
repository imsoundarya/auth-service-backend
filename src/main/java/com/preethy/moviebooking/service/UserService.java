package com.preethy.moviebooking.service;


import com.preethy.moviebooking.collections.UserInfo;
import com.preethy.moviebooking.exception.UserRegistrationError;
import com.preethy.moviebooking.payload.ResetRequest;
import com.preethy.moviebooking.payload.SignupRequest;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface UserService {


    ResponseEntity<?> registerUser(SignupRequest userModel) throws UserRegistrationError;

    String getrole(String username);


    Optional<UserInfo> getUserByUsername(String username);

    ResponseEntity<?> updatePassword(String username, ResetRequest resetRequest);

}
