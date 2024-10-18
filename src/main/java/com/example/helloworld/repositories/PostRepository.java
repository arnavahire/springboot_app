package com.example.helloworld.repositories;

import com.example.helloworld.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
    // Custom queries (if needed)
}
