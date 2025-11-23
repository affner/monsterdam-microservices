package com.fanflip.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.fanflip.app.IntegrationTest;
import com.fanflip.app.domain.UserUIPreferences;
import com.fanflip.app.repository.EntityManager;
import com.fanflip.app.repository.UserUIPreferencesRepository;
import com.fanflip.app.repository.search.UserUIPreferencesSearchRepository;
import com.fanflip.app.service.dto.UserUIPreferencesDTO;
import com.fanflip.app.service.mapper.UserUIPreferencesMapper;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link UserUIPreferencesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class UserUIPreferencesResourceIT {

    private static final String DEFAULT_PREFERENCES = "AAAAAAAAAA";
    private static final String UPDATED_PREFERENCES = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/user-ui-preferences";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/user-ui-preferences/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UserUIPreferencesRepository userUIPreferencesRepository;

    @Autowired
    private UserUIPreferencesMapper userUIPreferencesMapper;

    @Autowired
    private UserUIPreferencesSearchRepository userUIPreferencesSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private UserUIPreferences userUIPreferences;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserUIPreferences createEntity(EntityManager em) {
        UserUIPreferences userUIPreferences = new UserUIPreferences()
            .preferences(DEFAULT_PREFERENCES)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY);
        return userUIPreferences;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserUIPreferences createUpdatedEntity(EntityManager em) {
        UserUIPreferences userUIPreferences = new UserUIPreferences()
            .preferences(UPDATED_PREFERENCES)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);
        return userUIPreferences;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(UserUIPreferences.class).block();
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
        userUIPreferencesSearchRepository.deleteAll().block();
        assertThat(userUIPreferencesSearchRepository.count().block()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        userUIPreferences = createEntity(em);
    }

    @Test
    void createUserUIPreferences() throws Exception {
        int databaseSizeBeforeCreate = userUIPreferencesRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userUIPreferencesSearchRepository.findAll().collectList().block());
        // Create the UserUIPreferences
        UserUIPreferencesDTO userUIPreferencesDTO = userUIPreferencesMapper.toDto(userUIPreferences);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userUIPreferencesDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the UserUIPreferences in the database
        List<UserUIPreferences> userUIPreferencesList = userUIPreferencesRepository.findAll().collectList().block();
        assertThat(userUIPreferencesList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(userUIPreferencesSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        UserUIPreferences testUserUIPreferences = userUIPreferencesList.get(userUIPreferencesList.size() - 1);
        assertThat(testUserUIPreferences.getPreferences()).isEqualTo(DEFAULT_PREFERENCES);
        assertThat(testUserUIPreferences.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testUserUIPreferences.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testUserUIPreferences.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testUserUIPreferences.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
    }

    @Test
    void createUserUIPreferencesWithExistingId() throws Exception {
        // Create the UserUIPreferences with an existing ID
        userUIPreferences.setId(1L);
        UserUIPreferencesDTO userUIPreferencesDTO = userUIPreferencesMapper.toDto(userUIPreferences);

        int databaseSizeBeforeCreate = userUIPreferencesRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userUIPreferencesSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userUIPreferencesDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserUIPreferences in the database
        List<UserUIPreferences> userUIPreferencesList = userUIPreferencesRepository.findAll().collectList().block();
        assertThat(userUIPreferencesList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userUIPreferencesSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = userUIPreferencesRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userUIPreferencesSearchRepository.findAll().collectList().block());
        // set the field null
        userUIPreferences.setCreatedDate(null);

        // Create the UserUIPreferences, which fails.
        UserUIPreferencesDTO userUIPreferencesDTO = userUIPreferencesMapper.toDto(userUIPreferences);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userUIPreferencesDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<UserUIPreferences> userUIPreferencesList = userUIPreferencesRepository.findAll().collectList().block();
        assertThat(userUIPreferencesList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userUIPreferencesSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllUserUIPreferences() {
        // Initialize the database
        userUIPreferencesRepository.save(userUIPreferences).block();

        // Get all the userUIPreferencesList
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
            .value(hasItem(userUIPreferences.getId().intValue()))
            .jsonPath("$.[*].preferences")
            .value(hasItem(DEFAULT_PREFERENCES.toString()))
            .jsonPath("$.[*].createdDate")
            .value(hasItem(DEFAULT_CREATED_DATE.toString()))
            .jsonPath("$.[*].lastModifiedDate")
            .value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .jsonPath("$.[*].createdBy")
            .value(hasItem(DEFAULT_CREATED_BY))
            .jsonPath("$.[*].lastModifiedBy")
            .value(hasItem(DEFAULT_LAST_MODIFIED_BY));
    }

    @Test
    void getUserUIPreferences() {
        // Initialize the database
        userUIPreferencesRepository.save(userUIPreferences).block();

        // Get the userUIPreferences
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, userUIPreferences.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(userUIPreferences.getId().intValue()))
            .jsonPath("$.preferences")
            .value(is(DEFAULT_PREFERENCES.toString()))
            .jsonPath("$.createdDate")
            .value(is(DEFAULT_CREATED_DATE.toString()))
            .jsonPath("$.lastModifiedDate")
            .value(is(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .jsonPath("$.createdBy")
            .value(is(DEFAULT_CREATED_BY))
            .jsonPath("$.lastModifiedBy")
            .value(is(DEFAULT_LAST_MODIFIED_BY));
    }

    @Test
    void getNonExistingUserUIPreferences() {
        // Get the userUIPreferences
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingUserUIPreferences() throws Exception {
        // Initialize the database
        userUIPreferencesRepository.save(userUIPreferences).block();

        int databaseSizeBeforeUpdate = userUIPreferencesRepository.findAll().collectList().block().size();
        userUIPreferencesSearchRepository.save(userUIPreferences).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userUIPreferencesSearchRepository.findAll().collectList().block());

        // Update the userUIPreferences
        UserUIPreferences updatedUserUIPreferences = userUIPreferencesRepository.findById(userUIPreferences.getId()).block();
        updatedUserUIPreferences
            .preferences(UPDATED_PREFERENCES)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);
        UserUIPreferencesDTO userUIPreferencesDTO = userUIPreferencesMapper.toDto(updatedUserUIPreferences);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, userUIPreferencesDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userUIPreferencesDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the UserUIPreferences in the database
        List<UserUIPreferences> userUIPreferencesList = userUIPreferencesRepository.findAll().collectList().block();
        assertThat(userUIPreferencesList).hasSize(databaseSizeBeforeUpdate);
        UserUIPreferences testUserUIPreferences = userUIPreferencesList.get(userUIPreferencesList.size() - 1);
        assertThat(testUserUIPreferences.getPreferences()).isEqualTo(UPDATED_PREFERENCES);
        assertThat(testUserUIPreferences.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testUserUIPreferences.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testUserUIPreferences.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testUserUIPreferences.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(userUIPreferencesSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<UserUIPreferences> userUIPreferencesSearchList = IterableUtils.toList(
                    userUIPreferencesSearchRepository.findAll().collectList().block()
                );
                UserUIPreferences testUserUIPreferencesSearch = userUIPreferencesSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testUserUIPreferencesSearch.getPreferences()).isEqualTo(UPDATED_PREFERENCES);
                assertThat(testUserUIPreferencesSearch.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
                assertThat(testUserUIPreferencesSearch.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
                assertThat(testUserUIPreferencesSearch.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
                assertThat(testUserUIPreferencesSearch.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
            });
    }

    @Test
    void putNonExistingUserUIPreferences() throws Exception {
        int databaseSizeBeforeUpdate = userUIPreferencesRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userUIPreferencesSearchRepository.findAll().collectList().block());
        userUIPreferences.setId(longCount.incrementAndGet());

        // Create the UserUIPreferences
        UserUIPreferencesDTO userUIPreferencesDTO = userUIPreferencesMapper.toDto(userUIPreferences);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, userUIPreferencesDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userUIPreferencesDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserUIPreferences in the database
        List<UserUIPreferences> userUIPreferencesList = userUIPreferencesRepository.findAll().collectList().block();
        assertThat(userUIPreferencesList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userUIPreferencesSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchUserUIPreferences() throws Exception {
        int databaseSizeBeforeUpdate = userUIPreferencesRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userUIPreferencesSearchRepository.findAll().collectList().block());
        userUIPreferences.setId(longCount.incrementAndGet());

        // Create the UserUIPreferences
        UserUIPreferencesDTO userUIPreferencesDTO = userUIPreferencesMapper.toDto(userUIPreferences);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userUIPreferencesDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserUIPreferences in the database
        List<UserUIPreferences> userUIPreferencesList = userUIPreferencesRepository.findAll().collectList().block();
        assertThat(userUIPreferencesList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userUIPreferencesSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamUserUIPreferences() throws Exception {
        int databaseSizeBeforeUpdate = userUIPreferencesRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userUIPreferencesSearchRepository.findAll().collectList().block());
        userUIPreferences.setId(longCount.incrementAndGet());

        // Create the UserUIPreferences
        UserUIPreferencesDTO userUIPreferencesDTO = userUIPreferencesMapper.toDto(userUIPreferences);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userUIPreferencesDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the UserUIPreferences in the database
        List<UserUIPreferences> userUIPreferencesList = userUIPreferencesRepository.findAll().collectList().block();
        assertThat(userUIPreferencesList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userUIPreferencesSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateUserUIPreferencesWithPatch() throws Exception {
        // Initialize the database
        userUIPreferencesRepository.save(userUIPreferences).block();

        int databaseSizeBeforeUpdate = userUIPreferencesRepository.findAll().collectList().block().size();

        // Update the userUIPreferences using partial update
        UserUIPreferences partialUpdatedUserUIPreferences = new UserUIPreferences();
        partialUpdatedUserUIPreferences.setId(userUIPreferences.getId());

        partialUpdatedUserUIPreferences.createdBy(UPDATED_CREATED_BY);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedUserUIPreferences.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedUserUIPreferences))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the UserUIPreferences in the database
        List<UserUIPreferences> userUIPreferencesList = userUIPreferencesRepository.findAll().collectList().block();
        assertThat(userUIPreferencesList).hasSize(databaseSizeBeforeUpdate);
        UserUIPreferences testUserUIPreferences = userUIPreferencesList.get(userUIPreferencesList.size() - 1);
        assertThat(testUserUIPreferences.getPreferences()).isEqualTo(DEFAULT_PREFERENCES);
        assertThat(testUserUIPreferences.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testUserUIPreferences.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testUserUIPreferences.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testUserUIPreferences.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
    }

    @Test
    void fullUpdateUserUIPreferencesWithPatch() throws Exception {
        // Initialize the database
        userUIPreferencesRepository.save(userUIPreferences).block();

        int databaseSizeBeforeUpdate = userUIPreferencesRepository.findAll().collectList().block().size();

        // Update the userUIPreferences using partial update
        UserUIPreferences partialUpdatedUserUIPreferences = new UserUIPreferences();
        partialUpdatedUserUIPreferences.setId(userUIPreferences.getId());

        partialUpdatedUserUIPreferences
            .preferences(UPDATED_PREFERENCES)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedUserUIPreferences.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedUserUIPreferences))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the UserUIPreferences in the database
        List<UserUIPreferences> userUIPreferencesList = userUIPreferencesRepository.findAll().collectList().block();
        assertThat(userUIPreferencesList).hasSize(databaseSizeBeforeUpdate);
        UserUIPreferences testUserUIPreferences = userUIPreferencesList.get(userUIPreferencesList.size() - 1);
        assertThat(testUserUIPreferences.getPreferences()).isEqualTo(UPDATED_PREFERENCES);
        assertThat(testUserUIPreferences.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testUserUIPreferences.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testUserUIPreferences.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testUserUIPreferences.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    void patchNonExistingUserUIPreferences() throws Exception {
        int databaseSizeBeforeUpdate = userUIPreferencesRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userUIPreferencesSearchRepository.findAll().collectList().block());
        userUIPreferences.setId(longCount.incrementAndGet());

        // Create the UserUIPreferences
        UserUIPreferencesDTO userUIPreferencesDTO = userUIPreferencesMapper.toDto(userUIPreferences);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, userUIPreferencesDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(userUIPreferencesDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserUIPreferences in the database
        List<UserUIPreferences> userUIPreferencesList = userUIPreferencesRepository.findAll().collectList().block();
        assertThat(userUIPreferencesList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userUIPreferencesSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchUserUIPreferences() throws Exception {
        int databaseSizeBeforeUpdate = userUIPreferencesRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userUIPreferencesSearchRepository.findAll().collectList().block());
        userUIPreferences.setId(longCount.incrementAndGet());

        // Create the UserUIPreferences
        UserUIPreferencesDTO userUIPreferencesDTO = userUIPreferencesMapper.toDto(userUIPreferences);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(userUIPreferencesDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserUIPreferences in the database
        List<UserUIPreferences> userUIPreferencesList = userUIPreferencesRepository.findAll().collectList().block();
        assertThat(userUIPreferencesList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userUIPreferencesSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamUserUIPreferences() throws Exception {
        int databaseSizeBeforeUpdate = userUIPreferencesRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userUIPreferencesSearchRepository.findAll().collectList().block());
        userUIPreferences.setId(longCount.incrementAndGet());

        // Create the UserUIPreferences
        UserUIPreferencesDTO userUIPreferencesDTO = userUIPreferencesMapper.toDto(userUIPreferences);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(userUIPreferencesDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the UserUIPreferences in the database
        List<UserUIPreferences> userUIPreferencesList = userUIPreferencesRepository.findAll().collectList().block();
        assertThat(userUIPreferencesList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userUIPreferencesSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteUserUIPreferences() {
        // Initialize the database
        userUIPreferencesRepository.save(userUIPreferences).block();
        userUIPreferencesRepository.save(userUIPreferences).block();
        userUIPreferencesSearchRepository.save(userUIPreferences).block();

        int databaseSizeBeforeDelete = userUIPreferencesRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userUIPreferencesSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the userUIPreferences
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, userUIPreferences.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<UserUIPreferences> userUIPreferencesList = userUIPreferencesRepository.findAll().collectList().block();
        assertThat(userUIPreferencesList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userUIPreferencesSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchUserUIPreferences() {
        // Initialize the database
        userUIPreferences = userUIPreferencesRepository.save(userUIPreferences).block();
        userUIPreferencesSearchRepository.save(userUIPreferences).block();

        // Search the userUIPreferences
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + userUIPreferences.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(userUIPreferences.getId().intValue()))
            .jsonPath("$.[*].preferences")
            .value(hasItem(DEFAULT_PREFERENCES.toString()))
            .jsonPath("$.[*].createdDate")
            .value(hasItem(DEFAULT_CREATED_DATE.toString()))
            .jsonPath("$.[*].lastModifiedDate")
            .value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .jsonPath("$.[*].createdBy")
            .value(hasItem(DEFAULT_CREATED_BY))
            .jsonPath("$.[*].lastModifiedBy")
            .value(hasItem(DEFAULT_LAST_MODIFIED_BY));
    }
}
