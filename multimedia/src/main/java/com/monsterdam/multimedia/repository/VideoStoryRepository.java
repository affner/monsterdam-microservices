package com.monsterdam.multimedia.repository;

import com.monsterdam.multimedia.domain.VideoStory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the VideoStory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VideoStoryRepository extends JpaRepository<VideoStory, Long> {}
