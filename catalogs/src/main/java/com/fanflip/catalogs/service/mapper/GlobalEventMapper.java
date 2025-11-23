package com.fanflip.catalogs.service.mapper;

import com.fanflip.catalogs.domain.GlobalEvent;
import com.fanflip.catalogs.service.dto.GlobalEventDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link GlobalEvent} and its DTO {@link GlobalEventDTO}.
 */
@Mapper(componentModel = "spring")
public interface GlobalEventMapper extends EntityMapper<GlobalEventDTO, GlobalEvent> {}
