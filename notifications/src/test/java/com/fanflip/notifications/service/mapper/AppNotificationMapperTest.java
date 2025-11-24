package com.monsterdam.notifications.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class AppNotificationMapperTest {

    private AppNotificationMapper appNotificationMapper;

    @BeforeEach
    public void setUp() {
        appNotificationMapper = new AppNotificationMapperImpl();
    }
}
