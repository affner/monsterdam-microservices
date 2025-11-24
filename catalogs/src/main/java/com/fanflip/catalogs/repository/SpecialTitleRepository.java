package com.monsterdam.catalogs.repository;

import com.monsterdam.catalogs.domain.SpecialTitle;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SpecialTitle entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SpecialTitleRepository extends JpaRepository<SpecialTitle, Long> {}
