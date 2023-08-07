package com.learnspring.jwt.service.impl;

import com.learnspring.jwt.dto.LoginDto;
import com.learnspring.jwt.dto.RegisterUserResponse;
import com.learnspring.jwt.entity.Role;
import com.learnspring.jwt.entity.User;
import com.learnspring.jwt.repository.RoleRepository;
import com.learnspring.jwt.repository.UserRepository;
import com.learnspring.jwt.security.JwtTokenProvider;
import com.learnspring.jwt.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Value("${user-role.admin.id}")
    private int adminId;

    @Value("${user-role.user.id}")
    private int userId;

    private Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Override
    public String login(LoginDto loginDto) {

        Authentication authenticate = this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authenticate);

        String token = this.jwtTokenProvider.generateToken(authenticate);

        return token;
    }

    @Override
    public User register(User user) {

        Role userRole = this.roleRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new RuntimeException("Role not found " +
                "with Id: "));

        // user role set to new user
        user.getRoles().add(userRole);

        // encrypt password before saving it to database.
        String encodedPassword = this.passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        // save new user to database.
        User savedUser = this.userRepository.save(user);


        return savedUser;
    }
}
