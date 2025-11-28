package com.monsterdam.interactions.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class PostCommentMapperTest {

    private PostCommentMapper postCommentMapper;

    @BeforeEach
    public void setUp() {
        postCommentMapper = new PostCommentMapperImpl();
    }
}
