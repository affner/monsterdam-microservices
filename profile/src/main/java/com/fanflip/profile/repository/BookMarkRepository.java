package com.fanflip.profile.repository;

import com.fanflip.profile.domain.BookMark;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the BookMark entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BookMarkRepository extends JpaRepository<BookMark, Long> {}
