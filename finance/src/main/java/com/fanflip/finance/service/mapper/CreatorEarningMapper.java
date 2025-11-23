package com.fanflip.finance.service.mapper;

import com.fanflip.finance.domain.CreatorEarning;
import com.fanflip.finance.service.dto.CreatorEarningDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CreatorEarning} and its DTO {@link CreatorEarningDTO}.
 */
@Mapper(componentModel = "spring")
public interface CreatorEarningMapper extends EntityMapper<CreatorEarningDTO, CreatorEarning> {}
