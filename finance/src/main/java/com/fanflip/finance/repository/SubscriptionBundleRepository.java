package com.monsterdam.finance.repository;

import com.monsterdam.finance.domain.SubscriptionBundle;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SubscriptionBundle entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubscriptionBundleRepository extends JpaRepository<SubscriptionBundle, Long> {}
