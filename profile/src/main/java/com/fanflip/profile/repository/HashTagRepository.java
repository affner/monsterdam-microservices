package com.monsterdam.profile.repository;

import com.monsterdam.profile.domain.HashTag;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the HashTag entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HashTagRepository extends JpaRepository<HashTag, Long> {}
