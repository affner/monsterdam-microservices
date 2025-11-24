package com.monsterdam.finance.web.rest;

import static com.monsterdam.finance.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.monsterdam.finance.IntegrationTest;
import com.monsterdam.finance.domain.PaymentTransaction;
import com.monsterdam.finance.domain.enumeration.GenericStatus;
import com.monsterdam.finance.repository.PaymentTransactionRepository;
import com.monsterdam.finance.service.dto.PaymentTransactionDTO;
import com.monsterdam.finance.service.mapper.PaymentTransactionMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
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
 * Integration tests for the {@link PaymentTransactionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PaymentTransactionResourceIT {

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);

    private static final Instant DEFAULT_PAYMENT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PAYMENT_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final GenericStatus DEFAULT_PAYMENT_STATUS = GenericStatus.PENDING;
    private static final GenericStatus UPDATED_PAYMENT_STATUS = GenericStatus.COMPLETED;

    private static final String DEFAULT_PAYMENT_REFERENCE = "AAAAAAAAAA";
    private static final String UPDATED_PAYMENT_REFERENCE = "BBBBBBBBBB";

    private static final String DEFAULT_CLOUD_TRANSACTION_ID = "AAAAAAAAAA";
    private static final String UPDATED_CLOUD_TRANSACTION_ID = "BBBBBBBBBB";

    private static final Long DEFAULT_VIEWER_ID = 1L;
    private static final Long UPDATED_VIEWER_ID = 2L;

    private static final Long DEFAULT_PAYMENT_METHOD_ID = 1L;
    private static final Long UPDATED_PAYMENT_METHOD_ID = 2L;

    private static final Long DEFAULT_PAYMENT_PROVIDER_ID = 1L;
    private static final Long UPDATED_PAYMENT_PROVIDER_ID = 2L;

    private static final String ENTITY_API_URL = "/api/payment-transactions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PaymentTransactionRepository paymentTransactionRepository;

    @Autowired
    private PaymentTransactionMapper paymentTransactionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPaymentTransactionMockMvc;

    private PaymentTransaction paymentTransaction;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentTransaction createEntity(EntityManager em) {
        PaymentTransaction paymentTransaction = new PaymentTransaction()
            .amount(DEFAULT_AMOUNT)
            .paymentDate(DEFAULT_PAYMENT_DATE)
            .paymentStatus(DEFAULT_PAYMENT_STATUS)
            .paymentReference(DEFAULT_PAYMENT_REFERENCE)
            .cloudTransactionId(DEFAULT_CLOUD_TRANSACTION_ID)
            .viewerId(DEFAULT_VIEWER_ID)
            .paymentMethodId(DEFAULT_PAYMENT_METHOD_ID)
            .paymentProviderId(DEFAULT_PAYMENT_PROVIDER_ID);
        return paymentTransaction;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentTransaction createUpdatedEntity(EntityManager em) {
        PaymentTransaction paymentTransaction = new PaymentTransaction()
            .amount(UPDATED_AMOUNT)
            .paymentDate(UPDATED_PAYMENT_DATE)
            .paymentStatus(UPDATED_PAYMENT_STATUS)
            .paymentReference(UPDATED_PAYMENT_REFERENCE)
            .cloudTransactionId(UPDATED_CLOUD_TRANSACTION_ID)
            .viewerId(UPDATED_VIEWER_ID)
            .paymentMethodId(UPDATED_PAYMENT_METHOD_ID)
            .paymentProviderId(UPDATED_PAYMENT_PROVIDER_ID);
        return paymentTransaction;
    }

    @BeforeEach
    public void initTest() {
        paymentTransaction = createEntity(em);
    }

    @Test
    @Transactional
    void createPaymentTransaction() throws Exception {
        int databaseSizeBeforeCreate = paymentTransactionRepository.findAll().size();
        // Create the PaymentTransaction
        PaymentTransactionDTO paymentTransactionDTO = paymentTransactionMapper.toDto(paymentTransaction);
        restPaymentTransactionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paymentTransactionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the PaymentTransaction in the database
        List<PaymentTransaction> paymentTransactionList = paymentTransactionRepository.findAll();
        assertThat(paymentTransactionList).hasSize(databaseSizeBeforeCreate + 1);
        PaymentTransaction testPaymentTransaction = paymentTransactionList.get(paymentTransactionList.size() - 1);
        assertThat(testPaymentTransaction.getAmount()).isEqualByComparingTo(DEFAULT_AMOUNT);
        assertThat(testPaymentTransaction.getPaymentDate()).isEqualTo(DEFAULT_PAYMENT_DATE);
        assertThat(testPaymentTransaction.getPaymentStatus()).isEqualTo(DEFAULT_PAYMENT_STATUS);
        assertThat(testPaymentTransaction.getPaymentReference()).isEqualTo(DEFAULT_PAYMENT_REFERENCE);
        assertThat(testPaymentTransaction.getCloudTransactionId()).isEqualTo(DEFAULT_CLOUD_TRANSACTION_ID);
        assertThat(testPaymentTransaction.getViewerId()).isEqualTo(DEFAULT_VIEWER_ID);
        assertThat(testPaymentTransaction.getPaymentMethodId()).isEqualTo(DEFAULT_PAYMENT_METHOD_ID);
        assertThat(testPaymentTransaction.getPaymentProviderId()).isEqualTo(DEFAULT_PAYMENT_PROVIDER_ID);
    }

    @Test
    @Transactional
    void createPaymentTransactionWithExistingId() throws Exception {
        // Create the PaymentTransaction with an existing ID
        paymentTransaction.setId(1L);
        PaymentTransactionDTO paymentTransactionDTO = paymentTransactionMapper.toDto(paymentTransaction);

        int databaseSizeBeforeCreate = paymentTransactionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaymentTransactionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paymentTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentTransaction in the database
        List<PaymentTransaction> paymentTransactionList = paymentTransactionRepository.findAll();
        assertThat(paymentTransactionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentTransactionRepository.findAll().size();
        // set the field null
        paymentTransaction.setAmount(null);

        // Create the PaymentTransaction, which fails.
        PaymentTransactionDTO paymentTransactionDTO = paymentTransactionMapper.toDto(paymentTransaction);

        restPaymentTransactionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paymentTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        List<PaymentTransaction> paymentTransactionList = paymentTransactionRepository.findAll();
        assertThat(paymentTransactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPaymentDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentTransactionRepository.findAll().size();
        // set the field null
        paymentTransaction.setPaymentDate(null);

        // Create the PaymentTransaction, which fails.
        PaymentTransactionDTO paymentTransactionDTO = paymentTransactionMapper.toDto(paymentTransaction);

        restPaymentTransactionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paymentTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        List<PaymentTransaction> paymentTransactionList = paymentTransactionRepository.findAll();
        assertThat(paymentTransactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPaymentStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentTransactionRepository.findAll().size();
        // set the field null
        paymentTransaction.setPaymentStatus(null);

        // Create the PaymentTransaction, which fails.
        PaymentTransactionDTO paymentTransactionDTO = paymentTransactionMapper.toDto(paymentTransaction);

        restPaymentTransactionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paymentTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        List<PaymentTransaction> paymentTransactionList = paymentTransactionRepository.findAll();
        assertThat(paymentTransactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkViewerIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentTransactionRepository.findAll().size();
        // set the field null
        paymentTransaction.setViewerId(null);

        // Create the PaymentTransaction, which fails.
        PaymentTransactionDTO paymentTransactionDTO = paymentTransactionMapper.toDto(paymentTransaction);

        restPaymentTransactionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paymentTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        List<PaymentTransaction> paymentTransactionList = paymentTransactionRepository.findAll();
        assertThat(paymentTransactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPaymentTransactions() throws Exception {
        // Initialize the database
        paymentTransactionRepository.saveAndFlush(paymentTransaction);

        // Get all the paymentTransactionList
        restPaymentTransactionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentTransaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(DEFAULT_PAYMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].paymentStatus").value(hasItem(DEFAULT_PAYMENT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].paymentReference").value(hasItem(DEFAULT_PAYMENT_REFERENCE)))
            .andExpect(jsonPath("$.[*].cloudTransactionId").value(hasItem(DEFAULT_CLOUD_TRANSACTION_ID)))
            .andExpect(jsonPath("$.[*].viewerId").value(hasItem(DEFAULT_VIEWER_ID.intValue())))
            .andExpect(jsonPath("$.[*].paymentMethodId").value(hasItem(DEFAULT_PAYMENT_METHOD_ID.intValue())))
            .andExpect(jsonPath("$.[*].paymentProviderId").value(hasItem(DEFAULT_PAYMENT_PROVIDER_ID.intValue())));
    }

    @Test
    @Transactional
    void getPaymentTransaction() throws Exception {
        // Initialize the database
        paymentTransactionRepository.saveAndFlush(paymentTransaction);

        // Get the paymentTransaction
        restPaymentTransactionMockMvc
            .perform(get(ENTITY_API_URL_ID, paymentTransaction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(paymentTransaction.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.paymentDate").value(DEFAULT_PAYMENT_DATE.toString()))
            .andExpect(jsonPath("$.paymentStatus").value(DEFAULT_PAYMENT_STATUS.toString()))
            .andExpect(jsonPath("$.paymentReference").value(DEFAULT_PAYMENT_REFERENCE))
            .andExpect(jsonPath("$.cloudTransactionId").value(DEFAULT_CLOUD_TRANSACTION_ID))
            .andExpect(jsonPath("$.viewerId").value(DEFAULT_VIEWER_ID.intValue()))
            .andExpect(jsonPath("$.paymentMethodId").value(DEFAULT_PAYMENT_METHOD_ID.intValue()))
            .andExpect(jsonPath("$.paymentProviderId").value(DEFAULT_PAYMENT_PROVIDER_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingPaymentTransaction() throws Exception {
        // Get the paymentTransaction
        restPaymentTransactionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPaymentTransaction() throws Exception {
        // Initialize the database
        paymentTransactionRepository.saveAndFlush(paymentTransaction);

        int databaseSizeBeforeUpdate = paymentTransactionRepository.findAll().size();

        // Update the paymentTransaction
        PaymentTransaction updatedPaymentTransaction = paymentTransactionRepository.findById(paymentTransaction.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPaymentTransaction are not directly saved in db
        em.detach(updatedPaymentTransaction);
        updatedPaymentTransaction
            .amount(UPDATED_AMOUNT)
            .paymentDate(UPDATED_PAYMENT_DATE)
            .paymentStatus(UPDATED_PAYMENT_STATUS)
            .paymentReference(UPDATED_PAYMENT_REFERENCE)
            .cloudTransactionId(UPDATED_CLOUD_TRANSACTION_ID)
            .viewerId(UPDATED_VIEWER_ID)
            .paymentMethodId(UPDATED_PAYMENT_METHOD_ID)
            .paymentProviderId(UPDATED_PAYMENT_PROVIDER_ID);
        PaymentTransactionDTO paymentTransactionDTO = paymentTransactionMapper.toDto(updatedPaymentTransaction);

        restPaymentTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paymentTransactionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paymentTransactionDTO))
            )
            .andExpect(status().isOk());

        // Validate the PaymentTransaction in the database
        List<PaymentTransaction> paymentTransactionList = paymentTransactionRepository.findAll();
        assertThat(paymentTransactionList).hasSize(databaseSizeBeforeUpdate);
        PaymentTransaction testPaymentTransaction = paymentTransactionList.get(paymentTransactionList.size() - 1);
        assertThat(testPaymentTransaction.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
        assertThat(testPaymentTransaction.getPaymentDate()).isEqualTo(UPDATED_PAYMENT_DATE);
        assertThat(testPaymentTransaction.getPaymentStatus()).isEqualTo(UPDATED_PAYMENT_STATUS);
        assertThat(testPaymentTransaction.getPaymentReference()).isEqualTo(UPDATED_PAYMENT_REFERENCE);
        assertThat(testPaymentTransaction.getCloudTransactionId()).isEqualTo(UPDATED_CLOUD_TRANSACTION_ID);
        assertThat(testPaymentTransaction.getViewerId()).isEqualTo(UPDATED_VIEWER_ID);
        assertThat(testPaymentTransaction.getPaymentMethodId()).isEqualTo(UPDATED_PAYMENT_METHOD_ID);
        assertThat(testPaymentTransaction.getPaymentProviderId()).isEqualTo(UPDATED_PAYMENT_PROVIDER_ID);
    }

    @Test
    @Transactional
    void putNonExistingPaymentTransaction() throws Exception {
        int databaseSizeBeforeUpdate = paymentTransactionRepository.findAll().size();
        paymentTransaction.setId(longCount.incrementAndGet());

        // Create the PaymentTransaction
        PaymentTransactionDTO paymentTransactionDTO = paymentTransactionMapper.toDto(paymentTransaction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paymentTransactionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paymentTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentTransaction in the database
        List<PaymentTransaction> paymentTransactionList = paymentTransactionRepository.findAll();
        assertThat(paymentTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPaymentTransaction() throws Exception {
        int databaseSizeBeforeUpdate = paymentTransactionRepository.findAll().size();
        paymentTransaction.setId(longCount.incrementAndGet());

        // Create the PaymentTransaction
        PaymentTransactionDTO paymentTransactionDTO = paymentTransactionMapper.toDto(paymentTransaction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paymentTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentTransaction in the database
        List<PaymentTransaction> paymentTransactionList = paymentTransactionRepository.findAll();
        assertThat(paymentTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPaymentTransaction() throws Exception {
        int databaseSizeBeforeUpdate = paymentTransactionRepository.findAll().size();
        paymentTransaction.setId(longCount.incrementAndGet());

        // Create the PaymentTransaction
        PaymentTransactionDTO paymentTransactionDTO = paymentTransactionMapper.toDto(paymentTransaction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentTransactionMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paymentTransactionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaymentTransaction in the database
        List<PaymentTransaction> paymentTransactionList = paymentTransactionRepository.findAll();
        assertThat(paymentTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePaymentTransactionWithPatch() throws Exception {
        // Initialize the database
        paymentTransactionRepository.saveAndFlush(paymentTransaction);

        int databaseSizeBeforeUpdate = paymentTransactionRepository.findAll().size();

        // Update the paymentTransaction using partial update
        PaymentTransaction partialUpdatedPaymentTransaction = new PaymentTransaction();
        partialUpdatedPaymentTransaction.setId(paymentTransaction.getId());

        partialUpdatedPaymentTransaction
            .amount(UPDATED_AMOUNT)
            .paymentDate(UPDATED_PAYMENT_DATE)
            .viewerId(UPDATED_VIEWER_ID)
            .paymentMethodId(UPDATED_PAYMENT_METHOD_ID)
            .paymentProviderId(UPDATED_PAYMENT_PROVIDER_ID);

        restPaymentTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaymentTransaction.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPaymentTransaction))
            )
            .andExpect(status().isOk());

        // Validate the PaymentTransaction in the database
        List<PaymentTransaction> paymentTransactionList = paymentTransactionRepository.findAll();
        assertThat(paymentTransactionList).hasSize(databaseSizeBeforeUpdate);
        PaymentTransaction testPaymentTransaction = paymentTransactionList.get(paymentTransactionList.size() - 1);
        assertThat(testPaymentTransaction.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
        assertThat(testPaymentTransaction.getPaymentDate()).isEqualTo(UPDATED_PAYMENT_DATE);
        assertThat(testPaymentTransaction.getPaymentStatus()).isEqualTo(DEFAULT_PAYMENT_STATUS);
        assertThat(testPaymentTransaction.getPaymentReference()).isEqualTo(DEFAULT_PAYMENT_REFERENCE);
        assertThat(testPaymentTransaction.getCloudTransactionId()).isEqualTo(DEFAULT_CLOUD_TRANSACTION_ID);
        assertThat(testPaymentTransaction.getViewerId()).isEqualTo(UPDATED_VIEWER_ID);
        assertThat(testPaymentTransaction.getPaymentMethodId()).isEqualTo(UPDATED_PAYMENT_METHOD_ID);
        assertThat(testPaymentTransaction.getPaymentProviderId()).isEqualTo(UPDATED_PAYMENT_PROVIDER_ID);
    }

    @Test
    @Transactional
    void fullUpdatePaymentTransactionWithPatch() throws Exception {
        // Initialize the database
        paymentTransactionRepository.saveAndFlush(paymentTransaction);

        int databaseSizeBeforeUpdate = paymentTransactionRepository.findAll().size();

        // Update the paymentTransaction using partial update
        PaymentTransaction partialUpdatedPaymentTransaction = new PaymentTransaction();
        partialUpdatedPaymentTransaction.setId(paymentTransaction.getId());

        partialUpdatedPaymentTransaction
            .amount(UPDATED_AMOUNT)
            .paymentDate(UPDATED_PAYMENT_DATE)
            .paymentStatus(UPDATED_PAYMENT_STATUS)
            .paymentReference(UPDATED_PAYMENT_REFERENCE)
            .cloudTransactionId(UPDATED_CLOUD_TRANSACTION_ID)
            .viewerId(UPDATED_VIEWER_ID)
            .paymentMethodId(UPDATED_PAYMENT_METHOD_ID)
            .paymentProviderId(UPDATED_PAYMENT_PROVIDER_ID);

        restPaymentTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaymentTransaction.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPaymentTransaction))
            )
            .andExpect(status().isOk());

        // Validate the PaymentTransaction in the database
        List<PaymentTransaction> paymentTransactionList = paymentTransactionRepository.findAll();
        assertThat(paymentTransactionList).hasSize(databaseSizeBeforeUpdate);
        PaymentTransaction testPaymentTransaction = paymentTransactionList.get(paymentTransactionList.size() - 1);
        assertThat(testPaymentTransaction.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
        assertThat(testPaymentTransaction.getPaymentDate()).isEqualTo(UPDATED_PAYMENT_DATE);
        assertThat(testPaymentTransaction.getPaymentStatus()).isEqualTo(UPDATED_PAYMENT_STATUS);
        assertThat(testPaymentTransaction.getPaymentReference()).isEqualTo(UPDATED_PAYMENT_REFERENCE);
        assertThat(testPaymentTransaction.getCloudTransactionId()).isEqualTo(UPDATED_CLOUD_TRANSACTION_ID);
        assertThat(testPaymentTransaction.getViewerId()).isEqualTo(UPDATED_VIEWER_ID);
        assertThat(testPaymentTransaction.getPaymentMethodId()).isEqualTo(UPDATED_PAYMENT_METHOD_ID);
        assertThat(testPaymentTransaction.getPaymentProviderId()).isEqualTo(UPDATED_PAYMENT_PROVIDER_ID);
    }

    @Test
    @Transactional
    void patchNonExistingPaymentTransaction() throws Exception {
        int databaseSizeBeforeUpdate = paymentTransactionRepository.findAll().size();
        paymentTransaction.setId(longCount.incrementAndGet());

        // Create the PaymentTransaction
        PaymentTransactionDTO paymentTransactionDTO = paymentTransactionMapper.toDto(paymentTransaction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, paymentTransactionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(paymentTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentTransaction in the database
        List<PaymentTransaction> paymentTransactionList = paymentTransactionRepository.findAll();
        assertThat(paymentTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPaymentTransaction() throws Exception {
        int databaseSizeBeforeUpdate = paymentTransactionRepository.findAll().size();
        paymentTransaction.setId(longCount.incrementAndGet());

        // Create the PaymentTransaction
        PaymentTransactionDTO paymentTransactionDTO = paymentTransactionMapper.toDto(paymentTransaction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(paymentTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentTransaction in the database
        List<PaymentTransaction> paymentTransactionList = paymentTransactionRepository.findAll();
        assertThat(paymentTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPaymentTransaction() throws Exception {
        int databaseSizeBeforeUpdate = paymentTransactionRepository.findAll().size();
        paymentTransaction.setId(longCount.incrementAndGet());

        // Create the PaymentTransaction
        PaymentTransactionDTO paymentTransactionDTO = paymentTransactionMapper.toDto(paymentTransaction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(paymentTransactionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaymentTransaction in the database
        List<PaymentTransaction> paymentTransactionList = paymentTransactionRepository.findAll();
        assertThat(paymentTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePaymentTransaction() throws Exception {
        // Initialize the database
        paymentTransactionRepository.saveAndFlush(paymentTransaction);

        int databaseSizeBeforeDelete = paymentTransactionRepository.findAll().size();

        // Delete the paymentTransaction
        restPaymentTransactionMockMvc
            .perform(delete(ENTITY_API_URL_ID, paymentTransaction.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PaymentTransaction> paymentTransactionList = paymentTransactionRepository.findAll();
        assertThat(paymentTransactionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
