package com.monsterdam.profile.repository;

import com.monsterdam.profile.domain.UserLite;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UserLite entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserLiteRepository extends JpaRepository<UserLite, Long> {}
