package com.fanflip.admin.service.mapper;

import com.fanflip.admin.domain.PersonalSocialLinks;
import com.fanflip.admin.domain.SocialNetwork;
import com.fanflip.admin.domain.UserProfile;
import com.fanflip.admin.service.dto.PersonalSocialLinksDTO;
import com.fanflip.admin.service.dto.SocialNetworkDTO;
import com.fanflip.admin.service.dto.UserProfileDTO;
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
