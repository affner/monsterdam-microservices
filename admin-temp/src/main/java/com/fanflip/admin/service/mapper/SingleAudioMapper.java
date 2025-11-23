package com.fanflip.admin.service.mapper;

import com.fanflip.admin.domain.SingleAudio;
import com.fanflip.admin.service.dto.SingleAudioDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SingleAudio} and its DTO {@link SingleAudioDTO}.
 */
@Mapper(componentModel = "spring")
public interface SingleAudioMapper extends EntityMapper<SingleAudioDTO, SingleAudio> {}
