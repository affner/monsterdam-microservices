package com.fanflip.multimedia.service.mapper;

import com.fanflip.multimedia.domain.SingleAudio;
import com.fanflip.multimedia.service.dto.SingleAudioDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SingleAudio} and its DTO {@link SingleAudioDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
@DecoratedWith(SingleAudioMapperDecorator.class)
public interface SingleAudioMapper extends EntityMapper<SingleAudioDTO, SingleAudio> {
    @Mapping(target = "thumbnail", ignore = true)
    @Mapping(target = "content", ignore = true)
    SingleAudioDTO toDto(SingleAudio s);
}
