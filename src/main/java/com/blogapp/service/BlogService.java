package com.blogapp.service;

import com.blogapp.entity.BlogPost;
import com.blogapp.entity.User;
import com.blogapp.repository.BlogPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BlogService {
    
    @Autowired
    private BlogPostRepository blogPostRepository;
    
    public List<BlogPost> getAllBlogPosts() {
        return blogPostRepository.findAllByOrderByCreatedAtDesc();
    }
    
    public Optional<BlogPost> getBlogPostById(Long id) {
        return blogPostRepository.findById(id);
    }
    
    public BlogPost saveBlogPost(BlogPost blogPost) {
        return blogPostRepository.save(blogPost);
    }
    
    public void deleteBlogPost(Long id) {
        blogPostRepository.deleteById(id);
    }
    
    public List<BlogPost> getBlogPostsByAuthor(User author) {
        return blogPostRepository.findByAuthorOrderByCreatedAtDesc(author);
    }
    
    public List<BlogPost> searchBlogPosts(String keyword) {
        return blogPostRepository.findByTitleContainingOrContentContainingOrderByCreatedAtDesc(keyword);
    }
}
