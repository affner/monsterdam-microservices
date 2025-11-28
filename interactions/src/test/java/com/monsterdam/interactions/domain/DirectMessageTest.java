package com.monsterdam.interactions.domain;

import static com.monsterdam.interactions.domain.ChatRoomTestSamples.*;
import static com.monsterdam.interactions.domain.DirectMessageTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.interactions.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class DirectMessageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DirectMessage.class);
        DirectMessage directMessage1 = getDirectMessageSample1();
        DirectMessage directMessage2 = new DirectMessage();
        assertThat(directMessage1).isNotEqualTo(directMessage2);

        directMessage2.setId(directMessage1.getId());
        assertThat(directMessage1).isEqualTo(directMessage2);

        directMessage2 = getDirectMessageSample2();
        assertThat(directMessage1).isNotEqualTo(directMessage2);
    }

    @Test
    void responsesTest() throws Exception {
        DirectMessage directMessage = getDirectMessageRandomSampleGenerator();
        DirectMessage directMessageBack = getDirectMessageRandomSampleGenerator();

        directMessage.addResponses(directMessageBack);
        assertThat(directMessage.getResponses()).containsOnly(directMessageBack);
        assertThat(directMessageBack.getResponseTo()).isEqualTo(directMessage);

        directMessage.removeResponses(directMessageBack);
        assertThat(directMessage.getResponses()).doesNotContain(directMessageBack);
        assertThat(directMessageBack.getResponseTo()).isNull();

        directMessage.responses(new HashSet<>(Set.of(directMessageBack)));
        assertThat(directMessage.getResponses()).containsOnly(directMessageBack);
        assertThat(directMessageBack.getResponseTo()).isEqualTo(directMessage);

        directMessage.setResponses(new HashSet<>());
        assertThat(directMessage.getResponses()).doesNotContain(directMessageBack);
        assertThat(directMessageBack.getResponseTo()).isNull();
    }

    @Test
    void responseToTest() throws Exception {
        DirectMessage directMessage = getDirectMessageRandomSampleGenerator();
        DirectMessage directMessageBack = getDirectMessageRandomSampleGenerator();

        directMessage.setResponseTo(directMessageBack);
        assertThat(directMessage.getResponseTo()).isEqualTo(directMessageBack);

        directMessage.responseTo(null);
        assertThat(directMessage.getResponseTo()).isNull();
    }

    @Test
    void chatRoomsTest() throws Exception {
        DirectMessage directMessage = getDirectMessageRandomSampleGenerator();
        ChatRoom chatRoomBack = getChatRoomRandomSampleGenerator();

        directMessage.addChatRooms(chatRoomBack);
        assertThat(directMessage.getChatRooms()).containsOnly(chatRoomBack);
        assertThat(chatRoomBack.getSentMessages()).containsOnly(directMessage);

        directMessage.removeChatRooms(chatRoomBack);
        assertThat(directMessage.getChatRooms()).doesNotContain(chatRoomBack);
        assertThat(chatRoomBack.getSentMessages()).doesNotContain(directMessage);

        directMessage.chatRooms(new HashSet<>(Set.of(chatRoomBack)));
        assertThat(directMessage.getChatRooms()).containsOnly(chatRoomBack);
        assertThat(chatRoomBack.getSentMessages()).containsOnly(directMessage);

        directMessage.setChatRooms(new HashSet<>());
        assertThat(directMessage.getChatRooms()).doesNotContain(chatRoomBack);
        assertThat(chatRoomBack.getSentMessages()).doesNotContain(directMessage);
    }
}
