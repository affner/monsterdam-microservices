package com.monsterdam.multimedia.repository;

import com.monsterdam.multimedia.domain.SingleAudio;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SingleAudio entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SingleAudioRepository extends JpaRepository<SingleAudio, Long> {}
