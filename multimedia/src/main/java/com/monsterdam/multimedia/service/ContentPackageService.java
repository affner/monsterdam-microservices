package com.monsterdam.multimedia.service;

import com.monsterdam.multimedia.service.dto.ContentPackageDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.monsterdam.multimedia.domain.ContentPackage}.
 */
public interface ContentPackageService {
    /**
     * Save a contentPackage.
     *
     * @param contentPackageDTO the entity to save.
     * @return the persisted entity.
     */
    ContentPackageDTO save(ContentPackageDTO contentPackageDTO);

    /**
     * Updates a contentPackage.
     *
     * @param contentPackageDTO the entity to update.
     * @return the persisted entity.
     */
    ContentPackageDTO update(ContentPackageDTO contentPackageDTO);

    /**
     * Partially updates a contentPackage.
     *
     * @param contentPackageDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ContentPackageDTO> partialUpdate(ContentPackageDTO contentPackageDTO);

    /**
     * Get all the contentPackages.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ContentPackageDTO> findAll(Pageable pageable);

    /**
     * Get all the ContentPackageDTO where SpecialReward is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<ContentPackageDTO> findAllWhereSpecialRewardIsNull();

    /**
     * Get the "id" contentPackage.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ContentPackageDTO> findOne(Long id);

    /**
     * Delete the "id" contentPackage.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Delete the "id" contentPackage.
     *
     * @param postId the id of the entity.
     */
    Optional<ContentPackageDTO> findOneByPostFeedId(Long postId);
}
