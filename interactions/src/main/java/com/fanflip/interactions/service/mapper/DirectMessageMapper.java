package com.monsterdam.interactions.service.mapper;

import com.monsterdam.interactions.domain.DirectMessage;
import com.monsterdam.interactions.service.dto.DirectMessageDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DirectMessage} and its DTO {@link DirectMessageDTO}.
 */
@Mapper(componentModel = "spring")
public interface DirectMessageMapper extends EntityMapper<DirectMessageDTO, DirectMessage> {
    @Mapping(target = "responseTo", source = "responseTo", qualifiedByName = "directMessageMessageContent")
    DirectMessageDTO toDto(DirectMessage s);

    @Named("directMessageMessageContent")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "messageContent", source = "messageContent")
    DirectMessageDTO toDtoDirectMessageMessageContent(DirectMessage directMessage);
}
