package com.fanflip.admin.service.mapper;

import com.fanflip.admin.domain.CreatorEarning;
import com.fanflip.admin.domain.MoneyPayout;
import com.fanflip.admin.domain.UserProfile;
import com.fanflip.admin.service.dto.CreatorEarningDTO;
import com.fanflip.admin.service.dto.MoneyPayoutDTO;
import com.fanflip.admin.service.dto.UserProfileDTO;
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
