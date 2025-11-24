package com.monsterdam.admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.monsterdam.admin.IntegrationTest;
import com.monsterdam.admin.domain.PersonalSocialLinks;
import com.monsterdam.admin.domain.UserProfile;
import com.monsterdam.admin.repository.EntityManager;
import com.monsterdam.admin.repository.PersonalSocialLinksRepository;
import com.monsterdam.admin.repository.search.PersonalSocialLinksSearchRepository;
import com.monsterdam.admin.service.PersonalSocialLinksService;
import com.monsterdam.admin.service.dto.PersonalSocialLinksDTO;
import com.monsterdam.admin.service.mapper.PersonalSocialLinksMapper;
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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

/**
 * Integration tests for the {@link PersonalSocialLinksResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
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

    private static final String ENTITY_API_URL = "/api/personal-social-links";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/personal-social-links/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PersonalSocialLinksRepository personalSocialLinksRepository;

    @Mock
    private PersonalSocialLinksRepository personalSocialLinksRepositoryMock;

    @Autowired
    private PersonalSocialLinksMapper personalSocialLinksMapper;

    @Mock
    private PersonalSocialLinksService personalSocialLinksServiceMock;

    @Autowired
    private PersonalSocialLinksSearchRepository personalSocialLinksSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

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
            .isDeleted(DEFAULT_IS_DELETED);
        // Add required entity
        UserProfile userProfile;
        userProfile = em.insert(UserProfileResourceIT.createEntity(em)).block();
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
            .isDeleted(UPDATED_IS_DELETED);
        // Add required entity
        UserProfile userProfile;
        userProfile = em.insert(UserProfileResourceIT.createUpdatedEntity(em)).block();
        personalSocialLinks.setUser(userProfile);
        return personalSocialLinks;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(PersonalSocialLinks.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
        UserProfileResourceIT.deleteEntities(em);
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        personalSocialLinksSearchRepository.deleteAll().block();
        assertThat(personalSocialLinksSearchRepository.count().block()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        personalSocialLinks = createEntity(em);
    }

    @Test
    void createPersonalSocialLinks() throws Exception {
        int databaseSizeBeforeCreate = personalSocialLinksRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(personalSocialLinksSearchRepository.findAll().collectList().block());
        // Create the PersonalSocialLinks
        PersonalSocialLinksDTO personalSocialLinksDTO = personalSocialLinksMapper.toDto(personalSocialLinks);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(personalSocialLinksDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the PersonalSocialLinks in the database
        List<PersonalSocialLinks> personalSocialLinksList = personalSocialLinksRepository.findAll().collectList().block();
        assertThat(personalSocialLinksList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(personalSocialLinksSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
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
    }

    @Test
    void createPersonalSocialLinksWithExistingId() throws Exception {
        // Create the PersonalSocialLinks with an existing ID
        personalSocialLinks.setId(1L);
        PersonalSocialLinksDTO personalSocialLinksDTO = personalSocialLinksMapper.toDto(personalSocialLinks);

        int databaseSizeBeforeCreate = personalSocialLinksRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(personalSocialLinksSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(personalSocialLinksDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PersonalSocialLinks in the database
        List<PersonalSocialLinks> personalSocialLinksList = personalSocialLinksRepository.findAll().collectList().block();
        assertThat(personalSocialLinksList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(personalSocialLinksSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkSocialLinkIsRequired() throws Exception {
        int databaseSizeBeforeTest = personalSocialLinksRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(personalSocialLinksSearchRepository.findAll().collectList().block());
        // set the field null
        personalSocialLinks.setSocialLink(null);

        // Create the PersonalSocialLinks, which fails.
        PersonalSocialLinksDTO personalSocialLinksDTO = personalSocialLinksMapper.toDto(personalSocialLinks);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(personalSocialLinksDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<PersonalSocialLinks> personalSocialLinksList = personalSocialLinksRepository.findAll().collectList().block();
        assertThat(personalSocialLinksList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(personalSocialLinksSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = personalSocialLinksRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(personalSocialLinksSearchRepository.findAll().collectList().block());
        // set the field null
        personalSocialLinks.setCreatedDate(null);

        // Create the PersonalSocialLinks, which fails.
        PersonalSocialLinksDTO personalSocialLinksDTO = personalSocialLinksMapper.toDto(personalSocialLinks);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(personalSocialLinksDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<PersonalSocialLinks> personalSocialLinksList = personalSocialLinksRepository.findAll().collectList().block();
        assertThat(personalSocialLinksList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(personalSocialLinksSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = personalSocialLinksRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(personalSocialLinksSearchRepository.findAll().collectList().block());
        // set the field null
        personalSocialLinks.setIsDeleted(null);

        // Create the PersonalSocialLinks, which fails.
        PersonalSocialLinksDTO personalSocialLinksDTO = personalSocialLinksMapper.toDto(personalSocialLinks);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(personalSocialLinksDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<PersonalSocialLinks> personalSocialLinksList = personalSocialLinksRepository.findAll().collectList().block();
        assertThat(personalSocialLinksList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(personalSocialLinksSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllPersonalSocialLinks() {
        // Initialize the database
        personalSocialLinksRepository.save(personalSocialLinks).block();

        // Get all the personalSocialLinksList
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
            .value(hasItem(personalSocialLinks.getId().intValue()))
            .jsonPath("$.[*].thumbnailContentType")
            .value(hasItem(DEFAULT_THUMBNAIL_CONTENT_TYPE))
            .jsonPath("$.[*].thumbnail")
            .value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_THUMBNAIL)))
            .jsonPath("$.[*].normalImageContentType")
            .value(hasItem(DEFAULT_NORMAL_IMAGE_CONTENT_TYPE))
            .jsonPath("$.[*].normalImage")
            .value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_NORMAL_IMAGE)))
            .jsonPath("$.[*].normalImageS3Key")
            .value(hasItem(DEFAULT_NORMAL_IMAGE_S_3_KEY))
            .jsonPath("$.[*].thumbnailIconS3Key")
            .value(hasItem(DEFAULT_THUMBNAIL_ICON_S_3_KEY))
            .jsonPath("$.[*].socialLink")
            .value(hasItem(DEFAULT_SOCIAL_LINK))
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

    @SuppressWarnings({ "unchecked" })
    void getAllPersonalSocialLinksWithEagerRelationshipsIsEnabled() {
        when(personalSocialLinksServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(personalSocialLinksServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPersonalSocialLinksWithEagerRelationshipsIsNotEnabled() {
        when(personalSocialLinksServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(personalSocialLinksRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getPersonalSocialLinks() {
        // Initialize the database
        personalSocialLinksRepository.save(personalSocialLinks).block();

        // Get the personalSocialLinks
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, personalSocialLinks.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(personalSocialLinks.getId().intValue()))
            .jsonPath("$.thumbnailContentType")
            .value(is(DEFAULT_THUMBNAIL_CONTENT_TYPE))
            .jsonPath("$.thumbnail")
            .value(is(Base64.getEncoder().encodeToString(DEFAULT_THUMBNAIL)))
            .jsonPath("$.normalImageContentType")
            .value(is(DEFAULT_NORMAL_IMAGE_CONTENT_TYPE))
            .jsonPath("$.normalImage")
            .value(is(Base64.getEncoder().encodeToString(DEFAULT_NORMAL_IMAGE)))
            .jsonPath("$.normalImageS3Key")
            .value(is(DEFAULT_NORMAL_IMAGE_S_3_KEY))
            .jsonPath("$.thumbnailIconS3Key")
            .value(is(DEFAULT_THUMBNAIL_ICON_S_3_KEY))
            .jsonPath("$.socialLink")
            .value(is(DEFAULT_SOCIAL_LINK))
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
    void getNonExistingPersonalSocialLinks() {
        // Get the personalSocialLinks
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingPersonalSocialLinks() throws Exception {
        // Initialize the database
        personalSocialLinksRepository.save(personalSocialLinks).block();

        int databaseSizeBeforeUpdate = personalSocialLinksRepository.findAll().collectList().block().size();
        personalSocialLinksSearchRepository.save(personalSocialLinks).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(personalSocialLinksSearchRepository.findAll().collectList().block());

        // Update the personalSocialLinks
        PersonalSocialLinks updatedPersonalSocialLinks = personalSocialLinksRepository.findById(personalSocialLinks.getId()).block();
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
            .isDeleted(UPDATED_IS_DELETED);
        PersonalSocialLinksDTO personalSocialLinksDTO = personalSocialLinksMapper.toDto(updatedPersonalSocialLinks);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, personalSocialLinksDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(personalSocialLinksDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the PersonalSocialLinks in the database
        List<PersonalSocialLinks> personalSocialLinksList = personalSocialLinksRepository.findAll().collectList().block();
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
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(personalSocialLinksSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<PersonalSocialLinks> personalSocialLinksSearchList = IterableUtils.toList(
                    personalSocialLinksSearchRepository.findAll().collectList().block()
                );
                PersonalSocialLinks testPersonalSocialLinksSearch = personalSocialLinksSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testPersonalSocialLinksSearch.getThumbnail()).isEqualTo(UPDATED_THUMBNAIL);
                assertThat(testPersonalSocialLinksSearch.getThumbnailContentType()).isEqualTo(UPDATED_THUMBNAIL_CONTENT_TYPE);
                assertThat(testPersonalSocialLinksSearch.getNormalImage()).isEqualTo(UPDATED_NORMAL_IMAGE);
                assertThat(testPersonalSocialLinksSearch.getNormalImageContentType()).isEqualTo(UPDATED_NORMAL_IMAGE_CONTENT_TYPE);
                assertThat(testPersonalSocialLinksSearch.getNormalImageS3Key()).isEqualTo(UPDATED_NORMAL_IMAGE_S_3_KEY);
                assertThat(testPersonalSocialLinksSearch.getThumbnailIconS3Key()).isEqualTo(UPDATED_THUMBNAIL_ICON_S_3_KEY);
                assertThat(testPersonalSocialLinksSearch.getSocialLink()).isEqualTo(UPDATED_SOCIAL_LINK);
                assertThat(testPersonalSocialLinksSearch.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
                assertThat(testPersonalSocialLinksSearch.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
                assertThat(testPersonalSocialLinksSearch.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
                assertThat(testPersonalSocialLinksSearch.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
                assertThat(testPersonalSocialLinksSearch.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
            });
    }

    @Test
    void putNonExistingPersonalSocialLinks() throws Exception {
        int databaseSizeBeforeUpdate = personalSocialLinksRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(personalSocialLinksSearchRepository.findAll().collectList().block());
        personalSocialLinks.setId(longCount.incrementAndGet());

        // Create the PersonalSocialLinks
        PersonalSocialLinksDTO personalSocialLinksDTO = personalSocialLinksMapper.toDto(personalSocialLinks);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, personalSocialLinksDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(personalSocialLinksDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PersonalSocialLinks in the database
        List<PersonalSocialLinks> personalSocialLinksList = personalSocialLinksRepository.findAll().collectList().block();
        assertThat(personalSocialLinksList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(personalSocialLinksSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchPersonalSocialLinks() throws Exception {
        int databaseSizeBeforeUpdate = personalSocialLinksRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(personalSocialLinksSearchRepository.findAll().collectList().block());
        personalSocialLinks.setId(longCount.incrementAndGet());

        // Create the PersonalSocialLinks
        PersonalSocialLinksDTO personalSocialLinksDTO = personalSocialLinksMapper.toDto(personalSocialLinks);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(personalSocialLinksDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PersonalSocialLinks in the database
        List<PersonalSocialLinks> personalSocialLinksList = personalSocialLinksRepository.findAll().collectList().block();
        assertThat(personalSocialLinksList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(personalSocialLinksSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamPersonalSocialLinks() throws Exception {
        int databaseSizeBeforeUpdate = personalSocialLinksRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(personalSocialLinksSearchRepository.findAll().collectList().block());
        personalSocialLinks.setId(longCount.incrementAndGet());

        // Create the PersonalSocialLinks
        PersonalSocialLinksDTO personalSocialLinksDTO = personalSocialLinksMapper.toDto(personalSocialLinks);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(personalSocialLinksDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the PersonalSocialLinks in the database
        List<PersonalSocialLinks> personalSocialLinksList = personalSocialLinksRepository.findAll().collectList().block();
        assertThat(personalSocialLinksList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(personalSocialLinksSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdatePersonalSocialLinksWithPatch() throws Exception {
        // Initialize the database
        personalSocialLinksRepository.save(personalSocialLinks).block();

        int databaseSizeBeforeUpdate = personalSocialLinksRepository.findAll().collectList().block().size();

        // Update the personalSocialLinks using partial update
        PersonalSocialLinks partialUpdatedPersonalSocialLinks = new PersonalSocialLinks();
        partialUpdatedPersonalSocialLinks.setId(personalSocialLinks.getId());

        partialUpdatedPersonalSocialLinks
            .thumbnail(UPDATED_THUMBNAIL)
            .thumbnailContentType(UPDATED_THUMBNAIL_CONTENT_TYPE)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPersonalSocialLinks.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPersonalSocialLinks))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the PersonalSocialLinks in the database
        List<PersonalSocialLinks> personalSocialLinksList = personalSocialLinksRepository.findAll().collectList().block();
        assertThat(personalSocialLinksList).hasSize(databaseSizeBeforeUpdate);
        PersonalSocialLinks testPersonalSocialLinks = personalSocialLinksList.get(personalSocialLinksList.size() - 1);
        assertThat(testPersonalSocialLinks.getThumbnail()).isEqualTo(UPDATED_THUMBNAIL);
        assertThat(testPersonalSocialLinks.getThumbnailContentType()).isEqualTo(UPDATED_THUMBNAIL_CONTENT_TYPE);
        assertThat(testPersonalSocialLinks.getNormalImage()).isEqualTo(DEFAULT_NORMAL_IMAGE);
        assertThat(testPersonalSocialLinks.getNormalImageContentType()).isEqualTo(DEFAULT_NORMAL_IMAGE_CONTENT_TYPE);
        assertThat(testPersonalSocialLinks.getNormalImageS3Key()).isEqualTo(DEFAULT_NORMAL_IMAGE_S_3_KEY);
        assertThat(testPersonalSocialLinks.getThumbnailIconS3Key()).isEqualTo(DEFAULT_THUMBNAIL_ICON_S_3_KEY);
        assertThat(testPersonalSocialLinks.getSocialLink()).isEqualTo(DEFAULT_SOCIAL_LINK);
        assertThat(testPersonalSocialLinks.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testPersonalSocialLinks.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testPersonalSocialLinks.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testPersonalSocialLinks.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testPersonalSocialLinks.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    void fullUpdatePersonalSocialLinksWithPatch() throws Exception {
        // Initialize the database
        personalSocialLinksRepository.save(personalSocialLinks).block();

        int databaseSizeBeforeUpdate = personalSocialLinksRepository.findAll().collectList().block().size();

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
            .isDeleted(UPDATED_IS_DELETED);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPersonalSocialLinks.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPersonalSocialLinks))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the PersonalSocialLinks in the database
        List<PersonalSocialLinks> personalSocialLinksList = personalSocialLinksRepository.findAll().collectList().block();
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
    }

    @Test
    void patchNonExistingPersonalSocialLinks() throws Exception {
        int databaseSizeBeforeUpdate = personalSocialLinksRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(personalSocialLinksSearchRepository.findAll().collectList().block());
        personalSocialLinks.setId(longCount.incrementAndGet());

        // Create the PersonalSocialLinks
        PersonalSocialLinksDTO personalSocialLinksDTO = personalSocialLinksMapper.toDto(personalSocialLinks);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, personalSocialLinksDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(personalSocialLinksDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PersonalSocialLinks in the database
        List<PersonalSocialLinks> personalSocialLinksList = personalSocialLinksRepository.findAll().collectList().block();
        assertThat(personalSocialLinksList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(personalSocialLinksSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchPersonalSocialLinks() throws Exception {
        int databaseSizeBeforeUpdate = personalSocialLinksRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(personalSocialLinksSearchRepository.findAll().collectList().block());
        personalSocialLinks.setId(longCount.incrementAndGet());

        // Create the PersonalSocialLinks
        PersonalSocialLinksDTO personalSocialLinksDTO = personalSocialLinksMapper.toDto(personalSocialLinks);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(personalSocialLinksDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PersonalSocialLinks in the database
        List<PersonalSocialLinks> personalSocialLinksList = personalSocialLinksRepository.findAll().collectList().block();
        assertThat(personalSocialLinksList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(personalSocialLinksSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamPersonalSocialLinks() throws Exception {
        int databaseSizeBeforeUpdate = personalSocialLinksRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(personalSocialLinksSearchRepository.findAll().collectList().block());
        personalSocialLinks.setId(longCount.incrementAndGet());

        // Create the PersonalSocialLinks
        PersonalSocialLinksDTO personalSocialLinksDTO = personalSocialLinksMapper.toDto(personalSocialLinks);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(personalSocialLinksDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the PersonalSocialLinks in the database
        List<PersonalSocialLinks> personalSocialLinksList = personalSocialLinksRepository.findAll().collectList().block();
        assertThat(personalSocialLinksList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(personalSocialLinksSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deletePersonalSocialLinks() {
        // Initialize the database
        personalSocialLinksRepository.save(personalSocialLinks).block();
        personalSocialLinksRepository.save(personalSocialLinks).block();
        personalSocialLinksSearchRepository.save(personalSocialLinks).block();

        int databaseSizeBeforeDelete = personalSocialLinksRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(personalSocialLinksSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the personalSocialLinks
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, personalSocialLinks.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<PersonalSocialLinks> personalSocialLinksList = personalSocialLinksRepository.findAll().collectList().block();
        assertThat(personalSocialLinksList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(personalSocialLinksSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchPersonalSocialLinks() {
        // Initialize the database
        personalSocialLinks = personalSocialLinksRepository.save(personalSocialLinks).block();
        personalSocialLinksSearchRepository.save(personalSocialLinks).block();

        // Search the personalSocialLinks
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + personalSocialLinks.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(personalSocialLinks.getId().intValue()))
            .jsonPath("$.[*].thumbnailContentType")
            .value(hasItem(DEFAULT_THUMBNAIL_CONTENT_TYPE))
            .jsonPath("$.[*].thumbnail")
            .value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_THUMBNAIL)))
            .jsonPath("$.[*].normalImageContentType")
            .value(hasItem(DEFAULT_NORMAL_IMAGE_CONTENT_TYPE))
            .jsonPath("$.[*].normalImage")
            .value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_NORMAL_IMAGE)))
            .jsonPath("$.[*].normalImageS3Key")
            .value(hasItem(DEFAULT_NORMAL_IMAGE_S_3_KEY))
            .jsonPath("$.[*].thumbnailIconS3Key")
            .value(hasItem(DEFAULT_THUMBNAIL_ICON_S_3_KEY))
            .jsonPath("$.[*].socialLink")
            .value(hasItem(DEFAULT_SOCIAL_LINK))
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
