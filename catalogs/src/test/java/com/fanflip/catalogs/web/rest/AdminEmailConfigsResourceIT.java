package com.fanflip.catalogs.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fanflip.catalogs.IntegrationTest;
import com.fanflip.catalogs.domain.AdminEmailConfigs;
import com.fanflip.catalogs.domain.enumeration.EmailTemplateType;
import com.fanflip.catalogs.repository.AdminEmailConfigsRepository;
import com.fanflip.catalogs.service.dto.AdminEmailConfigsDTO;
import com.fanflip.catalogs.service.mapper.AdminEmailConfigsMapper;
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
 * Integration tests for the {@link AdminEmailConfigsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AdminEmailConfigsResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_SUBJECT = "AAAAAAAAAA";
    private static final String UPDATED_SUBJECT = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final EmailTemplateType DEFAULT_MAIL_TEMPLATE_TYPE = EmailTemplateType.NOTIFICATION;
    private static final EmailTemplateType UPDATED_MAIL_TEMPLATE_TYPE = EmailTemplateType.NEWSLETTER;

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/admin-email-configs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AdminEmailConfigsRepository adminEmailConfigsRepository;

    @Autowired
    private AdminEmailConfigsMapper adminEmailConfigsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAdminEmailConfigsMockMvc;

    private AdminEmailConfigs adminEmailConfigs;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AdminEmailConfigs createEntity(EntityManager em) {
        AdminEmailConfigs adminEmailConfigs = new AdminEmailConfigs()
            .title(DEFAULT_TITLE)
            .subject(DEFAULT_SUBJECT)
            .content(DEFAULT_CONTENT)
            .mailTemplateType(DEFAULT_MAIL_TEMPLATE_TYPE)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .isActive(DEFAULT_IS_ACTIVE);
        return adminEmailConfigs;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AdminEmailConfigs createUpdatedEntity(EntityManager em) {
        AdminEmailConfigs adminEmailConfigs = new AdminEmailConfigs()
            .title(UPDATED_TITLE)
            .subject(UPDATED_SUBJECT)
            .content(UPDATED_CONTENT)
            .mailTemplateType(UPDATED_MAIL_TEMPLATE_TYPE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isActive(UPDATED_IS_ACTIVE);
        return adminEmailConfigs;
    }

    @BeforeEach
    public void initTest() {
        adminEmailConfigs = createEntity(em);
    }

    @Test
    @Transactional
    void createAdminEmailConfigs() throws Exception {
        int databaseSizeBeforeCreate = adminEmailConfigsRepository.findAll().size();
        // Create the AdminEmailConfigs
        AdminEmailConfigsDTO adminEmailConfigsDTO = adminEmailConfigsMapper.toDto(adminEmailConfigs);
        restAdminEmailConfigsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(adminEmailConfigsDTO))
            )
            .andExpect(status().isCreated());

        // Validate the AdminEmailConfigs in the database
        List<AdminEmailConfigs> adminEmailConfigsList = adminEmailConfigsRepository.findAll();
        assertThat(adminEmailConfigsList).hasSize(databaseSizeBeforeCreate + 1);
        AdminEmailConfigs testAdminEmailConfigs = adminEmailConfigsList.get(adminEmailConfigsList.size() - 1);
        assertThat(testAdminEmailConfigs.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testAdminEmailConfigs.getSubject()).isEqualTo(DEFAULT_SUBJECT);
        assertThat(testAdminEmailConfigs.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testAdminEmailConfigs.getMailTemplateType()).isEqualTo(DEFAULT_MAIL_TEMPLATE_TYPE);
        assertThat(testAdminEmailConfigs.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testAdminEmailConfigs.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testAdminEmailConfigs.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testAdminEmailConfigs.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testAdminEmailConfigs.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    void createAdminEmailConfigsWithExistingId() throws Exception {
        // Create the AdminEmailConfigs with an existing ID
        adminEmailConfigs.setId(1L);
        AdminEmailConfigsDTO adminEmailConfigsDTO = adminEmailConfigsMapper.toDto(adminEmailConfigs);

        int databaseSizeBeforeCreate = adminEmailConfigsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAdminEmailConfigsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(adminEmailConfigsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AdminEmailConfigs in the database
        List<AdminEmailConfigs> adminEmailConfigsList = adminEmailConfigsRepository.findAll();
        assertThat(adminEmailConfigsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = adminEmailConfigsRepository.findAll().size();
        // set the field null
        adminEmailConfigs.setTitle(null);

        // Create the AdminEmailConfigs, which fails.
        AdminEmailConfigsDTO adminEmailConfigsDTO = adminEmailConfigsMapper.toDto(adminEmailConfigs);

        restAdminEmailConfigsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(adminEmailConfigsDTO))
            )
            .andExpect(status().isBadRequest());

        List<AdminEmailConfigs> adminEmailConfigsList = adminEmailConfigsRepository.findAll();
        assertThat(adminEmailConfigsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSubjectIsRequired() throws Exception {
        int databaseSizeBeforeTest = adminEmailConfigsRepository.findAll().size();
        // set the field null
        adminEmailConfigs.setSubject(null);

        // Create the AdminEmailConfigs, which fails.
        AdminEmailConfigsDTO adminEmailConfigsDTO = adminEmailConfigsMapper.toDto(adminEmailConfigs);

        restAdminEmailConfigsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(adminEmailConfigsDTO))
            )
            .andExpect(status().isBadRequest());

        List<AdminEmailConfigs> adminEmailConfigsList = adminEmailConfigsRepository.findAll();
        assertThat(adminEmailConfigsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkContentIsRequired() throws Exception {
        int databaseSizeBeforeTest = adminEmailConfigsRepository.findAll().size();
        // set the field null
        adminEmailConfigs.setContent(null);

        // Create the AdminEmailConfigs, which fails.
        AdminEmailConfigsDTO adminEmailConfigsDTO = adminEmailConfigsMapper.toDto(adminEmailConfigs);

        restAdminEmailConfigsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(adminEmailConfigsDTO))
            )
            .andExpect(status().isBadRequest());

        List<AdminEmailConfigs> adminEmailConfigsList = adminEmailConfigsRepository.findAll();
        assertThat(adminEmailConfigsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMailTemplateTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = adminEmailConfigsRepository.findAll().size();
        // set the field null
        adminEmailConfigs.setMailTemplateType(null);

        // Create the AdminEmailConfigs, which fails.
        AdminEmailConfigsDTO adminEmailConfigsDTO = adminEmailConfigsMapper.toDto(adminEmailConfigs);

        restAdminEmailConfigsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(adminEmailConfigsDTO))
            )
            .andExpect(status().isBadRequest());

        List<AdminEmailConfigs> adminEmailConfigsList = adminEmailConfigsRepository.findAll();
        assertThat(adminEmailConfigsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = adminEmailConfigsRepository.findAll().size();
        // set the field null
        adminEmailConfigs.setCreatedDate(null);

        // Create the AdminEmailConfigs, which fails.
        AdminEmailConfigsDTO adminEmailConfigsDTO = adminEmailConfigsMapper.toDto(adminEmailConfigs);

        restAdminEmailConfigsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(adminEmailConfigsDTO))
            )
            .andExpect(status().isBadRequest());

        List<AdminEmailConfigs> adminEmailConfigsList = adminEmailConfigsRepository.findAll();
        assertThat(adminEmailConfigsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = adminEmailConfigsRepository.findAll().size();
        // set the field null
        adminEmailConfigs.setIsActive(null);

        // Create the AdminEmailConfigs, which fails.
        AdminEmailConfigsDTO adminEmailConfigsDTO = adminEmailConfigsMapper.toDto(adminEmailConfigs);

        restAdminEmailConfigsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(adminEmailConfigsDTO))
            )
            .andExpect(status().isBadRequest());

        List<AdminEmailConfigs> adminEmailConfigsList = adminEmailConfigsRepository.findAll();
        assertThat(adminEmailConfigsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAdminEmailConfigs() throws Exception {
        // Initialize the database
        adminEmailConfigsRepository.saveAndFlush(adminEmailConfigs);

        // Get all the adminEmailConfigsList
        restAdminEmailConfigsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(adminEmailConfigs.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].subject").value(hasItem(DEFAULT_SUBJECT)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].mailTemplateType").value(hasItem(DEFAULT_MAIL_TEMPLATE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    void getAdminEmailConfigs() throws Exception {
        // Initialize the database
        adminEmailConfigsRepository.saveAndFlush(adminEmailConfigs);

        // Get the adminEmailConfigs
        restAdminEmailConfigsMockMvc
            .perform(get(ENTITY_API_URL_ID, adminEmailConfigs.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(adminEmailConfigs.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.subject").value(DEFAULT_SUBJECT))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT))
            .andExpect(jsonPath("$.mailTemplateType").value(DEFAULT_MAIL_TEMPLATE_TYPE.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingAdminEmailConfigs() throws Exception {
        // Get the adminEmailConfigs
        restAdminEmailConfigsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAdminEmailConfigs() throws Exception {
        // Initialize the database
        adminEmailConfigsRepository.saveAndFlush(adminEmailConfigs);

        int databaseSizeBeforeUpdate = adminEmailConfigsRepository.findAll().size();

        // Update the adminEmailConfigs
        AdminEmailConfigs updatedAdminEmailConfigs = adminEmailConfigsRepository.findById(adminEmailConfigs.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAdminEmailConfigs are not directly saved in db
        em.detach(updatedAdminEmailConfigs);
        updatedAdminEmailConfigs
            .title(UPDATED_TITLE)
            .subject(UPDATED_SUBJECT)
            .content(UPDATED_CONTENT)
            .mailTemplateType(UPDATED_MAIL_TEMPLATE_TYPE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isActive(UPDATED_IS_ACTIVE);
        AdminEmailConfigsDTO adminEmailConfigsDTO = adminEmailConfigsMapper.toDto(updatedAdminEmailConfigs);

        restAdminEmailConfigsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, adminEmailConfigsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(adminEmailConfigsDTO))
            )
            .andExpect(status().isOk());

        // Validate the AdminEmailConfigs in the database
        List<AdminEmailConfigs> adminEmailConfigsList = adminEmailConfigsRepository.findAll();
        assertThat(adminEmailConfigsList).hasSize(databaseSizeBeforeUpdate);
        AdminEmailConfigs testAdminEmailConfigs = adminEmailConfigsList.get(adminEmailConfigsList.size() - 1);
        assertThat(testAdminEmailConfigs.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testAdminEmailConfigs.getSubject()).isEqualTo(UPDATED_SUBJECT);
        assertThat(testAdminEmailConfigs.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testAdminEmailConfigs.getMailTemplateType()).isEqualTo(UPDATED_MAIL_TEMPLATE_TYPE);
        assertThat(testAdminEmailConfigs.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testAdminEmailConfigs.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testAdminEmailConfigs.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testAdminEmailConfigs.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testAdminEmailConfigs.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void putNonExistingAdminEmailConfigs() throws Exception {
        int databaseSizeBeforeUpdate = adminEmailConfigsRepository.findAll().size();
        adminEmailConfigs.setId(longCount.incrementAndGet());

        // Create the AdminEmailConfigs
        AdminEmailConfigsDTO adminEmailConfigsDTO = adminEmailConfigsMapper.toDto(adminEmailConfigs);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAdminEmailConfigsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, adminEmailConfigsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(adminEmailConfigsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AdminEmailConfigs in the database
        List<AdminEmailConfigs> adminEmailConfigsList = adminEmailConfigsRepository.findAll();
        assertThat(adminEmailConfigsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAdminEmailConfigs() throws Exception {
        int databaseSizeBeforeUpdate = adminEmailConfigsRepository.findAll().size();
        adminEmailConfigs.setId(longCount.incrementAndGet());

        // Create the AdminEmailConfigs
        AdminEmailConfigsDTO adminEmailConfigsDTO = adminEmailConfigsMapper.toDto(adminEmailConfigs);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdminEmailConfigsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(adminEmailConfigsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AdminEmailConfigs in the database
        List<AdminEmailConfigs> adminEmailConfigsList = adminEmailConfigsRepository.findAll();
        assertThat(adminEmailConfigsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAdminEmailConfigs() throws Exception {
        int databaseSizeBeforeUpdate = adminEmailConfigsRepository.findAll().size();
        adminEmailConfigs.setId(longCount.incrementAndGet());

        // Create the AdminEmailConfigs
        AdminEmailConfigsDTO adminEmailConfigsDTO = adminEmailConfigsMapper.toDto(adminEmailConfigs);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdminEmailConfigsMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(adminEmailConfigsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AdminEmailConfigs in the database
        List<AdminEmailConfigs> adminEmailConfigsList = adminEmailConfigsRepository.findAll();
        assertThat(adminEmailConfigsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAdminEmailConfigsWithPatch() throws Exception {
        // Initialize the database
        adminEmailConfigsRepository.saveAndFlush(adminEmailConfigs);

        int databaseSizeBeforeUpdate = adminEmailConfigsRepository.findAll().size();

        // Update the adminEmailConfigs using partial update
        AdminEmailConfigs partialUpdatedAdminEmailConfigs = new AdminEmailConfigs();
        partialUpdatedAdminEmailConfigs.setId(adminEmailConfigs.getId());

        partialUpdatedAdminEmailConfigs
            .title(UPDATED_TITLE)
            .subject(UPDATED_SUBJECT)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .isActive(UPDATED_IS_ACTIVE);

        restAdminEmailConfigsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAdminEmailConfigs.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAdminEmailConfigs))
            )
            .andExpect(status().isOk());

        // Validate the AdminEmailConfigs in the database
        List<AdminEmailConfigs> adminEmailConfigsList = adminEmailConfigsRepository.findAll();
        assertThat(adminEmailConfigsList).hasSize(databaseSizeBeforeUpdate);
        AdminEmailConfigs testAdminEmailConfigs = adminEmailConfigsList.get(adminEmailConfigsList.size() - 1);
        assertThat(testAdminEmailConfigs.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testAdminEmailConfigs.getSubject()).isEqualTo(UPDATED_SUBJECT);
        assertThat(testAdminEmailConfigs.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testAdminEmailConfigs.getMailTemplateType()).isEqualTo(DEFAULT_MAIL_TEMPLATE_TYPE);
        assertThat(testAdminEmailConfigs.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testAdminEmailConfigs.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testAdminEmailConfigs.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testAdminEmailConfigs.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testAdminEmailConfigs.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void fullUpdateAdminEmailConfigsWithPatch() throws Exception {
        // Initialize the database
        adminEmailConfigsRepository.saveAndFlush(adminEmailConfigs);

        int databaseSizeBeforeUpdate = adminEmailConfigsRepository.findAll().size();

        // Update the adminEmailConfigs using partial update
        AdminEmailConfigs partialUpdatedAdminEmailConfigs = new AdminEmailConfigs();
        partialUpdatedAdminEmailConfigs.setId(adminEmailConfigs.getId());

        partialUpdatedAdminEmailConfigs
            .title(UPDATED_TITLE)
            .subject(UPDATED_SUBJECT)
            .content(UPDATED_CONTENT)
            .mailTemplateType(UPDATED_MAIL_TEMPLATE_TYPE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isActive(UPDATED_IS_ACTIVE);

        restAdminEmailConfigsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAdminEmailConfigs.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAdminEmailConfigs))
            )
            .andExpect(status().isOk());

        // Validate the AdminEmailConfigs in the database
        List<AdminEmailConfigs> adminEmailConfigsList = adminEmailConfigsRepository.findAll();
        assertThat(adminEmailConfigsList).hasSize(databaseSizeBeforeUpdate);
        AdminEmailConfigs testAdminEmailConfigs = adminEmailConfigsList.get(adminEmailConfigsList.size() - 1);
        assertThat(testAdminEmailConfigs.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testAdminEmailConfigs.getSubject()).isEqualTo(UPDATED_SUBJECT);
        assertThat(testAdminEmailConfigs.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testAdminEmailConfigs.getMailTemplateType()).isEqualTo(UPDATED_MAIL_TEMPLATE_TYPE);
        assertThat(testAdminEmailConfigs.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testAdminEmailConfigs.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testAdminEmailConfigs.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testAdminEmailConfigs.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testAdminEmailConfigs.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void patchNonExistingAdminEmailConfigs() throws Exception {
        int databaseSizeBeforeUpdate = adminEmailConfigsRepository.findAll().size();
        adminEmailConfigs.setId(longCount.incrementAndGet());

        // Create the AdminEmailConfigs
        AdminEmailConfigsDTO adminEmailConfigsDTO = adminEmailConfigsMapper.toDto(adminEmailConfigs);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAdminEmailConfigsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, adminEmailConfigsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(adminEmailConfigsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AdminEmailConfigs in the database
        List<AdminEmailConfigs> adminEmailConfigsList = adminEmailConfigsRepository.findAll();
        assertThat(adminEmailConfigsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAdminEmailConfigs() throws Exception {
        int databaseSizeBeforeUpdate = adminEmailConfigsRepository.findAll().size();
        adminEmailConfigs.setId(longCount.incrementAndGet());

        // Create the AdminEmailConfigs
        AdminEmailConfigsDTO adminEmailConfigsDTO = adminEmailConfigsMapper.toDto(adminEmailConfigs);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdminEmailConfigsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(adminEmailConfigsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AdminEmailConfigs in the database
        List<AdminEmailConfigs> adminEmailConfigsList = adminEmailConfigsRepository.findAll();
        assertThat(adminEmailConfigsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAdminEmailConfigs() throws Exception {
        int databaseSizeBeforeUpdate = adminEmailConfigsRepository.findAll().size();
        adminEmailConfigs.setId(longCount.incrementAndGet());

        // Create the AdminEmailConfigs
        AdminEmailConfigsDTO adminEmailConfigsDTO = adminEmailConfigsMapper.toDto(adminEmailConfigs);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdminEmailConfigsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(adminEmailConfigsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AdminEmailConfigs in the database
        List<AdminEmailConfigs> adminEmailConfigsList = adminEmailConfigsRepository.findAll();
        assertThat(adminEmailConfigsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAdminEmailConfigs() throws Exception {
        // Initialize the database
        adminEmailConfigsRepository.saveAndFlush(adminEmailConfigs);

        int databaseSizeBeforeDelete = adminEmailConfigsRepository.findAll().size();

        // Delete the adminEmailConfigs
        restAdminEmailConfigsMockMvc
            .perform(delete(ENTITY_API_URL_ID, adminEmailConfigs.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AdminEmailConfigs> adminEmailConfigsList = adminEmailConfigsRepository.findAll();
        assertThat(adminEmailConfigsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
