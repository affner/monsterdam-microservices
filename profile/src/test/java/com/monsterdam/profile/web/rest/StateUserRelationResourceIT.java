package com.monsterdam.profile.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.monsterdam.profile.IntegrationTest;
import com.monsterdam.profile.domain.StateUserRelation;
import com.monsterdam.profile.domain.UserProfile;
import com.monsterdam.profile.repository.StateUserRelationRepository;
import com.monsterdam.profile.service.dto.StateUserRelationDTO;
import com.monsterdam.profile.service.mapper.StateUserRelationMapper;
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
 * Integration tests for the {@link StateUserRelationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StateUserRelationResourceIT {

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

    private static final Long DEFAULT_ID_STATE = 1L;
    private static final Long UPDATED_ID_STATE = 2L;

    private static final String ENTITY_API_URL = "/api/state-user-relations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private StateUserRelationRepository stateUserRelationRepository;

    @Autowired
    private StateUserRelationMapper stateUserRelationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStateUserRelationMockMvc;

    private StateUserRelation stateUserRelation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StateUserRelation createEntity(EntityManager em) {
        StateUserRelation stateUserRelation = new StateUserRelation()
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .isDeleted(DEFAULT_IS_DELETED)
            .idState(DEFAULT_ID_STATE);
        // Add required entity
        UserProfile userProfile;
        if (TestUtil.findAll(em, UserProfile.class).isEmpty()) {
            userProfile = UserProfileResourceIT.createEntity(em);
            em.persist(userProfile);
            em.flush();
        } else {
            userProfile = TestUtil.findAll(em, UserProfile.class).get(0);
        }
        stateUserRelation.setUser(userProfile);
        return stateUserRelation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StateUserRelation createUpdatedEntity(EntityManager em) {
        StateUserRelation stateUserRelation = new StateUserRelation()
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .idState(UPDATED_ID_STATE);
        // Add required entity
        UserProfile userProfile;
        if (TestUtil.findAll(em, UserProfile.class).isEmpty()) {
            userProfile = UserProfileResourceIT.createUpdatedEntity(em);
            em.persist(userProfile);
            em.flush();
        } else {
            userProfile = TestUtil.findAll(em, UserProfile.class).get(0);
        }
        stateUserRelation.setUser(userProfile);
        return stateUserRelation;
    }

    @BeforeEach
    public void initTest() {
        stateUserRelation = createEntity(em);
    }

    @Test
    @Transactional
    void createStateUserRelation() throws Exception {
        int databaseSizeBeforeCreate = stateUserRelationRepository.findAll().size();
        // Create the StateUserRelation
        StateUserRelationDTO stateUserRelationDTO = stateUserRelationMapper.toDto(stateUserRelation);
        restStateUserRelationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(stateUserRelationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the StateUserRelation in the database
        List<StateUserRelation> stateUserRelationList = stateUserRelationRepository.findAll();
        assertThat(stateUserRelationList).hasSize(databaseSizeBeforeCreate + 1);
        StateUserRelation testStateUserRelation = stateUserRelationList.get(stateUserRelationList.size() - 1);
        assertThat(testStateUserRelation.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testStateUserRelation.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testStateUserRelation.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testStateUserRelation.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testStateUserRelation.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
        assertThat(testStateUserRelation.getIdState()).isEqualTo(DEFAULT_ID_STATE);
    }

    @Test
    @Transactional
    void createStateUserRelationWithExistingId() throws Exception {
        // Create the StateUserRelation with an existing ID
        stateUserRelation.setId(1L);
        StateUserRelationDTO stateUserRelationDTO = stateUserRelationMapper.toDto(stateUserRelation);

        int databaseSizeBeforeCreate = stateUserRelationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStateUserRelationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(stateUserRelationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StateUserRelation in the database
        List<StateUserRelation> stateUserRelationList = stateUserRelationRepository.findAll();
        assertThat(stateUserRelationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = stateUserRelationRepository.findAll().size();
        // set the field null
        stateUserRelation.setCreatedDate(null);

        // Create the StateUserRelation, which fails.
        StateUserRelationDTO stateUserRelationDTO = stateUserRelationMapper.toDto(stateUserRelation);

        restStateUserRelationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(stateUserRelationDTO))
            )
            .andExpect(status().isBadRequest());

        List<StateUserRelation> stateUserRelationList = stateUserRelationRepository.findAll();
        assertThat(stateUserRelationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = stateUserRelationRepository.findAll().size();
        // set the field null
        stateUserRelation.setIsDeleted(null);

        // Create the StateUserRelation, which fails.
        StateUserRelationDTO stateUserRelationDTO = stateUserRelationMapper.toDto(stateUserRelation);

        restStateUserRelationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(stateUserRelationDTO))
            )
            .andExpect(status().isBadRequest());

        List<StateUserRelation> stateUserRelationList = stateUserRelationRepository.findAll();
        assertThat(stateUserRelationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIdStateIsRequired() throws Exception {
        int databaseSizeBeforeTest = stateUserRelationRepository.findAll().size();
        // set the field null
        stateUserRelation.setIdState(null);

        // Create the StateUserRelation, which fails.
        StateUserRelationDTO stateUserRelationDTO = stateUserRelationMapper.toDto(stateUserRelation);

        restStateUserRelationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(stateUserRelationDTO))
            )
            .andExpect(status().isBadRequest());

        List<StateUserRelation> stateUserRelationList = stateUserRelationRepository.findAll();
        assertThat(stateUserRelationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllStateUserRelations() throws Exception {
        // Initialize the database
        stateUserRelationRepository.saveAndFlush(stateUserRelation);

        // Get all the stateUserRelationList
        restStateUserRelationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stateUserRelation.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())))
            .andExpect(jsonPath("$.[*].idState").value(hasItem(DEFAULT_ID_STATE.intValue())));
    }

    @Test
    @Transactional
    void getStateUserRelation() throws Exception {
        // Initialize the database
        stateUserRelationRepository.saveAndFlush(stateUserRelation);

        // Get the stateUserRelation
        restStateUserRelationMockMvc
            .perform(get(ENTITY_API_URL_ID, stateUserRelation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(stateUserRelation.getId().intValue()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()))
            .andExpect(jsonPath("$.idState").value(DEFAULT_ID_STATE.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingStateUserRelation() throws Exception {
        // Get the stateUserRelation
        restStateUserRelationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingStateUserRelation() throws Exception {
        // Initialize the database
        stateUserRelationRepository.saveAndFlush(stateUserRelation);

        int databaseSizeBeforeUpdate = stateUserRelationRepository.findAll().size();

        // Update the stateUserRelation
        StateUserRelation updatedStateUserRelation = stateUserRelationRepository.findById(stateUserRelation.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedStateUserRelation are not directly saved in db
        em.detach(updatedStateUserRelation);
        updatedStateUserRelation
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .idState(UPDATED_ID_STATE);
        StateUserRelationDTO stateUserRelationDTO = stateUserRelationMapper.toDto(updatedStateUserRelation);

        restStateUserRelationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, stateUserRelationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(stateUserRelationDTO))
            )
            .andExpect(status().isOk());

        // Validate the StateUserRelation in the database
        List<StateUserRelation> stateUserRelationList = stateUserRelationRepository.findAll();
        assertThat(stateUserRelationList).hasSize(databaseSizeBeforeUpdate);
        StateUserRelation testStateUserRelation = stateUserRelationList.get(stateUserRelationList.size() - 1);
        assertThat(testStateUserRelation.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testStateUserRelation.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testStateUserRelation.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testStateUserRelation.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testStateUserRelation.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testStateUserRelation.getIdState()).isEqualTo(UPDATED_ID_STATE);
    }

    @Test
    @Transactional
    void putNonExistingStateUserRelation() throws Exception {
        int databaseSizeBeforeUpdate = stateUserRelationRepository.findAll().size();
        stateUserRelation.setId(longCount.incrementAndGet());

        // Create the StateUserRelation
        StateUserRelationDTO stateUserRelationDTO = stateUserRelationMapper.toDto(stateUserRelation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStateUserRelationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, stateUserRelationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(stateUserRelationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StateUserRelation in the database
        List<StateUserRelation> stateUserRelationList = stateUserRelationRepository.findAll();
        assertThat(stateUserRelationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStateUserRelation() throws Exception {
        int databaseSizeBeforeUpdate = stateUserRelationRepository.findAll().size();
        stateUserRelation.setId(longCount.incrementAndGet());

        // Create the StateUserRelation
        StateUserRelationDTO stateUserRelationDTO = stateUserRelationMapper.toDto(stateUserRelation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStateUserRelationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(stateUserRelationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StateUserRelation in the database
        List<StateUserRelation> stateUserRelationList = stateUserRelationRepository.findAll();
        assertThat(stateUserRelationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStateUserRelation() throws Exception {
        int databaseSizeBeforeUpdate = stateUserRelationRepository.findAll().size();
        stateUserRelation.setId(longCount.incrementAndGet());

        // Create the StateUserRelation
        StateUserRelationDTO stateUserRelationDTO = stateUserRelationMapper.toDto(stateUserRelation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStateUserRelationMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(stateUserRelationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the StateUserRelation in the database
        List<StateUserRelation> stateUserRelationList = stateUserRelationRepository.findAll();
        assertThat(stateUserRelationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStateUserRelationWithPatch() throws Exception {
        // Initialize the database
        stateUserRelationRepository.saveAndFlush(stateUserRelation);

        int databaseSizeBeforeUpdate = stateUserRelationRepository.findAll().size();

        // Update the stateUserRelation using partial update
        StateUserRelation partialUpdatedStateUserRelation = new StateUserRelation();
        partialUpdatedStateUserRelation.setId(stateUserRelation.getId());

        partialUpdatedStateUserRelation
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .idState(UPDATED_ID_STATE);

        restStateUserRelationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStateUserRelation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStateUserRelation))
            )
            .andExpect(status().isOk());

        // Validate the StateUserRelation in the database
        List<StateUserRelation> stateUserRelationList = stateUserRelationRepository.findAll();
        assertThat(stateUserRelationList).hasSize(databaseSizeBeforeUpdate);
        StateUserRelation testStateUserRelation = stateUserRelationList.get(stateUserRelationList.size() - 1);
        assertThat(testStateUserRelation.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testStateUserRelation.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testStateUserRelation.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testStateUserRelation.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testStateUserRelation.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testStateUserRelation.getIdState()).isEqualTo(UPDATED_ID_STATE);
    }

    @Test
    @Transactional
    void fullUpdateStateUserRelationWithPatch() throws Exception {
        // Initialize the database
        stateUserRelationRepository.saveAndFlush(stateUserRelation);

        int databaseSizeBeforeUpdate = stateUserRelationRepository.findAll().size();

        // Update the stateUserRelation using partial update
        StateUserRelation partialUpdatedStateUserRelation = new StateUserRelation();
        partialUpdatedStateUserRelation.setId(stateUserRelation.getId());

        partialUpdatedStateUserRelation
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .idState(UPDATED_ID_STATE);

        restStateUserRelationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStateUserRelation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStateUserRelation))
            )
            .andExpect(status().isOk());

        // Validate the StateUserRelation in the database
        List<StateUserRelation> stateUserRelationList = stateUserRelationRepository.findAll();
        assertThat(stateUserRelationList).hasSize(databaseSizeBeforeUpdate);
        StateUserRelation testStateUserRelation = stateUserRelationList.get(stateUserRelationList.size() - 1);
        assertThat(testStateUserRelation.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testStateUserRelation.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testStateUserRelation.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testStateUserRelation.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testStateUserRelation.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testStateUserRelation.getIdState()).isEqualTo(UPDATED_ID_STATE);
    }

    @Test
    @Transactional
    void patchNonExistingStateUserRelation() throws Exception {
        int databaseSizeBeforeUpdate = stateUserRelationRepository.findAll().size();
        stateUserRelation.setId(longCount.incrementAndGet());

        // Create the StateUserRelation
        StateUserRelationDTO stateUserRelationDTO = stateUserRelationMapper.toDto(stateUserRelation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStateUserRelationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, stateUserRelationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(stateUserRelationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StateUserRelation in the database
        List<StateUserRelation> stateUserRelationList = stateUserRelationRepository.findAll();
        assertThat(stateUserRelationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStateUserRelation() throws Exception {
        int databaseSizeBeforeUpdate = stateUserRelationRepository.findAll().size();
        stateUserRelation.setId(longCount.incrementAndGet());

        // Create the StateUserRelation
        StateUserRelationDTO stateUserRelationDTO = stateUserRelationMapper.toDto(stateUserRelation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStateUserRelationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(stateUserRelationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StateUserRelation in the database
        List<StateUserRelation> stateUserRelationList = stateUserRelationRepository.findAll();
        assertThat(stateUserRelationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStateUserRelation() throws Exception {
        int databaseSizeBeforeUpdate = stateUserRelationRepository.findAll().size();
        stateUserRelation.setId(longCount.incrementAndGet());

        // Create the StateUserRelation
        StateUserRelationDTO stateUserRelationDTO = stateUserRelationMapper.toDto(stateUserRelation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStateUserRelationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(stateUserRelationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the StateUserRelation in the database
        List<StateUserRelation> stateUserRelationList = stateUserRelationRepository.findAll();
        assertThat(stateUserRelationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStateUserRelation() throws Exception {
        // Initialize the database
        stateUserRelationRepository.saveAndFlush(stateUserRelation);

        int databaseSizeBeforeDelete = stateUserRelationRepository.findAll().size();

        // Delete the stateUserRelation
        restStateUserRelationMockMvc
            .perform(delete(ENTITY_API_URL_ID, stateUserRelation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<StateUserRelation> stateUserRelationList = stateUserRelationRepository.findAll();
        assertThat(stateUserRelationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
