package com.fanflip.multimedia.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fanflip.multimedia.IntegrationTest;
import com.fanflip.multimedia.domain.ContentPackage;
import com.fanflip.multimedia.domain.SingleAudio;
import com.fanflip.multimedia.repository.SingleAudioRepository;
import com.fanflip.multimedia.service.dto.SingleAudioDTO;
import com.fanflip.multimedia.service.mapper.SingleAudioMapper;
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
 * Integration tests for the {@link SingleAudioResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SingleAudioResourceIT {



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

    private static final String ENTITY_API_URL = "/api/single-audios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SingleAudioRepository singleAudioRepository;

    @Autowired
    private SingleAudioMapper singleAudioMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSingleAudioMockMvc;

    private SingleAudio singleAudio;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SingleAudio createEntity(EntityManager em) {
        SingleAudio singleAudio = new SingleAudio()
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
        singleAudio.setContentPackage(contentPackage);
        return singleAudio;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SingleAudio createUpdatedEntity(EntityManager em) {
        SingleAudio singleAudio = new SingleAudio()

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
        singleAudio.setContentPackage(contentPackage);
        return singleAudio;
    }

    @BeforeEach
    public void initTest() {
        singleAudio = createEntity(em);
    }

    @Test
    @Transactional
    void createSingleAudio() throws Exception {
        int databaseSizeBeforeCreate = singleAudioRepository.findAll().size();
        // Create the SingleAudio
        SingleAudioDTO singleAudioDTO = singleAudioMapper.toDto(singleAudio);
        restSingleAudioMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(singleAudioDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SingleAudio in the database
        List<SingleAudio> singleAudioList = singleAudioRepository.findAll();
        assertThat(singleAudioList).hasSize(databaseSizeBeforeCreate + 1);
        SingleAudio testSingleAudio = singleAudioList.get(singleAudioList.size() - 1);
        assertThat(testSingleAudio.getThumbnailContentType()).isEqualTo(DEFAULT_THUMBNAIL_CONTENT_TYPE);
        assertThat(testSingleAudio.getThumbnailS3Key()).isEqualTo(DEFAULT_THUMBNAIL_S_3_KEY);
        assertThat(testSingleAudio.getContentContentType()).isEqualTo(DEFAULT_CONTENT_CONTENT_TYPE);
        assertThat(testSingleAudio.getContentS3Key()).isEqualTo(DEFAULT_CONTENT_S_3_KEY);
        assertThat(testSingleAudio.getDuration()).isEqualTo(DEFAULT_DURATION);
        assertThat(testSingleAudio.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testSingleAudio.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testSingleAudio.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testSingleAudio.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testSingleAudio.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    @Transactional
    void createSingleAudioWithExistingId() throws Exception {
        // Create the SingleAudio with an existing ID
        singleAudio.setId(1L);
        SingleAudioDTO singleAudioDTO = singleAudioMapper.toDto(singleAudio);

        int databaseSizeBeforeCreate = singleAudioRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSingleAudioMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(singleAudioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SingleAudio in the database
        List<SingleAudio> singleAudioList = singleAudioRepository.findAll();
        assertThat(singleAudioList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkThumbnailS3KeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = singleAudioRepository.findAll().size();
        // set the field null
        singleAudio.setThumbnailS3Key(null);

        // Create the SingleAudio, which fails.
        SingleAudioDTO singleAudioDTO = singleAudioMapper.toDto(singleAudio);

        restSingleAudioMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(singleAudioDTO))
            )
            .andExpect(status().isBadRequest());

        List<SingleAudio> singleAudioList = singleAudioRepository.findAll();
        assertThat(singleAudioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkContentS3KeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = singleAudioRepository.findAll().size();
        // set the field null
        singleAudio.setContentS3Key(null);

        // Create the SingleAudio, which fails.
        SingleAudioDTO singleAudioDTO = singleAudioMapper.toDto(singleAudio);

        restSingleAudioMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(singleAudioDTO))
            )
            .andExpect(status().isBadRequest());

        List<SingleAudio> singleAudioList = singleAudioRepository.findAll();
        assertThat(singleAudioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = singleAudioRepository.findAll().size();
        // set the field null
        singleAudio.setCreatedDate(null);

        // Create the SingleAudio, which fails.
        SingleAudioDTO singleAudioDTO = singleAudioMapper.toDto(singleAudio);

        restSingleAudioMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(singleAudioDTO))
            )
            .andExpect(status().isBadRequest());

        List<SingleAudio> singleAudioList = singleAudioRepository.findAll();
        assertThat(singleAudioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = singleAudioRepository.findAll().size();
        // set the field null
        singleAudio.setIsDeleted(null);

        // Create the SingleAudio, which fails.
        SingleAudioDTO singleAudioDTO = singleAudioMapper.toDto(singleAudio);

        restSingleAudioMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(singleAudioDTO))
            )
            .andExpect(status().isBadRequest());

        List<SingleAudio> singleAudioList = singleAudioRepository.findAll();
        assertThat(singleAudioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSingleAudios() throws Exception {
        // Initialize the database
        singleAudioRepository.saveAndFlush(singleAudio);

        // Get all the singleAudioList
        restSingleAudioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(singleAudio.getId().intValue())))
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
    void getSingleAudio() throws Exception {
        // Initialize the database
        singleAudioRepository.saveAndFlush(singleAudio);

        // Get the singleAudio
        restSingleAudioMockMvc
            .perform(get(ENTITY_API_URL_ID, singleAudio.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(singleAudio.getId().intValue()))
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
    void getNonExistingSingleAudio() throws Exception {
        // Get the singleAudio
        restSingleAudioMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSingleAudio() throws Exception {
        // Initialize the database
        singleAudioRepository.saveAndFlush(singleAudio);

        int databaseSizeBeforeUpdate = singleAudioRepository.findAll().size();

        // Update the singleAudio
        SingleAudio updatedSingleAudio = singleAudioRepository.findById(singleAudio.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSingleAudio are not directly saved in db
        em.detach(updatedSingleAudio);
        updatedSingleAudio
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
        SingleAudioDTO singleAudioDTO = singleAudioMapper.toDto(updatedSingleAudio);

        restSingleAudioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, singleAudioDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(singleAudioDTO))
            )
            .andExpect(status().isOk());

        // Validate the SingleAudio in the database
        List<SingleAudio> singleAudioList = singleAudioRepository.findAll();
        assertThat(singleAudioList).hasSize(databaseSizeBeforeUpdate);
        SingleAudio testSingleAudio = singleAudioList.get(singleAudioList.size() - 1);
        assertThat(testSingleAudio.getThumbnailContentType()).isEqualTo(UPDATED_THUMBNAIL_CONTENT_TYPE);
        assertThat(testSingleAudio.getThumbnailS3Key()).isEqualTo(UPDATED_THUMBNAIL_S_3_KEY);
        assertThat(testSingleAudio.getContentContentType()).isEqualTo(UPDATED_CONTENT_CONTENT_TYPE);
        assertThat(testSingleAudio.getContentS3Key()).isEqualTo(UPDATED_CONTENT_S_3_KEY);
        assertThat(testSingleAudio.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testSingleAudio.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testSingleAudio.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testSingleAudio.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testSingleAudio.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testSingleAudio.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void putNonExistingSingleAudio() throws Exception {
        int databaseSizeBeforeUpdate = singleAudioRepository.findAll().size();
        singleAudio.setId(longCount.incrementAndGet());

        // Create the SingleAudio
        SingleAudioDTO singleAudioDTO = singleAudioMapper.toDto(singleAudio);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSingleAudioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, singleAudioDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(singleAudioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SingleAudio in the database
        List<SingleAudio> singleAudioList = singleAudioRepository.findAll();
        assertThat(singleAudioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSingleAudio() throws Exception {
        int databaseSizeBeforeUpdate = singleAudioRepository.findAll().size();
        singleAudio.setId(longCount.incrementAndGet());

        // Create the SingleAudio
        SingleAudioDTO singleAudioDTO = singleAudioMapper.toDto(singleAudio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSingleAudioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(singleAudioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SingleAudio in the database
        List<SingleAudio> singleAudioList = singleAudioRepository.findAll();
        assertThat(singleAudioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSingleAudio() throws Exception {
        int databaseSizeBeforeUpdate = singleAudioRepository.findAll().size();
        singleAudio.setId(longCount.incrementAndGet());

        // Create the SingleAudio
        SingleAudioDTO singleAudioDTO = singleAudioMapper.toDto(singleAudio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSingleAudioMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(singleAudioDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SingleAudio in the database
        List<SingleAudio> singleAudioList = singleAudioRepository.findAll();
        assertThat(singleAudioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSingleAudioWithPatch() throws Exception {
        // Initialize the database
        singleAudioRepository.saveAndFlush(singleAudio);

        int databaseSizeBeforeUpdate = singleAudioRepository.findAll().size();

        // Update the singleAudio using partial update
        SingleAudio partialUpdatedSingleAudio = new SingleAudio();
        partialUpdatedSingleAudio.setId(singleAudio.getId());

        partialUpdatedSingleAudio
            .thumbnailS3Key(UPDATED_THUMBNAIL_S_3_KEY)
            .contentS3Key(UPDATED_CONTENT_S_3_KEY)
            .duration(UPDATED_DURATION)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        restSingleAudioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSingleAudio.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSingleAudio))
            )
            .andExpect(status().isOk());

        // Validate the SingleAudio in the database
        List<SingleAudio> singleAudioList = singleAudioRepository.findAll();
        assertThat(singleAudioList).hasSize(databaseSizeBeforeUpdate);
        SingleAudio testSingleAudio = singleAudioList.get(singleAudioList.size() - 1);
        assertThat(testSingleAudio.getThumbnailContentType()).isEqualTo(DEFAULT_THUMBNAIL_CONTENT_TYPE);
        assertThat(testSingleAudio.getThumbnailS3Key()).isEqualTo(UPDATED_THUMBNAIL_S_3_KEY);
        assertThat(testSingleAudio.getContentContentType()).isEqualTo(DEFAULT_CONTENT_CONTENT_TYPE);
        assertThat(testSingleAudio.getContentS3Key()).isEqualTo(UPDATED_CONTENT_S_3_KEY);
        assertThat(testSingleAudio.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testSingleAudio.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testSingleAudio.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testSingleAudio.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testSingleAudio.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testSingleAudio.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    @Transactional
    void fullUpdateSingleAudioWithPatch() throws Exception {
        // Initialize the database
        singleAudioRepository.saveAndFlush(singleAudio);

        int databaseSizeBeforeUpdate = singleAudioRepository.findAll().size();

        // Update the singleAudio using partial update
        SingleAudio partialUpdatedSingleAudio = new SingleAudio();
        partialUpdatedSingleAudio.setId(singleAudio.getId());

        partialUpdatedSingleAudio
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

        restSingleAudioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSingleAudio.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSingleAudio))
            )
            .andExpect(status().isOk());

        // Validate the SingleAudio in the database
        List<SingleAudio> singleAudioList = singleAudioRepository.findAll();
        assertThat(singleAudioList).hasSize(databaseSizeBeforeUpdate);
        SingleAudio testSingleAudio = singleAudioList.get(singleAudioList.size() - 1);
        assertThat(testSingleAudio.getThumbnailContentType()).isEqualTo(UPDATED_THUMBNAIL_CONTENT_TYPE);
        assertThat(testSingleAudio.getThumbnailS3Key()).isEqualTo(UPDATED_THUMBNAIL_S_3_KEY);
        assertThat(testSingleAudio.getContentContentType()).isEqualTo(UPDATED_CONTENT_CONTENT_TYPE);
        assertThat(testSingleAudio.getContentS3Key()).isEqualTo(UPDATED_CONTENT_S_3_KEY);
        assertThat(testSingleAudio.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testSingleAudio.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testSingleAudio.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testSingleAudio.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testSingleAudio.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testSingleAudio.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void patchNonExistingSingleAudio() throws Exception {
        int databaseSizeBeforeUpdate = singleAudioRepository.findAll().size();
        singleAudio.setId(longCount.incrementAndGet());

        // Create the SingleAudio
        SingleAudioDTO singleAudioDTO = singleAudioMapper.toDto(singleAudio);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSingleAudioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, singleAudioDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(singleAudioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SingleAudio in the database
        List<SingleAudio> singleAudioList = singleAudioRepository.findAll();
        assertThat(singleAudioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSingleAudio() throws Exception {
        int databaseSizeBeforeUpdate = singleAudioRepository.findAll().size();
        singleAudio.setId(longCount.incrementAndGet());

        // Create the SingleAudio
        SingleAudioDTO singleAudioDTO = singleAudioMapper.toDto(singleAudio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSingleAudioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(singleAudioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SingleAudio in the database
        List<SingleAudio> singleAudioList = singleAudioRepository.findAll();
        assertThat(singleAudioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSingleAudio() throws Exception {
        int databaseSizeBeforeUpdate = singleAudioRepository.findAll().size();
        singleAudio.setId(longCount.incrementAndGet());

        // Create the SingleAudio
        SingleAudioDTO singleAudioDTO = singleAudioMapper.toDto(singleAudio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSingleAudioMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(singleAudioDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SingleAudio in the database
        List<SingleAudio> singleAudioList = singleAudioRepository.findAll();
        assertThat(singleAudioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSingleAudio() throws Exception {
        // Initialize the database
        singleAudioRepository.saveAndFlush(singleAudio);

        int databaseSizeBeforeDelete = singleAudioRepository.findAll().size();

        // Delete the singleAudio
        restSingleAudioMockMvc
            .perform(delete(ENTITY_API_URL_ID, singleAudio.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SingleAudio> singleAudioList = singleAudioRepository.findAll();
        assertThat(singleAudioList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
