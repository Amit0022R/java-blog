package com.blogapp.controller;

import com.blogapp.entity.BlogPost;
import com.blogapp.entity.User;
import com.blogapp.service.BlogService;
import com.blogapp.service.LikeService;
import com.blogapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController {
    
    @Autowired
    private BlogService blogService;
    
    @Autowired
    private LikeService likeService;
    
    @Autowired
    private UserService userService;
    
    @GetMapping({"/", "/home"})
    public String home(Model model, Authentication authentication) {
        List<BlogPost> blogPosts = blogService.getAllBlogPosts();
        
        // Get actual likes count and user like status for each blog post
        Map<Long, Long> likesCountMap = new HashMap<>();
        Map<Long, Boolean> userLikedMap = new HashMap<>();
        
        User currentUser = null;
        if (authentication != null && authentication.getName() != null) {
            currentUser = userService.findByEmail(authentication.getName()).orElse(null);
        }
        
        for (BlogPost post : blogPosts) {
            // Get actual likes count from LikeService
            long actualLikesCount = likeService.getLikesCount(post);
            likesCountMap.put(post.getId(), actualLikesCount);
            
            // Check if current user has liked this post
            boolean userHasLiked = false;
            if (currentUser != null) {
                userHasLiked = likeService.hasUserLikedPost(currentUser, post);
            }
            userLikedMap.put(post.getId(), userHasLiked);
        }
        
        model.addAttribute("blogPosts", blogPosts);
        model.addAttribute("likesCountMap", likesCountMap);
        model.addAttribute("userLikedMap", userLikedMap);
        return "home";
    }
    
    @GetMapping("/about")
    public String about() {
        return "about";
    }
}
