package com.monsterdam.admin.service.mapper;

import com.monsterdam.admin.domain.HashTag;
import com.monsterdam.admin.service.dto.HashTagDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link HashTag} and its DTO {@link HashTagDTO}.
 */
@Mapper(componentModel = "spring")
public interface HashTagMapper extends EntityMapper<HashTagDTO, HashTag> {}
