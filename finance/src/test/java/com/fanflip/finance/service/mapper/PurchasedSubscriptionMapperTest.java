package com.monsterdam.finance.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class PurchasedSubscriptionMapperTest {

    private PurchasedSubscriptionMapper purchasedSubscriptionMapper;

    @BeforeEach
    public void setUp() {
        purchasedSubscriptionMapper = new PurchasedSubscriptionMapperImpl();
    }
}
