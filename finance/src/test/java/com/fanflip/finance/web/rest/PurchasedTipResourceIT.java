package com.monsterdam.finance.web.rest;

import static com.monsterdam.finance.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.monsterdam.finance.IntegrationTest;
import com.monsterdam.finance.domain.CreatorEarning;
import com.monsterdam.finance.domain.PurchasedTip;
import com.monsterdam.finance.repository.PurchasedTipRepository;
import com.monsterdam.finance.service.PurchasedTipService;
import com.monsterdam.finance.service.dto.PurchasedTipDTO;
import com.monsterdam.finance.service.mapper.PurchasedTipMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Instant;
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
 * Integration tests for the {@link PurchasedTipResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PurchasedTipResourceIT {

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);

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

    private static final Long DEFAULT_MESSAGE_ID = 1L;
    private static final Long UPDATED_MESSAGE_ID = 2L;

    private static final String ENTITY_API_URL = "/api/purchased-tips";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PurchasedTipRepository purchasedTipRepository;

    @Mock
    private PurchasedTipRepository purchasedTipRepositoryMock;

    @Autowired
    private PurchasedTipMapper purchasedTipMapper;

    @Mock
    private PurchasedTipService purchasedTipServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPurchasedTipMockMvc;

    private PurchasedTip purchasedTip;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PurchasedTip createEntity(EntityManager em) {
        PurchasedTip purchasedTip = new PurchasedTip()
            .amount(DEFAULT_AMOUNT)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .isDeleted(DEFAULT_IS_DELETED)
            .messageId(DEFAULT_MESSAGE_ID);
        // Add required entity
        CreatorEarning creatorEarning;
        if (TestUtil.findAll(em, CreatorEarning.class).isEmpty()) {
            creatorEarning = CreatorEarningResourceIT.createEntity(em);
            em.persist(creatorEarning);
            em.flush();
        } else {
            creatorEarning = TestUtil.findAll(em, CreatorEarning.class).get(0);
        }
        purchasedTip.setCreatorEarning(creatorEarning);
        return purchasedTip;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PurchasedTip createUpdatedEntity(EntityManager em) {
        PurchasedTip purchasedTip = new PurchasedTip()
            .amount(UPDATED_AMOUNT)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .messageId(UPDATED_MESSAGE_ID);
        // Add required entity
        CreatorEarning creatorEarning;
        if (TestUtil.findAll(em, CreatorEarning.class).isEmpty()) {
            creatorEarning = CreatorEarningResourceIT.createUpdatedEntity(em);
            em.persist(creatorEarning);
            em.flush();
        } else {
            creatorEarning = TestUtil.findAll(em, CreatorEarning.class).get(0);
        }
        purchasedTip.setCreatorEarning(creatorEarning);
        return purchasedTip;
    }

    @BeforeEach
    public void initTest() {
        purchasedTip = createEntity(em);
    }

    @Test
    @Transactional
    void createPurchasedTip() throws Exception {
        int databaseSizeBeforeCreate = purchasedTipRepository.findAll().size();
        // Create the PurchasedTip
        PurchasedTipDTO purchasedTipDTO = purchasedTipMapper.toDto(purchasedTip);
        restPurchasedTipMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(purchasedTipDTO))
            )
            .andExpect(status().isCreated());

        // Validate the PurchasedTip in the database
        List<PurchasedTip> purchasedTipList = purchasedTipRepository.findAll();
        assertThat(purchasedTipList).hasSize(databaseSizeBeforeCreate + 1);
        PurchasedTip testPurchasedTip = purchasedTipList.get(purchasedTipList.size() - 1);
        assertThat(testPurchasedTip.getAmount()).isEqualByComparingTo(DEFAULT_AMOUNT);
        assertThat(testPurchasedTip.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testPurchasedTip.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testPurchasedTip.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testPurchasedTip.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testPurchasedTip.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
        assertThat(testPurchasedTip.getMessageId()).isEqualTo(DEFAULT_MESSAGE_ID);
    }

    @Test
    @Transactional
    void createPurchasedTipWithExistingId() throws Exception {
        // Create the PurchasedTip with an existing ID
        purchasedTip.setId(1L);
        PurchasedTipDTO purchasedTipDTO = purchasedTipMapper.toDto(purchasedTip);

        int databaseSizeBeforeCreate = purchasedTipRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPurchasedTipMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(purchasedTipDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchasedTip in the database
        List<PurchasedTip> purchasedTipList = purchasedTipRepository.findAll();
        assertThat(purchasedTipList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = purchasedTipRepository.findAll().size();
        // set the field null
        purchasedTip.setAmount(null);

        // Create the PurchasedTip, which fails.
        PurchasedTipDTO purchasedTipDTO = purchasedTipMapper.toDto(purchasedTip);

        restPurchasedTipMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(purchasedTipDTO))
            )
            .andExpect(status().isBadRequest());

        List<PurchasedTip> purchasedTipList = purchasedTipRepository.findAll();
        assertThat(purchasedTipList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = purchasedTipRepository.findAll().size();
        // set the field null
        purchasedTip.setCreatedDate(null);

        // Create the PurchasedTip, which fails.
        PurchasedTipDTO purchasedTipDTO = purchasedTipMapper.toDto(purchasedTip);

        restPurchasedTipMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(purchasedTipDTO))
            )
            .andExpect(status().isBadRequest());

        List<PurchasedTip> purchasedTipList = purchasedTipRepository.findAll();
        assertThat(purchasedTipList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = purchasedTipRepository.findAll().size();
        // set the field null
        purchasedTip.setIsDeleted(null);

        // Create the PurchasedTip, which fails.
        PurchasedTipDTO purchasedTipDTO = purchasedTipMapper.toDto(purchasedTip);

        restPurchasedTipMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(purchasedTipDTO))
            )
            .andExpect(status().isBadRequest());

        List<PurchasedTip> purchasedTipList = purchasedTipRepository.findAll();
        assertThat(purchasedTipList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPurchasedTips() throws Exception {
        // Initialize the database
        purchasedTipRepository.saveAndFlush(purchasedTip);

        // Get all the purchasedTipList
        restPurchasedTipMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(purchasedTip.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())))
            .andExpect(jsonPath("$.[*].messageId").value(hasItem(DEFAULT_MESSAGE_ID.intValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPurchasedTipsWithEagerRelationshipsIsEnabled() throws Exception {
        when(purchasedTipServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPurchasedTipMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(purchasedTipServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPurchasedTipsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(purchasedTipServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPurchasedTipMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(purchasedTipRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPurchasedTip() throws Exception {
        // Initialize the database
        purchasedTipRepository.saveAndFlush(purchasedTip);

        // Get the purchasedTip
        restPurchasedTipMockMvc
            .perform(get(ENTITY_API_URL_ID, purchasedTip.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(purchasedTip.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()))
            .andExpect(jsonPath("$.messageId").value(DEFAULT_MESSAGE_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingPurchasedTip() throws Exception {
        // Get the purchasedTip
        restPurchasedTipMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPurchasedTip() throws Exception {
        // Initialize the database
        purchasedTipRepository.saveAndFlush(purchasedTip);

        int databaseSizeBeforeUpdate = purchasedTipRepository.findAll().size();

        // Update the purchasedTip
        PurchasedTip updatedPurchasedTip = purchasedTipRepository.findById(purchasedTip.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPurchasedTip are not directly saved in db
        em.detach(updatedPurchasedTip);
        updatedPurchasedTip
            .amount(UPDATED_AMOUNT)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .messageId(UPDATED_MESSAGE_ID);
        PurchasedTipDTO purchasedTipDTO = purchasedTipMapper.toDto(updatedPurchasedTip);

        restPurchasedTipMockMvc
            .perform(
                put(ENTITY_API_URL_ID, purchasedTipDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(purchasedTipDTO))
            )
            .andExpect(status().isOk());

        // Validate the PurchasedTip in the database
        List<PurchasedTip> purchasedTipList = purchasedTipRepository.findAll();
        assertThat(purchasedTipList).hasSize(databaseSizeBeforeUpdate);
        PurchasedTip testPurchasedTip = purchasedTipList.get(purchasedTipList.size() - 1);
        assertThat(testPurchasedTip.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
        assertThat(testPurchasedTip.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testPurchasedTip.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testPurchasedTip.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testPurchasedTip.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testPurchasedTip.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testPurchasedTip.getMessageId()).isEqualTo(UPDATED_MESSAGE_ID);
    }

    @Test
    @Transactional
    void putNonExistingPurchasedTip() throws Exception {
        int databaseSizeBeforeUpdate = purchasedTipRepository.findAll().size();
        purchasedTip.setId(longCount.incrementAndGet());

        // Create the PurchasedTip
        PurchasedTipDTO purchasedTipDTO = purchasedTipMapper.toDto(purchasedTip);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPurchasedTipMockMvc
            .perform(
                put(ENTITY_API_URL_ID, purchasedTipDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(purchasedTipDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchasedTip in the database
        List<PurchasedTip> purchasedTipList = purchasedTipRepository.findAll();
        assertThat(purchasedTipList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPurchasedTip() throws Exception {
        int databaseSizeBeforeUpdate = purchasedTipRepository.findAll().size();
        purchasedTip.setId(longCount.incrementAndGet());

        // Create the PurchasedTip
        PurchasedTipDTO purchasedTipDTO = purchasedTipMapper.toDto(purchasedTip);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPurchasedTipMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(purchasedTipDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchasedTip in the database
        List<PurchasedTip> purchasedTipList = purchasedTipRepository.findAll();
        assertThat(purchasedTipList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPurchasedTip() throws Exception {
        int databaseSizeBeforeUpdate = purchasedTipRepository.findAll().size();
        purchasedTip.setId(longCount.incrementAndGet());

        // Create the PurchasedTip
        PurchasedTipDTO purchasedTipDTO = purchasedTipMapper.toDto(purchasedTip);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPurchasedTipMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(purchasedTipDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PurchasedTip in the database
        List<PurchasedTip> purchasedTipList = purchasedTipRepository.findAll();
        assertThat(purchasedTipList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePurchasedTipWithPatch() throws Exception {
        // Initialize the database
        purchasedTipRepository.saveAndFlush(purchasedTip);

        int databaseSizeBeforeUpdate = purchasedTipRepository.findAll().size();

        // Update the purchasedTip using partial update
        PurchasedTip partialUpdatedPurchasedTip = new PurchasedTip();
        partialUpdatedPurchasedTip.setId(purchasedTip.getId());

        partialUpdatedPurchasedTip
            .amount(UPDATED_AMOUNT)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .isDeleted(UPDATED_IS_DELETED);

        restPurchasedTipMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPurchasedTip.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPurchasedTip))
            )
            .andExpect(status().isOk());

        // Validate the PurchasedTip in the database
        List<PurchasedTip> purchasedTipList = purchasedTipRepository.findAll();
        assertThat(purchasedTipList).hasSize(databaseSizeBeforeUpdate);
        PurchasedTip testPurchasedTip = purchasedTipList.get(purchasedTipList.size() - 1);
        assertThat(testPurchasedTip.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
        assertThat(testPurchasedTip.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testPurchasedTip.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testPurchasedTip.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testPurchasedTip.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testPurchasedTip.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testPurchasedTip.getMessageId()).isEqualTo(DEFAULT_MESSAGE_ID);
    }

    @Test
    @Transactional
    void fullUpdatePurchasedTipWithPatch() throws Exception {
        // Initialize the database
        purchasedTipRepository.saveAndFlush(purchasedTip);

        int databaseSizeBeforeUpdate = purchasedTipRepository.findAll().size();

        // Update the purchasedTip using partial update
        PurchasedTip partialUpdatedPurchasedTip = new PurchasedTip();
        partialUpdatedPurchasedTip.setId(purchasedTip.getId());

        partialUpdatedPurchasedTip
            .amount(UPDATED_AMOUNT)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .messageId(UPDATED_MESSAGE_ID);

        restPurchasedTipMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPurchasedTip.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPurchasedTip))
            )
            .andExpect(status().isOk());

        // Validate the PurchasedTip in the database
        List<PurchasedTip> purchasedTipList = purchasedTipRepository.findAll();
        assertThat(purchasedTipList).hasSize(databaseSizeBeforeUpdate);
        PurchasedTip testPurchasedTip = purchasedTipList.get(purchasedTipList.size() - 1);
        assertThat(testPurchasedTip.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
        assertThat(testPurchasedTip.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testPurchasedTip.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testPurchasedTip.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testPurchasedTip.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testPurchasedTip.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testPurchasedTip.getMessageId()).isEqualTo(UPDATED_MESSAGE_ID);
    }

    @Test
    @Transactional
    void patchNonExistingPurchasedTip() throws Exception {
        int databaseSizeBeforeUpdate = purchasedTipRepository.findAll().size();
        purchasedTip.setId(longCount.incrementAndGet());

        // Create the PurchasedTip
        PurchasedTipDTO purchasedTipDTO = purchasedTipMapper.toDto(purchasedTip);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPurchasedTipMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, purchasedTipDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(purchasedTipDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchasedTip in the database
        List<PurchasedTip> purchasedTipList = purchasedTipRepository.findAll();
        assertThat(purchasedTipList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPurchasedTip() throws Exception {
        int databaseSizeBeforeUpdate = purchasedTipRepository.findAll().size();
        purchasedTip.setId(longCount.incrementAndGet());

        // Create the PurchasedTip
        PurchasedTipDTO purchasedTipDTO = purchasedTipMapper.toDto(purchasedTip);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPurchasedTipMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(purchasedTipDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchasedTip in the database
        List<PurchasedTip> purchasedTipList = purchasedTipRepository.findAll();
        assertThat(purchasedTipList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPurchasedTip() throws Exception {
        int databaseSizeBeforeUpdate = purchasedTipRepository.findAll().size();
        purchasedTip.setId(longCount.incrementAndGet());

        // Create the PurchasedTip
        PurchasedTipDTO purchasedTipDTO = purchasedTipMapper.toDto(purchasedTip);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPurchasedTipMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(purchasedTipDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PurchasedTip in the database
        List<PurchasedTip> purchasedTipList = purchasedTipRepository.findAll();
        assertThat(purchasedTipList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePurchasedTip() throws Exception {
        // Initialize the database
        purchasedTipRepository.saveAndFlush(purchasedTip);

        int databaseSizeBeforeDelete = purchasedTipRepository.findAll().size();

        // Delete the purchasedTip
        restPurchasedTipMockMvc
            .perform(delete(ENTITY_API_URL_ID, purchasedTip.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PurchasedTip> purchasedTipList = purchasedTipRepository.findAll();
        assertThat(purchasedTipList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
