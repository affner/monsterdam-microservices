package com.monsterdam.admin.service.mapper;

import com.monsterdam.admin.domain.ModerationAction;
import com.monsterdam.admin.service.dto.ModerationActionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ModerationAction} and its DTO {@link ModerationActionDTO}.
 */
@Mapper(componentModel = "spring")
public interface ModerationActionMapper extends EntityMapper<ModerationActionDTO, ModerationAction> {}
