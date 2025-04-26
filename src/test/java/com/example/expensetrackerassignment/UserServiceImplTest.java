package com.example.expensetrackerassignment;

import com.example.expensetrackerassignment.dto.UserDto;
import com.example.expensetrackerassignment.entity.User;
import com.example.expensetrackerassignment.enums.Role;
import com.example.expensetrackerassignment.exception.InvalidCredentialsException;
import com.example.expensetrackerassignment.repository.UserRepository;
import com.example.expensetrackerassignment.service.implementation.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser() {
        UserDto userDto = new UserDto();
        userDto.setEmail("test@example.com");
        userDto.setPassword("password123");
        userDto.setName("Test User");
        userDto.setRole(Role.valueOf("USER"));

        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        userService.register(userDto);

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testLogin_Success() {
        UserDto userDto = new UserDto();
        userDto.setEmail("test@example.com");
        userDto.setPassword("password123");

        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        String token = userService.login(userDto);

        assertNotNull(token);
    }

    @Test
    void testLogin_InvalidEmail() {
        UserDto userDto = new UserDto();
        userDto.setEmail("wrong@example.com");
        userDto.setPassword("password123");

        when(userRepository.findByEmail("wrong@example.com")).thenReturn(Optional.empty());

        assertThrows(InvalidCredentialsException.class, () -> userService.login(userDto));
    }

    @Test
    void testLogin_InvalidPassword() {
        UserDto userDto = new UserDto();
        userDto.setEmail("test@example.com");
        userDto.setPassword("wrongpassword");

        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        assertThrows(InvalidCredentialsException.class, () -> userService.login(userDto));
    }
}
