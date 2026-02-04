package com.forumly.web.dto.comment;

import com.forumly.web.dto.post.PostDto;
import com.forumly.web.dto.user.UserDto;
import com.forumly.web.model.Comment;
import com.forumly.web.model.Post;
import com.forumly.web.model.User;

import java.time.LocalDateTime;

public class CommentMapper {
    public static Comment toEntity(CommentRequestDto dto, User user, Post post){
        Comment comment=new Comment();
        comment.setContent(dto.getContent());
        comment.setCreatedAt(LocalDateTime.now());
        comment.setPost(post);
        comment.setUser(user);
        comment.setScore(0);

        return comment;
    }

    public static CommentResponseDto toResponse(Comment comment){
        return new CommentResponseDto(
                comment.getId(),
                comment.getContent(),
                comment.getScore(),
                comment.getCreatedAt(),
                new UserDto(
                        comment.getUser().getId(),
                        comment.getUser().getNickname(),
                        comment.getUser().getEmail()),
                new PostDto(
                        comment.getPost().getId(),
                        comment.getPost().getTitle(),
                        comment.getPost().getContent(),
                        comment.getPost().getScore(),
                        comment.getPost().getCreatedAt()));
    }
}
