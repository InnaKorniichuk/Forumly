package com.forumly.web.repository;

import com.forumly.web.model.Community;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommunityRepository extends JpaRepository<Community,Long> {
    Optional<Community> findByName (String name);

    boolean existsByName (String name);

    @Query(value = "SELECT * FROM communities WHERE owner_id = :userId", nativeQuery = true)
    List<Community> findByOwnerId(Long userId);
}
