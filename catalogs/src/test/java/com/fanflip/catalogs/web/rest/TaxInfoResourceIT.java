package com.fanflip.catalogs.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fanflip.catalogs.IntegrationTest;
import com.fanflip.catalogs.domain.Country;
import com.fanflip.catalogs.domain.TaxInfo;
import com.fanflip.catalogs.domain.enumeration.TaxType;
import com.fanflip.catalogs.repository.TaxInfoRepository;
import com.fanflip.catalogs.service.TaxInfoService;
import com.fanflip.catalogs.service.dto.TaxInfoDTO;
import com.fanflip.catalogs.service.mapper.TaxInfoMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link TaxInfoResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TaxInfoResourceIT {

    private static final Float DEFAULT_RATE_PERCENTAGE = 1F;
    private static final Float UPDATED_RATE_PERCENTAGE = 2F;

    private static final TaxType DEFAULT_TAX_TYPE = TaxType.VAT;
    private static final TaxType UPDATED_TAX_TYPE = TaxType.WITHHOLDING;

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

    private static final String ENTITY_API_URL = "/api/tax-infos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TaxInfoRepository taxInfoRepository;

    @Mock
    private TaxInfoRepository taxInfoRepositoryMock;

    @Autowired
    private TaxInfoMapper taxInfoMapper;

    @Mock
    private TaxInfoService taxInfoServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTaxInfoMockMvc;

    private TaxInfo taxInfo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TaxInfo createEntity(EntityManager em) {
        TaxInfo taxInfo = new TaxInfo()
            .ratePercentage(DEFAULT_RATE_PERCENTAGE)
            .taxType(DEFAULT_TAX_TYPE)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .isDeleted(DEFAULT_IS_DELETED);
        // Add required entity
        Country country;
        if (TestUtil.findAll(em, Country.class).isEmpty()) {
            country = CountryResourceIT.createEntity(em);
            em.persist(country);
            em.flush();
        } else {
            country = TestUtil.findAll(em, Country.class).get(0);
        }
        taxInfo.setCountry(country);
        return taxInfo;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TaxInfo createUpdatedEntity(EntityManager em) {
        TaxInfo taxInfo = new TaxInfo()
            .ratePercentage(UPDATED_RATE_PERCENTAGE)
            .taxType(UPDATED_TAX_TYPE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        // Add required entity
        Country country;
        if (TestUtil.findAll(em, Country.class).isEmpty()) {
            country = CountryResourceIT.createUpdatedEntity(em);
            em.persist(country);
            em.flush();
        } else {
            country = TestUtil.findAll(em, Country.class).get(0);
        }
        taxInfo.setCountry(country);
        return taxInfo;
    }

    @BeforeEach
    public void initTest() {
        taxInfo = createEntity(em);
    }

    @Test
    @Transactional
    void createTaxInfo() throws Exception {
        int databaseSizeBeforeCreate = taxInfoRepository.findAll().size();
        // Create the TaxInfo
        TaxInfoDTO taxInfoDTO = taxInfoMapper.toDto(taxInfo);
        restTaxInfoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(taxInfoDTO)))
            .andExpect(status().isCreated());

        // Validate the TaxInfo in the database
        List<TaxInfo> taxInfoList = taxInfoRepository.findAll();
        assertThat(taxInfoList).hasSize(databaseSizeBeforeCreate + 1);
        TaxInfo testTaxInfo = taxInfoList.get(taxInfoList.size() - 1);
        assertThat(testTaxInfo.getRatePercentage()).isEqualTo(DEFAULT_RATE_PERCENTAGE);
        assertThat(testTaxInfo.getTaxType()).isEqualTo(DEFAULT_TAX_TYPE);
        assertThat(testTaxInfo.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testTaxInfo.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testTaxInfo.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testTaxInfo.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testTaxInfo.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    @Transactional
    void createTaxInfoWithExistingId() throws Exception {
        // Create the TaxInfo with an existing ID
        taxInfo.setId(1L);
        TaxInfoDTO taxInfoDTO = taxInfoMapper.toDto(taxInfo);

        int databaseSizeBeforeCreate = taxInfoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTaxInfoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(taxInfoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TaxInfo in the database
        List<TaxInfo> taxInfoList = taxInfoRepository.findAll();
        assertThat(taxInfoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTaxTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = taxInfoRepository.findAll().size();
        // set the field null
        taxInfo.setTaxType(null);

        // Create the TaxInfo, which fails.
        TaxInfoDTO taxInfoDTO = taxInfoMapper.toDto(taxInfo);

        restTaxInfoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(taxInfoDTO)))
            .andExpect(status().isBadRequest());

        List<TaxInfo> taxInfoList = taxInfoRepository.findAll();
        assertThat(taxInfoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = taxInfoRepository.findAll().size();
        // set the field null
        taxInfo.setCreatedDate(null);

        // Create the TaxInfo, which fails.
        TaxInfoDTO taxInfoDTO = taxInfoMapper.toDto(taxInfo);

        restTaxInfoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(taxInfoDTO)))
            .andExpect(status().isBadRequest());

        List<TaxInfo> taxInfoList = taxInfoRepository.findAll();
        assertThat(taxInfoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = taxInfoRepository.findAll().size();
        // set the field null
        taxInfo.setIsDeleted(null);

        // Create the TaxInfo, which fails.
        TaxInfoDTO taxInfoDTO = taxInfoMapper.toDto(taxInfo);

        restTaxInfoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(taxInfoDTO)))
            .andExpect(status().isBadRequest());

        List<TaxInfo> taxInfoList = taxInfoRepository.findAll();
        assertThat(taxInfoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTaxInfos() throws Exception {
        // Initialize the database
        taxInfoRepository.saveAndFlush(taxInfo);

        // Get all the taxInfoList
        restTaxInfoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(taxInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].ratePercentage").value(hasItem(DEFAULT_RATE_PERCENTAGE.doubleValue())))
            .andExpect(jsonPath("$.[*].taxType").value(hasItem(DEFAULT_TAX_TYPE.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTaxInfosWithEagerRelationshipsIsEnabled() throws Exception {
        when(taxInfoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTaxInfoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(taxInfoServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTaxInfosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(taxInfoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTaxInfoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(taxInfoRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getTaxInfo() throws Exception {
        // Initialize the database
        taxInfoRepository.saveAndFlush(taxInfo);

        // Get the taxInfo
        restTaxInfoMockMvc
            .perform(get(ENTITY_API_URL_ID, taxInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(taxInfo.getId().intValue()))
            .andExpect(jsonPath("$.ratePercentage").value(DEFAULT_RATE_PERCENTAGE.doubleValue()))
            .andExpect(jsonPath("$.taxType").value(DEFAULT_TAX_TYPE.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingTaxInfo() throws Exception {
        // Get the taxInfo
        restTaxInfoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTaxInfo() throws Exception {
        // Initialize the database
        taxInfoRepository.saveAndFlush(taxInfo);

        int databaseSizeBeforeUpdate = taxInfoRepository.findAll().size();

        // Update the taxInfo
        TaxInfo updatedTaxInfo = taxInfoRepository.findById(taxInfo.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTaxInfo are not directly saved in db
        em.detach(updatedTaxInfo);
        updatedTaxInfo
            .ratePercentage(UPDATED_RATE_PERCENTAGE)
            .taxType(UPDATED_TAX_TYPE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        TaxInfoDTO taxInfoDTO = taxInfoMapper.toDto(updatedTaxInfo);

        restTaxInfoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, taxInfoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(taxInfoDTO))
            )
            .andExpect(status().isOk());

        // Validate the TaxInfo in the database
        List<TaxInfo> taxInfoList = taxInfoRepository.findAll();
        assertThat(taxInfoList).hasSize(databaseSizeBeforeUpdate);
        TaxInfo testTaxInfo = taxInfoList.get(taxInfoList.size() - 1);
        assertThat(testTaxInfo.getRatePercentage()).isEqualTo(UPDATED_RATE_PERCENTAGE);
        assertThat(testTaxInfo.getTaxType()).isEqualTo(UPDATED_TAX_TYPE);
        assertThat(testTaxInfo.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testTaxInfo.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testTaxInfo.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testTaxInfo.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testTaxInfo.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void putNonExistingTaxInfo() throws Exception {
        int databaseSizeBeforeUpdate = taxInfoRepository.findAll().size();
        taxInfo.setId(longCount.incrementAndGet());

        // Create the TaxInfo
        TaxInfoDTO taxInfoDTO = taxInfoMapper.toDto(taxInfo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTaxInfoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, taxInfoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(taxInfoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaxInfo in the database
        List<TaxInfo> taxInfoList = taxInfoRepository.findAll();
        assertThat(taxInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTaxInfo() throws Exception {
        int databaseSizeBeforeUpdate = taxInfoRepository.findAll().size();
        taxInfo.setId(longCount.incrementAndGet());

        // Create the TaxInfo
        TaxInfoDTO taxInfoDTO = taxInfoMapper.toDto(taxInfo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaxInfoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(taxInfoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaxInfo in the database
        List<TaxInfo> taxInfoList = taxInfoRepository.findAll();
        assertThat(taxInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTaxInfo() throws Exception {
        int databaseSizeBeforeUpdate = taxInfoRepository.findAll().size();
        taxInfo.setId(longCount.incrementAndGet());

        // Create the TaxInfo
        TaxInfoDTO taxInfoDTO = taxInfoMapper.toDto(taxInfo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaxInfoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(taxInfoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TaxInfo in the database
        List<TaxInfo> taxInfoList = taxInfoRepository.findAll();
        assertThat(taxInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTaxInfoWithPatch() throws Exception {
        // Initialize the database
        taxInfoRepository.saveAndFlush(taxInfo);

        int databaseSizeBeforeUpdate = taxInfoRepository.findAll().size();

        // Update the taxInfo using partial update
        TaxInfo partialUpdatedTaxInfo = new TaxInfo();
        partialUpdatedTaxInfo.setId(taxInfo.getId());

        partialUpdatedTaxInfo.ratePercentage(UPDATED_RATE_PERCENTAGE);

        restTaxInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTaxInfo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTaxInfo))
            )
            .andExpect(status().isOk());

        // Validate the TaxInfo in the database
        List<TaxInfo> taxInfoList = taxInfoRepository.findAll();
        assertThat(taxInfoList).hasSize(databaseSizeBeforeUpdate);
        TaxInfo testTaxInfo = taxInfoList.get(taxInfoList.size() - 1);
        assertThat(testTaxInfo.getRatePercentage()).isEqualTo(UPDATED_RATE_PERCENTAGE);
        assertThat(testTaxInfo.getTaxType()).isEqualTo(DEFAULT_TAX_TYPE);
        assertThat(testTaxInfo.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testTaxInfo.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testTaxInfo.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testTaxInfo.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testTaxInfo.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    @Transactional
    void fullUpdateTaxInfoWithPatch() throws Exception {
        // Initialize the database
        taxInfoRepository.saveAndFlush(taxInfo);

        int databaseSizeBeforeUpdate = taxInfoRepository.findAll().size();

        // Update the taxInfo using partial update
        TaxInfo partialUpdatedTaxInfo = new TaxInfo();
        partialUpdatedTaxInfo.setId(taxInfo.getId());

        partialUpdatedTaxInfo
            .ratePercentage(UPDATED_RATE_PERCENTAGE)
            .taxType(UPDATED_TAX_TYPE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);

        restTaxInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTaxInfo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTaxInfo))
            )
            .andExpect(status().isOk());

        // Validate the TaxInfo in the database
        List<TaxInfo> taxInfoList = taxInfoRepository.findAll();
        assertThat(taxInfoList).hasSize(databaseSizeBeforeUpdate);
        TaxInfo testTaxInfo = taxInfoList.get(taxInfoList.size() - 1);
        assertThat(testTaxInfo.getRatePercentage()).isEqualTo(UPDATED_RATE_PERCENTAGE);
        assertThat(testTaxInfo.getTaxType()).isEqualTo(UPDATED_TAX_TYPE);
        assertThat(testTaxInfo.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testTaxInfo.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testTaxInfo.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testTaxInfo.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testTaxInfo.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void patchNonExistingTaxInfo() throws Exception {
        int databaseSizeBeforeUpdate = taxInfoRepository.findAll().size();
        taxInfo.setId(longCount.incrementAndGet());

        // Create the TaxInfo
        TaxInfoDTO taxInfoDTO = taxInfoMapper.toDto(taxInfo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTaxInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, taxInfoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(taxInfoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaxInfo in the database
        List<TaxInfo> taxInfoList = taxInfoRepository.findAll();
        assertThat(taxInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTaxInfo() throws Exception {
        int databaseSizeBeforeUpdate = taxInfoRepository.findAll().size();
        taxInfo.setId(longCount.incrementAndGet());

        // Create the TaxInfo
        TaxInfoDTO taxInfoDTO = taxInfoMapper.toDto(taxInfo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaxInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(taxInfoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaxInfo in the database
        List<TaxInfo> taxInfoList = taxInfoRepository.findAll();
        assertThat(taxInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTaxInfo() throws Exception {
        int databaseSizeBeforeUpdate = taxInfoRepository.findAll().size();
        taxInfo.setId(longCount.incrementAndGet());

        // Create the TaxInfo
        TaxInfoDTO taxInfoDTO = taxInfoMapper.toDto(taxInfo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaxInfoMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(taxInfoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TaxInfo in the database
        List<TaxInfo> taxInfoList = taxInfoRepository.findAll();
        assertThat(taxInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTaxInfo() throws Exception {
        // Initialize the database
        taxInfoRepository.saveAndFlush(taxInfo);

        int databaseSizeBeforeDelete = taxInfoRepository.findAll().size();

        // Delete the taxInfo
        restTaxInfoMockMvc
            .perform(delete(ENTITY_API_URL_ID, taxInfo.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TaxInfo> taxInfoList = taxInfoRepository.findAll();
        assertThat(taxInfoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
