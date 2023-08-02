package com.learnspring.jwt.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "jwt_roleauth_roles")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
}
