package com.monsterdam.catalogs.service.mapper;

import com.monsterdam.catalogs.domain.GlobalEvent;
import com.monsterdam.catalogs.service.dto.GlobalEventDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link GlobalEvent} and its DTO {@link GlobalEventDTO}.
 */
@Mapper(componentModel = "spring")
public interface GlobalEventMapper extends EntityMapper<GlobalEventDTO, GlobalEvent> {}
