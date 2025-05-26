package com.lab.fitnesstracker.service;

import com.lab.fitnesstracker.model.User;
import com.lab.fitnesstracker.repository.UserRepository;
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
        request.setUsername(request.getUsername());
        request.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(createdUser);
    }

}
