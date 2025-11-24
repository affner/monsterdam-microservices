package com.monsterdam.profile.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class FeedbackMapperTest {

    private FeedbackMapper feedbackMapper;

    @BeforeEach
    public void setUp() {
        feedbackMapper = new FeedbackMapperImpl();
    }
}
