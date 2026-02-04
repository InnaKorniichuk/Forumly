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
    public void removeVoteFromPost(Long userId, Long postId) {
        Vote vote = voteRepository.findByUserIdAndPostId(userId, postId)
                .orElseThrow(() -> ApplicationException.notFound("Vote not found"));

        if (!vote.getUser().getId().equals(userId))
            throw ApplicationException.unauthorized("You can only remove your own vote");

        voteRepository.delete(vote);
    }

    @Override
    public void removeVoteFromComment(Long userId, Long commentId) {
        Vote vote = voteRepository.findByUserIdAndCommentId(userId, commentId)
                .orElseThrow(() -> ApplicationException.notFound("Vote not found"));

        if(!vote.getUser().getId().equals(userId))
            throw ApplicationException.unauthorized("You can only remove your own vote");

        voteRepository.delete(vote);
    }

    @Override
    public void addVoteToComment(Long userId, Long commentId, VoteType type) {
        if(voteRepository.existsByUserIdAndCommentId(userId,commentId))
            throw  ApplicationException.conflict("You have already voted");

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> ApplicationException.notFound("Comment not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> ApplicationException.notFound("User not found"));

        Vote vote = new Vote();
        vote.setComment(comment);
        vote.setUser(user);
        vote.setType(type);

        voteRepository.save(vote);
    }

    @Override
    public void addVoteToPost(Long userId, Long postId, VoteType type) {
        if(voteRepository.existsByUserIdAndPostId(userId,postId))
            throw  ApplicationException.conflict("You have already voted");

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> ApplicationException.notFound("Post not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> ApplicationException.notFound("User not found"));

        Vote vote = new Vote();
        vote.setPost(post);
        vote.setUser(user);
        vote.setType(type);

        voteRepository.save(vote);

        int newScore  = post.getScore();
        post.setScore(newScore);
        postRepository.save(post);
    }
}
