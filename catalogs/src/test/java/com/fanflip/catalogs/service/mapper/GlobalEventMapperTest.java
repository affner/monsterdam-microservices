package com.fanflip.catalogs.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class GlobalEventMapperTest {

    private GlobalEventMapper globalEventMapper;

    @BeforeEach
    public void setUp() {
        globalEventMapper = new GlobalEventMapperImpl();
    }
}
