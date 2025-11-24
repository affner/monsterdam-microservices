package com.monsterdam.interactions.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.monsterdam.interactions.IntegrationTest;
import com.monsterdam.interactions.domain.UserMention;
import com.monsterdam.interactions.repository.UserMentionRepository;
import com.monsterdam.interactions.service.UserMentionService;
import com.monsterdam.interactions.service.dto.UserMentionDTO;
import com.monsterdam.interactions.service.mapper.UserMentionMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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
 * Integration tests for the {@link UserMentionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class UserMentionResourceIT {

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

    private static final Long DEFAULT_MENTIONED_USER_ID = 1L;
    private static final Long UPDATED_MENTIONED_USER_ID = 2L;

    private static final String ENTITY_API_URL = "/api/user-mentions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UserMentionRepository userMentionRepository;

    @Mock
    private UserMentionRepository userMentionRepositoryMock;

    @Autowired
    private UserMentionMapper userMentionMapper;

    @Mock
    private UserMentionService userMentionServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserMentionMockMvc;

    private UserMention userMention;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserMention createEntity(EntityManager em) {
        UserMention userMention = new UserMention()
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .isDeleted(DEFAULT_IS_DELETED)
            .mentionedUserId(DEFAULT_MENTIONED_USER_ID);
        return userMention;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserMention createUpdatedEntity(EntityManager em) {
        UserMention userMention = new UserMention()
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .mentionedUserId(UPDATED_MENTIONED_USER_ID);
        return userMention;
    }

    @BeforeEach
    public void initTest() {
        userMention = createEntity(em);
    }

    @Test
    @Transactional
    void createUserMention() throws Exception {
        int databaseSizeBeforeCreate = userMentionRepository.findAll().size();
        // Create the UserMention
        UserMentionDTO userMentionDTO = userMentionMapper.toDto(userMention);
        restUserMentionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userMentionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the UserMention in the database
        List<UserMention> userMentionList = userMentionRepository.findAll();
        assertThat(userMentionList).hasSize(databaseSizeBeforeCreate + 1);
        UserMention testUserMention = userMentionList.get(userMentionList.size() - 1);
        assertThat(testUserMention.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testUserMention.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testUserMention.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testUserMention.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testUserMention.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
        assertThat(testUserMention.getMentionedUserId()).isEqualTo(DEFAULT_MENTIONED_USER_ID);
    }

    @Test
    @Transactional
    void createUserMentionWithExistingId() throws Exception {
        // Create the UserMention with an existing ID
        userMention.setId(1L);
        UserMentionDTO userMentionDTO = userMentionMapper.toDto(userMention);

        int databaseSizeBeforeCreate = userMentionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserMentionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userMentionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserMention in the database
        List<UserMention> userMentionList = userMentionRepository.findAll();
        assertThat(userMentionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = userMentionRepository.findAll().size();
        // set the field null
        userMention.setCreatedDate(null);

        // Create the UserMention, which fails.
        UserMentionDTO userMentionDTO = userMentionMapper.toDto(userMention);

        restUserMentionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userMentionDTO))
            )
            .andExpect(status().isBadRequest());

        List<UserMention> userMentionList = userMentionRepository.findAll();
        assertThat(userMentionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = userMentionRepository.findAll().size();
        // set the field null
        userMention.setIsDeleted(null);

        // Create the UserMention, which fails.
        UserMentionDTO userMentionDTO = userMentionMapper.toDto(userMention);

        restUserMentionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userMentionDTO))
            )
            .andExpect(status().isBadRequest());

        List<UserMention> userMentionList = userMentionRepository.findAll();
        assertThat(userMentionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMentionedUserIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = userMentionRepository.findAll().size();
        // set the field null
        userMention.setMentionedUserId(null);

        // Create the UserMention, which fails.
        UserMentionDTO userMentionDTO = userMentionMapper.toDto(userMention);

        restUserMentionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userMentionDTO))
            )
            .andExpect(status().isBadRequest());

        List<UserMention> userMentionList = userMentionRepository.findAll();
        assertThat(userMentionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUserMentions() throws Exception {
        // Initialize the database
        userMentionRepository.saveAndFlush(userMention);

        // Get all the userMentionList
        restUserMentionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userMention.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())))
            .andExpect(jsonPath("$.[*].mentionedUserId").value(hasItem(DEFAULT_MENTIONED_USER_ID.intValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUserMentionsWithEagerRelationshipsIsEnabled() throws Exception {
        when(userMentionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUserMentionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(userMentionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUserMentionsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(userMentionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUserMentionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(userMentionRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getUserMention() throws Exception {
        // Initialize the database
        userMentionRepository.saveAndFlush(userMention);

        // Get the userMention
        restUserMentionMockMvc
            .perform(get(ENTITY_API_URL_ID, userMention.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userMention.getId().intValue()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()))
            .andExpect(jsonPath("$.mentionedUserId").value(DEFAULT_MENTIONED_USER_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingUserMention() throws Exception {
        // Get the userMention
        restUserMentionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUserMention() throws Exception {
        // Initialize the database
        userMentionRepository.saveAndFlush(userMention);

        int databaseSizeBeforeUpdate = userMentionRepository.findAll().size();

        // Update the userMention
        UserMention updatedUserMention = userMentionRepository.findById(userMention.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUserMention are not directly saved in db
        em.detach(updatedUserMention);
        updatedUserMention
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .mentionedUserId(UPDATED_MENTIONED_USER_ID);
        UserMentionDTO userMentionDTO = userMentionMapper.toDto(updatedUserMention);

        restUserMentionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userMentionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userMentionDTO))
            )
            .andExpect(status().isOk());

        // Validate the UserMention in the database
        List<UserMention> userMentionList = userMentionRepository.findAll();
        assertThat(userMentionList).hasSize(databaseSizeBeforeUpdate);
        UserMention testUserMention = userMentionList.get(userMentionList.size() - 1);
        assertThat(testUserMention.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testUserMention.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testUserMention.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testUserMention.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testUserMention.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testUserMention.getMentionedUserId()).isEqualTo(UPDATED_MENTIONED_USER_ID);
    }

    @Test
    @Transactional
    void putNonExistingUserMention() throws Exception {
        int databaseSizeBeforeUpdate = userMentionRepository.findAll().size();
        userMention.setId(longCount.incrementAndGet());

        // Create the UserMention
        UserMentionDTO userMentionDTO = userMentionMapper.toDto(userMention);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserMentionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userMentionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userMentionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserMention in the database
        List<UserMention> userMentionList = userMentionRepository.findAll();
        assertThat(userMentionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserMention() throws Exception {
        int databaseSizeBeforeUpdate = userMentionRepository.findAll().size();
        userMention.setId(longCount.incrementAndGet());

        // Create the UserMention
        UserMentionDTO userMentionDTO = userMentionMapper.toDto(userMention);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserMentionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userMentionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserMention in the database
        List<UserMention> userMentionList = userMentionRepository.findAll();
        assertThat(userMentionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserMention() throws Exception {
        int databaseSizeBeforeUpdate = userMentionRepository.findAll().size();
        userMention.setId(longCount.incrementAndGet());

        // Create the UserMention
        UserMentionDTO userMentionDTO = userMentionMapper.toDto(userMention);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserMentionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userMentionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserMention in the database
        List<UserMention> userMentionList = userMentionRepository.findAll();
        assertThat(userMentionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserMentionWithPatch() throws Exception {
        // Initialize the database
        userMentionRepository.saveAndFlush(userMention);

        int databaseSizeBeforeUpdate = userMentionRepository.findAll().size();

        // Update the userMention using partial update
        UserMention partialUpdatedUserMention = new UserMention();
        partialUpdatedUserMention.setId(userMention.getId());

        partialUpdatedUserMention
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .mentionedUserId(UPDATED_MENTIONED_USER_ID);

        restUserMentionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserMention.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserMention))
            )
            .andExpect(status().isOk());

        // Validate the UserMention in the database
        List<UserMention> userMentionList = userMentionRepository.findAll();
        assertThat(userMentionList).hasSize(databaseSizeBeforeUpdate);
        UserMention testUserMention = userMentionList.get(userMentionList.size() - 1);
        assertThat(testUserMention.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testUserMention.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testUserMention.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testUserMention.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testUserMention.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
        assertThat(testUserMention.getMentionedUserId()).isEqualTo(UPDATED_MENTIONED_USER_ID);
    }

    @Test
    @Transactional
    void fullUpdateUserMentionWithPatch() throws Exception {
        // Initialize the database
        userMentionRepository.saveAndFlush(userMention);

        int databaseSizeBeforeUpdate = userMentionRepository.findAll().size();

        // Update the userMention using partial update
        UserMention partialUpdatedUserMention = new UserMention();
        partialUpdatedUserMention.setId(userMention.getId());

        partialUpdatedUserMention
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .mentionedUserId(UPDATED_MENTIONED_USER_ID);

        restUserMentionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserMention.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserMention))
            )
            .andExpect(status().isOk());

        // Validate the UserMention in the database
        List<UserMention> userMentionList = userMentionRepository.findAll();
        assertThat(userMentionList).hasSize(databaseSizeBeforeUpdate);
        UserMention testUserMention = userMentionList.get(userMentionList.size() - 1);
        assertThat(testUserMention.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testUserMention.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testUserMention.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testUserMention.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testUserMention.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testUserMention.getMentionedUserId()).isEqualTo(UPDATED_MENTIONED_USER_ID);
    }

    @Test
    @Transactional
    void patchNonExistingUserMention() throws Exception {
        int databaseSizeBeforeUpdate = userMentionRepository.findAll().size();
        userMention.setId(longCount.incrementAndGet());

        // Create the UserMention
        UserMentionDTO userMentionDTO = userMentionMapper.toDto(userMention);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserMentionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userMentionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userMentionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserMention in the database
        List<UserMention> userMentionList = userMentionRepository.findAll();
        assertThat(userMentionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserMention() throws Exception {
        int databaseSizeBeforeUpdate = userMentionRepository.findAll().size();
        userMention.setId(longCount.incrementAndGet());

        // Create the UserMention
        UserMentionDTO userMentionDTO = userMentionMapper.toDto(userMention);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserMentionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userMentionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserMention in the database
        List<UserMention> userMentionList = userMentionRepository.findAll();
        assertThat(userMentionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserMention() throws Exception {
        int databaseSizeBeforeUpdate = userMentionRepository.findAll().size();
        userMention.setId(longCount.incrementAndGet());

        // Create the UserMention
        UserMentionDTO userMentionDTO = userMentionMapper.toDto(userMention);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserMentionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(userMentionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserMention in the database
        List<UserMention> userMentionList = userMentionRepository.findAll();
        assertThat(userMentionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserMention() throws Exception {
        // Initialize the database
        userMentionRepository.saveAndFlush(userMention);

        int databaseSizeBeforeDelete = userMentionRepository.findAll().size();

        // Delete the userMention
        restUserMentionMockMvc
            .perform(delete(ENTITY_API_URL_ID, userMention.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserMention> userMentionList = userMentionRepository.findAll();
        assertThat(userMentionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
