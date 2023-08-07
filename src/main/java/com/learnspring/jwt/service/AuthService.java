package com.learnspring.jwt.service;

import com.learnspring.jwt.dto.LoginDto;
import com.learnspring.jwt.dto.RegisterUserResponse;
import com.learnspring.jwt.entity.User;

public interface AuthService {

    String login(LoginDto loginDto);

    User register(User user);
}
