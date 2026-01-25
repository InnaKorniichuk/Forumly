package com.forumly.web.repository;

import com.forumly.web.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {
    @Query(value = "SELECT * FROM posts WHERE community_id = :communityId", nativeQuery = true)
    List<Post> findByCommunityId(Long communityId);

    @Query(value = "SELECT * FROM posts WHERE author_id = :userId", nativeQuery = true)
    List<Post> findByAuthorId(Long userId);

    Optional<Post> findByTitle(String title);
}
