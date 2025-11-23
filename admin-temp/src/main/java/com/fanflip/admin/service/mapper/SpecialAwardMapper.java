package com.fanflip.admin.service.mapper;

import com.fanflip.admin.domain.SpecialAward;
import com.fanflip.admin.service.dto.SpecialAwardDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SpecialAward} and its DTO {@link SpecialAwardDTO}.
 */
@Mapper(componentModel = "spring")
public interface SpecialAwardMapper extends EntityMapper<SpecialAwardDTO, SpecialAward> {}
