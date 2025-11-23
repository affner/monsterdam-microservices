package com.fanflip.multimedia.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fanflip.multimedia.IntegrationTest;
import com.fanflip.multimedia.domain.VideoStory;
import com.fanflip.multimedia.repository.VideoStoryRepository;
import com.fanflip.multimedia.service.dto.VideoStoryDTO;
import com.fanflip.multimedia.service.mapper.VideoStoryMapper;
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
 * Integration tests for the {@link VideoStoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VideoStoryResourceIT {

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
    private VideoStoryRepository videoStoryRepository;

    @Autowired
    private VideoStoryMapper videoStoryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVideoStoryMockMvc;

    private VideoStory videoStory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VideoStory createEntity(EntityManager em) {
        VideoStory videoStory = new VideoStory()
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
        return videoStory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VideoStory createUpdatedEntity(EntityManager em) {
        VideoStory videoStory = new VideoStory()
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
        return videoStory;
    }

    @BeforeEach
    public void initTest() {
        videoStory = createEntity(em);
    }

    @Test
    @Transactional
    void createVideoStory() throws Exception {
        int databaseSizeBeforeCreate = videoStoryRepository.findAll().size();
        // Create the VideoStory
        VideoStoryDTO videoStoryDTO = videoStoryMapper.toDto(videoStory);
        restVideoStoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(videoStoryDTO)))
            .andExpect(status().isCreated());

        // Validate the VideoStory in the database
        List<VideoStory> videoStoryList = videoStoryRepository.findAll();
        assertThat(videoStoryList).hasSize(databaseSizeBeforeCreate + 1);
        VideoStory testVideoStory = videoStoryList.get(videoStoryList.size() - 1);
        assertThat(testVideoStory.getThumbnailContentType()).isEqualTo(DEFAULT_THUMBNAIL_CONTENT_TYPE);
        assertThat(testVideoStory.getThumbnailS3Key()).isEqualTo(DEFAULT_THUMBNAIL_S_3_KEY);
        assertThat(testVideoStory.getContentContentType()).isEqualTo(DEFAULT_CONTENT_CONTENT_TYPE);
        assertThat(testVideoStory.getContentS3Key()).isEqualTo(DEFAULT_CONTENT_S_3_KEY);
        assertThat(testVideoStory.getDuration()).isEqualTo(DEFAULT_DURATION);
        assertThat(testVideoStory.getLikeCount()).isEqualTo(DEFAULT_LIKE_COUNT);
        assertThat(testVideoStory.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testVideoStory.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testVideoStory.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testVideoStory.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testVideoStory.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
        assertThat(testVideoStory.getCreatorId()).isEqualTo(DEFAULT_CREATOR_ID);
    }

    @Test
    @Transactional
    void createVideoStoryWithExistingId() throws Exception {
        // Create the VideoStory with an existing ID
        videoStory.setId(1L);
        VideoStoryDTO videoStoryDTO = videoStoryMapper.toDto(videoStory);

        int databaseSizeBeforeCreate = videoStoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVideoStoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(videoStoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the VideoStory in the database
        List<VideoStory> videoStoryList = videoStoryRepository.findAll();
        assertThat(videoStoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkThumbnailS3KeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = videoStoryRepository.findAll().size();
        // set the field null
        videoStory.setThumbnailS3Key(null);

        // Create the VideoStory, which fails.
        VideoStoryDTO videoStoryDTO = videoStoryMapper.toDto(videoStory);

        restVideoStoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(videoStoryDTO)))
            .andExpect(status().isBadRequest());

        List<VideoStory> videoStoryList = videoStoryRepository.findAll();
        assertThat(videoStoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkContentS3KeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = videoStoryRepository.findAll().size();
        // set the field null
        videoStory.setContentS3Key(null);

        // Create the VideoStory, which fails.
        VideoStoryDTO videoStoryDTO = videoStoryMapper.toDto(videoStory);

        restVideoStoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(videoStoryDTO)))
            .andExpect(status().isBadRequest());

        List<VideoStory> videoStoryList = videoStoryRepository.findAll();
        assertThat(videoStoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = videoStoryRepository.findAll().size();
        // set the field null
        videoStory.setCreatedDate(null);

        // Create the VideoStory, which fails.
        VideoStoryDTO videoStoryDTO = videoStoryMapper.toDto(videoStory);

        restVideoStoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(videoStoryDTO)))
            .andExpect(status().isBadRequest());

        List<VideoStory> videoStoryList = videoStoryRepository.findAll();
        assertThat(videoStoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = videoStoryRepository.findAll().size();
        // set the field null
        videoStory.setIsDeleted(null);

        // Create the VideoStory, which fails.
        VideoStoryDTO videoStoryDTO = videoStoryMapper.toDto(videoStory);

        restVideoStoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(videoStoryDTO)))
            .andExpect(status().isBadRequest());

        List<VideoStory> videoStoryList = videoStoryRepository.findAll();
        assertThat(videoStoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatorIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = videoStoryRepository.findAll().size();
        // set the field null
        videoStory.setCreatorId(null);

        // Create the VideoStory, which fails.
        VideoStoryDTO videoStoryDTO = videoStoryMapper.toDto(videoStory);

        restVideoStoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(videoStoryDTO)))
            .andExpect(status().isBadRequest());

        List<VideoStory> videoStoryList = videoStoryRepository.findAll();
        assertThat(videoStoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllVideoStories() throws Exception {
        // Initialize the database
        videoStoryRepository.saveAndFlush(videoStory);

        // Get all the videoStoryList
        restVideoStoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(videoStory.getId().intValue())))
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
    void getVideoStory() throws Exception {
        // Initialize the database
        videoStoryRepository.saveAndFlush(videoStory);

        // Get the videoStory
        restVideoStoryMockMvc
            .perform(get(ENTITY_API_URL_ID, videoStory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(videoStory.getId().intValue()))
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
    void getNonExistingVideoStory() throws Exception {
        // Get the videoStory
        restVideoStoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVideoStory() throws Exception {
        // Initialize the database
        videoStoryRepository.saveAndFlush(videoStory);

        int databaseSizeBeforeUpdate = videoStoryRepository.findAll().size();

        // Update the videoStory
        VideoStory updatedVideoStory = videoStoryRepository.findById(videoStory.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedVideoStory are not directly saved in db
        em.detach(updatedVideoStory);
        updatedVideoStory
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
        VideoStoryDTO videoStoryDTO = videoStoryMapper.toDto(updatedVideoStory);

        restVideoStoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, videoStoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(videoStoryDTO))
            )
            .andExpect(status().isOk());

        // Validate the VideoStory in the database
        List<VideoStory> videoStoryList = videoStoryRepository.findAll();
        assertThat(videoStoryList).hasSize(databaseSizeBeforeUpdate);
        VideoStory testVideoStory = videoStoryList.get(videoStoryList.size() - 1);
        assertThat(testVideoStory.getThumbnailContentType()).isEqualTo(UPDATED_THUMBNAIL_CONTENT_TYPE);
        assertThat(testVideoStory.getThumbnailS3Key()).isEqualTo(UPDATED_THUMBNAIL_S_3_KEY);
        assertThat(testVideoStory.getContentContentType()).isEqualTo(UPDATED_CONTENT_CONTENT_TYPE);
        assertThat(testVideoStory.getContentS3Key()).isEqualTo(UPDATED_CONTENT_S_3_KEY);
        assertThat(testVideoStory.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testVideoStory.getLikeCount()).isEqualTo(UPDATED_LIKE_COUNT);
        assertThat(testVideoStory.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testVideoStory.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testVideoStory.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testVideoStory.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testVideoStory.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testVideoStory.getCreatorId()).isEqualTo(UPDATED_CREATOR_ID);
    }

    @Test
    @Transactional
    void putNonExistingVideoStory() throws Exception {
        int databaseSizeBeforeUpdate = videoStoryRepository.findAll().size();
        videoStory.setId(longCount.incrementAndGet());

        // Create the VideoStory
        VideoStoryDTO videoStoryDTO = videoStoryMapper.toDto(videoStory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVideoStoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, videoStoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(videoStoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VideoStory in the database
        List<VideoStory> videoStoryList = videoStoryRepository.findAll();
        assertThat(videoStoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVideoStory() throws Exception {
        int databaseSizeBeforeUpdate = videoStoryRepository.findAll().size();
        videoStory.setId(longCount.incrementAndGet());

        // Create the VideoStory
        VideoStoryDTO videoStoryDTO = videoStoryMapper.toDto(videoStory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVideoStoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(videoStoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VideoStory in the database
        List<VideoStory> videoStoryList = videoStoryRepository.findAll();
        assertThat(videoStoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVideoStory() throws Exception {
        int databaseSizeBeforeUpdate = videoStoryRepository.findAll().size();
        videoStory.setId(longCount.incrementAndGet());

        // Create the VideoStory
        VideoStoryDTO videoStoryDTO = videoStoryMapper.toDto(videoStory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVideoStoryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(videoStoryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the VideoStory in the database
        List<VideoStory> videoStoryList = videoStoryRepository.findAll();
        assertThat(videoStoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVideoStoryWithPatch() throws Exception {
        // Initialize the database
        videoStoryRepository.saveAndFlush(videoStory);

        int databaseSizeBeforeUpdate = videoStoryRepository.findAll().size();

        // Update the videoStory using partial update
        VideoStory partialUpdatedVideoStory = new VideoStory();
        partialUpdatedVideoStory.setId(videoStory.getId());

        partialUpdatedVideoStory
            .contentS3Key(UPDATED_CONTENT_S_3_KEY)
            .likeCount(UPDATED_LIKE_COUNT)
            .createdBy(UPDATED_CREATED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .creatorId(UPDATED_CREATOR_ID);

        restVideoStoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVideoStory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVideoStory))
            )
            .andExpect(status().isOk());

        // Validate the VideoStory in the database
        List<VideoStory> videoStoryList = videoStoryRepository.findAll();
        assertThat(videoStoryList).hasSize(databaseSizeBeforeUpdate);
        VideoStory testVideoStory = videoStoryList.get(videoStoryList.size() - 1);
        assertThat(testVideoStory.getThumbnailContentType()).isEqualTo(DEFAULT_THUMBNAIL_CONTENT_TYPE);
        assertThat(testVideoStory.getThumbnailS3Key()).isEqualTo(DEFAULT_THUMBNAIL_S_3_KEY);
        assertThat(testVideoStory.getContentContentType()).isEqualTo(DEFAULT_CONTENT_CONTENT_TYPE);
        assertThat(testVideoStory.getContentS3Key()).isEqualTo(UPDATED_CONTENT_S_3_KEY);
        assertThat(testVideoStory.getDuration()).isEqualTo(DEFAULT_DURATION);
        assertThat(testVideoStory.getLikeCount()).isEqualTo(UPDATED_LIKE_COUNT);
        assertThat(testVideoStory.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testVideoStory.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testVideoStory.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testVideoStory.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testVideoStory.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testVideoStory.getCreatorId()).isEqualTo(UPDATED_CREATOR_ID);
    }

    @Test
    @Transactional
    void fullUpdateVideoStoryWithPatch() throws Exception {
        // Initialize the database
        videoStoryRepository.saveAndFlush(videoStory);

        int databaseSizeBeforeUpdate = videoStoryRepository.findAll().size();

        // Update the videoStory using partial update
        VideoStory partialUpdatedVideoStory = new VideoStory();
        partialUpdatedVideoStory.setId(videoStory.getId());

        partialUpdatedVideoStory
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

        restVideoStoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVideoStory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVideoStory))
            )
            .andExpect(status().isOk());

        // Validate the VideoStory in the database
        List<VideoStory> videoStoryList = videoStoryRepository.findAll();
        assertThat(videoStoryList).hasSize(databaseSizeBeforeUpdate);
        VideoStory testVideoStory = videoStoryList.get(videoStoryList.size() - 1);
        assertThat(testVideoStory.getThumbnailContentType()).isEqualTo(UPDATED_THUMBNAIL_CONTENT_TYPE);
        assertThat(testVideoStory.getThumbnailS3Key()).isEqualTo(UPDATED_THUMBNAIL_S_3_KEY);
        assertThat(testVideoStory.getContentContentType()).isEqualTo(UPDATED_CONTENT_CONTENT_TYPE);
        assertThat(testVideoStory.getContentS3Key()).isEqualTo(UPDATED_CONTENT_S_3_KEY);
        assertThat(testVideoStory.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testVideoStory.getLikeCount()).isEqualTo(UPDATED_LIKE_COUNT);
        assertThat(testVideoStory.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testVideoStory.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testVideoStory.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testVideoStory.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testVideoStory.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testVideoStory.getCreatorId()).isEqualTo(UPDATED_CREATOR_ID);
    }

    @Test
    @Transactional
    void patchNonExistingVideoStory() throws Exception {
        int databaseSizeBeforeUpdate = videoStoryRepository.findAll().size();
        videoStory.setId(longCount.incrementAndGet());

        // Create the VideoStory
        VideoStoryDTO videoStoryDTO = videoStoryMapper.toDto(videoStory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVideoStoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, videoStoryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(videoStoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VideoStory in the database
        List<VideoStory> videoStoryList = videoStoryRepository.findAll();
        assertThat(videoStoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVideoStory() throws Exception {
        int databaseSizeBeforeUpdate = videoStoryRepository.findAll().size();
        videoStory.setId(longCount.incrementAndGet());

        // Create the VideoStory
        VideoStoryDTO videoStoryDTO = videoStoryMapper.toDto(videoStory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVideoStoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(videoStoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VideoStory in the database
        List<VideoStory> videoStoryList = videoStoryRepository.findAll();
        assertThat(videoStoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVideoStory() throws Exception {
        int databaseSizeBeforeUpdate = videoStoryRepository.findAll().size();
        videoStory.setId(longCount.incrementAndGet());

        // Create the VideoStory
        VideoStoryDTO videoStoryDTO = videoStoryMapper.toDto(videoStory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVideoStoryMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(videoStoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the VideoStory in the database
        List<VideoStory> videoStoryList = videoStoryRepository.findAll();
        assertThat(videoStoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVideoStory() throws Exception {
        // Initialize the database
        videoStoryRepository.saveAndFlush(videoStory);

        int databaseSizeBeforeDelete = videoStoryRepository.findAll().size();

        // Delete the videoStory
        restVideoStoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, videoStory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<VideoStory> videoStoryList = videoStoryRepository.findAll();
        assertThat(videoStoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
