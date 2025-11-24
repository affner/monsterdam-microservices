package com.monsterdam.admin.service.mapper;

import com.monsterdam.admin.domain.Liability;
import com.monsterdam.admin.service.dto.LiabilityDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Liability} and its DTO {@link LiabilityDTO}.
 */
@Mapper(componentModel = "spring")
public interface LiabilityMapper extends EntityMapper<LiabilityDTO, Liability> {}
