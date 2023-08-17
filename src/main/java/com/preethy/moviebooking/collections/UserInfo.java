package com.preethy.moviebooking.collections;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "users")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserInfo {


    @Id
    private String id;

    private String username;

    private String email;

    private String password;

    private String securityQuestion;

    private String securityAnswer;

    private String role = "customer";



}