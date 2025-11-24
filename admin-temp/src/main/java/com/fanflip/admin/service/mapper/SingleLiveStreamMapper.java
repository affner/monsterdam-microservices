package com.monsterdam.admin.service.mapper;

import com.monsterdam.admin.domain.SingleLiveStream;
import com.monsterdam.admin.service.dto.SingleLiveStreamDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SingleLiveStream} and its DTO {@link SingleLiveStreamDTO}.
 */
@Mapper(componentModel = "spring")
public interface SingleLiveStreamMapper extends EntityMapper<SingleLiveStreamDTO, SingleLiveStream> {}
