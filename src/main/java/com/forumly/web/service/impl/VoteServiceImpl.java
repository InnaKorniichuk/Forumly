package com.forumly.web.service.impl;

import com.forumly.web.exception.ApplicationException;
import com.forumly.web.model.*;
import com.forumly.web.repository.CommentRepository;
import com.forumly.web.repository.PostRepository;
import com.forumly.web.repository.UserRepository;
import com.forumly.web.repository.VoteRepository;
import com.forumly.web.service.VoteService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class VoteServiceImpl implements VoteService {
    private final VoteRepository voteRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public VoteServiceImpl(VoteRepository voteRepository,
                           UserRepository userRepository,
                           PostRepository postRepository,
                           CommentRepository commentRepository){
        this.voteRepository=voteRepository;
        this.userRepository=userRepository;
        this.postRepository=postRepository;
        this.commentRepository=commentRepository;
    }

    @Override
    @Transactional
    public void vote(Long userId, Long commentId, VoteType newType) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> ApplicationException.notFound("Comment not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> ApplicationException.notFound("User not found"));

        Vote existingVote = voteRepository.findByUserIdAndCommentId(userId, commentId)
                .orElse(null);

        if (existingVote == null) {
            Vote vote = new Vote();
            vote.setUser(user);
            vote.setComment(comment);
            vote.setType(newType);
            voteRepository.save(vote);

            comment.setScore(comment.getScore() + newType.getValue());
            commentRepository.save(comment);
            return;
        }

        if (existingVote.getType() == newType) {
            comment.setScore(comment.getScore() - existingVote.getType().getValue());
            voteRepository.delete(existingVote);
            commentRepository.save(comment);
            return;
        }

        int delta = newType.getValue() - existingVote.getType().getValue();
        existingVote.setType(newType);
        voteRepository.save(existingVote);

        comment.setScore(comment.getScore() + delta);
        commentRepository.save(comment);
    }

    @Override
    public Long votePost(Long userId, Long postId, VoteType newType) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> ApplicationException.notFound("Post not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> ApplicationException.notFound("User not found"));

        Vote existingVote = voteRepository.findByUserIdAndPostId(userId, postId)
                .orElse(null);

        if (existingVote == null) {
            Vote vote = new Vote();
            vote.setUser(user);
            vote.setPost(post);
            vote.setType(newType);
            voteRepository.save(vote);
            post.setScore(post.getScore() + newType.getValue());
        } else if (existingVote.getType() == newType) {
            post.setScore(post.getScore() - existingVote.getType().getValue());
            voteRepository.delete(existingVote);
        } else {
            int delta = newType.getValue() - existingVote.getType().getValue();
            existingVote.setType(newType);
            voteRepository.save(existingVote);
            post.setScore(post.getScore() + delta);
        }

        postRepository.save(post);
        return post.getCommunity().getId();
    }
}
