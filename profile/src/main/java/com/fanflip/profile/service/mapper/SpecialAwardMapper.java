package com.fanflip.profile.service.mapper;

import com.fanflip.profile.domain.SpecialAward;
import com.fanflip.profile.service.dto.SpecialAwardDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SpecialAward} and its DTO {@link SpecialAwardDTO}.
 */
@Mapper(componentModel = "spring")
public interface SpecialAwardMapper extends EntityMapper<SpecialAwardDTO, SpecialAward> {}
