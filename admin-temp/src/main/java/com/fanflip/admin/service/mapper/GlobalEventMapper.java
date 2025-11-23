package com.fanflip.admin.service.mapper;

import com.fanflip.admin.domain.GlobalEvent;
import com.fanflip.admin.service.dto.GlobalEventDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link GlobalEvent} and its DTO {@link GlobalEventDTO}.
 */
@Mapper(componentModel = "spring")
public interface GlobalEventMapper extends EntityMapper<GlobalEventDTO, GlobalEvent> {}
