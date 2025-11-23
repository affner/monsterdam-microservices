package com.fanflip.admin.service.mapper;

import com.fanflip.admin.domain.ModerationAction;
import com.fanflip.admin.service.dto.ModerationActionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ModerationAction} and its DTO {@link ModerationActionDTO}.
 */
@Mapper(componentModel = "spring")
public interface ModerationActionMapper extends EntityMapper<ModerationActionDTO, ModerationAction> {}
