package com.fanflip.admin.service.mapper;

import com.fanflip.admin.domain.UserProfile;
import com.fanflip.admin.domain.VideoStory;
import com.fanflip.admin.service.dto.UserProfileDTO;
import com.fanflip.admin.service.dto.VideoStoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link VideoStory} and its DTO {@link VideoStoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface VideoStoryMapper extends EntityMapper<VideoStoryDTO, VideoStory> {
    @Mapping(target = "creator", source = "creator", qualifiedByName = "userProfileId")
    VideoStoryDTO toDto(VideoStory s);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);
}
