package com.monsterdam.admin.service.mapper;

import com.monsterdam.admin.domain.SpecialReward;
import com.monsterdam.admin.service.dto.SpecialRewardDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SpecialReward} and its DTO {@link SpecialRewardDTO}.
 */
@Mapper(componentModel = "spring")
public interface SpecialRewardMapper extends EntityMapper<SpecialRewardDTO, SpecialReward> {}
