package com.fanflip.finance.service;

import com.fanflip.finance.service.dto.SubscriptionBundleDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.fanflip.finance.domain.SubscriptionBundle}.
 */
public interface SubscriptionBundleService {
    /**
     * Save a subscriptionBundle.
     *
     * @param subscriptionBundleDTO the entity to save.
     * @return the persisted entity.
     */
    SubscriptionBundleDTO save(SubscriptionBundleDTO subscriptionBundleDTO);

    /**
     * Updates a subscriptionBundle.
     *
     * @param subscriptionBundleDTO the entity to update.
     * @return the persisted entity.
     */
    SubscriptionBundleDTO update(SubscriptionBundleDTO subscriptionBundleDTO);

    /**
     * Partially updates a subscriptionBundle.
     *
     * @param subscriptionBundleDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SubscriptionBundleDTO> partialUpdate(SubscriptionBundleDTO subscriptionBundleDTO);

    /**
     * Get all the subscriptionBundles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SubscriptionBundleDTO> findAll(Pageable pageable);

    /**
     * Get the "id" subscriptionBundle.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SubscriptionBundleDTO> findOne(Long id);

    /**
     * Delete the "id" subscriptionBundle.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
