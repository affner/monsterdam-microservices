package com.monsterdam.interactions.repository;

import com.monsterdam.interactions.domain.ChatRoom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class ChatRoomRepositoryWithBagRelationshipsImpl implements ChatRoomRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<ChatRoom> fetchBagRelationships(Optional<ChatRoom> chatRoom) {
        return chatRoom.map(this::fetchSentMessages);
    }

    @Override
    public Page<ChatRoom> fetchBagRelationships(Page<ChatRoom> chatRooms) {
        return new PageImpl<>(fetchBagRelationships(chatRooms.getContent()), chatRooms.getPageable(), chatRooms.getTotalElements());
    }

    @Override
    public List<ChatRoom> fetchBagRelationships(List<ChatRoom> chatRooms) {
        return Optional.of(chatRooms).map(this::fetchSentMessages).orElse(Collections.emptyList());
    }

    ChatRoom fetchSentMessages(ChatRoom result) {
        return entityManager
            .createQuery(
                "select chatRoom from ChatRoom chatRoom left join fetch chatRoom.sentMessages where chatRoom.id = :id",
                ChatRoom.class
            )
            .setParameter("id", result.getId())
            .getSingleResult();
    }

    List<ChatRoom> fetchSentMessages(List<ChatRoom> chatRooms) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, chatRooms.size()).forEach(index -> order.put(chatRooms.get(index).getId(), index));
        List<ChatRoom> result = entityManager
            .createQuery(
                "select chatRoom from ChatRoom chatRoom left join fetch chatRoom.sentMessages where chatRoom in :chatRooms",
                ChatRoom.class
            )
            .setParameter("chatRooms", chatRooms)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
