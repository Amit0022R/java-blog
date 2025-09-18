package com.blogapp.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "likes", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "blog_post_id"}))
public class Like {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blog_post_id", nullable = false)
    private BlogPost blogPost;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    public Like() {
        this.createdAt = LocalDateTime.now();
    }
    
    public Like(User user, BlogPost blogPost) {
        this.user = user;
        this.blogPost = blogPost;
        this.createdAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public BlogPost getBlogPost() {
        return blogPost;
    }
    
    public void setBlogPost(BlogPost blogPost) {
        this.blogPost = blogPost;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Like)) return false;
        Like like = (Like) o;
        return user != null && user.equals(like.user) && 
               blogPost != null && blogPost.equals(like.blogPost);
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
