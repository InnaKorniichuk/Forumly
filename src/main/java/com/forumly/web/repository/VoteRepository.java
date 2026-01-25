package com.forumly.web.repository;

import com.forumly.web.model.Vote;
import com.forumly.web.model.VoteType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    boolean existsByUserIdAndCommentId (Long userId, Long commentId);

    int countByCommentIdAndType(Long commentId, VoteType type);

    int countByPostIdAndType(Long postId, VoteType type);

    boolean existsByUserIdAndPostId(Long userId, Long postId);

    Optional<Vote> findByUserIdAndPostId(Long userId, Long postId);

    Optional<Vote> findByUserIdAndCommentId(Long userId, Long commentId);
}
