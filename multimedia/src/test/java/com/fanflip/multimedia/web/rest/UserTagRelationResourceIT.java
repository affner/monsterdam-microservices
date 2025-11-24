package com.monsterdam.multimedia.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.monsterdam.multimedia.IntegrationTest;
import com.monsterdam.multimedia.domain.UserTagRelation;
import com.monsterdam.multimedia.domain.enumeration.TagStatus;
import com.monsterdam.multimedia.repository.UserTagRelationRepository;
import com.monsterdam.multimedia.service.dto.UserTagRelationDTO;
import com.monsterdam.multimedia.service.mapper.UserTagRelationMapper;
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
 * Integration tests for the {@link UserTagRelationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserTagRelationResourceIT {

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

    private static final Long DEFAULT_TAGGED_USER_ID = 1L;
    private static final Long UPDATED_TAGGED_USER_ID = 2L;

    private static final TagStatus DEFAULT_TAG_STATUS = TagStatus.AUTHORIZED;
    private static final TagStatus UPDATED_TAG_STATUS = TagStatus.PENDING;

    private static final String ENTITY_API_URL = "/api/user-tag-relations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UserTagRelationRepository userTagRelationRepository;

    @Autowired
    private UserTagRelationMapper userTagRelationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserTagRelationMockMvc;

    private UserTagRelation userTagRelation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserTagRelation createEntity(EntityManager em) {
        UserTagRelation userTagRelation = new UserTagRelation()
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .isDeleted(DEFAULT_IS_DELETED)
            .taggedUserId(DEFAULT_TAGGED_USER_ID)
            .tagStatus(DEFAULT_TAG_STATUS);
        return userTagRelation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserTagRelation createUpdatedEntity(EntityManager em) {
        UserTagRelation userTagRelation = new UserTagRelation()
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .taggedUserId(UPDATED_TAGGED_USER_ID)
            .tagStatus(UPDATED_TAG_STATUS);
        return userTagRelation;
    }

    @BeforeEach
    public void initTest() {
        userTagRelation = createEntity(em);
    }

    @Test
    @Transactional
    void createUserTagRelation() throws Exception {
        int databaseSizeBeforeCreate = userTagRelationRepository.findAll().size();
        // Create the UserTagRelation
        UserTagRelationDTO userTagRelationDTO = userTagRelationMapper.toDto(userTagRelation);
        restUserTagRelationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userTagRelationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the UserTagRelation in the database
        List<UserTagRelation> userTagRelationList = userTagRelationRepository.findAll();
        assertThat(userTagRelationList).hasSize(databaseSizeBeforeCreate + 1);
        UserTagRelation testUserTagRelation = userTagRelationList.get(userTagRelationList.size() - 1);
        assertThat(testUserTagRelation.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testUserTagRelation.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testUserTagRelation.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testUserTagRelation.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testUserTagRelation.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
        assertThat(testUserTagRelation.getTaggedUserId()).isEqualTo(DEFAULT_TAGGED_USER_ID);
        assertThat(testUserTagRelation.getTagStatus()).isEqualTo(DEFAULT_TAG_STATUS);
    }

    @Test
    @Transactional
    void createUserTagRelationWithExistingId() throws Exception {
        // Create the UserTagRelation with an existing ID
        userTagRelation.setId(1L);
        UserTagRelationDTO userTagRelationDTO = userTagRelationMapper.toDto(userTagRelation);

        int databaseSizeBeforeCreate = userTagRelationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserTagRelationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userTagRelationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserTagRelation in the database
        List<UserTagRelation> userTagRelationList = userTagRelationRepository.findAll();
        assertThat(userTagRelationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = userTagRelationRepository.findAll().size();
        // set the field null
        userTagRelation.setCreatedDate(null);

        // Create the UserTagRelation, which fails.
        UserTagRelationDTO userTagRelationDTO = userTagRelationMapper.toDto(userTagRelation);

        restUserTagRelationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userTagRelationDTO))
            )
            .andExpect(status().isBadRequest());

        List<UserTagRelation> userTagRelationList = userTagRelationRepository.findAll();
        assertThat(userTagRelationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = userTagRelationRepository.findAll().size();
        // set the field null
        userTagRelation.setIsDeleted(null);

        // Create the UserTagRelation, which fails.
        UserTagRelationDTO userTagRelationDTO = userTagRelationMapper.toDto(userTagRelation);

        restUserTagRelationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userTagRelationDTO))
            )
            .andExpect(status().isBadRequest());

        List<UserTagRelation> userTagRelationList = userTagRelationRepository.findAll();
        assertThat(userTagRelationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTaggedUserIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = userTagRelationRepository.findAll().size();
        // set the field null
        userTagRelation.setTaggedUserId(null);

        // Create the UserTagRelation, which fails.
        UserTagRelationDTO userTagRelationDTO = userTagRelationMapper.toDto(userTagRelation);

        restUserTagRelationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userTagRelationDTO))
            )
            .andExpect(status().isBadRequest());

        List<UserTagRelation> userTagRelationList = userTagRelationRepository.findAll();
        assertThat(userTagRelationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTagStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = userTagRelationRepository.findAll().size();
        // set the field null
        userTagRelation.setTagStatus(null);

        // Create the UserTagRelation, which fails.
        UserTagRelationDTO userTagRelationDTO = userTagRelationMapper.toDto(userTagRelation);

        restUserTagRelationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userTagRelationDTO))
            )
            .andExpect(status().isBadRequest());

        List<UserTagRelation> userTagRelationList = userTagRelationRepository.findAll();
        assertThat(userTagRelationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUserTagRelations() throws Exception {
        // Initialize the database
        userTagRelationRepository.saveAndFlush(userTagRelation);

        // Get all the userTagRelationList
        restUserTagRelationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userTagRelation.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())))
            .andExpect(jsonPath("$.[*].taggedUserId").value(hasItem(DEFAULT_TAGGED_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].tagStatus").value(hasItem(DEFAULT_TAG_STATUS.toString())));
    }

    @Test
    @Transactional
    void getUserTagRelation() throws Exception {
        // Initialize the database
        userTagRelationRepository.saveAndFlush(userTagRelation);

        // Get the userTagRelation
        restUserTagRelationMockMvc
            .perform(get(ENTITY_API_URL_ID, userTagRelation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userTagRelation.getId().intValue()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()))
            .andExpect(jsonPath("$.taggedUserId").value(DEFAULT_TAGGED_USER_ID.intValue()))
            .andExpect(jsonPath("$.tagStatus").value(DEFAULT_TAG_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingUserTagRelation() throws Exception {
        // Get the userTagRelation
        restUserTagRelationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUserTagRelation() throws Exception {
        // Initialize the database
        userTagRelationRepository.saveAndFlush(userTagRelation);

        int databaseSizeBeforeUpdate = userTagRelationRepository.findAll().size();

        // Update the userTagRelation
        UserTagRelation updatedUserTagRelation = userTagRelationRepository.findById(userTagRelation.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUserTagRelation are not directly saved in db
        em.detach(updatedUserTagRelation);
        updatedUserTagRelation
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .taggedUserId(UPDATED_TAGGED_USER_ID)
            .tagStatus(UPDATED_TAG_STATUS);
        UserTagRelationDTO userTagRelationDTO = userTagRelationMapper.toDto(updatedUserTagRelation);

        restUserTagRelationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userTagRelationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userTagRelationDTO))
            )
            .andExpect(status().isOk());

        // Validate the UserTagRelation in the database
        List<UserTagRelation> userTagRelationList = userTagRelationRepository.findAll();
        assertThat(userTagRelationList).hasSize(databaseSizeBeforeUpdate);
        UserTagRelation testUserTagRelation = userTagRelationList.get(userTagRelationList.size() - 1);
        assertThat(testUserTagRelation.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testUserTagRelation.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testUserTagRelation.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testUserTagRelation.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testUserTagRelation.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testUserTagRelation.getTaggedUserId()).isEqualTo(UPDATED_TAGGED_USER_ID);
        assertThat(testUserTagRelation.getTagStatus()).isEqualTo(UPDATED_TAG_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingUserTagRelation() throws Exception {
        int databaseSizeBeforeUpdate = userTagRelationRepository.findAll().size();
        userTagRelation.setId(longCount.incrementAndGet());

        // Create the UserTagRelation
        UserTagRelationDTO userTagRelationDTO = userTagRelationMapper.toDto(userTagRelation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserTagRelationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userTagRelationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userTagRelationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserTagRelation in the database
        List<UserTagRelation> userTagRelationList = userTagRelationRepository.findAll();
        assertThat(userTagRelationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserTagRelation() throws Exception {
        int databaseSizeBeforeUpdate = userTagRelationRepository.findAll().size();
        userTagRelation.setId(longCount.incrementAndGet());

        // Create the UserTagRelation
        UserTagRelationDTO userTagRelationDTO = userTagRelationMapper.toDto(userTagRelation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserTagRelationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userTagRelationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserTagRelation in the database
        List<UserTagRelation> userTagRelationList = userTagRelationRepository.findAll();
        assertThat(userTagRelationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserTagRelation() throws Exception {
        int databaseSizeBeforeUpdate = userTagRelationRepository.findAll().size();
        userTagRelation.setId(longCount.incrementAndGet());

        // Create the UserTagRelation
        UserTagRelationDTO userTagRelationDTO = userTagRelationMapper.toDto(userTagRelation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserTagRelationMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userTagRelationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserTagRelation in the database
        List<UserTagRelation> userTagRelationList = userTagRelationRepository.findAll();
        assertThat(userTagRelationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserTagRelationWithPatch() throws Exception {
        // Initialize the database
        userTagRelationRepository.saveAndFlush(userTagRelation);

        int databaseSizeBeforeUpdate = userTagRelationRepository.findAll().size();

        // Update the userTagRelation using partial update
        UserTagRelation partialUpdatedUserTagRelation = new UserTagRelation();
        partialUpdatedUserTagRelation.setId(userTagRelation.getId());

        partialUpdatedUserTagRelation
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .tagStatus(UPDATED_TAG_STATUS);

        restUserTagRelationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserTagRelation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserTagRelation))
            )
            .andExpect(status().isOk());

        // Validate the UserTagRelation in the database
        List<UserTagRelation> userTagRelationList = userTagRelationRepository.findAll();
        assertThat(userTagRelationList).hasSize(databaseSizeBeforeUpdate);
        UserTagRelation testUserTagRelation = userTagRelationList.get(userTagRelationList.size() - 1);
        assertThat(testUserTagRelation.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testUserTagRelation.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testUserTagRelation.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testUserTagRelation.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testUserTagRelation.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
        assertThat(testUserTagRelation.getTaggedUserId()).isEqualTo(DEFAULT_TAGGED_USER_ID);
        assertThat(testUserTagRelation.getTagStatus()).isEqualTo(UPDATED_TAG_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateUserTagRelationWithPatch() throws Exception {
        // Initialize the database
        userTagRelationRepository.saveAndFlush(userTagRelation);

        int databaseSizeBeforeUpdate = userTagRelationRepository.findAll().size();

        // Update the userTagRelation using partial update
        UserTagRelation partialUpdatedUserTagRelation = new UserTagRelation();
        partialUpdatedUserTagRelation.setId(userTagRelation.getId());

        partialUpdatedUserTagRelation
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .taggedUserId(UPDATED_TAGGED_USER_ID)
            .tagStatus(UPDATED_TAG_STATUS);

        restUserTagRelationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserTagRelation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserTagRelation))
            )
            .andExpect(status().isOk());

        // Validate the UserTagRelation in the database
        List<UserTagRelation> userTagRelationList = userTagRelationRepository.findAll();
        assertThat(userTagRelationList).hasSize(databaseSizeBeforeUpdate);
        UserTagRelation testUserTagRelation = userTagRelationList.get(userTagRelationList.size() - 1);
        assertThat(testUserTagRelation.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testUserTagRelation.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testUserTagRelation.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testUserTagRelation.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testUserTagRelation.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testUserTagRelation.getTaggedUserId()).isEqualTo(UPDATED_TAGGED_USER_ID);
        assertThat(testUserTagRelation.getTagStatus()).isEqualTo(UPDATED_TAG_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingUserTagRelation() throws Exception {
        int databaseSizeBeforeUpdate = userTagRelationRepository.findAll().size();
        userTagRelation.setId(longCount.incrementAndGet());

        // Create the UserTagRelation
        UserTagRelationDTO userTagRelationDTO = userTagRelationMapper.toDto(userTagRelation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserTagRelationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userTagRelationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userTagRelationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserTagRelation in the database
        List<UserTagRelation> userTagRelationList = userTagRelationRepository.findAll();
        assertThat(userTagRelationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserTagRelation() throws Exception {
        int databaseSizeBeforeUpdate = userTagRelationRepository.findAll().size();
        userTagRelation.setId(longCount.incrementAndGet());

        // Create the UserTagRelation
        UserTagRelationDTO userTagRelationDTO = userTagRelationMapper.toDto(userTagRelation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserTagRelationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userTagRelationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserTagRelation in the database
        List<UserTagRelation> userTagRelationList = userTagRelationRepository.findAll();
        assertThat(userTagRelationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserTagRelation() throws Exception {
        int databaseSizeBeforeUpdate = userTagRelationRepository.findAll().size();
        userTagRelation.setId(longCount.incrementAndGet());

        // Create the UserTagRelation
        UserTagRelationDTO userTagRelationDTO = userTagRelationMapper.toDto(userTagRelation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserTagRelationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userTagRelationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserTagRelation in the database
        List<UserTagRelation> userTagRelationList = userTagRelationRepository.findAll();
        assertThat(userTagRelationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserTagRelation() throws Exception {
        // Initialize the database
        userTagRelationRepository.saveAndFlush(userTagRelation);

        int databaseSizeBeforeDelete = userTagRelationRepository.findAll().size();

        // Delete the userTagRelation
        restUserTagRelationMockMvc
            .perform(delete(ENTITY_API_URL_ID, userTagRelation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserTagRelation> userTagRelationList = userTagRelationRepository.findAll();
        assertThat(userTagRelationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
