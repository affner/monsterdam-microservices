package com.monsterdam.admin.domain;

import static com.monsterdam.admin.domain.ChatRoomTestSamples.*;
import static com.monsterdam.admin.domain.DirectMessageTestSamples.*;
import static com.monsterdam.admin.domain.UserProfileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.admin.web.rest.TestUtil;
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

    @Test
    void userTest() throws Exception {
        ChatRoom chatRoom = getChatRoomRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        chatRoom.setUser(userProfileBack);
        assertThat(chatRoom.getUser()).isEqualTo(userProfileBack);

        chatRoom.user(null);
        assertThat(chatRoom.getUser()).isNull();
    }
}
