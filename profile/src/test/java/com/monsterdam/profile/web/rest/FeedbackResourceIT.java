package com.monsterdam.profile.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.monsterdam.profile.IntegrationTest;
import com.monsterdam.profile.domain.Feedback;
import com.monsterdam.profile.domain.enumeration.FeedbackType;
import com.monsterdam.profile.repository.FeedbackRepository;
import com.monsterdam.profile.service.dto.FeedbackDTO;
import com.monsterdam.profile.service.mapper.FeedbackMapper;
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
 * Integration tests for the {@link FeedbackResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FeedbackResourceIT {

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final Instant DEFAULT_FEEDBACK_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FEEDBACK_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_FEEDBACK_RATING = 1;
    private static final Integer UPDATED_FEEDBACK_RATING = 2;

    private static final FeedbackType DEFAULT_FEEDBACK_TYPE = FeedbackType.ERROR;
    private static final FeedbackType UPDATED_FEEDBACK_TYPE = FeedbackType.SUGGESTION;

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

    private static final Long DEFAULT_CREATOR_ID = 1L;
    private static final Long UPDATED_CREATOR_ID = 2L;

    private static final String ENTITY_API_URL = "/api/feedbacks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private FeedbackMapper feedbackMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFeedbackMockMvc;

    private Feedback feedback;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Feedback createEntity(EntityManager em) {
        Feedback feedback = new Feedback()
            .content(DEFAULT_CONTENT)
            .feedbackDate(DEFAULT_FEEDBACK_DATE)
            .feedbackRating(DEFAULT_FEEDBACK_RATING)
            .feedbackType(DEFAULT_FEEDBACK_TYPE)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .isDeleted(DEFAULT_IS_DELETED)
            .creatorId(DEFAULT_CREATOR_ID);
        return feedback;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Feedback createUpdatedEntity(EntityManager em) {
        Feedback feedback = new Feedback()
            .content(UPDATED_CONTENT)
            .feedbackDate(UPDATED_FEEDBACK_DATE)
            .feedbackRating(UPDATED_FEEDBACK_RATING)
            .feedbackType(UPDATED_FEEDBACK_TYPE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .creatorId(UPDATED_CREATOR_ID);
        return feedback;
    }

    @BeforeEach
    public void initTest() {
        feedback = createEntity(em);
    }

    @Test
    @Transactional
    void createFeedback() throws Exception {
        int databaseSizeBeforeCreate = feedbackRepository.findAll().size();
        // Create the Feedback
        FeedbackDTO feedbackDTO = feedbackMapper.toDto(feedback);
        restFeedbackMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(feedbackDTO)))
            .andExpect(status().isCreated());

        // Validate the Feedback in the database
        List<Feedback> feedbackList = feedbackRepository.findAll();
        assertThat(feedbackList).hasSize(databaseSizeBeforeCreate + 1);
        Feedback testFeedback = feedbackList.get(feedbackList.size() - 1);
        assertThat(testFeedback.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testFeedback.getFeedbackDate()).isEqualTo(DEFAULT_FEEDBACK_DATE);
        assertThat(testFeedback.getFeedbackRating()).isEqualTo(DEFAULT_FEEDBACK_RATING);
        assertThat(testFeedback.getFeedbackType()).isEqualTo(DEFAULT_FEEDBACK_TYPE);
        assertThat(testFeedback.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testFeedback.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testFeedback.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testFeedback.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testFeedback.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
        assertThat(testFeedback.getCreatorId()).isEqualTo(DEFAULT_CREATOR_ID);
    }

    @Test
    @Transactional
    void createFeedbackWithExistingId() throws Exception {
        // Create the Feedback with an existing ID
        feedback.setId(1L);
        FeedbackDTO feedbackDTO = feedbackMapper.toDto(feedback);

        int databaseSizeBeforeCreate = feedbackRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFeedbackMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(feedbackDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Feedback in the database
        List<Feedback> feedbackList = feedbackRepository.findAll();
        assertThat(feedbackList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkContentIsRequired() throws Exception {
        int databaseSizeBeforeTest = feedbackRepository.findAll().size();
        // set the field null
        feedback.setContent(null);

        // Create the Feedback, which fails.
        FeedbackDTO feedbackDTO = feedbackMapper.toDto(feedback);

        restFeedbackMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(feedbackDTO)))
            .andExpect(status().isBadRequest());

        List<Feedback> feedbackList = feedbackRepository.findAll();
        assertThat(feedbackList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFeedbackDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = feedbackRepository.findAll().size();
        // set the field null
        feedback.setFeedbackDate(null);

        // Create the Feedback, which fails.
        FeedbackDTO feedbackDTO = feedbackMapper.toDto(feedback);

        restFeedbackMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(feedbackDTO)))
            .andExpect(status().isBadRequest());

        List<Feedback> feedbackList = feedbackRepository.findAll();
        assertThat(feedbackList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = feedbackRepository.findAll().size();
        // set the field null
        feedback.setCreatedDate(null);

        // Create the Feedback, which fails.
        FeedbackDTO feedbackDTO = feedbackMapper.toDto(feedback);

        restFeedbackMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(feedbackDTO)))
            .andExpect(status().isBadRequest());

        List<Feedback> feedbackList = feedbackRepository.findAll();
        assertThat(feedbackList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = feedbackRepository.findAll().size();
        // set the field null
        feedback.setIsDeleted(null);

        // Create the Feedback, which fails.
        FeedbackDTO feedbackDTO = feedbackMapper.toDto(feedback);

        restFeedbackMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(feedbackDTO)))
            .andExpect(status().isBadRequest());

        List<Feedback> feedbackList = feedbackRepository.findAll();
        assertThat(feedbackList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatorIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = feedbackRepository.findAll().size();
        // set the field null
        feedback.setCreatorId(null);

        // Create the Feedback, which fails.
        FeedbackDTO feedbackDTO = feedbackMapper.toDto(feedback);

        restFeedbackMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(feedbackDTO)))
            .andExpect(status().isBadRequest());

        List<Feedback> feedbackList = feedbackRepository.findAll();
        assertThat(feedbackList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFeedbacks() throws Exception {
        // Initialize the database
        feedbackRepository.saveAndFlush(feedback);

        // Get all the feedbackList
        restFeedbackMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(feedback.getId().intValue())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].feedbackDate").value(hasItem(DEFAULT_FEEDBACK_DATE.toString())))
            .andExpect(jsonPath("$.[*].feedbackRating").value(hasItem(DEFAULT_FEEDBACK_RATING)))
            .andExpect(jsonPath("$.[*].feedbackType").value(hasItem(DEFAULT_FEEDBACK_TYPE.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())))
            .andExpect(jsonPath("$.[*].creatorId").value(hasItem(DEFAULT_CREATOR_ID.intValue())));
    }

    @Test
    @Transactional
    void getFeedback() throws Exception {
        // Initialize the database
        feedbackRepository.saveAndFlush(feedback);

        // Get the feedback
        restFeedbackMockMvc
            .perform(get(ENTITY_API_URL_ID, feedback.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(feedback.getId().intValue()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT))
            .andExpect(jsonPath("$.feedbackDate").value(DEFAULT_FEEDBACK_DATE.toString()))
            .andExpect(jsonPath("$.feedbackRating").value(DEFAULT_FEEDBACK_RATING))
            .andExpect(jsonPath("$.feedbackType").value(DEFAULT_FEEDBACK_TYPE.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()))
            .andExpect(jsonPath("$.creatorId").value(DEFAULT_CREATOR_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingFeedback() throws Exception {
        // Get the feedback
        restFeedbackMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFeedback() throws Exception {
        // Initialize the database
        feedbackRepository.saveAndFlush(feedback);

        int databaseSizeBeforeUpdate = feedbackRepository.findAll().size();

        // Update the feedback
        Feedback updatedFeedback = feedbackRepository.findById(feedback.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFeedback are not directly saved in db
        em.detach(updatedFeedback);
        updatedFeedback
            .content(UPDATED_CONTENT)
            .feedbackDate(UPDATED_FEEDBACK_DATE)
            .feedbackRating(UPDATED_FEEDBACK_RATING)
            .feedbackType(UPDATED_FEEDBACK_TYPE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .creatorId(UPDATED_CREATOR_ID);
        FeedbackDTO feedbackDTO = feedbackMapper.toDto(updatedFeedback);

        restFeedbackMockMvc
            .perform(
                put(ENTITY_API_URL_ID, feedbackDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(feedbackDTO))
            )
            .andExpect(status().isOk());

        // Validate the Feedback in the database
        List<Feedback> feedbackList = feedbackRepository.findAll();
        assertThat(feedbackList).hasSize(databaseSizeBeforeUpdate);
        Feedback testFeedback = feedbackList.get(feedbackList.size() - 1);
        assertThat(testFeedback.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testFeedback.getFeedbackDate()).isEqualTo(UPDATED_FEEDBACK_DATE);
        assertThat(testFeedback.getFeedbackRating()).isEqualTo(UPDATED_FEEDBACK_RATING);
        assertThat(testFeedback.getFeedbackType()).isEqualTo(UPDATED_FEEDBACK_TYPE);
        assertThat(testFeedback.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testFeedback.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testFeedback.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testFeedback.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testFeedback.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testFeedback.getCreatorId()).isEqualTo(UPDATED_CREATOR_ID);
    }

    @Test
    @Transactional
    void putNonExistingFeedback() throws Exception {
        int databaseSizeBeforeUpdate = feedbackRepository.findAll().size();
        feedback.setId(longCount.incrementAndGet());

        // Create the Feedback
        FeedbackDTO feedbackDTO = feedbackMapper.toDto(feedback);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFeedbackMockMvc
            .perform(
                put(ENTITY_API_URL_ID, feedbackDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(feedbackDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Feedback in the database
        List<Feedback> feedbackList = feedbackRepository.findAll();
        assertThat(feedbackList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFeedback() throws Exception {
        int databaseSizeBeforeUpdate = feedbackRepository.findAll().size();
        feedback.setId(longCount.incrementAndGet());

        // Create the Feedback
        FeedbackDTO feedbackDTO = feedbackMapper.toDto(feedback);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeedbackMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(feedbackDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Feedback in the database
        List<Feedback> feedbackList = feedbackRepository.findAll();
        assertThat(feedbackList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFeedback() throws Exception {
        int databaseSizeBeforeUpdate = feedbackRepository.findAll().size();
        feedback.setId(longCount.incrementAndGet());

        // Create the Feedback
        FeedbackDTO feedbackDTO = feedbackMapper.toDto(feedback);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeedbackMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(feedbackDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Feedback in the database
        List<Feedback> feedbackList = feedbackRepository.findAll();
        assertThat(feedbackList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFeedbackWithPatch() throws Exception {
        // Initialize the database
        feedbackRepository.saveAndFlush(feedback);

        int databaseSizeBeforeUpdate = feedbackRepository.findAll().size();

        // Update the feedback using partial update
        Feedback partialUpdatedFeedback = new Feedback();
        partialUpdatedFeedback.setId(feedback.getId());

        partialUpdatedFeedback
            .feedbackRating(UPDATED_FEEDBACK_RATING)
            .feedbackType(UPDATED_FEEDBACK_TYPE)
            .createdBy(UPDATED_CREATED_BY)
            .isDeleted(UPDATED_IS_DELETED);

        restFeedbackMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFeedback.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFeedback))
            )
            .andExpect(status().isOk());

        // Validate the Feedback in the database
        List<Feedback> feedbackList = feedbackRepository.findAll();
        assertThat(feedbackList).hasSize(databaseSizeBeforeUpdate);
        Feedback testFeedback = feedbackList.get(feedbackList.size() - 1);
        assertThat(testFeedback.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testFeedback.getFeedbackDate()).isEqualTo(DEFAULT_FEEDBACK_DATE);
        assertThat(testFeedback.getFeedbackRating()).isEqualTo(UPDATED_FEEDBACK_RATING);
        assertThat(testFeedback.getFeedbackType()).isEqualTo(UPDATED_FEEDBACK_TYPE);
        assertThat(testFeedback.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testFeedback.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testFeedback.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testFeedback.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testFeedback.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testFeedback.getCreatorId()).isEqualTo(DEFAULT_CREATOR_ID);
    }

    @Test
    @Transactional
    void fullUpdateFeedbackWithPatch() throws Exception {
        // Initialize the database
        feedbackRepository.saveAndFlush(feedback);

        int databaseSizeBeforeUpdate = feedbackRepository.findAll().size();

        // Update the feedback using partial update
        Feedback partialUpdatedFeedback = new Feedback();
        partialUpdatedFeedback.setId(feedback.getId());

        partialUpdatedFeedback
            .content(UPDATED_CONTENT)
            .feedbackDate(UPDATED_FEEDBACK_DATE)
            .feedbackRating(UPDATED_FEEDBACK_RATING)
            .feedbackType(UPDATED_FEEDBACK_TYPE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .creatorId(UPDATED_CREATOR_ID);

        restFeedbackMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFeedback.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFeedback))
            )
            .andExpect(status().isOk());

        // Validate the Feedback in the database
        List<Feedback> feedbackList = feedbackRepository.findAll();
        assertThat(feedbackList).hasSize(databaseSizeBeforeUpdate);
        Feedback testFeedback = feedbackList.get(feedbackList.size() - 1);
        assertThat(testFeedback.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testFeedback.getFeedbackDate()).isEqualTo(UPDATED_FEEDBACK_DATE);
        assertThat(testFeedback.getFeedbackRating()).isEqualTo(UPDATED_FEEDBACK_RATING);
        assertThat(testFeedback.getFeedbackType()).isEqualTo(UPDATED_FEEDBACK_TYPE);
        assertThat(testFeedback.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testFeedback.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testFeedback.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testFeedback.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testFeedback.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testFeedback.getCreatorId()).isEqualTo(UPDATED_CREATOR_ID);
    }

    @Test
    @Transactional
    void patchNonExistingFeedback() throws Exception {
        int databaseSizeBeforeUpdate = feedbackRepository.findAll().size();
        feedback.setId(longCount.incrementAndGet());

        // Create the Feedback
        FeedbackDTO feedbackDTO = feedbackMapper.toDto(feedback);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFeedbackMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, feedbackDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(feedbackDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Feedback in the database
        List<Feedback> feedbackList = feedbackRepository.findAll();
        assertThat(feedbackList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFeedback() throws Exception {
        int databaseSizeBeforeUpdate = feedbackRepository.findAll().size();
        feedback.setId(longCount.incrementAndGet());

        // Create the Feedback
        FeedbackDTO feedbackDTO = feedbackMapper.toDto(feedback);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeedbackMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(feedbackDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Feedback in the database
        List<Feedback> feedbackList = feedbackRepository.findAll();
        assertThat(feedbackList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFeedback() throws Exception {
        int databaseSizeBeforeUpdate = feedbackRepository.findAll().size();
        feedback.setId(longCount.incrementAndGet());

        // Create the Feedback
        FeedbackDTO feedbackDTO = feedbackMapper.toDto(feedback);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeedbackMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(feedbackDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Feedback in the database
        List<Feedback> feedbackList = feedbackRepository.findAll();
        assertThat(feedbackList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFeedback() throws Exception {
        // Initialize the database
        feedbackRepository.saveAndFlush(feedback);

        int databaseSizeBeforeDelete = feedbackRepository.findAll().size();

        // Delete the feedback
        restFeedbackMockMvc
            .perform(delete(ENTITY_API_URL_ID, feedback.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Feedback> feedbackList = feedbackRepository.findAll();
        assertThat(feedbackList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
