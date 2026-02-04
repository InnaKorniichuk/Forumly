package com.forumly.web.service.impl;

import com.forumly.web.dto.comment.CommentMapper;
import com.forumly.web.dto.comment.CommentRequestDto;
import com.forumly.web.dto.comment.CommentResponseDto;
import com.forumly.web.exception.ApplicationException;
import com.forumly.web.model.Comment;
import com.forumly.web.model.Post;
import com.forumly.web.model.User;
import com.forumly.web.repository.CommentRepository;
import com.forumly.web.repository.PostRepository;
import com.forumly.web.repository.UserRepository;
import com.forumly.web.service.CommentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public CommentServiceImpl(CommentRepository commentRepository,
                              PostRepository postRepository,
                              UserRepository userRepository){
        this.commentRepository=commentRepository;
        this.postRepository=postRepository;
        this.userRepository=userRepository;
    }

    @Override
    public Comment create(CommentRequestDto dto, Long authorId, Long postId) {
        if(dto==null)
            throw new IllegalArgumentException("Comment must not be null");

        User author = userRepository.findById(authorId)
                .orElseThrow(() -> ApplicationException.notFound("User not found"));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> ApplicationException.notFound("Post not found"));

        Comment comment = CommentMapper.toEntity(dto, author, post);

        return commentRepository.save(comment);
    }

    @Override
    public CommentResponseDto readById(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() ->  ApplicationException.notFound("Comment with id " + id + " not found"));
        return CommentMapper.toResponse(comment);
    }

    @Override
    public Comment update(Long id, CommentRequestDto dto, Long userId) {
        if (dto == null || dto.getContent() == null || dto.getContent().trim().isEmpty()) {
            throw ApplicationException.badRequest("Comment content must not be null or empty");
        }

        Comment existing = commentRepository.findById(id)
                .orElseThrow(() -> ApplicationException.notFound("Comment with id " + id + " not found"));

        if (!existing.getUser().getId().equals(userId)) {
            throw ApplicationException.unauthorized("Only comment author can update");
        }

        existing.setContent(dto.getContent());
        return commentRepository.save(existing);
    }

    @Override
    public void delete(Long id, Long userId) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() ->  ApplicationException.notFound("Comment with id " + id + " not found"));

        if(!comment.getUser().getId().equals(userId))
            throw ApplicationException.unauthorized("Only comment author or admin can delete");

        commentRepository.delete(comment);
    }

    @Override
    public List<Comment> getByUserId(Long userId) {
        List<Comment> comments = commentRepository.findByUserId(userId);
        return comments != null ? comments : Collections.emptyList();
    }

    @Override
    public List<CommentResponseDto> getByPostId(Long postId) {
        List<CommentResponseDto> comments = commentRepository.findByPostId(postId)
                .stream()
                .map(CommentMapper::toResponse)
                .toList();
        return comments != null ? comments : Collections.emptyList();
    }
}
