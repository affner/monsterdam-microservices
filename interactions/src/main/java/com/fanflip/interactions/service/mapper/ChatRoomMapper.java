package com.fanflip.interactions.service.mapper;

import com.fanflip.interactions.domain.ChatRoom;
import com.fanflip.interactions.domain.DirectMessage;
import com.fanflip.interactions.service.dto.ChatRoomDTO;
import com.fanflip.interactions.service.dto.DirectMessageDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ChatRoom} and its DTO {@link ChatRoomDTO}.
 */
@Mapper(componentModel = "spring")
public interface ChatRoomMapper extends EntityMapper<ChatRoomDTO, ChatRoom> {
    @Mapping(target = "sentMessages", source = "sentMessages", qualifiedByName = "directMessageMessageContentSet")
    ChatRoomDTO toDto(ChatRoom s);

    @Mapping(target = "removeSentMessages", ignore = true)
    ChatRoom toEntity(ChatRoomDTO chatRoomDTO);

    @Named("directMessageMessageContent")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "messageContent", source = "messageContent")
    DirectMessageDTO toDtoDirectMessageMessageContent(DirectMessage directMessage);

    @Named("directMessageMessageContentSet")
    default Set<DirectMessageDTO> toDtoDirectMessageMessageContentSet(Set<DirectMessage> directMessage) {
        return directMessage.stream().map(this::toDtoDirectMessageMessageContent).collect(Collectors.toSet());
    }
}
