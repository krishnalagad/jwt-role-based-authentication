package com.learnspring.jwt.service;

import com.learnspring.jwt.dto.LoginDto;

public interface AuthService {

    String login(LoginDto loginDto);
}
