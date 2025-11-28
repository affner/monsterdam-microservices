package com.monsterdam.finance.service.mapper;

import com.monsterdam.finance.domain.OfferPromotion;
import com.monsterdam.finance.service.dto.OfferPromotionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link OfferPromotion} and its DTO {@link OfferPromotionDTO}.
 */
@Mapper(componentModel = "spring")
public interface OfferPromotionMapper extends EntityMapper<OfferPromotionDTO, OfferPromotion> {}
