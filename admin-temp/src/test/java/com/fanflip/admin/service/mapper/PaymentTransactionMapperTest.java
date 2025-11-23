package com.fanflip.admin.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class PaymentTransactionMapperTest {

    private PaymentTransactionMapper paymentTransactionMapper;

    @BeforeEach
    public void setUp() {
        paymentTransactionMapper = new PaymentTransactionMapperImpl();
    }
}
