package com.fanflip.admin.service.mapper;

import com.fanflip.admin.domain.LikeMark;
import com.fanflip.admin.service.dto.LikeMarkDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link LikeMark} and its DTO {@link LikeMarkDTO}.
 */
@Mapper(componentModel = "spring")
public interface LikeMarkMapper extends EntityMapper<LikeMarkDTO, LikeMark> {}
