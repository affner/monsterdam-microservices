package com.fanflip.catalogs.repository;

import com.fanflip.catalogs.domain.AdminEmailConfigs;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AdminEmailConfigs entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AdminEmailConfigsRepository extends JpaRepository<AdminEmailConfigs, Long> {}
