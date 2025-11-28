package com.monsterdam.app.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class UserUIPreferencesMapperTest {

    private UserUIPreferencesMapper userUIPreferencesMapper;

    @BeforeEach
    public void setUp() {
        userUIPreferencesMapper = new UserUIPreferencesMapperImpl();
    }
}
