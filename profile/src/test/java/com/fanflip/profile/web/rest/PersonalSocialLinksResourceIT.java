package com.monsterdam.profile.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.monsterdam.profile.IntegrationTest;
import com.monsterdam.profile.domain.PersonalSocialLinks;
import com.monsterdam.profile.domain.UserProfile;
import com.monsterdam.profile.repository.PersonalSocialLinksRepository;
import com.monsterdam.profile.service.dto.PersonalSocialLinksDTO;
import com.monsterdam.profile.service.mapper.PersonalSocialLinksMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
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
 * Integration tests for the {@link PersonalSocialLinksResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PersonalSocialLinksResourceIT {

    private static final byte[] DEFAULT_THUMBNAIL = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_THUMBNAIL = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_THUMBNAIL_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_THUMBNAIL_CONTENT_TYPE = "image/png";

    private static final byte[] DEFAULT_NORMAL_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_NORMAL_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_NORMAL_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_NORMAL_IMAGE_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_NORMAL_IMAGE_S_3_KEY = "AAAAAAAAAA";
    private static final String UPDATED_NORMAL_IMAGE_S_3_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_THUMBNAIL_ICON_S_3_KEY = "AAAAAAAAAA";
    private static final String UPDATED_THUMBNAIL_ICON_S_3_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_SOCIAL_LINK = "AAAAAAAAAA";
    private static final String UPDATED_SOCIAL_LINK = "BBBBBBBBBB";

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

    private static final Long DEFAULT_SOCIAL_NETWORK_ID = 1L;
    private static final Long UPDATED_SOCIAL_NETWORK_ID = 2L;

    private static final String ENTITY_API_URL = "/api/personal-social-links";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PersonalSocialLinksRepository personalSocialLinksRepository;

    @Autowired
    private PersonalSocialLinksMapper personalSocialLinksMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPersonalSocialLinksMockMvc;

    private PersonalSocialLinks personalSocialLinks;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PersonalSocialLinks createEntity(EntityManager em) {
        PersonalSocialLinks personalSocialLinks = new PersonalSocialLinks()
            .thumbnail(DEFAULT_THUMBNAIL)
            .thumbnailContentType(DEFAULT_THUMBNAIL_CONTENT_TYPE)
            .normalImage(DEFAULT_NORMAL_IMAGE)
            .normalImageContentType(DEFAULT_NORMAL_IMAGE_CONTENT_TYPE)
            .normalImageS3Key(DEFAULT_NORMAL_IMAGE_S_3_KEY)
            .thumbnailIconS3Key(DEFAULT_THUMBNAIL_ICON_S_3_KEY)
            .socialLink(DEFAULT_SOCIAL_LINK)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .isDeleted(DEFAULT_IS_DELETED)
            .socialNetworkId(DEFAULT_SOCIAL_NETWORK_ID);
        // Add required entity
        UserProfile userProfile;
        if (TestUtil.findAll(em, UserProfile.class).isEmpty()) {
            userProfile = UserProfileResourceIT.createEntity(em);
            em.persist(userProfile);
            em.flush();
        } else {
            userProfile = TestUtil.findAll(em, UserProfile.class).get(0);
        }
        personalSocialLinks.setUser(userProfile);
        return personalSocialLinks;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PersonalSocialLinks createUpdatedEntity(EntityManager em) {
        PersonalSocialLinks personalSocialLinks = new PersonalSocialLinks()
            .thumbnail(UPDATED_THUMBNAIL)
            .thumbnailContentType(UPDATED_THUMBNAIL_CONTENT_TYPE)
            .normalImage(UPDATED_NORMAL_IMAGE)
            .normalImageContentType(UPDATED_NORMAL_IMAGE_CONTENT_TYPE)
            .normalImageS3Key(UPDATED_NORMAL_IMAGE_S_3_KEY)
            .thumbnailIconS3Key(UPDATED_THUMBNAIL_ICON_S_3_KEY)
            .socialLink(UPDATED_SOCIAL_LINK)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .socialNetworkId(UPDATED_SOCIAL_NETWORK_ID);
        // Add required entity
        UserProfile userProfile;
        if (TestUtil.findAll(em, UserProfile.class).isEmpty()) {
            userProfile = UserProfileResourceIT.createUpdatedEntity(em);
            em.persist(userProfile);
            em.flush();
        } else {
            userProfile = TestUtil.findAll(em, UserProfile.class).get(0);
        }
        personalSocialLinks.setUser(userProfile);
        return personalSocialLinks;
    }

    @BeforeEach
    public void initTest() {
        personalSocialLinks = createEntity(em);
    }

    @Test
    @Transactional
    void createPersonalSocialLinks() throws Exception {
        int databaseSizeBeforeCreate = personalSocialLinksRepository.findAll().size();
        // Create the PersonalSocialLinks
        PersonalSocialLinksDTO personalSocialLinksDTO = personalSocialLinksMapper.toDto(personalSocialLinks);
        restPersonalSocialLinksMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personalSocialLinksDTO))
            )
            .andExpect(status().isCreated());

        // Validate the PersonalSocialLinks in the database
        List<PersonalSocialLinks> personalSocialLinksList = personalSocialLinksRepository.findAll();
        assertThat(personalSocialLinksList).hasSize(databaseSizeBeforeCreate + 1);
        PersonalSocialLinks testPersonalSocialLinks = personalSocialLinksList.get(personalSocialLinksList.size() - 1);
        assertThat(testPersonalSocialLinks.getThumbnail()).isEqualTo(DEFAULT_THUMBNAIL);
        assertThat(testPersonalSocialLinks.getThumbnailContentType()).isEqualTo(DEFAULT_THUMBNAIL_CONTENT_TYPE);
        assertThat(testPersonalSocialLinks.getNormalImage()).isEqualTo(DEFAULT_NORMAL_IMAGE);
        assertThat(testPersonalSocialLinks.getNormalImageContentType()).isEqualTo(DEFAULT_NORMAL_IMAGE_CONTENT_TYPE);
        assertThat(testPersonalSocialLinks.getNormalImageS3Key()).isEqualTo(DEFAULT_NORMAL_IMAGE_S_3_KEY);
        assertThat(testPersonalSocialLinks.getThumbnailIconS3Key()).isEqualTo(DEFAULT_THUMBNAIL_ICON_S_3_KEY);
        assertThat(testPersonalSocialLinks.getSocialLink()).isEqualTo(DEFAULT_SOCIAL_LINK);
        assertThat(testPersonalSocialLinks.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testPersonalSocialLinks.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testPersonalSocialLinks.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testPersonalSocialLinks.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testPersonalSocialLinks.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
        assertThat(testPersonalSocialLinks.getSocialNetworkId()).isEqualTo(DEFAULT_SOCIAL_NETWORK_ID);
    }

    @Test
    @Transactional
    void createPersonalSocialLinksWithExistingId() throws Exception {
        // Create the PersonalSocialLinks with an existing ID
        personalSocialLinks.setId(1L);
        PersonalSocialLinksDTO personalSocialLinksDTO = personalSocialLinksMapper.toDto(personalSocialLinks);

        int databaseSizeBeforeCreate = personalSocialLinksRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPersonalSocialLinksMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personalSocialLinksDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonalSocialLinks in the database
        List<PersonalSocialLinks> personalSocialLinksList = personalSocialLinksRepository.findAll();
        assertThat(personalSocialLinksList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSocialLinkIsRequired() throws Exception {
        int databaseSizeBeforeTest = personalSocialLinksRepository.findAll().size();
        // set the field null
        personalSocialLinks.setSocialLink(null);

        // Create the PersonalSocialLinks, which fails.
        PersonalSocialLinksDTO personalSocialLinksDTO = personalSocialLinksMapper.toDto(personalSocialLinks);

        restPersonalSocialLinksMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personalSocialLinksDTO))
            )
            .andExpect(status().isBadRequest());

        List<PersonalSocialLinks> personalSocialLinksList = personalSocialLinksRepository.findAll();
        assertThat(personalSocialLinksList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = personalSocialLinksRepository.findAll().size();
        // set the field null
        personalSocialLinks.setCreatedDate(null);

        // Create the PersonalSocialLinks, which fails.
        PersonalSocialLinksDTO personalSocialLinksDTO = personalSocialLinksMapper.toDto(personalSocialLinks);

        restPersonalSocialLinksMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personalSocialLinksDTO))
            )
            .andExpect(status().isBadRequest());

        List<PersonalSocialLinks> personalSocialLinksList = personalSocialLinksRepository.findAll();
        assertThat(personalSocialLinksList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = personalSocialLinksRepository.findAll().size();
        // set the field null
        personalSocialLinks.setIsDeleted(null);

        // Create the PersonalSocialLinks, which fails.
        PersonalSocialLinksDTO personalSocialLinksDTO = personalSocialLinksMapper.toDto(personalSocialLinks);

        restPersonalSocialLinksMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personalSocialLinksDTO))
            )
            .andExpect(status().isBadRequest());

        List<PersonalSocialLinks> personalSocialLinksList = personalSocialLinksRepository.findAll();
        assertThat(personalSocialLinksList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPersonalSocialLinks() throws Exception {
        // Initialize the database
        personalSocialLinksRepository.saveAndFlush(personalSocialLinks);

        // Get all the personalSocialLinksList
        restPersonalSocialLinksMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(personalSocialLinks.getId().intValue())))
            .andExpect(jsonPath("$.[*].thumbnailContentType").value(hasItem(DEFAULT_THUMBNAIL_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].thumbnail").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_THUMBNAIL))))
            .andExpect(jsonPath("$.[*].normalImageContentType").value(hasItem(DEFAULT_NORMAL_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].normalImage").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_NORMAL_IMAGE))))
            .andExpect(jsonPath("$.[*].normalImageS3Key").value(hasItem(DEFAULT_NORMAL_IMAGE_S_3_KEY)))
            .andExpect(jsonPath("$.[*].thumbnailIconS3Key").value(hasItem(DEFAULT_THUMBNAIL_ICON_S_3_KEY)))
            .andExpect(jsonPath("$.[*].socialLink").value(hasItem(DEFAULT_SOCIAL_LINK)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())))
            .andExpect(jsonPath("$.[*].socialNetworkId").value(hasItem(DEFAULT_SOCIAL_NETWORK_ID.intValue())));
    }

    @Test
    @Transactional
    void getPersonalSocialLinks() throws Exception {
        // Initialize the database
        personalSocialLinksRepository.saveAndFlush(personalSocialLinks);

        // Get the personalSocialLinks
        restPersonalSocialLinksMockMvc
            .perform(get(ENTITY_API_URL_ID, personalSocialLinks.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(personalSocialLinks.getId().intValue()))
            .andExpect(jsonPath("$.thumbnailContentType").value(DEFAULT_THUMBNAIL_CONTENT_TYPE))
            .andExpect(jsonPath("$.thumbnail").value(Base64.getEncoder().encodeToString(DEFAULT_THUMBNAIL)))
            .andExpect(jsonPath("$.normalImageContentType").value(DEFAULT_NORMAL_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.normalImage").value(Base64.getEncoder().encodeToString(DEFAULT_NORMAL_IMAGE)))
            .andExpect(jsonPath("$.normalImageS3Key").value(DEFAULT_NORMAL_IMAGE_S_3_KEY))
            .andExpect(jsonPath("$.thumbnailIconS3Key").value(DEFAULT_THUMBNAIL_ICON_S_3_KEY))
            .andExpect(jsonPath("$.socialLink").value(DEFAULT_SOCIAL_LINK))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()))
            .andExpect(jsonPath("$.socialNetworkId").value(DEFAULT_SOCIAL_NETWORK_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingPersonalSocialLinks() throws Exception {
        // Get the personalSocialLinks
        restPersonalSocialLinksMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPersonalSocialLinks() throws Exception {
        // Initialize the database
        personalSocialLinksRepository.saveAndFlush(personalSocialLinks);

        int databaseSizeBeforeUpdate = personalSocialLinksRepository.findAll().size();

        // Update the personalSocialLinks
        PersonalSocialLinks updatedPersonalSocialLinks = personalSocialLinksRepository.findById(personalSocialLinks.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPersonalSocialLinks are not directly saved in db
        em.detach(updatedPersonalSocialLinks);
        updatedPersonalSocialLinks
            .thumbnail(UPDATED_THUMBNAIL)
            .thumbnailContentType(UPDATED_THUMBNAIL_CONTENT_TYPE)
            .normalImage(UPDATED_NORMAL_IMAGE)
            .normalImageContentType(UPDATED_NORMAL_IMAGE_CONTENT_TYPE)
            .normalImageS3Key(UPDATED_NORMAL_IMAGE_S_3_KEY)
            .thumbnailIconS3Key(UPDATED_THUMBNAIL_ICON_S_3_KEY)
            .socialLink(UPDATED_SOCIAL_LINK)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .socialNetworkId(UPDATED_SOCIAL_NETWORK_ID);
        PersonalSocialLinksDTO personalSocialLinksDTO = personalSocialLinksMapper.toDto(updatedPersonalSocialLinks);

        restPersonalSocialLinksMockMvc
            .perform(
                put(ENTITY_API_URL_ID, personalSocialLinksDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personalSocialLinksDTO))
            )
            .andExpect(status().isOk());

        // Validate the PersonalSocialLinks in the database
        List<PersonalSocialLinks> personalSocialLinksList = personalSocialLinksRepository.findAll();
        assertThat(personalSocialLinksList).hasSize(databaseSizeBeforeUpdate);
        PersonalSocialLinks testPersonalSocialLinks = personalSocialLinksList.get(personalSocialLinksList.size() - 1);
        assertThat(testPersonalSocialLinks.getThumbnail()).isEqualTo(UPDATED_THUMBNAIL);
        assertThat(testPersonalSocialLinks.getThumbnailContentType()).isEqualTo(UPDATED_THUMBNAIL_CONTENT_TYPE);
        assertThat(testPersonalSocialLinks.getNormalImage()).isEqualTo(UPDATED_NORMAL_IMAGE);
        assertThat(testPersonalSocialLinks.getNormalImageContentType()).isEqualTo(UPDATED_NORMAL_IMAGE_CONTENT_TYPE);
        assertThat(testPersonalSocialLinks.getNormalImageS3Key()).isEqualTo(UPDATED_NORMAL_IMAGE_S_3_KEY);
        assertThat(testPersonalSocialLinks.getThumbnailIconS3Key()).isEqualTo(UPDATED_THUMBNAIL_ICON_S_3_KEY);
        assertThat(testPersonalSocialLinks.getSocialLink()).isEqualTo(UPDATED_SOCIAL_LINK);
        assertThat(testPersonalSocialLinks.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testPersonalSocialLinks.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testPersonalSocialLinks.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testPersonalSocialLinks.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testPersonalSocialLinks.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testPersonalSocialLinks.getSocialNetworkId()).isEqualTo(UPDATED_SOCIAL_NETWORK_ID);
    }

    @Test
    @Transactional
    void putNonExistingPersonalSocialLinks() throws Exception {
        int databaseSizeBeforeUpdate = personalSocialLinksRepository.findAll().size();
        personalSocialLinks.setId(longCount.incrementAndGet());

        // Create the PersonalSocialLinks
        PersonalSocialLinksDTO personalSocialLinksDTO = personalSocialLinksMapper.toDto(personalSocialLinks);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonalSocialLinksMockMvc
            .perform(
                put(ENTITY_API_URL_ID, personalSocialLinksDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personalSocialLinksDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonalSocialLinks in the database
        List<PersonalSocialLinks> personalSocialLinksList = personalSocialLinksRepository.findAll();
        assertThat(personalSocialLinksList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPersonalSocialLinks() throws Exception {
        int databaseSizeBeforeUpdate = personalSocialLinksRepository.findAll().size();
        personalSocialLinks.setId(longCount.incrementAndGet());

        // Create the PersonalSocialLinks
        PersonalSocialLinksDTO personalSocialLinksDTO = personalSocialLinksMapper.toDto(personalSocialLinks);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonalSocialLinksMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personalSocialLinksDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonalSocialLinks in the database
        List<PersonalSocialLinks> personalSocialLinksList = personalSocialLinksRepository.findAll();
        assertThat(personalSocialLinksList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPersonalSocialLinks() throws Exception {
        int databaseSizeBeforeUpdate = personalSocialLinksRepository.findAll().size();
        personalSocialLinks.setId(longCount.incrementAndGet());

        // Create the PersonalSocialLinks
        PersonalSocialLinksDTO personalSocialLinksDTO = personalSocialLinksMapper.toDto(personalSocialLinks);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonalSocialLinksMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personalSocialLinksDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PersonalSocialLinks in the database
        List<PersonalSocialLinks> personalSocialLinksList = personalSocialLinksRepository.findAll();
        assertThat(personalSocialLinksList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePersonalSocialLinksWithPatch() throws Exception {
        // Initialize the database
        personalSocialLinksRepository.saveAndFlush(personalSocialLinks);

        int databaseSizeBeforeUpdate = personalSocialLinksRepository.findAll().size();

        // Update the personalSocialLinks using partial update
        PersonalSocialLinks partialUpdatedPersonalSocialLinks = new PersonalSocialLinks();
        partialUpdatedPersonalSocialLinks.setId(personalSocialLinks.getId());

        partialUpdatedPersonalSocialLinks.lastModifiedDate(UPDATED_LAST_MODIFIED_DATE).lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        restPersonalSocialLinksMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPersonalSocialLinks.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPersonalSocialLinks))
            )
            .andExpect(status().isOk());

        // Validate the PersonalSocialLinks in the database
        List<PersonalSocialLinks> personalSocialLinksList = personalSocialLinksRepository.findAll();
        assertThat(personalSocialLinksList).hasSize(databaseSizeBeforeUpdate);
        PersonalSocialLinks testPersonalSocialLinks = personalSocialLinksList.get(personalSocialLinksList.size() - 1);
        assertThat(testPersonalSocialLinks.getThumbnail()).isEqualTo(DEFAULT_THUMBNAIL);
        assertThat(testPersonalSocialLinks.getThumbnailContentType()).isEqualTo(DEFAULT_THUMBNAIL_CONTENT_TYPE);
        assertThat(testPersonalSocialLinks.getNormalImage()).isEqualTo(DEFAULT_NORMAL_IMAGE);
        assertThat(testPersonalSocialLinks.getNormalImageContentType()).isEqualTo(DEFAULT_NORMAL_IMAGE_CONTENT_TYPE);
        assertThat(testPersonalSocialLinks.getNormalImageS3Key()).isEqualTo(DEFAULT_NORMAL_IMAGE_S_3_KEY);
        assertThat(testPersonalSocialLinks.getThumbnailIconS3Key()).isEqualTo(DEFAULT_THUMBNAIL_ICON_S_3_KEY);
        assertThat(testPersonalSocialLinks.getSocialLink()).isEqualTo(DEFAULT_SOCIAL_LINK);
        assertThat(testPersonalSocialLinks.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testPersonalSocialLinks.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testPersonalSocialLinks.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testPersonalSocialLinks.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testPersonalSocialLinks.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
        assertThat(testPersonalSocialLinks.getSocialNetworkId()).isEqualTo(DEFAULT_SOCIAL_NETWORK_ID);
    }

    @Test
    @Transactional
    void fullUpdatePersonalSocialLinksWithPatch() throws Exception {
        // Initialize the database
        personalSocialLinksRepository.saveAndFlush(personalSocialLinks);

        int databaseSizeBeforeUpdate = personalSocialLinksRepository.findAll().size();

        // Update the personalSocialLinks using partial update
        PersonalSocialLinks partialUpdatedPersonalSocialLinks = new PersonalSocialLinks();
        partialUpdatedPersonalSocialLinks.setId(personalSocialLinks.getId());

        partialUpdatedPersonalSocialLinks
            .thumbnail(UPDATED_THUMBNAIL)
            .thumbnailContentType(UPDATED_THUMBNAIL_CONTENT_TYPE)
            .normalImage(UPDATED_NORMAL_IMAGE)
            .normalImageContentType(UPDATED_NORMAL_IMAGE_CONTENT_TYPE)
            .normalImageS3Key(UPDATED_NORMAL_IMAGE_S_3_KEY)
            .thumbnailIconS3Key(UPDATED_THUMBNAIL_ICON_S_3_KEY)
            .socialLink(UPDATED_SOCIAL_LINK)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .socialNetworkId(UPDATED_SOCIAL_NETWORK_ID);

        restPersonalSocialLinksMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPersonalSocialLinks.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPersonalSocialLinks))
            )
            .andExpect(status().isOk());

        // Validate the PersonalSocialLinks in the database
        List<PersonalSocialLinks> personalSocialLinksList = personalSocialLinksRepository.findAll();
        assertThat(personalSocialLinksList).hasSize(databaseSizeBeforeUpdate);
        PersonalSocialLinks testPersonalSocialLinks = personalSocialLinksList.get(personalSocialLinksList.size() - 1);
        assertThat(testPersonalSocialLinks.getThumbnail()).isEqualTo(UPDATED_THUMBNAIL);
        assertThat(testPersonalSocialLinks.getThumbnailContentType()).isEqualTo(UPDATED_THUMBNAIL_CONTENT_TYPE);
        assertThat(testPersonalSocialLinks.getNormalImage()).isEqualTo(UPDATED_NORMAL_IMAGE);
        assertThat(testPersonalSocialLinks.getNormalImageContentType()).isEqualTo(UPDATED_NORMAL_IMAGE_CONTENT_TYPE);
        assertThat(testPersonalSocialLinks.getNormalImageS3Key()).isEqualTo(UPDATED_NORMAL_IMAGE_S_3_KEY);
        assertThat(testPersonalSocialLinks.getThumbnailIconS3Key()).isEqualTo(UPDATED_THUMBNAIL_ICON_S_3_KEY);
        assertThat(testPersonalSocialLinks.getSocialLink()).isEqualTo(UPDATED_SOCIAL_LINK);
        assertThat(testPersonalSocialLinks.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testPersonalSocialLinks.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testPersonalSocialLinks.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testPersonalSocialLinks.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testPersonalSocialLinks.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testPersonalSocialLinks.getSocialNetworkId()).isEqualTo(UPDATED_SOCIAL_NETWORK_ID);
    }

    @Test
    @Transactional
    void patchNonExistingPersonalSocialLinks() throws Exception {
        int databaseSizeBeforeUpdate = personalSocialLinksRepository.findAll().size();
        personalSocialLinks.setId(longCount.incrementAndGet());

        // Create the PersonalSocialLinks
        PersonalSocialLinksDTO personalSocialLinksDTO = personalSocialLinksMapper.toDto(personalSocialLinks);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonalSocialLinksMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, personalSocialLinksDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(personalSocialLinksDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonalSocialLinks in the database
        List<PersonalSocialLinks> personalSocialLinksList = personalSocialLinksRepository.findAll();
        assertThat(personalSocialLinksList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPersonalSocialLinks() throws Exception {
        int databaseSizeBeforeUpdate = personalSocialLinksRepository.findAll().size();
        personalSocialLinks.setId(longCount.incrementAndGet());

        // Create the PersonalSocialLinks
        PersonalSocialLinksDTO personalSocialLinksDTO = personalSocialLinksMapper.toDto(personalSocialLinks);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonalSocialLinksMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(personalSocialLinksDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonalSocialLinks in the database
        List<PersonalSocialLinks> personalSocialLinksList = personalSocialLinksRepository.findAll();
        assertThat(personalSocialLinksList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPersonalSocialLinks() throws Exception {
        int databaseSizeBeforeUpdate = personalSocialLinksRepository.findAll().size();
        personalSocialLinks.setId(longCount.incrementAndGet());

        // Create the PersonalSocialLinks
        PersonalSocialLinksDTO personalSocialLinksDTO = personalSocialLinksMapper.toDto(personalSocialLinks);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonalSocialLinksMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(personalSocialLinksDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PersonalSocialLinks in the database
        List<PersonalSocialLinks> personalSocialLinksList = personalSocialLinksRepository.findAll();
        assertThat(personalSocialLinksList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePersonalSocialLinks() throws Exception {
        // Initialize the database
        personalSocialLinksRepository.saveAndFlush(personalSocialLinks);

        int databaseSizeBeforeDelete = personalSocialLinksRepository.findAll().size();

        // Delete the personalSocialLinks
        restPersonalSocialLinksMockMvc
            .perform(delete(ENTITY_API_URL_ID, personalSocialLinks.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PersonalSocialLinks> personalSocialLinksList = personalSocialLinksRepository.findAll();
        assertThat(personalSocialLinksList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
