package com.monsterdam.profile.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.monsterdam.profile.IntegrationTest;
import com.monsterdam.profile.domain.UserAssociation;
import com.monsterdam.profile.domain.enumeration.AssociationStatus;
import com.monsterdam.profile.repository.UserAssociationRepository;
import com.monsterdam.profile.service.dto.UserAssociationDTO;
import com.monsterdam.profile.service.mapper.UserAssociationMapper;
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
 * Integration tests for the {@link UserAssociationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserAssociationResourceIT {

    private static final Instant DEFAULT_REQUESTED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_REQUESTED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final AssociationStatus DEFAULT_STATUS = AssociationStatus.REQUESTED;
    private static final AssociationStatus UPDATED_STATUS = AssociationStatus.APPROVED;

    private static final String DEFAULT_ASSOCIATION_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_ASSOCIATION_TOKEN = "BBBBBBBBBB";

    private static final Instant DEFAULT_EXPIRY_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EXPIRY_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

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

    private static final Long DEFAULT_OWNER_ID = 1L;
    private static final Long UPDATED_OWNER_ID = 2L;

    private static final String ENTITY_API_URL = "/api/user-associations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UserAssociationRepository userAssociationRepository;

    @Autowired
    private UserAssociationMapper userAssociationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserAssociationMockMvc;

    private UserAssociation userAssociation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserAssociation createEntity(EntityManager em) {
        UserAssociation userAssociation = new UserAssociation()
            .requestedDate(DEFAULT_REQUESTED_DATE)
            .status(DEFAULT_STATUS)
            .associationToken(DEFAULT_ASSOCIATION_TOKEN)
            .expiryDate(DEFAULT_EXPIRY_DATE)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .isDeleted(DEFAULT_IS_DELETED)
            .ownerId(DEFAULT_OWNER_ID);
        return userAssociation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserAssociation createUpdatedEntity(EntityManager em) {
        UserAssociation userAssociation = new UserAssociation()
            .requestedDate(UPDATED_REQUESTED_DATE)
            .status(UPDATED_STATUS)
            .associationToken(UPDATED_ASSOCIATION_TOKEN)
            .expiryDate(UPDATED_EXPIRY_DATE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .ownerId(UPDATED_OWNER_ID);
        return userAssociation;
    }

    @BeforeEach
    public void initTest() {
        userAssociation = createEntity(em);
    }

    @Test
    @Transactional
    void createUserAssociation() throws Exception {
        int databaseSizeBeforeCreate = userAssociationRepository.findAll().size();
        // Create the UserAssociation
        UserAssociationDTO userAssociationDTO = userAssociationMapper.toDto(userAssociation);
        restUserAssociationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userAssociationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the UserAssociation in the database
        List<UserAssociation> userAssociationList = userAssociationRepository.findAll();
        assertThat(userAssociationList).hasSize(databaseSizeBeforeCreate + 1);
        UserAssociation testUserAssociation = userAssociationList.get(userAssociationList.size() - 1);
        assertThat(testUserAssociation.getRequestedDate()).isEqualTo(DEFAULT_REQUESTED_DATE);
        assertThat(testUserAssociation.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testUserAssociation.getAssociationToken()).isEqualTo(DEFAULT_ASSOCIATION_TOKEN);
        assertThat(testUserAssociation.getExpiryDate()).isEqualTo(DEFAULT_EXPIRY_DATE);
        assertThat(testUserAssociation.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testUserAssociation.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testUserAssociation.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testUserAssociation.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testUserAssociation.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
        assertThat(testUserAssociation.getOwnerId()).isEqualTo(DEFAULT_OWNER_ID);
    }

    @Test
    @Transactional
    void createUserAssociationWithExistingId() throws Exception {
        // Create the UserAssociation with an existing ID
        userAssociation.setId(1L);
        UserAssociationDTO userAssociationDTO = userAssociationMapper.toDto(userAssociation);

        int databaseSizeBeforeCreate = userAssociationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserAssociationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userAssociationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAssociation in the database
        List<UserAssociation> userAssociationList = userAssociationRepository.findAll();
        assertThat(userAssociationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRequestedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = userAssociationRepository.findAll().size();
        // set the field null
        userAssociation.setRequestedDate(null);

        // Create the UserAssociation, which fails.
        UserAssociationDTO userAssociationDTO = userAssociationMapper.toDto(userAssociation);

        restUserAssociationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userAssociationDTO))
            )
            .andExpect(status().isBadRequest());

        List<UserAssociation> userAssociationList = userAssociationRepository.findAll();
        assertThat(userAssociationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAssociationTokenIsRequired() throws Exception {
        int databaseSizeBeforeTest = userAssociationRepository.findAll().size();
        // set the field null
        userAssociation.setAssociationToken(null);

        // Create the UserAssociation, which fails.
        UserAssociationDTO userAssociationDTO = userAssociationMapper.toDto(userAssociation);

        restUserAssociationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userAssociationDTO))
            )
            .andExpect(status().isBadRequest());

        List<UserAssociation> userAssociationList = userAssociationRepository.findAll();
        assertThat(userAssociationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkExpiryDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = userAssociationRepository.findAll().size();
        // set the field null
        userAssociation.setExpiryDate(null);

        // Create the UserAssociation, which fails.
        UserAssociationDTO userAssociationDTO = userAssociationMapper.toDto(userAssociation);

        restUserAssociationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userAssociationDTO))
            )
            .andExpect(status().isBadRequest());

        List<UserAssociation> userAssociationList = userAssociationRepository.findAll();
        assertThat(userAssociationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = userAssociationRepository.findAll().size();
        // set the field null
        userAssociation.setCreatedDate(null);

        // Create the UserAssociation, which fails.
        UserAssociationDTO userAssociationDTO = userAssociationMapper.toDto(userAssociation);

        restUserAssociationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userAssociationDTO))
            )
            .andExpect(status().isBadRequest());

        List<UserAssociation> userAssociationList = userAssociationRepository.findAll();
        assertThat(userAssociationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = userAssociationRepository.findAll().size();
        // set the field null
        userAssociation.setIsDeleted(null);

        // Create the UserAssociation, which fails.
        UserAssociationDTO userAssociationDTO = userAssociationMapper.toDto(userAssociation);

        restUserAssociationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userAssociationDTO))
            )
            .andExpect(status().isBadRequest());

        List<UserAssociation> userAssociationList = userAssociationRepository.findAll();
        assertThat(userAssociationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOwnerIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = userAssociationRepository.findAll().size();
        // set the field null
        userAssociation.setOwnerId(null);

        // Create the UserAssociation, which fails.
        UserAssociationDTO userAssociationDTO = userAssociationMapper.toDto(userAssociation);

        restUserAssociationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userAssociationDTO))
            )
            .andExpect(status().isBadRequest());

        List<UserAssociation> userAssociationList = userAssociationRepository.findAll();
        assertThat(userAssociationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUserAssociations() throws Exception {
        // Initialize the database
        userAssociationRepository.saveAndFlush(userAssociation);

        // Get all the userAssociationList
        restUserAssociationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userAssociation.getId().intValue())))
            .andExpect(jsonPath("$.[*].requestedDate").value(hasItem(DEFAULT_REQUESTED_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].associationToken").value(hasItem(DEFAULT_ASSOCIATION_TOKEN)))
            .andExpect(jsonPath("$.[*].expiryDate").value(hasItem(DEFAULT_EXPIRY_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())))
            .andExpect(jsonPath("$.[*].ownerId").value(hasItem(DEFAULT_OWNER_ID.intValue())));
    }

    @Test
    @Transactional
    void getUserAssociation() throws Exception {
        // Initialize the database
        userAssociationRepository.saveAndFlush(userAssociation);

        // Get the userAssociation
        restUserAssociationMockMvc
            .perform(get(ENTITY_API_URL_ID, userAssociation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userAssociation.getId().intValue()))
            .andExpect(jsonPath("$.requestedDate").value(DEFAULT_REQUESTED_DATE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.associationToken").value(DEFAULT_ASSOCIATION_TOKEN))
            .andExpect(jsonPath("$.expiryDate").value(DEFAULT_EXPIRY_DATE.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()))
            .andExpect(jsonPath("$.ownerId").value(DEFAULT_OWNER_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingUserAssociation() throws Exception {
        // Get the userAssociation
        restUserAssociationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUserAssociation() throws Exception {
        // Initialize the database
        userAssociationRepository.saveAndFlush(userAssociation);

        int databaseSizeBeforeUpdate = userAssociationRepository.findAll().size();

        // Update the userAssociation
        UserAssociation updatedUserAssociation = userAssociationRepository.findById(userAssociation.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUserAssociation are not directly saved in db
        em.detach(updatedUserAssociation);
        updatedUserAssociation
            .requestedDate(UPDATED_REQUESTED_DATE)
            .status(UPDATED_STATUS)
            .associationToken(UPDATED_ASSOCIATION_TOKEN)
            .expiryDate(UPDATED_EXPIRY_DATE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .ownerId(UPDATED_OWNER_ID);
        UserAssociationDTO userAssociationDTO = userAssociationMapper.toDto(updatedUserAssociation);

        restUserAssociationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userAssociationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userAssociationDTO))
            )
            .andExpect(status().isOk());

        // Validate the UserAssociation in the database
        List<UserAssociation> userAssociationList = userAssociationRepository.findAll();
        assertThat(userAssociationList).hasSize(databaseSizeBeforeUpdate);
        UserAssociation testUserAssociation = userAssociationList.get(userAssociationList.size() - 1);
        assertThat(testUserAssociation.getRequestedDate()).isEqualTo(UPDATED_REQUESTED_DATE);
        assertThat(testUserAssociation.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testUserAssociation.getAssociationToken()).isEqualTo(UPDATED_ASSOCIATION_TOKEN);
        assertThat(testUserAssociation.getExpiryDate()).isEqualTo(UPDATED_EXPIRY_DATE);
        assertThat(testUserAssociation.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testUserAssociation.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testUserAssociation.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testUserAssociation.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testUserAssociation.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testUserAssociation.getOwnerId()).isEqualTo(UPDATED_OWNER_ID);
    }

    @Test
    @Transactional
    void putNonExistingUserAssociation() throws Exception {
        int databaseSizeBeforeUpdate = userAssociationRepository.findAll().size();
        userAssociation.setId(longCount.incrementAndGet());

        // Create the UserAssociation
        UserAssociationDTO userAssociationDTO = userAssociationMapper.toDto(userAssociation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserAssociationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userAssociationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userAssociationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAssociation in the database
        List<UserAssociation> userAssociationList = userAssociationRepository.findAll();
        assertThat(userAssociationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserAssociation() throws Exception {
        int databaseSizeBeforeUpdate = userAssociationRepository.findAll().size();
        userAssociation.setId(longCount.incrementAndGet());

        // Create the UserAssociation
        UserAssociationDTO userAssociationDTO = userAssociationMapper.toDto(userAssociation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserAssociationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userAssociationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAssociation in the database
        List<UserAssociation> userAssociationList = userAssociationRepository.findAll();
        assertThat(userAssociationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserAssociation() throws Exception {
        int databaseSizeBeforeUpdate = userAssociationRepository.findAll().size();
        userAssociation.setId(longCount.incrementAndGet());

        // Create the UserAssociation
        UserAssociationDTO userAssociationDTO = userAssociationMapper.toDto(userAssociation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserAssociationMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userAssociationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserAssociation in the database
        List<UserAssociation> userAssociationList = userAssociationRepository.findAll();
        assertThat(userAssociationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserAssociationWithPatch() throws Exception {
        // Initialize the database
        userAssociationRepository.saveAndFlush(userAssociation);

        int databaseSizeBeforeUpdate = userAssociationRepository.findAll().size();

        // Update the userAssociation using partial update
        UserAssociation partialUpdatedUserAssociation = new UserAssociation();
        partialUpdatedUserAssociation.setId(userAssociation.getId());

        partialUpdatedUserAssociation
            .requestedDate(UPDATED_REQUESTED_DATE)
            .associationToken(UPDATED_ASSOCIATION_TOKEN)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);

        restUserAssociationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserAssociation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserAssociation))
            )
            .andExpect(status().isOk());

        // Validate the UserAssociation in the database
        List<UserAssociation> userAssociationList = userAssociationRepository.findAll();
        assertThat(userAssociationList).hasSize(databaseSizeBeforeUpdate);
        UserAssociation testUserAssociation = userAssociationList.get(userAssociationList.size() - 1);
        assertThat(testUserAssociation.getRequestedDate()).isEqualTo(UPDATED_REQUESTED_DATE);
        assertThat(testUserAssociation.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testUserAssociation.getAssociationToken()).isEqualTo(UPDATED_ASSOCIATION_TOKEN);
        assertThat(testUserAssociation.getExpiryDate()).isEqualTo(DEFAULT_EXPIRY_DATE);
        assertThat(testUserAssociation.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testUserAssociation.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testUserAssociation.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testUserAssociation.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testUserAssociation.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testUserAssociation.getOwnerId()).isEqualTo(DEFAULT_OWNER_ID);
    }

    @Test
    @Transactional
    void fullUpdateUserAssociationWithPatch() throws Exception {
        // Initialize the database
        userAssociationRepository.saveAndFlush(userAssociation);

        int databaseSizeBeforeUpdate = userAssociationRepository.findAll().size();

        // Update the userAssociation using partial update
        UserAssociation partialUpdatedUserAssociation = new UserAssociation();
        partialUpdatedUserAssociation.setId(userAssociation.getId());

        partialUpdatedUserAssociation
            .requestedDate(UPDATED_REQUESTED_DATE)
            .status(UPDATED_STATUS)
            .associationToken(UPDATED_ASSOCIATION_TOKEN)
            .expiryDate(UPDATED_EXPIRY_DATE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .ownerId(UPDATED_OWNER_ID);

        restUserAssociationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserAssociation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserAssociation))
            )
            .andExpect(status().isOk());

        // Validate the UserAssociation in the database
        List<UserAssociation> userAssociationList = userAssociationRepository.findAll();
        assertThat(userAssociationList).hasSize(databaseSizeBeforeUpdate);
        UserAssociation testUserAssociation = userAssociationList.get(userAssociationList.size() - 1);
        assertThat(testUserAssociation.getRequestedDate()).isEqualTo(UPDATED_REQUESTED_DATE);
        assertThat(testUserAssociation.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testUserAssociation.getAssociationToken()).isEqualTo(UPDATED_ASSOCIATION_TOKEN);
        assertThat(testUserAssociation.getExpiryDate()).isEqualTo(UPDATED_EXPIRY_DATE);
        assertThat(testUserAssociation.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testUserAssociation.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testUserAssociation.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testUserAssociation.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testUserAssociation.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testUserAssociation.getOwnerId()).isEqualTo(UPDATED_OWNER_ID);
    }

    @Test
    @Transactional
    void patchNonExistingUserAssociation() throws Exception {
        int databaseSizeBeforeUpdate = userAssociationRepository.findAll().size();
        userAssociation.setId(longCount.incrementAndGet());

        // Create the UserAssociation
        UserAssociationDTO userAssociationDTO = userAssociationMapper.toDto(userAssociation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserAssociationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userAssociationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userAssociationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAssociation in the database
        List<UserAssociation> userAssociationList = userAssociationRepository.findAll();
        assertThat(userAssociationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserAssociation() throws Exception {
        int databaseSizeBeforeUpdate = userAssociationRepository.findAll().size();
        userAssociation.setId(longCount.incrementAndGet());

        // Create the UserAssociation
        UserAssociationDTO userAssociationDTO = userAssociationMapper.toDto(userAssociation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserAssociationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userAssociationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAssociation in the database
        List<UserAssociation> userAssociationList = userAssociationRepository.findAll();
        assertThat(userAssociationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserAssociation() throws Exception {
        int databaseSizeBeforeUpdate = userAssociationRepository.findAll().size();
        userAssociation.setId(longCount.incrementAndGet());

        // Create the UserAssociation
        UserAssociationDTO userAssociationDTO = userAssociationMapper.toDto(userAssociation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserAssociationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userAssociationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserAssociation in the database
        List<UserAssociation> userAssociationList = userAssociationRepository.findAll();
        assertThat(userAssociationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserAssociation() throws Exception {
        // Initialize the database
        userAssociationRepository.saveAndFlush(userAssociation);

        int databaseSizeBeforeDelete = userAssociationRepository.findAll().size();

        // Delete the userAssociation
        restUserAssociationMockMvc
            .perform(delete(ENTITY_API_URL_ID, userAssociation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserAssociation> userAssociationList = userAssociationRepository.findAll();
        assertThat(userAssociationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
