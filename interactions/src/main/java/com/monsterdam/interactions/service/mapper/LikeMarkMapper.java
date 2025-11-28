package com.monsterdam.interactions.service.mapper;

import com.monsterdam.interactions.domain.LikeMark;
import com.monsterdam.interactions.service.dto.LikeMarkDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link LikeMark} and its DTO {@link LikeMarkDTO}.
 */
@Mapper(componentModel = "spring")
public interface LikeMarkMapper extends EntityMapper<LikeMarkDTO, LikeMark> {}
