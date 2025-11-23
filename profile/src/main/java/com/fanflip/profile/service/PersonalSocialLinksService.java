package com.fanflip.profile.service;

import com.fanflip.profile.service.dto.PersonalSocialLinksDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.fanflip.profile.domain.PersonalSocialLinks}.
 */
public interface PersonalSocialLinksService {
    /**
     * Save a personalSocialLinks.
     *
     * @param personalSocialLinksDTO the entity to save.
     * @return the persisted entity.
     */
    PersonalSocialLinksDTO save(PersonalSocialLinksDTO personalSocialLinksDTO);

    /**
     * Updates a personalSocialLinks.
     *
     * @param personalSocialLinksDTO the entity to update.
     * @return the persisted entity.
     */
    PersonalSocialLinksDTO update(PersonalSocialLinksDTO personalSocialLinksDTO);

    /**
     * Partially updates a personalSocialLinks.
     *
     * @param personalSocialLinksDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PersonalSocialLinksDTO> partialUpdate(PersonalSocialLinksDTO personalSocialLinksDTO);

    /**
     * Get all the personalSocialLinks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PersonalSocialLinksDTO> findAll(Pageable pageable);

    /**
     * Get the "id" personalSocialLinks.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PersonalSocialLinksDTO> findOne(Long id);

    /**
     * Delete the "id" personalSocialLinks.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
