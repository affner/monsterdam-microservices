package com.fanflip.multimedia.service.mapper;

import com.fanflip.multimedia.domain.VideoStory;
import com.fanflip.multimedia.service.dto.VideoStoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link VideoStory} and its DTO {@link VideoStoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface VideoStoryMapper extends EntityMapper<VideoStoryDTO, VideoStory> {}
