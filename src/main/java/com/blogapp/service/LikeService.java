package com.blogapp.service;

import com.blogapp.entity.BlogPost;
import com.blogapp.entity.Like;
import com.blogapp.entity.User;
import com.blogapp.repository.LikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class LikeService {
    
    @Autowired
    private LikeRepository likeRepository;
    
    /**
     * Toggle like status for a blog post by a user
     * Returns true if liked, false if unliked
     */
    public boolean toggleLike(User user, BlogPost blogPost) {
        if (user == null || blogPost == null) {
            throw new IllegalArgumentException("User and BlogPost cannot be null");
        }
        
        Optional<Like> existingLike = likeRepository.findByUserAndBlogPost(user, blogPost);
        
        if (existingLike.isPresent()) {
            // Unlike: remove the like
            likeRepository.delete(existingLike.get());
            return false; // Unliked
        } else {
            // Like: create a new like
            Like newLike = new Like(user, blogPost);
            likeRepository.save(newLike);
            return true; // Liked
        }
    }
    
    /**
     * Check if a user has liked a specific blog post
     */
    public boolean hasUserLikedPost(User user, BlogPost blogPost) {
        if (user == null || blogPost == null) {
            return false;
        }
        return likeRepository.existsByUserAndBlogPost(user, blogPost);
    }
    
    /**
     * Get the total number of likes for a blog post
     */
    public long getLikesCount(BlogPost blogPost) {
        if (blogPost == null) {
            return 0;
        }
        return likeRepository.countByBlogPost(blogPost);
    }
    
    /**
     * Save a like
     */
    public Like saveLike(Like like) {
        return likeRepository.save(like);
    }
    
    /**
     * Delete a like
     */
    public void deleteLike(Like like) {
        likeRepository.delete(like);
    }
    
    /**
     * Find like by user and blog post
     */
    public Optional<Like> findLikeByUserAndBlogPost(User user, BlogPost blogPost) {
        return likeRepository.findByUserAndBlogPost(user, blogPost);
    }
    
    /**
     * Find all likes for a specific blog post
     */
    public java.util.List<Like> findLikesByBlogPost(BlogPost blogPost) {
        if (blogPost == null) {
            return new java.util.ArrayList<>();
        }
        return likeRepository.findByBlogPost(blogPost);
    }
    
    /**
     * Delete all likes for a specific blog post (used when deleting blog post)
     */
    public void deleteAllLikesForBlogPost(BlogPost blogPost) {
        if (blogPost != null) {
            likeRepository.deleteAllByBlogPost(blogPost);
        }
    }
}
