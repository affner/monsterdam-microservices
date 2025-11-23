package com.fanflip.multimedia.repository;

import com.fanflip.multimedia.domain.ContentPackage;
import com.fanflip.multimedia.service.dto.ContentPackageDTO;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA repository for the ContentPackage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContentPackageRepository extends JpaRepository<ContentPackage, Long> {

    @Query("SELECT entity FROM ContentPackage entity WHERE entity.postId = :postId")
    Optional<ContentPackageDTO> findByIdPostFeed(@Param("postId") Long postId);

}
