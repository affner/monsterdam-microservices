package com.monsterdam.profile.service.mapper;

import com.monsterdam.profile.domain.SpecialAward;
import com.monsterdam.profile.service.dto.SpecialAwardDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SpecialAward} and its DTO {@link SpecialAwardDTO}.
 */
@Mapper(componentModel = "spring")
public interface SpecialAwardMapper extends EntityMapper<SpecialAwardDTO, SpecialAward> {}
