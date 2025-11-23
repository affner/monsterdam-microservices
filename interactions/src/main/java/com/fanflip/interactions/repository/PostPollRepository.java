package com.fanflip.interactions.repository;

import com.fanflip.interactions.domain.PostPoll;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PostPoll entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PostPollRepository extends JpaRepository<PostPoll, Long> {}
