package com.learnspring.jwt.controller;

import com.learnspring.jwt.dto.JwtAuthResponse;
import com.learnspring.jwt.dto.LoginDto;
import com.learnspring.jwt.dto.RegisterUserResponse;
import com.learnspring.jwt.entity.User;
import com.learnspring.jwt.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/")
    public ResponseEntity<JwtAuthResponse> login(@RequestBody LoginDto loginDto) {

        String token = this.authService.login(loginDto);

        JwtAuthResponse authResponse = new JwtAuthResponse();
        authResponse.setAccessToken(token);

        return ResponseEntity.status(HttpStatus.OK).body(authResponse);
    }

    @PostMapping("/register-user")
    public ResponseEntity<RegisterUserResponse> register(@RequestBody User user) {

        User savedUser = this.authService.register(user);

        String roleName = savedUser.getRoles().iterator().next().getName();

        RegisterUserResponse response = new RegisterUserResponse(savedUser.getId(), savedUser.getName(), savedUser.getUsername(),
                savedUser.getEmail(), savedUser.getProfile(), roleName);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
