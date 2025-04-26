package com.example.expensetrackerassignment.service.implementation;

import com.example.expensetrackerassignment.config.security.JwtUtils;
import com.example.expensetrackerassignment.dto.UserDto;
import com.example.expensetrackerassignment.entity.User;
import com.example.expensetrackerassignment.exception.InvalidCredentialsException;
import com.example.expensetrackerassignment.repository.UserRepository;
import com.example.expensetrackerassignment.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void register(UserDto userDto) {
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setName(userDto.getName());
        user.setRole(userDto.getRole());
        userRepository.save(user);
    }

    public String login(UserDto userDto) {
        User user = userRepository.findByEmail(userDto.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(userDto.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        List<String> roles = List.of(String.valueOf(user.getRole()));
        return JwtUtils.generateToken(user.getEmail(), roles);
    }
}
