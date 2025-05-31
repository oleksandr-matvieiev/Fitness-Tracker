package com.lab.fitnesstracker.service;

import com.lab.fitnesstracker.model.User;
import com.lab.fitnesstracker.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        userService = new UserService(userRepository, passwordEncoder);
    }

    @Test
    void registerSuccess() {
        User request = new User();
        request.setUsername("username");
        request.setPassword("password");

        when(userRepository.findByUsername("username")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password")).thenReturn("encodedPass");

        userService.register(request);

        verify(userRepository).save(argThat(user ->
                user.getUsername().equals("username") && user.getPassword().equals("encodedPass")
        ));
    }

    @Test
    void registerFailIfUsernameTaken() {
        when(userRepository.findByUsername("username")).thenReturn(Optional.of(new User()));

        User request = new User();
        request.setUsername("username");

        assertThrows(IllegalArgumentException.class, () -> userService.register(request));
    }

    @Test
    void getUserByIdSuccess() {
        User user = new User();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.getUserById(1L);
        assertEquals(1L, result.getId());
    }

    @Test
    void getUserByIdNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> userService.getUserById(99L));
    }

    @Test
    void getCurrentUserSuccess() {
        User user = new User();
        user.setUsername("username");

        Authentication authentication = mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn("username");

        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(authentication);

        try (MockedStatic<SecurityContextHolder> mocked = mockStatic(SecurityContextHolder.class)) {
            mocked.when(SecurityContextHolder::getContext).thenReturn(context);
            when(userRepository.findByUsername("username")).thenReturn(Optional.of(user));

            User result = userService.getCurrentUser();
            assertEquals("username", result.getUsername());
        }
    }

    @Test
    void getCurrentUserNotFound() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn("username");

        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(authentication);

        try (MockedStatic<SecurityContextHolder> mocked = mockStatic(SecurityContextHolder.class)) {
            mocked.when(SecurityContextHolder::getContext).thenReturn(context);
            when(userRepository.findByUsername("username")).thenReturn(Optional.empty());

            assertThrows(IllegalArgumentException.class, () -> userService.getCurrentUser());
        }
    }
}
