package com.monsterdam.catalogs.repository;

import com.monsterdam.catalogs.domain.SocialNetwork;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SocialNetwork entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SocialNetworkRepository extends JpaRepository<SocialNetwork, Long> {}
