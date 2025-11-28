package com.monsterdam.catalogs.service;

import com.monsterdam.catalogs.service.dto.PayoutMethodDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.monsterdam.catalogs.domain.PayoutMethod}.
 */
public interface PayoutMethodService {
    /**
     * Save a payoutMethod.
     *
     * @param payoutMethodDTO the entity to save.
     * @return the persisted entity.
     */
    PayoutMethodDTO save(PayoutMethodDTO payoutMethodDTO);

    /**
     * Updates a payoutMethod.
     *
     * @param payoutMethodDTO the entity to update.
     * @return the persisted entity.
     */
    PayoutMethodDTO update(PayoutMethodDTO payoutMethodDTO);

    /**
     * Partially updates a payoutMethod.
     *
     * @param payoutMethodDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PayoutMethodDTO> partialUpdate(PayoutMethodDTO payoutMethodDTO);

    /**
     * Get all the payoutMethods.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PayoutMethodDTO> findAll(Pageable pageable);

    /**
     * Get the "id" payoutMethod.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PayoutMethodDTO> findOne(Long id);

    /**
     * Delete the "id" payoutMethod.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
