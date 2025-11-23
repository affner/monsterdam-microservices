package com.fanflip.admin.service.mapper;

import com.fanflip.admin.domain.HashTag;
import com.fanflip.admin.service.dto.HashTagDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link HashTag} and its DTO {@link HashTagDTO}.
 */
@Mapper(componentModel = "spring")
public interface HashTagMapper extends EntityMapper<HashTagDTO, HashTag> {}
