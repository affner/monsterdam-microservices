package com.monsterdam.catalogs.repository;

import com.monsterdam.catalogs.domain.AdminSystemConfigs;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AdminSystemConfigs entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AdminSystemConfigsRepository extends JpaRepository<AdminSystemConfigs, Long> {}
