package com.monsterdam.admin.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class SingleLiveStreamMapperTest {

    private SingleLiveStreamMapper singleLiveStreamMapper;

    @BeforeEach
    public void setUp() {
        singleLiveStreamMapper = new SingleLiveStreamMapperImpl();
    }
}
