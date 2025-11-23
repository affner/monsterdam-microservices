package com.fanflip.admin.service.mapper;

import com.fanflip.admin.domain.SpecialReward;
import com.fanflip.admin.service.dto.SpecialRewardDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SpecialReward} and its DTO {@link SpecialRewardDTO}.
 */
@Mapper(componentModel = "spring")
public interface SpecialRewardMapper extends EntityMapper<SpecialRewardDTO, SpecialReward> {}
