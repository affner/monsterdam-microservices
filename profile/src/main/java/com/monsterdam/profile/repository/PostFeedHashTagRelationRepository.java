package com.monsterdam.profile.repository;

import com.monsterdam.profile.domain.PostFeedHashTagRelation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PostFeedHashTagRelation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PostFeedHashTagRelationRepository extends JpaRepository<PostFeedHashTagRelation, Long> {}
