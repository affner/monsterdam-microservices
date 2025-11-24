package com.monsterdam.catalogs.service;

import com.monsterdam.catalogs.service.dto.PaymentProviderDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.monsterdam.catalogs.domain.PaymentProvider}.
 */
public interface PaymentProviderService {
    /**
     * Save a paymentProvider.
     *
     * @param paymentProviderDTO the entity to save.
     * @return the persisted entity.
     */
    PaymentProviderDTO save(PaymentProviderDTO paymentProviderDTO);

    /**
     * Updates a paymentProvider.
     *
     * @param paymentProviderDTO the entity to update.
     * @return the persisted entity.
     */
    PaymentProviderDTO update(PaymentProviderDTO paymentProviderDTO);

    /**
     * Partially updates a paymentProvider.
     *
     * @param paymentProviderDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PaymentProviderDTO> partialUpdate(PaymentProviderDTO paymentProviderDTO);

    /**
     * Get all the paymentProviders.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PaymentProviderDTO> findAll(Pageable pageable);

    /**
     * Get the "id" paymentProvider.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PaymentProviderDTO> findOne(Long id);

    /**
     * Delete the "id" paymentProvider.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
