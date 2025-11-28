package com.monsterdam.profile.repository;

import com.monsterdam.profile.domain.StateUserRelation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the StateUserRelation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StateUserRelationRepository extends JpaRepository<StateUserRelation, Long> {}
