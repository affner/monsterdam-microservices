package com.fanflip.profile.repository;

import com.fanflip.profile.domain.SpecialAward;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SpecialAward entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SpecialAwardRepository extends JpaRepository<SpecialAward, Long> {}
