package com.fanflip.catalogs.repository;

import com.fanflip.catalogs.domain.HelpCategory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the HelpCategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HelpCategoryRepository extends JpaRepository<HelpCategory, Long> {}
