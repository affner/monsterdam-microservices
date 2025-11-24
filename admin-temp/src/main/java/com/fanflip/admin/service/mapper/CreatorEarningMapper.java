package com.monsterdam.admin.service.mapper;

import com.monsterdam.admin.domain.CreatorEarning;
import com.monsterdam.admin.domain.UserProfile;
import com.monsterdam.admin.service.dto.CreatorEarningDTO;
import com.monsterdam.admin.service.dto.UserProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CreatorEarning} and its DTO {@link CreatorEarningDTO}.
 */
@Mapper(componentModel = "spring")
public interface CreatorEarningMapper extends EntityMapper<CreatorEarningDTO, CreatorEarning> {
    @Mapping(target = "creator", source = "creator", qualifiedByName = "userProfileId")
    CreatorEarningDTO toDto(CreatorEarning s);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);
}
