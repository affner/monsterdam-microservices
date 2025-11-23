package com.fanflip.profile.service.mapper;

import com.fanflip.profile.domain.HashTag;
import com.fanflip.profile.domain.UserEvent;
import com.fanflip.profile.domain.UserProfile;
import com.fanflip.profile.domain.UserSettings;
import com.fanflip.profile.service.dto.HashTagDTO;
import com.fanflip.profile.service.dto.UserEventDTO;
import com.fanflip.profile.service.dto.UserProfileDTO;
import com.fanflip.profile.service.dto.UserSettingsDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserProfile} and its DTO {@link UserProfileDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserProfileMapper extends EntityMapper<UserProfileDTO, UserProfile> {
    @Mapping(target = "settings", source = "settings", qualifiedByName = "userSettingsId")
    @Mapping(target = "followeds", source = "followeds", qualifiedByName = "userProfileIdSet")
    @Mapping(target = "blockedLists", source = "blockedLists", qualifiedByName = "userProfileIdSet")
    @Mapping(target = "loyaLists", source = "loyaLists", qualifiedByName = "userProfileIdSet")
    @Mapping(target = "subscribeds", source = "subscribeds", qualifiedByName = "userProfileIdSet")
    @Mapping(target = "joinedEvents", source = "joinedEvents", qualifiedByName = "userEventStartDateSet")
    @Mapping(target = "hashtags", source = "hashtags", qualifiedByName = "hashTagIdSet")
    UserProfileDTO toDto(UserProfile s);

    @Mapping(target = "removeFollowed", ignore = true)
    @Mapping(target = "removeBlockedList", ignore = true)
    @Mapping(target = "removeLoyaLists", ignore = true)
    @Mapping(target = "removeSubscribed", ignore = true)
    @Mapping(target = "removeJoinedEvents", ignore = true)
    @Mapping(target = "removeHashtags", ignore = true)
    UserProfile toEntity(UserProfileDTO userProfileDTO);

    @Named("userSettingsId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserSettingsDTO toDtoUserSettingsId(UserSettings userSettings);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);

    @Named("userProfileIdSet")
    default Set<UserProfileDTO> toDtoUserProfileIdSet(Set<UserProfile> userProfile) {
        return userProfile.stream().map(this::toDtoUserProfileId).collect(Collectors.toSet());
    }

    @Named("userEventStartDate")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "startDate", source = "startDate")
    UserEventDTO toDtoUserEventStartDate(UserEvent userEvent);

    @Named("userEventStartDateSet")
    default Set<UserEventDTO> toDtoUserEventStartDateSet(Set<UserEvent> userEvent) {
        return userEvent.stream().map(this::toDtoUserEventStartDate).collect(Collectors.toSet());
    }

    @Named("hashTagId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    HashTagDTO toDtoHashTagId(HashTag hashTag);

    @Named("hashTagIdSet")
    default Set<HashTagDTO> toDtoHashTagIdSet(Set<HashTag> hashTag) {
        return hashTag.stream().map(this::toDtoHashTagId).collect(Collectors.toSet());
    }
}
