package com.fanflip.catalogs.repository;

import com.fanflip.catalogs.domain.HelpSubcategory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the HelpSubcategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HelpSubcategoryRepository extends JpaRepository<HelpSubcategory, Long> {}
