package com.forumly.web.repository;

import com.forumly.web.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    @Query(value = "SELECT * FROM comments WHERE user_id = :userId",nativeQuery = true)
    List<Comment> findByUserId(Long userId);

    @Query(value = "SELECT * FROM comments WHERE post_id = :postId",nativeQuery = true)
    List<Comment> findByPostId(Long postId);
}
