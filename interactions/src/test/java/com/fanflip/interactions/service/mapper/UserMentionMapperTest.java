package com.fanflip.interactions.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class UserMentionMapperTest {

    private UserMentionMapper userMentionMapper;

    @BeforeEach
    public void setUp() {
        userMentionMapper = new UserMentionMapperImpl();
    }
}
