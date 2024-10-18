package com.example.helloworld.controllers;

import com.example.helloworld.models.User;
import com.example.helloworld.repositories.UserRepository;
import com.example.helloworld.services.UserService;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class SignInController {
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody Map<String, String> signInRequest) {
        String email = signInRequest.get("email");
        String password = signInRequest.get("password");

        // Add your authentication logic here
        // For example, find the user by email and check the password
        User user = userRepository.findByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            // You can return a JWT token or user data
            System.out.println(user);
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }
    }
}
