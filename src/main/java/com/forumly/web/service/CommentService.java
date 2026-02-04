package com.forumly.web.service;

import com.forumly.web.dto.comment.CommentRequestDto;
import com.forumly.web.dto.comment.CommentResponseDto;
import com.forumly.web.model.Comment;

import java.util.List;

public interface CommentService {
    Comment create(CommentRequestDto dto, Long authorId, Long postId);

    CommentResponseDto readById(Long id);

    Comment update(Long id, CommentRequestDto dto, Long userId);

    void delete(Long id, Long userId);

    List<Comment> getByUserId(Long userId);

    List<CommentResponseDto> getByPostId(Long postId);
}
