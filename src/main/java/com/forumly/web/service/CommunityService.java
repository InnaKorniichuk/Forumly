package com.forumly.web.service;

import com.forumly.web.dto.community.CommunityRequestDto;
import com.forumly.web.dto.community.CommunityResponseDto;
import com.forumly.web.model.Community;

import java.util.List;

public interface CommunityService {
    Community create(CommunityRequestDto dto, Long ownerId);

    CommunityResponseDto readById(Long id);

    void delete(Long id, Long userId);

    List<CommunityResponseDto> getAll();

    Community getByName (String name);

    List<CommunityResponseDto> getByOwnerId(Long userId);
}
