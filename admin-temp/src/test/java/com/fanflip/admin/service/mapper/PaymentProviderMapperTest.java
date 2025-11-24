package com.monsterdam.admin.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class PaymentProviderMapperTest {

    private PaymentProviderMapper paymentProviderMapper;

    @BeforeEach
    public void setUp() {
        paymentProviderMapper = new PaymentProviderMapperImpl();
    }
}
