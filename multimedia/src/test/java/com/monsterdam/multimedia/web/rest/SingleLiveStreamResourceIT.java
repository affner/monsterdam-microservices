package com.monsterdam.multimedia.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.monsterdam.multimedia.IntegrationTest;
import com.monsterdam.multimedia.domain.ContentPackage;
import com.monsterdam.multimedia.domain.SingleLiveStream;
import com.monsterdam.multimedia.repository.SingleLiveStreamRepository;
import com.monsterdam.multimedia.service.dto.SingleLiveStreamDTO;
import com.monsterdam.multimedia.service.mapper.SingleLiveStreamMapper;
import jakarta.persistence.EntityManager;

import java.time.Duration;
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
 * Integration tests for the {@link SingleLiveStreamResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SingleLiveStreamResourceIT {


    private static final String DEFAULT_THUMBNAIL_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_THUMBNAIL_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_THUMBNAIL_S_3_KEY = "AAAAAAAAAA";
    private static final String UPDATED_THUMBNAIL_S_3_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_CONTENT_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_CONTENT_S_3_KEY = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT_S_3_KEY = "BBBBBBBBBB";

    private static final Duration DEFAULT_DURATION = Duration.ofHours(6);
    private static final Duration UPDATED_DURATION = Duration.ofHours(12);

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

    private static final String ENTITY_API_URL = "/api/single-live-streams";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SingleLiveStreamRepository singleLiveStreamRepository;

    @Autowired
    private SingleLiveStreamMapper singleLiveStreamMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSingleLiveStreamMockMvc;

    private SingleLiveStream singleLiveStream;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SingleLiveStream createEntity(EntityManager em) {
        SingleLiveStream singleLiveStream = new SingleLiveStream()
            .thumbnailContentType(DEFAULT_THUMBNAIL_CONTENT_TYPE)
            .thumbnailS3Key(DEFAULT_THUMBNAIL_S_3_KEY)
            .contentContentType(DEFAULT_CONTENT_CONTENT_TYPE)
            .contentS3Key(DEFAULT_CONTENT_S_3_KEY)
            .duration(DEFAULT_DURATION)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .isDeleted(DEFAULT_IS_DELETED);
        // Add required entity
        ContentPackage contentPackage;
        if (TestUtil.findAll(em, ContentPackage.class).isEmpty()) {
            contentPackage = ContentPackageResourceIT.createEntity(em);
            em.persist(contentPackage);
            em.flush();
        } else {
            contentPackage = TestUtil.findAll(em, ContentPackage.class).get(0);
        }
        singleLiveStream.setContentPackage(contentPackage);
        return singleLiveStream;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SingleLiveStream createUpdatedEntity(EntityManager em) {
        SingleLiveStream singleLiveStream = new SingleLiveStream()

            .thumbnailContentType(UPDATED_THUMBNAIL_CONTENT_TYPE)
            .thumbnailS3Key(UPDATED_THUMBNAIL_S_3_KEY)
            .contentContentType(UPDATED_CONTENT_CONTENT_TYPE)
            .contentS3Key(UPDATED_CONTENT_S_3_KEY)
            .duration(UPDATED_DURATION)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        // Add required entity
        ContentPackage contentPackage;
        if (TestUtil.findAll(em, ContentPackage.class).isEmpty()) {
            contentPackage = ContentPackageResourceIT.createUpdatedEntity(em);
            em.persist(contentPackage);
            em.flush();
        } else {
            contentPackage = TestUtil.findAll(em, ContentPackage.class).get(0);
        }
        singleLiveStream.setContentPackage(contentPackage);
        return singleLiveStream;
    }

    @BeforeEach
    public void initTest() {
        singleLiveStream = createEntity(em);
    }

    @Test
    @Transactional
    void createSingleLiveStream() throws Exception {
        int databaseSizeBeforeCreate = singleLiveStreamRepository.findAll().size();
        // Create the SingleLiveStream
        SingleLiveStreamDTO singleLiveStreamDTO = singleLiveStreamMapper.toDto(singleLiveStream);
        restSingleLiveStreamMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(singleLiveStreamDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SingleLiveStream in the database
        List<SingleLiveStream> singleLiveStreamList = singleLiveStreamRepository.findAll();
        assertThat(singleLiveStreamList).hasSize(databaseSizeBeforeCreate + 1);
        SingleLiveStream testSingleLiveStream = singleLiveStreamList.get(singleLiveStreamList.size() - 1);
        assertThat(testSingleLiveStream.getThumbnailContentType()).isEqualTo(DEFAULT_THUMBNAIL_CONTENT_TYPE);
        assertThat(testSingleLiveStream.getThumbnailS3Key()).isEqualTo(DEFAULT_THUMBNAIL_S_3_KEY);
        assertThat(testSingleLiveStream.getContentContentType()).isEqualTo(DEFAULT_CONTENT_CONTENT_TYPE);
        assertThat(testSingleLiveStream.getContentS3Key()).isEqualTo(DEFAULT_CONTENT_S_3_KEY);
        assertThat(testSingleLiveStream.getDuration()).isEqualTo(DEFAULT_DURATION);
        assertThat(testSingleLiveStream.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testSingleLiveStream.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testSingleLiveStream.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testSingleLiveStream.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testSingleLiveStream.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    @Transactional
    void createSingleLiveStreamWithExistingId() throws Exception {
        // Create the SingleLiveStream with an existing ID
        singleLiveStream.setId(1L);
        SingleLiveStreamDTO singleLiveStreamDTO = singleLiveStreamMapper.toDto(singleLiveStream);

        int databaseSizeBeforeCreate = singleLiveStreamRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSingleLiveStreamMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(singleLiveStreamDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SingleLiveStream in the database
        List<SingleLiveStream> singleLiveStreamList = singleLiveStreamRepository.findAll();
        assertThat(singleLiveStreamList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkThumbnailS3KeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = singleLiveStreamRepository.findAll().size();
        // set the field null
        singleLiveStream.setThumbnailS3Key(null);

        // Create the SingleLiveStream, which fails.
        SingleLiveStreamDTO singleLiveStreamDTO = singleLiveStreamMapper.toDto(singleLiveStream);

        restSingleLiveStreamMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(singleLiveStreamDTO))
            )
            .andExpect(status().isBadRequest());

        List<SingleLiveStream> singleLiveStreamList = singleLiveStreamRepository.findAll();
        assertThat(singleLiveStreamList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkContentS3KeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = singleLiveStreamRepository.findAll().size();
        // set the field null
        singleLiveStream.setContentS3Key(null);

        // Create the SingleLiveStream, which fails.
        SingleLiveStreamDTO singleLiveStreamDTO = singleLiveStreamMapper.toDto(singleLiveStream);

        restSingleLiveStreamMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(singleLiveStreamDTO))
            )
            .andExpect(status().isBadRequest());

        List<SingleLiveStream> singleLiveStreamList = singleLiveStreamRepository.findAll();
        assertThat(singleLiveStreamList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = singleLiveStreamRepository.findAll().size();
        // set the field null
        singleLiveStream.setCreatedDate(null);

        // Create the SingleLiveStream, which fails.
        SingleLiveStreamDTO singleLiveStreamDTO = singleLiveStreamMapper.toDto(singleLiveStream);

        restSingleLiveStreamMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(singleLiveStreamDTO))
            )
            .andExpect(status().isBadRequest());

        List<SingleLiveStream> singleLiveStreamList = singleLiveStreamRepository.findAll();
        assertThat(singleLiveStreamList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = singleLiveStreamRepository.findAll().size();
        // set the field null
        singleLiveStream.setIsDeleted(null);

        // Create the SingleLiveStream, which fails.
        SingleLiveStreamDTO singleLiveStreamDTO = singleLiveStreamMapper.toDto(singleLiveStream);

        restSingleLiveStreamMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(singleLiveStreamDTO))
            )
            .andExpect(status().isBadRequest());

        List<SingleLiveStream> singleLiveStreamList = singleLiveStreamRepository.findAll();
        assertThat(singleLiveStreamList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSingleLiveStreams() throws Exception {
        // Initialize the database
        singleLiveStreamRepository.saveAndFlush(singleLiveStream);

        // Get all the singleLiveStreamList
        restSingleLiveStreamMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(singleLiveStream.getId().intValue())))
            .andExpect(jsonPath("$.[*].thumbnailContentType").value(hasItem(DEFAULT_THUMBNAIL_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].thumbnailS3Key").value(hasItem(DEFAULT_THUMBNAIL_S_3_KEY)))
            .andExpect(jsonPath("$.[*].contentContentType").value(hasItem(DEFAULT_CONTENT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].contentS3Key").value(hasItem(DEFAULT_CONTENT_S_3_KEY)))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())));
    }

    @Test
    @Transactional
    void getSingleLiveStream() throws Exception {
        // Initialize the database
        singleLiveStreamRepository.saveAndFlush(singleLiveStream);

        // Get the singleLiveStream
        restSingleLiveStreamMockMvc
            .perform(get(ENTITY_API_URL_ID, singleLiveStream.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(singleLiveStream.getId().intValue()))
            .andExpect(jsonPath("$.thumbnailContentType").value(DEFAULT_THUMBNAIL_CONTENT_TYPE))
            .andExpect(jsonPath("$.thumbnailS3Key").value(DEFAULT_THUMBNAIL_S_3_KEY))
            .andExpect(jsonPath("$.contentContentType").value(DEFAULT_CONTENT_CONTENT_TYPE))
            .andExpect(jsonPath("$.contentS3Key").value(DEFAULT_CONTENT_S_3_KEY))
            .andExpect(jsonPath("$.duration").value(DEFAULT_DURATION.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingSingleLiveStream() throws Exception {
        // Get the singleLiveStream
        restSingleLiveStreamMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSingleLiveStream() throws Exception {
        // Initialize the database
        singleLiveStreamRepository.saveAndFlush(singleLiveStream);

        int databaseSizeBeforeUpdate = singleLiveStreamRepository.findAll().size();

        // Update the singleLiveStream
        SingleLiveStream updatedSingleLiveStream = singleLiveStreamRepository.findById(singleLiveStream.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSingleLiveStream are not directly saved in db
        em.detach(updatedSingleLiveStream);
        updatedSingleLiveStream
            .thumbnailContentType(UPDATED_THUMBNAIL_CONTENT_TYPE)
            .thumbnailS3Key(UPDATED_THUMBNAIL_S_3_KEY)
            .contentContentType(UPDATED_CONTENT_CONTENT_TYPE)
            .contentS3Key(UPDATED_CONTENT_S_3_KEY)
            .duration(UPDATED_DURATION)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        SingleLiveStreamDTO singleLiveStreamDTO = singleLiveStreamMapper.toDto(updatedSingleLiveStream);

        restSingleLiveStreamMockMvc
            .perform(
                put(ENTITY_API_URL_ID, singleLiveStreamDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(singleLiveStreamDTO))
            )
            .andExpect(status().isOk());

        // Validate the SingleLiveStream in the database
        List<SingleLiveStream> singleLiveStreamList = singleLiveStreamRepository.findAll();
        assertThat(singleLiveStreamList).hasSize(databaseSizeBeforeUpdate);
        SingleLiveStream testSingleLiveStream = singleLiveStreamList.get(singleLiveStreamList.size() - 1);
        assertThat(testSingleLiveStream.getThumbnailContentType()).isEqualTo(UPDATED_THUMBNAIL_CONTENT_TYPE);
        assertThat(testSingleLiveStream.getThumbnailS3Key()).isEqualTo(UPDATED_THUMBNAIL_S_3_KEY);
        assertThat(testSingleLiveStream.getContentContentType()).isEqualTo(UPDATED_CONTENT_CONTENT_TYPE);
        assertThat(testSingleLiveStream.getContentS3Key()).isEqualTo(UPDATED_CONTENT_S_3_KEY);
        assertThat(testSingleLiveStream.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testSingleLiveStream.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testSingleLiveStream.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testSingleLiveStream.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testSingleLiveStream.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testSingleLiveStream.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void putNonExistingSingleLiveStream() throws Exception {
        int databaseSizeBeforeUpdate = singleLiveStreamRepository.findAll().size();
        singleLiveStream.setId(longCount.incrementAndGet());

        // Create the SingleLiveStream
        SingleLiveStreamDTO singleLiveStreamDTO = singleLiveStreamMapper.toDto(singleLiveStream);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSingleLiveStreamMockMvc
            .perform(
                put(ENTITY_API_URL_ID, singleLiveStreamDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(singleLiveStreamDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SingleLiveStream in the database
        List<SingleLiveStream> singleLiveStreamList = singleLiveStreamRepository.findAll();
        assertThat(singleLiveStreamList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSingleLiveStream() throws Exception {
        int databaseSizeBeforeUpdate = singleLiveStreamRepository.findAll().size();
        singleLiveStream.setId(longCount.incrementAndGet());

        // Create the SingleLiveStream
        SingleLiveStreamDTO singleLiveStreamDTO = singleLiveStreamMapper.toDto(singleLiveStream);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSingleLiveStreamMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(singleLiveStreamDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SingleLiveStream in the database
        List<SingleLiveStream> singleLiveStreamList = singleLiveStreamRepository.findAll();
        assertThat(singleLiveStreamList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSingleLiveStream() throws Exception {
        int databaseSizeBeforeUpdate = singleLiveStreamRepository.findAll().size();
        singleLiveStream.setId(longCount.incrementAndGet());

        // Create the SingleLiveStream
        SingleLiveStreamDTO singleLiveStreamDTO = singleLiveStreamMapper.toDto(singleLiveStream);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSingleLiveStreamMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(singleLiveStreamDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SingleLiveStream in the database
        List<SingleLiveStream> singleLiveStreamList = singleLiveStreamRepository.findAll();
        assertThat(singleLiveStreamList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSingleLiveStreamWithPatch() throws Exception {
        // Initialize the database
        singleLiveStreamRepository.saveAndFlush(singleLiveStream);

        int databaseSizeBeforeUpdate = singleLiveStreamRepository.findAll().size();

        // Update the singleLiveStream using partial update
        SingleLiveStream partialUpdatedSingleLiveStream = new SingleLiveStream();
        partialUpdatedSingleLiveStream.setId(singleLiveStream.getId());

        partialUpdatedSingleLiveStream
            .thumbnailS3Key(UPDATED_THUMBNAIL_S_3_KEY)
            .contentS3Key(UPDATED_CONTENT_S_3_KEY)
            .duration(UPDATED_DURATION)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        restSingleLiveStreamMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSingleLiveStream.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSingleLiveStream))
            )
            .andExpect(status().isOk());

        // Validate the SingleLiveStream in the database
        List<SingleLiveStream> singleLiveStreamList = singleLiveStreamRepository.findAll();
        assertThat(singleLiveStreamList).hasSize(databaseSizeBeforeUpdate);
        SingleLiveStream testSingleLiveStream = singleLiveStreamList.get(singleLiveStreamList.size() - 1);
        assertThat(testSingleLiveStream.getThumbnailContentType()).isEqualTo(DEFAULT_THUMBNAIL_CONTENT_TYPE);
        assertThat(testSingleLiveStream.getThumbnailS3Key()).isEqualTo(UPDATED_THUMBNAIL_S_3_KEY);
        assertThat(testSingleLiveStream.getContentContentType()).isEqualTo(DEFAULT_CONTENT_CONTENT_TYPE);
        assertThat(testSingleLiveStream.getContentS3Key()).isEqualTo(UPDATED_CONTENT_S_3_KEY);
        assertThat(testSingleLiveStream.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testSingleLiveStream.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testSingleLiveStream.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testSingleLiveStream.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testSingleLiveStream.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testSingleLiveStream.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    @Transactional
    void fullUpdateSingleLiveStreamWithPatch() throws Exception {
        // Initialize the database
        singleLiveStreamRepository.saveAndFlush(singleLiveStream);

        int databaseSizeBeforeUpdate = singleLiveStreamRepository.findAll().size();

        // Update the singleLiveStream using partial update
        SingleLiveStream partialUpdatedSingleLiveStream = new SingleLiveStream();
        partialUpdatedSingleLiveStream.setId(singleLiveStream.getId());

        partialUpdatedSingleLiveStream
            .thumbnailContentType(UPDATED_THUMBNAIL_CONTENT_TYPE)
            .thumbnailS3Key(UPDATED_THUMBNAIL_S_3_KEY)
            .contentContentType(UPDATED_CONTENT_CONTENT_TYPE)
            .contentS3Key(UPDATED_CONTENT_S_3_KEY)
            .duration(UPDATED_DURATION)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);

        restSingleLiveStreamMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSingleLiveStream.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSingleLiveStream))
            )
            .andExpect(status().isOk());

        // Validate the SingleLiveStream in the database
        List<SingleLiveStream> singleLiveStreamList = singleLiveStreamRepository.findAll();
        assertThat(singleLiveStreamList).hasSize(databaseSizeBeforeUpdate);
        SingleLiveStream testSingleLiveStream = singleLiveStreamList.get(singleLiveStreamList.size() - 1);
        assertThat(testSingleLiveStream.getThumbnailContentType()).isEqualTo(UPDATED_THUMBNAIL_CONTENT_TYPE);
        assertThat(testSingleLiveStream.getThumbnailS3Key()).isEqualTo(UPDATED_THUMBNAIL_S_3_KEY);
        assertThat(testSingleLiveStream.getContentContentType()).isEqualTo(UPDATED_CONTENT_CONTENT_TYPE);
        assertThat(testSingleLiveStream.getContentS3Key()).isEqualTo(UPDATED_CONTENT_S_3_KEY);
        assertThat(testSingleLiveStream.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testSingleLiveStream.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testSingleLiveStream.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testSingleLiveStream.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testSingleLiveStream.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testSingleLiveStream.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void patchNonExistingSingleLiveStream() throws Exception {
        int databaseSizeBeforeUpdate = singleLiveStreamRepository.findAll().size();
        singleLiveStream.setId(longCount.incrementAndGet());

        // Create the SingleLiveStream
        SingleLiveStreamDTO singleLiveStreamDTO = singleLiveStreamMapper.toDto(singleLiveStream);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSingleLiveStreamMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, singleLiveStreamDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(singleLiveStreamDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SingleLiveStream in the database
        List<SingleLiveStream> singleLiveStreamList = singleLiveStreamRepository.findAll();
        assertThat(singleLiveStreamList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSingleLiveStream() throws Exception {
        int databaseSizeBeforeUpdate = singleLiveStreamRepository.findAll().size();
        singleLiveStream.setId(longCount.incrementAndGet());

        // Create the SingleLiveStream
        SingleLiveStreamDTO singleLiveStreamDTO = singleLiveStreamMapper.toDto(singleLiveStream);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSingleLiveStreamMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(singleLiveStreamDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SingleLiveStream in the database
        List<SingleLiveStream> singleLiveStreamList = singleLiveStreamRepository.findAll();
        assertThat(singleLiveStreamList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSingleLiveStream() throws Exception {
        int databaseSizeBeforeUpdate = singleLiveStreamRepository.findAll().size();
        singleLiveStream.setId(longCount.incrementAndGet());

        // Create the SingleLiveStream
        SingleLiveStreamDTO singleLiveStreamDTO = singleLiveStreamMapper.toDto(singleLiveStream);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSingleLiveStreamMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(singleLiveStreamDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SingleLiveStream in the database
        List<SingleLiveStream> singleLiveStreamList = singleLiveStreamRepository.findAll();
        assertThat(singleLiveStreamList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSingleLiveStream() throws Exception {
        // Initialize the database
        singleLiveStreamRepository.saveAndFlush(singleLiveStream);

        int databaseSizeBeforeDelete = singleLiveStreamRepository.findAll().size();

        // Delete the singleLiveStream
        restSingleLiveStreamMockMvc
            .perform(delete(ENTITY_API_URL_ID, singleLiveStream.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SingleLiveStream> singleLiveStreamList = singleLiveStreamRepository.findAll();
        assertThat(singleLiveStreamList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
