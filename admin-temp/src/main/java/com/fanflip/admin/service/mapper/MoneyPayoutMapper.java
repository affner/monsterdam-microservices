package com.monsterdam.admin.service.mapper;

import com.monsterdam.admin.domain.CreatorEarning;
import com.monsterdam.admin.domain.MoneyPayout;
import com.monsterdam.admin.domain.UserProfile;
import com.monsterdam.admin.service.dto.CreatorEarningDTO;
import com.monsterdam.admin.service.dto.MoneyPayoutDTO;
import com.monsterdam.admin.service.dto.UserProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MoneyPayout} and its DTO {@link MoneyPayoutDTO}.
 */
@Mapper(componentModel = "spring")
public interface MoneyPayoutMapper extends EntityMapper<MoneyPayoutDTO, MoneyPayout> {
    @Mapping(target = "creatorEarning", source = "creatorEarning", qualifiedByName = "creatorEarningAmount")
    @Mapping(target = "creator", source = "creator", qualifiedByName = "userProfileId")
    MoneyPayoutDTO toDto(MoneyPayout s);

    @Named("creatorEarningAmount")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "amount", source = "amount")
    CreatorEarningDTO toDtoCreatorEarningAmount(CreatorEarning creatorEarning);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);
}
