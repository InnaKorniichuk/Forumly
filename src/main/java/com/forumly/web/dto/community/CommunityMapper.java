package com.forumly.web.dto.community;

import com.forumly.web.dto.post.PostDto;
import com.forumly.web.dto.user.UserDto;
import com.forumly.web.model.Community;
import com.forumly.web.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class CommunityMapper {
    public static Community toEntity(CommunityRequestDto dto, User owner){
        Community community= new Community();
        community.setName(dto.getName());
        community.setPosts(new ArrayList<>());
        community.setCreatedAt(LocalDateTime.now());
        community.setOwner(owner);

        return community;
    }

    public static CommunityResponseDto toResponse(Community community){
        return new CommunityResponseDto(
                community.getId(),
                community.getName(),
                community.getCreatedAt(),
                new UserDto(
                        community.getOwner().getId(),
                        community.getOwner().getNickname(),
                        community.getOwner().getEmail()),
                community.getPosts().stream()
                        .map(post->new PostDto(post.getId(),
                                post.getTitle(),
                                post.getScore(),
                                post.getCreatedAt()))
                        .toList()
        );
    }
}
