package com.monsterdam.catalogs.repository;

import com.monsterdam.catalogs.domain.GlobalEvent;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the GlobalEvent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GlobalEventRepository extends JpaRepository<GlobalEvent, Long> {}
