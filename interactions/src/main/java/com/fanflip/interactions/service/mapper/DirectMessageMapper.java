package com.fanflip.interactions.service.mapper;

import com.fanflip.interactions.domain.DirectMessage;
import com.fanflip.interactions.service.dto.DirectMessageDTO;
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
