package com.example.helloworld.controllers;

import com.example.helloworld.models.Post;
import com.example.helloworld.models.User;
import com.example.helloworld.services.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private final PostService postService;

    @Value("${upload.directory}")
    private String uploadDir;

    @Autowired
    private ObjectMapper objectMapper;

    public PostController(PostService postService, ObjectMapper objectMapper) {
        this.postService = postService;
        this.objectMapper = objectMapper;
    }

    // @GetMapping
    // public ResponseEntity<List<Post>> getAllPosts() {
    //     List<Post> posts = postService.getAllPosts();
    //     return new ResponseEntity<>(posts, HttpStatus.OK);
    // }
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllPosts(@RequestParam(defaultValue = "0") int page, 
                                                           @RequestParam(defaultValue = "6") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Post> postPage = postService.getPosts(pageRequest);
    
        Map<String, Object> response = new HashMap<>();
        response.put("posts", postPage.getContent()); // List of posts
        response.put("total", postPage.getTotalElements()); // Total posts count
    
        return ResponseEntity.ok(response);
    }
    
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadPost(
            @RequestParam("user") String userJson,
            @RequestParam("caption") String caption,
            @RequestParam("image") MultipartFile image) {

        System.out.println("User: " + userJson);
        System.out.println("Caption: " + caption);
        System.out.println("Image: " + image.getResource());

        try {
            // Ensure the upload directory exists
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs(); // Create directory if it doesn't exist
            }

            // Construct the file name and target path
            String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
            Path targetLocation = Paths.get(uploadDir + fileName);

            // Transfer the file to the specified path
            Files.copy(image.getInputStream(), targetLocation);

            // Convert user JSON to User object (you may use a library like Jackson)
            User user = objectMapper.readValue(userJson, User.class);

            Post post = new Post();
            post.setUser(user);
            post.setCaption(caption);
            post.setImageUrl("/uploads/" + fileName);
            post.setCreatedAt(LocalDateTime.now());

            Post uploadedPost = postService.uploadPost(post);
            return new ResponseEntity<>(uploadedPost, HttpStatus.CREATED);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to upload file: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to process post: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Long id) {
        Post post = postService.getPostById(id);
        return post != null ? new ResponseEntity<>(post, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
