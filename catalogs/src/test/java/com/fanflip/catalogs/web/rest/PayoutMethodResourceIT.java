package com.fanflip.catalogs.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fanflip.catalogs.IntegrationTest;
import com.fanflip.catalogs.domain.PayoutMethod;
import com.fanflip.catalogs.repository.PayoutMethodRepository;
import com.fanflip.catalogs.service.dto.PayoutMethodDTO;
import com.fanflip.catalogs.service.mapper.PayoutMethodMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link PayoutMethodResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PayoutMethodResourceIT {

    private static final String DEFAULT_METHOD_NAME = "AAAAAAAAAA";
    private static final String UPDATED_METHOD_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TOKEN_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_TOKEN_TEXT = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_EXPIRATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EXPIRATION_DATE = LocalDate.now(ZoneId.systemDefault());

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

    private static final String ENTITY_API_URL = "/api/payout-methods";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PayoutMethodRepository payoutMethodRepository;

    @Autowired
    private PayoutMethodMapper payoutMethodMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPayoutMethodMockMvc;

    private PayoutMethod payoutMethod;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PayoutMethod createEntity(EntityManager em) {
        PayoutMethod payoutMethod = new PayoutMethod()
            .methodName(DEFAULT_METHOD_NAME)
            .tokenText(DEFAULT_TOKEN_TEXT)
            .expirationDate(DEFAULT_EXPIRATION_DATE)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .isDeleted(DEFAULT_IS_DELETED);
        return payoutMethod;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PayoutMethod createUpdatedEntity(EntityManager em) {
        PayoutMethod payoutMethod = new PayoutMethod()
            .methodName(UPDATED_METHOD_NAME)
            .tokenText(UPDATED_TOKEN_TEXT)
            .expirationDate(UPDATED_EXPIRATION_DATE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        return payoutMethod;
    }

    @BeforeEach
    public void initTest() {
        payoutMethod = createEntity(em);
    }

    @Test
    @Transactional
    void createPayoutMethod() throws Exception {
        int databaseSizeBeforeCreate = payoutMethodRepository.findAll().size();
        // Create the PayoutMethod
        PayoutMethodDTO payoutMethodDTO = payoutMethodMapper.toDto(payoutMethod);
        restPayoutMethodMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(payoutMethodDTO))
            )
            .andExpect(status().isCreated());

        // Validate the PayoutMethod in the database
        List<PayoutMethod> payoutMethodList = payoutMethodRepository.findAll();
        assertThat(payoutMethodList).hasSize(databaseSizeBeforeCreate + 1);
        PayoutMethod testPayoutMethod = payoutMethodList.get(payoutMethodList.size() - 1);
        assertThat(testPayoutMethod.getMethodName()).isEqualTo(DEFAULT_METHOD_NAME);
        assertThat(testPayoutMethod.getTokenText()).isEqualTo(DEFAULT_TOKEN_TEXT);
        assertThat(testPayoutMethod.getExpirationDate()).isEqualTo(DEFAULT_EXPIRATION_DATE);
        assertThat(testPayoutMethod.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testPayoutMethod.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testPayoutMethod.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testPayoutMethod.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testPayoutMethod.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    @Transactional
    void createPayoutMethodWithExistingId() throws Exception {
        // Create the PayoutMethod with an existing ID
        payoutMethod.setId(1L);
        PayoutMethodDTO payoutMethodDTO = payoutMethodMapper.toDto(payoutMethod);

        int databaseSizeBeforeCreate = payoutMethodRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPayoutMethodMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(payoutMethodDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PayoutMethod in the database
        List<PayoutMethod> payoutMethodList = payoutMethodRepository.findAll();
        assertThat(payoutMethodList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkMethodNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = payoutMethodRepository.findAll().size();
        // set the field null
        payoutMethod.setMethodName(null);

        // Create the PayoutMethod, which fails.
        PayoutMethodDTO payoutMethodDTO = payoutMethodMapper.toDto(payoutMethod);

        restPayoutMethodMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(payoutMethodDTO))
            )
            .andExpect(status().isBadRequest());

        List<PayoutMethod> payoutMethodList = payoutMethodRepository.findAll();
        assertThat(payoutMethodList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTokenTextIsRequired() throws Exception {
        int databaseSizeBeforeTest = payoutMethodRepository.findAll().size();
        // set the field null
        payoutMethod.setTokenText(null);

        // Create the PayoutMethod, which fails.
        PayoutMethodDTO payoutMethodDTO = payoutMethodMapper.toDto(payoutMethod);

        restPayoutMethodMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(payoutMethodDTO))
            )
            .andExpect(status().isBadRequest());

        List<PayoutMethod> payoutMethodList = payoutMethodRepository.findAll();
        assertThat(payoutMethodList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = payoutMethodRepository.findAll().size();
        // set the field null
        payoutMethod.setCreatedDate(null);

        // Create the PayoutMethod, which fails.
        PayoutMethodDTO payoutMethodDTO = payoutMethodMapper.toDto(payoutMethod);

        restPayoutMethodMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(payoutMethodDTO))
            )
            .andExpect(status().isBadRequest());

        List<PayoutMethod> payoutMethodList = payoutMethodRepository.findAll();
        assertThat(payoutMethodList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = payoutMethodRepository.findAll().size();
        // set the field null
        payoutMethod.setIsDeleted(null);

        // Create the PayoutMethod, which fails.
        PayoutMethodDTO payoutMethodDTO = payoutMethodMapper.toDto(payoutMethod);

        restPayoutMethodMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(payoutMethodDTO))
            )
            .andExpect(status().isBadRequest());

        List<PayoutMethod> payoutMethodList = payoutMethodRepository.findAll();
        assertThat(payoutMethodList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPayoutMethods() throws Exception {
        // Initialize the database
        payoutMethodRepository.saveAndFlush(payoutMethod);

        // Get all the payoutMethodList
        restPayoutMethodMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(payoutMethod.getId().intValue())))
            .andExpect(jsonPath("$.[*].methodName").value(hasItem(DEFAULT_METHOD_NAME)))
            .andExpect(jsonPath("$.[*].tokenText").value(hasItem(DEFAULT_TOKEN_TEXT)))
            .andExpect(jsonPath("$.[*].expirationDate").value(hasItem(DEFAULT_EXPIRATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())));
    }

    @Test
    @Transactional
    void getPayoutMethod() throws Exception {
        // Initialize the database
        payoutMethodRepository.saveAndFlush(payoutMethod);

        // Get the payoutMethod
        restPayoutMethodMockMvc
            .perform(get(ENTITY_API_URL_ID, payoutMethod.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(payoutMethod.getId().intValue()))
            .andExpect(jsonPath("$.methodName").value(DEFAULT_METHOD_NAME))
            .andExpect(jsonPath("$.tokenText").value(DEFAULT_TOKEN_TEXT))
            .andExpect(jsonPath("$.expirationDate").value(DEFAULT_EXPIRATION_DATE.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingPayoutMethod() throws Exception {
        // Get the payoutMethod
        restPayoutMethodMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPayoutMethod() throws Exception {
        // Initialize the database
        payoutMethodRepository.saveAndFlush(payoutMethod);

        int databaseSizeBeforeUpdate = payoutMethodRepository.findAll().size();

        // Update the payoutMethod
        PayoutMethod updatedPayoutMethod = payoutMethodRepository.findById(payoutMethod.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPayoutMethod are not directly saved in db
        em.detach(updatedPayoutMethod);
        updatedPayoutMethod
            .methodName(UPDATED_METHOD_NAME)
            .tokenText(UPDATED_TOKEN_TEXT)
            .expirationDate(UPDATED_EXPIRATION_DATE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        PayoutMethodDTO payoutMethodDTO = payoutMethodMapper.toDto(updatedPayoutMethod);

        restPayoutMethodMockMvc
            .perform(
                put(ENTITY_API_URL_ID, payoutMethodDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(payoutMethodDTO))
            )
            .andExpect(status().isOk());

        // Validate the PayoutMethod in the database
        List<PayoutMethod> payoutMethodList = payoutMethodRepository.findAll();
        assertThat(payoutMethodList).hasSize(databaseSizeBeforeUpdate);
        PayoutMethod testPayoutMethod = payoutMethodList.get(payoutMethodList.size() - 1);
        assertThat(testPayoutMethod.getMethodName()).isEqualTo(UPDATED_METHOD_NAME);
        assertThat(testPayoutMethod.getTokenText()).isEqualTo(UPDATED_TOKEN_TEXT);
        assertThat(testPayoutMethod.getExpirationDate()).isEqualTo(UPDATED_EXPIRATION_DATE);
        assertThat(testPayoutMethod.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testPayoutMethod.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testPayoutMethod.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testPayoutMethod.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testPayoutMethod.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void putNonExistingPayoutMethod() throws Exception {
        int databaseSizeBeforeUpdate = payoutMethodRepository.findAll().size();
        payoutMethod.setId(longCount.incrementAndGet());

        // Create the PayoutMethod
        PayoutMethodDTO payoutMethodDTO = payoutMethodMapper.toDto(payoutMethod);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPayoutMethodMockMvc
            .perform(
                put(ENTITY_API_URL_ID, payoutMethodDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(payoutMethodDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PayoutMethod in the database
        List<PayoutMethod> payoutMethodList = payoutMethodRepository.findAll();
        assertThat(payoutMethodList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPayoutMethod() throws Exception {
        int databaseSizeBeforeUpdate = payoutMethodRepository.findAll().size();
        payoutMethod.setId(longCount.incrementAndGet());

        // Create the PayoutMethod
        PayoutMethodDTO payoutMethodDTO = payoutMethodMapper.toDto(payoutMethod);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPayoutMethodMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(payoutMethodDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PayoutMethod in the database
        List<PayoutMethod> payoutMethodList = payoutMethodRepository.findAll();
        assertThat(payoutMethodList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPayoutMethod() throws Exception {
        int databaseSizeBeforeUpdate = payoutMethodRepository.findAll().size();
        payoutMethod.setId(longCount.incrementAndGet());

        // Create the PayoutMethod
        PayoutMethodDTO payoutMethodDTO = payoutMethodMapper.toDto(payoutMethod);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPayoutMethodMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(payoutMethodDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PayoutMethod in the database
        List<PayoutMethod> payoutMethodList = payoutMethodRepository.findAll();
        assertThat(payoutMethodList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePayoutMethodWithPatch() throws Exception {
        // Initialize the database
        payoutMethodRepository.saveAndFlush(payoutMethod);

        int databaseSizeBeforeUpdate = payoutMethodRepository.findAll().size();

        // Update the payoutMethod using partial update
        PayoutMethod partialUpdatedPayoutMethod = new PayoutMethod();
        partialUpdatedPayoutMethod.setId(payoutMethod.getId());

        partialUpdatedPayoutMethod
            .methodName(UPDATED_METHOD_NAME)
            .tokenText(UPDATED_TOKEN_TEXT)
            .expirationDate(UPDATED_EXPIRATION_DATE)
            .createdBy(UPDATED_CREATED_BY);

        restPayoutMethodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPayoutMethod.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPayoutMethod))
            )
            .andExpect(status().isOk());

        // Validate the PayoutMethod in the database
        List<PayoutMethod> payoutMethodList = payoutMethodRepository.findAll();
        assertThat(payoutMethodList).hasSize(databaseSizeBeforeUpdate);
        PayoutMethod testPayoutMethod = payoutMethodList.get(payoutMethodList.size() - 1);
        assertThat(testPayoutMethod.getMethodName()).isEqualTo(UPDATED_METHOD_NAME);
        assertThat(testPayoutMethod.getTokenText()).isEqualTo(UPDATED_TOKEN_TEXT);
        assertThat(testPayoutMethod.getExpirationDate()).isEqualTo(UPDATED_EXPIRATION_DATE);
        assertThat(testPayoutMethod.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testPayoutMethod.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testPayoutMethod.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testPayoutMethod.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testPayoutMethod.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    @Transactional
    void fullUpdatePayoutMethodWithPatch() throws Exception {
        // Initialize the database
        payoutMethodRepository.saveAndFlush(payoutMethod);

        int databaseSizeBeforeUpdate = payoutMethodRepository.findAll().size();

        // Update the payoutMethod using partial update
        PayoutMethod partialUpdatedPayoutMethod = new PayoutMethod();
        partialUpdatedPayoutMethod.setId(payoutMethod.getId());

        partialUpdatedPayoutMethod
            .methodName(UPDATED_METHOD_NAME)
            .tokenText(UPDATED_TOKEN_TEXT)
            .expirationDate(UPDATED_EXPIRATION_DATE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);

        restPayoutMethodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPayoutMethod.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPayoutMethod))
            )
            .andExpect(status().isOk());

        // Validate the PayoutMethod in the database
        List<PayoutMethod> payoutMethodList = payoutMethodRepository.findAll();
        assertThat(payoutMethodList).hasSize(databaseSizeBeforeUpdate);
        PayoutMethod testPayoutMethod = payoutMethodList.get(payoutMethodList.size() - 1);
        assertThat(testPayoutMethod.getMethodName()).isEqualTo(UPDATED_METHOD_NAME);
        assertThat(testPayoutMethod.getTokenText()).isEqualTo(UPDATED_TOKEN_TEXT);
        assertThat(testPayoutMethod.getExpirationDate()).isEqualTo(UPDATED_EXPIRATION_DATE);
        assertThat(testPayoutMethod.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testPayoutMethod.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testPayoutMethod.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testPayoutMethod.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testPayoutMethod.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void patchNonExistingPayoutMethod() throws Exception {
        int databaseSizeBeforeUpdate = payoutMethodRepository.findAll().size();
        payoutMethod.setId(longCount.incrementAndGet());

        // Create the PayoutMethod
        PayoutMethodDTO payoutMethodDTO = payoutMethodMapper.toDto(payoutMethod);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPayoutMethodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, payoutMethodDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(payoutMethodDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PayoutMethod in the database
        List<PayoutMethod> payoutMethodList = payoutMethodRepository.findAll();
        assertThat(payoutMethodList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPayoutMethod() throws Exception {
        int databaseSizeBeforeUpdate = payoutMethodRepository.findAll().size();
        payoutMethod.setId(longCount.incrementAndGet());

        // Create the PayoutMethod
        PayoutMethodDTO payoutMethodDTO = payoutMethodMapper.toDto(payoutMethod);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPayoutMethodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(payoutMethodDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PayoutMethod in the database
        List<PayoutMethod> payoutMethodList = payoutMethodRepository.findAll();
        assertThat(payoutMethodList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPayoutMethod() throws Exception {
        int databaseSizeBeforeUpdate = payoutMethodRepository.findAll().size();
        payoutMethod.setId(longCount.incrementAndGet());

        // Create the PayoutMethod
        PayoutMethodDTO payoutMethodDTO = payoutMethodMapper.toDto(payoutMethod);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPayoutMethodMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(payoutMethodDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PayoutMethod in the database
        List<PayoutMethod> payoutMethodList = payoutMethodRepository.findAll();
        assertThat(payoutMethodList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePayoutMethod() throws Exception {
        // Initialize the database
        payoutMethodRepository.saveAndFlush(payoutMethod);

        int databaseSizeBeforeDelete = payoutMethodRepository.findAll().size();

        // Delete the payoutMethod
        restPayoutMethodMockMvc
            .perform(delete(ENTITY_API_URL_ID, payoutMethod.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PayoutMethod> payoutMethodList = payoutMethodRepository.findAll();
        assertThat(payoutMethodList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
