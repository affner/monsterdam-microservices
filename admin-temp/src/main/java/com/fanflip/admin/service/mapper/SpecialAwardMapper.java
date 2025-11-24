package com.monsterdam.admin.service.mapper;

import com.monsterdam.admin.domain.SpecialAward;
import com.monsterdam.admin.service.dto.SpecialAwardDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SpecialAward} and its DTO {@link SpecialAwardDTO}.
 */
@Mapper(componentModel = "spring")
public interface SpecialAwardMapper extends EntityMapper<SpecialAwardDTO, SpecialAward> {}
