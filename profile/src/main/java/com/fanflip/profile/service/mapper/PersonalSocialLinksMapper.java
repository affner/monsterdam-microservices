package com.fanflip.profile.service.mapper;

import com.fanflip.profile.domain.PersonalSocialLinks;
import com.fanflip.profile.domain.UserProfile;
import com.fanflip.profile.service.dto.PersonalSocialLinksDTO;
import com.fanflip.profile.service.dto.UserProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PersonalSocialLinks} and its DTO {@link PersonalSocialLinksDTO}.
 */
@Mapper(componentModel = "spring")
public interface PersonalSocialLinksMapper extends EntityMapper<PersonalSocialLinksDTO, PersonalSocialLinks> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userProfileId")
    PersonalSocialLinksDTO toDto(PersonalSocialLinks s);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);
}
