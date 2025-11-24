package com.monsterdam.multimedia.repository;

import com.monsterdam.multimedia.domain.SpecialReward;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SpecialReward entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SpecialRewardRepository extends JpaRepository<SpecialReward, Long> {}
