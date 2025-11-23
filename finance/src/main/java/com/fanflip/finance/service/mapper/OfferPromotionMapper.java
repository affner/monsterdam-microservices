package com.fanflip.finance.service.mapper;

import com.fanflip.finance.domain.OfferPromotion;
import com.fanflip.finance.service.dto.OfferPromotionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link OfferPromotion} and its DTO {@link OfferPromotionDTO}.
 */
@Mapper(componentModel = "spring")
public interface OfferPromotionMapper extends EntityMapper<OfferPromotionDTO, OfferPromotion> {}
