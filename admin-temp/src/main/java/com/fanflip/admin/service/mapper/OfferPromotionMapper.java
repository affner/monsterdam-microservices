package com.fanflip.admin.service.mapper;

import com.fanflip.admin.domain.OfferPromotion;
import com.fanflip.admin.domain.UserProfile;
import com.fanflip.admin.service.dto.OfferPromotionDTO;
import com.fanflip.admin.service.dto.UserProfileDTO;
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
