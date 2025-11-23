package com.fanflip.admin.service.mapper;

import com.fanflip.admin.domain.SubscriptionBundle;
import com.fanflip.admin.domain.UserProfile;
import com.fanflip.admin.service.dto.SubscriptionBundleDTO;
import com.fanflip.admin.service.dto.UserProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SubscriptionBundle} and its DTO {@link SubscriptionBundleDTO}.
 */
@Mapper(componentModel = "spring")
public interface SubscriptionBundleMapper extends EntityMapper<SubscriptionBundleDTO, SubscriptionBundle> {
    @Mapping(target = "creator", source = "creator", qualifiedByName = "userProfileId")
    SubscriptionBundleDTO toDto(SubscriptionBundle s);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);
}
