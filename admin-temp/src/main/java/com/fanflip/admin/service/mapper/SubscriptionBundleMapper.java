package com.monsterdam.admin.service.mapper;

import com.monsterdam.admin.domain.SubscriptionBundle;
import com.monsterdam.admin.domain.UserProfile;
import com.monsterdam.admin.service.dto.SubscriptionBundleDTO;
import com.monsterdam.admin.service.dto.UserProfileDTO;
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
