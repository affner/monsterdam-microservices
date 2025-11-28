package com.monsterdam.multimedia.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.monsterdam.multimedia.IntegrationTest;
import com.monsterdam.multimedia.domain.SinglePhoto;
import com.monsterdam.multimedia.repository.SinglePhotoRepository;
import com.monsterdam.multimedia.service.SinglePhotoService;
import com.monsterdam.multimedia.service.dto.SinglePhotoDTO;
import com.monsterdam.multimedia.service.mapper.SinglePhotoMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link SinglePhotoResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SinglePhotoResourceIT {

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

    private static final String ENTITY_API_URL = "/api/single-photos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SinglePhotoRepository singlePhotoRepository;

    @Mock
    private SinglePhotoRepository singlePhotoRepositoryMock;

    @Autowired
    private SinglePhotoMapper singlePhotoMapper;

    @Mock
    private SinglePhotoService singlePhotoServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSinglePhotoMockMvc;

    private SinglePhoto singlePhoto;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SinglePhoto createEntity(EntityManager em) {
        SinglePhoto singlePhoto = new SinglePhoto()
            .thumbnailContentType(DEFAULT_THUMBNAIL_CONTENT_TYPE)
            .thumbnailS3Key(DEFAULT_THUMBNAIL_S_3_KEY)
            .contentContentType(DEFAULT_CONTENT_CONTENT_TYPE)
            .contentS3Key(DEFAULT_CONTENT_S_3_KEY)
            .likeCount(DEFAULT_LIKE_COUNT)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .isDeleted(DEFAULT_IS_DELETED);
        return singlePhoto;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SinglePhoto createUpdatedEntity(EntityManager em) {
        SinglePhoto singlePhoto = new SinglePhoto()
            .thumbnailContentType(UPDATED_THUMBNAIL_CONTENT_TYPE)
            .thumbnailS3Key(UPDATED_THUMBNAIL_S_3_KEY)
            .contentContentType(UPDATED_CONTENT_CONTENT_TYPE)
            .contentS3Key(UPDATED_CONTENT_S_3_KEY)
            .likeCount(UPDATED_LIKE_COUNT)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        return singlePhoto;
    }

    @BeforeEach
    public void initTest() {
        singlePhoto = createEntity(em);
    }

    @Test
    @Transactional
    void createSinglePhoto() throws Exception {
        int databaseSizeBeforeCreate = singlePhotoRepository.findAll().size();
        // Create the SinglePhoto
        SinglePhotoDTO singlePhotoDTO = singlePhotoMapper.toDto(singlePhoto);
        restSinglePhotoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(singlePhotoDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SinglePhoto in the database
        List<SinglePhoto> singlePhotoList = singlePhotoRepository.findAll();
        assertThat(singlePhotoList).hasSize(databaseSizeBeforeCreate + 1);
        SinglePhoto testSinglePhoto = singlePhotoList.get(singlePhotoList.size() - 1);
        assertThat(testSinglePhoto.getThumbnailContentType()).isEqualTo(DEFAULT_THUMBNAIL_CONTENT_TYPE);
        assertThat(testSinglePhoto.getThumbnailS3Key()).isEqualTo(DEFAULT_THUMBNAIL_S_3_KEY);
        assertThat(testSinglePhoto.getContentContentType()).isEqualTo(DEFAULT_CONTENT_CONTENT_TYPE);
        assertThat(testSinglePhoto.getContentS3Key()).isEqualTo(DEFAULT_CONTENT_S_3_KEY);
        assertThat(testSinglePhoto.getLikeCount()).isEqualTo(DEFAULT_LIKE_COUNT);
        assertThat(testSinglePhoto.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testSinglePhoto.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testSinglePhoto.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testSinglePhoto.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testSinglePhoto.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    @Transactional
    void createSinglePhotoWithExistingId() throws Exception {
        // Create the SinglePhoto with an existing ID
        singlePhoto.setId(1L);
        SinglePhotoDTO singlePhotoDTO = singlePhotoMapper.toDto(singlePhoto);

        int databaseSizeBeforeCreate = singlePhotoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSinglePhotoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(singlePhotoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SinglePhoto in the database
        List<SinglePhoto> singlePhotoList = singlePhotoRepository.findAll();
        assertThat(singlePhotoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkThumbnailS3KeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = singlePhotoRepository.findAll().size();
        // set the field null
        singlePhoto.setThumbnailS3Key(null);

        // Create the SinglePhoto, which fails.
        SinglePhotoDTO singlePhotoDTO = singlePhotoMapper.toDto(singlePhoto);

        restSinglePhotoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(singlePhotoDTO))
            )
            .andExpect(status().isBadRequest());

        List<SinglePhoto> singlePhotoList = singlePhotoRepository.findAll();
        assertThat(singlePhotoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkContentS3KeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = singlePhotoRepository.findAll().size();
        // set the field null
        singlePhoto.setContentS3Key(null);

        // Create the SinglePhoto, which fails.
        SinglePhotoDTO singlePhotoDTO = singlePhotoMapper.toDto(singlePhoto);

        restSinglePhotoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(singlePhotoDTO))
            )
            .andExpect(status().isBadRequest());

        List<SinglePhoto> singlePhotoList = singlePhotoRepository.findAll();
        assertThat(singlePhotoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = singlePhotoRepository.findAll().size();
        // set the field null
        singlePhoto.setCreatedDate(null);

        // Create the SinglePhoto, which fails.
        SinglePhotoDTO singlePhotoDTO = singlePhotoMapper.toDto(singlePhoto);

        restSinglePhotoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(singlePhotoDTO))
            )
            .andExpect(status().isBadRequest());

        List<SinglePhoto> singlePhotoList = singlePhotoRepository.findAll();
        assertThat(singlePhotoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = singlePhotoRepository.findAll().size();
        // set the field null
        singlePhoto.setIsDeleted(null);

        // Create the SinglePhoto, which fails.
        SinglePhotoDTO singlePhotoDTO = singlePhotoMapper.toDto(singlePhoto);

        restSinglePhotoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(singlePhotoDTO))
            )
            .andExpect(status().isBadRequest());

        List<SinglePhoto> singlePhotoList = singlePhotoRepository.findAll();
        assertThat(singlePhotoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSinglePhotos() throws Exception {
        // Initialize the database
        singlePhotoRepository.saveAndFlush(singlePhoto);

        // Get all the singlePhotoList
        restSinglePhotoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(singlePhoto.getId().intValue())))
            .andExpect(jsonPath("$.[*].thumbnailContentType").value(hasItem(DEFAULT_THUMBNAIL_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].thumbnail").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_THUMBNAIL))))
            .andExpect(jsonPath("$.[*].thumbnailS3Key").value(hasItem(DEFAULT_THUMBNAIL_S_3_KEY)))
            .andExpect(jsonPath("$.[*].contentContentType").value(hasItem(DEFAULT_CONTENT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_CONTENT))))
            .andExpect(jsonPath("$.[*].contentS3Key").value(hasItem(DEFAULT_CONTENT_S_3_KEY)))
            .andExpect(jsonPath("$.[*].likeCount").value(hasItem(DEFAULT_LIKE_COUNT)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSinglePhotosWithEagerRelationshipsIsEnabled() throws Exception {
        when(singlePhotoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSinglePhotoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(singlePhotoServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSinglePhotosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(singlePhotoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSinglePhotoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(singlePhotoRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getSinglePhoto() throws Exception {
        // Initialize the database
        singlePhotoRepository.saveAndFlush(singlePhoto);

        // Get the singlePhoto
        restSinglePhotoMockMvc
            .perform(get(ENTITY_API_URL_ID, singlePhoto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(singlePhoto.getId().intValue()))
            .andExpect(jsonPath("$.thumbnailContentType").value(DEFAULT_THUMBNAIL_CONTENT_TYPE))
            .andExpect(jsonPath("$.thumbnail").value(Base64.getEncoder().encodeToString(DEFAULT_THUMBNAIL)))
            .andExpect(jsonPath("$.thumbnailS3Key").value(DEFAULT_THUMBNAIL_S_3_KEY))
            .andExpect(jsonPath("$.contentContentType").value(DEFAULT_CONTENT_CONTENT_TYPE))
            .andExpect(jsonPath("$.content").value(Base64.getEncoder().encodeToString(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.contentS3Key").value(DEFAULT_CONTENT_S_3_KEY))
            .andExpect(jsonPath("$.likeCount").value(DEFAULT_LIKE_COUNT))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingSinglePhoto() throws Exception {
        // Get the singlePhoto
        restSinglePhotoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSinglePhoto() throws Exception {
        // Initialize the database
        singlePhotoRepository.saveAndFlush(singlePhoto);

        int databaseSizeBeforeUpdate = singlePhotoRepository.findAll().size();

        // Update the singlePhoto
        SinglePhoto updatedSinglePhoto = singlePhotoRepository.findById(singlePhoto.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSinglePhoto are not directly saved in db
        em.detach(updatedSinglePhoto);
        updatedSinglePhoto
            .thumbnailContentType(UPDATED_THUMBNAIL_CONTENT_TYPE)
            .thumbnailS3Key(UPDATED_THUMBNAIL_S_3_KEY)
            .contentContentType(UPDATED_CONTENT_CONTENT_TYPE)
            .contentS3Key(UPDATED_CONTENT_S_3_KEY)
            .likeCount(UPDATED_LIKE_COUNT)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        SinglePhotoDTO singlePhotoDTO = singlePhotoMapper.toDto(updatedSinglePhoto);

        restSinglePhotoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, singlePhotoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(singlePhotoDTO))
            )
            .andExpect(status().isOk());

        // Validate the SinglePhoto in the database
        List<SinglePhoto> singlePhotoList = singlePhotoRepository.findAll();
        assertThat(singlePhotoList).hasSize(databaseSizeBeforeUpdate);
        SinglePhoto testSinglePhoto = singlePhotoList.get(singlePhotoList.size() - 1);
        assertThat(testSinglePhoto.getThumbnailContentType()).isEqualTo(UPDATED_THUMBNAIL_CONTENT_TYPE);
        assertThat(testSinglePhoto.getThumbnailS3Key()).isEqualTo(UPDATED_THUMBNAIL_S_3_KEY);
        assertThat(testSinglePhoto.getContentContentType()).isEqualTo(UPDATED_CONTENT_CONTENT_TYPE);
        assertThat(testSinglePhoto.getContentS3Key()).isEqualTo(UPDATED_CONTENT_S_3_KEY);
        assertThat(testSinglePhoto.getLikeCount()).isEqualTo(UPDATED_LIKE_COUNT);
        assertThat(testSinglePhoto.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testSinglePhoto.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testSinglePhoto.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testSinglePhoto.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testSinglePhoto.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void putNonExistingSinglePhoto() throws Exception {
        int databaseSizeBeforeUpdate = singlePhotoRepository.findAll().size();
        singlePhoto.setId(longCount.incrementAndGet());

        // Create the SinglePhoto
        SinglePhotoDTO singlePhotoDTO = singlePhotoMapper.toDto(singlePhoto);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSinglePhotoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, singlePhotoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(singlePhotoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SinglePhoto in the database
        List<SinglePhoto> singlePhotoList = singlePhotoRepository.findAll();
        assertThat(singlePhotoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSinglePhoto() throws Exception {
        int databaseSizeBeforeUpdate = singlePhotoRepository.findAll().size();
        singlePhoto.setId(longCount.incrementAndGet());

        // Create the SinglePhoto
        SinglePhotoDTO singlePhotoDTO = singlePhotoMapper.toDto(singlePhoto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSinglePhotoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(singlePhotoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SinglePhoto in the database
        List<SinglePhoto> singlePhotoList = singlePhotoRepository.findAll();
        assertThat(singlePhotoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSinglePhoto() throws Exception {
        int databaseSizeBeforeUpdate = singlePhotoRepository.findAll().size();
        singlePhoto.setId(longCount.incrementAndGet());

        // Create the SinglePhoto
        SinglePhotoDTO singlePhotoDTO = singlePhotoMapper.toDto(singlePhoto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSinglePhotoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(singlePhotoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SinglePhoto in the database
        List<SinglePhoto> singlePhotoList = singlePhotoRepository.findAll();
        assertThat(singlePhotoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSinglePhotoWithPatch() throws Exception {
        // Initialize the database
        singlePhotoRepository.saveAndFlush(singlePhoto);

        int databaseSizeBeforeUpdate = singlePhotoRepository.findAll().size();

        // Update the singlePhoto using partial update
        SinglePhoto partialUpdatedSinglePhoto = new SinglePhoto();
        partialUpdatedSinglePhoto.setId(singlePhoto.getId());

        partialUpdatedSinglePhoto
            .thumbnailS3Key(UPDATED_THUMBNAIL_S_3_KEY)
            .contentContentType(UPDATED_CONTENT_CONTENT_TYPE)
            .contentS3Key(UPDATED_CONTENT_S_3_KEY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        restSinglePhotoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSinglePhoto.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSinglePhoto))
            )
            .andExpect(status().isOk());

        // Validate the SinglePhoto in the database
        List<SinglePhoto> singlePhotoList = singlePhotoRepository.findAll();
        assertThat(singlePhotoList).hasSize(databaseSizeBeforeUpdate);
        SinglePhoto testSinglePhoto = singlePhotoList.get(singlePhotoList.size() - 1);
        assertThat(testSinglePhoto.getThumbnailContentType()).isEqualTo(DEFAULT_THUMBNAIL_CONTENT_TYPE);
        assertThat(testSinglePhoto.getThumbnailS3Key()).isEqualTo(UPDATED_THUMBNAIL_S_3_KEY);
        assertThat(testSinglePhoto.getContentContentType()).isEqualTo(UPDATED_CONTENT_CONTENT_TYPE);
        assertThat(testSinglePhoto.getContentS3Key()).isEqualTo(UPDATED_CONTENT_S_3_KEY);
        assertThat(testSinglePhoto.getLikeCount()).isEqualTo(DEFAULT_LIKE_COUNT);
        assertThat(testSinglePhoto.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testSinglePhoto.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testSinglePhoto.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testSinglePhoto.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testSinglePhoto.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    @Transactional
    void fullUpdateSinglePhotoWithPatch() throws Exception {
        // Initialize the database
        singlePhotoRepository.saveAndFlush(singlePhoto);

        int databaseSizeBeforeUpdate = singlePhotoRepository.findAll().size();

        // Update the singlePhoto using partial update
        SinglePhoto partialUpdatedSinglePhoto = new SinglePhoto();
        partialUpdatedSinglePhoto.setId(singlePhoto.getId());

        partialUpdatedSinglePhoto
            .thumbnailContentType(UPDATED_THUMBNAIL_CONTENT_TYPE)
            .thumbnailS3Key(UPDATED_THUMBNAIL_S_3_KEY)
            .contentContentType(UPDATED_CONTENT_CONTENT_TYPE)
            .contentS3Key(UPDATED_CONTENT_S_3_KEY)
            .likeCount(UPDATED_LIKE_COUNT)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);

        restSinglePhotoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSinglePhoto.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSinglePhoto))
            )
            .andExpect(status().isOk());

        // Validate the SinglePhoto in the database
        List<SinglePhoto> singlePhotoList = singlePhotoRepository.findAll();
        assertThat(singlePhotoList).hasSize(databaseSizeBeforeUpdate);
        SinglePhoto testSinglePhoto = singlePhotoList.get(singlePhotoList.size() - 1);
        assertThat(testSinglePhoto.getThumbnailContentType()).isEqualTo(UPDATED_THUMBNAIL_CONTENT_TYPE);
        assertThat(testSinglePhoto.getThumbnailS3Key()).isEqualTo(UPDATED_THUMBNAIL_S_3_KEY);
        assertThat(testSinglePhoto.getContentContentType()).isEqualTo(UPDATED_CONTENT_CONTENT_TYPE);
        assertThat(testSinglePhoto.getContentS3Key()).isEqualTo(UPDATED_CONTENT_S_3_KEY);
        assertThat(testSinglePhoto.getLikeCount()).isEqualTo(UPDATED_LIKE_COUNT);
        assertThat(testSinglePhoto.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testSinglePhoto.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testSinglePhoto.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testSinglePhoto.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testSinglePhoto.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void patchNonExistingSinglePhoto() throws Exception {
        int databaseSizeBeforeUpdate = singlePhotoRepository.findAll().size();
        singlePhoto.setId(longCount.incrementAndGet());

        // Create the SinglePhoto
        SinglePhotoDTO singlePhotoDTO = singlePhotoMapper.toDto(singlePhoto);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSinglePhotoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, singlePhotoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(singlePhotoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SinglePhoto in the database
        List<SinglePhoto> singlePhotoList = singlePhotoRepository.findAll();
        assertThat(singlePhotoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSinglePhoto() throws Exception {
        int databaseSizeBeforeUpdate = singlePhotoRepository.findAll().size();
        singlePhoto.setId(longCount.incrementAndGet());

        // Create the SinglePhoto
        SinglePhotoDTO singlePhotoDTO = singlePhotoMapper.toDto(singlePhoto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSinglePhotoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(singlePhotoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SinglePhoto in the database
        List<SinglePhoto> singlePhotoList = singlePhotoRepository.findAll();
        assertThat(singlePhotoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSinglePhoto() throws Exception {
        int databaseSizeBeforeUpdate = singlePhotoRepository.findAll().size();
        singlePhoto.setId(longCount.incrementAndGet());

        // Create the SinglePhoto
        SinglePhotoDTO singlePhotoDTO = singlePhotoMapper.toDto(singlePhoto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSinglePhotoMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(singlePhotoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SinglePhoto in the database
        List<SinglePhoto> singlePhotoList = singlePhotoRepository.findAll();
        assertThat(singlePhotoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSinglePhoto() throws Exception {
        // Initialize the database
        singlePhotoRepository.saveAndFlush(singlePhoto);

        int databaseSizeBeforeDelete = singlePhotoRepository.findAll().size();

        // Delete the singlePhoto
        restSinglePhotoMockMvc
            .perform(delete(ENTITY_API_URL_ID, singlePhoto.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SinglePhoto> singlePhotoList = singlePhotoRepository.findAll();
        assertThat(singlePhotoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
