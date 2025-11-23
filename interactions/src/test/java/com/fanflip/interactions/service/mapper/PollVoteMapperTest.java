package com.fanflip.interactions.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class PollVoteMapperTest {

    private PollVoteMapper pollVoteMapper;

    @BeforeEach
    public void setUp() {
        pollVoteMapper = new PollVoteMapperImpl();
    }
}
