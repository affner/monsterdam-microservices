package com.monsterdam.interactions.repository;

import com.monsterdam.interactions.domain.PostPoll;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PostPoll entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PostPollRepository extends JpaRepository<PostPoll, Long> {}
