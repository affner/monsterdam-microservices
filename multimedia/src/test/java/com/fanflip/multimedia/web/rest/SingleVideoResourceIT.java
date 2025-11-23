package com.fanflip.multimedia.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fanflip.multimedia.IntegrationTest;
import com.fanflip.multimedia.domain.SingleVideo;
import com.fanflip.multimedia.repository.SingleVideoRepository;
import com.fanflip.multimedia.service.SingleVideoService;
import com.fanflip.multimedia.service.dto.SingleVideoDTO;
import com.fanflip.multimedia.service.mapper.SingleVideoMapper;
import jakarta.persistence.EntityManager;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Base64;
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
 * Integration tests for the {@link SingleVideoResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SingleVideoResourceIT {

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

    private static final String ENTITY_API_URL = "/api/single-videos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SingleVideoRepository singleVideoRepository;

    @Mock
    private SingleVideoRepository singleVideoRepositoryMock;

    @Autowired
    private SingleVideoMapper singleVideoMapper;

    @Mock
    private SingleVideoService singleVideoServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSingleVideoMockMvc;

    private SingleVideo singleVideo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SingleVideo createEntity(EntityManager em) {
        SingleVideo singleVideo = new SingleVideo()
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
            .isDeleted(DEFAULT_IS_DELETED);
        return singleVideo;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SingleVideo createUpdatedEntity(EntityManager em) {
        SingleVideo singleVideo = new SingleVideo()
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
            .isDeleted(UPDATED_IS_DELETED);
        return singleVideo;
    }

    @BeforeEach
    public void initTest() {
        singleVideo = createEntity(em);
    }

    @Test
    @Transactional
    void createSingleVideo() throws Exception {
        int databaseSizeBeforeCreate = singleVideoRepository.findAll().size();
        // Create the SingleVideo
        SingleVideoDTO singleVideoDTO = singleVideoMapper.toDto(singleVideo);
        restSingleVideoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(singleVideoDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SingleVideo in the database
        List<SingleVideo> singleVideoList = singleVideoRepository.findAll();
        assertThat(singleVideoList).hasSize(databaseSizeBeforeCreate + 1);
        SingleVideo testSingleVideo = singleVideoList.get(singleVideoList.size() - 1);
        assertThat(testSingleVideo.getThumbnailContentType()).isEqualTo(DEFAULT_THUMBNAIL_CONTENT_TYPE);
        assertThat(testSingleVideo.getThumbnailS3Key()).isEqualTo(DEFAULT_THUMBNAIL_S_3_KEY);
        assertThat(testSingleVideo.getContentContentType()).isEqualTo(DEFAULT_CONTENT_CONTENT_TYPE);
        assertThat(testSingleVideo.getContentS3Key()).isEqualTo(DEFAULT_CONTENT_S_3_KEY);
        assertThat(testSingleVideo.getDuration()).isEqualTo(DEFAULT_DURATION);
        assertThat(testSingleVideo.getLikeCount()).isEqualTo(DEFAULT_LIKE_COUNT);
        assertThat(testSingleVideo.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testSingleVideo.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testSingleVideo.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testSingleVideo.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testSingleVideo.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    @Transactional
    void createSingleVideoWithExistingId() throws Exception {
        // Create the SingleVideo with an existing ID
        singleVideo.setId(1L);
        SingleVideoDTO singleVideoDTO = singleVideoMapper.toDto(singleVideo);

        int databaseSizeBeforeCreate = singleVideoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSingleVideoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(singleVideoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SingleVideo in the database
        List<SingleVideo> singleVideoList = singleVideoRepository.findAll();
        assertThat(singleVideoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkThumbnailS3KeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = singleVideoRepository.findAll().size();
        // set the field null
        singleVideo.setThumbnailS3Key(null);

        // Create the SingleVideo, which fails.
        SingleVideoDTO singleVideoDTO = singleVideoMapper.toDto(singleVideo);

        restSingleVideoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(singleVideoDTO))
            )
            .andExpect(status().isBadRequest());

        List<SingleVideo> singleVideoList = singleVideoRepository.findAll();
        assertThat(singleVideoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkContentS3KeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = singleVideoRepository.findAll().size();
        // set the field null
        singleVideo.setContentS3Key(null);

        // Create the SingleVideo, which fails.
        SingleVideoDTO singleVideoDTO = singleVideoMapper.toDto(singleVideo);

        restSingleVideoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(singleVideoDTO))
            )
            .andExpect(status().isBadRequest());

        List<SingleVideo> singleVideoList = singleVideoRepository.findAll();
        assertThat(singleVideoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = singleVideoRepository.findAll().size();
        // set the field null
        singleVideo.setCreatedDate(null);

        // Create the SingleVideo, which fails.
        SingleVideoDTO singleVideoDTO = singleVideoMapper.toDto(singleVideo);

        restSingleVideoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(singleVideoDTO))
            )
            .andExpect(status().isBadRequest());

        List<SingleVideo> singleVideoList = singleVideoRepository.findAll();
        assertThat(singleVideoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = singleVideoRepository.findAll().size();
        // set the field null
        singleVideo.setIsDeleted(null);

        // Create the SingleVideo, which fails.
        SingleVideoDTO singleVideoDTO = singleVideoMapper.toDto(singleVideo);

        restSingleVideoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(singleVideoDTO))
            )
            .andExpect(status().isBadRequest());

        List<SingleVideo> singleVideoList = singleVideoRepository.findAll();
        assertThat(singleVideoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSingleVideos() throws Exception {
        // Initialize the database
        singleVideoRepository.saveAndFlush(singleVideo);

        // Get all the singleVideoList
        restSingleVideoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(singleVideo.getId().intValue())))
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
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSingleVideosWithEagerRelationshipsIsEnabled() throws Exception {
        when(singleVideoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSingleVideoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(singleVideoServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSingleVideosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(singleVideoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSingleVideoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(singleVideoRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getSingleVideo() throws Exception {
        // Initialize the database
        singleVideoRepository.saveAndFlush(singleVideo);

        // Get the singleVideo
        restSingleVideoMockMvc
            .perform(get(ENTITY_API_URL_ID, singleVideo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(singleVideo.getId().intValue()))
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
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingSingleVideo() throws Exception {
        // Get the singleVideo
        restSingleVideoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSingleVideo() throws Exception {
        // Initialize the database
        singleVideoRepository.saveAndFlush(singleVideo);

        int databaseSizeBeforeUpdate = singleVideoRepository.findAll().size();

        // Update the singleVideo
        SingleVideo updatedSingleVideo = singleVideoRepository.findById(singleVideo.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSingleVideo are not directly saved in db
        em.detach(updatedSingleVideo);
        updatedSingleVideo
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
            .isDeleted(UPDATED_IS_DELETED);
        SingleVideoDTO singleVideoDTO = singleVideoMapper.toDto(updatedSingleVideo);

        restSingleVideoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, singleVideoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(singleVideoDTO))
            )
            .andExpect(status().isOk());

        // Validate the SingleVideo in the database
        List<SingleVideo> singleVideoList = singleVideoRepository.findAll();
        assertThat(singleVideoList).hasSize(databaseSizeBeforeUpdate);
        SingleVideo testSingleVideo = singleVideoList.get(singleVideoList.size() - 1);
        assertThat(testSingleVideo.getThumbnailContentType()).isEqualTo(UPDATED_THUMBNAIL_CONTENT_TYPE);
        assertThat(testSingleVideo.getThumbnailS3Key()).isEqualTo(UPDATED_THUMBNAIL_S_3_KEY);
        assertThat(testSingleVideo.getContentContentType()).isEqualTo(UPDATED_CONTENT_CONTENT_TYPE);
        assertThat(testSingleVideo.getContentS3Key()).isEqualTo(UPDATED_CONTENT_S_3_KEY);
        assertThat(testSingleVideo.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testSingleVideo.getLikeCount()).isEqualTo(UPDATED_LIKE_COUNT);
        assertThat(testSingleVideo.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testSingleVideo.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testSingleVideo.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testSingleVideo.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testSingleVideo.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void putNonExistingSingleVideo() throws Exception {
        int databaseSizeBeforeUpdate = singleVideoRepository.findAll().size();
        singleVideo.setId(longCount.incrementAndGet());

        // Create the SingleVideo
        SingleVideoDTO singleVideoDTO = singleVideoMapper.toDto(singleVideo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSingleVideoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, singleVideoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(singleVideoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SingleVideo in the database
        List<SingleVideo> singleVideoList = singleVideoRepository.findAll();
        assertThat(singleVideoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSingleVideo() throws Exception {
        int databaseSizeBeforeUpdate = singleVideoRepository.findAll().size();
        singleVideo.setId(longCount.incrementAndGet());

        // Create the SingleVideo
        SingleVideoDTO singleVideoDTO = singleVideoMapper.toDto(singleVideo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSingleVideoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(singleVideoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SingleVideo in the database
        List<SingleVideo> singleVideoList = singleVideoRepository.findAll();
        assertThat(singleVideoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSingleVideo() throws Exception {
        int databaseSizeBeforeUpdate = singleVideoRepository.findAll().size();
        singleVideo.setId(longCount.incrementAndGet());

        // Create the SingleVideo
        SingleVideoDTO singleVideoDTO = singleVideoMapper.toDto(singleVideo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSingleVideoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(singleVideoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SingleVideo in the database
        List<SingleVideo> singleVideoList = singleVideoRepository.findAll();
        assertThat(singleVideoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSingleVideoWithPatch() throws Exception {
        // Initialize the database
        singleVideoRepository.saveAndFlush(singleVideo);

        int databaseSizeBeforeUpdate = singleVideoRepository.findAll().size();

        // Update the singleVideo using partial update
        SingleVideo partialUpdatedSingleVideo = new SingleVideo();
        partialUpdatedSingleVideo.setId(singleVideo.getId());

        partialUpdatedSingleVideo
            .thumbnailContentType(UPDATED_THUMBNAIL_CONTENT_TYPE)
            .contentS3Key(UPDATED_CONTENT_S_3_KEY)
            .duration(UPDATED_DURATION)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        restSingleVideoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSingleVideo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSingleVideo))
            )
            .andExpect(status().isOk());

        // Validate the SingleVideo in the database
        List<SingleVideo> singleVideoList = singleVideoRepository.findAll();
        assertThat(singleVideoList).hasSize(databaseSizeBeforeUpdate);
        SingleVideo testSingleVideo = singleVideoList.get(singleVideoList.size() - 1);
        assertThat(testSingleVideo.getThumbnailContentType()).isEqualTo(UPDATED_THUMBNAIL_CONTENT_TYPE);
        assertThat(testSingleVideo.getThumbnailS3Key()).isEqualTo(DEFAULT_THUMBNAIL_S_3_KEY);
        assertThat(testSingleVideo.getContentContentType()).isEqualTo(DEFAULT_CONTENT_CONTENT_TYPE);
        assertThat(testSingleVideo.getContentS3Key()).isEqualTo(UPDATED_CONTENT_S_3_KEY);
        assertThat(testSingleVideo.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testSingleVideo.getLikeCount()).isEqualTo(DEFAULT_LIKE_COUNT);
        assertThat(testSingleVideo.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testSingleVideo.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testSingleVideo.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testSingleVideo.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testSingleVideo.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    @Transactional
    void fullUpdateSingleVideoWithPatch() throws Exception {
        // Initialize the database
        singleVideoRepository.saveAndFlush(singleVideo);

        int databaseSizeBeforeUpdate = singleVideoRepository.findAll().size();

        // Update the singleVideo using partial update
        SingleVideo partialUpdatedSingleVideo = new SingleVideo();
        partialUpdatedSingleVideo.setId(singleVideo.getId());

        partialUpdatedSingleVideo
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
            .isDeleted(UPDATED_IS_DELETED);

        restSingleVideoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSingleVideo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSingleVideo))
            )
            .andExpect(status().isOk());

        // Validate the SingleVideo in the database
        List<SingleVideo> singleVideoList = singleVideoRepository.findAll();
        assertThat(singleVideoList).hasSize(databaseSizeBeforeUpdate);
        SingleVideo testSingleVideo = singleVideoList.get(singleVideoList.size() - 1);
        assertThat(testSingleVideo.getThumbnailContentType()).isEqualTo(UPDATED_THUMBNAIL_CONTENT_TYPE);
        assertThat(testSingleVideo.getThumbnailS3Key()).isEqualTo(UPDATED_THUMBNAIL_S_3_KEY);
        assertThat(testSingleVideo.getContentContentType()).isEqualTo(UPDATED_CONTENT_CONTENT_TYPE);
        assertThat(testSingleVideo.getContentS3Key()).isEqualTo(UPDATED_CONTENT_S_3_KEY);
        assertThat(testSingleVideo.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testSingleVideo.getLikeCount()).isEqualTo(UPDATED_LIKE_COUNT);
        assertThat(testSingleVideo.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testSingleVideo.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testSingleVideo.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testSingleVideo.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testSingleVideo.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void patchNonExistingSingleVideo() throws Exception {
        int databaseSizeBeforeUpdate = singleVideoRepository.findAll().size();
        singleVideo.setId(longCount.incrementAndGet());

        // Create the SingleVideo
        SingleVideoDTO singleVideoDTO = singleVideoMapper.toDto(singleVideo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSingleVideoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, singleVideoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(singleVideoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SingleVideo in the database
        List<SingleVideo> singleVideoList = singleVideoRepository.findAll();
        assertThat(singleVideoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSingleVideo() throws Exception {
        int databaseSizeBeforeUpdate = singleVideoRepository.findAll().size();
        singleVideo.setId(longCount.incrementAndGet());

        // Create the SingleVideo
        SingleVideoDTO singleVideoDTO = singleVideoMapper.toDto(singleVideo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSingleVideoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(singleVideoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SingleVideo in the database
        List<SingleVideo> singleVideoList = singleVideoRepository.findAll();
        assertThat(singleVideoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSingleVideo() throws Exception {
        int databaseSizeBeforeUpdate = singleVideoRepository.findAll().size();
        singleVideo.setId(longCount.incrementAndGet());

        // Create the SingleVideo
        SingleVideoDTO singleVideoDTO = singleVideoMapper.toDto(singleVideo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSingleVideoMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(singleVideoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SingleVideo in the database
        List<SingleVideo> singleVideoList = singleVideoRepository.findAll();
        assertThat(singleVideoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSingleVideo() throws Exception {
        // Initialize the database
        singleVideoRepository.saveAndFlush(singleVideo);

        int databaseSizeBeforeDelete = singleVideoRepository.findAll().size();

        // Delete the singleVideo
        restSingleVideoMockMvc
            .perform(delete(ENTITY_API_URL_ID, singleVideo.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SingleVideo> singleVideoList = singleVideoRepository.findAll();
        assertThat(singleVideoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
