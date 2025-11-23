package com.fanflip.admin.service.mapper;

import com.fanflip.admin.domain.SingleLiveStream;
import com.fanflip.admin.service.dto.SingleLiveStreamDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SingleLiveStream} and its DTO {@link SingleLiveStreamDTO}.
 */
@Mapper(componentModel = "spring")
public interface SingleLiveStreamMapper extends EntityMapper<SingleLiveStreamDTO, SingleLiveStream> {}
