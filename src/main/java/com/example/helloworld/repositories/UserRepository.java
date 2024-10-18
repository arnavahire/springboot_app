package com.example.helloworld.repositories;

import com.example.helloworld.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    // Custom queries (if needed)
    User findByEmail(String email);
}