package com.monsterdam.admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.monsterdam.admin.IntegrationTest;
import com.monsterdam.admin.domain.UserMention;
import com.monsterdam.admin.domain.UserProfile;
import com.monsterdam.admin.repository.EntityManager;
import com.monsterdam.admin.repository.UserMentionRepository;
import com.monsterdam.admin.repository.search.UserMentionSearchRepository;
import com.monsterdam.admin.service.UserMentionService;
import com.monsterdam.admin.service.dto.UserMentionDTO;
import com.monsterdam.admin.service.mapper.UserMentionMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link UserMentionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class UserMentionResourceIT {

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

    private static final String ENTITY_API_URL = "/api/user-mentions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/user-mentions/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UserMentionRepository userMentionRepository;

    @Mock
    private UserMentionRepository userMentionRepositoryMock;

    @Autowired
    private UserMentionMapper userMentionMapper;

    @Mock
    private UserMentionService userMentionServiceMock;

    @Autowired
    private UserMentionSearchRepository userMentionSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private UserMention userMention;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserMention createEntity(EntityManager em) {
        UserMention userMention = new UserMention()
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .isDeleted(DEFAULT_IS_DELETED);
        // Add required entity
        UserProfile userProfile;
        userProfile = em.insert(UserProfileResourceIT.createEntity(em)).block();
        userMention.setMentionedUser(userProfile);
        return userMention;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserMention createUpdatedEntity(EntityManager em) {
        UserMention userMention = new UserMention()
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        // Add required entity
        UserProfile userProfile;
        userProfile = em.insert(UserProfileResourceIT.createUpdatedEntity(em)).block();
        userMention.setMentionedUser(userProfile);
        return userMention;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(UserMention.class).block();
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
        userMentionSearchRepository.deleteAll().block();
        assertThat(userMentionSearchRepository.count().block()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        userMention = createEntity(em);
    }

    @Test
    void createUserMention() throws Exception {
        int databaseSizeBeforeCreate = userMentionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userMentionSearchRepository.findAll().collectList().block());
        // Create the UserMention
        UserMentionDTO userMentionDTO = userMentionMapper.toDto(userMention);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userMentionDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the UserMention in the database
        List<UserMention> userMentionList = userMentionRepository.findAll().collectList().block();
        assertThat(userMentionList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(userMentionSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        UserMention testUserMention = userMentionList.get(userMentionList.size() - 1);
        assertThat(testUserMention.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testUserMention.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testUserMention.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testUserMention.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testUserMention.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    void createUserMentionWithExistingId() throws Exception {
        // Create the UserMention with an existing ID
        userMention.setId(1L);
        UserMentionDTO userMentionDTO = userMentionMapper.toDto(userMention);

        int databaseSizeBeforeCreate = userMentionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userMentionSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userMentionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserMention in the database
        List<UserMention> userMentionList = userMentionRepository.findAll().collectList().block();
        assertThat(userMentionList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userMentionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = userMentionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userMentionSearchRepository.findAll().collectList().block());
        // set the field null
        userMention.setCreatedDate(null);

        // Create the UserMention, which fails.
        UserMentionDTO userMentionDTO = userMentionMapper.toDto(userMention);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userMentionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<UserMention> userMentionList = userMentionRepository.findAll().collectList().block();
        assertThat(userMentionList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userMentionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = userMentionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userMentionSearchRepository.findAll().collectList().block());
        // set the field null
        userMention.setIsDeleted(null);

        // Create the UserMention, which fails.
        UserMentionDTO userMentionDTO = userMentionMapper.toDto(userMention);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userMentionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<UserMention> userMentionList = userMentionRepository.findAll().collectList().block();
        assertThat(userMentionList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userMentionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllUserMentions() {
        // Initialize the database
        userMentionRepository.save(userMention).block();

        // Get all the userMentionList
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
            .value(hasItem(userMention.getId().intValue()))
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
    void getAllUserMentionsWithEagerRelationshipsIsEnabled() {
        when(userMentionServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(userMentionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUserMentionsWithEagerRelationshipsIsNotEnabled() {
        when(userMentionServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(userMentionRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getUserMention() {
        // Initialize the database
        userMentionRepository.save(userMention).block();

        // Get the userMention
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, userMention.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(userMention.getId().intValue()))
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
    void getNonExistingUserMention() {
        // Get the userMention
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingUserMention() throws Exception {
        // Initialize the database
        userMentionRepository.save(userMention).block();

        int databaseSizeBeforeUpdate = userMentionRepository.findAll().collectList().block().size();
        userMentionSearchRepository.save(userMention).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userMentionSearchRepository.findAll().collectList().block());

        // Update the userMention
        UserMention updatedUserMention = userMentionRepository.findById(userMention.getId()).block();
        updatedUserMention
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        UserMentionDTO userMentionDTO = userMentionMapper.toDto(updatedUserMention);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, userMentionDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userMentionDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the UserMention in the database
        List<UserMention> userMentionList = userMentionRepository.findAll().collectList().block();
        assertThat(userMentionList).hasSize(databaseSizeBeforeUpdate);
        UserMention testUserMention = userMentionList.get(userMentionList.size() - 1);
        assertThat(testUserMention.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testUserMention.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testUserMention.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testUserMention.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testUserMention.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(userMentionSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<UserMention> userMentionSearchList = IterableUtils.toList(userMentionSearchRepository.findAll().collectList().block());
                UserMention testUserMentionSearch = userMentionSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testUserMentionSearch.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
                assertThat(testUserMentionSearch.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
                assertThat(testUserMentionSearch.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
                assertThat(testUserMentionSearch.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
                assertThat(testUserMentionSearch.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
            });
    }

    @Test
    void putNonExistingUserMention() throws Exception {
        int databaseSizeBeforeUpdate = userMentionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userMentionSearchRepository.findAll().collectList().block());
        userMention.setId(longCount.incrementAndGet());

        // Create the UserMention
        UserMentionDTO userMentionDTO = userMentionMapper.toDto(userMention);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, userMentionDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userMentionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserMention in the database
        List<UserMention> userMentionList = userMentionRepository.findAll().collectList().block();
        assertThat(userMentionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userMentionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchUserMention() throws Exception {
        int databaseSizeBeforeUpdate = userMentionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userMentionSearchRepository.findAll().collectList().block());
        userMention.setId(longCount.incrementAndGet());

        // Create the UserMention
        UserMentionDTO userMentionDTO = userMentionMapper.toDto(userMention);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userMentionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserMention in the database
        List<UserMention> userMentionList = userMentionRepository.findAll().collectList().block();
        assertThat(userMentionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userMentionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamUserMention() throws Exception {
        int databaseSizeBeforeUpdate = userMentionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userMentionSearchRepository.findAll().collectList().block());
        userMention.setId(longCount.incrementAndGet());

        // Create the UserMention
        UserMentionDTO userMentionDTO = userMentionMapper.toDto(userMention);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userMentionDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the UserMention in the database
        List<UserMention> userMentionList = userMentionRepository.findAll().collectList().block();
        assertThat(userMentionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userMentionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateUserMentionWithPatch() throws Exception {
        // Initialize the database
        userMentionRepository.save(userMention).block();

        int databaseSizeBeforeUpdate = userMentionRepository.findAll().collectList().block().size();

        // Update the userMention using partial update
        UserMention partialUpdatedUserMention = new UserMention();
        partialUpdatedUserMention.setId(userMention.getId());

        partialUpdatedUserMention.lastModifiedDate(UPDATED_LAST_MODIFIED_DATE).lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedUserMention.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedUserMention))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the UserMention in the database
        List<UserMention> userMentionList = userMentionRepository.findAll().collectList().block();
        assertThat(userMentionList).hasSize(databaseSizeBeforeUpdate);
        UserMention testUserMention = userMentionList.get(userMentionList.size() - 1);
        assertThat(testUserMention.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testUserMention.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testUserMention.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testUserMention.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testUserMention.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    void fullUpdateUserMentionWithPatch() throws Exception {
        // Initialize the database
        userMentionRepository.save(userMention).block();

        int databaseSizeBeforeUpdate = userMentionRepository.findAll().collectList().block().size();

        // Update the userMention using partial update
        UserMention partialUpdatedUserMention = new UserMention();
        partialUpdatedUserMention.setId(userMention.getId());

        partialUpdatedUserMention
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedUserMention.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedUserMention))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the UserMention in the database
        List<UserMention> userMentionList = userMentionRepository.findAll().collectList().block();
        assertThat(userMentionList).hasSize(databaseSizeBeforeUpdate);
        UserMention testUserMention = userMentionList.get(userMentionList.size() - 1);
        assertThat(testUserMention.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testUserMention.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testUserMention.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testUserMention.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testUserMention.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    void patchNonExistingUserMention() throws Exception {
        int databaseSizeBeforeUpdate = userMentionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userMentionSearchRepository.findAll().collectList().block());
        userMention.setId(longCount.incrementAndGet());

        // Create the UserMention
        UserMentionDTO userMentionDTO = userMentionMapper.toDto(userMention);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, userMentionDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(userMentionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserMention in the database
        List<UserMention> userMentionList = userMentionRepository.findAll().collectList().block();
        assertThat(userMentionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userMentionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchUserMention() throws Exception {
        int databaseSizeBeforeUpdate = userMentionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userMentionSearchRepository.findAll().collectList().block());
        userMention.setId(longCount.incrementAndGet());

        // Create the UserMention
        UserMentionDTO userMentionDTO = userMentionMapper.toDto(userMention);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(userMentionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserMention in the database
        List<UserMention> userMentionList = userMentionRepository.findAll().collectList().block();
        assertThat(userMentionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userMentionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamUserMention() throws Exception {
        int databaseSizeBeforeUpdate = userMentionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userMentionSearchRepository.findAll().collectList().block());
        userMention.setId(longCount.incrementAndGet());

        // Create the UserMention
        UserMentionDTO userMentionDTO = userMentionMapper.toDto(userMention);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(userMentionDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the UserMention in the database
        List<UserMention> userMentionList = userMentionRepository.findAll().collectList().block();
        assertThat(userMentionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userMentionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteUserMention() {
        // Initialize the database
        userMentionRepository.save(userMention).block();
        userMentionRepository.save(userMention).block();
        userMentionSearchRepository.save(userMention).block();

        int databaseSizeBeforeDelete = userMentionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userMentionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the userMention
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, userMention.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<UserMention> userMentionList = userMentionRepository.findAll().collectList().block();
        assertThat(userMentionList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userMentionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchUserMention() {
        // Initialize the database
        userMention = userMentionRepository.save(userMention).block();
        userMentionSearchRepository.save(userMention).block();

        // Search the userMention
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + userMention.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(userMention.getId().intValue()))
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
