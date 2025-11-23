package com.fanflip.admin.service.mapper;

import com.fanflip.admin.domain.Liability;
import com.fanflip.admin.service.dto.LiabilityDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Liability} and its DTO {@link LiabilityDTO}.
 */
@Mapper(componentModel = "spring")
public interface LiabilityMapper extends EntityMapper<LiabilityDTO, Liability> {}
