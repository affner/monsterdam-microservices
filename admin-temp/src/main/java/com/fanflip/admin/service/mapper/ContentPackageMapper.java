package com.fanflip.admin.service.mapper;

import com.fanflip.admin.domain.ContentPackage;
import com.fanflip.admin.domain.SingleAudio;
import com.fanflip.admin.domain.UserProfile;
import com.fanflip.admin.service.dto.ContentPackageDTO;
import com.fanflip.admin.service.dto.SingleAudioDTO;
import com.fanflip.admin.service.dto.UserProfileDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ContentPackage} and its DTO {@link ContentPackageDTO}.
 */
@Mapper(componentModel = "spring")
public interface ContentPackageMapper extends EntityMapper<ContentPackageDTO, ContentPackage> {
    @Mapping(target = "audio", source = "audio", qualifiedByName = "singleAudioId")
    @Mapping(target = "usersTaggeds", source = "usersTaggeds", qualifiedByName = "userProfileIdSet")
    ContentPackageDTO toDto(ContentPackage s);

    @Mapping(target = "removeUsersTagged", ignore = true)
    ContentPackage toEntity(ContentPackageDTO contentPackageDTO);

    @Named("singleAudioId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SingleAudioDTO toDtoSingleAudioId(SingleAudio singleAudio);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);

    @Named("userProfileIdSet")
    default Set<UserProfileDTO> toDtoUserProfileIdSet(Set<UserProfile> userProfile) {
        return userProfile.stream().map(this::toDtoUserProfileId).collect(Collectors.toSet());
    }
}
