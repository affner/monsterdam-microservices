package com.monsterdam.profile.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class UserEventMapperTest {

    private UserEventMapper userEventMapper;

    @BeforeEach
    public void setUp() {
        userEventMapper = new UserEventMapperImpl();
    }
}
