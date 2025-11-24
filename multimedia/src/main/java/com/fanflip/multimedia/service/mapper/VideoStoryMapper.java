package com.monsterdam.multimedia.service.mapper;

import com.monsterdam.multimedia.domain.VideoStory;
import com.monsterdam.multimedia.service.dto.VideoStoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link VideoStory} and its DTO {@link VideoStoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface VideoStoryMapper extends EntityMapper<VideoStoryDTO, VideoStory> {}
