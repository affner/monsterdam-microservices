package com.monsterdam.profile.repository;

import com.monsterdam.profile.domain.UserEvent;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UserEvent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserEventRepository extends JpaRepository<UserEvent, Long> {}
