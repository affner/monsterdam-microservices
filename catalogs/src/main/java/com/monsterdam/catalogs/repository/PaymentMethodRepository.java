package com.monsterdam.catalogs.repository;

import com.monsterdam.catalogs.domain.PaymentMethod;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PaymentMethod entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {}
