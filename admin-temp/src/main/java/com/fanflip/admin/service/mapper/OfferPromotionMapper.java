package com.monsterdam.admin.service.mapper;

import com.monsterdam.admin.domain.OfferPromotion;
import com.monsterdam.admin.domain.UserProfile;
import com.monsterdam.admin.service.dto.OfferPromotionDTO;
import com.monsterdam.admin.service.dto.UserProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link OfferPromotion} and its DTO {@link OfferPromotionDTO}.
 */
@Mapper(componentModel = "spring")
public interface OfferPromotionMapper extends EntityMapper<OfferPromotionDTO, OfferPromotion> {
    @Mapping(target = "creator", source = "creator", qualifiedByName = "userProfileId")
    OfferPromotionDTO toDto(OfferPromotion s);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);
}
