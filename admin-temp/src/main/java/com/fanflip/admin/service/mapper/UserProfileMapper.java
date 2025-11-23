package com.fanflip.admin.service.mapper;

import com.fanflip.admin.domain.Country;
import com.fanflip.admin.domain.HashTag;
import com.fanflip.admin.domain.State;
import com.fanflip.admin.domain.UserEvent;
import com.fanflip.admin.domain.UserLite;
import com.fanflip.admin.domain.UserProfile;
import com.fanflip.admin.domain.UserSettings;
import com.fanflip.admin.service.dto.CountryDTO;
import com.fanflip.admin.service.dto.HashTagDTO;
import com.fanflip.admin.service.dto.StateDTO;
import com.fanflip.admin.service.dto.UserEventDTO;
import com.fanflip.admin.service.dto.UserLiteDTO;
import com.fanflip.admin.service.dto.UserProfileDTO;
import com.fanflip.admin.service.dto.UserSettingsDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserProfile} and its DTO {@link UserProfileDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserProfileMapper extends EntityMapper<UserProfileDTO, UserProfile> {
    @Mapping(target = "userLite", source = "userLite", qualifiedByName = "userLiteId")
    @Mapping(target = "settings", source = "settings", qualifiedByName = "userSettingsId")
    @Mapping(target = "countryOfBirth", source = "countryOfBirth", qualifiedByName = "countryName")
    @Mapping(target = "stateOfResidence", source = "stateOfResidence", qualifiedByName = "stateStateName")
    @Mapping(target = "followeds", source = "followeds", qualifiedByName = "userProfileIdSet")
    @Mapping(target = "blockedLists", source = "blockedLists", qualifiedByName = "userProfileIdSet")
    @Mapping(target = "loyaLists", source = "loyaLists", qualifiedByName = "userProfileIdSet")
    @Mapping(target = "subscribeds", source = "subscribeds", qualifiedByName = "userProfileIdSet")
    @Mapping(target = "joinedEvents", source = "joinedEvents", qualifiedByName = "userEventStartDateSet")
    @Mapping(target = "blockedUbications", source = "blockedUbications", qualifiedByName = "stateIdSet")
    @Mapping(target = "hashTags", source = "hashTags", qualifiedByName = "hashTagIdSet")
    UserProfileDTO toDto(UserProfile s);

    @Mapping(target = "removeFollowed", ignore = true)
    @Mapping(target = "removeBlockedList", ignore = true)
    @Mapping(target = "removeLoyaLists", ignore = true)
    @Mapping(target = "removeSubscribed", ignore = true)
    @Mapping(target = "removeJoinedEvents", ignore = true)
    @Mapping(target = "removeBlockedUbications", ignore = true)
    @Mapping(target = "removeHashTags", ignore = true)
    UserProfile toEntity(UserProfileDTO userProfileDTO);

    @Named("userLiteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserLiteDTO toDtoUserLiteId(UserLite userLite);

    @Named("userSettingsId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserSettingsDTO toDtoUserSettingsId(UserSettings userSettings);

    @Named("userEventStartDate")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "startDate", source = "startDate")
    UserEventDTO toDtoUserEventStartDate(UserEvent userEvent);

    @Named("userEventStartDateSet")
    default Set<UserEventDTO> toDtoUserEventStartDateSet(Set<UserEvent> userEvent) {
        return userEvent.stream().map(this::toDtoUserEventStartDate).collect(Collectors.toSet());
    }

    @Named("countryName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    CountryDTO toDtoCountryName(Country country);

    @Named("stateStateName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "stateName", source = "stateName")
    StateDTO toDtoStateStateName(State state);

    @Named("stateId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    StateDTO toDtoStateId(State state);

    @Named("stateIdSet")
    default Set<StateDTO> toDtoStateIdSet(Set<State> state) {
        return state.stream().map(this::toDtoStateId).collect(Collectors.toSet());
    }

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);

    @Named("userProfileIdSet")
    default Set<UserProfileDTO> toDtoUserProfileIdSet(Set<UserProfile> userProfile) {
        return userProfile.stream().map(this::toDtoUserProfileId).collect(Collectors.toSet());
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
