package com.fanflip.finance.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class WalletTransactionMapperTest {

    private WalletTransactionMapper walletTransactionMapper;

    @BeforeEach
    public void setUp() {
        walletTransactionMapper = new WalletTransactionMapperImpl();
    }
}
