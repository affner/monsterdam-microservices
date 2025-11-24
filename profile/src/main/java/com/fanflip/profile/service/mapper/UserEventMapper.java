package com.monsterdam.profile.service.mapper;

import com.monsterdam.profile.domain.UserEvent;
import com.monsterdam.profile.service.dto.UserEventDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserEvent} and its DTO {@link UserEventDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserEventMapper extends EntityMapper<UserEventDTO, UserEvent> {}
