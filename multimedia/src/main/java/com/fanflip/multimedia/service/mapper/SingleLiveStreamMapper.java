package com.fanflip.multimedia.service.mapper;

import com.fanflip.multimedia.domain.SingleAudio;
import com.fanflip.multimedia.domain.SingleLiveStream;
import com.fanflip.multimedia.service.dto.SingleAudioDTO;
import com.fanflip.multimedia.service.dto.SingleLiveStreamDTO;
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
