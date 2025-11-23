package com.fanflip.admin.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class UserEventMapperTest {

    private UserEventMapper userEventMapper;

    @BeforeEach
    public void setUp() {
        userEventMapper = new UserEventMapperImpl();
    }
}
