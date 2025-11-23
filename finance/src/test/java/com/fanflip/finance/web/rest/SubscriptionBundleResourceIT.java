package com.fanflip.finance.web.rest;

import static com.fanflip.finance.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fanflip.finance.IntegrationTest;
import com.fanflip.finance.domain.SubscriptionBundle;
import com.fanflip.finance.repository.SubscriptionBundleRepository;
import com.fanflip.finance.service.dto.SubscriptionBundleDTO;
import com.fanflip.finance.service.mapper.SubscriptionBundleMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link SubscriptionBundleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SubscriptionBundleResourceIT {

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(0);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(1);

    private static final Duration DEFAULT_DURATION = Duration.ofHours(6);
    private static final Duration UPDATED_DURATION = Duration.ofHours(12);

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_DELETED = false;
    private static final Boolean UPDATED_IS_DELETED = true;

    private static final Long DEFAULT_CREATOR_ID = 1L;
    private static final Long UPDATED_CREATOR_ID = 2L;

    private static final String ENTITY_API_URL = "/api/subscription-bundles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SubscriptionBundleRepository subscriptionBundleRepository;

    @Autowired
    private SubscriptionBundleMapper subscriptionBundleMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSubscriptionBundleMockMvc;

    private SubscriptionBundle subscriptionBundle;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubscriptionBundle createEntity(EntityManager em) {
        SubscriptionBundle subscriptionBundle = new SubscriptionBundle()
            .amount(DEFAULT_AMOUNT)
            .duration(DEFAULT_DURATION)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .isDeleted(DEFAULT_IS_DELETED)
            .creatorId(DEFAULT_CREATOR_ID);
        return subscriptionBundle;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubscriptionBundle createUpdatedEntity(EntityManager em) {
        SubscriptionBundle subscriptionBundle = new SubscriptionBundle()
            .amount(UPDATED_AMOUNT)
            .duration(UPDATED_DURATION)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .creatorId(UPDATED_CREATOR_ID);
        return subscriptionBundle;
    }

    @BeforeEach
    public void initTest() {
        subscriptionBundle = createEntity(em);
    }

    @Test
    @Transactional
    void createSubscriptionBundle() throws Exception {
        int databaseSizeBeforeCreate = subscriptionBundleRepository.findAll().size();
        // Create the SubscriptionBundle
        SubscriptionBundleDTO subscriptionBundleDTO = subscriptionBundleMapper.toDto(subscriptionBundle);
        restSubscriptionBundleMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subscriptionBundleDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SubscriptionBundle in the database
        List<SubscriptionBundle> subscriptionBundleList = subscriptionBundleRepository.findAll();
        assertThat(subscriptionBundleList).hasSize(databaseSizeBeforeCreate + 1);
        SubscriptionBundle testSubscriptionBundle = subscriptionBundleList.get(subscriptionBundleList.size() - 1);
        assertThat(testSubscriptionBundle.getAmount()).isEqualByComparingTo(DEFAULT_AMOUNT);
        assertThat(testSubscriptionBundle.getDuration()).isEqualTo(DEFAULT_DURATION);
        assertThat(testSubscriptionBundle.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testSubscriptionBundle.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testSubscriptionBundle.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testSubscriptionBundle.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testSubscriptionBundle.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
        assertThat(testSubscriptionBundle.getCreatorId()).isEqualTo(DEFAULT_CREATOR_ID);
    }

    @Test
    @Transactional
    void createSubscriptionBundleWithExistingId() throws Exception {
        // Create the SubscriptionBundle with an existing ID
        subscriptionBundle.setId(1L);
        SubscriptionBundleDTO subscriptionBundleDTO = subscriptionBundleMapper.toDto(subscriptionBundle);

        int databaseSizeBeforeCreate = subscriptionBundleRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSubscriptionBundleMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subscriptionBundleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubscriptionBundle in the database
        List<SubscriptionBundle> subscriptionBundleList = subscriptionBundleRepository.findAll();
        assertThat(subscriptionBundleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = subscriptionBundleRepository.findAll().size();
        // set the field null
        subscriptionBundle.setAmount(null);

        // Create the SubscriptionBundle, which fails.
        SubscriptionBundleDTO subscriptionBundleDTO = subscriptionBundleMapper.toDto(subscriptionBundle);

        restSubscriptionBundleMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subscriptionBundleDTO))
            )
            .andExpect(status().isBadRequest());

        List<SubscriptionBundle> subscriptionBundleList = subscriptionBundleRepository.findAll();
        assertThat(subscriptionBundleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDurationIsRequired() throws Exception {
        int databaseSizeBeforeTest = subscriptionBundleRepository.findAll().size();
        // set the field null
        subscriptionBundle.setDuration(null);

        // Create the SubscriptionBundle, which fails.
        SubscriptionBundleDTO subscriptionBundleDTO = subscriptionBundleMapper.toDto(subscriptionBundle);

        restSubscriptionBundleMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subscriptionBundleDTO))
            )
            .andExpect(status().isBadRequest());

        List<SubscriptionBundle> subscriptionBundleList = subscriptionBundleRepository.findAll();
        assertThat(subscriptionBundleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = subscriptionBundleRepository.findAll().size();
        // set the field null
        subscriptionBundle.setCreatedDate(null);

        // Create the SubscriptionBundle, which fails.
        SubscriptionBundleDTO subscriptionBundleDTO = subscriptionBundleMapper.toDto(subscriptionBundle);

        restSubscriptionBundleMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subscriptionBundleDTO))
            )
            .andExpect(status().isBadRequest());

        List<SubscriptionBundle> subscriptionBundleList = subscriptionBundleRepository.findAll();
        assertThat(subscriptionBundleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = subscriptionBundleRepository.findAll().size();
        // set the field null
        subscriptionBundle.setIsDeleted(null);

        // Create the SubscriptionBundle, which fails.
        SubscriptionBundleDTO subscriptionBundleDTO = subscriptionBundleMapper.toDto(subscriptionBundle);

        restSubscriptionBundleMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subscriptionBundleDTO))
            )
            .andExpect(status().isBadRequest());

        List<SubscriptionBundle> subscriptionBundleList = subscriptionBundleRepository.findAll();
        assertThat(subscriptionBundleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatorIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = subscriptionBundleRepository.findAll().size();
        // set the field null
        subscriptionBundle.setCreatorId(null);

        // Create the SubscriptionBundle, which fails.
        SubscriptionBundleDTO subscriptionBundleDTO = subscriptionBundleMapper.toDto(subscriptionBundle);

        restSubscriptionBundleMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subscriptionBundleDTO))
            )
            .andExpect(status().isBadRequest());

        List<SubscriptionBundle> subscriptionBundleList = subscriptionBundleRepository.findAll();
        assertThat(subscriptionBundleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSubscriptionBundles() throws Exception {
        // Initialize the database
        subscriptionBundleRepository.saveAndFlush(subscriptionBundle);

        // Get all the subscriptionBundleList
        restSubscriptionBundleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subscriptionBundle.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())))
            .andExpect(jsonPath("$.[*].creatorId").value(hasItem(DEFAULT_CREATOR_ID.intValue())));
    }

    @Test
    @Transactional
    void getSubscriptionBundle() throws Exception {
        // Initialize the database
        subscriptionBundleRepository.saveAndFlush(subscriptionBundle);

        // Get the subscriptionBundle
        restSubscriptionBundleMockMvc
            .perform(get(ENTITY_API_URL_ID, subscriptionBundle.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(subscriptionBundle.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.duration").value(DEFAULT_DURATION.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()))
            .andExpect(jsonPath("$.creatorId").value(DEFAULT_CREATOR_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingSubscriptionBundle() throws Exception {
        // Get the subscriptionBundle
        restSubscriptionBundleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSubscriptionBundle() throws Exception {
        // Initialize the database
        subscriptionBundleRepository.saveAndFlush(subscriptionBundle);

        int databaseSizeBeforeUpdate = subscriptionBundleRepository.findAll().size();

        // Update the subscriptionBundle
        SubscriptionBundle updatedSubscriptionBundle = subscriptionBundleRepository.findById(subscriptionBundle.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSubscriptionBundle are not directly saved in db
        em.detach(updatedSubscriptionBundle);
        updatedSubscriptionBundle
            .amount(UPDATED_AMOUNT)
            .duration(UPDATED_DURATION)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .creatorId(UPDATED_CREATOR_ID);
        SubscriptionBundleDTO subscriptionBundleDTO = subscriptionBundleMapper.toDto(updatedSubscriptionBundle);

        restSubscriptionBundleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, subscriptionBundleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subscriptionBundleDTO))
            )
            .andExpect(status().isOk());

        // Validate the SubscriptionBundle in the database
        List<SubscriptionBundle> subscriptionBundleList = subscriptionBundleRepository.findAll();
        assertThat(subscriptionBundleList).hasSize(databaseSizeBeforeUpdate);
        SubscriptionBundle testSubscriptionBundle = subscriptionBundleList.get(subscriptionBundleList.size() - 1);
        assertThat(testSubscriptionBundle.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
        assertThat(testSubscriptionBundle.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testSubscriptionBundle.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testSubscriptionBundle.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testSubscriptionBundle.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testSubscriptionBundle.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testSubscriptionBundle.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testSubscriptionBundle.getCreatorId()).isEqualTo(UPDATED_CREATOR_ID);
    }

    @Test
    @Transactional
    void putNonExistingSubscriptionBundle() throws Exception {
        int databaseSizeBeforeUpdate = subscriptionBundleRepository.findAll().size();
        subscriptionBundle.setId(longCount.incrementAndGet());

        // Create the SubscriptionBundle
        SubscriptionBundleDTO subscriptionBundleDTO = subscriptionBundleMapper.toDto(subscriptionBundle);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubscriptionBundleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, subscriptionBundleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subscriptionBundleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubscriptionBundle in the database
        List<SubscriptionBundle> subscriptionBundleList = subscriptionBundleRepository.findAll();
        assertThat(subscriptionBundleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSubscriptionBundle() throws Exception {
        int databaseSizeBeforeUpdate = subscriptionBundleRepository.findAll().size();
        subscriptionBundle.setId(longCount.incrementAndGet());

        // Create the SubscriptionBundle
        SubscriptionBundleDTO subscriptionBundleDTO = subscriptionBundleMapper.toDto(subscriptionBundle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubscriptionBundleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subscriptionBundleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubscriptionBundle in the database
        List<SubscriptionBundle> subscriptionBundleList = subscriptionBundleRepository.findAll();
        assertThat(subscriptionBundleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSubscriptionBundle() throws Exception {
        int databaseSizeBeforeUpdate = subscriptionBundleRepository.findAll().size();
        subscriptionBundle.setId(longCount.incrementAndGet());

        // Create the SubscriptionBundle
        SubscriptionBundleDTO subscriptionBundleDTO = subscriptionBundleMapper.toDto(subscriptionBundle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubscriptionBundleMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subscriptionBundleDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SubscriptionBundle in the database
        List<SubscriptionBundle> subscriptionBundleList = subscriptionBundleRepository.findAll();
        assertThat(subscriptionBundleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSubscriptionBundleWithPatch() throws Exception {
        // Initialize the database
        subscriptionBundleRepository.saveAndFlush(subscriptionBundle);

        int databaseSizeBeforeUpdate = subscriptionBundleRepository.findAll().size();

        // Update the subscriptionBundle using partial update
        SubscriptionBundle partialUpdatedSubscriptionBundle = new SubscriptionBundle();
        partialUpdatedSubscriptionBundle.setId(subscriptionBundle.getId());

        partialUpdatedSubscriptionBundle
            .amount(UPDATED_AMOUNT)
            .duration(UPDATED_DURATION)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .isDeleted(UPDATED_IS_DELETED);

        restSubscriptionBundleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubscriptionBundle.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSubscriptionBundle))
            )
            .andExpect(status().isOk());

        // Validate the SubscriptionBundle in the database
        List<SubscriptionBundle> subscriptionBundleList = subscriptionBundleRepository.findAll();
        assertThat(subscriptionBundleList).hasSize(databaseSizeBeforeUpdate);
        SubscriptionBundle testSubscriptionBundle = subscriptionBundleList.get(subscriptionBundleList.size() - 1);
        assertThat(testSubscriptionBundle.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
        assertThat(testSubscriptionBundle.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testSubscriptionBundle.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testSubscriptionBundle.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testSubscriptionBundle.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testSubscriptionBundle.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testSubscriptionBundle.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testSubscriptionBundle.getCreatorId()).isEqualTo(DEFAULT_CREATOR_ID);
    }

    @Test
    @Transactional
    void fullUpdateSubscriptionBundleWithPatch() throws Exception {
        // Initialize the database
        subscriptionBundleRepository.saveAndFlush(subscriptionBundle);

        int databaseSizeBeforeUpdate = subscriptionBundleRepository.findAll().size();

        // Update the subscriptionBundle using partial update
        SubscriptionBundle partialUpdatedSubscriptionBundle = new SubscriptionBundle();
        partialUpdatedSubscriptionBundle.setId(subscriptionBundle.getId());

        partialUpdatedSubscriptionBundle
            .amount(UPDATED_AMOUNT)
            .duration(UPDATED_DURATION)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .creatorId(UPDATED_CREATOR_ID);

        restSubscriptionBundleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubscriptionBundle.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSubscriptionBundle))
            )
            .andExpect(status().isOk());

        // Validate the SubscriptionBundle in the database
        List<SubscriptionBundle> subscriptionBundleList = subscriptionBundleRepository.findAll();
        assertThat(subscriptionBundleList).hasSize(databaseSizeBeforeUpdate);
        SubscriptionBundle testSubscriptionBundle = subscriptionBundleList.get(subscriptionBundleList.size() - 1);
        assertThat(testSubscriptionBundle.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
        assertThat(testSubscriptionBundle.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testSubscriptionBundle.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testSubscriptionBundle.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testSubscriptionBundle.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testSubscriptionBundle.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testSubscriptionBundle.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testSubscriptionBundle.getCreatorId()).isEqualTo(UPDATED_CREATOR_ID);
    }

    @Test
    @Transactional
    void patchNonExistingSubscriptionBundle() throws Exception {
        int databaseSizeBeforeUpdate = subscriptionBundleRepository.findAll().size();
        subscriptionBundle.setId(longCount.incrementAndGet());

        // Create the SubscriptionBundle
        SubscriptionBundleDTO subscriptionBundleDTO = subscriptionBundleMapper.toDto(subscriptionBundle);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubscriptionBundleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, subscriptionBundleDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(subscriptionBundleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubscriptionBundle in the database
        List<SubscriptionBundle> subscriptionBundleList = subscriptionBundleRepository.findAll();
        assertThat(subscriptionBundleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSubscriptionBundle() throws Exception {
        int databaseSizeBeforeUpdate = subscriptionBundleRepository.findAll().size();
        subscriptionBundle.setId(longCount.incrementAndGet());

        // Create the SubscriptionBundle
        SubscriptionBundleDTO subscriptionBundleDTO = subscriptionBundleMapper.toDto(subscriptionBundle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubscriptionBundleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(subscriptionBundleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubscriptionBundle in the database
        List<SubscriptionBundle> subscriptionBundleList = subscriptionBundleRepository.findAll();
        assertThat(subscriptionBundleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSubscriptionBundle() throws Exception {
        int databaseSizeBeforeUpdate = subscriptionBundleRepository.findAll().size();
        subscriptionBundle.setId(longCount.incrementAndGet());

        // Create the SubscriptionBundle
        SubscriptionBundleDTO subscriptionBundleDTO = subscriptionBundleMapper.toDto(subscriptionBundle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubscriptionBundleMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(subscriptionBundleDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SubscriptionBundle in the database
        List<SubscriptionBundle> subscriptionBundleList = subscriptionBundleRepository.findAll();
        assertThat(subscriptionBundleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSubscriptionBundle() throws Exception {
        // Initialize the database
        subscriptionBundleRepository.saveAndFlush(subscriptionBundle);

        int databaseSizeBeforeDelete = subscriptionBundleRepository.findAll().size();

        // Delete the subscriptionBundle
        restSubscriptionBundleMockMvc
            .perform(delete(ENTITY_API_URL_ID, subscriptionBundle.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SubscriptionBundle> subscriptionBundleList = subscriptionBundleRepository.findAll();
        assertThat(subscriptionBundleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
