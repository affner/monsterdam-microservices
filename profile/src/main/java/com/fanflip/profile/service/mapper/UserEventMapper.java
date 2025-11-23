package com.fanflip.profile.service.mapper;

import com.fanflip.profile.domain.UserEvent;
import com.fanflip.profile.service.dto.UserEventDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserEvent} and its DTO {@link UserEventDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserEventMapper extends EntityMapper<UserEventDTO, UserEvent> {}
