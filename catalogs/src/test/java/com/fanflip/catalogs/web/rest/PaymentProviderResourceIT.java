package com.monsterdam.catalogs.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.monsterdam.catalogs.IntegrationTest;
import com.monsterdam.catalogs.domain.PaymentProvider;
import com.monsterdam.catalogs.repository.PaymentProviderRepository;
import com.monsterdam.catalogs.service.dto.PaymentProviderDTO;
import com.monsterdam.catalogs.service.mapper.PaymentProviderMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link PaymentProviderResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PaymentProviderResourceIT {

    private static final String DEFAULT_PROVIDER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PROVIDER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_API_KEY_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_API_KEY_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_API_SECRET_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_API_SECRET_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_ENDPOINT_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_ENDPOINT_TEXT = "BBBBBBBBBB";

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

    private static final String ENTITY_API_URL = "/api/payment-providers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PaymentProviderRepository paymentProviderRepository;

    @Autowired
    private PaymentProviderMapper paymentProviderMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPaymentProviderMockMvc;

    private PaymentProvider paymentProvider;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentProvider createEntity(EntityManager em) {
        PaymentProvider paymentProvider = new PaymentProvider()
            .providerName(DEFAULT_PROVIDER_NAME)
            .description(DEFAULT_DESCRIPTION)
            .apiKeyText(DEFAULT_API_KEY_TEXT)
            .apiSecretText(DEFAULT_API_SECRET_TEXT)
            .endpointText(DEFAULT_ENDPOINT_TEXT)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .isDeleted(DEFAULT_IS_DELETED);
        return paymentProvider;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentProvider createUpdatedEntity(EntityManager em) {
        PaymentProvider paymentProvider = new PaymentProvider()
            .providerName(UPDATED_PROVIDER_NAME)
            .description(UPDATED_DESCRIPTION)
            .apiKeyText(UPDATED_API_KEY_TEXT)
            .apiSecretText(UPDATED_API_SECRET_TEXT)
            .endpointText(UPDATED_ENDPOINT_TEXT)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        return paymentProvider;
    }

    @BeforeEach
    public void initTest() {
        paymentProvider = createEntity(em);
    }

    @Test
    @Transactional
    void createPaymentProvider() throws Exception {
        int databaseSizeBeforeCreate = paymentProviderRepository.findAll().size();
        // Create the PaymentProvider
        PaymentProviderDTO paymentProviderDTO = paymentProviderMapper.toDto(paymentProvider);
        restPaymentProviderMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paymentProviderDTO))
            )
            .andExpect(status().isCreated());

        // Validate the PaymentProvider in the database
        List<PaymentProvider> paymentProviderList = paymentProviderRepository.findAll();
        assertThat(paymentProviderList).hasSize(databaseSizeBeforeCreate + 1);
        PaymentProvider testPaymentProvider = paymentProviderList.get(paymentProviderList.size() - 1);
        assertThat(testPaymentProvider.getProviderName()).isEqualTo(DEFAULT_PROVIDER_NAME);
        assertThat(testPaymentProvider.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPaymentProvider.getApiKeyText()).isEqualTo(DEFAULT_API_KEY_TEXT);
        assertThat(testPaymentProvider.getApiSecretText()).isEqualTo(DEFAULT_API_SECRET_TEXT);
        assertThat(testPaymentProvider.getEndpointText()).isEqualTo(DEFAULT_ENDPOINT_TEXT);
        assertThat(testPaymentProvider.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testPaymentProvider.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testPaymentProvider.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testPaymentProvider.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testPaymentProvider.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    @Transactional
    void createPaymentProviderWithExistingId() throws Exception {
        // Create the PaymentProvider with an existing ID
        paymentProvider.setId(1L);
        PaymentProviderDTO paymentProviderDTO = paymentProviderMapper.toDto(paymentProvider);

        int databaseSizeBeforeCreate = paymentProviderRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaymentProviderMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paymentProviderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentProvider in the database
        List<PaymentProvider> paymentProviderList = paymentProviderRepository.findAll();
        assertThat(paymentProviderList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkProviderNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentProviderRepository.findAll().size();
        // set the field null
        paymentProvider.setProviderName(null);

        // Create the PaymentProvider, which fails.
        PaymentProviderDTO paymentProviderDTO = paymentProviderMapper.toDto(paymentProvider);

        restPaymentProviderMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paymentProviderDTO))
            )
            .andExpect(status().isBadRequest());

        List<PaymentProvider> paymentProviderList = paymentProviderRepository.findAll();
        assertThat(paymentProviderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkApiKeyTextIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentProviderRepository.findAll().size();
        // set the field null
        paymentProvider.setApiKeyText(null);

        // Create the PaymentProvider, which fails.
        PaymentProviderDTO paymentProviderDTO = paymentProviderMapper.toDto(paymentProvider);

        restPaymentProviderMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paymentProviderDTO))
            )
            .andExpect(status().isBadRequest());

        List<PaymentProvider> paymentProviderList = paymentProviderRepository.findAll();
        assertThat(paymentProviderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkApiSecretTextIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentProviderRepository.findAll().size();
        // set the field null
        paymentProvider.setApiSecretText(null);

        // Create the PaymentProvider, which fails.
        PaymentProviderDTO paymentProviderDTO = paymentProviderMapper.toDto(paymentProvider);

        restPaymentProviderMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paymentProviderDTO))
            )
            .andExpect(status().isBadRequest());

        List<PaymentProvider> paymentProviderList = paymentProviderRepository.findAll();
        assertThat(paymentProviderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEndpointTextIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentProviderRepository.findAll().size();
        // set the field null
        paymentProvider.setEndpointText(null);

        // Create the PaymentProvider, which fails.
        PaymentProviderDTO paymentProviderDTO = paymentProviderMapper.toDto(paymentProvider);

        restPaymentProviderMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paymentProviderDTO))
            )
            .andExpect(status().isBadRequest());

        List<PaymentProvider> paymentProviderList = paymentProviderRepository.findAll();
        assertThat(paymentProviderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentProviderRepository.findAll().size();
        // set the field null
        paymentProvider.setCreatedDate(null);

        // Create the PaymentProvider, which fails.
        PaymentProviderDTO paymentProviderDTO = paymentProviderMapper.toDto(paymentProvider);

        restPaymentProviderMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paymentProviderDTO))
            )
            .andExpect(status().isBadRequest());

        List<PaymentProvider> paymentProviderList = paymentProviderRepository.findAll();
        assertThat(paymentProviderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentProviderRepository.findAll().size();
        // set the field null
        paymentProvider.setIsDeleted(null);

        // Create the PaymentProvider, which fails.
        PaymentProviderDTO paymentProviderDTO = paymentProviderMapper.toDto(paymentProvider);

        restPaymentProviderMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paymentProviderDTO))
            )
            .andExpect(status().isBadRequest());

        List<PaymentProvider> paymentProviderList = paymentProviderRepository.findAll();
        assertThat(paymentProviderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPaymentProviders() throws Exception {
        // Initialize the database
        paymentProviderRepository.saveAndFlush(paymentProvider);

        // Get all the paymentProviderList
        restPaymentProviderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentProvider.getId().intValue())))
            .andExpect(jsonPath("$.[*].providerName").value(hasItem(DEFAULT_PROVIDER_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].apiKeyText").value(hasItem(DEFAULT_API_KEY_TEXT)))
            .andExpect(jsonPath("$.[*].apiSecretText").value(hasItem(DEFAULT_API_SECRET_TEXT)))
            .andExpect(jsonPath("$.[*].endpointText").value(hasItem(DEFAULT_ENDPOINT_TEXT)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())));
    }

    @Test
    @Transactional
    void getPaymentProvider() throws Exception {
        // Initialize the database
        paymentProviderRepository.saveAndFlush(paymentProvider);

        // Get the paymentProvider
        restPaymentProviderMockMvc
            .perform(get(ENTITY_API_URL_ID, paymentProvider.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(paymentProvider.getId().intValue()))
            .andExpect(jsonPath("$.providerName").value(DEFAULT_PROVIDER_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.apiKeyText").value(DEFAULT_API_KEY_TEXT))
            .andExpect(jsonPath("$.apiSecretText").value(DEFAULT_API_SECRET_TEXT))
            .andExpect(jsonPath("$.endpointText").value(DEFAULT_ENDPOINT_TEXT))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingPaymentProvider() throws Exception {
        // Get the paymentProvider
        restPaymentProviderMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPaymentProvider() throws Exception {
        // Initialize the database
        paymentProviderRepository.saveAndFlush(paymentProvider);

        int databaseSizeBeforeUpdate = paymentProviderRepository.findAll().size();

        // Update the paymentProvider
        PaymentProvider updatedPaymentProvider = paymentProviderRepository.findById(paymentProvider.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPaymentProvider are not directly saved in db
        em.detach(updatedPaymentProvider);
        updatedPaymentProvider
            .providerName(UPDATED_PROVIDER_NAME)
            .description(UPDATED_DESCRIPTION)
            .apiKeyText(UPDATED_API_KEY_TEXT)
            .apiSecretText(UPDATED_API_SECRET_TEXT)
            .endpointText(UPDATED_ENDPOINT_TEXT)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        PaymentProviderDTO paymentProviderDTO = paymentProviderMapper.toDto(updatedPaymentProvider);

        restPaymentProviderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paymentProviderDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paymentProviderDTO))
            )
            .andExpect(status().isOk());

        // Validate the PaymentProvider in the database
        List<PaymentProvider> paymentProviderList = paymentProviderRepository.findAll();
        assertThat(paymentProviderList).hasSize(databaseSizeBeforeUpdate);
        PaymentProvider testPaymentProvider = paymentProviderList.get(paymentProviderList.size() - 1);
        assertThat(testPaymentProvider.getProviderName()).isEqualTo(UPDATED_PROVIDER_NAME);
        assertThat(testPaymentProvider.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPaymentProvider.getApiKeyText()).isEqualTo(UPDATED_API_KEY_TEXT);
        assertThat(testPaymentProvider.getApiSecretText()).isEqualTo(UPDATED_API_SECRET_TEXT);
        assertThat(testPaymentProvider.getEndpointText()).isEqualTo(UPDATED_ENDPOINT_TEXT);
        assertThat(testPaymentProvider.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testPaymentProvider.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testPaymentProvider.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testPaymentProvider.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testPaymentProvider.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void putNonExistingPaymentProvider() throws Exception {
        int databaseSizeBeforeUpdate = paymentProviderRepository.findAll().size();
        paymentProvider.setId(longCount.incrementAndGet());

        // Create the PaymentProvider
        PaymentProviderDTO paymentProviderDTO = paymentProviderMapper.toDto(paymentProvider);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentProviderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paymentProviderDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paymentProviderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentProvider in the database
        List<PaymentProvider> paymentProviderList = paymentProviderRepository.findAll();
        assertThat(paymentProviderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPaymentProvider() throws Exception {
        int databaseSizeBeforeUpdate = paymentProviderRepository.findAll().size();
        paymentProvider.setId(longCount.incrementAndGet());

        // Create the PaymentProvider
        PaymentProviderDTO paymentProviderDTO = paymentProviderMapper.toDto(paymentProvider);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentProviderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paymentProviderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentProvider in the database
        List<PaymentProvider> paymentProviderList = paymentProviderRepository.findAll();
        assertThat(paymentProviderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPaymentProvider() throws Exception {
        int databaseSizeBeforeUpdate = paymentProviderRepository.findAll().size();
        paymentProvider.setId(longCount.incrementAndGet());

        // Create the PaymentProvider
        PaymentProviderDTO paymentProviderDTO = paymentProviderMapper.toDto(paymentProvider);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentProviderMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paymentProviderDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaymentProvider in the database
        List<PaymentProvider> paymentProviderList = paymentProviderRepository.findAll();
        assertThat(paymentProviderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePaymentProviderWithPatch() throws Exception {
        // Initialize the database
        paymentProviderRepository.saveAndFlush(paymentProvider);

        int databaseSizeBeforeUpdate = paymentProviderRepository.findAll().size();

        // Update the paymentProvider using partial update
        PaymentProvider partialUpdatedPaymentProvider = new PaymentProvider();
        partialUpdatedPaymentProvider.setId(paymentProvider.getId());

        partialUpdatedPaymentProvider.apiKeyText(UPDATED_API_KEY_TEXT).endpointText(UPDATED_ENDPOINT_TEXT).createdBy(UPDATED_CREATED_BY);

        restPaymentProviderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaymentProvider.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPaymentProvider))
            )
            .andExpect(status().isOk());

        // Validate the PaymentProvider in the database
        List<PaymentProvider> paymentProviderList = paymentProviderRepository.findAll();
        assertThat(paymentProviderList).hasSize(databaseSizeBeforeUpdate);
        PaymentProvider testPaymentProvider = paymentProviderList.get(paymentProviderList.size() - 1);
        assertThat(testPaymentProvider.getProviderName()).isEqualTo(DEFAULT_PROVIDER_NAME);
        assertThat(testPaymentProvider.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPaymentProvider.getApiKeyText()).isEqualTo(UPDATED_API_KEY_TEXT);
        assertThat(testPaymentProvider.getApiSecretText()).isEqualTo(DEFAULT_API_SECRET_TEXT);
        assertThat(testPaymentProvider.getEndpointText()).isEqualTo(UPDATED_ENDPOINT_TEXT);
        assertThat(testPaymentProvider.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testPaymentProvider.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testPaymentProvider.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testPaymentProvider.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testPaymentProvider.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    @Transactional
    void fullUpdatePaymentProviderWithPatch() throws Exception {
        // Initialize the database
        paymentProviderRepository.saveAndFlush(paymentProvider);

        int databaseSizeBeforeUpdate = paymentProviderRepository.findAll().size();

        // Update the paymentProvider using partial update
        PaymentProvider partialUpdatedPaymentProvider = new PaymentProvider();
        partialUpdatedPaymentProvider.setId(paymentProvider.getId());

        partialUpdatedPaymentProvider
            .providerName(UPDATED_PROVIDER_NAME)
            .description(UPDATED_DESCRIPTION)
            .apiKeyText(UPDATED_API_KEY_TEXT)
            .apiSecretText(UPDATED_API_SECRET_TEXT)
            .endpointText(UPDATED_ENDPOINT_TEXT)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);

        restPaymentProviderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaymentProvider.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPaymentProvider))
            )
            .andExpect(status().isOk());

        // Validate the PaymentProvider in the database
        List<PaymentProvider> paymentProviderList = paymentProviderRepository.findAll();
        assertThat(paymentProviderList).hasSize(databaseSizeBeforeUpdate);
        PaymentProvider testPaymentProvider = paymentProviderList.get(paymentProviderList.size() - 1);
        assertThat(testPaymentProvider.getProviderName()).isEqualTo(UPDATED_PROVIDER_NAME);
        assertThat(testPaymentProvider.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPaymentProvider.getApiKeyText()).isEqualTo(UPDATED_API_KEY_TEXT);
        assertThat(testPaymentProvider.getApiSecretText()).isEqualTo(UPDATED_API_SECRET_TEXT);
        assertThat(testPaymentProvider.getEndpointText()).isEqualTo(UPDATED_ENDPOINT_TEXT);
        assertThat(testPaymentProvider.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testPaymentProvider.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testPaymentProvider.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testPaymentProvider.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testPaymentProvider.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void patchNonExistingPaymentProvider() throws Exception {
        int databaseSizeBeforeUpdate = paymentProviderRepository.findAll().size();
        paymentProvider.setId(longCount.incrementAndGet());

        // Create the PaymentProvider
        PaymentProviderDTO paymentProviderDTO = paymentProviderMapper.toDto(paymentProvider);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentProviderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, paymentProviderDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(paymentProviderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentProvider in the database
        List<PaymentProvider> paymentProviderList = paymentProviderRepository.findAll();
        assertThat(paymentProviderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPaymentProvider() throws Exception {
        int databaseSizeBeforeUpdate = paymentProviderRepository.findAll().size();
        paymentProvider.setId(longCount.incrementAndGet());

        // Create the PaymentProvider
        PaymentProviderDTO paymentProviderDTO = paymentProviderMapper.toDto(paymentProvider);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentProviderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(paymentProviderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentProvider in the database
        List<PaymentProvider> paymentProviderList = paymentProviderRepository.findAll();
        assertThat(paymentProviderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPaymentProvider() throws Exception {
        int databaseSizeBeforeUpdate = paymentProviderRepository.findAll().size();
        paymentProvider.setId(longCount.incrementAndGet());

        // Create the PaymentProvider
        PaymentProviderDTO paymentProviderDTO = paymentProviderMapper.toDto(paymentProvider);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentProviderMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(paymentProviderDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaymentProvider in the database
        List<PaymentProvider> paymentProviderList = paymentProviderRepository.findAll();
        assertThat(paymentProviderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePaymentProvider() throws Exception {
        // Initialize the database
        paymentProviderRepository.saveAndFlush(paymentProvider);

        int databaseSizeBeforeDelete = paymentProviderRepository.findAll().size();

        // Delete the paymentProvider
        restPaymentProviderMockMvc
            .perform(delete(ENTITY_API_URL_ID, paymentProvider.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PaymentProvider> paymentProviderList = paymentProviderRepository.findAll();
        assertThat(paymentProviderList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
