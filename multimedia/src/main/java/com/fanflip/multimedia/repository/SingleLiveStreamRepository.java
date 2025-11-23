package com.fanflip.multimedia.repository;

import com.fanflip.multimedia.domain.SingleLiveStream;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SingleLiveStream entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SingleLiveStreamRepository extends JpaRepository<SingleLiveStream, Long> {}
