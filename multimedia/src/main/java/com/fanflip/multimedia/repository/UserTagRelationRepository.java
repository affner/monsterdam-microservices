package com.fanflip.multimedia.repository;

import com.fanflip.multimedia.domain.UserTagRelation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UserTagRelation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserTagRelationRepository extends JpaRepository<UserTagRelation, Long> {}
