package com.monsterdam.admin.service.mapper;

import com.monsterdam.admin.domain.ContentPackage;
import com.monsterdam.admin.domain.DirectMessage;
import com.monsterdam.admin.domain.UserProfile;
import com.monsterdam.admin.domain.VideoStory;
import com.monsterdam.admin.service.dto.ContentPackageDTO;
import com.monsterdam.admin.service.dto.DirectMessageDTO;
import com.monsterdam.admin.service.dto.UserProfileDTO;
import com.monsterdam.admin.service.dto.VideoStoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DirectMessage} and its DTO {@link DirectMessageDTO}.
 */
@Mapper(componentModel = "spring")
public interface DirectMessageMapper extends EntityMapper<DirectMessageDTO, DirectMessage> {
    @Mapping(target = "contentPackage", source = "contentPackage", qualifiedByName = "contentPackageId")
    @Mapping(target = "responseTo", source = "responseTo", qualifiedByName = "directMessageMessageContent")
    @Mapping(target = "repliedStory", source = "repliedStory", qualifiedByName = "videoStoryId")
    @Mapping(target = "user", source = "user", qualifiedByName = "userProfileId")
    DirectMessageDTO toDto(DirectMessage s);

    @Named("contentPackageId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ContentPackageDTO toDtoContentPackageId(ContentPackage contentPackage);

    @Named("directMessageMessageContent")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "messageContent", source = "messageContent")
    DirectMessageDTO toDtoDirectMessageMessageContent(DirectMessage directMessage);

    @Named("videoStoryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    VideoStoryDTO toDtoVideoStoryId(VideoStory videoStory);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);
}
