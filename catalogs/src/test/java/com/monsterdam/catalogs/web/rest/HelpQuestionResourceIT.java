package com.monsterdam.catalogs.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.monsterdam.catalogs.IntegrationTest;
import com.monsterdam.catalogs.domain.HelpQuestion;
import com.monsterdam.catalogs.repository.HelpQuestionRepository;
import com.monsterdam.catalogs.service.HelpQuestionService;
import com.monsterdam.catalogs.service.dto.HelpQuestionDTO;
import com.monsterdam.catalogs.service.mapper.HelpQuestionMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link HelpQuestionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class HelpQuestionResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_DELETED = false;
    private static final Boolean UPDATED_IS_DELETED = true;

    private static final String ENTITY_API_URL = "/api/help-questions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private HelpQuestionRepository helpQuestionRepository;

    @Mock
    private HelpQuestionRepository helpQuestionRepositoryMock;

    @Autowired
    private HelpQuestionMapper helpQuestionMapper;

    @Mock
    private HelpQuestionService helpQuestionServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHelpQuestionMockMvc;

    private HelpQuestion helpQuestion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HelpQuestion createEntity(EntityManager em) {
        HelpQuestion helpQuestion = new HelpQuestion().title(DEFAULT_TITLE).content(DEFAULT_CONTENT).isDeleted(DEFAULT_IS_DELETED);
        return helpQuestion;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HelpQuestion createUpdatedEntity(EntityManager em) {
        HelpQuestion helpQuestion = new HelpQuestion().title(UPDATED_TITLE).content(UPDATED_CONTENT).isDeleted(UPDATED_IS_DELETED);
        return helpQuestion;
    }

    @BeforeEach
    public void initTest() {
        helpQuestion = createEntity(em);
    }

    @Test
    @Transactional
    void createHelpQuestion() throws Exception {
        int databaseSizeBeforeCreate = helpQuestionRepository.findAll().size();
        // Create the HelpQuestion
        HelpQuestionDTO helpQuestionDTO = helpQuestionMapper.toDto(helpQuestion);
        restHelpQuestionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(helpQuestionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the HelpQuestion in the database
        List<HelpQuestion> helpQuestionList = helpQuestionRepository.findAll();
        assertThat(helpQuestionList).hasSize(databaseSizeBeforeCreate + 1);
        HelpQuestion testHelpQuestion = helpQuestionList.get(helpQuestionList.size() - 1);
        assertThat(testHelpQuestion.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testHelpQuestion.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testHelpQuestion.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    @Transactional
    void createHelpQuestionWithExistingId() throws Exception {
        // Create the HelpQuestion with an existing ID
        helpQuestion.setId(1L);
        HelpQuestionDTO helpQuestionDTO = helpQuestionMapper.toDto(helpQuestion);

        int databaseSizeBeforeCreate = helpQuestionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHelpQuestionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(helpQuestionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HelpQuestion in the database
        List<HelpQuestion> helpQuestionList = helpQuestionRepository.findAll();
        assertThat(helpQuestionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = helpQuestionRepository.findAll().size();
        // set the field null
        helpQuestion.setTitle(null);

        // Create the HelpQuestion, which fails.
        HelpQuestionDTO helpQuestionDTO = helpQuestionMapper.toDto(helpQuestion);

        restHelpQuestionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(helpQuestionDTO))
            )
            .andExpect(status().isBadRequest());

        List<HelpQuestion> helpQuestionList = helpQuestionRepository.findAll();
        assertThat(helpQuestionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = helpQuestionRepository.findAll().size();
        // set the field null
        helpQuestion.setIsDeleted(null);

        // Create the HelpQuestion, which fails.
        HelpQuestionDTO helpQuestionDTO = helpQuestionMapper.toDto(helpQuestion);

        restHelpQuestionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(helpQuestionDTO))
            )
            .andExpect(status().isBadRequest());

        List<HelpQuestion> helpQuestionList = helpQuestionRepository.findAll();
        assertThat(helpQuestionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllHelpQuestions() throws Exception {
        // Initialize the database
        helpQuestionRepository.saveAndFlush(helpQuestion);

        // Get all the helpQuestionList
        restHelpQuestionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(helpQuestion.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllHelpQuestionsWithEagerRelationshipsIsEnabled() throws Exception {
        when(helpQuestionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restHelpQuestionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(helpQuestionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllHelpQuestionsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(helpQuestionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restHelpQuestionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(helpQuestionRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getHelpQuestion() throws Exception {
        // Initialize the database
        helpQuestionRepository.saveAndFlush(helpQuestion);

        // Get the helpQuestion
        restHelpQuestionMockMvc
            .perform(get(ENTITY_API_URL_ID, helpQuestion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(helpQuestion.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingHelpQuestion() throws Exception {
        // Get the helpQuestion
        restHelpQuestionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingHelpQuestion() throws Exception {
        // Initialize the database
        helpQuestionRepository.saveAndFlush(helpQuestion);

        int databaseSizeBeforeUpdate = helpQuestionRepository.findAll().size();

        // Update the helpQuestion
        HelpQuestion updatedHelpQuestion = helpQuestionRepository.findById(helpQuestion.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedHelpQuestion are not directly saved in db
        em.detach(updatedHelpQuestion);
        updatedHelpQuestion.title(UPDATED_TITLE).content(UPDATED_CONTENT).isDeleted(UPDATED_IS_DELETED);
        HelpQuestionDTO helpQuestionDTO = helpQuestionMapper.toDto(updatedHelpQuestion);

        restHelpQuestionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, helpQuestionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(helpQuestionDTO))
            )
            .andExpect(status().isOk());

        // Validate the HelpQuestion in the database
        List<HelpQuestion> helpQuestionList = helpQuestionRepository.findAll();
        assertThat(helpQuestionList).hasSize(databaseSizeBeforeUpdate);
        HelpQuestion testHelpQuestion = helpQuestionList.get(helpQuestionList.size() - 1);
        assertThat(testHelpQuestion.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testHelpQuestion.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testHelpQuestion.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void putNonExistingHelpQuestion() throws Exception {
        int databaseSizeBeforeUpdate = helpQuestionRepository.findAll().size();
        helpQuestion.setId(longCount.incrementAndGet());

        // Create the HelpQuestion
        HelpQuestionDTO helpQuestionDTO = helpQuestionMapper.toDto(helpQuestion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHelpQuestionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, helpQuestionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(helpQuestionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HelpQuestion in the database
        List<HelpQuestion> helpQuestionList = helpQuestionRepository.findAll();
        assertThat(helpQuestionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHelpQuestion() throws Exception {
        int databaseSizeBeforeUpdate = helpQuestionRepository.findAll().size();
        helpQuestion.setId(longCount.incrementAndGet());

        // Create the HelpQuestion
        HelpQuestionDTO helpQuestionDTO = helpQuestionMapper.toDto(helpQuestion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHelpQuestionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(helpQuestionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HelpQuestion in the database
        List<HelpQuestion> helpQuestionList = helpQuestionRepository.findAll();
        assertThat(helpQuestionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHelpQuestion() throws Exception {
        int databaseSizeBeforeUpdate = helpQuestionRepository.findAll().size();
        helpQuestion.setId(longCount.incrementAndGet());

        // Create the HelpQuestion
        HelpQuestionDTO helpQuestionDTO = helpQuestionMapper.toDto(helpQuestion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHelpQuestionMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(helpQuestionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HelpQuestion in the database
        List<HelpQuestion> helpQuestionList = helpQuestionRepository.findAll();
        assertThat(helpQuestionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHelpQuestionWithPatch() throws Exception {
        // Initialize the database
        helpQuestionRepository.saveAndFlush(helpQuestion);

        int databaseSizeBeforeUpdate = helpQuestionRepository.findAll().size();

        // Update the helpQuestion using partial update
        HelpQuestion partialUpdatedHelpQuestion = new HelpQuestion();
        partialUpdatedHelpQuestion.setId(helpQuestion.getId());

        partialUpdatedHelpQuestion.content(UPDATED_CONTENT);

        restHelpQuestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHelpQuestion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHelpQuestion))
            )
            .andExpect(status().isOk());

        // Validate the HelpQuestion in the database
        List<HelpQuestion> helpQuestionList = helpQuestionRepository.findAll();
        assertThat(helpQuestionList).hasSize(databaseSizeBeforeUpdate);
        HelpQuestion testHelpQuestion = helpQuestionList.get(helpQuestionList.size() - 1);
        assertThat(testHelpQuestion.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testHelpQuestion.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testHelpQuestion.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    @Transactional
    void fullUpdateHelpQuestionWithPatch() throws Exception {
        // Initialize the database
        helpQuestionRepository.saveAndFlush(helpQuestion);

        int databaseSizeBeforeUpdate = helpQuestionRepository.findAll().size();

        // Update the helpQuestion using partial update
        HelpQuestion partialUpdatedHelpQuestion = new HelpQuestion();
        partialUpdatedHelpQuestion.setId(helpQuestion.getId());

        partialUpdatedHelpQuestion.title(UPDATED_TITLE).content(UPDATED_CONTENT).isDeleted(UPDATED_IS_DELETED);

        restHelpQuestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHelpQuestion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHelpQuestion))
            )
            .andExpect(status().isOk());

        // Validate the HelpQuestion in the database
        List<HelpQuestion> helpQuestionList = helpQuestionRepository.findAll();
        assertThat(helpQuestionList).hasSize(databaseSizeBeforeUpdate);
        HelpQuestion testHelpQuestion = helpQuestionList.get(helpQuestionList.size() - 1);
        assertThat(testHelpQuestion.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testHelpQuestion.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testHelpQuestion.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void patchNonExistingHelpQuestion() throws Exception {
        int databaseSizeBeforeUpdate = helpQuestionRepository.findAll().size();
        helpQuestion.setId(longCount.incrementAndGet());

        // Create the HelpQuestion
        HelpQuestionDTO helpQuestionDTO = helpQuestionMapper.toDto(helpQuestion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHelpQuestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, helpQuestionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(helpQuestionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HelpQuestion in the database
        List<HelpQuestion> helpQuestionList = helpQuestionRepository.findAll();
        assertThat(helpQuestionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHelpQuestion() throws Exception {
        int databaseSizeBeforeUpdate = helpQuestionRepository.findAll().size();
        helpQuestion.setId(longCount.incrementAndGet());

        // Create the HelpQuestion
        HelpQuestionDTO helpQuestionDTO = helpQuestionMapper.toDto(helpQuestion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHelpQuestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(helpQuestionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HelpQuestion in the database
        List<HelpQuestion> helpQuestionList = helpQuestionRepository.findAll();
        assertThat(helpQuestionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHelpQuestion() throws Exception {
        int databaseSizeBeforeUpdate = helpQuestionRepository.findAll().size();
        helpQuestion.setId(longCount.incrementAndGet());

        // Create the HelpQuestion
        HelpQuestionDTO helpQuestionDTO = helpQuestionMapper.toDto(helpQuestion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHelpQuestionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(helpQuestionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HelpQuestion in the database
        List<HelpQuestion> helpQuestionList = helpQuestionRepository.findAll();
        assertThat(helpQuestionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHelpQuestion() throws Exception {
        // Initialize the database
        helpQuestionRepository.saveAndFlush(helpQuestion);

        int databaseSizeBeforeDelete = helpQuestionRepository.findAll().size();

        // Delete the helpQuestion
        restHelpQuestionMockMvc
            .perform(delete(ENTITY_API_URL_ID, helpQuestion.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<HelpQuestion> helpQuestionList = helpQuestionRepository.findAll();
        assertThat(helpQuestionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
