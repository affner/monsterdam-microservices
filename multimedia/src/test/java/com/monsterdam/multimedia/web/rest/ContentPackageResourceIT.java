package com.monsterdam.multimedia.web.rest;

import static com.monsterdam.multimedia.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.monsterdam.multimedia.IntegrationTest;
import com.monsterdam.multimedia.domain.ContentPackage;
import com.monsterdam.multimedia.repository.ContentPackageRepository;
import com.monsterdam.multimedia.service.dto.ContentPackageDTO;
import com.monsterdam.multimedia.service.mapper.ContentPackageMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
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
 * Integration tests for the {@link ContentPackageResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ContentPackageResourceIT {

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);

    private static final Integer DEFAULT_VIDEO_COUNT = 1;
    private static final Integer UPDATED_VIDEO_COUNT = 2;

    private static final Integer DEFAULT_IMAGE_COUNT = 1;
    private static final Integer UPDATED_IMAGE_COUNT = 2;

    private static final Boolean DEFAULT_IS_PAID_CONTENT = false;
    private static final Boolean UPDATED_IS_PAID_CONTENT = true;

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

    private static final Long DEFAULT_MESSAGE_ID = 1L;
    private static final Long UPDATED_MESSAGE_ID = 2L;

    private static final Long DEFAULT_POST_ID = 1L;
    private static final Long UPDATED_POST_ID = 2L;

    private static final String ENTITY_API_URL = "/api/content-packages";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ContentPackageRepository contentPackageRepository;

    @Autowired
    private ContentPackageMapper contentPackageMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContentPackageMockMvc;

    private ContentPackage contentPackage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContentPackage createEntity(EntityManager em) {
        ContentPackage contentPackage = new ContentPackage()
            .amount(DEFAULT_AMOUNT)
            .videoCount(DEFAULT_VIDEO_COUNT)
            .imageCount(DEFAULT_IMAGE_COUNT)
            .isPaidContent(DEFAULT_IS_PAID_CONTENT)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .isDeleted(DEFAULT_IS_DELETED)
            .messageId(DEFAULT_MESSAGE_ID)
            .postId(DEFAULT_POST_ID);
        return contentPackage;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContentPackage createUpdatedEntity(EntityManager em) {
        ContentPackage contentPackage = new ContentPackage()
            .amount(UPDATED_AMOUNT)
            .videoCount(UPDATED_VIDEO_COUNT)
            .imageCount(UPDATED_IMAGE_COUNT)
            .isPaidContent(UPDATED_IS_PAID_CONTENT)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .messageId(UPDATED_MESSAGE_ID)
            .postId(UPDATED_POST_ID);
        return contentPackage;
    }

    @BeforeEach
    public void initTest() {
        contentPackage = createEntity(em);
    }

    @Test
    @Transactional
    void createContentPackage() throws Exception {
        int databaseSizeBeforeCreate = contentPackageRepository.findAll().size();
        // Create the ContentPackage
        ContentPackageDTO contentPackageDTO = contentPackageMapper.toDto(contentPackage);
        restContentPackageMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contentPackageDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ContentPackage in the database
        List<ContentPackage> contentPackageList = contentPackageRepository.findAll();
        assertThat(contentPackageList).hasSize(databaseSizeBeforeCreate + 1);
        ContentPackage testContentPackage = contentPackageList.get(contentPackageList.size() - 1);
        assertThat(testContentPackage.getAmount()).isEqualByComparingTo(DEFAULT_AMOUNT);
        assertThat(testContentPackage.getVideoCount()).isEqualTo(DEFAULT_VIDEO_COUNT);
        assertThat(testContentPackage.getImageCount()).isEqualTo(DEFAULT_IMAGE_COUNT);
        assertThat(testContentPackage.getIsPaidContent()).isEqualTo(DEFAULT_IS_PAID_CONTENT);
        assertThat(testContentPackage.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testContentPackage.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testContentPackage.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testContentPackage.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testContentPackage.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
        assertThat(testContentPackage.getMessageId()).isEqualTo(DEFAULT_MESSAGE_ID);
        assertThat(testContentPackage.getPostId()).isEqualTo(DEFAULT_POST_ID);
    }

    @Test
    @Transactional
    void createContentPackageWithExistingId() throws Exception {
        // Create the ContentPackage with an existing ID
        contentPackage.setId(1L);
        ContentPackageDTO contentPackageDTO = contentPackageMapper.toDto(contentPackage);

        int databaseSizeBeforeCreate = contentPackageRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restContentPackageMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contentPackageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContentPackage in the database
        List<ContentPackage> contentPackageList = contentPackageRepository.findAll();
        assertThat(contentPackageList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIsPaidContentIsRequired() throws Exception {
        int databaseSizeBeforeTest = contentPackageRepository.findAll().size();
        // set the field null
        contentPackage.setIsPaidContent(null);

        // Create the ContentPackage, which fails.
        ContentPackageDTO contentPackageDTO = contentPackageMapper.toDto(contentPackage);

        restContentPackageMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contentPackageDTO))
            )
            .andExpect(status().isBadRequest());

        List<ContentPackage> contentPackageList = contentPackageRepository.findAll();
        assertThat(contentPackageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = contentPackageRepository.findAll().size();
        // set the field null
        contentPackage.setCreatedDate(null);

        // Create the ContentPackage, which fails.
        ContentPackageDTO contentPackageDTO = contentPackageMapper.toDto(contentPackage);

        restContentPackageMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contentPackageDTO))
            )
            .andExpect(status().isBadRequest());

        List<ContentPackage> contentPackageList = contentPackageRepository.findAll();
        assertThat(contentPackageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = contentPackageRepository.findAll().size();
        // set the field null
        contentPackage.setIsDeleted(null);

        // Create the ContentPackage, which fails.
        ContentPackageDTO contentPackageDTO = contentPackageMapper.toDto(contentPackage);

        restContentPackageMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contentPackageDTO))
            )
            .andExpect(status().isBadRequest());

        List<ContentPackage> contentPackageList = contentPackageRepository.findAll();
        assertThat(contentPackageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllContentPackages() throws Exception {
        // Initialize the database
        contentPackageRepository.saveAndFlush(contentPackage);

        // Get all the contentPackageList
        restContentPackageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contentPackage.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].videoCount").value(hasItem(DEFAULT_VIDEO_COUNT)))
            .andExpect(jsonPath("$.[*].imageCount").value(hasItem(DEFAULT_IMAGE_COUNT)))
            .andExpect(jsonPath("$.[*].isPaidContent").value(hasItem(DEFAULT_IS_PAID_CONTENT.booleanValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())))
            .andExpect(jsonPath("$.[*].messageId").value(hasItem(DEFAULT_MESSAGE_ID.intValue())))
            .andExpect(jsonPath("$.[*].postId").value(hasItem(DEFAULT_POST_ID.intValue())));
    }

    @Test
    @Transactional
    void getContentPackage() throws Exception {
        // Initialize the database
        contentPackageRepository.saveAndFlush(contentPackage);

        // Get the contentPackage
        restContentPackageMockMvc
            .perform(get(ENTITY_API_URL_ID, contentPackage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contentPackage.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.videoCount").value(DEFAULT_VIDEO_COUNT))
            .andExpect(jsonPath("$.imageCount").value(DEFAULT_IMAGE_COUNT))
            .andExpect(jsonPath("$.isPaidContent").value(DEFAULT_IS_PAID_CONTENT.booleanValue()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()))
            .andExpect(jsonPath("$.messageId").value(DEFAULT_MESSAGE_ID.intValue()))
            .andExpect(jsonPath("$.postId").value(DEFAULT_POST_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingContentPackage() throws Exception {
        // Get the contentPackage
        restContentPackageMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingContentPackage() throws Exception {
        // Initialize the database
        contentPackageRepository.saveAndFlush(contentPackage);

        int databaseSizeBeforeUpdate = contentPackageRepository.findAll().size();

        // Update the contentPackage
        ContentPackage updatedContentPackage = contentPackageRepository.findById(contentPackage.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedContentPackage are not directly saved in db
        em.detach(updatedContentPackage);
        updatedContentPackage
            .amount(UPDATED_AMOUNT)
            .videoCount(UPDATED_VIDEO_COUNT)
            .imageCount(UPDATED_IMAGE_COUNT)
            .isPaidContent(UPDATED_IS_PAID_CONTENT)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .messageId(UPDATED_MESSAGE_ID)
            .postId(UPDATED_POST_ID);
        ContentPackageDTO contentPackageDTO = contentPackageMapper.toDto(updatedContentPackage);

        restContentPackageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contentPackageDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contentPackageDTO))
            )
            .andExpect(status().isOk());

        // Validate the ContentPackage in the database
        List<ContentPackage> contentPackageList = contentPackageRepository.findAll();
        assertThat(contentPackageList).hasSize(databaseSizeBeforeUpdate);
        ContentPackage testContentPackage = contentPackageList.get(contentPackageList.size() - 1);
        assertThat(testContentPackage.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
        assertThat(testContentPackage.getVideoCount()).isEqualTo(UPDATED_VIDEO_COUNT);
        assertThat(testContentPackage.getImageCount()).isEqualTo(UPDATED_IMAGE_COUNT);
        assertThat(testContentPackage.getIsPaidContent()).isEqualTo(UPDATED_IS_PAID_CONTENT);
        assertThat(testContentPackage.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testContentPackage.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testContentPackage.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testContentPackage.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testContentPackage.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testContentPackage.getMessageId()).isEqualTo(UPDATED_MESSAGE_ID);
        assertThat(testContentPackage.getPostId()).isEqualTo(UPDATED_POST_ID);
    }

    @Test
    @Transactional
    void putNonExistingContentPackage() throws Exception {
        int databaseSizeBeforeUpdate = contentPackageRepository.findAll().size();
        contentPackage.setId(longCount.incrementAndGet());

        // Create the ContentPackage
        ContentPackageDTO contentPackageDTO = contentPackageMapper.toDto(contentPackage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContentPackageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contentPackageDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contentPackageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContentPackage in the database
        List<ContentPackage> contentPackageList = contentPackageRepository.findAll();
        assertThat(contentPackageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchContentPackage() throws Exception {
        int databaseSizeBeforeUpdate = contentPackageRepository.findAll().size();
        contentPackage.setId(longCount.incrementAndGet());

        // Create the ContentPackage
        ContentPackageDTO contentPackageDTO = contentPackageMapper.toDto(contentPackage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContentPackageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contentPackageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContentPackage in the database
        List<ContentPackage> contentPackageList = contentPackageRepository.findAll();
        assertThat(contentPackageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamContentPackage() throws Exception {
        int databaseSizeBeforeUpdate = contentPackageRepository.findAll().size();
        contentPackage.setId(longCount.incrementAndGet());

        // Create the ContentPackage
        ContentPackageDTO contentPackageDTO = contentPackageMapper.toDto(contentPackage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContentPackageMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contentPackageDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContentPackage in the database
        List<ContentPackage> contentPackageList = contentPackageRepository.findAll();
        assertThat(contentPackageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateContentPackageWithPatch() throws Exception {
        // Initialize the database
        contentPackageRepository.saveAndFlush(contentPackage);

        int databaseSizeBeforeUpdate = contentPackageRepository.findAll().size();

        // Update the contentPackage using partial update
        ContentPackage partialUpdatedContentPackage = new ContentPackage();
        partialUpdatedContentPackage.setId(contentPackage.getId());

        partialUpdatedContentPackage.videoCount(UPDATED_VIDEO_COUNT).isPaidContent(UPDATED_IS_PAID_CONTENT).isDeleted(UPDATED_IS_DELETED);

        restContentPackageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContentPackage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContentPackage))
            )
            .andExpect(status().isOk());

        // Validate the ContentPackage in the database
        List<ContentPackage> contentPackageList = contentPackageRepository.findAll();
        assertThat(contentPackageList).hasSize(databaseSizeBeforeUpdate);
        ContentPackage testContentPackage = contentPackageList.get(contentPackageList.size() - 1);
        assertThat(testContentPackage.getAmount()).isEqualByComparingTo(DEFAULT_AMOUNT);
        assertThat(testContentPackage.getVideoCount()).isEqualTo(UPDATED_VIDEO_COUNT);
        assertThat(testContentPackage.getImageCount()).isEqualTo(DEFAULT_IMAGE_COUNT);
        assertThat(testContentPackage.getIsPaidContent()).isEqualTo(UPDATED_IS_PAID_CONTENT);
        assertThat(testContentPackage.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testContentPackage.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testContentPackage.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testContentPackage.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testContentPackage.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testContentPackage.getMessageId()).isEqualTo(DEFAULT_MESSAGE_ID);
        assertThat(testContentPackage.getPostId()).isEqualTo(DEFAULT_POST_ID);
    }

    @Test
    @Transactional
    void fullUpdateContentPackageWithPatch() throws Exception {
        // Initialize the database
        contentPackageRepository.saveAndFlush(contentPackage);

        int databaseSizeBeforeUpdate = contentPackageRepository.findAll().size();

        // Update the contentPackage using partial update
        ContentPackage partialUpdatedContentPackage = new ContentPackage();
        partialUpdatedContentPackage.setId(contentPackage.getId());

        partialUpdatedContentPackage
            .amount(UPDATED_AMOUNT)
            .videoCount(UPDATED_VIDEO_COUNT)
            .imageCount(UPDATED_IMAGE_COUNT)
            .isPaidContent(UPDATED_IS_PAID_CONTENT)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .messageId(UPDATED_MESSAGE_ID)
            .postId(UPDATED_POST_ID);

        restContentPackageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContentPackage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContentPackage))
            )
            .andExpect(status().isOk());

        // Validate the ContentPackage in the database
        List<ContentPackage> contentPackageList = contentPackageRepository.findAll();
        assertThat(contentPackageList).hasSize(databaseSizeBeforeUpdate);
        ContentPackage testContentPackage = contentPackageList.get(contentPackageList.size() - 1);
        assertThat(testContentPackage.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
        assertThat(testContentPackage.getVideoCount()).isEqualTo(UPDATED_VIDEO_COUNT);
        assertThat(testContentPackage.getImageCount()).isEqualTo(UPDATED_IMAGE_COUNT);
        assertThat(testContentPackage.getIsPaidContent()).isEqualTo(UPDATED_IS_PAID_CONTENT);
        assertThat(testContentPackage.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testContentPackage.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testContentPackage.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testContentPackage.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testContentPackage.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testContentPackage.getMessageId()).isEqualTo(UPDATED_MESSAGE_ID);
        assertThat(testContentPackage.getPostId()).isEqualTo(UPDATED_POST_ID);
    }

    @Test
    @Transactional
    void patchNonExistingContentPackage() throws Exception {
        int databaseSizeBeforeUpdate = contentPackageRepository.findAll().size();
        contentPackage.setId(longCount.incrementAndGet());

        // Create the ContentPackage
        ContentPackageDTO contentPackageDTO = contentPackageMapper.toDto(contentPackage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContentPackageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, contentPackageDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contentPackageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContentPackage in the database
        List<ContentPackage> contentPackageList = contentPackageRepository.findAll();
        assertThat(contentPackageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchContentPackage() throws Exception {
        int databaseSizeBeforeUpdate = contentPackageRepository.findAll().size();
        contentPackage.setId(longCount.incrementAndGet());

        // Create the ContentPackage
        ContentPackageDTO contentPackageDTO = contentPackageMapper.toDto(contentPackage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContentPackageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contentPackageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContentPackage in the database
        List<ContentPackage> contentPackageList = contentPackageRepository.findAll();
        assertThat(contentPackageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamContentPackage() throws Exception {
        int databaseSizeBeforeUpdate = contentPackageRepository.findAll().size();
        contentPackage.setId(longCount.incrementAndGet());

        // Create the ContentPackage
        ContentPackageDTO contentPackageDTO = contentPackageMapper.toDto(contentPackage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContentPackageMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contentPackageDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContentPackage in the database
        List<ContentPackage> contentPackageList = contentPackageRepository.findAll();
        assertThat(contentPackageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteContentPackage() throws Exception {
        // Initialize the database
        contentPackageRepository.saveAndFlush(contentPackage);

        int databaseSizeBeforeDelete = contentPackageRepository.findAll().size();

        // Delete the contentPackage
        restContentPackageMockMvc
            .perform(delete(ENTITY_API_URL_ID, contentPackage.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ContentPackage> contentPackageList = contentPackageRepository.findAll();
        assertThat(contentPackageList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
