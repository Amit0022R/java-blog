package com.blogapp.repository;

import com.blogapp.entity.BlogPost;
import com.blogapp.entity.Like;
import com.blogapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    
    /**
     * Find a like by user and blog post
     */
    Optional<Like> findByUserAndBlogPost(User user, BlogPost blogPost);
    
    /**
     * Check if a user has liked a specific blog post
     */
    boolean existsByUserAndBlogPost(User user, BlogPost blogPost);
    
    /**
     * Count total likes for a blog post
     */
    long countByBlogPost(BlogPost blogPost);
    
    /**
     * Delete a like by user and blog post
     */
    void deleteByUserAndBlogPost(User user, BlogPost blogPost);
    
    /**
     * Get all likes for a specific blog post
     */
    @Query("SELECT l FROM Like l WHERE l.blogPost = :blogPost")
    java.util.List<Like> findByBlogPost(@Param("blogPost") BlogPost blogPost);
    
    /**
     * Get all likes by a specific user
     */
    @Query("SELECT l FROM Like l WHERE l.user = :user")
    java.util.List<Like> findByUser(@Param("user") User user);
    
    /**
     * Delete all likes for a specific blog post
     */
    @Modifying
    @Query("DELETE FROM Like l WHERE l.blogPost = :blogPost")
    void deleteAllByBlogPost(@Param("blogPost") BlogPost blogPost);
}
