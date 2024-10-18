package com.example.helloworld.models;

import jakarta.persistence.*;
import java.util.List;
import java.sql.Timestamp;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;
    private String password;
    private Timestamp created_at;

    // @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    // private List<Post> posts;

    // Getters and Setters

    /*
    GETTERS and SETTERS ARE A MUST in order to use jparepository's findall() function.

    Reason:

    JPA uses reflection to access the fields of your entity classes. When you retrieve data using methods like findAll(), 
    JPA needs to access the properties of your entities. 
    Without getters and setters, JPA cannot properly set the values of your entity attributes when populating them from the database.
    */ 

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Timestamp getCreatedAt() {
        return created_at;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.created_at = createdAt;
    }

}
