package com.monsterdam.admin.service.mapper;

import com.monsterdam.admin.domain.GlobalEvent;
import com.monsterdam.admin.service.dto.GlobalEventDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link GlobalEvent} and its DTO {@link GlobalEventDTO}.
 */
@Mapper(componentModel = "spring")
public interface GlobalEventMapper extends EntityMapper<GlobalEventDTO, GlobalEvent> {}
