package com.monsterdam.profile.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.monsterdam.profile.IntegrationTest;
import com.monsterdam.profile.domain.HashTag;
import com.monsterdam.profile.domain.PostFeedHashTagRelation;
import com.monsterdam.profile.repository.PostFeedHashTagRelationRepository;
import com.monsterdam.profile.service.dto.PostFeedHashTagRelationDTO;
import com.monsterdam.profile.service.mapper.PostFeedHashTagRelationMapper;
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
 * Integration tests for the {@link PostFeedHashTagRelationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PostFeedHashTagRelationResourceIT {

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

    private static final Long DEFAULT_POST_FEED_ID = 1L;
    private static final Long UPDATED_POST_FEED_ID = 2L;

    private static final String ENTITY_API_URL = "/api/post-feed-hash-tag-relations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PostFeedHashTagRelationRepository postFeedHashTagRelationRepository;

    @Autowired
    private PostFeedHashTagRelationMapper postFeedHashTagRelationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPostFeedHashTagRelationMockMvc;

    private PostFeedHashTagRelation postFeedHashTagRelation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PostFeedHashTagRelation createEntity(EntityManager em) {
        PostFeedHashTagRelation postFeedHashTagRelation = new PostFeedHashTagRelation()
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .isDeleted(DEFAULT_IS_DELETED)
            .postFeedId(DEFAULT_POST_FEED_ID);
        // Add required entity
        HashTag hashTag;
        if (TestUtil.findAll(em, HashTag.class).isEmpty()) {
            hashTag = HashTagResourceIT.createEntity(em);
            em.persist(hashTag);
            em.flush();
        } else {
            hashTag = TestUtil.findAll(em, HashTag.class).get(0);
        }
        postFeedHashTagRelation.setHashtag(hashTag);
        return postFeedHashTagRelation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PostFeedHashTagRelation createUpdatedEntity(EntityManager em) {
        PostFeedHashTagRelation postFeedHashTagRelation = new PostFeedHashTagRelation()
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .postFeedId(UPDATED_POST_FEED_ID);
        // Add required entity
        HashTag hashTag;
        if (TestUtil.findAll(em, HashTag.class).isEmpty()) {
            hashTag = HashTagResourceIT.createUpdatedEntity(em);
            em.persist(hashTag);
            em.flush();
        } else {
            hashTag = TestUtil.findAll(em, HashTag.class).get(0);
        }
        postFeedHashTagRelation.setHashtag(hashTag);
        return postFeedHashTagRelation;
    }

    @BeforeEach
    public void initTest() {
        postFeedHashTagRelation = createEntity(em);
    }

    @Test
    @Transactional
    void createPostFeedHashTagRelation() throws Exception {
        int databaseSizeBeforeCreate = postFeedHashTagRelationRepository.findAll().size();
        // Create the PostFeedHashTagRelation
        PostFeedHashTagRelationDTO postFeedHashTagRelationDTO = postFeedHashTagRelationMapper.toDto(postFeedHashTagRelation);
        restPostFeedHashTagRelationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(postFeedHashTagRelationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the PostFeedHashTagRelation in the database
        List<PostFeedHashTagRelation> postFeedHashTagRelationList = postFeedHashTagRelationRepository.findAll();
        assertThat(postFeedHashTagRelationList).hasSize(databaseSizeBeforeCreate + 1);
        PostFeedHashTagRelation testPostFeedHashTagRelation = postFeedHashTagRelationList.get(postFeedHashTagRelationList.size() - 1);
        assertThat(testPostFeedHashTagRelation.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testPostFeedHashTagRelation.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testPostFeedHashTagRelation.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testPostFeedHashTagRelation.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testPostFeedHashTagRelation.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
        assertThat(testPostFeedHashTagRelation.getPostFeedId()).isEqualTo(DEFAULT_POST_FEED_ID);
    }

    @Test
    @Transactional
    void createPostFeedHashTagRelationWithExistingId() throws Exception {
        // Create the PostFeedHashTagRelation with an existing ID
        postFeedHashTagRelation.setId(1L);
        PostFeedHashTagRelationDTO postFeedHashTagRelationDTO = postFeedHashTagRelationMapper.toDto(postFeedHashTagRelation);

        int databaseSizeBeforeCreate = postFeedHashTagRelationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPostFeedHashTagRelationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(postFeedHashTagRelationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PostFeedHashTagRelation in the database
        List<PostFeedHashTagRelation> postFeedHashTagRelationList = postFeedHashTagRelationRepository.findAll();
        assertThat(postFeedHashTagRelationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = postFeedHashTagRelationRepository.findAll().size();
        // set the field null
        postFeedHashTagRelation.setCreatedDate(null);

        // Create the PostFeedHashTagRelation, which fails.
        PostFeedHashTagRelationDTO postFeedHashTagRelationDTO = postFeedHashTagRelationMapper.toDto(postFeedHashTagRelation);

        restPostFeedHashTagRelationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(postFeedHashTagRelationDTO))
            )
            .andExpect(status().isBadRequest());

        List<PostFeedHashTagRelation> postFeedHashTagRelationList = postFeedHashTagRelationRepository.findAll();
        assertThat(postFeedHashTagRelationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = postFeedHashTagRelationRepository.findAll().size();
        // set the field null
        postFeedHashTagRelation.setIsDeleted(null);

        // Create the PostFeedHashTagRelation, which fails.
        PostFeedHashTagRelationDTO postFeedHashTagRelationDTO = postFeedHashTagRelationMapper.toDto(postFeedHashTagRelation);

        restPostFeedHashTagRelationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(postFeedHashTagRelationDTO))
            )
            .andExpect(status().isBadRequest());

        List<PostFeedHashTagRelation> postFeedHashTagRelationList = postFeedHashTagRelationRepository.findAll();
        assertThat(postFeedHashTagRelationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPostFeedIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = postFeedHashTagRelationRepository.findAll().size();
        // set the field null
        postFeedHashTagRelation.setPostFeedId(null);

        // Create the PostFeedHashTagRelation, which fails.
        PostFeedHashTagRelationDTO postFeedHashTagRelationDTO = postFeedHashTagRelationMapper.toDto(postFeedHashTagRelation);

        restPostFeedHashTagRelationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(postFeedHashTagRelationDTO))
            )
            .andExpect(status().isBadRequest());

        List<PostFeedHashTagRelation> postFeedHashTagRelationList = postFeedHashTagRelationRepository.findAll();
        assertThat(postFeedHashTagRelationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPostFeedHashTagRelations() throws Exception {
        // Initialize the database
        postFeedHashTagRelationRepository.saveAndFlush(postFeedHashTagRelation);

        // Get all the postFeedHashTagRelationList
        restPostFeedHashTagRelationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(postFeedHashTagRelation.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())))
            .andExpect(jsonPath("$.[*].postFeedId").value(hasItem(DEFAULT_POST_FEED_ID.intValue())));
    }

    @Test
    @Transactional
    void getPostFeedHashTagRelation() throws Exception {
        // Initialize the database
        postFeedHashTagRelationRepository.saveAndFlush(postFeedHashTagRelation);

        // Get the postFeedHashTagRelation
        restPostFeedHashTagRelationMockMvc
            .perform(get(ENTITY_API_URL_ID, postFeedHashTagRelation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(postFeedHashTagRelation.getId().intValue()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()))
            .andExpect(jsonPath("$.postFeedId").value(DEFAULT_POST_FEED_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingPostFeedHashTagRelation() throws Exception {
        // Get the postFeedHashTagRelation
        restPostFeedHashTagRelationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPostFeedHashTagRelation() throws Exception {
        // Initialize the database
        postFeedHashTagRelationRepository.saveAndFlush(postFeedHashTagRelation);

        int databaseSizeBeforeUpdate = postFeedHashTagRelationRepository.findAll().size();

        // Update the postFeedHashTagRelation
        PostFeedHashTagRelation updatedPostFeedHashTagRelation = postFeedHashTagRelationRepository
            .findById(postFeedHashTagRelation.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedPostFeedHashTagRelation are not directly saved in db
        em.detach(updatedPostFeedHashTagRelation);
        updatedPostFeedHashTagRelation
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .postFeedId(UPDATED_POST_FEED_ID);
        PostFeedHashTagRelationDTO postFeedHashTagRelationDTO = postFeedHashTagRelationMapper.toDto(updatedPostFeedHashTagRelation);

        restPostFeedHashTagRelationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, postFeedHashTagRelationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(postFeedHashTagRelationDTO))
            )
            .andExpect(status().isOk());

        // Validate the PostFeedHashTagRelation in the database
        List<PostFeedHashTagRelation> postFeedHashTagRelationList = postFeedHashTagRelationRepository.findAll();
        assertThat(postFeedHashTagRelationList).hasSize(databaseSizeBeforeUpdate);
        PostFeedHashTagRelation testPostFeedHashTagRelation = postFeedHashTagRelationList.get(postFeedHashTagRelationList.size() - 1);
        assertThat(testPostFeedHashTagRelation.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testPostFeedHashTagRelation.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testPostFeedHashTagRelation.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testPostFeedHashTagRelation.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testPostFeedHashTagRelation.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testPostFeedHashTagRelation.getPostFeedId()).isEqualTo(UPDATED_POST_FEED_ID);
    }

    @Test
    @Transactional
    void putNonExistingPostFeedHashTagRelation() throws Exception {
        int databaseSizeBeforeUpdate = postFeedHashTagRelationRepository.findAll().size();
        postFeedHashTagRelation.setId(longCount.incrementAndGet());

        // Create the PostFeedHashTagRelation
        PostFeedHashTagRelationDTO postFeedHashTagRelationDTO = postFeedHashTagRelationMapper.toDto(postFeedHashTagRelation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPostFeedHashTagRelationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, postFeedHashTagRelationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(postFeedHashTagRelationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PostFeedHashTagRelation in the database
        List<PostFeedHashTagRelation> postFeedHashTagRelationList = postFeedHashTagRelationRepository.findAll();
        assertThat(postFeedHashTagRelationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPostFeedHashTagRelation() throws Exception {
        int databaseSizeBeforeUpdate = postFeedHashTagRelationRepository.findAll().size();
        postFeedHashTagRelation.setId(longCount.incrementAndGet());

        // Create the PostFeedHashTagRelation
        PostFeedHashTagRelationDTO postFeedHashTagRelationDTO = postFeedHashTagRelationMapper.toDto(postFeedHashTagRelation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPostFeedHashTagRelationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(postFeedHashTagRelationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PostFeedHashTagRelation in the database
        List<PostFeedHashTagRelation> postFeedHashTagRelationList = postFeedHashTagRelationRepository.findAll();
        assertThat(postFeedHashTagRelationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPostFeedHashTagRelation() throws Exception {
        int databaseSizeBeforeUpdate = postFeedHashTagRelationRepository.findAll().size();
        postFeedHashTagRelation.setId(longCount.incrementAndGet());

        // Create the PostFeedHashTagRelation
        PostFeedHashTagRelationDTO postFeedHashTagRelationDTO = postFeedHashTagRelationMapper.toDto(postFeedHashTagRelation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPostFeedHashTagRelationMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(postFeedHashTagRelationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PostFeedHashTagRelation in the database
        List<PostFeedHashTagRelation> postFeedHashTagRelationList = postFeedHashTagRelationRepository.findAll();
        assertThat(postFeedHashTagRelationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePostFeedHashTagRelationWithPatch() throws Exception {
        // Initialize the database
        postFeedHashTagRelationRepository.saveAndFlush(postFeedHashTagRelation);

        int databaseSizeBeforeUpdate = postFeedHashTagRelationRepository.findAll().size();

        // Update the postFeedHashTagRelation using partial update
        PostFeedHashTagRelation partialUpdatedPostFeedHashTagRelation = new PostFeedHashTagRelation();
        partialUpdatedPostFeedHashTagRelation.setId(postFeedHashTagRelation.getId());

        partialUpdatedPostFeedHashTagRelation
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .postFeedId(UPDATED_POST_FEED_ID);

        restPostFeedHashTagRelationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPostFeedHashTagRelation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPostFeedHashTagRelation))
            )
            .andExpect(status().isOk());

        // Validate the PostFeedHashTagRelation in the database
        List<PostFeedHashTagRelation> postFeedHashTagRelationList = postFeedHashTagRelationRepository.findAll();
        assertThat(postFeedHashTagRelationList).hasSize(databaseSizeBeforeUpdate);
        PostFeedHashTagRelation testPostFeedHashTagRelation = postFeedHashTagRelationList.get(postFeedHashTagRelationList.size() - 1);
        assertThat(testPostFeedHashTagRelation.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testPostFeedHashTagRelation.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testPostFeedHashTagRelation.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testPostFeedHashTagRelation.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testPostFeedHashTagRelation.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testPostFeedHashTagRelation.getPostFeedId()).isEqualTo(UPDATED_POST_FEED_ID);
    }

    @Test
    @Transactional
    void fullUpdatePostFeedHashTagRelationWithPatch() throws Exception {
        // Initialize the database
        postFeedHashTagRelationRepository.saveAndFlush(postFeedHashTagRelation);

        int databaseSizeBeforeUpdate = postFeedHashTagRelationRepository.findAll().size();

        // Update the postFeedHashTagRelation using partial update
        PostFeedHashTagRelation partialUpdatedPostFeedHashTagRelation = new PostFeedHashTagRelation();
        partialUpdatedPostFeedHashTagRelation.setId(postFeedHashTagRelation.getId());

        partialUpdatedPostFeedHashTagRelation
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .postFeedId(UPDATED_POST_FEED_ID);

        restPostFeedHashTagRelationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPostFeedHashTagRelation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPostFeedHashTagRelation))
            )
            .andExpect(status().isOk());

        // Validate the PostFeedHashTagRelation in the database
        List<PostFeedHashTagRelation> postFeedHashTagRelationList = postFeedHashTagRelationRepository.findAll();
        assertThat(postFeedHashTagRelationList).hasSize(databaseSizeBeforeUpdate);
        PostFeedHashTagRelation testPostFeedHashTagRelation = postFeedHashTagRelationList.get(postFeedHashTagRelationList.size() - 1);
        assertThat(testPostFeedHashTagRelation.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testPostFeedHashTagRelation.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testPostFeedHashTagRelation.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testPostFeedHashTagRelation.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testPostFeedHashTagRelation.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testPostFeedHashTagRelation.getPostFeedId()).isEqualTo(UPDATED_POST_FEED_ID);
    }

    @Test
    @Transactional
    void patchNonExistingPostFeedHashTagRelation() throws Exception {
        int databaseSizeBeforeUpdate = postFeedHashTagRelationRepository.findAll().size();
        postFeedHashTagRelation.setId(longCount.incrementAndGet());

        // Create the PostFeedHashTagRelation
        PostFeedHashTagRelationDTO postFeedHashTagRelationDTO = postFeedHashTagRelationMapper.toDto(postFeedHashTagRelation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPostFeedHashTagRelationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, postFeedHashTagRelationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(postFeedHashTagRelationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PostFeedHashTagRelation in the database
        List<PostFeedHashTagRelation> postFeedHashTagRelationList = postFeedHashTagRelationRepository.findAll();
        assertThat(postFeedHashTagRelationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPostFeedHashTagRelation() throws Exception {
        int databaseSizeBeforeUpdate = postFeedHashTagRelationRepository.findAll().size();
        postFeedHashTagRelation.setId(longCount.incrementAndGet());

        // Create the PostFeedHashTagRelation
        PostFeedHashTagRelationDTO postFeedHashTagRelationDTO = postFeedHashTagRelationMapper.toDto(postFeedHashTagRelation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPostFeedHashTagRelationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(postFeedHashTagRelationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PostFeedHashTagRelation in the database
        List<PostFeedHashTagRelation> postFeedHashTagRelationList = postFeedHashTagRelationRepository.findAll();
        assertThat(postFeedHashTagRelationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPostFeedHashTagRelation() throws Exception {
        int databaseSizeBeforeUpdate = postFeedHashTagRelationRepository.findAll().size();
        postFeedHashTagRelation.setId(longCount.incrementAndGet());

        // Create the PostFeedHashTagRelation
        PostFeedHashTagRelationDTO postFeedHashTagRelationDTO = postFeedHashTagRelationMapper.toDto(postFeedHashTagRelation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPostFeedHashTagRelationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(postFeedHashTagRelationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PostFeedHashTagRelation in the database
        List<PostFeedHashTagRelation> postFeedHashTagRelationList = postFeedHashTagRelationRepository.findAll();
        assertThat(postFeedHashTagRelationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePostFeedHashTagRelation() throws Exception {
        // Initialize the database
        postFeedHashTagRelationRepository.saveAndFlush(postFeedHashTagRelation);

        int databaseSizeBeforeDelete = postFeedHashTagRelationRepository.findAll().size();

        // Delete the postFeedHashTagRelation
        restPostFeedHashTagRelationMockMvc
            .perform(delete(ENTITY_API_URL_ID, postFeedHashTagRelation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PostFeedHashTagRelation> postFeedHashTagRelationList = postFeedHashTagRelationRepository.findAll();
        assertThat(postFeedHashTagRelationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
