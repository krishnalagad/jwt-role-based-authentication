package com.learnspring.jwt.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learnspring.jwt.dto.JwtAuthResponse;
import com.learnspring.jwt.dto.LoginDto;
import com.learnspring.jwt.dto.RegisterUserResponse;
import com.learnspring.jwt.entity.User;
import com.learnspring.jwt.repository.UserRepository;
import com.learnspring.jwt.service.AuthService;
import com.learnspring.jwt.service.ImageService;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private ImageService imageService;

    @Value("${app.files.profile-image}")
    private String path;
    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private UserRepository userRepository;

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
                                                                  @RequestParam("userData") String userData) throws IOException {
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

        // assign role to new user and save it to database.
        User saveUser = this.authService.register(user);

        String fileName = this.imageService.uploadImage(this.path, file, saveUser.getId());
        saveUser.setProfile(fileName);

        User updateUser = this.userRepository.save(saveUser);
        String roleName = updateUser.getRoles().iterator().next().getName();

        RegisterUserResponse registerUserResponse = new RegisterUserResponse(updateUser.getId(), updateUser.getName()
                , updateUser.getUsername(), updateUser.getEmail(), updateUser.getProfile(), roleName);

        return ResponseEntity.status(HttpStatus.CREATED).body(registerUserResponse);
    }

    // API to serve/retrieve file
    @GetMapping(value = "/user/image/{imageName}", produces = MediaType.IMAGE_JPEG_VALUE)
    public void getImage(@PathVariable("imageName") String imageaName, HttpServletResponse response)
            throws IOException {

        InputStream resource = this.imageService.getResource(path, imageaName);

        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());

    }
}
