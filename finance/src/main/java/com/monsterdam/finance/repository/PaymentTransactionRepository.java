package com.monsterdam.finance.repository;

import com.monsterdam.finance.domain.PaymentTransaction;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PaymentTransaction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, Long> {}
