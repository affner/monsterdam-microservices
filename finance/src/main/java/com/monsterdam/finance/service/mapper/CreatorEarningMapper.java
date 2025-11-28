package com.monsterdam.finance.service.mapper;

import com.monsterdam.finance.domain.CreatorEarning;
import com.monsterdam.finance.service.dto.CreatorEarningDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CreatorEarning} and its DTO {@link CreatorEarningDTO}.
 */
@Mapper(componentModel = "spring")
public interface CreatorEarningMapper extends EntityMapper<CreatorEarningDTO, CreatorEarning> {}
