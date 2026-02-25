package com.forumly.web.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column
    private String title;

    @NotNull
    @Column
    private String content;

    @Column(name = "created_at",nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private int score=0;

    @OneToMany(mappedBy = "post")
    private List<Vote> votes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "community_id", nullable = false)
    private Community community;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("createdAt DESC") // Сортування за датою (новіші перші)
    private List<Comment> comments = new ArrayList<>();
}
