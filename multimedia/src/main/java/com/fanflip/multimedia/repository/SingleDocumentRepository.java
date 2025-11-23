package com.fanflip.multimedia.repository;

import com.fanflip.multimedia.domain.SingleDocument;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SingleDocument entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SingleDocumentRepository extends JpaRepository<SingleDocument, Long> {}
