package com.preethy.moviebooking.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtTokenResponse {

    private String token;
    private String id;
    private String username;
    private String email;
    private String role;

}
