package com.monsterdam.finance.web.rest;

import static com.monsterdam.finance.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.monsterdam.finance.IntegrationTest;
import com.monsterdam.finance.domain.CreatorEarning;
import com.monsterdam.finance.domain.MoneyPayout;
import com.monsterdam.finance.domain.enumeration.PayoutStatus;
import com.monsterdam.finance.repository.MoneyPayoutRepository;
import com.monsterdam.finance.service.MoneyPayoutService;
import com.monsterdam.finance.service.dto.MoneyPayoutDTO;
import com.monsterdam.finance.service.mapper.MoneyPayoutMapper;
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
 * Integration tests for the {@link MoneyPayoutResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class MoneyPayoutResourceIT {

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

    private static final PayoutStatus DEFAULT_WITHDRAW_STATUS = PayoutStatus.WITHDRAW_PENDING;
    private static final PayoutStatus UPDATED_WITHDRAW_STATUS = PayoutStatus.WITHDRAW_PROCESSED;

    private static final Long DEFAULT_CREATOR_ID = 1L;
    private static final Long UPDATED_CREATOR_ID = 2L;

    private static final String ENTITY_API_URL = "/api/money-payouts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MoneyPayoutRepository moneyPayoutRepository;

    @Mock
    private MoneyPayoutRepository moneyPayoutRepositoryMock;

    @Autowired
    private MoneyPayoutMapper moneyPayoutMapper;

    @Mock
    private MoneyPayoutService moneyPayoutServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMoneyPayoutMockMvc;

    private MoneyPayout moneyPayout;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MoneyPayout createEntity(EntityManager em) {
        MoneyPayout moneyPayout = new MoneyPayout()
            .amount(DEFAULT_AMOUNT)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .isDeleted(DEFAULT_IS_DELETED)
            .withdrawStatus(DEFAULT_WITHDRAW_STATUS)
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
        moneyPayout.setCreatorEarning(creatorEarning);
        return moneyPayout;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MoneyPayout createUpdatedEntity(EntityManager em) {
        MoneyPayout moneyPayout = new MoneyPayout()
            .amount(UPDATED_AMOUNT)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .withdrawStatus(UPDATED_WITHDRAW_STATUS)
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
        moneyPayout.setCreatorEarning(creatorEarning);
        return moneyPayout;
    }

    @BeforeEach
    public void initTest() {
        moneyPayout = createEntity(em);
    }

    @Test
    @Transactional
    void createMoneyPayout() throws Exception {
        int databaseSizeBeforeCreate = moneyPayoutRepository.findAll().size();
        // Create the MoneyPayout
        MoneyPayoutDTO moneyPayoutDTO = moneyPayoutMapper.toDto(moneyPayout);
        restMoneyPayoutMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(moneyPayoutDTO))
            )
            .andExpect(status().isCreated());

        // Validate the MoneyPayout in the database
        List<MoneyPayout> moneyPayoutList = moneyPayoutRepository.findAll();
        assertThat(moneyPayoutList).hasSize(databaseSizeBeforeCreate + 1);
        MoneyPayout testMoneyPayout = moneyPayoutList.get(moneyPayoutList.size() - 1);
        assertThat(testMoneyPayout.getAmount()).isEqualByComparingTo(DEFAULT_AMOUNT);
        assertThat(testMoneyPayout.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testMoneyPayout.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testMoneyPayout.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testMoneyPayout.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testMoneyPayout.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
        assertThat(testMoneyPayout.getWithdrawStatus()).isEqualTo(DEFAULT_WITHDRAW_STATUS);
        assertThat(testMoneyPayout.getCreatorId()).isEqualTo(DEFAULT_CREATOR_ID);
    }

    @Test
    @Transactional
    void createMoneyPayoutWithExistingId() throws Exception {
        // Create the MoneyPayout with an existing ID
        moneyPayout.setId(1L);
        MoneyPayoutDTO moneyPayoutDTO = moneyPayoutMapper.toDto(moneyPayout);

        int databaseSizeBeforeCreate = moneyPayoutRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMoneyPayoutMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(moneyPayoutDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MoneyPayout in the database
        List<MoneyPayout> moneyPayoutList = moneyPayoutRepository.findAll();
        assertThat(moneyPayoutList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = moneyPayoutRepository.findAll().size();
        // set the field null
        moneyPayout.setAmount(null);

        // Create the MoneyPayout, which fails.
        MoneyPayoutDTO moneyPayoutDTO = moneyPayoutMapper.toDto(moneyPayout);

        restMoneyPayoutMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(moneyPayoutDTO))
            )
            .andExpect(status().isBadRequest());

        List<MoneyPayout> moneyPayoutList = moneyPayoutRepository.findAll();
        assertThat(moneyPayoutList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = moneyPayoutRepository.findAll().size();
        // set the field null
        moneyPayout.setCreatedDate(null);

        // Create the MoneyPayout, which fails.
        MoneyPayoutDTO moneyPayoutDTO = moneyPayoutMapper.toDto(moneyPayout);

        restMoneyPayoutMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(moneyPayoutDTO))
            )
            .andExpect(status().isBadRequest());

        List<MoneyPayout> moneyPayoutList = moneyPayoutRepository.findAll();
        assertThat(moneyPayoutList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = moneyPayoutRepository.findAll().size();
        // set the field null
        moneyPayout.setIsDeleted(null);

        // Create the MoneyPayout, which fails.
        MoneyPayoutDTO moneyPayoutDTO = moneyPayoutMapper.toDto(moneyPayout);

        restMoneyPayoutMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(moneyPayoutDTO))
            )
            .andExpect(status().isBadRequest());

        List<MoneyPayout> moneyPayoutList = moneyPayoutRepository.findAll();
        assertThat(moneyPayoutList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkWithdrawStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = moneyPayoutRepository.findAll().size();
        // set the field null
        moneyPayout.setWithdrawStatus(null);

        // Create the MoneyPayout, which fails.
        MoneyPayoutDTO moneyPayoutDTO = moneyPayoutMapper.toDto(moneyPayout);

        restMoneyPayoutMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(moneyPayoutDTO))
            )
            .andExpect(status().isBadRequest());

        List<MoneyPayout> moneyPayoutList = moneyPayoutRepository.findAll();
        assertThat(moneyPayoutList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatorIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = moneyPayoutRepository.findAll().size();
        // set the field null
        moneyPayout.setCreatorId(null);

        // Create the MoneyPayout, which fails.
        MoneyPayoutDTO moneyPayoutDTO = moneyPayoutMapper.toDto(moneyPayout);

        restMoneyPayoutMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(moneyPayoutDTO))
            )
            .andExpect(status().isBadRequest());

        List<MoneyPayout> moneyPayoutList = moneyPayoutRepository.findAll();
        assertThat(moneyPayoutList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMoneyPayouts() throws Exception {
        // Initialize the database
        moneyPayoutRepository.saveAndFlush(moneyPayout);

        // Get all the moneyPayoutList
        restMoneyPayoutMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(moneyPayout.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())))
            .andExpect(jsonPath("$.[*].withdrawStatus").value(hasItem(DEFAULT_WITHDRAW_STATUS.toString())))
            .andExpect(jsonPath("$.[*].creatorId").value(hasItem(DEFAULT_CREATOR_ID.intValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMoneyPayoutsWithEagerRelationshipsIsEnabled() throws Exception {
        when(moneyPayoutServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMoneyPayoutMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(moneyPayoutServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMoneyPayoutsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(moneyPayoutServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMoneyPayoutMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(moneyPayoutRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getMoneyPayout() throws Exception {
        // Initialize the database
        moneyPayoutRepository.saveAndFlush(moneyPayout);

        // Get the moneyPayout
        restMoneyPayoutMockMvc
            .perform(get(ENTITY_API_URL_ID, moneyPayout.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(moneyPayout.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()))
            .andExpect(jsonPath("$.withdrawStatus").value(DEFAULT_WITHDRAW_STATUS.toString()))
            .andExpect(jsonPath("$.creatorId").value(DEFAULT_CREATOR_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingMoneyPayout() throws Exception {
        // Get the moneyPayout
        restMoneyPayoutMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMoneyPayout() throws Exception {
        // Initialize the database
        moneyPayoutRepository.saveAndFlush(moneyPayout);

        int databaseSizeBeforeUpdate = moneyPayoutRepository.findAll().size();

        // Update the moneyPayout
        MoneyPayout updatedMoneyPayout = moneyPayoutRepository.findById(moneyPayout.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMoneyPayout are not directly saved in db
        em.detach(updatedMoneyPayout);
        updatedMoneyPayout
            .amount(UPDATED_AMOUNT)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .withdrawStatus(UPDATED_WITHDRAW_STATUS)
            .creatorId(UPDATED_CREATOR_ID);
        MoneyPayoutDTO moneyPayoutDTO = moneyPayoutMapper.toDto(updatedMoneyPayout);

        restMoneyPayoutMockMvc
            .perform(
                put(ENTITY_API_URL_ID, moneyPayoutDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(moneyPayoutDTO))
            )
            .andExpect(status().isOk());

        // Validate the MoneyPayout in the database
        List<MoneyPayout> moneyPayoutList = moneyPayoutRepository.findAll();
        assertThat(moneyPayoutList).hasSize(databaseSizeBeforeUpdate);
        MoneyPayout testMoneyPayout = moneyPayoutList.get(moneyPayoutList.size() - 1);
        assertThat(testMoneyPayout.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
        assertThat(testMoneyPayout.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testMoneyPayout.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testMoneyPayout.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testMoneyPayout.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testMoneyPayout.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testMoneyPayout.getWithdrawStatus()).isEqualTo(UPDATED_WITHDRAW_STATUS);
        assertThat(testMoneyPayout.getCreatorId()).isEqualTo(UPDATED_CREATOR_ID);
    }

    @Test
    @Transactional
    void putNonExistingMoneyPayout() throws Exception {
        int databaseSizeBeforeUpdate = moneyPayoutRepository.findAll().size();
        moneyPayout.setId(longCount.incrementAndGet());

        // Create the MoneyPayout
        MoneyPayoutDTO moneyPayoutDTO = moneyPayoutMapper.toDto(moneyPayout);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMoneyPayoutMockMvc
            .perform(
                put(ENTITY_API_URL_ID, moneyPayoutDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(moneyPayoutDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MoneyPayout in the database
        List<MoneyPayout> moneyPayoutList = moneyPayoutRepository.findAll();
        assertThat(moneyPayoutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMoneyPayout() throws Exception {
        int databaseSizeBeforeUpdate = moneyPayoutRepository.findAll().size();
        moneyPayout.setId(longCount.incrementAndGet());

        // Create the MoneyPayout
        MoneyPayoutDTO moneyPayoutDTO = moneyPayoutMapper.toDto(moneyPayout);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMoneyPayoutMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(moneyPayoutDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MoneyPayout in the database
        List<MoneyPayout> moneyPayoutList = moneyPayoutRepository.findAll();
        assertThat(moneyPayoutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMoneyPayout() throws Exception {
        int databaseSizeBeforeUpdate = moneyPayoutRepository.findAll().size();
        moneyPayout.setId(longCount.incrementAndGet());

        // Create the MoneyPayout
        MoneyPayoutDTO moneyPayoutDTO = moneyPayoutMapper.toDto(moneyPayout);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMoneyPayoutMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(moneyPayoutDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MoneyPayout in the database
        List<MoneyPayout> moneyPayoutList = moneyPayoutRepository.findAll();
        assertThat(moneyPayoutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMoneyPayoutWithPatch() throws Exception {
        // Initialize the database
        moneyPayoutRepository.saveAndFlush(moneyPayout);

        int databaseSizeBeforeUpdate = moneyPayoutRepository.findAll().size();

        // Update the moneyPayout using partial update
        MoneyPayout partialUpdatedMoneyPayout = new MoneyPayout();
        partialUpdatedMoneyPayout.setId(moneyPayout.getId());

        partialUpdatedMoneyPayout
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .withdrawStatus(UPDATED_WITHDRAW_STATUS);

        restMoneyPayoutMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMoneyPayout.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMoneyPayout))
            )
            .andExpect(status().isOk());

        // Validate the MoneyPayout in the database
        List<MoneyPayout> moneyPayoutList = moneyPayoutRepository.findAll();
        assertThat(moneyPayoutList).hasSize(databaseSizeBeforeUpdate);
        MoneyPayout testMoneyPayout = moneyPayoutList.get(moneyPayoutList.size() - 1);
        assertThat(testMoneyPayout.getAmount()).isEqualByComparingTo(DEFAULT_AMOUNT);
        assertThat(testMoneyPayout.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testMoneyPayout.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testMoneyPayout.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testMoneyPayout.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testMoneyPayout.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testMoneyPayout.getWithdrawStatus()).isEqualTo(UPDATED_WITHDRAW_STATUS);
        assertThat(testMoneyPayout.getCreatorId()).isEqualTo(DEFAULT_CREATOR_ID);
    }

    @Test
    @Transactional
    void fullUpdateMoneyPayoutWithPatch() throws Exception {
        // Initialize the database
        moneyPayoutRepository.saveAndFlush(moneyPayout);

        int databaseSizeBeforeUpdate = moneyPayoutRepository.findAll().size();

        // Update the moneyPayout using partial update
        MoneyPayout partialUpdatedMoneyPayout = new MoneyPayout();
        partialUpdatedMoneyPayout.setId(moneyPayout.getId());

        partialUpdatedMoneyPayout
            .amount(UPDATED_AMOUNT)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .withdrawStatus(UPDATED_WITHDRAW_STATUS)
            .creatorId(UPDATED_CREATOR_ID);

        restMoneyPayoutMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMoneyPayout.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMoneyPayout))
            )
            .andExpect(status().isOk());

        // Validate the MoneyPayout in the database
        List<MoneyPayout> moneyPayoutList = moneyPayoutRepository.findAll();
        assertThat(moneyPayoutList).hasSize(databaseSizeBeforeUpdate);
        MoneyPayout testMoneyPayout = moneyPayoutList.get(moneyPayoutList.size() - 1);
        assertThat(testMoneyPayout.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
        assertThat(testMoneyPayout.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testMoneyPayout.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testMoneyPayout.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testMoneyPayout.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testMoneyPayout.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testMoneyPayout.getWithdrawStatus()).isEqualTo(UPDATED_WITHDRAW_STATUS);
        assertThat(testMoneyPayout.getCreatorId()).isEqualTo(UPDATED_CREATOR_ID);
    }

    @Test
    @Transactional
    void patchNonExistingMoneyPayout() throws Exception {
        int databaseSizeBeforeUpdate = moneyPayoutRepository.findAll().size();
        moneyPayout.setId(longCount.incrementAndGet());

        // Create the MoneyPayout
        MoneyPayoutDTO moneyPayoutDTO = moneyPayoutMapper.toDto(moneyPayout);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMoneyPayoutMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, moneyPayoutDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(moneyPayoutDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MoneyPayout in the database
        List<MoneyPayout> moneyPayoutList = moneyPayoutRepository.findAll();
        assertThat(moneyPayoutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMoneyPayout() throws Exception {
        int databaseSizeBeforeUpdate = moneyPayoutRepository.findAll().size();
        moneyPayout.setId(longCount.incrementAndGet());

        // Create the MoneyPayout
        MoneyPayoutDTO moneyPayoutDTO = moneyPayoutMapper.toDto(moneyPayout);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMoneyPayoutMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(moneyPayoutDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MoneyPayout in the database
        List<MoneyPayout> moneyPayoutList = moneyPayoutRepository.findAll();
        assertThat(moneyPayoutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMoneyPayout() throws Exception {
        int databaseSizeBeforeUpdate = moneyPayoutRepository.findAll().size();
        moneyPayout.setId(longCount.incrementAndGet());

        // Create the MoneyPayout
        MoneyPayoutDTO moneyPayoutDTO = moneyPayoutMapper.toDto(moneyPayout);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMoneyPayoutMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(moneyPayoutDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MoneyPayout in the database
        List<MoneyPayout> moneyPayoutList = moneyPayoutRepository.findAll();
        assertThat(moneyPayoutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMoneyPayout() throws Exception {
        // Initialize the database
        moneyPayoutRepository.saveAndFlush(moneyPayout);

        int databaseSizeBeforeDelete = moneyPayoutRepository.findAll().size();

        // Delete the moneyPayout
        restMoneyPayoutMockMvc
            .perform(delete(ENTITY_API_URL_ID, moneyPayout.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MoneyPayout> moneyPayoutList = moneyPayoutRepository.findAll();
        assertThat(moneyPayoutList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
