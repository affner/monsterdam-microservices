package com.fanflip.interactions.domain;

import static com.fanflip.interactions.domain.ChatRoomTestSamples.*;
import static com.fanflip.interactions.domain.DirectMessageTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.fanflip.interactions.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ChatRoomTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChatRoom.class);
        ChatRoom chatRoom1 = getChatRoomSample1();
        ChatRoom chatRoom2 = new ChatRoom();
        assertThat(chatRoom1).isNotEqualTo(chatRoom2);

        chatRoom2.setId(chatRoom1.getId());
        assertThat(chatRoom1).isEqualTo(chatRoom2);

        chatRoom2 = getChatRoomSample2();
        assertThat(chatRoom1).isNotEqualTo(chatRoom2);
    }

    @Test
    void sentMessagesTest() throws Exception {
        ChatRoom chatRoom = getChatRoomRandomSampleGenerator();
        DirectMessage directMessageBack = getDirectMessageRandomSampleGenerator();

        chatRoom.addSentMessages(directMessageBack);
        assertThat(chatRoom.getSentMessages()).containsOnly(directMessageBack);

        chatRoom.removeSentMessages(directMessageBack);
        assertThat(chatRoom.getSentMessages()).doesNotContain(directMessageBack);

        chatRoom.sentMessages(new HashSet<>(Set.of(directMessageBack)));
        assertThat(chatRoom.getSentMessages()).containsOnly(directMessageBack);

        chatRoom.setSentMessages(new HashSet<>());
        assertThat(chatRoom.getSentMessages()).doesNotContain(directMessageBack);
    }
}
