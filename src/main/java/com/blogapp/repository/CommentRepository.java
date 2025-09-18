package com.blogapp.repository;

import com.blogapp.entity.BlogPost;
import com.blogapp.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    
    List<Comment> findByBlogPostOrderByCreatedAtDesc(BlogPost blogPost);
    
    List<Comment> findByBlogPostIdOrderByCreatedAtDesc(Long blogPostId);
}
