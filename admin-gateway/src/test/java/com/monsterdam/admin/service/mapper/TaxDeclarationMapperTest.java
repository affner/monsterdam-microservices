package com.monsterdam.admin.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class TaxDeclarationMapperTest {

    private TaxDeclarationMapper taxDeclarationMapper;

    @BeforeEach
    public void setUp() {
        taxDeclarationMapper = new TaxDeclarationMapperImpl();
    }
}
