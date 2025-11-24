package com.monsterdam.catalogs.repository;

import com.monsterdam.catalogs.domain.HelpRelatedArticle;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the HelpRelatedArticle entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HelpRelatedArticleRepository extends JpaRepository<HelpRelatedArticle, Long> {}
