package com.monsterdam.admin.service.mapper;

import com.monsterdam.admin.domain.PersonalSocialLinks;
import com.monsterdam.admin.domain.SocialNetwork;
import com.monsterdam.admin.domain.UserProfile;
import com.monsterdam.admin.service.dto.PersonalSocialLinksDTO;
import com.monsterdam.admin.service.dto.SocialNetworkDTO;
import com.monsterdam.admin.service.dto.UserProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PersonalSocialLinks} and its DTO {@link PersonalSocialLinksDTO}.
 */
@Mapper(componentModel = "spring")
public interface PersonalSocialLinksMapper extends EntityMapper<PersonalSocialLinksDTO, PersonalSocialLinks> {
    @Mapping(target = "socialNetwork", source = "socialNetwork", qualifiedByName = "socialNetworkThumbnail")
    @Mapping(target = "user", source = "user", qualifiedByName = "userProfileId")
    PersonalSocialLinksDTO toDto(PersonalSocialLinks s);

    @Named("socialNetworkThumbnail")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "thumbnail", source = "thumbnail")
    SocialNetworkDTO toDtoSocialNetworkThumbnail(SocialNetwork socialNetwork);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);
}
