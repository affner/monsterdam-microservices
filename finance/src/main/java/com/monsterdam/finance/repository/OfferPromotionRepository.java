package com.monsterdam.finance.repository;

import com.monsterdam.finance.domain.OfferPromotion;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the OfferPromotion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OfferPromotionRepository extends JpaRepository<OfferPromotion, Long> {}
