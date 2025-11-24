package com.monsterdam.interactions.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class PostPollMapperTest {

    private PostPollMapper postPollMapper;

    @BeforeEach
    public void setUp() {
        postPollMapper = new PostPollMapperImpl();
    }
}
