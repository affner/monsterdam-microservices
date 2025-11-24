package com.monsterdam.interactions.repository;

import com.monsterdam.interactions.domain.LikeMark;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the LikeMark entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LikeMarkRepository extends JpaRepository<LikeMark, Long> {}
