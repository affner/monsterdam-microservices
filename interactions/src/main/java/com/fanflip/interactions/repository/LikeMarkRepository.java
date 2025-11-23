package com.fanflip.interactions.repository;

import com.fanflip.interactions.domain.LikeMark;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the LikeMark entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LikeMarkRepository extends JpaRepository<LikeMark, Long> {}
