package com.fanflip.interactions.service.mapper;

import com.fanflip.interactions.domain.LikeMark;
import com.fanflip.interactions.service.dto.LikeMarkDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link LikeMark} and its DTO {@link LikeMarkDTO}.
 */
@Mapper(componentModel = "spring")
public interface LikeMarkMapper extends EntityMapper<LikeMarkDTO, LikeMark> {}
