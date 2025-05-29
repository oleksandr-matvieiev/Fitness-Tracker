package com.lab.fitnesstracker.service;

import com.lab.fitnesstracker.model.User;
import com.lab.fitnesstracker.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public void register(User request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username is already in use");
        }

        User createdUser = new User();
        createdUser.setUsername(request.getUsername());
        createdUser.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(createdUser);
    }

    public User getUserById(Long id) {

        return userRepository.findById(id).
                orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalArgumentException("No authenticated user found.");
        }

        Object principal = authentication.getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else if (principal instanceof String) {
            username = (String) principal;
        } else {
            throw new IllegalArgumentException("Unexpected authentication principal.");
        }
        return userRepository.findByUsername(username)
                .orElseThrow(IllegalArgumentException::new);
    }
}

