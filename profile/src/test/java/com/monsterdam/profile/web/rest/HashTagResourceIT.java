package com.monsterdam.profile.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.monsterdam.profile.IntegrationTest;
import com.monsterdam.profile.domain.HashTag;
import com.monsterdam.profile.domain.UserProfile;
import com.monsterdam.profile.repository.HashTagRepository;
import com.monsterdam.profile.service.dto.HashTagDTO;
import com.monsterdam.profile.service.mapper.HashTagMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link HashTagResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HashTagResourceIT {

    private static final String DEFAULT_TAG_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TAG_NAME = "BBBBBBBBBB";

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

    private static final String ENTITY_API_URL = "/api/hash-tags";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private HashTagRepository hashTagRepository;

    @Autowired
    private HashTagMapper hashTagMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHashTagMockMvc;

    private HashTag hashTag;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HashTag createEntity(EntityManager em) {
        HashTag hashTag = new HashTag()
            .tagName(DEFAULT_TAG_NAME)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .isDeleted(DEFAULT_IS_DELETED);
        // Add required entity
        UserProfile userProfile;
        if (TestUtil.findAll(em, UserProfile.class).isEmpty()) {
            userProfile = UserProfileResourceIT.createEntity(em);
            em.persist(userProfile);
            em.flush();
        } else {
            userProfile = TestUtil.findAll(em, UserProfile.class).get(0);
        }
        hashTag.getUsers().add(userProfile);
        return hashTag;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HashTag createUpdatedEntity(EntityManager em) {
        HashTag hashTag = new HashTag()
            .tagName(UPDATED_TAG_NAME)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        // Add required entity
        UserProfile userProfile;
        if (TestUtil.findAll(em, UserProfile.class).isEmpty()) {
            userProfile = UserProfileResourceIT.createUpdatedEntity(em);
            em.persist(userProfile);
            em.flush();
        } else {
            userProfile = TestUtil.findAll(em, UserProfile.class).get(0);
        }
        hashTag.getUsers().add(userProfile);
        return hashTag;
    }

    @BeforeEach
    public void initTest() {
        hashTag = createEntity(em);
    }

    @Test
    @Transactional
    void createHashTag() throws Exception {
        int databaseSizeBeforeCreate = hashTagRepository.findAll().size();
        // Create the HashTag
        HashTagDTO hashTagDTO = hashTagMapper.toDto(hashTag);
        restHashTagMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(hashTagDTO)))
            .andExpect(status().isCreated());

        // Validate the HashTag in the database
        List<HashTag> hashTagList = hashTagRepository.findAll();
        assertThat(hashTagList).hasSize(databaseSizeBeforeCreate + 1);
        HashTag testHashTag = hashTagList.get(hashTagList.size() - 1);
        assertThat(testHashTag.getTagName()).isEqualTo(DEFAULT_TAG_NAME);
        assertThat(testHashTag.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testHashTag.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testHashTag.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testHashTag.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testHashTag.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    @Transactional
    void createHashTagWithExistingId() throws Exception {
        // Create the HashTag with an existing ID
        hashTag.setId(1L);
        HashTagDTO hashTagDTO = hashTagMapper.toDto(hashTag);

        int databaseSizeBeforeCreate = hashTagRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHashTagMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(hashTagDTO)))
            .andExpect(status().isBadRequest());

        // Validate the HashTag in the database
        List<HashTag> hashTagList = hashTagRepository.findAll();
        assertThat(hashTagList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTagNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = hashTagRepository.findAll().size();
        // set the field null
        hashTag.setTagName(null);

        // Create the HashTag, which fails.
        HashTagDTO hashTagDTO = hashTagMapper.toDto(hashTag);

        restHashTagMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(hashTagDTO)))
            .andExpect(status().isBadRequest());

        List<HashTag> hashTagList = hashTagRepository.findAll();
        assertThat(hashTagList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = hashTagRepository.findAll().size();
        // set the field null
        hashTag.setCreatedDate(null);

        // Create the HashTag, which fails.
        HashTagDTO hashTagDTO = hashTagMapper.toDto(hashTag);

        restHashTagMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(hashTagDTO)))
            .andExpect(status().isBadRequest());

        List<HashTag> hashTagList = hashTagRepository.findAll();
        assertThat(hashTagList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = hashTagRepository.findAll().size();
        // set the field null
        hashTag.setIsDeleted(null);

        // Create the HashTag, which fails.
        HashTagDTO hashTagDTO = hashTagMapper.toDto(hashTag);

        restHashTagMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(hashTagDTO)))
            .andExpect(status().isBadRequest());

        List<HashTag> hashTagList = hashTagRepository.findAll();
        assertThat(hashTagList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllHashTags() throws Exception {
        // Initialize the database
        hashTagRepository.saveAndFlush(hashTag);

        // Get all the hashTagList
        restHashTagMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hashTag.getId().intValue())))
            .andExpect(jsonPath("$.[*].tagName").value(hasItem(DEFAULT_TAG_NAME)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())));
    }

    @Test
    @Transactional
    void getHashTag() throws Exception {
        // Initialize the database
        hashTagRepository.saveAndFlush(hashTag);

        // Get the hashTag
        restHashTagMockMvc
            .perform(get(ENTITY_API_URL_ID, hashTag.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(hashTag.getId().intValue()))
            .andExpect(jsonPath("$.tagName").value(DEFAULT_TAG_NAME))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingHashTag() throws Exception {
        // Get the hashTag
        restHashTagMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingHashTag() throws Exception {
        // Initialize the database
        hashTagRepository.saveAndFlush(hashTag);

        int databaseSizeBeforeUpdate = hashTagRepository.findAll().size();

        // Update the hashTag
        HashTag updatedHashTag = hashTagRepository.findById(hashTag.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedHashTag are not directly saved in db
        em.detach(updatedHashTag);
        updatedHashTag
            .tagName(UPDATED_TAG_NAME)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        HashTagDTO hashTagDTO = hashTagMapper.toDto(updatedHashTag);

        restHashTagMockMvc
            .perform(
                put(ENTITY_API_URL_ID, hashTagDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(hashTagDTO))
            )
            .andExpect(status().isOk());

        // Validate the HashTag in the database
        List<HashTag> hashTagList = hashTagRepository.findAll();
        assertThat(hashTagList).hasSize(databaseSizeBeforeUpdate);
        HashTag testHashTag = hashTagList.get(hashTagList.size() - 1);
        assertThat(testHashTag.getTagName()).isEqualTo(UPDATED_TAG_NAME);
        assertThat(testHashTag.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testHashTag.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testHashTag.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testHashTag.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testHashTag.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);

    }

    @Test
    @Transactional
    void putNonExistingHashTag() throws Exception {
        int databaseSizeBeforeUpdate = hashTagRepository.findAll().size();
        hashTag.setId(longCount.incrementAndGet());

        // Create the HashTag
        HashTagDTO hashTagDTO = hashTagMapper.toDto(hashTag);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHashTagMockMvc
            .perform(
                put(ENTITY_API_URL_ID, hashTagDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(hashTagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HashTag in the database
        List<HashTag> hashTagList = hashTagRepository.findAll();
        assertThat(hashTagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHashTag() throws Exception {
        int databaseSizeBeforeUpdate = hashTagRepository.findAll().size();
        hashTag.setId(longCount.incrementAndGet());

        // Create the HashTag
        HashTagDTO hashTagDTO = hashTagMapper.toDto(hashTag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHashTagMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(hashTagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HashTag in the database
        List<HashTag> hashTagList = hashTagRepository.findAll();
        assertThat(hashTagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHashTag() throws Exception {
        int databaseSizeBeforeUpdate = hashTagRepository.findAll().size();
        hashTag.setId(longCount.incrementAndGet());

        // Create the HashTag
        HashTagDTO hashTagDTO = hashTagMapper.toDto(hashTag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHashTagMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(hashTagDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the HashTag in the database
        List<HashTag> hashTagList = hashTagRepository.findAll();
        assertThat(hashTagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHashTagWithPatch() throws Exception {
        // Initialize the database
        hashTagRepository.saveAndFlush(hashTag);

        int databaseSizeBeforeUpdate = hashTagRepository.findAll().size();

        // Update the hashTag using partial update
        HashTag partialUpdatedHashTag = new HashTag();
        partialUpdatedHashTag.setId(hashTag.getId());

        partialUpdatedHashTag.createdDate(UPDATED_CREATED_DATE);

        restHashTagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHashTag.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHashTag))
            )
            .andExpect(status().isOk());

        // Validate the HashTag in the database
        List<HashTag> hashTagList = hashTagRepository.findAll();
        assertThat(hashTagList).hasSize(databaseSizeBeforeUpdate);
        HashTag testHashTag = hashTagList.get(hashTagList.size() - 1);
        assertThat(testHashTag.getTagName()).isEqualTo(DEFAULT_TAG_NAME);
        assertThat(testHashTag.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testHashTag.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testHashTag.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testHashTag.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testHashTag.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    @Transactional
    void fullUpdateHashTagWithPatch() throws Exception {
        // Initialize the database
        hashTagRepository.saveAndFlush(hashTag);

        int databaseSizeBeforeUpdate = hashTagRepository.findAll().size();

        // Update the hashTag using partial update
        HashTag partialUpdatedHashTag = new HashTag();
        partialUpdatedHashTag.setId(hashTag.getId());

        partialUpdatedHashTag
            .tagName(UPDATED_TAG_NAME)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);

        restHashTagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHashTag.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHashTag))
            )
            .andExpect(status().isOk());

        // Validate the HashTag in the database
        List<HashTag> hashTagList = hashTagRepository.findAll();
        assertThat(hashTagList).hasSize(databaseSizeBeforeUpdate);
        HashTag testHashTag = hashTagList.get(hashTagList.size() - 1);
        assertThat(testHashTag.getTagName()).isEqualTo(UPDATED_TAG_NAME);
        assertThat(testHashTag.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testHashTag.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testHashTag.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testHashTag.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testHashTag.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void patchNonExistingHashTag() throws Exception {
        int databaseSizeBeforeUpdate = hashTagRepository.findAll().size();
        hashTag.setId(longCount.incrementAndGet());

        // Create the HashTag
        HashTagDTO hashTagDTO = hashTagMapper.toDto(hashTag);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHashTagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, hashTagDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(hashTagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HashTag in the database
        List<HashTag> hashTagList = hashTagRepository.findAll();
        assertThat(hashTagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHashTag() throws Exception {
        int databaseSizeBeforeUpdate = hashTagRepository.findAll().size();
        hashTag.setId(longCount.incrementAndGet());

        // Create the HashTag
        HashTagDTO hashTagDTO = hashTagMapper.toDto(hashTag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHashTagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(hashTagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HashTag in the database
        List<HashTag> hashTagList = hashTagRepository.findAll();
        assertThat(hashTagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHashTag() throws Exception {
        int databaseSizeBeforeUpdate = hashTagRepository.findAll().size();
        hashTag.setId(longCount.incrementAndGet());

        // Create the HashTag
        HashTagDTO hashTagDTO = hashTagMapper.toDto(hashTag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHashTagMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(hashTagDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HashTag in the database
        List<HashTag> hashTagList = hashTagRepository.findAll();
        assertThat(hashTagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHashTag() throws Exception {
        // Initialize the database
        hashTagRepository.saveAndFlush(hashTag);
        hashTagRepository.save(hashTag);

        int databaseSizeBeforeDelete = hashTagRepository.findAll().size();

        // Delete the hashTag
        restHashTagMockMvc
            .perform(delete(ENTITY_API_URL_ID, hashTag.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<HashTag> hashTagList = hashTagRepository.findAll();
        assertThat(hashTagList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    void searchHashTag() throws Exception {
        // Initialize the database
        hashTag = hashTagRepository.saveAndFlush(hashTag);

        // Search the hashTag
//        restHashTagMockMvc
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//            .andExpect(jsonPath("$.[*].id").value(hasItem(hashTag.getId().intValue())))
//            .andExpect(jsonPath("$.[*].tagName").value(hasItem(DEFAULT_TAG_NAME)))
//            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
//            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
//            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
//            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
//            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())));
    }
}
