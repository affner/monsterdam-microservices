package com.monsterdam.profile.repository;

import com.monsterdam.profile.domain.Feedback;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Feedback entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {}
