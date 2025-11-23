package com.fanflip.profile.service.mapper;

import com.fanflip.profile.domain.HashTag;
import com.fanflip.profile.service.dto.HashTagDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link HashTag} and its DTO {@link HashTagDTO}.
 */
@Mapper(componentModel = "spring")
public interface HashTagMapper extends EntityMapper<HashTagDTO, HashTag> {}
