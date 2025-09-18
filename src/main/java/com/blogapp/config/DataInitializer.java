package com.blogapp.config;

import com.blogapp.entity.BlogPost;
import com.blogapp.entity.Comment;
import com.blogapp.entity.User;
import com.blogapp.service.BlogService;
import com.blogapp.service.CommentService;
import com.blogapp.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    
    private final UserService userService;
    private final BlogService blogService;
    private final CommentService commentService;
    
    public DataInitializer(UserService userService, BlogService blogService, CommentService commentService) {
        this.userService = userService;
        this.blogService = blogService;
        this.commentService = commentService;
    }
    
    @Override
    public void run(String... args) throws Exception {
        // Check if data already exists
        if (blogService.getAllBlogPosts().isEmpty()) {
            initializeData();
        }
    }
    
    private void initializeData() {
        try {
            // Create a sample user if no users exist
            if (!userService.existsByEmail("demo@example.com")) {
                User demoUser = new User();
                demoUser.setName("Demo User");
                demoUser.setEmail("demo@example.com");
                demoUser.setPassword("password123");
                demoUser.setRole(User.Role.USER);
                demoUser = userService.saveUser(demoUser);
                
                // Create sample blog posts
                BlogPost post1 = new BlogPost();
                post1.setTitle("Welcome to BlogApp!");
                post1.setContent("This is your first blog post on BlogApp. BlogApp is a modern, feature-rich blogging platform built with Spring Boot and Bootstrap 5.\n\nFeatures include:\n- User authentication and authorization\n- Create, edit, and delete blog posts\n- Like and comment on posts\n- Social media sharing\n- Responsive design\n- And much more!\n\nFeel free to create your own account and start blogging. Share your thoughts, stories, and expertise with the world!");
                post1.setAuthor(demoUser);
                post1.setLikesCount(5);
                post1 = blogService.saveBlogPost(post1);
                
                BlogPost post2 = new BlogPost();
                post2.setTitle("Getting Started with Spring Boot");
                post2.setContent("Spring Boot is an amazing framework for building Java applications quickly and efficiently.\n\nKey benefits of Spring Boot:\n\n1. **Auto-configuration**: Spring Boot automatically configures your application based on the dependencies you have added.\n\n2. **Embedded servers**: No need to deploy WAR files to external servers. Spring Boot comes with embedded Tomcat, Jetty, or Undertow.\n\n3. **Production-ready features**: Includes metrics, health checks, and externalized configuration out of the box.\n\n4. **No XML configuration**: Everything can be configured using Java annotations and properties files.\n\n5. **Microservices ready**: Perfect for building microservices architecture.\n\nThis blog application itself is built using Spring Boot, demonstrating its power and simplicity!");
                post2.setAuthor(demoUser);
                post2.setLikesCount(12);
                post2 = blogService.saveBlogPost(post2);
                
                BlogPost post3 = new BlogPost();
                post3.setTitle("The Power of Bootstrap for Responsive Design");
                post3.setContent("Bootstrap is the world's most popular CSS framework for building responsive, mobile-first websites.\n\nWhy choose Bootstrap?\n\n• **Responsive Grid System**: Create layouts that work on all devices\n• **Pre-built Components**: Buttons, forms, navigation, and more\n• **Customizable**: Easy to customize with Sass variables\n• **Browser Compatibility**: Works across all modern browsers\n• **Large Community**: Extensive documentation and community support\n\nThis BlogApp uses Bootstrap 5 to ensure it looks great on desktop, tablet, and mobile devices. The card-based layout for blog posts is responsive and adapts to different screen sizes seamlessly.\n\nBootstrap makes it easy to create professional-looking websites without writing extensive custom CSS!");
                post3.setAuthor(demoUser);
                post3.setLikesCount(8);
                post3 = blogService.saveBlogPost(post3);
                
                // Add some sample comments
                Comment comment1 = new Comment();
                comment1.setContent("Great introduction to BlogApp! Looking forward to using this platform.");
                comment1.setAuthor(demoUser);
                comment1.setBlogPost(post1);
                commentService.saveComment(comment1);
                
                Comment comment2 = new Comment();
                comment2.setContent("Spring Boot really is amazing. Thanks for the detailed explanation!");
                comment2.setAuthor(demoUser);
                comment2.setBlogPost(post2);
                commentService.saveComment(comment2);
                
                System.out.println("Sample data initialized successfully!");
            }
        } catch (Exception e) {
            System.err.println("Error initializing sample data: " + e.getMessage());
        }
    }
}
