package com.monsterdam.catalogs.repository;

import com.monsterdam.catalogs.domain.PayoutMethod;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PayoutMethod entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PayoutMethodRepository extends JpaRepository<PayoutMethod, Long> {}
