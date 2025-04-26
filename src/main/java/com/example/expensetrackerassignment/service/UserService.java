package com.example.expensetrackerassignment.service;

import com.example.expensetrackerassignment.dto.UserDto;

public interface UserService {
    void register(UserDto userDto);
    String login(UserDto userDto);
}
