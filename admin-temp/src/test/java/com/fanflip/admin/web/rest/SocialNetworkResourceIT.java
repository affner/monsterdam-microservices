package com.fanflip.admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.fanflip.admin.IntegrationTest;
import com.fanflip.admin.domain.SocialNetwork;
import com.fanflip.admin.repository.EntityManager;
import com.fanflip.admin.repository.SocialNetworkRepository;
import com.fanflip.admin.repository.search.SocialNetworkSearchRepository;
import com.fanflip.admin.service.dto.SocialNetworkDTO;
import com.fanflip.admin.service.mapper.SocialNetworkMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.commons.collections4.IterableUtils;
import org.assertj.core.util.IterableUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link SocialNetworkResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
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
    private static final String ENTITY_SEARCH_API_URL = "/api/social-networks/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SocialNetworkRepository socialNetworkRepository;

    @Autowired
    private SocialNetworkMapper socialNetworkMapper;

    @Autowired
    private SocialNetworkSearchRepository socialNetworkSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

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

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(SocialNetwork.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        socialNetworkSearchRepository.deleteAll().block();
        assertThat(socialNetworkSearchRepository.count().block()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        socialNetwork = createEntity(em);
    }

    @Test
    void createSocialNetwork() throws Exception {
        int databaseSizeBeforeCreate = socialNetworkRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(socialNetworkSearchRepository.findAll().collectList().block());
        // Create the SocialNetwork
        SocialNetworkDTO socialNetworkDTO = socialNetworkMapper.toDto(socialNetwork);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(socialNetworkDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the SocialNetwork in the database
        List<SocialNetwork> socialNetworkList = socialNetworkRepository.findAll().collectList().block();
        assertThat(socialNetworkList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(socialNetworkSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
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
    void createSocialNetworkWithExistingId() throws Exception {
        // Create the SocialNetwork with an existing ID
        socialNetwork.setId(1L);
        SocialNetworkDTO socialNetworkDTO = socialNetworkMapper.toDto(socialNetwork);

        int databaseSizeBeforeCreate = socialNetworkRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(socialNetworkSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(socialNetworkDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SocialNetwork in the database
        List<SocialNetwork> socialNetworkList = socialNetworkRepository.findAll().collectList().block();
        assertThat(socialNetworkList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(socialNetworkSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = socialNetworkRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(socialNetworkSearchRepository.findAll().collectList().block());
        // set the field null
        socialNetwork.setName(null);

        // Create the SocialNetwork, which fails.
        SocialNetworkDTO socialNetworkDTO = socialNetworkMapper.toDto(socialNetwork);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(socialNetworkDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<SocialNetwork> socialNetworkList = socialNetworkRepository.findAll().collectList().block();
        assertThat(socialNetworkList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(socialNetworkSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkCompleteNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = socialNetworkRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(socialNetworkSearchRepository.findAll().collectList().block());
        // set the field null
        socialNetwork.setCompleteName(null);

        // Create the SocialNetwork, which fails.
        SocialNetworkDTO socialNetworkDTO = socialNetworkMapper.toDto(socialNetwork);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(socialNetworkDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<SocialNetwork> socialNetworkList = socialNetworkRepository.findAll().collectList().block();
        assertThat(socialNetworkList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(socialNetworkSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkMainLinkIsRequired() throws Exception {
        int databaseSizeBeforeTest = socialNetworkRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(socialNetworkSearchRepository.findAll().collectList().block());
        // set the field null
        socialNetwork.setMainLink(null);

        // Create the SocialNetwork, which fails.
        SocialNetworkDTO socialNetworkDTO = socialNetworkMapper.toDto(socialNetwork);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(socialNetworkDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<SocialNetwork> socialNetworkList = socialNetworkRepository.findAll().collectList().block();
        assertThat(socialNetworkList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(socialNetworkSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = socialNetworkRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(socialNetworkSearchRepository.findAll().collectList().block());
        // set the field null
        socialNetwork.setCreatedDate(null);

        // Create the SocialNetwork, which fails.
        SocialNetworkDTO socialNetworkDTO = socialNetworkMapper.toDto(socialNetwork);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(socialNetworkDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<SocialNetwork> socialNetworkList = socialNetworkRepository.findAll().collectList().block();
        assertThat(socialNetworkList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(socialNetworkSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = socialNetworkRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(socialNetworkSearchRepository.findAll().collectList().block());
        // set the field null
        socialNetwork.setIsDeleted(null);

        // Create the SocialNetwork, which fails.
        SocialNetworkDTO socialNetworkDTO = socialNetworkMapper.toDto(socialNetwork);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(socialNetworkDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<SocialNetwork> socialNetworkList = socialNetworkRepository.findAll().collectList().block();
        assertThat(socialNetworkList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(socialNetworkSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllSocialNetworks() {
        // Initialize the database
        socialNetworkRepository.save(socialNetwork).block();

        // Get all the socialNetworkList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(socialNetwork.getId().intValue()))
            .jsonPath("$.[*].thumbnailContentType")
            .value(hasItem(DEFAULT_THUMBNAIL_CONTENT_TYPE))
            .jsonPath("$.[*].thumbnail")
            .value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_THUMBNAIL)))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].completeName")
            .value(hasItem(DEFAULT_COMPLETE_NAME))
            .jsonPath("$.[*].mainLink")
            .value(hasItem(DEFAULT_MAIN_LINK))
            .jsonPath("$.[*].createdDate")
            .value(hasItem(DEFAULT_CREATED_DATE.toString()))
            .jsonPath("$.[*].lastModifiedDate")
            .value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .jsonPath("$.[*].createdBy")
            .value(hasItem(DEFAULT_CREATED_BY))
            .jsonPath("$.[*].lastModifiedBy")
            .value(hasItem(DEFAULT_LAST_MODIFIED_BY))
            .jsonPath("$.[*].isDeleted")
            .value(hasItem(DEFAULT_IS_DELETED.booleanValue()));
    }

    @Test
    void getSocialNetwork() {
        // Initialize the database
        socialNetworkRepository.save(socialNetwork).block();

        // Get the socialNetwork
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, socialNetwork.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(socialNetwork.getId().intValue()))
            .jsonPath("$.thumbnailContentType")
            .value(is(DEFAULT_THUMBNAIL_CONTENT_TYPE))
            .jsonPath("$.thumbnail")
            .value(is(Base64.getEncoder().encodeToString(DEFAULT_THUMBNAIL)))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.completeName")
            .value(is(DEFAULT_COMPLETE_NAME))
            .jsonPath("$.mainLink")
            .value(is(DEFAULT_MAIN_LINK))
            .jsonPath("$.createdDate")
            .value(is(DEFAULT_CREATED_DATE.toString()))
            .jsonPath("$.lastModifiedDate")
            .value(is(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .jsonPath("$.createdBy")
            .value(is(DEFAULT_CREATED_BY))
            .jsonPath("$.lastModifiedBy")
            .value(is(DEFAULT_LAST_MODIFIED_BY))
            .jsonPath("$.isDeleted")
            .value(is(DEFAULT_IS_DELETED.booleanValue()));
    }

    @Test
    void getNonExistingSocialNetwork() {
        // Get the socialNetwork
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingSocialNetwork() throws Exception {
        // Initialize the database
        socialNetworkRepository.save(socialNetwork).block();

        int databaseSizeBeforeUpdate = socialNetworkRepository.findAll().collectList().block().size();
        socialNetworkSearchRepository.save(socialNetwork).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(socialNetworkSearchRepository.findAll().collectList().block());

        // Update the socialNetwork
        SocialNetwork updatedSocialNetwork = socialNetworkRepository.findById(socialNetwork.getId()).block();
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

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, socialNetworkDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(socialNetworkDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the SocialNetwork in the database
        List<SocialNetwork> socialNetworkList = socialNetworkRepository.findAll().collectList().block();
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
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(socialNetworkSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<SocialNetwork> socialNetworkSearchList = IterableUtils.toList(
                    socialNetworkSearchRepository.findAll().collectList().block()
                );
                SocialNetwork testSocialNetworkSearch = socialNetworkSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testSocialNetworkSearch.getThumbnail()).isEqualTo(UPDATED_THUMBNAIL);
                assertThat(testSocialNetworkSearch.getThumbnailContentType()).isEqualTo(UPDATED_THUMBNAIL_CONTENT_TYPE);
                assertThat(testSocialNetworkSearch.getName()).isEqualTo(UPDATED_NAME);
                assertThat(testSocialNetworkSearch.getCompleteName()).isEqualTo(UPDATED_COMPLETE_NAME);
                assertThat(testSocialNetworkSearch.getMainLink()).isEqualTo(UPDATED_MAIN_LINK);
                assertThat(testSocialNetworkSearch.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
                assertThat(testSocialNetworkSearch.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
                assertThat(testSocialNetworkSearch.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
                assertThat(testSocialNetworkSearch.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
                assertThat(testSocialNetworkSearch.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
            });
    }

    @Test
    void putNonExistingSocialNetwork() throws Exception {
        int databaseSizeBeforeUpdate = socialNetworkRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(socialNetworkSearchRepository.findAll().collectList().block());
        socialNetwork.setId(longCount.incrementAndGet());

        // Create the SocialNetwork
        SocialNetworkDTO socialNetworkDTO = socialNetworkMapper.toDto(socialNetwork);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, socialNetworkDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(socialNetworkDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SocialNetwork in the database
        List<SocialNetwork> socialNetworkList = socialNetworkRepository.findAll().collectList().block();
        assertThat(socialNetworkList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(socialNetworkSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchSocialNetwork() throws Exception {
        int databaseSizeBeforeUpdate = socialNetworkRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(socialNetworkSearchRepository.findAll().collectList().block());
        socialNetwork.setId(longCount.incrementAndGet());

        // Create the SocialNetwork
        SocialNetworkDTO socialNetworkDTO = socialNetworkMapper.toDto(socialNetwork);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(socialNetworkDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SocialNetwork in the database
        List<SocialNetwork> socialNetworkList = socialNetworkRepository.findAll().collectList().block();
        assertThat(socialNetworkList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(socialNetworkSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamSocialNetwork() throws Exception {
        int databaseSizeBeforeUpdate = socialNetworkRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(socialNetworkSearchRepository.findAll().collectList().block());
        socialNetwork.setId(longCount.incrementAndGet());

        // Create the SocialNetwork
        SocialNetworkDTO socialNetworkDTO = socialNetworkMapper.toDto(socialNetwork);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(socialNetworkDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the SocialNetwork in the database
        List<SocialNetwork> socialNetworkList = socialNetworkRepository.findAll().collectList().block();
        assertThat(socialNetworkList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(socialNetworkSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateSocialNetworkWithPatch() throws Exception {
        // Initialize the database
        socialNetworkRepository.save(socialNetwork).block();

        int databaseSizeBeforeUpdate = socialNetworkRepository.findAll().collectList().block().size();

        // Update the socialNetwork using partial update
        SocialNetwork partialUpdatedSocialNetwork = new SocialNetwork();
        partialUpdatedSocialNetwork.setId(socialNetwork.getId());

        partialUpdatedSocialNetwork
            .thumbnail(UPDATED_THUMBNAIL)
            .thumbnailContentType(UPDATED_THUMBNAIL_CONTENT_TYPE)
            .mainLink(UPDATED_MAIN_LINK)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .isDeleted(UPDATED_IS_DELETED);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedSocialNetwork.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedSocialNetwork))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the SocialNetwork in the database
        List<SocialNetwork> socialNetworkList = socialNetworkRepository.findAll().collectList().block();
        assertThat(socialNetworkList).hasSize(databaseSizeBeforeUpdate);
        SocialNetwork testSocialNetwork = socialNetworkList.get(socialNetworkList.size() - 1);
        assertThat(testSocialNetwork.getThumbnail()).isEqualTo(UPDATED_THUMBNAIL);
        assertThat(testSocialNetwork.getThumbnailContentType()).isEqualTo(UPDATED_THUMBNAIL_CONTENT_TYPE);
        assertThat(testSocialNetwork.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSocialNetwork.getCompleteName()).isEqualTo(DEFAULT_COMPLETE_NAME);
        assertThat(testSocialNetwork.getMainLink()).isEqualTo(UPDATED_MAIN_LINK);
        assertThat(testSocialNetwork.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testSocialNetwork.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testSocialNetwork.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testSocialNetwork.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testSocialNetwork.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    void fullUpdateSocialNetworkWithPatch() throws Exception {
        // Initialize the database
        socialNetworkRepository.save(socialNetwork).block();

        int databaseSizeBeforeUpdate = socialNetworkRepository.findAll().collectList().block().size();

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

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedSocialNetwork.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedSocialNetwork))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the SocialNetwork in the database
        List<SocialNetwork> socialNetworkList = socialNetworkRepository.findAll().collectList().block();
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
    void patchNonExistingSocialNetwork() throws Exception {
        int databaseSizeBeforeUpdate = socialNetworkRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(socialNetworkSearchRepository.findAll().collectList().block());
        socialNetwork.setId(longCount.incrementAndGet());

        // Create the SocialNetwork
        SocialNetworkDTO socialNetworkDTO = socialNetworkMapper.toDto(socialNetwork);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, socialNetworkDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(socialNetworkDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SocialNetwork in the database
        List<SocialNetwork> socialNetworkList = socialNetworkRepository.findAll().collectList().block();
        assertThat(socialNetworkList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(socialNetworkSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchSocialNetwork() throws Exception {
        int databaseSizeBeforeUpdate = socialNetworkRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(socialNetworkSearchRepository.findAll().collectList().block());
        socialNetwork.setId(longCount.incrementAndGet());

        // Create the SocialNetwork
        SocialNetworkDTO socialNetworkDTO = socialNetworkMapper.toDto(socialNetwork);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(socialNetworkDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SocialNetwork in the database
        List<SocialNetwork> socialNetworkList = socialNetworkRepository.findAll().collectList().block();
        assertThat(socialNetworkList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(socialNetworkSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamSocialNetwork() throws Exception {
        int databaseSizeBeforeUpdate = socialNetworkRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(socialNetworkSearchRepository.findAll().collectList().block());
        socialNetwork.setId(longCount.incrementAndGet());

        // Create the SocialNetwork
        SocialNetworkDTO socialNetworkDTO = socialNetworkMapper.toDto(socialNetwork);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(socialNetworkDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the SocialNetwork in the database
        List<SocialNetwork> socialNetworkList = socialNetworkRepository.findAll().collectList().block();
        assertThat(socialNetworkList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(socialNetworkSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteSocialNetwork() {
        // Initialize the database
        socialNetworkRepository.save(socialNetwork).block();
        socialNetworkRepository.save(socialNetwork).block();
        socialNetworkSearchRepository.save(socialNetwork).block();

        int databaseSizeBeforeDelete = socialNetworkRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(socialNetworkSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the socialNetwork
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, socialNetwork.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<SocialNetwork> socialNetworkList = socialNetworkRepository.findAll().collectList().block();
        assertThat(socialNetworkList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(socialNetworkSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchSocialNetwork() {
        // Initialize the database
        socialNetwork = socialNetworkRepository.save(socialNetwork).block();
        socialNetworkSearchRepository.save(socialNetwork).block();

        // Search the socialNetwork
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + socialNetwork.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(socialNetwork.getId().intValue()))
            .jsonPath("$.[*].thumbnailContentType")
            .value(hasItem(DEFAULT_THUMBNAIL_CONTENT_TYPE))
            .jsonPath("$.[*].thumbnail")
            .value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_THUMBNAIL)))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].completeName")
            .value(hasItem(DEFAULT_COMPLETE_NAME))
            .jsonPath("$.[*].mainLink")
            .value(hasItem(DEFAULT_MAIN_LINK))
            .jsonPath("$.[*].createdDate")
            .value(hasItem(DEFAULT_CREATED_DATE.toString()))
            .jsonPath("$.[*].lastModifiedDate")
            .value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .jsonPath("$.[*].createdBy")
            .value(hasItem(DEFAULT_CREATED_BY))
            .jsonPath("$.[*].lastModifiedBy")
            .value(hasItem(DEFAULT_LAST_MODIFIED_BY))
            .jsonPath("$.[*].isDeleted")
            .value(hasItem(DEFAULT_IS_DELETED.booleanValue()));
    }
}
