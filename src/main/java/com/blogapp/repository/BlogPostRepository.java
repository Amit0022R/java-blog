package com.blogapp.repository;

import com.blogapp.entity.BlogPost;
import com.blogapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {
    
    List<BlogPost> findByAuthorOrderByCreatedAtDesc(User author);
    
    List<BlogPost> findAllByOrderByCreatedAtDesc();
    
    @Query("SELECT b FROM BlogPost b WHERE b.title LIKE %?1% OR b.content LIKE %?1% ORDER BY b.createdAt DESC")
    List<BlogPost> findByTitleContainingOrContentContainingOrderByCreatedAtDesc(String keyword);
}
