package com.forumly.web.service;

import com.forumly.web.dto.community.CommunityRequestDto;
import com.forumly.web.model.Community;

import java.util.List;

public interface CommunityService {
    Community create(CommunityRequestDto dto, Long ownerId);

    Community readById(Long id);

    void delete(Long id);

    List<Community> getAll();

    Community getByName (String name);

    List<Community> getByOwnerId(Long userId);
}
