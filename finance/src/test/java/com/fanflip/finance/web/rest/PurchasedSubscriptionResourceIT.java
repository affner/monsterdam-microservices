package com.fanflip.finance.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fanflip.finance.IntegrationTest;
import com.fanflip.finance.domain.CreatorEarning;
import com.fanflip.finance.domain.PurchasedSubscription;
import com.fanflip.finance.domain.SubscriptionBundle;
import com.fanflip.finance.domain.enumeration.PurchasedSubscriptionStatus;
import com.fanflip.finance.repository.PurchasedSubscriptionRepository;
import com.fanflip.finance.service.PurchasedSubscriptionService;
import com.fanflip.finance.service.dto.PurchasedSubscriptionDTO;
import com.fanflip.finance.service.mapper.PurchasedSubscriptionMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PurchasedSubscriptionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PurchasedSubscriptionResourceIT {

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

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final PurchasedSubscriptionStatus DEFAULT_SUBSCRIPTION_STATUS = PurchasedSubscriptionStatus.PURCHASED;
    private static final PurchasedSubscriptionStatus UPDATED_SUBSCRIPTION_STATUS = PurchasedSubscriptionStatus.PENDING;

    private static final Long DEFAULT_VIEWER_ID = 1L;
    private static final Long UPDATED_VIEWER_ID = 2L;

    private static final Long DEFAULT_CREATOR_ID = 1L;
    private static final Long UPDATED_CREATOR_ID = 2L;

    private static final String ENTITY_API_URL = "/api/purchased-subscriptions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PurchasedSubscriptionRepository purchasedSubscriptionRepository;

    @Mock
    private PurchasedSubscriptionRepository purchasedSubscriptionRepositoryMock;

    @Autowired
    private PurchasedSubscriptionMapper purchasedSubscriptionMapper;

    @Mock
    private PurchasedSubscriptionService purchasedSubscriptionServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPurchasedSubscriptionMockMvc;

    private PurchasedSubscription purchasedSubscription;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PurchasedSubscription createEntity(EntityManager em) {
        PurchasedSubscription purchasedSubscription = new PurchasedSubscription()
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .isDeleted(DEFAULT_IS_DELETED)
            .endDate(DEFAULT_END_DATE)
            .subscriptionStatus(DEFAULT_SUBSCRIPTION_STATUS)
            .viewerId(DEFAULT_VIEWER_ID)
            .creatorId(DEFAULT_CREATOR_ID);
        // Add required entity
        CreatorEarning creatorEarning;
        if (TestUtil.findAll(em, CreatorEarning.class).isEmpty()) {
            creatorEarning = CreatorEarningResourceIT.createEntity(em);
            em.persist(creatorEarning);
            em.flush();
        } else {
            creatorEarning = TestUtil.findAll(em, CreatorEarning.class).get(0);
        }
        purchasedSubscription.setCreatorEarning(creatorEarning);
        // Add required entity
        SubscriptionBundle subscriptionBundle;
        if (TestUtil.findAll(em, SubscriptionBundle.class).isEmpty()) {
            subscriptionBundle = SubscriptionBundleResourceIT.createEntity(em);
            em.persist(subscriptionBundle);
            em.flush();
        } else {
            subscriptionBundle = TestUtil.findAll(em, SubscriptionBundle.class).get(0);
        }
        purchasedSubscription.setSubscriptionBundle(subscriptionBundle);
        return purchasedSubscription;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PurchasedSubscription createUpdatedEntity(EntityManager em) {
        PurchasedSubscription purchasedSubscription = new PurchasedSubscription()
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .endDate(UPDATED_END_DATE)
            .subscriptionStatus(UPDATED_SUBSCRIPTION_STATUS)
            .viewerId(UPDATED_VIEWER_ID)
            .creatorId(UPDATED_CREATOR_ID);
        // Add required entity
        CreatorEarning creatorEarning;
        if (TestUtil.findAll(em, CreatorEarning.class).isEmpty()) {
            creatorEarning = CreatorEarningResourceIT.createUpdatedEntity(em);
            em.persist(creatorEarning);
            em.flush();
        } else {
            creatorEarning = TestUtil.findAll(em, CreatorEarning.class).get(0);
        }
        purchasedSubscription.setCreatorEarning(creatorEarning);
        // Add required entity
        SubscriptionBundle subscriptionBundle;
        if (TestUtil.findAll(em, SubscriptionBundle.class).isEmpty()) {
            subscriptionBundle = SubscriptionBundleResourceIT.createUpdatedEntity(em);
            em.persist(subscriptionBundle);
            em.flush();
        } else {
            subscriptionBundle = TestUtil.findAll(em, SubscriptionBundle.class).get(0);
        }
        purchasedSubscription.setSubscriptionBundle(subscriptionBundle);
        return purchasedSubscription;
    }

    @BeforeEach
    public void initTest() {
        purchasedSubscription = createEntity(em);
    }

    @Test
    @Transactional
    void createPurchasedSubscription() throws Exception {
        int databaseSizeBeforeCreate = purchasedSubscriptionRepository.findAll().size();
        // Create the PurchasedSubscription
        PurchasedSubscriptionDTO purchasedSubscriptionDTO = purchasedSubscriptionMapper.toDto(purchasedSubscription);
        restPurchasedSubscriptionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(purchasedSubscriptionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the PurchasedSubscription in the database
        List<PurchasedSubscription> purchasedSubscriptionList = purchasedSubscriptionRepository.findAll();
        assertThat(purchasedSubscriptionList).hasSize(databaseSizeBeforeCreate + 1);
        PurchasedSubscription testPurchasedSubscription = purchasedSubscriptionList.get(purchasedSubscriptionList.size() - 1);
        assertThat(testPurchasedSubscription.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testPurchasedSubscription.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testPurchasedSubscription.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testPurchasedSubscription.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testPurchasedSubscription.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
        assertThat(testPurchasedSubscription.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testPurchasedSubscription.getSubscriptionStatus()).isEqualTo(DEFAULT_SUBSCRIPTION_STATUS);
        assertThat(testPurchasedSubscription.getViewerId()).isEqualTo(DEFAULT_VIEWER_ID);
        assertThat(testPurchasedSubscription.getCreatorId()).isEqualTo(DEFAULT_CREATOR_ID);
    }

    @Test
    @Transactional
    void createPurchasedSubscriptionWithExistingId() throws Exception {
        // Create the PurchasedSubscription with an existing ID
        purchasedSubscription.setId(1L);
        PurchasedSubscriptionDTO purchasedSubscriptionDTO = purchasedSubscriptionMapper.toDto(purchasedSubscription);

        int databaseSizeBeforeCreate = purchasedSubscriptionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPurchasedSubscriptionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(purchasedSubscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchasedSubscription in the database
        List<PurchasedSubscription> purchasedSubscriptionList = purchasedSubscriptionRepository.findAll();
        assertThat(purchasedSubscriptionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = purchasedSubscriptionRepository.findAll().size();
        // set the field null
        purchasedSubscription.setCreatedDate(null);

        // Create the PurchasedSubscription, which fails.
        PurchasedSubscriptionDTO purchasedSubscriptionDTO = purchasedSubscriptionMapper.toDto(purchasedSubscription);

        restPurchasedSubscriptionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(purchasedSubscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        List<PurchasedSubscription> purchasedSubscriptionList = purchasedSubscriptionRepository.findAll();
        assertThat(purchasedSubscriptionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = purchasedSubscriptionRepository.findAll().size();
        // set the field null
        purchasedSubscription.setIsDeleted(null);

        // Create the PurchasedSubscription, which fails.
        PurchasedSubscriptionDTO purchasedSubscriptionDTO = purchasedSubscriptionMapper.toDto(purchasedSubscription);

        restPurchasedSubscriptionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(purchasedSubscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        List<PurchasedSubscription> purchasedSubscriptionList = purchasedSubscriptionRepository.findAll();
        assertThat(purchasedSubscriptionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEndDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = purchasedSubscriptionRepository.findAll().size();
        // set the field null
        purchasedSubscription.setEndDate(null);

        // Create the PurchasedSubscription, which fails.
        PurchasedSubscriptionDTO purchasedSubscriptionDTO = purchasedSubscriptionMapper.toDto(purchasedSubscription);

        restPurchasedSubscriptionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(purchasedSubscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        List<PurchasedSubscription> purchasedSubscriptionList = purchasedSubscriptionRepository.findAll();
        assertThat(purchasedSubscriptionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSubscriptionStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = purchasedSubscriptionRepository.findAll().size();
        // set the field null
        purchasedSubscription.setSubscriptionStatus(null);

        // Create the PurchasedSubscription, which fails.
        PurchasedSubscriptionDTO purchasedSubscriptionDTO = purchasedSubscriptionMapper.toDto(purchasedSubscription);

        restPurchasedSubscriptionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(purchasedSubscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        List<PurchasedSubscription> purchasedSubscriptionList = purchasedSubscriptionRepository.findAll();
        assertThat(purchasedSubscriptionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkViewerIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = purchasedSubscriptionRepository.findAll().size();
        // set the field null
        purchasedSubscription.setViewerId(null);

        // Create the PurchasedSubscription, which fails.
        PurchasedSubscriptionDTO purchasedSubscriptionDTO = purchasedSubscriptionMapper.toDto(purchasedSubscription);

        restPurchasedSubscriptionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(purchasedSubscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        List<PurchasedSubscription> purchasedSubscriptionList = purchasedSubscriptionRepository.findAll();
        assertThat(purchasedSubscriptionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatorIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = purchasedSubscriptionRepository.findAll().size();
        // set the field null
        purchasedSubscription.setCreatorId(null);

        // Create the PurchasedSubscription, which fails.
        PurchasedSubscriptionDTO purchasedSubscriptionDTO = purchasedSubscriptionMapper.toDto(purchasedSubscription);

        restPurchasedSubscriptionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(purchasedSubscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        List<PurchasedSubscription> purchasedSubscriptionList = purchasedSubscriptionRepository.findAll();
        assertThat(purchasedSubscriptionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPurchasedSubscriptions() throws Exception {
        // Initialize the database
        purchasedSubscriptionRepository.saveAndFlush(purchasedSubscription);

        // Get all the purchasedSubscriptionList
        restPurchasedSubscriptionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(purchasedSubscription.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].subscriptionStatus").value(hasItem(DEFAULT_SUBSCRIPTION_STATUS.toString())))
            .andExpect(jsonPath("$.[*].viewerId").value(hasItem(DEFAULT_VIEWER_ID.intValue())))
            .andExpect(jsonPath("$.[*].creatorId").value(hasItem(DEFAULT_CREATOR_ID.intValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPurchasedSubscriptionsWithEagerRelationshipsIsEnabled() throws Exception {
        when(purchasedSubscriptionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPurchasedSubscriptionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(purchasedSubscriptionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPurchasedSubscriptionsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(purchasedSubscriptionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPurchasedSubscriptionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(purchasedSubscriptionRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPurchasedSubscription() throws Exception {
        // Initialize the database
        purchasedSubscriptionRepository.saveAndFlush(purchasedSubscription);

        // Get the purchasedSubscription
        restPurchasedSubscriptionMockMvc
            .perform(get(ENTITY_API_URL_ID, purchasedSubscription.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(purchasedSubscription.getId().intValue()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.subscriptionStatus").value(DEFAULT_SUBSCRIPTION_STATUS.toString()))
            .andExpect(jsonPath("$.viewerId").value(DEFAULT_VIEWER_ID.intValue()))
            .andExpect(jsonPath("$.creatorId").value(DEFAULT_CREATOR_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingPurchasedSubscription() throws Exception {
        // Get the purchasedSubscription
        restPurchasedSubscriptionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPurchasedSubscription() throws Exception {
        // Initialize the database
        purchasedSubscriptionRepository.saveAndFlush(purchasedSubscription);

        int databaseSizeBeforeUpdate = purchasedSubscriptionRepository.findAll().size();

        // Update the purchasedSubscription
        PurchasedSubscription updatedPurchasedSubscription = purchasedSubscriptionRepository
            .findById(purchasedSubscription.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedPurchasedSubscription are not directly saved in db
        em.detach(updatedPurchasedSubscription);
        updatedPurchasedSubscription
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .endDate(UPDATED_END_DATE)
            .subscriptionStatus(UPDATED_SUBSCRIPTION_STATUS)
            .viewerId(UPDATED_VIEWER_ID)
            .creatorId(UPDATED_CREATOR_ID);
        PurchasedSubscriptionDTO purchasedSubscriptionDTO = purchasedSubscriptionMapper.toDto(updatedPurchasedSubscription);

        restPurchasedSubscriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, purchasedSubscriptionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(purchasedSubscriptionDTO))
            )
            .andExpect(status().isOk());

        // Validate the PurchasedSubscription in the database
        List<PurchasedSubscription> purchasedSubscriptionList = purchasedSubscriptionRepository.findAll();
        assertThat(purchasedSubscriptionList).hasSize(databaseSizeBeforeUpdate);
        PurchasedSubscription testPurchasedSubscription = purchasedSubscriptionList.get(purchasedSubscriptionList.size() - 1);
        assertThat(testPurchasedSubscription.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testPurchasedSubscription.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testPurchasedSubscription.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testPurchasedSubscription.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testPurchasedSubscription.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testPurchasedSubscription.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testPurchasedSubscription.getSubscriptionStatus()).isEqualTo(UPDATED_SUBSCRIPTION_STATUS);
        assertThat(testPurchasedSubscription.getViewerId()).isEqualTo(UPDATED_VIEWER_ID);
        assertThat(testPurchasedSubscription.getCreatorId()).isEqualTo(UPDATED_CREATOR_ID);
    }

    @Test
    @Transactional
    void putNonExistingPurchasedSubscription() throws Exception {
        int databaseSizeBeforeUpdate = purchasedSubscriptionRepository.findAll().size();
        purchasedSubscription.setId(longCount.incrementAndGet());

        // Create the PurchasedSubscription
        PurchasedSubscriptionDTO purchasedSubscriptionDTO = purchasedSubscriptionMapper.toDto(purchasedSubscription);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPurchasedSubscriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, purchasedSubscriptionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(purchasedSubscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchasedSubscription in the database
        List<PurchasedSubscription> purchasedSubscriptionList = purchasedSubscriptionRepository.findAll();
        assertThat(purchasedSubscriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPurchasedSubscription() throws Exception {
        int databaseSizeBeforeUpdate = purchasedSubscriptionRepository.findAll().size();
        purchasedSubscription.setId(longCount.incrementAndGet());

        // Create the PurchasedSubscription
        PurchasedSubscriptionDTO purchasedSubscriptionDTO = purchasedSubscriptionMapper.toDto(purchasedSubscription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPurchasedSubscriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(purchasedSubscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchasedSubscription in the database
        List<PurchasedSubscription> purchasedSubscriptionList = purchasedSubscriptionRepository.findAll();
        assertThat(purchasedSubscriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPurchasedSubscription() throws Exception {
        int databaseSizeBeforeUpdate = purchasedSubscriptionRepository.findAll().size();
        purchasedSubscription.setId(longCount.incrementAndGet());

        // Create the PurchasedSubscription
        PurchasedSubscriptionDTO purchasedSubscriptionDTO = purchasedSubscriptionMapper.toDto(purchasedSubscription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPurchasedSubscriptionMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(purchasedSubscriptionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PurchasedSubscription in the database
        List<PurchasedSubscription> purchasedSubscriptionList = purchasedSubscriptionRepository.findAll();
        assertThat(purchasedSubscriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePurchasedSubscriptionWithPatch() throws Exception {
        // Initialize the database
        purchasedSubscriptionRepository.saveAndFlush(purchasedSubscription);

        int databaseSizeBeforeUpdate = purchasedSubscriptionRepository.findAll().size();

        // Update the purchasedSubscription using partial update
        PurchasedSubscription partialUpdatedPurchasedSubscription = new PurchasedSubscription();
        partialUpdatedPurchasedSubscription.setId(purchasedSubscription.getId());

        partialUpdatedPurchasedSubscription
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .endDate(UPDATED_END_DATE)
            .subscriptionStatus(UPDATED_SUBSCRIPTION_STATUS)
            .creatorId(UPDATED_CREATOR_ID);

        restPurchasedSubscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPurchasedSubscription.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPurchasedSubscription))
            )
            .andExpect(status().isOk());

        // Validate the PurchasedSubscription in the database
        List<PurchasedSubscription> purchasedSubscriptionList = purchasedSubscriptionRepository.findAll();
        assertThat(purchasedSubscriptionList).hasSize(databaseSizeBeforeUpdate);
        PurchasedSubscription testPurchasedSubscription = purchasedSubscriptionList.get(purchasedSubscriptionList.size() - 1);
        assertThat(testPurchasedSubscription.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testPurchasedSubscription.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testPurchasedSubscription.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testPurchasedSubscription.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testPurchasedSubscription.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
        assertThat(testPurchasedSubscription.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testPurchasedSubscription.getSubscriptionStatus()).isEqualTo(UPDATED_SUBSCRIPTION_STATUS);
        assertThat(testPurchasedSubscription.getViewerId()).isEqualTo(DEFAULT_VIEWER_ID);
        assertThat(testPurchasedSubscription.getCreatorId()).isEqualTo(UPDATED_CREATOR_ID);
    }

    @Test
    @Transactional
    void fullUpdatePurchasedSubscriptionWithPatch() throws Exception {
        // Initialize the database
        purchasedSubscriptionRepository.saveAndFlush(purchasedSubscription);

        int databaseSizeBeforeUpdate = purchasedSubscriptionRepository.findAll().size();

        // Update the purchasedSubscription using partial update
        PurchasedSubscription partialUpdatedPurchasedSubscription = new PurchasedSubscription();
        partialUpdatedPurchasedSubscription.setId(purchasedSubscription.getId());

        partialUpdatedPurchasedSubscription
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .endDate(UPDATED_END_DATE)
            .subscriptionStatus(UPDATED_SUBSCRIPTION_STATUS)
            .viewerId(UPDATED_VIEWER_ID)
            .creatorId(UPDATED_CREATOR_ID);

        restPurchasedSubscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPurchasedSubscription.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPurchasedSubscription))
            )
            .andExpect(status().isOk());

        // Validate the PurchasedSubscription in the database
        List<PurchasedSubscription> purchasedSubscriptionList = purchasedSubscriptionRepository.findAll();
        assertThat(purchasedSubscriptionList).hasSize(databaseSizeBeforeUpdate);
        PurchasedSubscription testPurchasedSubscription = purchasedSubscriptionList.get(purchasedSubscriptionList.size() - 1);
        assertThat(testPurchasedSubscription.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testPurchasedSubscription.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testPurchasedSubscription.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testPurchasedSubscription.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testPurchasedSubscription.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testPurchasedSubscription.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testPurchasedSubscription.getSubscriptionStatus()).isEqualTo(UPDATED_SUBSCRIPTION_STATUS);
        assertThat(testPurchasedSubscription.getViewerId()).isEqualTo(UPDATED_VIEWER_ID);
        assertThat(testPurchasedSubscription.getCreatorId()).isEqualTo(UPDATED_CREATOR_ID);
    }

    @Test
    @Transactional
    void patchNonExistingPurchasedSubscription() throws Exception {
        int databaseSizeBeforeUpdate = purchasedSubscriptionRepository.findAll().size();
        purchasedSubscription.setId(longCount.incrementAndGet());

        // Create the PurchasedSubscription
        PurchasedSubscriptionDTO purchasedSubscriptionDTO = purchasedSubscriptionMapper.toDto(purchasedSubscription);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPurchasedSubscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, purchasedSubscriptionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(purchasedSubscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchasedSubscription in the database
        List<PurchasedSubscription> purchasedSubscriptionList = purchasedSubscriptionRepository.findAll();
        assertThat(purchasedSubscriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPurchasedSubscription() throws Exception {
        int databaseSizeBeforeUpdate = purchasedSubscriptionRepository.findAll().size();
        purchasedSubscription.setId(longCount.incrementAndGet());

        // Create the PurchasedSubscription
        PurchasedSubscriptionDTO purchasedSubscriptionDTO = purchasedSubscriptionMapper.toDto(purchasedSubscription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPurchasedSubscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(purchasedSubscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchasedSubscription in the database
        List<PurchasedSubscription> purchasedSubscriptionList = purchasedSubscriptionRepository.findAll();
        assertThat(purchasedSubscriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPurchasedSubscription() throws Exception {
        int databaseSizeBeforeUpdate = purchasedSubscriptionRepository.findAll().size();
        purchasedSubscription.setId(longCount.incrementAndGet());

        // Create the PurchasedSubscription
        PurchasedSubscriptionDTO purchasedSubscriptionDTO = purchasedSubscriptionMapper.toDto(purchasedSubscription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPurchasedSubscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(purchasedSubscriptionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PurchasedSubscription in the database
        List<PurchasedSubscription> purchasedSubscriptionList = purchasedSubscriptionRepository.findAll();
        assertThat(purchasedSubscriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePurchasedSubscription() throws Exception {
        // Initialize the database
        purchasedSubscriptionRepository.saveAndFlush(purchasedSubscription);

        int databaseSizeBeforeDelete = purchasedSubscriptionRepository.findAll().size();

        // Delete the purchasedSubscription
        restPurchasedSubscriptionMockMvc
            .perform(delete(ENTITY_API_URL_ID, purchasedSubscription.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PurchasedSubscription> purchasedSubscriptionList = purchasedSubscriptionRepository.findAll();
        assertThat(purchasedSubscriptionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
