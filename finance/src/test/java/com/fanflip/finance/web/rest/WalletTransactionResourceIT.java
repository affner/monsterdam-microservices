package com.fanflip.finance.web.rest;

import static com.fanflip.finance.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fanflip.finance.IntegrationTest;
import com.fanflip.finance.domain.WalletTransaction;
import com.fanflip.finance.domain.enumeration.WalletTransactionType;
import com.fanflip.finance.repository.WalletTransactionRepository;
import com.fanflip.finance.service.WalletTransactionService;
import com.fanflip.finance.service.dto.WalletTransactionDTO;
import com.fanflip.finance.service.mapper.WalletTransactionMapper;
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
 * Integration tests for the {@link WalletTransactionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class WalletTransactionResourceIT {

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final WalletTransactionType DEFAULT_TRANSACTION_TYPE = WalletTransactionType.TOP_UP;
    private static final WalletTransactionType UPDATED_TRANSACTION_TYPE = WalletTransactionType.PURCHASE;

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_DELETED = false;
    private static final Boolean UPDATED_IS_DELETED = true;

    private static final Long DEFAULT_VIEWER_ID = 1L;
    private static final Long UPDATED_VIEWER_ID = 2L;

    private static final String ENTITY_API_URL = "/api/wallet-transactions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WalletTransactionRepository walletTransactionRepository;

    @Mock
    private WalletTransactionRepository walletTransactionRepositoryMock;

    @Autowired
    private WalletTransactionMapper walletTransactionMapper;

    @Mock
    private WalletTransactionService walletTransactionServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWalletTransactionMockMvc;

    private WalletTransaction walletTransaction;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WalletTransaction createEntity(EntityManager em) {
        WalletTransaction walletTransaction = new WalletTransaction()
            .amount(DEFAULT_AMOUNT)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .transactionType(DEFAULT_TRANSACTION_TYPE)
            .createdDate(DEFAULT_CREATED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .isDeleted(DEFAULT_IS_DELETED)
            .viewerId(DEFAULT_VIEWER_ID);
        return walletTransaction;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WalletTransaction createUpdatedEntity(EntityManager em) {
        WalletTransaction walletTransaction = new WalletTransaction()
            .amount(UPDATED_AMOUNT)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .transactionType(UPDATED_TRANSACTION_TYPE)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .viewerId(UPDATED_VIEWER_ID);
        return walletTransaction;
    }

    @BeforeEach
    public void initTest() {
        walletTransaction = createEntity(em);
    }

    @Test
    @Transactional
    void createWalletTransaction() throws Exception {
        int databaseSizeBeforeCreate = walletTransactionRepository.findAll().size();
        // Create the WalletTransaction
        WalletTransactionDTO walletTransactionDTO = walletTransactionMapper.toDto(walletTransaction);
        restWalletTransactionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(walletTransactionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the WalletTransaction in the database
        List<WalletTransaction> walletTransactionList = walletTransactionRepository.findAll();
        assertThat(walletTransactionList).hasSize(databaseSizeBeforeCreate + 1);
        WalletTransaction testWalletTransaction = walletTransactionList.get(walletTransactionList.size() - 1);
        assertThat(testWalletTransaction.getAmount()).isEqualByComparingTo(DEFAULT_AMOUNT);
        assertThat(testWalletTransaction.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testWalletTransaction.getTransactionType()).isEqualTo(DEFAULT_TRANSACTION_TYPE);
        assertThat(testWalletTransaction.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testWalletTransaction.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testWalletTransaction.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testWalletTransaction.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
        assertThat(testWalletTransaction.getViewerId()).isEqualTo(DEFAULT_VIEWER_ID);
    }

    @Test
    @Transactional
    void createWalletTransactionWithExistingId() throws Exception {
        // Create the WalletTransaction with an existing ID
        walletTransaction.setId(1L);
        WalletTransactionDTO walletTransactionDTO = walletTransactionMapper.toDto(walletTransaction);

        int databaseSizeBeforeCreate = walletTransactionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWalletTransactionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(walletTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WalletTransaction in the database
        List<WalletTransaction> walletTransactionList = walletTransactionRepository.findAll();
        assertThat(walletTransactionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = walletTransactionRepository.findAll().size();
        // set the field null
        walletTransaction.setAmount(null);

        // Create the WalletTransaction, which fails.
        WalletTransactionDTO walletTransactionDTO = walletTransactionMapper.toDto(walletTransaction);

        restWalletTransactionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(walletTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        List<WalletTransaction> walletTransactionList = walletTransactionRepository.findAll();
        assertThat(walletTransactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTransactionTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = walletTransactionRepository.findAll().size();
        // set the field null
        walletTransaction.setTransactionType(null);

        // Create the WalletTransaction, which fails.
        WalletTransactionDTO walletTransactionDTO = walletTransactionMapper.toDto(walletTransaction);

        restWalletTransactionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(walletTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        List<WalletTransaction> walletTransactionList = walletTransactionRepository.findAll();
        assertThat(walletTransactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = walletTransactionRepository.findAll().size();
        // set the field null
        walletTransaction.setCreatedDate(null);

        // Create the WalletTransaction, which fails.
        WalletTransactionDTO walletTransactionDTO = walletTransactionMapper.toDto(walletTransaction);

        restWalletTransactionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(walletTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        List<WalletTransaction> walletTransactionList = walletTransactionRepository.findAll();
        assertThat(walletTransactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = walletTransactionRepository.findAll().size();
        // set the field null
        walletTransaction.setIsDeleted(null);

        // Create the WalletTransaction, which fails.
        WalletTransactionDTO walletTransactionDTO = walletTransactionMapper.toDto(walletTransaction);

        restWalletTransactionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(walletTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        List<WalletTransaction> walletTransactionList = walletTransactionRepository.findAll();
        assertThat(walletTransactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkViewerIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = walletTransactionRepository.findAll().size();
        // set the field null
        walletTransaction.setViewerId(null);

        // Create the WalletTransaction, which fails.
        WalletTransactionDTO walletTransactionDTO = walletTransactionMapper.toDto(walletTransaction);

        restWalletTransactionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(walletTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        List<WalletTransaction> walletTransactionList = walletTransactionRepository.findAll();
        assertThat(walletTransactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllWalletTransactions() throws Exception {
        // Initialize the database
        walletTransactionRepository.saveAndFlush(walletTransaction);

        // Get all the walletTransactionList
        restWalletTransactionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(walletTransaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].transactionType").value(hasItem(DEFAULT_TRANSACTION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())))
            .andExpect(jsonPath("$.[*].viewerId").value(hasItem(DEFAULT_VIEWER_ID.intValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllWalletTransactionsWithEagerRelationshipsIsEnabled() throws Exception {
        when(walletTransactionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restWalletTransactionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(walletTransactionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllWalletTransactionsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(walletTransactionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restWalletTransactionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(walletTransactionRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getWalletTransaction() throws Exception {
        // Initialize the database
        walletTransactionRepository.saveAndFlush(walletTransaction);

        // Get the walletTransaction
        restWalletTransactionMockMvc
            .perform(get(ENTITY_API_URL_ID, walletTransaction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(walletTransaction.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.transactionType").value(DEFAULT_TRANSACTION_TYPE.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()))
            .andExpect(jsonPath("$.viewerId").value(DEFAULT_VIEWER_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingWalletTransaction() throws Exception {
        // Get the walletTransaction
        restWalletTransactionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingWalletTransaction() throws Exception {
        // Initialize the database
        walletTransactionRepository.saveAndFlush(walletTransaction);

        int databaseSizeBeforeUpdate = walletTransactionRepository.findAll().size();

        // Update the walletTransaction
        WalletTransaction updatedWalletTransaction = walletTransactionRepository.findById(walletTransaction.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedWalletTransaction are not directly saved in db
        em.detach(updatedWalletTransaction);
        updatedWalletTransaction
            .amount(UPDATED_AMOUNT)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .transactionType(UPDATED_TRANSACTION_TYPE)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .viewerId(UPDATED_VIEWER_ID);
        WalletTransactionDTO walletTransactionDTO = walletTransactionMapper.toDto(updatedWalletTransaction);

        restWalletTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, walletTransactionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(walletTransactionDTO))
            )
            .andExpect(status().isOk());

        // Validate the WalletTransaction in the database
        List<WalletTransaction> walletTransactionList = walletTransactionRepository.findAll();
        assertThat(walletTransactionList).hasSize(databaseSizeBeforeUpdate);
        WalletTransaction testWalletTransaction = walletTransactionList.get(walletTransactionList.size() - 1);
        assertThat(testWalletTransaction.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
        assertThat(testWalletTransaction.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testWalletTransaction.getTransactionType()).isEqualTo(UPDATED_TRANSACTION_TYPE);
        assertThat(testWalletTransaction.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testWalletTransaction.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testWalletTransaction.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testWalletTransaction.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testWalletTransaction.getViewerId()).isEqualTo(UPDATED_VIEWER_ID);
    }

    @Test
    @Transactional
    void putNonExistingWalletTransaction() throws Exception {
        int databaseSizeBeforeUpdate = walletTransactionRepository.findAll().size();
        walletTransaction.setId(longCount.incrementAndGet());

        // Create the WalletTransaction
        WalletTransactionDTO walletTransactionDTO = walletTransactionMapper.toDto(walletTransaction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWalletTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, walletTransactionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(walletTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WalletTransaction in the database
        List<WalletTransaction> walletTransactionList = walletTransactionRepository.findAll();
        assertThat(walletTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWalletTransaction() throws Exception {
        int databaseSizeBeforeUpdate = walletTransactionRepository.findAll().size();
        walletTransaction.setId(longCount.incrementAndGet());

        // Create the WalletTransaction
        WalletTransactionDTO walletTransactionDTO = walletTransactionMapper.toDto(walletTransaction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWalletTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(walletTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WalletTransaction in the database
        List<WalletTransaction> walletTransactionList = walletTransactionRepository.findAll();
        assertThat(walletTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWalletTransaction() throws Exception {
        int databaseSizeBeforeUpdate = walletTransactionRepository.findAll().size();
        walletTransaction.setId(longCount.incrementAndGet());

        // Create the WalletTransaction
        WalletTransactionDTO walletTransactionDTO = walletTransactionMapper.toDto(walletTransaction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWalletTransactionMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(walletTransactionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WalletTransaction in the database
        List<WalletTransaction> walletTransactionList = walletTransactionRepository.findAll();
        assertThat(walletTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWalletTransactionWithPatch() throws Exception {
        // Initialize the database
        walletTransactionRepository.saveAndFlush(walletTransaction);

        int databaseSizeBeforeUpdate = walletTransactionRepository.findAll().size();

        // Update the walletTransaction using partial update
        WalletTransaction partialUpdatedWalletTransaction = new WalletTransaction();
        partialUpdatedWalletTransaction.setId(walletTransaction.getId());

        partialUpdatedWalletTransaction
            .transactionType(UPDATED_TRANSACTION_TYPE)
            .createdBy(UPDATED_CREATED_BY)
            .isDeleted(UPDATED_IS_DELETED);

        restWalletTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWalletTransaction.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWalletTransaction))
            )
            .andExpect(status().isOk());

        // Validate the WalletTransaction in the database
        List<WalletTransaction> walletTransactionList = walletTransactionRepository.findAll();
        assertThat(walletTransactionList).hasSize(databaseSizeBeforeUpdate);
        WalletTransaction testWalletTransaction = walletTransactionList.get(walletTransactionList.size() - 1);
        assertThat(testWalletTransaction.getAmount()).isEqualByComparingTo(DEFAULT_AMOUNT);
        assertThat(testWalletTransaction.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testWalletTransaction.getTransactionType()).isEqualTo(UPDATED_TRANSACTION_TYPE);
        assertThat(testWalletTransaction.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testWalletTransaction.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testWalletTransaction.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testWalletTransaction.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testWalletTransaction.getViewerId()).isEqualTo(DEFAULT_VIEWER_ID);
    }

    @Test
    @Transactional
    void fullUpdateWalletTransactionWithPatch() throws Exception {
        // Initialize the database
        walletTransactionRepository.saveAndFlush(walletTransaction);

        int databaseSizeBeforeUpdate = walletTransactionRepository.findAll().size();

        // Update the walletTransaction using partial update
        WalletTransaction partialUpdatedWalletTransaction = new WalletTransaction();
        partialUpdatedWalletTransaction.setId(walletTransaction.getId());

        partialUpdatedWalletTransaction
            .amount(UPDATED_AMOUNT)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .transactionType(UPDATED_TRANSACTION_TYPE)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .viewerId(UPDATED_VIEWER_ID);

        restWalletTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWalletTransaction.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWalletTransaction))
            )
            .andExpect(status().isOk());

        // Validate the WalletTransaction in the database
        List<WalletTransaction> walletTransactionList = walletTransactionRepository.findAll();
        assertThat(walletTransactionList).hasSize(databaseSizeBeforeUpdate);
        WalletTransaction testWalletTransaction = walletTransactionList.get(walletTransactionList.size() - 1);
        assertThat(testWalletTransaction.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
        assertThat(testWalletTransaction.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testWalletTransaction.getTransactionType()).isEqualTo(UPDATED_TRANSACTION_TYPE);
        assertThat(testWalletTransaction.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testWalletTransaction.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testWalletTransaction.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testWalletTransaction.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testWalletTransaction.getViewerId()).isEqualTo(UPDATED_VIEWER_ID);
    }

    @Test
    @Transactional
    void patchNonExistingWalletTransaction() throws Exception {
        int databaseSizeBeforeUpdate = walletTransactionRepository.findAll().size();
        walletTransaction.setId(longCount.incrementAndGet());

        // Create the WalletTransaction
        WalletTransactionDTO walletTransactionDTO = walletTransactionMapper.toDto(walletTransaction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWalletTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, walletTransactionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(walletTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WalletTransaction in the database
        List<WalletTransaction> walletTransactionList = walletTransactionRepository.findAll();
        assertThat(walletTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWalletTransaction() throws Exception {
        int databaseSizeBeforeUpdate = walletTransactionRepository.findAll().size();
        walletTransaction.setId(longCount.incrementAndGet());

        // Create the WalletTransaction
        WalletTransactionDTO walletTransactionDTO = walletTransactionMapper.toDto(walletTransaction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWalletTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(walletTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WalletTransaction in the database
        List<WalletTransaction> walletTransactionList = walletTransactionRepository.findAll();
        assertThat(walletTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWalletTransaction() throws Exception {
        int databaseSizeBeforeUpdate = walletTransactionRepository.findAll().size();
        walletTransaction.setId(longCount.incrementAndGet());

        // Create the WalletTransaction
        WalletTransactionDTO walletTransactionDTO = walletTransactionMapper.toDto(walletTransaction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWalletTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(walletTransactionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WalletTransaction in the database
        List<WalletTransaction> walletTransactionList = walletTransactionRepository.findAll();
        assertThat(walletTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWalletTransaction() throws Exception {
        // Initialize the database
        walletTransactionRepository.saveAndFlush(walletTransaction);

        int databaseSizeBeforeDelete = walletTransactionRepository.findAll().size();

        // Delete the walletTransaction
        restWalletTransactionMockMvc
            .perform(delete(ENTITY_API_URL_ID, walletTransaction.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WalletTransaction> walletTransactionList = walletTransactionRepository.findAll();
        assertThat(walletTransactionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
