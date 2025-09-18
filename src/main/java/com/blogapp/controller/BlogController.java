package com.blogapp.controller;

import com.blogapp.entity.BlogPost;
import com.blogapp.entity.Comment;
import com.blogapp.entity.User;
import com.blogapp.service.BlogService;
import com.blogapp.service.CommentService;
import com.blogapp.service.LikeService;
import com.blogapp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import java.util.Optional;

@Controller
@RequestMapping("/blog")
public class BlogController {
    
    @Autowired
    private BlogService blogService;
    
    @Autowired
    private CommentService commentService;
    
    @Autowired
    private LikeService likeService;
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("blogPost", new BlogPost());
        return "blog/create";
    }
    
    @PostMapping("/create")
    public String createBlogPost(@Valid @ModelAttribute("blogPost") BlogPost blogPost,
                                BindingResult result, Authentication authentication) {
        
        if (result.hasErrors()) {
            return "blog/create";
        }
        
        // Check if authentication is null
        if (authentication == null || authentication.getName() == null) {
            return "redirect:/login";
        }
        
        try {
            User author = userService.findByEmail(authentication.getName()).orElse(null);
            if (author != null) {
                blogPost.setAuthor(author);
                blogService.saveBlogPost(blogPost);
            } else {
                // Author not found, redirect to login
                return "redirect:/login?error=usernotfound";
            }
        } catch (Exception e) {
            // Log error and redirect with error message
            return "redirect:/?error=createfailed";
        }
        
        return "redirect:/";
    }
    @GetMapping("/view/{id}")
    public String viewBlogPost(@PathVariable Long id, Model model, Authentication authentication) {
        // Validate input
        if (id == null || id <= 0) {
            return "redirect:/?error=invalidid";
        }
        
        try {
            Optional<BlogPost> blogPostOpt = blogService.getBlogPostById(id);
            
            if (blogPostOpt.isEmpty()) {
                return "redirect:/?error=notfound";
            }
            
            BlogPost blogPost = blogPostOpt.get();
            
            // Ensure author is not null
            if (blogPost.getAuthor() == null) {
                User defaultAuthor = new User();
                defaultAuthor.setName("Unknown Author");
                defaultAuthor.setEmail("unknown@example.com");
                blogPost.setAuthor(defaultAuthor);
            }
            
            // Ensure title and content are not null
            if (blogPost.getTitle() == null) {
                blogPost.setTitle("Untitled Post");
            }
            if (blogPost.getContent() == null) {
                blogPost.setContent("No content available.");
            }
            
            // Ensure likes count is not null
            if (blogPost.getLikesCount() == null) {
                blogPost.setLikesCount(0);
            }
            
            // Get comments safely
            List<Comment> comments = commentService.getCommentsByBlogPost(blogPost);
            if (comments == null) {
                comments = new ArrayList<>();
            }
            
            // Get actual likes count from LikeService
            long actualLikesCount = likeService.getLikesCount(blogPost);
            
            // Check if current user has liked this post
            boolean userHasLiked = false;
            if (authentication != null && authentication.getName() != null) {
                User currentUser = userService.findByEmail(authentication.getName()).orElse(null);
                if (currentUser != null) {
                    userHasLiked = likeService.hasUserLikedPost(currentUser, blogPost);
                }
            }
            
            model.addAttribute("blogPost", blogPost);
            model.addAttribute("comments", comments);
            model.addAttribute("newComment", new Comment());
            model.addAttribute("actualLikesCount", actualLikesCount);
            model.addAttribute("userHasLiked", userHasLiked);
            
            return "blog/view";
        } catch (Exception e) {
            // Handle any database or other errors
            return "redirect:/?error=database";
        }
    }
    
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, Authentication authentication) {
        // Validate input
        if (id == null || id <= 0) {
            return "redirect:/?error=invalidid";
        }
        
        // Check authentication
        if (authentication == null || authentication.getName() == null) {
            return "redirect:/login";
        }
        
        try {
            Optional<BlogPost> blogPostOpt = blogService.getBlogPostById(id);
            if (blogPostOpt.isPresent()) {
                BlogPost blogPost = blogPostOpt.get();
                User currentUser = userService.findByEmail(authentication.getName()).orElse(null);
                
                // Check if blog post has author and current user exists
                if (currentUser != null && blogPost.getAuthor() != null && 
                    blogPost.getAuthor().getId() != null && 
                    blogPost.getAuthor().getId().equals(currentUser.getId())) {
                    model.addAttribute("blogPost", blogPost);
                    return "blog/edit";
                } else {
                    return "redirect:/?error=unauthorized";
                }
            }
        } catch (Exception e) {
            return "redirect:/?error=database";
        }
        return "redirect:/?error=notfound";
    }
    
    @PostMapping("/edit/{id}")
    public String updateBlogPost(@PathVariable Long id, 
                                @Valid @ModelAttribute("blogPost") BlogPost updatedBlogPost,
                                BindingResult result, Authentication authentication) {
        
        if (result.hasErrors()) {
            return "blog/edit";
        }
        
        Optional<BlogPost> blogPostOpt = blogService.getBlogPostById(id);
        if (blogPostOpt.isPresent()) {
            BlogPost existingBlogPost = blogPostOpt.get();
            User currentUser = userService.findByEmail(authentication.getName()).orElse(null);
            
            if (currentUser != null && existingBlogPost.getAuthor().getId().equals(currentUser.getId())) {
                existingBlogPost.setTitle(updatedBlogPost.getTitle());
                existingBlogPost.setContent(updatedBlogPost.getContent());
                blogService.saveBlogPost(existingBlogPost);
                return "redirect:/blog/view/" + id;
            }
        }
        
        return "redirect:/";
    }
    
    @PostMapping("/delete/{id}")
    public String deleteBlogPost(@PathVariable Long id, Authentication authentication) {
        // Validate input
        if (id == null || id <= 0) {
            return "redirect:/?error=invalidid";
        }
        
        // Check authentication
        if (authentication == null || authentication.getName() == null) {
            return "redirect:/login";
        }
        
        try {
            Optional<BlogPost> blogPostOpt = blogService.getBlogPostById(id);
            if (blogPostOpt.isPresent()) {
                BlogPost blogPost = blogPostOpt.get();
                User currentUser = userService.findByEmail(authentication.getName()).orElse(null);
                
                // Check authorization - only author can delete
                if (currentUser != null && blogPost.getAuthor() != null && 
                    blogPost.getAuthor().getId() != null && 
                    blogPost.getAuthor().getId().equals(currentUser.getId())) {
                    
                    // Delete related entities first to avoid foreign key constraints
                    
                    // 1. Delete all likes for this blog post (efficient batch delete)
                    try {
                        likeService.deleteAllLikesForBlogPost(blogPost);
                    } catch (Exception e) {
                        // Log error but continue with deletion
                        System.err.println("Error deleting likes: " + e.getMessage());
                    }
                    
                    // 2. Comments will be automatically deleted due to cascade = CascadeType.ALL in BlogPost entity
                    
                    // 3. Finally delete the blog post (comments will be cascaded automatically)
                    blogService.deleteBlogPost(id);
                    
                    return "redirect:/?success=deleted";
                } else {
                    return "redirect:/?error=unauthorized";
                }
            } else {
                return "redirect:/?error=notfound";
            }
        } catch (Exception e) {
            System.err.println("Error deleting blog post: " + e.getMessage());
            return "redirect:/?error=deletefailed";
        }
    }
    
    @PostMapping("/like/{id}")
    @ResponseBody
    public ResponseEntity<?> toggleLikeBlogPost(@PathVariable Long id, Authentication authentication) {
        // Check authentication
        if (authentication == null || authentication.getName() == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Authentication required"));
        }
        
        // Validate input
        if (id == null || id <= 0) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid blog post ID"));
        }
        
        try {
            // Get the blog post
            Optional<BlogPost> blogPostOpt = blogService.getBlogPostById(id);
            if (blogPostOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            
            BlogPost blogPost = blogPostOpt.get();
            
            // Get the current user
            User currentUser = userService.findByEmail(authentication.getName()).orElse(null);
            if (currentUser == null) {
                return ResponseEntity.status(401).body(Map.of("error", "User not found"));
            }
            
            // Toggle like status
            boolean isLiked = likeService.toggleLike(currentUser, blogPost);
            
            // Get updated likes count
            long likesCount = likeService.getLikesCount(blogPost);
            
            // Return response with like status and count
            return ResponseEntity.ok(Map.of(
                "success", true,
                "liked", isLiked,
                "likes", likesCount,
                "message", isLiked ? "Post liked!" : "Post unliked!"
            ));
            
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Failed to toggle like: " + e.getMessage()));
        }
    }
    
    @PostMapping("/comment/{id}")
    public String addComment(@PathVariable Long id, 
                            @Valid @ModelAttribute("newComment") Comment comment,
                            BindingResult result, Authentication authentication) {
        
        if (!result.hasErrors()) {
            Optional<BlogPost> blogPostOpt = blogService.getBlogPostById(id);
            User author = userService.findByEmail(authentication.getName()).orElse(null);
            
            if (blogPostOpt.isPresent() && author != null) {
                comment.setBlogPost(blogPostOpt.get());
                comment.setAuthor(author);
                commentService.saveComment(comment);
            }
        }
        
        return "redirect:/blog/view/" + id;
    }
}
