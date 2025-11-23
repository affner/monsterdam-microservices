package com.fanflip.catalogs.repository;

import com.fanflip.catalogs.domain.EmojiType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EmojiType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmojiTypeRepository extends JpaRepository<EmojiType, Long> {}
