package com.monsterdam.admin.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class UserLiteMapperTest {

    private UserLiteMapper userLiteMapper;

    @BeforeEach
    public void setUp() {
        userLiteMapper = new UserLiteMapperImpl();
    }
}
