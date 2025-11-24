package com.monsterdam.multimedia.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.monsterdam.multimedia.IntegrationTest;
import com.monsterdam.multimedia.domain.SingleDocument;
import com.monsterdam.multimedia.repository.SingleDocumentRepository;
import com.monsterdam.multimedia.service.dto.SingleDocumentDTO;
import com.monsterdam.multimedia.service.mapper.SingleDocumentMapper;
import jakarta.persistence.EntityManager;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
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
 * Integration tests for the {@link SingleDocumentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SingleDocumentResourceIT {

    private static final byte[] DEFAULT_THUMBNAIL = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_THUMBNAIL = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_THUMBNAIL_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_THUMBNAIL_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_THUMBNAIL_S_3_KEY = "AAAAAAAAAA";
    private static final String UPDATED_THUMBNAIL_S_3_KEY = "BBBBBBBBBB";

    private static final byte[] DEFAULT_CONTENT = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_CONTENT = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_CONTENT_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_CONTENT_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_CONTENT_S_3_KEY = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT_S_3_KEY = "BBBBBBBBBB";

    private static final Duration DEFAULT_DURATION = Duration.ofHours(6);
    private static final Duration UPDATED_DURATION = Duration.ofHours(12);

    private static final Integer DEFAULT_LIKE_COUNT = 1;
    private static final Integer UPDATED_LIKE_COUNT = 2;

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

    private static final String ENTITY_API_URL = "/api/video-stories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SingleDocumentRepository singleDocumentRepository;

    @Autowired
    private SingleDocumentMapper singleDocumentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSingleDocumentMockMvc;

    private SingleDocument singleDocument;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SingleDocument createEntity(EntityManager em) {
        SingleDocument singleDocument = new SingleDocument()
            .thumbnailContentType(DEFAULT_THUMBNAIL_CONTENT_TYPE)
            .thumbnailS3Key(DEFAULT_THUMBNAIL_S_3_KEY)
            .contentContentType(DEFAULT_CONTENT_CONTENT_TYPE)
            .contentS3Key(DEFAULT_CONTENT_S_3_KEY)
            .duration(DEFAULT_DURATION)
            .likeCount(DEFAULT_LIKE_COUNT)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .isDeleted(DEFAULT_IS_DELETED)
            .creatorId(DEFAULT_CREATOR_ID);
        return singleDocument;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SingleDocument createUpdatedEntity(EntityManager em) {
        SingleDocument singleDocument = new SingleDocument()
            .thumbnailContentType(UPDATED_THUMBNAIL_CONTENT_TYPE)
            .thumbnailS3Key(UPDATED_THUMBNAIL_S_3_KEY)
            .contentContentType(UPDATED_CONTENT_CONTENT_TYPE)
            .contentS3Key(UPDATED_CONTENT_S_3_KEY)
            .duration(UPDATED_DURATION)
            .likeCount(UPDATED_LIKE_COUNT)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .creatorId(UPDATED_CREATOR_ID);
        return singleDocument;
    }

    @BeforeEach
    public void initTest() {
        singleDocument = createEntity(em);
    }

    @Test
    @Transactional
    void createSingleDocument() throws Exception {
        int databaseSizeBeforeCreate = singleDocumentRepository.findAll().size();
        // Create the SingleDocument
        SingleDocumentDTO singleDocumentDTO = singleDocumentMapper.toDto(singleDocument);
        restSingleDocumentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(singleDocumentDTO)))
            .andExpect(status().isCreated());

        // Validate the SingleDocument in the database
        List<SingleDocument> singleDocumentList = singleDocumentRepository.findAll();
        assertThat(singleDocumentList).hasSize(databaseSizeBeforeCreate + 1);
        SingleDocument testSingleDocument = singleDocumentList.get(singleDocumentList.size() - 1);
        assertThat(testSingleDocument.getThumbnailContentType()).isEqualTo(DEFAULT_THUMBNAIL_CONTENT_TYPE);
        assertThat(testSingleDocument.getThumbnailS3Key()).isEqualTo(DEFAULT_THUMBNAIL_S_3_KEY);
        assertThat(testSingleDocument.getContentContentType()).isEqualTo(DEFAULT_CONTENT_CONTENT_TYPE);
        assertThat(testSingleDocument.getContentS3Key()).isEqualTo(DEFAULT_CONTENT_S_3_KEY);
        assertThat(testSingleDocument.getDuration()).isEqualTo(DEFAULT_DURATION);
        assertThat(testSingleDocument.getLikeCount()).isEqualTo(DEFAULT_LIKE_COUNT);
        assertThat(testSingleDocument.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testSingleDocument.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testSingleDocument.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testSingleDocument.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testSingleDocument.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
        assertThat(testSingleDocument.getCreatorId()).isEqualTo(DEFAULT_CREATOR_ID);
    }

    @Test
    @Transactional
    void createSingleDocumentWithExistingId() throws Exception {
        // Create the SingleDocument with an existing ID
        singleDocument.setId(1L);
        SingleDocumentDTO singleDocumentDTO = singleDocumentMapper.toDto(singleDocument);

        int databaseSizeBeforeCreate = singleDocumentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSingleDocumentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(singleDocumentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SingleDocument in the database
        List<SingleDocument> singleDocumentList = singleDocumentRepository.findAll();
        assertThat(singleDocumentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkThumbnailS3KeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = singleDocumentRepository.findAll().size();
        // set the field null
        singleDocument.setThumbnailS3Key(null);

        // Create the SingleDocument, which fails.
        SingleDocumentDTO singleDocumentDTO = singleDocumentMapper.toDto(singleDocument);

        restSingleDocumentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(singleDocumentDTO)))
            .andExpect(status().isBadRequest());

        List<SingleDocument> singleDocumentList = singleDocumentRepository.findAll();
        assertThat(singleDocumentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkContentS3KeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = singleDocumentRepository.findAll().size();
        // set the field null
        singleDocument.setContentS3Key(null);

        // Create the SingleDocument, which fails.
        SingleDocumentDTO singleDocumentDTO = singleDocumentMapper.toDto(singleDocument);

        restSingleDocumentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(singleDocumentDTO)))
            .andExpect(status().isBadRequest());

        List<SingleDocument> singleDocumentList = singleDocumentRepository.findAll();
        assertThat(singleDocumentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = singleDocumentRepository.findAll().size();
        // set the field null
        singleDocument.setCreatedDate(null);

        // Create the SingleDocument, which fails.
        SingleDocumentDTO singleDocumentDTO = singleDocumentMapper.toDto(singleDocument);

        restSingleDocumentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(singleDocumentDTO)))
            .andExpect(status().isBadRequest());

        List<SingleDocument> singleDocumentList = singleDocumentRepository.findAll();
        assertThat(singleDocumentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = singleDocumentRepository.findAll().size();
        // set the field null
        singleDocument.setIsDeleted(null);

        // Create the SingleDocument, which fails.
        SingleDocumentDTO singleDocumentDTO = singleDocumentMapper.toDto(singleDocument);

        restSingleDocumentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(singleDocumentDTO)))
            .andExpect(status().isBadRequest());

        List<SingleDocument> singleDocumentList = singleDocumentRepository.findAll();
        assertThat(singleDocumentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatorIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = singleDocumentRepository.findAll().size();
        // set the field null
        singleDocument.setCreatorId(null);

        // Create the SingleDocument, which fails.
        SingleDocumentDTO singleDocumentDTO = singleDocumentMapper.toDto(singleDocument);

        restSingleDocumentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(singleDocumentDTO)))
            .andExpect(status().isBadRequest());

        List<SingleDocument> singleDocumentList = singleDocumentRepository.findAll();
        assertThat(singleDocumentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllVideoStories() throws Exception {
        // Initialize the database
        singleDocumentRepository.saveAndFlush(singleDocument);

        // Get all the singleDocumentList
        restSingleDocumentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(singleDocument.getId().intValue())))
            .andExpect(jsonPath("$.[*].thumbnailContentType").value(hasItem(DEFAULT_THUMBNAIL_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].thumbnail").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_THUMBNAIL))))
            .andExpect(jsonPath("$.[*].thumbnailS3Key").value(hasItem(DEFAULT_THUMBNAIL_S_3_KEY)))
            .andExpect(jsonPath("$.[*].contentContentType").value(hasItem(DEFAULT_CONTENT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_CONTENT))))
            .andExpect(jsonPath("$.[*].contentS3Key").value(hasItem(DEFAULT_CONTENT_S_3_KEY)))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION.toString())))
            .andExpect(jsonPath("$.[*].likeCount").value(hasItem(DEFAULT_LIKE_COUNT)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())))
            .andExpect(jsonPath("$.[*].creatorId").value(hasItem(DEFAULT_CREATOR_ID.intValue())));
    }

    @Test
    @Transactional
    void getSingleDocument() throws Exception {
        // Initialize the database
        singleDocumentRepository.saveAndFlush(singleDocument);

        // Get the singleDocument
        restSingleDocumentMockMvc
            .perform(get(ENTITY_API_URL_ID, singleDocument.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(singleDocument.getId().intValue()))
            .andExpect(jsonPath("$.thumbnailContentType").value(DEFAULT_THUMBNAIL_CONTENT_TYPE))
            .andExpect(jsonPath("$.thumbnail").value(Base64.getEncoder().encodeToString(DEFAULT_THUMBNAIL)))
            .andExpect(jsonPath("$.thumbnailS3Key").value(DEFAULT_THUMBNAIL_S_3_KEY))
            .andExpect(jsonPath("$.contentContentType").value(DEFAULT_CONTENT_CONTENT_TYPE))
            .andExpect(jsonPath("$.content").value(Base64.getEncoder().encodeToString(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.contentS3Key").value(DEFAULT_CONTENT_S_3_KEY))
            .andExpect(jsonPath("$.duration").value(DEFAULT_DURATION.toString()))
            .andExpect(jsonPath("$.likeCount").value(DEFAULT_LIKE_COUNT))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()))
            .andExpect(jsonPath("$.creatorId").value(DEFAULT_CREATOR_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingSingleDocument() throws Exception {
        // Get the singleDocument
        restSingleDocumentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSingleDocument() throws Exception {
        // Initialize the database
        singleDocumentRepository.saveAndFlush(singleDocument);

        int databaseSizeBeforeUpdate = singleDocumentRepository.findAll().size();

        // Update the singleDocument
        SingleDocument updatedSingleDocument = singleDocumentRepository.findById(singleDocument.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSingleDocument are not directly saved in db
        em.detach(updatedSingleDocument);
        updatedSingleDocument
            .thumbnailContentType(UPDATED_THUMBNAIL_CONTENT_TYPE)
            .thumbnailS3Key(UPDATED_THUMBNAIL_S_3_KEY)
            .contentContentType(UPDATED_CONTENT_CONTENT_TYPE)
            .contentS3Key(UPDATED_CONTENT_S_3_KEY)
            .duration(UPDATED_DURATION)
            .likeCount(UPDATED_LIKE_COUNT)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .creatorId(UPDATED_CREATOR_ID);
        SingleDocumentDTO singleDocumentDTO = singleDocumentMapper.toDto(updatedSingleDocument);

        restSingleDocumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, singleDocumentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(singleDocumentDTO))
            )
            .andExpect(status().isOk());

        // Validate the SingleDocument in the database
        List<SingleDocument> singleDocumentList = singleDocumentRepository.findAll();
        assertThat(singleDocumentList).hasSize(databaseSizeBeforeUpdate);
        SingleDocument testSingleDocument = singleDocumentList.get(singleDocumentList.size() - 1);
        assertThat(testSingleDocument.getThumbnailContentType()).isEqualTo(UPDATED_THUMBNAIL_CONTENT_TYPE);
        assertThat(testSingleDocument.getThumbnailS3Key()).isEqualTo(UPDATED_THUMBNAIL_S_3_KEY);
        assertThat(testSingleDocument.getContentContentType()).isEqualTo(UPDATED_CONTENT_CONTENT_TYPE);
        assertThat(testSingleDocument.getContentS3Key()).isEqualTo(UPDATED_CONTENT_S_3_KEY);
        assertThat(testSingleDocument.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testSingleDocument.getLikeCount()).isEqualTo(UPDATED_LIKE_COUNT);
        assertThat(testSingleDocument.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testSingleDocument.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testSingleDocument.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testSingleDocument.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testSingleDocument.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testSingleDocument.getCreatorId()).isEqualTo(UPDATED_CREATOR_ID);
    }

    @Test
    @Transactional
    void putNonExistingSingleDocument() throws Exception {
        int databaseSizeBeforeUpdate = singleDocumentRepository.findAll().size();
        singleDocument.setId(longCount.incrementAndGet());

        // Create the SingleDocument
        SingleDocumentDTO singleDocumentDTO = singleDocumentMapper.toDto(singleDocument);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSingleDocumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, singleDocumentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(singleDocumentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SingleDocument in the database
        List<SingleDocument> singleDocumentList = singleDocumentRepository.findAll();
        assertThat(singleDocumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSingleDocument() throws Exception {
        int databaseSizeBeforeUpdate = singleDocumentRepository.findAll().size();
        singleDocument.setId(longCount.incrementAndGet());

        // Create the SingleDocument
        SingleDocumentDTO singleDocumentDTO = singleDocumentMapper.toDto(singleDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSingleDocumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(singleDocumentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SingleDocument in the database
        List<SingleDocument> singleDocumentList = singleDocumentRepository.findAll();
        assertThat(singleDocumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSingleDocument() throws Exception {
        int databaseSizeBeforeUpdate = singleDocumentRepository.findAll().size();
        singleDocument.setId(longCount.incrementAndGet());

        // Create the SingleDocument
        SingleDocumentDTO singleDocumentDTO = singleDocumentMapper.toDto(singleDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSingleDocumentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(singleDocumentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SingleDocument in the database
        List<SingleDocument> singleDocumentList = singleDocumentRepository.findAll();
        assertThat(singleDocumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSingleDocumentWithPatch() throws Exception {
        // Initialize the database
        singleDocumentRepository.saveAndFlush(singleDocument);

        int databaseSizeBeforeUpdate = singleDocumentRepository.findAll().size();

        // Update the singleDocument using partial update
        SingleDocument partialUpdatedSingleDocument = new SingleDocument();
        partialUpdatedSingleDocument.setId(singleDocument.getId());

        partialUpdatedSingleDocument
            .contentS3Key(UPDATED_CONTENT_S_3_KEY)
            .likeCount(UPDATED_LIKE_COUNT)
            .createdBy(UPDATED_CREATED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .creatorId(UPDATED_CREATOR_ID);

        restSingleDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSingleDocument.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSingleDocument))
            )
            .andExpect(status().isOk());

        // Validate the SingleDocument in the database
        List<SingleDocument> singleDocumentList = singleDocumentRepository.findAll();
        assertThat(singleDocumentList).hasSize(databaseSizeBeforeUpdate);
        SingleDocument testSingleDocument = singleDocumentList.get(singleDocumentList.size() - 1);
        assertThat(testSingleDocument.getThumbnailContentType()).isEqualTo(DEFAULT_THUMBNAIL_CONTENT_TYPE);
        assertThat(testSingleDocument.getThumbnailS3Key()).isEqualTo(DEFAULT_THUMBNAIL_S_3_KEY);
        assertThat(testSingleDocument.getContentContentType()).isEqualTo(DEFAULT_CONTENT_CONTENT_TYPE);
        assertThat(testSingleDocument.getContentS3Key()).isEqualTo(UPDATED_CONTENT_S_3_KEY);
        assertThat(testSingleDocument.getDuration()).isEqualTo(DEFAULT_DURATION);
        assertThat(testSingleDocument.getLikeCount()).isEqualTo(UPDATED_LIKE_COUNT);
        assertThat(testSingleDocument.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testSingleDocument.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testSingleDocument.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testSingleDocument.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testSingleDocument.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testSingleDocument.getCreatorId()).isEqualTo(UPDATED_CREATOR_ID);
    }

    @Test
    @Transactional
    void fullUpdateSingleDocumentWithPatch() throws Exception {
        // Initialize the database
        singleDocumentRepository.saveAndFlush(singleDocument);

        int databaseSizeBeforeUpdate = singleDocumentRepository.findAll().size();

        // Update the singleDocument using partial update
        SingleDocument partialUpdatedSingleDocument = new SingleDocument();
        partialUpdatedSingleDocument.setId(singleDocument.getId());

        partialUpdatedSingleDocument
            .thumbnailContentType(UPDATED_THUMBNAIL_CONTENT_TYPE)
            .thumbnailS3Key(UPDATED_THUMBNAIL_S_3_KEY)
            .contentContentType(UPDATED_CONTENT_CONTENT_TYPE)
            .contentS3Key(UPDATED_CONTENT_S_3_KEY)
            .duration(UPDATED_DURATION)
            .likeCount(UPDATED_LIKE_COUNT)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .creatorId(UPDATED_CREATOR_ID);

        restSingleDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSingleDocument.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSingleDocument))
            )
            .andExpect(status().isOk());

        // Validate the SingleDocument in the database
        List<SingleDocument> singleDocumentList = singleDocumentRepository.findAll();
        assertThat(singleDocumentList).hasSize(databaseSizeBeforeUpdate);
        SingleDocument testSingleDocument = singleDocumentList.get(singleDocumentList.size() - 1);
        assertThat(testSingleDocument.getThumbnailContentType()).isEqualTo(UPDATED_THUMBNAIL_CONTENT_TYPE);
        assertThat(testSingleDocument.getThumbnailS3Key()).isEqualTo(UPDATED_THUMBNAIL_S_3_KEY);
        assertThat(testSingleDocument.getContentContentType()).isEqualTo(UPDATED_CONTENT_CONTENT_TYPE);
        assertThat(testSingleDocument.getContentS3Key()).isEqualTo(UPDATED_CONTENT_S_3_KEY);
        assertThat(testSingleDocument.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testSingleDocument.getLikeCount()).isEqualTo(UPDATED_LIKE_COUNT);
        assertThat(testSingleDocument.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testSingleDocument.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testSingleDocument.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testSingleDocument.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testSingleDocument.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testSingleDocument.getCreatorId()).isEqualTo(UPDATED_CREATOR_ID);
    }

    @Test
    @Transactional
    void patchNonExistingSingleDocument() throws Exception {
        int databaseSizeBeforeUpdate = singleDocumentRepository.findAll().size();
        singleDocument.setId(longCount.incrementAndGet());

        // Create the SingleDocument
        SingleDocumentDTO singleDocumentDTO = singleDocumentMapper.toDto(singleDocument);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSingleDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, singleDocumentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(singleDocumentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SingleDocument in the database
        List<SingleDocument> singleDocumentList = singleDocumentRepository.findAll();
        assertThat(singleDocumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSingleDocument() throws Exception {
        int databaseSizeBeforeUpdate = singleDocumentRepository.findAll().size();
        singleDocument.setId(longCount.incrementAndGet());

        // Create the SingleDocument
        SingleDocumentDTO singleDocumentDTO = singleDocumentMapper.toDto(singleDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSingleDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(singleDocumentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SingleDocument in the database
        List<SingleDocument> singleDocumentList = singleDocumentRepository.findAll();
        assertThat(singleDocumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSingleDocument() throws Exception {
        int databaseSizeBeforeUpdate = singleDocumentRepository.findAll().size();
        singleDocument.setId(longCount.incrementAndGet());

        // Create the SingleDocument
        SingleDocumentDTO singleDocumentDTO = singleDocumentMapper.toDto(singleDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSingleDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(singleDocumentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SingleDocument in the database
        List<SingleDocument> singleDocumentList = singleDocumentRepository.findAll();
        assertThat(singleDocumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSingleDocument() throws Exception {
        // Initialize the database
        singleDocumentRepository.saveAndFlush(singleDocument);

        int databaseSizeBeforeDelete = singleDocumentRepository.findAll().size();

        // Delete the singleDocument
        restSingleDocumentMockMvc
            .perform(delete(ENTITY_API_URL_ID, singleDocument.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SingleDocument> singleDocumentList = singleDocumentRepository.findAll();
        assertThat(singleDocumentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
