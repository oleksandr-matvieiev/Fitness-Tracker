package com.lab.fitnesstracker.controller;

import com.lab.fitnesstracker.model.User;
import com.lab.fitnesstracker.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    private final UserService userService = mock(UserService.class);
    private final UserController userController = new UserController(userService);

    @Test
    void registerSuccess() {
        User request = new User();
        doNothing().when(userService).register(request);

        ResponseEntity<String> response = userController.register(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("User registered successfully", response.getBody());
    }

    @Test
    void registerFail() {
        User request = new User();
        doThrow(new IllegalArgumentException("Username is already in use")).when(userService).register(request);

        ResponseEntity<String> response = userController.register(request);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Username is already in use", response.getBody());
    }

    @Test
    void getUserByIdSuccess() {
        User user = new User();
        user.setId(1L);
        when(userService.getUserById(1L)).thenReturn(user);

        ResponseEntity<User> response = userController.getUser(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void getUserByIdNotFound() {
        when(userService.getUserById(99L)).thenThrow(new IllegalArgumentException());

        ResponseEntity<User> response = userController.getUser(99L);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void getCurrentUserSuccess() {
        User user = new User();
        user.setUsername("alex");
        when(userService.getCurrentUser()).thenReturn(user);

        ResponseEntity<User> response = userController.getCurrentUser();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("alex", response.getBody().getUsername());
    }

    @Test
    void getCurrentUserFail() {
        when(userService.getCurrentUser()).thenThrow(new IllegalArgumentException());

        ResponseEntity<User> response = userController.getCurrentUser();

        assertEquals(404, response.getStatusCodeValue());
    }
}
