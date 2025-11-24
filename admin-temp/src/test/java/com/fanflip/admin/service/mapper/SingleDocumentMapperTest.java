package com.monsterdam.admin.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class SingleDocumentMapperTest {

    private SingleDocumentMapper singleDocumentMapper;

    @BeforeEach
    public void setUp() {
        singleDocumentMapper = new SingleDocumentMapperImpl();
    }
}
