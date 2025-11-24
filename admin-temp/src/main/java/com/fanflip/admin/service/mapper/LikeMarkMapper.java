package com.monsterdam.admin.service.mapper;

import com.monsterdam.admin.domain.LikeMark;
import com.monsterdam.admin.service.dto.LikeMarkDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link LikeMark} and its DTO {@link LikeMarkDTO}.
 */
@Mapper(componentModel = "spring")
public interface LikeMarkMapper extends EntityMapper<LikeMarkDTO, LikeMark> {}
