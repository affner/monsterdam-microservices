package com.monsterdam.interactions.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class ChatRoomMapperTest {

    private ChatRoomMapper chatRoomMapper;

    @BeforeEach
    public void setUp() {
        chatRoomMapper = new ChatRoomMapperImpl();
    }
}
