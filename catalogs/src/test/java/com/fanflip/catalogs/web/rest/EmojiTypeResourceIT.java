package com.fanflip.catalogs.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fanflip.catalogs.IntegrationTest;
import com.fanflip.catalogs.domain.EmojiType;
import com.fanflip.catalogs.repository.EmojiTypeRepository;
import com.fanflip.catalogs.service.dto.EmojiTypeDTO;
import com.fanflip.catalogs.service.mapper.EmojiTypeMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link EmojiTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EmojiTypeResourceIT {

    private static final byte[] DEFAULT_THUMBNAIL = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_THUMBNAIL = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_THUMBNAIL_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_THUMBNAIL_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

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

    private static final String ENTITY_API_URL = "/api/emoji-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EmojiTypeRepository emojiTypeRepository;

    @Autowired
    private EmojiTypeMapper emojiTypeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmojiTypeMockMvc;

    private EmojiType emojiType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmojiType createEntity(EntityManager em) {
        EmojiType emojiType = new EmojiType()
            .thumbnail(DEFAULT_THUMBNAIL)
            .thumbnailContentType(DEFAULT_THUMBNAIL_CONTENT_TYPE)
            .description(DEFAULT_DESCRIPTION)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .isDeleted(DEFAULT_IS_DELETED);
        return emojiType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmojiType createUpdatedEntity(EntityManager em) {
        EmojiType emojiType = new EmojiType()
            .thumbnail(UPDATED_THUMBNAIL)
            .thumbnailContentType(UPDATED_THUMBNAIL_CONTENT_TYPE)
            .description(UPDATED_DESCRIPTION)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        return emojiType;
    }

    @BeforeEach
    public void initTest() {
        emojiType = createEntity(em);
    }

    @Test
    @Transactional
    void createEmojiType() throws Exception {
        int databaseSizeBeforeCreate = emojiTypeRepository.findAll().size();
        // Create the EmojiType
        EmojiTypeDTO emojiTypeDTO = emojiTypeMapper.toDto(emojiType);
        restEmojiTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(emojiTypeDTO)))
            .andExpect(status().isCreated());

        // Validate the EmojiType in the database
        List<EmojiType> emojiTypeList = emojiTypeRepository.findAll();
        assertThat(emojiTypeList).hasSize(databaseSizeBeforeCreate + 1);
        EmojiType testEmojiType = emojiTypeList.get(emojiTypeList.size() - 1);
        assertThat(testEmojiType.getThumbnail()).isEqualTo(DEFAULT_THUMBNAIL);
        assertThat(testEmojiType.getThumbnailContentType()).isEqualTo(DEFAULT_THUMBNAIL_CONTENT_TYPE);
        assertThat(testEmojiType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testEmojiType.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testEmojiType.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testEmojiType.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testEmojiType.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testEmojiType.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    @Transactional
    void createEmojiTypeWithExistingId() throws Exception {
        // Create the EmojiType with an existing ID
        emojiType.setId(1L);
        EmojiTypeDTO emojiTypeDTO = emojiTypeMapper.toDto(emojiType);

        int databaseSizeBeforeCreate = emojiTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmojiTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(emojiTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the EmojiType in the database
        List<EmojiType> emojiTypeList = emojiTypeRepository.findAll();
        assertThat(emojiTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = emojiTypeRepository.findAll().size();
        // set the field null
        emojiType.setDescription(null);

        // Create the EmojiType, which fails.
        EmojiTypeDTO emojiTypeDTO = emojiTypeMapper.toDto(emojiType);

        restEmojiTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(emojiTypeDTO)))
            .andExpect(status().isBadRequest());

        List<EmojiType> emojiTypeList = emojiTypeRepository.findAll();
        assertThat(emojiTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = emojiTypeRepository.findAll().size();
        // set the field null
        emojiType.setCreatedDate(null);

        // Create the EmojiType, which fails.
        EmojiTypeDTO emojiTypeDTO = emojiTypeMapper.toDto(emojiType);

        restEmojiTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(emojiTypeDTO)))
            .andExpect(status().isBadRequest());

        List<EmojiType> emojiTypeList = emojiTypeRepository.findAll();
        assertThat(emojiTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = emojiTypeRepository.findAll().size();
        // set the field null
        emojiType.setIsDeleted(null);

        // Create the EmojiType, which fails.
        EmojiTypeDTO emojiTypeDTO = emojiTypeMapper.toDto(emojiType);

        restEmojiTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(emojiTypeDTO)))
            .andExpect(status().isBadRequest());

        List<EmojiType> emojiTypeList = emojiTypeRepository.findAll();
        assertThat(emojiTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEmojiTypes() throws Exception {
        // Initialize the database
        emojiTypeRepository.saveAndFlush(emojiType);

        // Get all the emojiTypeList
        restEmojiTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(emojiType.getId().intValue())))
            .andExpect(jsonPath("$.[*].thumbnailContentType").value(hasItem(DEFAULT_THUMBNAIL_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].thumbnail").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_THUMBNAIL))))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())));
    }

    @Test
    @Transactional
    void getEmojiType() throws Exception {
        // Initialize the database
        emojiTypeRepository.saveAndFlush(emojiType);

        // Get the emojiType
        restEmojiTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, emojiType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(emojiType.getId().intValue()))
            .andExpect(jsonPath("$.thumbnailContentType").value(DEFAULT_THUMBNAIL_CONTENT_TYPE))
            .andExpect(jsonPath("$.thumbnail").value(Base64.getEncoder().encodeToString(DEFAULT_THUMBNAIL)))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingEmojiType() throws Exception {
        // Get the emojiType
        restEmojiTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEmojiType() throws Exception {
        // Initialize the database
        emojiTypeRepository.saveAndFlush(emojiType);

        int databaseSizeBeforeUpdate = emojiTypeRepository.findAll().size();

        // Update the emojiType
        EmojiType updatedEmojiType = emojiTypeRepository.findById(emojiType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEmojiType are not directly saved in db
        em.detach(updatedEmojiType);
        updatedEmojiType
            .thumbnail(UPDATED_THUMBNAIL)
            .thumbnailContentType(UPDATED_THUMBNAIL_CONTENT_TYPE)
            .description(UPDATED_DESCRIPTION)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        EmojiTypeDTO emojiTypeDTO = emojiTypeMapper.toDto(updatedEmojiType);

        restEmojiTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, emojiTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(emojiTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the EmojiType in the database
        List<EmojiType> emojiTypeList = emojiTypeRepository.findAll();
        assertThat(emojiTypeList).hasSize(databaseSizeBeforeUpdate);
        EmojiType testEmojiType = emojiTypeList.get(emojiTypeList.size() - 1);
        assertThat(testEmojiType.getThumbnail()).isEqualTo(UPDATED_THUMBNAIL);
        assertThat(testEmojiType.getThumbnailContentType()).isEqualTo(UPDATED_THUMBNAIL_CONTENT_TYPE);
        assertThat(testEmojiType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testEmojiType.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testEmojiType.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testEmojiType.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testEmojiType.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testEmojiType.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void putNonExistingEmojiType() throws Exception {
        int databaseSizeBeforeUpdate = emojiTypeRepository.findAll().size();
        emojiType.setId(longCount.incrementAndGet());

        // Create the EmojiType
        EmojiTypeDTO emojiTypeDTO = emojiTypeMapper.toDto(emojiType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmojiTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, emojiTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(emojiTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmojiType in the database
        List<EmojiType> emojiTypeList = emojiTypeRepository.findAll();
        assertThat(emojiTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEmojiType() throws Exception {
        int databaseSizeBeforeUpdate = emojiTypeRepository.findAll().size();
        emojiType.setId(longCount.incrementAndGet());

        // Create the EmojiType
        EmojiTypeDTO emojiTypeDTO = emojiTypeMapper.toDto(emojiType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmojiTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(emojiTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmojiType in the database
        List<EmojiType> emojiTypeList = emojiTypeRepository.findAll();
        assertThat(emojiTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEmojiType() throws Exception {
        int databaseSizeBeforeUpdate = emojiTypeRepository.findAll().size();
        emojiType.setId(longCount.incrementAndGet());

        // Create the EmojiType
        EmojiTypeDTO emojiTypeDTO = emojiTypeMapper.toDto(emojiType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmojiTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(emojiTypeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmojiType in the database
        List<EmojiType> emojiTypeList = emojiTypeRepository.findAll();
        assertThat(emojiTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEmojiTypeWithPatch() throws Exception {
        // Initialize the database
        emojiTypeRepository.saveAndFlush(emojiType);

        int databaseSizeBeforeUpdate = emojiTypeRepository.findAll().size();

        // Update the emojiType using partial update
        EmojiType partialUpdatedEmojiType = new EmojiType();
        partialUpdatedEmojiType.setId(emojiType.getId());

        partialUpdatedEmojiType
            .thumbnail(UPDATED_THUMBNAIL)
            .thumbnailContentType(UPDATED_THUMBNAIL_CONTENT_TYPE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .isDeleted(UPDATED_IS_DELETED);

        restEmojiTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmojiType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmojiType))
            )
            .andExpect(status().isOk());

        // Validate the EmojiType in the database
        List<EmojiType> emojiTypeList = emojiTypeRepository.findAll();
        assertThat(emojiTypeList).hasSize(databaseSizeBeforeUpdate);
        EmojiType testEmojiType = emojiTypeList.get(emojiTypeList.size() - 1);
        assertThat(testEmojiType.getThumbnail()).isEqualTo(UPDATED_THUMBNAIL);
        assertThat(testEmojiType.getThumbnailContentType()).isEqualTo(UPDATED_THUMBNAIL_CONTENT_TYPE);
        assertThat(testEmojiType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testEmojiType.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testEmojiType.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testEmojiType.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testEmojiType.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testEmojiType.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void fullUpdateEmojiTypeWithPatch() throws Exception {
        // Initialize the database
        emojiTypeRepository.saveAndFlush(emojiType);

        int databaseSizeBeforeUpdate = emojiTypeRepository.findAll().size();

        // Update the emojiType using partial update
        EmojiType partialUpdatedEmojiType = new EmojiType();
        partialUpdatedEmojiType.setId(emojiType.getId());

        partialUpdatedEmojiType
            .thumbnail(UPDATED_THUMBNAIL)
            .thumbnailContentType(UPDATED_THUMBNAIL_CONTENT_TYPE)
            .description(UPDATED_DESCRIPTION)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);

        restEmojiTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmojiType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmojiType))
            )
            .andExpect(status().isOk());

        // Validate the EmojiType in the database
        List<EmojiType> emojiTypeList = emojiTypeRepository.findAll();
        assertThat(emojiTypeList).hasSize(databaseSizeBeforeUpdate);
        EmojiType testEmojiType = emojiTypeList.get(emojiTypeList.size() - 1);
        assertThat(testEmojiType.getThumbnail()).isEqualTo(UPDATED_THUMBNAIL);
        assertThat(testEmojiType.getThumbnailContentType()).isEqualTo(UPDATED_THUMBNAIL_CONTENT_TYPE);
        assertThat(testEmojiType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testEmojiType.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testEmojiType.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testEmojiType.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testEmojiType.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testEmojiType.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void patchNonExistingEmojiType() throws Exception {
        int databaseSizeBeforeUpdate = emojiTypeRepository.findAll().size();
        emojiType.setId(longCount.incrementAndGet());

        // Create the EmojiType
        EmojiTypeDTO emojiTypeDTO = emojiTypeMapper.toDto(emojiType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmojiTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, emojiTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(emojiTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmojiType in the database
        List<EmojiType> emojiTypeList = emojiTypeRepository.findAll();
        assertThat(emojiTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEmojiType() throws Exception {
        int databaseSizeBeforeUpdate = emojiTypeRepository.findAll().size();
        emojiType.setId(longCount.incrementAndGet());

        // Create the EmojiType
        EmojiTypeDTO emojiTypeDTO = emojiTypeMapper.toDto(emojiType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmojiTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(emojiTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmojiType in the database
        List<EmojiType> emojiTypeList = emojiTypeRepository.findAll();
        assertThat(emojiTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEmojiType() throws Exception {
        int databaseSizeBeforeUpdate = emojiTypeRepository.findAll().size();
        emojiType.setId(longCount.incrementAndGet());

        // Create the EmojiType
        EmojiTypeDTO emojiTypeDTO = emojiTypeMapper.toDto(emojiType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmojiTypeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(emojiTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmojiType in the database
        List<EmojiType> emojiTypeList = emojiTypeRepository.findAll();
        assertThat(emojiTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEmojiType() throws Exception {
        // Initialize the database
        emojiTypeRepository.saveAndFlush(emojiType);

        int databaseSizeBeforeDelete = emojiTypeRepository.findAll().size();

        // Delete the emojiType
        restEmojiTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, emojiType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EmojiType> emojiTypeList = emojiTypeRepository.findAll();
        assertThat(emojiTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
