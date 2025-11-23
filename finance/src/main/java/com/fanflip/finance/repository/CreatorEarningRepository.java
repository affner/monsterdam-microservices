package com.fanflip.finance.repository;

import com.fanflip.finance.domain.CreatorEarning;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CreatorEarning entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CreatorEarningRepository extends JpaRepository<CreatorEarning, Long> {}
