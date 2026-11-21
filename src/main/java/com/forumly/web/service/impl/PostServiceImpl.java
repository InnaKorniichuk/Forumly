package com.forumly.web.service.impl;

import com.forumly.web.dto.post.PostMapper;
import com.forumly.web.dto.post.PostRequestDto;
import com.forumly.web.dto.post.PostResponseDto;
import com.forumly.web.exception.ApplicationException;
import com.forumly.web.model.Community;
import com.forumly.web.model.Post;
import com.forumly.web.model.User;
import com.forumly.web.repository.CommunityRepository;
import com.forumly.web.repository.PostRepository;
import com.forumly.web.repository.UserRepository;
import com.forumly.web.service.PostService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final CommunityRepository communityRepository;
    private final UserRepository userRepository;

    public PostServiceImpl(PostRepository postRepository, CommunityRepository communityRepository, UserRepository userRepository){
        this.postRepository=postRepository;
        this.communityRepository=communityRepository;
        this.userRepository=userRepository;
    }
    @Override
    public Post create(PostRequestDto dto, Long communityId, Long authorId) {
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> ApplicationException.notFound("Community not found"));
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> ApplicationException.notFound("User not found"));

        Post post = PostMapper.toEntity(dto, author, community);

        return postRepository.save(post);
    }

    @Override
    public PostResponseDto readById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> ApplicationException.notFound("Post with id " + id + " not found"));
        return PostMapper.toResponse(post);
    }

    @Override
    public Post update(Long id, PostRequestDto dto, Long userId) {
        Post existing = postRepository.findById(id)
                .orElseThrow(() -> ApplicationException.notFound("Post not found"));

        if (!existing.getAuthor().getId().equals(userId))
            throw ApplicationException.unauthorized("You have no access to update the post");

        if (dto.getTitle() != null && !dto.getTitle().equals(existing.getTitle())) {
            postRepository.findByTitle(dto.getTitle())
                    .ifPresent(p -> {
                        if (!p.getId().equals(id)) {
                            throw ApplicationException.conflict("Title already exists");
                        }
                    });
            existing.setTitle(dto.getTitle());
        }

        if (dto.getContent() != null) {
            existing.setContent(dto.getContent());
        }

        return postRepository.save(existing);
    }

    @Override
    public void delete(Long id, Long userId) {
        Post post = postRepository.findById(id)
                .orElseThrow(() ->ApplicationException.notFound("Post not found"));

        if (!post.getAuthor().getId().equals(userId)) {
            throw ApplicationException.unauthorized("Only post author or admin can delete");
        }

        postRepository.delete(post);
    }

    @Override
    public List<Post> getAll() {
        return postRepository.findAll();
    }

    @Override
    public List<PostResponseDto> getByCommunityId(Long communityId) {
        return postRepository.findByCommunityId(communityId)
                .stream()
                .map(PostMapper::toResponse)
                .toList();
    }

    @Override
    public List<PostResponseDto> getByAuthorId(Long userId) {
        return postRepository.findByAuthorId(userId)
                .stream()
                .map(PostMapper::toResponse)
                .toList();
    }
}
