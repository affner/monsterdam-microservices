package com.monsterdam.catalogs.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class PaymentMethodMapperTest {

    private PaymentMethodMapper paymentMethodMapper;

    @BeforeEach
    public void setUp() {
        paymentMethodMapper = new PaymentMethodMapperImpl();
    }
}
