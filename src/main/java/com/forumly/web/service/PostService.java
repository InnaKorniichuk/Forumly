package com.forumly.web.service;

import com.forumly.web.dto.post.PostRequestDto;
import com.forumly.web.dto.post.PostResponseDto;
import com.forumly.web.model.Post;

import java.util.List;

public interface PostService {
    Post create(PostRequestDto dto, Long communityId, Long authorId);

    PostResponseDto readById(Long id);

    Post update (Long id, PostRequestDto dto, Long userId);

    void delete (Long id, Long userId);

    List<Post> getAll();

    List<PostResponseDto> getByCommunityId(Long communityId);

    List<PostResponseDto> getByAuthorId(Long userId);
}
