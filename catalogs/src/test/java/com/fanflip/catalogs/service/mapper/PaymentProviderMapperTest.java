package com.monsterdam.catalogs.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class PaymentProviderMapperTest {

    private PaymentProviderMapper paymentProviderMapper;

    @BeforeEach
    public void setUp() {
        paymentProviderMapper = new PaymentProviderMapperImpl();
    }
}
