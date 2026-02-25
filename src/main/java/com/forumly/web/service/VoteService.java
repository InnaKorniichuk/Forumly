package com.forumly.web.service;

import com.forumly.web.model.VoteType;

public interface VoteService {
    void vote(Long userId, Long commentId, VoteType newType);

    void votePost(Long userId, Long postId, VoteType newType);
}
