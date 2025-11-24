package com.monsterdam.admin.service.mapper;

import com.monsterdam.admin.domain.SingleAudio;
import com.monsterdam.admin.service.dto.SingleAudioDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SingleAudio} and its DTO {@link SingleAudioDTO}.
 */
@Mapper(componentModel = "spring")
public interface SingleAudioMapper extends EntityMapper<SingleAudioDTO, SingleAudio> {}
