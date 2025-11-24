package com.monsterdam.admin.service.mapper;

import com.monsterdam.admin.domain.UserProfile;
import com.monsterdam.admin.domain.VideoStory;
import com.monsterdam.admin.service.dto.UserProfileDTO;
import com.monsterdam.admin.service.dto.VideoStoryDTO;
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
