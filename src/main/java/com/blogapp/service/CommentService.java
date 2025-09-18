package com.blogapp.service;

import com.blogapp.entity.BlogPost;
import com.blogapp.entity.Comment;
import com.blogapp.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    
    @Autowired
    private CommentRepository commentRepository;
    
    public Comment saveComment(Comment comment) {
        return commentRepository.save(comment);
    }
    
    public List<Comment> getCommentsByBlogPost(BlogPost blogPost) {
        return commentRepository.findByBlogPostOrderByCreatedAtDesc(blogPost);
    }
    
    public List<Comment> getCommentsByBlogPostId(Long blogPostId) {
        return commentRepository.findByBlogPostIdOrderByCreatedAtDesc(blogPostId);
    }
    
    public Optional<Comment> getCommentById(Long id) {
        return commentRepository.findById(id);
    }
    
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }
}
