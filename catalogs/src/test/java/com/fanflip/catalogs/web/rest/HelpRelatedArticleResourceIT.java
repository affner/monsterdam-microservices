package com.fanflip.catalogs.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fanflip.catalogs.IntegrationTest;
import com.fanflip.catalogs.domain.HelpRelatedArticle;
import com.fanflip.catalogs.repository.HelpRelatedArticleRepository;
import com.fanflip.catalogs.service.dto.HelpRelatedArticleDTO;
import com.fanflip.catalogs.service.mapper.HelpRelatedArticleMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link HelpRelatedArticleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HelpRelatedArticleResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/help-related-articles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private HelpRelatedArticleRepository helpRelatedArticleRepository;

    @Autowired
    private HelpRelatedArticleMapper helpRelatedArticleMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHelpRelatedArticleMockMvc;

    private HelpRelatedArticle helpRelatedArticle;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HelpRelatedArticle createEntity(EntityManager em) {
        HelpRelatedArticle helpRelatedArticle = new HelpRelatedArticle().title(DEFAULT_TITLE).content(DEFAULT_CONTENT);
        return helpRelatedArticle;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HelpRelatedArticle createUpdatedEntity(EntityManager em) {
        HelpRelatedArticle helpRelatedArticle = new HelpRelatedArticle().title(UPDATED_TITLE).content(UPDATED_CONTENT);
        return helpRelatedArticle;
    }

    @BeforeEach
    public void initTest() {
        helpRelatedArticle = createEntity(em);
    }

    @Test
    @Transactional
    void createHelpRelatedArticle() throws Exception {
        int databaseSizeBeforeCreate = helpRelatedArticleRepository.findAll().size();
        // Create the HelpRelatedArticle
        HelpRelatedArticleDTO helpRelatedArticleDTO = helpRelatedArticleMapper.toDto(helpRelatedArticle);
        restHelpRelatedArticleMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(helpRelatedArticleDTO))
            )
            .andExpect(status().isCreated());

        // Validate the HelpRelatedArticle in the database
        List<HelpRelatedArticle> helpRelatedArticleList = helpRelatedArticleRepository.findAll();
        assertThat(helpRelatedArticleList).hasSize(databaseSizeBeforeCreate + 1);
        HelpRelatedArticle testHelpRelatedArticle = helpRelatedArticleList.get(helpRelatedArticleList.size() - 1);
        assertThat(testHelpRelatedArticle.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testHelpRelatedArticle.getContent()).isEqualTo(DEFAULT_CONTENT);
    }

    @Test
    @Transactional
    void createHelpRelatedArticleWithExistingId() throws Exception {
        // Create the HelpRelatedArticle with an existing ID
        helpRelatedArticle.setId(1L);
        HelpRelatedArticleDTO helpRelatedArticleDTO = helpRelatedArticleMapper.toDto(helpRelatedArticle);

        int databaseSizeBeforeCreate = helpRelatedArticleRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHelpRelatedArticleMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(helpRelatedArticleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HelpRelatedArticle in the database
        List<HelpRelatedArticle> helpRelatedArticleList = helpRelatedArticleRepository.findAll();
        assertThat(helpRelatedArticleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = helpRelatedArticleRepository.findAll().size();
        // set the field null
        helpRelatedArticle.setTitle(null);

        // Create the HelpRelatedArticle, which fails.
        HelpRelatedArticleDTO helpRelatedArticleDTO = helpRelatedArticleMapper.toDto(helpRelatedArticle);

        restHelpRelatedArticleMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(helpRelatedArticleDTO))
            )
            .andExpect(status().isBadRequest());

        List<HelpRelatedArticle> helpRelatedArticleList = helpRelatedArticleRepository.findAll();
        assertThat(helpRelatedArticleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllHelpRelatedArticles() throws Exception {
        // Initialize the database
        helpRelatedArticleRepository.saveAndFlush(helpRelatedArticle);

        // Get all the helpRelatedArticleList
        restHelpRelatedArticleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(helpRelatedArticle.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())));
    }

    @Test
    @Transactional
    void getHelpRelatedArticle() throws Exception {
        // Initialize the database
        helpRelatedArticleRepository.saveAndFlush(helpRelatedArticle);

        // Get the helpRelatedArticle
        restHelpRelatedArticleMockMvc
            .perform(get(ENTITY_API_URL_ID, helpRelatedArticle.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(helpRelatedArticle.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingHelpRelatedArticle() throws Exception {
        // Get the helpRelatedArticle
        restHelpRelatedArticleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingHelpRelatedArticle() throws Exception {
        // Initialize the database
        helpRelatedArticleRepository.saveAndFlush(helpRelatedArticle);

        int databaseSizeBeforeUpdate = helpRelatedArticleRepository.findAll().size();

        // Update the helpRelatedArticle
        HelpRelatedArticle updatedHelpRelatedArticle = helpRelatedArticleRepository.findById(helpRelatedArticle.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedHelpRelatedArticle are not directly saved in db
        em.detach(updatedHelpRelatedArticle);
        updatedHelpRelatedArticle.title(UPDATED_TITLE).content(UPDATED_CONTENT);
        HelpRelatedArticleDTO helpRelatedArticleDTO = helpRelatedArticleMapper.toDto(updatedHelpRelatedArticle);

        restHelpRelatedArticleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, helpRelatedArticleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(helpRelatedArticleDTO))
            )
            .andExpect(status().isOk());

        // Validate the HelpRelatedArticle in the database
        List<HelpRelatedArticle> helpRelatedArticleList = helpRelatedArticleRepository.findAll();
        assertThat(helpRelatedArticleList).hasSize(databaseSizeBeforeUpdate);
        HelpRelatedArticle testHelpRelatedArticle = helpRelatedArticleList.get(helpRelatedArticleList.size() - 1);
        assertThat(testHelpRelatedArticle.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testHelpRelatedArticle.getContent()).isEqualTo(UPDATED_CONTENT);
    }

    @Test
    @Transactional
    void putNonExistingHelpRelatedArticle() throws Exception {
        int databaseSizeBeforeUpdate = helpRelatedArticleRepository.findAll().size();
        helpRelatedArticle.setId(longCount.incrementAndGet());

        // Create the HelpRelatedArticle
        HelpRelatedArticleDTO helpRelatedArticleDTO = helpRelatedArticleMapper.toDto(helpRelatedArticle);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHelpRelatedArticleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, helpRelatedArticleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(helpRelatedArticleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HelpRelatedArticle in the database
        List<HelpRelatedArticle> helpRelatedArticleList = helpRelatedArticleRepository.findAll();
        assertThat(helpRelatedArticleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHelpRelatedArticle() throws Exception {
        int databaseSizeBeforeUpdate = helpRelatedArticleRepository.findAll().size();
        helpRelatedArticle.setId(longCount.incrementAndGet());

        // Create the HelpRelatedArticle
        HelpRelatedArticleDTO helpRelatedArticleDTO = helpRelatedArticleMapper.toDto(helpRelatedArticle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHelpRelatedArticleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(helpRelatedArticleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HelpRelatedArticle in the database
        List<HelpRelatedArticle> helpRelatedArticleList = helpRelatedArticleRepository.findAll();
        assertThat(helpRelatedArticleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHelpRelatedArticle() throws Exception {
        int databaseSizeBeforeUpdate = helpRelatedArticleRepository.findAll().size();
        helpRelatedArticle.setId(longCount.incrementAndGet());

        // Create the HelpRelatedArticle
        HelpRelatedArticleDTO helpRelatedArticleDTO = helpRelatedArticleMapper.toDto(helpRelatedArticle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHelpRelatedArticleMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(helpRelatedArticleDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HelpRelatedArticle in the database
        List<HelpRelatedArticle> helpRelatedArticleList = helpRelatedArticleRepository.findAll();
        assertThat(helpRelatedArticleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHelpRelatedArticleWithPatch() throws Exception {
        // Initialize the database
        helpRelatedArticleRepository.saveAndFlush(helpRelatedArticle);

        int databaseSizeBeforeUpdate = helpRelatedArticleRepository.findAll().size();

        // Update the helpRelatedArticle using partial update
        HelpRelatedArticle partialUpdatedHelpRelatedArticle = new HelpRelatedArticle();
        partialUpdatedHelpRelatedArticle.setId(helpRelatedArticle.getId());

        restHelpRelatedArticleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHelpRelatedArticle.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHelpRelatedArticle))
            )
            .andExpect(status().isOk());

        // Validate the HelpRelatedArticle in the database
        List<HelpRelatedArticle> helpRelatedArticleList = helpRelatedArticleRepository.findAll();
        assertThat(helpRelatedArticleList).hasSize(databaseSizeBeforeUpdate);
        HelpRelatedArticle testHelpRelatedArticle = helpRelatedArticleList.get(helpRelatedArticleList.size() - 1);
        assertThat(testHelpRelatedArticle.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testHelpRelatedArticle.getContent()).isEqualTo(DEFAULT_CONTENT);
    }

    @Test
    @Transactional
    void fullUpdateHelpRelatedArticleWithPatch() throws Exception {
        // Initialize the database
        helpRelatedArticleRepository.saveAndFlush(helpRelatedArticle);

        int databaseSizeBeforeUpdate = helpRelatedArticleRepository.findAll().size();

        // Update the helpRelatedArticle using partial update
        HelpRelatedArticle partialUpdatedHelpRelatedArticle = new HelpRelatedArticle();
        partialUpdatedHelpRelatedArticle.setId(helpRelatedArticle.getId());

        partialUpdatedHelpRelatedArticle.title(UPDATED_TITLE).content(UPDATED_CONTENT);

        restHelpRelatedArticleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHelpRelatedArticle.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHelpRelatedArticle))
            )
            .andExpect(status().isOk());

        // Validate the HelpRelatedArticle in the database
        List<HelpRelatedArticle> helpRelatedArticleList = helpRelatedArticleRepository.findAll();
        assertThat(helpRelatedArticleList).hasSize(databaseSizeBeforeUpdate);
        HelpRelatedArticle testHelpRelatedArticle = helpRelatedArticleList.get(helpRelatedArticleList.size() - 1);
        assertThat(testHelpRelatedArticle.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testHelpRelatedArticle.getContent()).isEqualTo(UPDATED_CONTENT);
    }

    @Test
    @Transactional
    void patchNonExistingHelpRelatedArticle() throws Exception {
        int databaseSizeBeforeUpdate = helpRelatedArticleRepository.findAll().size();
        helpRelatedArticle.setId(longCount.incrementAndGet());

        // Create the HelpRelatedArticle
        HelpRelatedArticleDTO helpRelatedArticleDTO = helpRelatedArticleMapper.toDto(helpRelatedArticle);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHelpRelatedArticleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, helpRelatedArticleDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(helpRelatedArticleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HelpRelatedArticle in the database
        List<HelpRelatedArticle> helpRelatedArticleList = helpRelatedArticleRepository.findAll();
        assertThat(helpRelatedArticleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHelpRelatedArticle() throws Exception {
        int databaseSizeBeforeUpdate = helpRelatedArticleRepository.findAll().size();
        helpRelatedArticle.setId(longCount.incrementAndGet());

        // Create the HelpRelatedArticle
        HelpRelatedArticleDTO helpRelatedArticleDTO = helpRelatedArticleMapper.toDto(helpRelatedArticle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHelpRelatedArticleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(helpRelatedArticleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HelpRelatedArticle in the database
        List<HelpRelatedArticle> helpRelatedArticleList = helpRelatedArticleRepository.findAll();
        assertThat(helpRelatedArticleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHelpRelatedArticle() throws Exception {
        int databaseSizeBeforeUpdate = helpRelatedArticleRepository.findAll().size();
        helpRelatedArticle.setId(longCount.incrementAndGet());

        // Create the HelpRelatedArticle
        HelpRelatedArticleDTO helpRelatedArticleDTO = helpRelatedArticleMapper.toDto(helpRelatedArticle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHelpRelatedArticleMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(helpRelatedArticleDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HelpRelatedArticle in the database
        List<HelpRelatedArticle> helpRelatedArticleList = helpRelatedArticleRepository.findAll();
        assertThat(helpRelatedArticleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHelpRelatedArticle() throws Exception {
        // Initialize the database
        helpRelatedArticleRepository.saveAndFlush(helpRelatedArticle);

        int databaseSizeBeforeDelete = helpRelatedArticleRepository.findAll().size();

        // Delete the helpRelatedArticle
        restHelpRelatedArticleMockMvc
            .perform(delete(ENTITY_API_URL_ID, helpRelatedArticle.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<HelpRelatedArticle> helpRelatedArticleList = helpRelatedArticleRepository.findAll();
        assertThat(helpRelatedArticleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
