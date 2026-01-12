package com.forumly.web.repository;

import com.forumly.web.model.Community;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommunityRepository extends JpaRepository<Community,Long> {
    Community findByName (String name);

    @Query(value = "SELECT * FROM communities WHERE owner_id = :userId", nativeQuery = true)
    List<Community> findByOwnerId(Long userId);
}
