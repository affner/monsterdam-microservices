package com.fanflip.catalogs.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fanflip.catalogs.IntegrationTest;
import com.fanflip.catalogs.domain.SocialNetwork;
import com.fanflip.catalogs.repository.SocialNetworkRepository;
import com.fanflip.catalogs.service.dto.SocialNetworkDTO;
import com.fanflip.catalogs.service.mapper.SocialNetworkMapper;
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
 * Integration tests for the {@link SocialNetworkResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SocialNetworkResourceIT {

    private static final byte[] DEFAULT_THUMBNAIL = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_THUMBNAIL = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_THUMBNAIL_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_THUMBNAIL_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_COMPLETE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COMPLETE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_MAIN_LINK = "AAAAAAAAAA";
    private static final String UPDATED_MAIN_LINK = "BBBBBBBBBB";

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

    private static final String ENTITY_API_URL = "/api/social-networks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SocialNetworkRepository socialNetworkRepository;

    @Autowired
    private SocialNetworkMapper socialNetworkMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSocialNetworkMockMvc;

    private SocialNetwork socialNetwork;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SocialNetwork createEntity(EntityManager em) {
        SocialNetwork socialNetwork = new SocialNetwork()
            .thumbnail(DEFAULT_THUMBNAIL)
            .thumbnailContentType(DEFAULT_THUMBNAIL_CONTENT_TYPE)
            .name(DEFAULT_NAME)
            .completeName(DEFAULT_COMPLETE_NAME)
            .mainLink(DEFAULT_MAIN_LINK)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .isDeleted(DEFAULT_IS_DELETED);
        return socialNetwork;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SocialNetwork createUpdatedEntity(EntityManager em) {
        SocialNetwork socialNetwork = new SocialNetwork()
            .thumbnail(UPDATED_THUMBNAIL)
            .thumbnailContentType(UPDATED_THUMBNAIL_CONTENT_TYPE)
            .name(UPDATED_NAME)
            .completeName(UPDATED_COMPLETE_NAME)
            .mainLink(UPDATED_MAIN_LINK)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        return socialNetwork;
    }

    @BeforeEach
    public void initTest() {
        socialNetwork = createEntity(em);
    }

    @Test
    @Transactional
    void createSocialNetwork() throws Exception {
        int databaseSizeBeforeCreate = socialNetworkRepository.findAll().size();
        // Create the SocialNetwork
        SocialNetworkDTO socialNetworkDTO = socialNetworkMapper.toDto(socialNetwork);
        restSocialNetworkMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(socialNetworkDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SocialNetwork in the database
        List<SocialNetwork> socialNetworkList = socialNetworkRepository.findAll();
        assertThat(socialNetworkList).hasSize(databaseSizeBeforeCreate + 1);
        SocialNetwork testSocialNetwork = socialNetworkList.get(socialNetworkList.size() - 1);
        assertThat(testSocialNetwork.getThumbnail()).isEqualTo(DEFAULT_THUMBNAIL);
        assertThat(testSocialNetwork.getThumbnailContentType()).isEqualTo(DEFAULT_THUMBNAIL_CONTENT_TYPE);
        assertThat(testSocialNetwork.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSocialNetwork.getCompleteName()).isEqualTo(DEFAULT_COMPLETE_NAME);
        assertThat(testSocialNetwork.getMainLink()).isEqualTo(DEFAULT_MAIN_LINK);
        assertThat(testSocialNetwork.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testSocialNetwork.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testSocialNetwork.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testSocialNetwork.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testSocialNetwork.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    @Transactional
    void createSocialNetworkWithExistingId() throws Exception {
        // Create the SocialNetwork with an existing ID
        socialNetwork.setId(1L);
        SocialNetworkDTO socialNetworkDTO = socialNetworkMapper.toDto(socialNetwork);

        int databaseSizeBeforeCreate = socialNetworkRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSocialNetworkMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(socialNetworkDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SocialNetwork in the database
        List<SocialNetwork> socialNetworkList = socialNetworkRepository.findAll();
        assertThat(socialNetworkList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = socialNetworkRepository.findAll().size();
        // set the field null
        socialNetwork.setName(null);

        // Create the SocialNetwork, which fails.
        SocialNetworkDTO socialNetworkDTO = socialNetworkMapper.toDto(socialNetwork);

        restSocialNetworkMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(socialNetworkDTO))
            )
            .andExpect(status().isBadRequest());

        List<SocialNetwork> socialNetworkList = socialNetworkRepository.findAll();
        assertThat(socialNetworkList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCompleteNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = socialNetworkRepository.findAll().size();
        // set the field null
        socialNetwork.setCompleteName(null);

        // Create the SocialNetwork, which fails.
        SocialNetworkDTO socialNetworkDTO = socialNetworkMapper.toDto(socialNetwork);

        restSocialNetworkMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(socialNetworkDTO))
            )
            .andExpect(status().isBadRequest());

        List<SocialNetwork> socialNetworkList = socialNetworkRepository.findAll();
        assertThat(socialNetworkList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMainLinkIsRequired() throws Exception {
        int databaseSizeBeforeTest = socialNetworkRepository.findAll().size();
        // set the field null
        socialNetwork.setMainLink(null);

        // Create the SocialNetwork, which fails.
        SocialNetworkDTO socialNetworkDTO = socialNetworkMapper.toDto(socialNetwork);

        restSocialNetworkMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(socialNetworkDTO))
            )
            .andExpect(status().isBadRequest());

        List<SocialNetwork> socialNetworkList = socialNetworkRepository.findAll();
        assertThat(socialNetworkList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = socialNetworkRepository.findAll().size();
        // set the field null
        socialNetwork.setCreatedDate(null);

        // Create the SocialNetwork, which fails.
        SocialNetworkDTO socialNetworkDTO = socialNetworkMapper.toDto(socialNetwork);

        restSocialNetworkMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(socialNetworkDTO))
            )
            .andExpect(status().isBadRequest());

        List<SocialNetwork> socialNetworkList = socialNetworkRepository.findAll();
        assertThat(socialNetworkList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = socialNetworkRepository.findAll().size();
        // set the field null
        socialNetwork.setIsDeleted(null);

        // Create the SocialNetwork, which fails.
        SocialNetworkDTO socialNetworkDTO = socialNetworkMapper.toDto(socialNetwork);

        restSocialNetworkMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(socialNetworkDTO))
            )
            .andExpect(status().isBadRequest());

        List<SocialNetwork> socialNetworkList = socialNetworkRepository.findAll();
        assertThat(socialNetworkList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSocialNetworks() throws Exception {
        // Initialize the database
        socialNetworkRepository.saveAndFlush(socialNetwork);

        // Get all the socialNetworkList
        restSocialNetworkMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(socialNetwork.getId().intValue())))
            .andExpect(jsonPath("$.[*].thumbnailContentType").value(hasItem(DEFAULT_THUMBNAIL_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].thumbnail").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_THUMBNAIL))))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].completeName").value(hasItem(DEFAULT_COMPLETE_NAME)))
            .andExpect(jsonPath("$.[*].mainLink").value(hasItem(DEFAULT_MAIN_LINK)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())));
    }

    @Test
    @Transactional
    void getSocialNetwork() throws Exception {
        // Initialize the database
        socialNetworkRepository.saveAndFlush(socialNetwork);

        // Get the socialNetwork
        restSocialNetworkMockMvc
            .perform(get(ENTITY_API_URL_ID, socialNetwork.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(socialNetwork.getId().intValue()))
            .andExpect(jsonPath("$.thumbnailContentType").value(DEFAULT_THUMBNAIL_CONTENT_TYPE))
            .andExpect(jsonPath("$.thumbnail").value(Base64.getEncoder().encodeToString(DEFAULT_THUMBNAIL)))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.completeName").value(DEFAULT_COMPLETE_NAME))
            .andExpect(jsonPath("$.mainLink").value(DEFAULT_MAIN_LINK))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingSocialNetwork() throws Exception {
        // Get the socialNetwork
        restSocialNetworkMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSocialNetwork() throws Exception {
        // Initialize the database
        socialNetworkRepository.saveAndFlush(socialNetwork);

        int databaseSizeBeforeUpdate = socialNetworkRepository.findAll().size();

        // Update the socialNetwork
        SocialNetwork updatedSocialNetwork = socialNetworkRepository.findById(socialNetwork.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSocialNetwork are not directly saved in db
        em.detach(updatedSocialNetwork);
        updatedSocialNetwork
            .thumbnail(UPDATED_THUMBNAIL)
            .thumbnailContentType(UPDATED_THUMBNAIL_CONTENT_TYPE)
            .name(UPDATED_NAME)
            .completeName(UPDATED_COMPLETE_NAME)
            .mainLink(UPDATED_MAIN_LINK)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        SocialNetworkDTO socialNetworkDTO = socialNetworkMapper.toDto(updatedSocialNetwork);

        restSocialNetworkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, socialNetworkDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(socialNetworkDTO))
            )
            .andExpect(status().isOk());

        // Validate the SocialNetwork in the database
        List<SocialNetwork> socialNetworkList = socialNetworkRepository.findAll();
        assertThat(socialNetworkList).hasSize(databaseSizeBeforeUpdate);
        SocialNetwork testSocialNetwork = socialNetworkList.get(socialNetworkList.size() - 1);
        assertThat(testSocialNetwork.getThumbnail()).isEqualTo(UPDATED_THUMBNAIL);
        assertThat(testSocialNetwork.getThumbnailContentType()).isEqualTo(UPDATED_THUMBNAIL_CONTENT_TYPE);
        assertThat(testSocialNetwork.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSocialNetwork.getCompleteName()).isEqualTo(UPDATED_COMPLETE_NAME);
        assertThat(testSocialNetwork.getMainLink()).isEqualTo(UPDATED_MAIN_LINK);
        assertThat(testSocialNetwork.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testSocialNetwork.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testSocialNetwork.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testSocialNetwork.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testSocialNetwork.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void putNonExistingSocialNetwork() throws Exception {
        int databaseSizeBeforeUpdate = socialNetworkRepository.findAll().size();
        socialNetwork.setId(longCount.incrementAndGet());

        // Create the SocialNetwork
        SocialNetworkDTO socialNetworkDTO = socialNetworkMapper.toDto(socialNetwork);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSocialNetworkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, socialNetworkDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(socialNetworkDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SocialNetwork in the database
        List<SocialNetwork> socialNetworkList = socialNetworkRepository.findAll();
        assertThat(socialNetworkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSocialNetwork() throws Exception {
        int databaseSizeBeforeUpdate = socialNetworkRepository.findAll().size();
        socialNetwork.setId(longCount.incrementAndGet());

        // Create the SocialNetwork
        SocialNetworkDTO socialNetworkDTO = socialNetworkMapper.toDto(socialNetwork);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSocialNetworkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(socialNetworkDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SocialNetwork in the database
        List<SocialNetwork> socialNetworkList = socialNetworkRepository.findAll();
        assertThat(socialNetworkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSocialNetwork() throws Exception {
        int databaseSizeBeforeUpdate = socialNetworkRepository.findAll().size();
        socialNetwork.setId(longCount.incrementAndGet());

        // Create the SocialNetwork
        SocialNetworkDTO socialNetworkDTO = socialNetworkMapper.toDto(socialNetwork);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSocialNetworkMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(socialNetworkDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SocialNetwork in the database
        List<SocialNetwork> socialNetworkList = socialNetworkRepository.findAll();
        assertThat(socialNetworkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSocialNetworkWithPatch() throws Exception {
        // Initialize the database
        socialNetworkRepository.saveAndFlush(socialNetwork);

        int databaseSizeBeforeUpdate = socialNetworkRepository.findAll().size();

        // Update the socialNetwork using partial update
        SocialNetwork partialUpdatedSocialNetwork = new SocialNetwork();
        partialUpdatedSocialNetwork.setId(socialNetwork.getId());

        partialUpdatedSocialNetwork
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .isDeleted(UPDATED_IS_DELETED);

        restSocialNetworkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSocialNetwork.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSocialNetwork))
            )
            .andExpect(status().isOk());

        // Validate the SocialNetwork in the database
        List<SocialNetwork> socialNetworkList = socialNetworkRepository.findAll();
        assertThat(socialNetworkList).hasSize(databaseSizeBeforeUpdate);
        SocialNetwork testSocialNetwork = socialNetworkList.get(socialNetworkList.size() - 1);
        assertThat(testSocialNetwork.getThumbnail()).isEqualTo(DEFAULT_THUMBNAIL);
        assertThat(testSocialNetwork.getThumbnailContentType()).isEqualTo(DEFAULT_THUMBNAIL_CONTENT_TYPE);
        assertThat(testSocialNetwork.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSocialNetwork.getCompleteName()).isEqualTo(DEFAULT_COMPLETE_NAME);
        assertThat(testSocialNetwork.getMainLink()).isEqualTo(DEFAULT_MAIN_LINK);
        assertThat(testSocialNetwork.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testSocialNetwork.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testSocialNetwork.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testSocialNetwork.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testSocialNetwork.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void fullUpdateSocialNetworkWithPatch() throws Exception {
        // Initialize the database
        socialNetworkRepository.saveAndFlush(socialNetwork);

        int databaseSizeBeforeUpdate = socialNetworkRepository.findAll().size();

        // Update the socialNetwork using partial update
        SocialNetwork partialUpdatedSocialNetwork = new SocialNetwork();
        partialUpdatedSocialNetwork.setId(socialNetwork.getId());

        partialUpdatedSocialNetwork
            .thumbnail(UPDATED_THUMBNAIL)
            .thumbnailContentType(UPDATED_THUMBNAIL_CONTENT_TYPE)
            .name(UPDATED_NAME)
            .completeName(UPDATED_COMPLETE_NAME)
            .mainLink(UPDATED_MAIN_LINK)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);

        restSocialNetworkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSocialNetwork.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSocialNetwork))
            )
            .andExpect(status().isOk());

        // Validate the SocialNetwork in the database
        List<SocialNetwork> socialNetworkList = socialNetworkRepository.findAll();
        assertThat(socialNetworkList).hasSize(databaseSizeBeforeUpdate);
        SocialNetwork testSocialNetwork = socialNetworkList.get(socialNetworkList.size() - 1);
        assertThat(testSocialNetwork.getThumbnail()).isEqualTo(UPDATED_THUMBNAIL);
        assertThat(testSocialNetwork.getThumbnailContentType()).isEqualTo(UPDATED_THUMBNAIL_CONTENT_TYPE);
        assertThat(testSocialNetwork.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSocialNetwork.getCompleteName()).isEqualTo(UPDATED_COMPLETE_NAME);
        assertThat(testSocialNetwork.getMainLink()).isEqualTo(UPDATED_MAIN_LINK);
        assertThat(testSocialNetwork.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testSocialNetwork.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testSocialNetwork.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testSocialNetwork.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testSocialNetwork.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void patchNonExistingSocialNetwork() throws Exception {
        int databaseSizeBeforeUpdate = socialNetworkRepository.findAll().size();
        socialNetwork.setId(longCount.incrementAndGet());

        // Create the SocialNetwork
        SocialNetworkDTO socialNetworkDTO = socialNetworkMapper.toDto(socialNetwork);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSocialNetworkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, socialNetworkDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(socialNetworkDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SocialNetwork in the database
        List<SocialNetwork> socialNetworkList = socialNetworkRepository.findAll();
        assertThat(socialNetworkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSocialNetwork() throws Exception {
        int databaseSizeBeforeUpdate = socialNetworkRepository.findAll().size();
        socialNetwork.setId(longCount.incrementAndGet());

        // Create the SocialNetwork
        SocialNetworkDTO socialNetworkDTO = socialNetworkMapper.toDto(socialNetwork);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSocialNetworkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(socialNetworkDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SocialNetwork in the database
        List<SocialNetwork> socialNetworkList = socialNetworkRepository.findAll();
        assertThat(socialNetworkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSocialNetwork() throws Exception {
        int databaseSizeBeforeUpdate = socialNetworkRepository.findAll().size();
        socialNetwork.setId(longCount.incrementAndGet());

        // Create the SocialNetwork
        SocialNetworkDTO socialNetworkDTO = socialNetworkMapper.toDto(socialNetwork);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSocialNetworkMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(socialNetworkDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SocialNetwork in the database
        List<SocialNetwork> socialNetworkList = socialNetworkRepository.findAll();
        assertThat(socialNetworkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSocialNetwork() throws Exception {
        // Initialize the database
        socialNetworkRepository.saveAndFlush(socialNetwork);

        int databaseSizeBeforeDelete = socialNetworkRepository.findAll().size();

        // Delete the socialNetwork
        restSocialNetworkMockMvc
            .perform(delete(ENTITY_API_URL_ID, socialNetwork.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SocialNetwork> socialNetworkList = socialNetworkRepository.findAll();
        assertThat(socialNetworkList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
