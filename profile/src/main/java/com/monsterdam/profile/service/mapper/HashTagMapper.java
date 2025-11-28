package com.monsterdam.profile.service.mapper;

import com.monsterdam.profile.domain.HashTag;
import com.monsterdam.profile.service.dto.HashTagDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link HashTag} and its DTO {@link HashTagDTO}.
 */
@Mapper(componentModel = "spring")
public interface HashTagMapper extends EntityMapper<HashTagDTO, HashTag> {}
