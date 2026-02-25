package com.forumly.web.dto.post;

import com.forumly.web.dto.comment.CommentMapper;
import com.forumly.web.dto.comment.CommentResponseDto;
import com.forumly.web.dto.community.CommunityDto;
import com.forumly.web.dto.user.UserDto;
import com.forumly.web.model.Community;
import com.forumly.web.model.Post;
import com.forumly.web.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class PostMapper {
    public static Post toEntity(PostRequestDto  dto, User author, Community community){
        Post post=new Post();
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setCreatedAt(LocalDateTime.now());
        post.setAuthor(author);
        post.setCommunity(community);

        return post;
    }

    public static PostResponseDto toResponse(Post post) {
        List<CommentResponseDto> commentDtos = null;

        if (post.getComments() != null) {
            commentDtos = post.getComments().stream()
                    .map(CommentMapper::toResponse)
                    .collect(Collectors.toList());
        }

        return new PostResponseDto(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getCreatedAt(),
                post.getScore(),
                new UserDto(
                        post.getAuthor().getId(),
                        post.getAuthor().getNickname(),
                        post.getAuthor().getEmail()
                ),
                new CommunityDto(
                        post.getCommunity().getId(),
                        post.getCommunity().getName(),
                        post.getCommunity().getCreatedAt().toLocalDate()
                ),
                commentDtos
        );
    }
}
