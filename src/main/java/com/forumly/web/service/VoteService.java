package com.forumly.web.service;

import com.forumly.web.model.VoteType;

public interface VoteService {
    void addVoteToComment(Long userId, Long commentId, VoteType type);

    void addVoteToPost(Long userId, Long postId, VoteType type);

    void removeVoteFromPost(Long userId, Long postId);

    void removeVoteFromComment(Long userId, Long commentId);
}
