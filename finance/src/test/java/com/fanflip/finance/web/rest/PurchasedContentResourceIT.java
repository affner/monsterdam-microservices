package com.fanflip.finance.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fanflip.finance.IntegrationTest;
import com.fanflip.finance.domain.CreatorEarning;
import com.fanflip.finance.domain.PurchasedContent;
import com.fanflip.finance.repository.PurchasedContentRepository;
import com.fanflip.finance.service.PurchasedContentService;
import com.fanflip.finance.service.dto.PurchasedContentDTO;
import com.fanflip.finance.service.mapper.PurchasedContentMapper;
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
 * Integration tests for the {@link PurchasedContentResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PurchasedContentResourceIT {

    private static final Float DEFAULT_RATING = 1F;
    private static final Float UPDATED_RATING = 2F;

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

    private static final Long DEFAULT_VIEWER_ID = 1L;
    private static final Long UPDATED_VIEWER_ID = 2L;

    private static final Long DEFAULT_CONTENT_PACKAGE_ID = 1L;
    private static final Long UPDATED_CONTENT_PACKAGE_ID = 2L;

    private static final String ENTITY_API_URL = "/api/purchased-contents";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PurchasedContentRepository purchasedContentRepository;

    @Mock
    private PurchasedContentRepository purchasedContentRepositoryMock;

    @Autowired
    private PurchasedContentMapper purchasedContentMapper;

    @Mock
    private PurchasedContentService purchasedContentServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPurchasedContentMockMvc;

    private PurchasedContent purchasedContent;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PurchasedContent createEntity(EntityManager em) {
        PurchasedContent purchasedContent = new PurchasedContent()
            .rating(DEFAULT_RATING)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .isDeleted(DEFAULT_IS_DELETED)
            .viewerId(DEFAULT_VIEWER_ID)
            .contentPackageId(DEFAULT_CONTENT_PACKAGE_ID);
        // Add required entity
        CreatorEarning creatorEarning;
        if (TestUtil.findAll(em, CreatorEarning.class).isEmpty()) {
            creatorEarning = CreatorEarningResourceIT.createEntity(em);
            em.persist(creatorEarning);
            em.flush();
        } else {
            creatorEarning = TestUtil.findAll(em, CreatorEarning.class).get(0);
        }
        purchasedContent.setCreatorEarning(creatorEarning);
        return purchasedContent;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PurchasedContent createUpdatedEntity(EntityManager em) {
        PurchasedContent purchasedContent = new PurchasedContent()
            .rating(UPDATED_RATING)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .viewerId(UPDATED_VIEWER_ID)
            .contentPackageId(UPDATED_CONTENT_PACKAGE_ID);
        // Add required entity
        CreatorEarning creatorEarning;
        if (TestUtil.findAll(em, CreatorEarning.class).isEmpty()) {
            creatorEarning = CreatorEarningResourceIT.createUpdatedEntity(em);
            em.persist(creatorEarning);
            em.flush();
        } else {
            creatorEarning = TestUtil.findAll(em, CreatorEarning.class).get(0);
        }
        purchasedContent.setCreatorEarning(creatorEarning);
        return purchasedContent;
    }

    @BeforeEach
    public void initTest() {
        purchasedContent = createEntity(em);
    }

    @Test
    @Transactional
    void createPurchasedContent() throws Exception {
        int databaseSizeBeforeCreate = purchasedContentRepository.findAll().size();
        // Create the PurchasedContent
        PurchasedContentDTO purchasedContentDTO = purchasedContentMapper.toDto(purchasedContent);
        restPurchasedContentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(purchasedContentDTO))
            )
            .andExpect(status().isCreated());

        // Validate the PurchasedContent in the database
        List<PurchasedContent> purchasedContentList = purchasedContentRepository.findAll();
        assertThat(purchasedContentList).hasSize(databaseSizeBeforeCreate + 1);
        PurchasedContent testPurchasedContent = purchasedContentList.get(purchasedContentList.size() - 1);
        assertThat(testPurchasedContent.getRating()).isEqualTo(DEFAULT_RATING);
        assertThat(testPurchasedContent.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testPurchasedContent.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testPurchasedContent.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testPurchasedContent.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testPurchasedContent.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
        assertThat(testPurchasedContent.getViewerId()).isEqualTo(DEFAULT_VIEWER_ID);
        assertThat(testPurchasedContent.getContentPackageId()).isEqualTo(DEFAULT_CONTENT_PACKAGE_ID);
    }

    @Test
    @Transactional
    void createPurchasedContentWithExistingId() throws Exception {
        // Create the PurchasedContent with an existing ID
        purchasedContent.setId(1L);
        PurchasedContentDTO purchasedContentDTO = purchasedContentMapper.toDto(purchasedContent);

        int databaseSizeBeforeCreate = purchasedContentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPurchasedContentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(purchasedContentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchasedContent in the database
        List<PurchasedContent> purchasedContentList = purchasedContentRepository.findAll();
        assertThat(purchasedContentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = purchasedContentRepository.findAll().size();
        // set the field null
        purchasedContent.setCreatedDate(null);

        // Create the PurchasedContent, which fails.
        PurchasedContentDTO purchasedContentDTO = purchasedContentMapper.toDto(purchasedContent);

        restPurchasedContentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(purchasedContentDTO))
            )
            .andExpect(status().isBadRequest());

        List<PurchasedContent> purchasedContentList = purchasedContentRepository.findAll();
        assertThat(purchasedContentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = purchasedContentRepository.findAll().size();
        // set the field null
        purchasedContent.setIsDeleted(null);

        // Create the PurchasedContent, which fails.
        PurchasedContentDTO purchasedContentDTO = purchasedContentMapper.toDto(purchasedContent);

        restPurchasedContentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(purchasedContentDTO))
            )
            .andExpect(status().isBadRequest());

        List<PurchasedContent> purchasedContentList = purchasedContentRepository.findAll();
        assertThat(purchasedContentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkViewerIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = purchasedContentRepository.findAll().size();
        // set the field null
        purchasedContent.setViewerId(null);

        // Create the PurchasedContent, which fails.
        PurchasedContentDTO purchasedContentDTO = purchasedContentMapper.toDto(purchasedContent);

        restPurchasedContentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(purchasedContentDTO))
            )
            .andExpect(status().isBadRequest());

        List<PurchasedContent> purchasedContentList = purchasedContentRepository.findAll();
        assertThat(purchasedContentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkContentPackageIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = purchasedContentRepository.findAll().size();
        // set the field null
        purchasedContent.setContentPackageId(null);

        // Create the PurchasedContent, which fails.
        PurchasedContentDTO purchasedContentDTO = purchasedContentMapper.toDto(purchasedContent);

        restPurchasedContentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(purchasedContentDTO))
            )
            .andExpect(status().isBadRequest());

        List<PurchasedContent> purchasedContentList = purchasedContentRepository.findAll();
        assertThat(purchasedContentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPurchasedContents() throws Exception {
        // Initialize the database
        purchasedContentRepository.saveAndFlush(purchasedContent);

        // Get all the purchasedContentList
        restPurchasedContentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(purchasedContent.getId().intValue())))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING.doubleValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())))
            .andExpect(jsonPath("$.[*].viewerId").value(hasItem(DEFAULT_VIEWER_ID.intValue())))
            .andExpect(jsonPath("$.[*].contentPackageId").value(hasItem(DEFAULT_CONTENT_PACKAGE_ID.intValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPurchasedContentsWithEagerRelationshipsIsEnabled() throws Exception {
        when(purchasedContentServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPurchasedContentMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(purchasedContentServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPurchasedContentsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(purchasedContentServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPurchasedContentMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(purchasedContentRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPurchasedContent() throws Exception {
        // Initialize the database
        purchasedContentRepository.saveAndFlush(purchasedContent);

        // Get the purchasedContent
        restPurchasedContentMockMvc
            .perform(get(ENTITY_API_URL_ID, purchasedContent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(purchasedContent.getId().intValue()))
            .andExpect(jsonPath("$.rating").value(DEFAULT_RATING.doubleValue()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()))
            .andExpect(jsonPath("$.viewerId").value(DEFAULT_VIEWER_ID.intValue()))
            .andExpect(jsonPath("$.contentPackageId").value(DEFAULT_CONTENT_PACKAGE_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingPurchasedContent() throws Exception {
        // Get the purchasedContent
        restPurchasedContentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPurchasedContent() throws Exception {
        // Initialize the database
        purchasedContentRepository.saveAndFlush(purchasedContent);

        int databaseSizeBeforeUpdate = purchasedContentRepository.findAll().size();

        // Update the purchasedContent
        PurchasedContent updatedPurchasedContent = purchasedContentRepository.findById(purchasedContent.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPurchasedContent are not directly saved in db
        em.detach(updatedPurchasedContent);
        updatedPurchasedContent
            .rating(UPDATED_RATING)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .viewerId(UPDATED_VIEWER_ID)
            .contentPackageId(UPDATED_CONTENT_PACKAGE_ID);
        PurchasedContentDTO purchasedContentDTO = purchasedContentMapper.toDto(updatedPurchasedContent);

        restPurchasedContentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, purchasedContentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(purchasedContentDTO))
            )
            .andExpect(status().isOk());

        // Validate the PurchasedContent in the database
        List<PurchasedContent> purchasedContentList = purchasedContentRepository.findAll();
        assertThat(purchasedContentList).hasSize(databaseSizeBeforeUpdate);
        PurchasedContent testPurchasedContent = purchasedContentList.get(purchasedContentList.size() - 1);
        assertThat(testPurchasedContent.getRating()).isEqualTo(UPDATED_RATING);
        assertThat(testPurchasedContent.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testPurchasedContent.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testPurchasedContent.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testPurchasedContent.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testPurchasedContent.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testPurchasedContent.getViewerId()).isEqualTo(UPDATED_VIEWER_ID);
        assertThat(testPurchasedContent.getContentPackageId()).isEqualTo(UPDATED_CONTENT_PACKAGE_ID);
    }

    @Test
    @Transactional
    void putNonExistingPurchasedContent() throws Exception {
        int databaseSizeBeforeUpdate = purchasedContentRepository.findAll().size();
        purchasedContent.setId(longCount.incrementAndGet());

        // Create the PurchasedContent
        PurchasedContentDTO purchasedContentDTO = purchasedContentMapper.toDto(purchasedContent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPurchasedContentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, purchasedContentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(purchasedContentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchasedContent in the database
        List<PurchasedContent> purchasedContentList = purchasedContentRepository.findAll();
        assertThat(purchasedContentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPurchasedContent() throws Exception {
        int databaseSizeBeforeUpdate = purchasedContentRepository.findAll().size();
        purchasedContent.setId(longCount.incrementAndGet());

        // Create the PurchasedContent
        PurchasedContentDTO purchasedContentDTO = purchasedContentMapper.toDto(purchasedContent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPurchasedContentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(purchasedContentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchasedContent in the database
        List<PurchasedContent> purchasedContentList = purchasedContentRepository.findAll();
        assertThat(purchasedContentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPurchasedContent() throws Exception {
        int databaseSizeBeforeUpdate = purchasedContentRepository.findAll().size();
        purchasedContent.setId(longCount.incrementAndGet());

        // Create the PurchasedContent
        PurchasedContentDTO purchasedContentDTO = purchasedContentMapper.toDto(purchasedContent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPurchasedContentMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(purchasedContentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PurchasedContent in the database
        List<PurchasedContent> purchasedContentList = purchasedContentRepository.findAll();
        assertThat(purchasedContentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePurchasedContentWithPatch() throws Exception {
        // Initialize the database
        purchasedContentRepository.saveAndFlush(purchasedContent);

        int databaseSizeBeforeUpdate = purchasedContentRepository.findAll().size();

        // Update the purchasedContent using partial update
        PurchasedContent partialUpdatedPurchasedContent = new PurchasedContent();
        partialUpdatedPurchasedContent.setId(purchasedContent.getId());

        partialUpdatedPurchasedContent
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .viewerId(UPDATED_VIEWER_ID);

        restPurchasedContentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPurchasedContent.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPurchasedContent))
            )
            .andExpect(status().isOk());

        // Validate the PurchasedContent in the database
        List<PurchasedContent> purchasedContentList = purchasedContentRepository.findAll();
        assertThat(purchasedContentList).hasSize(databaseSizeBeforeUpdate);
        PurchasedContent testPurchasedContent = purchasedContentList.get(purchasedContentList.size() - 1);
        assertThat(testPurchasedContent.getRating()).isEqualTo(DEFAULT_RATING);
        assertThat(testPurchasedContent.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testPurchasedContent.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testPurchasedContent.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testPurchasedContent.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testPurchasedContent.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
        assertThat(testPurchasedContent.getViewerId()).isEqualTo(UPDATED_VIEWER_ID);
        assertThat(testPurchasedContent.getContentPackageId()).isEqualTo(DEFAULT_CONTENT_PACKAGE_ID);
    }

    @Test
    @Transactional
    void fullUpdatePurchasedContentWithPatch() throws Exception {
        // Initialize the database
        purchasedContentRepository.saveAndFlush(purchasedContent);

        int databaseSizeBeforeUpdate = purchasedContentRepository.findAll().size();

        // Update the purchasedContent using partial update
        PurchasedContent partialUpdatedPurchasedContent = new PurchasedContent();
        partialUpdatedPurchasedContent.setId(purchasedContent.getId());

        partialUpdatedPurchasedContent
            .rating(UPDATED_RATING)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .viewerId(UPDATED_VIEWER_ID)
            .contentPackageId(UPDATED_CONTENT_PACKAGE_ID);

        restPurchasedContentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPurchasedContent.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPurchasedContent))
            )
            .andExpect(status().isOk());

        // Validate the PurchasedContent in the database
        List<PurchasedContent> purchasedContentList = purchasedContentRepository.findAll();
        assertThat(purchasedContentList).hasSize(databaseSizeBeforeUpdate);
        PurchasedContent testPurchasedContent = purchasedContentList.get(purchasedContentList.size() - 1);
        assertThat(testPurchasedContent.getRating()).isEqualTo(UPDATED_RATING);
        assertThat(testPurchasedContent.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testPurchasedContent.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testPurchasedContent.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testPurchasedContent.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testPurchasedContent.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testPurchasedContent.getViewerId()).isEqualTo(UPDATED_VIEWER_ID);
        assertThat(testPurchasedContent.getContentPackageId()).isEqualTo(UPDATED_CONTENT_PACKAGE_ID);
    }

    @Test
    @Transactional
    void patchNonExistingPurchasedContent() throws Exception {
        int databaseSizeBeforeUpdate = purchasedContentRepository.findAll().size();
        purchasedContent.setId(longCount.incrementAndGet());

        // Create the PurchasedContent
        PurchasedContentDTO purchasedContentDTO = purchasedContentMapper.toDto(purchasedContent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPurchasedContentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, purchasedContentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(purchasedContentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchasedContent in the database
        List<PurchasedContent> purchasedContentList = purchasedContentRepository.findAll();
        assertThat(purchasedContentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPurchasedContent() throws Exception {
        int databaseSizeBeforeUpdate = purchasedContentRepository.findAll().size();
        purchasedContent.setId(longCount.incrementAndGet());

        // Create the PurchasedContent
        PurchasedContentDTO purchasedContentDTO = purchasedContentMapper.toDto(purchasedContent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPurchasedContentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(purchasedContentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchasedContent in the database
        List<PurchasedContent> purchasedContentList = purchasedContentRepository.findAll();
        assertThat(purchasedContentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPurchasedContent() throws Exception {
        int databaseSizeBeforeUpdate = purchasedContentRepository.findAll().size();
        purchasedContent.setId(longCount.incrementAndGet());

        // Create the PurchasedContent
        PurchasedContentDTO purchasedContentDTO = purchasedContentMapper.toDto(purchasedContent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPurchasedContentMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(purchasedContentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PurchasedContent in the database
        List<PurchasedContent> purchasedContentList = purchasedContentRepository.findAll();
        assertThat(purchasedContentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePurchasedContent() throws Exception {
        // Initialize the database
        purchasedContentRepository.saveAndFlush(purchasedContent);

        int databaseSizeBeforeDelete = purchasedContentRepository.findAll().size();

        // Delete the purchasedContent
        restPurchasedContentMockMvc
            .perform(delete(ENTITY_API_URL_ID, purchasedContent.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PurchasedContent> purchasedContentList = purchasedContentRepository.findAll();
        assertThat(purchasedContentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
