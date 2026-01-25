package com.forumly.web.service;

import com.forumly.web.dto.post.PostRequestDto;
import com.forumly.web.dto.post.PostUpdateDto;
import com.forumly.web.model.Post;

import java.util.List;

public interface PostService {
    Post create(PostRequestDto dto, Long communityId, Long authorId);

    Post readById(Long id);

    Post update (Long id, PostUpdateDto dto, Long userId);

    void delete (Long id, Long userId);

    List<Post> getAll();

    List<Post> getByCommunityId(Long communityId);

    List<Post> getByAuthorId(Long userId);
}
