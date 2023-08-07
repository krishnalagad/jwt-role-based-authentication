package com.learnspring.jwt.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserResponse {

    private Long id;

    private String name;

    private String username;

    private String email;

    private String profile;

    private String role;
}
