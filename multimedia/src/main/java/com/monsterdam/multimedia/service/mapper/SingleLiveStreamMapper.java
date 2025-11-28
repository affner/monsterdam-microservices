package com.monsterdam.multimedia.service.mapper;

import com.monsterdam.multimedia.domain.SingleAudio;
import com.monsterdam.multimedia.domain.SingleLiveStream;
import com.monsterdam.multimedia.service.dto.SingleAudioDTO;
import com.monsterdam.multimedia.service.dto.SingleLiveStreamDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SingleLiveStream} and its DTO {@link SingleLiveStreamDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
@DecoratedWith(SingleLiveStreamMapperDecorator.class)
public interface SingleLiveStreamMapper extends EntityMapper<SingleLiveStreamDTO, SingleLiveStream> {
    @Mapping(target = "thumbnail", ignore = true)
    @Mapping(target = "content", ignore = true)
    SingleAudioDTO toDto(SingleAudio s);
}
