package com.learnspring.jwt.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "jwt_roleauth_users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User {

    private Long id;
    private String name;
    private String username;
    private String email;
    private String password;
    private String profile;
}
