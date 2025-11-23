package com.fanflip.interactions.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fanflip.interactions.IntegrationTest;
import com.fanflip.interactions.domain.PostFeed;
import com.fanflip.interactions.domain.PostPoll;
import com.fanflip.interactions.repository.PostPollRepository;
import com.fanflip.interactions.service.dto.PostPollDTO;
import com.fanflip.interactions.service.mapper.PostPollMapper;
import jakarta.persistence.EntityManager;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link PostPollResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PostPollResourceIT {

    private static final String DEFAULT_QUESTION = "AAAAAAAAAA";
    private static final String UPDATED_QUESTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_MULTI_CHOICE = false;
    private static final Boolean UPDATED_IS_MULTI_CHOICE = true;

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Duration DEFAULT_POST_POLL_DURATION = Duration.ofHours(6);
    private static final Duration UPDATED_POST_POLL_DURATION = Duration.ofHours(12);

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_DELETED = false;
    private static final Boolean UPDATED_IS_DELETED = true;

    private static final String ENTITY_API_URL = "/api/post-polls";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PostPollRepository postPollRepository;

    @Autowired
    private PostPollMapper postPollMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPostPollMockMvc;

    private PostPoll postPoll;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PostPoll createEntity(EntityManager em) {
        PostPoll postPoll = new PostPoll()
            .question(DEFAULT_QUESTION)
            .isMultiChoice(DEFAULT_IS_MULTI_CHOICE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .endDate(DEFAULT_END_DATE)
            .postPollDuration(DEFAULT_POST_POLL_DURATION)
            .createdDate(DEFAULT_CREATED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .isDeleted(DEFAULT_IS_DELETED);
        // Add required entity
        PostFeed postFeed;
        if (TestUtil.findAll(em, PostFeed.class).isEmpty()) {
            postFeed = PostFeedResourceIT.createEntity(em);
            em.persist(postFeed);
            em.flush();
        } else {
            postFeed = TestUtil.findAll(em, PostFeed.class).get(0);
        }
        postPoll.setPost(postFeed);
        return postPoll;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PostPoll createUpdatedEntity(EntityManager em) {
        PostPoll postPoll = new PostPoll()
            .question(UPDATED_QUESTION)
            .isMultiChoice(UPDATED_IS_MULTI_CHOICE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .endDate(UPDATED_END_DATE)
            .postPollDuration(UPDATED_POST_POLL_DURATION)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        // Add required entity
        PostFeed postFeed;
        if (TestUtil.findAll(em, PostFeed.class).isEmpty()) {
            postFeed = PostFeedResourceIT.createUpdatedEntity(em);
            em.persist(postFeed);
            em.flush();
        } else {
            postFeed = TestUtil.findAll(em, PostFeed.class).get(0);
        }
        postPoll.setPost(postFeed);
        return postPoll;
    }

    @BeforeEach
    public void initTest() {
        postPoll = createEntity(em);
    }

    @Test
    @Transactional
    void createPostPoll() throws Exception {
        int databaseSizeBeforeCreate = postPollRepository.findAll().size();
        // Create the PostPoll
        PostPollDTO postPollDTO = postPollMapper.toDto(postPoll);
        restPostPollMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(postPollDTO)))
            .andExpect(status().isCreated());

        // Validate the PostPoll in the database
        List<PostPoll> postPollList = postPollRepository.findAll();
        assertThat(postPollList).hasSize(databaseSizeBeforeCreate + 1);
        PostPoll testPostPoll = postPollList.get(postPollList.size() - 1);
        assertThat(testPostPoll.getQuestion()).isEqualTo(DEFAULT_QUESTION);
        assertThat(testPostPoll.getIsMultiChoice()).isEqualTo(DEFAULT_IS_MULTI_CHOICE);
        assertThat(testPostPoll.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testPostPoll.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testPostPoll.getPostPollDuration()).isEqualTo(DEFAULT_POST_POLL_DURATION);
        assertThat(testPostPoll.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testPostPoll.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testPostPoll.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testPostPoll.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    @Transactional
    void createPostPollWithExistingId() throws Exception {
        // Create the PostPoll with an existing ID
        postPoll.setId(1L);
        PostPollDTO postPollDTO = postPollMapper.toDto(postPoll);

        int databaseSizeBeforeCreate = postPollRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPostPollMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(postPollDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PostPoll in the database
        List<PostPoll> postPollList = postPollRepository.findAll();
        assertThat(postPollList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIsMultiChoiceIsRequired() throws Exception {
        int databaseSizeBeforeTest = postPollRepository.findAll().size();
        // set the field null
        postPoll.setIsMultiChoice(null);

        // Create the PostPoll, which fails.
        PostPollDTO postPollDTO = postPollMapper.toDto(postPoll);

        restPostPollMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(postPollDTO)))
            .andExpect(status().isBadRequest());

        List<PostPoll> postPollList = postPollRepository.findAll();
        assertThat(postPollList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEndDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = postPollRepository.findAll().size();
        // set the field null
        postPoll.setEndDate(null);

        // Create the PostPoll, which fails.
        PostPollDTO postPollDTO = postPollMapper.toDto(postPoll);

        restPostPollMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(postPollDTO)))
            .andExpect(status().isBadRequest());

        List<PostPoll> postPollList = postPollRepository.findAll();
        assertThat(postPollList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPostPollDurationIsRequired() throws Exception {
        int databaseSizeBeforeTest = postPollRepository.findAll().size();
        // set the field null
        postPoll.setPostPollDuration(null);

        // Create the PostPoll, which fails.
        PostPollDTO postPollDTO = postPollMapper.toDto(postPoll);

        restPostPollMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(postPollDTO)))
            .andExpect(status().isBadRequest());

        List<PostPoll> postPollList = postPollRepository.findAll();
        assertThat(postPollList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = postPollRepository.findAll().size();
        // set the field null
        postPoll.setCreatedDate(null);

        // Create the PostPoll, which fails.
        PostPollDTO postPollDTO = postPollMapper.toDto(postPoll);

        restPostPollMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(postPollDTO)))
            .andExpect(status().isBadRequest());

        List<PostPoll> postPollList = postPollRepository.findAll();
        assertThat(postPollList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = postPollRepository.findAll().size();
        // set the field null
        postPoll.setIsDeleted(null);

        // Create the PostPoll, which fails.
        PostPollDTO postPollDTO = postPollMapper.toDto(postPoll);

        restPostPollMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(postPollDTO)))
            .andExpect(status().isBadRequest());

        List<PostPoll> postPollList = postPollRepository.findAll();
        assertThat(postPollList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPostPolls() throws Exception {
        // Initialize the database
        postPollRepository.saveAndFlush(postPoll);

        // Get all the postPollList
        restPostPollMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(postPoll.getId().intValue())))
            .andExpect(jsonPath("$.[*].question").value(hasItem(DEFAULT_QUESTION.toString())))
            .andExpect(jsonPath("$.[*].isMultiChoice").value(hasItem(DEFAULT_IS_MULTI_CHOICE.booleanValue())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].postPollDuration").value(hasItem(DEFAULT_POST_POLL_DURATION.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())));
    }

    @Test
    @Transactional
    void getPostPoll() throws Exception {
        // Initialize the database
        postPollRepository.saveAndFlush(postPoll);

        // Get the postPoll
        restPostPollMockMvc
            .perform(get(ENTITY_API_URL_ID, postPoll.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(postPoll.getId().intValue()))
            .andExpect(jsonPath("$.question").value(DEFAULT_QUESTION.toString()))
            .andExpect(jsonPath("$.isMultiChoice").value(DEFAULT_IS_MULTI_CHOICE.booleanValue()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.postPollDuration").value(DEFAULT_POST_POLL_DURATION.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingPostPoll() throws Exception {
        // Get the postPoll
        restPostPollMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPostPoll() throws Exception {
        // Initialize the database
        postPollRepository.saveAndFlush(postPoll);

        int databaseSizeBeforeUpdate = postPollRepository.findAll().size();

        // Update the postPoll
        PostPoll updatedPostPoll = postPollRepository.findById(postPoll.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPostPoll are not directly saved in db
        em.detach(updatedPostPoll);
        updatedPostPoll
            .question(UPDATED_QUESTION)
            .isMultiChoice(UPDATED_IS_MULTI_CHOICE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .endDate(UPDATED_END_DATE)
            .postPollDuration(UPDATED_POST_POLL_DURATION)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        PostPollDTO postPollDTO = postPollMapper.toDto(updatedPostPoll);

        restPostPollMockMvc
            .perform(
                put(ENTITY_API_URL_ID, postPollDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(postPollDTO))
            )
            .andExpect(status().isOk());

        // Validate the PostPoll in the database
        List<PostPoll> postPollList = postPollRepository.findAll();
        assertThat(postPollList).hasSize(databaseSizeBeforeUpdate);
        PostPoll testPostPoll = postPollList.get(postPollList.size() - 1);
        assertThat(testPostPoll.getQuestion()).isEqualTo(UPDATED_QUESTION);
        assertThat(testPostPoll.getIsMultiChoice()).isEqualTo(UPDATED_IS_MULTI_CHOICE);
        assertThat(testPostPoll.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testPostPoll.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testPostPoll.getPostPollDuration()).isEqualTo(UPDATED_POST_POLL_DURATION);
        assertThat(testPostPoll.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testPostPoll.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testPostPoll.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testPostPoll.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void putNonExistingPostPoll() throws Exception {
        int databaseSizeBeforeUpdate = postPollRepository.findAll().size();
        postPoll.setId(longCount.incrementAndGet());

        // Create the PostPoll
        PostPollDTO postPollDTO = postPollMapper.toDto(postPoll);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPostPollMockMvc
            .perform(
                put(ENTITY_API_URL_ID, postPollDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(postPollDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PostPoll in the database
        List<PostPoll> postPollList = postPollRepository.findAll();
        assertThat(postPollList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPostPoll() throws Exception {
        int databaseSizeBeforeUpdate = postPollRepository.findAll().size();
        postPoll.setId(longCount.incrementAndGet());

        // Create the PostPoll
        PostPollDTO postPollDTO = postPollMapper.toDto(postPoll);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPostPollMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(postPollDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PostPoll in the database
        List<PostPoll> postPollList = postPollRepository.findAll();
        assertThat(postPollList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPostPoll() throws Exception {
        int databaseSizeBeforeUpdate = postPollRepository.findAll().size();
        postPoll.setId(longCount.incrementAndGet());

        // Create the PostPoll
        PostPollDTO postPollDTO = postPollMapper.toDto(postPoll);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPostPollMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(postPollDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PostPoll in the database
        List<PostPoll> postPollList = postPollRepository.findAll();
        assertThat(postPollList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePostPollWithPatch() throws Exception {
        // Initialize the database
        postPollRepository.saveAndFlush(postPoll);

        int databaseSizeBeforeUpdate = postPollRepository.findAll().size();

        // Update the postPoll using partial update
        PostPoll partialUpdatedPostPoll = new PostPoll();
        partialUpdatedPostPoll.setId(postPoll.getId());

        partialUpdatedPostPoll
            .question(UPDATED_QUESTION)
            .isMultiChoice(UPDATED_IS_MULTI_CHOICE)
            .endDate(UPDATED_END_DATE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        restPostPollMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPostPoll.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPostPoll))
            )
            .andExpect(status().isOk());

        // Validate the PostPoll in the database
        List<PostPoll> postPollList = postPollRepository.findAll();
        assertThat(postPollList).hasSize(databaseSizeBeforeUpdate);
        PostPoll testPostPoll = postPollList.get(postPollList.size() - 1);
        assertThat(testPostPoll.getQuestion()).isEqualTo(UPDATED_QUESTION);
        assertThat(testPostPoll.getIsMultiChoice()).isEqualTo(UPDATED_IS_MULTI_CHOICE);
        assertThat(testPostPoll.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testPostPoll.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testPostPoll.getPostPollDuration()).isEqualTo(DEFAULT_POST_POLL_DURATION);
        assertThat(testPostPoll.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testPostPoll.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testPostPoll.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testPostPoll.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    @Transactional
    void fullUpdatePostPollWithPatch() throws Exception {
        // Initialize the database
        postPollRepository.saveAndFlush(postPoll);

        int databaseSizeBeforeUpdate = postPollRepository.findAll().size();

        // Update the postPoll using partial update
        PostPoll partialUpdatedPostPoll = new PostPoll();
        partialUpdatedPostPoll.setId(postPoll.getId());

        partialUpdatedPostPoll
            .question(UPDATED_QUESTION)
            .isMultiChoice(UPDATED_IS_MULTI_CHOICE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .endDate(UPDATED_END_DATE)
            .postPollDuration(UPDATED_POST_POLL_DURATION)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);

        restPostPollMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPostPoll.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPostPoll))
            )
            .andExpect(status().isOk());

        // Validate the PostPoll in the database
        List<PostPoll> postPollList = postPollRepository.findAll();
        assertThat(postPollList).hasSize(databaseSizeBeforeUpdate);
        PostPoll testPostPoll = postPollList.get(postPollList.size() - 1);
        assertThat(testPostPoll.getQuestion()).isEqualTo(UPDATED_QUESTION);
        assertThat(testPostPoll.getIsMultiChoice()).isEqualTo(UPDATED_IS_MULTI_CHOICE);
        assertThat(testPostPoll.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testPostPoll.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testPostPoll.getPostPollDuration()).isEqualTo(UPDATED_POST_POLL_DURATION);
        assertThat(testPostPoll.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testPostPoll.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testPostPoll.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testPostPoll.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void patchNonExistingPostPoll() throws Exception {
        int databaseSizeBeforeUpdate = postPollRepository.findAll().size();
        postPoll.setId(longCount.incrementAndGet());

        // Create the PostPoll
        PostPollDTO postPollDTO = postPollMapper.toDto(postPoll);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPostPollMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, postPollDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(postPollDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PostPoll in the database
        List<PostPoll> postPollList = postPollRepository.findAll();
        assertThat(postPollList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPostPoll() throws Exception {
        int databaseSizeBeforeUpdate = postPollRepository.findAll().size();
        postPoll.setId(longCount.incrementAndGet());

        // Create the PostPoll
        PostPollDTO postPollDTO = postPollMapper.toDto(postPoll);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPostPollMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(postPollDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PostPoll in the database
        List<PostPoll> postPollList = postPollRepository.findAll();
        assertThat(postPollList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPostPoll() throws Exception {
        int databaseSizeBeforeUpdate = postPollRepository.findAll().size();
        postPoll.setId(longCount.incrementAndGet());

        // Create the PostPoll
        PostPollDTO postPollDTO = postPollMapper.toDto(postPoll);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPostPollMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(postPollDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PostPoll in the database
        List<PostPoll> postPollList = postPollRepository.findAll();
        assertThat(postPollList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePostPoll() throws Exception {
        // Initialize the database
        postPollRepository.saveAndFlush(postPoll);

        int databaseSizeBeforeDelete = postPollRepository.findAll().size();

        // Delete the postPoll
        restPostPollMockMvc
            .perform(delete(ENTITY_API_URL_ID, postPoll.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PostPoll> postPollList = postPollRepository.findAll();
        assertThat(postPollList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
