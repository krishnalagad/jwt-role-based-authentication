package com.learnspring.jwt.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learnspring.jwt.dto.JwtAuthResponse;
import com.learnspring.jwt.dto.LoginDto;
import com.learnspring.jwt.dto.RegisterUserResponse;
import com.learnspring.jwt.entity.User;
import com.learnspring.jwt.service.AuthService;
import com.learnspring.jwt.service.ImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private ImageService imageService;

    @Value("${app.files.profile-image}")
    private String path;
    @Autowired
    private ObjectMapper mapper;

    private Logger logger = LoggerFactory.getLogger(AuthController.class);

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

    @PostMapping("/register-user/image")
    public ResponseEntity<RegisterUserResponse> registerWithImage(@RequestParam("file") MultipartFile file,
                                                                  @RequestParam("userData") String userData) {
        User user = null;

        // convert String data into JSON object.
        try {
            user = this.mapper.readValue(userData, User.class);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        this.logger.info("User JSON: {}", user);
        

        return null;
    }
}
