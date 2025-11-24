package com.monsterdam.admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.monsterdam.admin.IntegrationTest;
import com.monsterdam.admin.domain.ModerationAction;
import com.monsterdam.admin.domain.enumeration.ModerationActionType;
import com.monsterdam.admin.repository.EntityManager;
import com.monsterdam.admin.repository.ModerationActionRepository;
import com.monsterdam.admin.repository.search.ModerationActionSearchRepository;
import com.monsterdam.admin.service.dto.ModerationActionDTO;
import com.monsterdam.admin.service.mapper.ModerationActionMapper;
import java.time.Duration;
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
 * Integration tests for the {@link ModerationActionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ModerationActionResourceIT {

    private static final ModerationActionType DEFAULT_ACTION_TYPE = ModerationActionType.WARNING;
    private static final ModerationActionType UPDATED_ACTION_TYPE = ModerationActionType.TEMPORARY_BAN;

    private static final String DEFAULT_REASON = "AAAAAAAAAA";
    private static final String UPDATED_REASON = "BBBBBBBBBB";

    private static final Instant DEFAULT_ACTION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ACTION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Duration DEFAULT_DURATION_DAYS = Duration.ofHours(6);
    private static final Duration UPDATED_DURATION_DAYS = Duration.ofHours(12);

    private static final String ENTITY_API_URL = "/api/moderation-actions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/moderation-actions/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ModerationActionRepository moderationActionRepository;

    @Autowired
    private ModerationActionMapper moderationActionMapper;

    @Autowired
    private ModerationActionSearchRepository moderationActionSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private ModerationAction moderationAction;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ModerationAction createEntity(EntityManager em) {
        ModerationAction moderationAction = new ModerationAction()
            .actionType(DEFAULT_ACTION_TYPE)
            .reason(DEFAULT_REASON)
            .actionDate(DEFAULT_ACTION_DATE)
            .durationDays(DEFAULT_DURATION_DAYS);
        return moderationAction;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ModerationAction createUpdatedEntity(EntityManager em) {
        ModerationAction moderationAction = new ModerationAction()
            .actionType(UPDATED_ACTION_TYPE)
            .reason(UPDATED_REASON)
            .actionDate(UPDATED_ACTION_DATE)
            .durationDays(UPDATED_DURATION_DAYS);
        return moderationAction;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(ModerationAction.class).block();
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
        moderationActionSearchRepository.deleteAll().block();
        assertThat(moderationActionSearchRepository.count().block()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        moderationAction = createEntity(em);
    }

    @Test
    void createModerationAction() throws Exception {
        int databaseSizeBeforeCreate = moderationActionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(moderationActionSearchRepository.findAll().collectList().block());
        // Create the ModerationAction
        ModerationActionDTO moderationActionDTO = moderationActionMapper.toDto(moderationAction);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(moderationActionDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the ModerationAction in the database
        List<ModerationAction> moderationActionList = moderationActionRepository.findAll().collectList().block();
        assertThat(moderationActionList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(moderationActionSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        ModerationAction testModerationAction = moderationActionList.get(moderationActionList.size() - 1);
        assertThat(testModerationAction.getActionType()).isEqualTo(DEFAULT_ACTION_TYPE);
        assertThat(testModerationAction.getReason()).isEqualTo(DEFAULT_REASON);
        assertThat(testModerationAction.getActionDate()).isEqualTo(DEFAULT_ACTION_DATE);
        assertThat(testModerationAction.getDurationDays()).isEqualTo(DEFAULT_DURATION_DAYS);
    }

    @Test
    void createModerationActionWithExistingId() throws Exception {
        // Create the ModerationAction with an existing ID
        moderationAction.setId(1L);
        ModerationActionDTO moderationActionDTO = moderationActionMapper.toDto(moderationAction);

        int databaseSizeBeforeCreate = moderationActionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(moderationActionSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(moderationActionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ModerationAction in the database
        List<ModerationAction> moderationActionList = moderationActionRepository.findAll().collectList().block();
        assertThat(moderationActionList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(moderationActionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkActionTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = moderationActionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(moderationActionSearchRepository.findAll().collectList().block());
        // set the field null
        moderationAction.setActionType(null);

        // Create the ModerationAction, which fails.
        ModerationActionDTO moderationActionDTO = moderationActionMapper.toDto(moderationAction);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(moderationActionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ModerationAction> moderationActionList = moderationActionRepository.findAll().collectList().block();
        assertThat(moderationActionList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(moderationActionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllModerationActions() {
        // Initialize the database
        moderationActionRepository.save(moderationAction).block();

        // Get all the moderationActionList
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
            .value(hasItem(moderationAction.getId().intValue()))
            .jsonPath("$.[*].actionType")
            .value(hasItem(DEFAULT_ACTION_TYPE.toString()))
            .jsonPath("$.[*].reason")
            .value(hasItem(DEFAULT_REASON))
            .jsonPath("$.[*].actionDate")
            .value(hasItem(DEFAULT_ACTION_DATE.toString()))
            .jsonPath("$.[*].durationDays")
            .value(hasItem(DEFAULT_DURATION_DAYS.toString()));
    }

    @Test
    void getModerationAction() {
        // Initialize the database
        moderationActionRepository.save(moderationAction).block();

        // Get the moderationAction
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, moderationAction.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(moderationAction.getId().intValue()))
            .jsonPath("$.actionType")
            .value(is(DEFAULT_ACTION_TYPE.toString()))
            .jsonPath("$.reason")
            .value(is(DEFAULT_REASON))
            .jsonPath("$.actionDate")
            .value(is(DEFAULT_ACTION_DATE.toString()))
            .jsonPath("$.durationDays")
            .value(is(DEFAULT_DURATION_DAYS.toString()));
    }

    @Test
    void getNonExistingModerationAction() {
        // Get the moderationAction
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingModerationAction() throws Exception {
        // Initialize the database
        moderationActionRepository.save(moderationAction).block();

        int databaseSizeBeforeUpdate = moderationActionRepository.findAll().collectList().block().size();
        moderationActionSearchRepository.save(moderationAction).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(moderationActionSearchRepository.findAll().collectList().block());

        // Update the moderationAction
        ModerationAction updatedModerationAction = moderationActionRepository.findById(moderationAction.getId()).block();
        updatedModerationAction
            .actionType(UPDATED_ACTION_TYPE)
            .reason(UPDATED_REASON)
            .actionDate(UPDATED_ACTION_DATE)
            .durationDays(UPDATED_DURATION_DAYS);
        ModerationActionDTO moderationActionDTO = moderationActionMapper.toDto(updatedModerationAction);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, moderationActionDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(moderationActionDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ModerationAction in the database
        List<ModerationAction> moderationActionList = moderationActionRepository.findAll().collectList().block();
        assertThat(moderationActionList).hasSize(databaseSizeBeforeUpdate);
        ModerationAction testModerationAction = moderationActionList.get(moderationActionList.size() - 1);
        assertThat(testModerationAction.getActionType()).isEqualTo(UPDATED_ACTION_TYPE);
        assertThat(testModerationAction.getReason()).isEqualTo(UPDATED_REASON);
        assertThat(testModerationAction.getActionDate()).isEqualTo(UPDATED_ACTION_DATE);
        assertThat(testModerationAction.getDurationDays()).isEqualTo(UPDATED_DURATION_DAYS);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(moderationActionSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<ModerationAction> moderationActionSearchList = IterableUtils.toList(
                    moderationActionSearchRepository.findAll().collectList().block()
                );
                ModerationAction testModerationActionSearch = moderationActionSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testModerationActionSearch.getActionType()).isEqualTo(UPDATED_ACTION_TYPE);
                assertThat(testModerationActionSearch.getReason()).isEqualTo(UPDATED_REASON);
                assertThat(testModerationActionSearch.getActionDate()).isEqualTo(UPDATED_ACTION_DATE);
                assertThat(testModerationActionSearch.getDurationDays()).isEqualTo(UPDATED_DURATION_DAYS);
            });
    }

    @Test
    void putNonExistingModerationAction() throws Exception {
        int databaseSizeBeforeUpdate = moderationActionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(moderationActionSearchRepository.findAll().collectList().block());
        moderationAction.setId(longCount.incrementAndGet());

        // Create the ModerationAction
        ModerationActionDTO moderationActionDTO = moderationActionMapper.toDto(moderationAction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, moderationActionDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(moderationActionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ModerationAction in the database
        List<ModerationAction> moderationActionList = moderationActionRepository.findAll().collectList().block();
        assertThat(moderationActionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(moderationActionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchModerationAction() throws Exception {
        int databaseSizeBeforeUpdate = moderationActionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(moderationActionSearchRepository.findAll().collectList().block());
        moderationAction.setId(longCount.incrementAndGet());

        // Create the ModerationAction
        ModerationActionDTO moderationActionDTO = moderationActionMapper.toDto(moderationAction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(moderationActionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ModerationAction in the database
        List<ModerationAction> moderationActionList = moderationActionRepository.findAll().collectList().block();
        assertThat(moderationActionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(moderationActionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamModerationAction() throws Exception {
        int databaseSizeBeforeUpdate = moderationActionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(moderationActionSearchRepository.findAll().collectList().block());
        moderationAction.setId(longCount.incrementAndGet());

        // Create the ModerationAction
        ModerationActionDTO moderationActionDTO = moderationActionMapper.toDto(moderationAction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(moderationActionDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ModerationAction in the database
        List<ModerationAction> moderationActionList = moderationActionRepository.findAll().collectList().block();
        assertThat(moderationActionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(moderationActionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateModerationActionWithPatch() throws Exception {
        // Initialize the database
        moderationActionRepository.save(moderationAction).block();

        int databaseSizeBeforeUpdate = moderationActionRepository.findAll().collectList().block().size();

        // Update the moderationAction using partial update
        ModerationAction partialUpdatedModerationAction = new ModerationAction();
        partialUpdatedModerationAction.setId(moderationAction.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedModerationAction.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedModerationAction))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ModerationAction in the database
        List<ModerationAction> moderationActionList = moderationActionRepository.findAll().collectList().block();
        assertThat(moderationActionList).hasSize(databaseSizeBeforeUpdate);
        ModerationAction testModerationAction = moderationActionList.get(moderationActionList.size() - 1);
        assertThat(testModerationAction.getActionType()).isEqualTo(DEFAULT_ACTION_TYPE);
        assertThat(testModerationAction.getReason()).isEqualTo(DEFAULT_REASON);
        assertThat(testModerationAction.getActionDate()).isEqualTo(DEFAULT_ACTION_DATE);
        assertThat(testModerationAction.getDurationDays()).isEqualTo(DEFAULT_DURATION_DAYS);
    }

    @Test
    void fullUpdateModerationActionWithPatch() throws Exception {
        // Initialize the database
        moderationActionRepository.save(moderationAction).block();

        int databaseSizeBeforeUpdate = moderationActionRepository.findAll().collectList().block().size();

        // Update the moderationAction using partial update
        ModerationAction partialUpdatedModerationAction = new ModerationAction();
        partialUpdatedModerationAction.setId(moderationAction.getId());

        partialUpdatedModerationAction
            .actionType(UPDATED_ACTION_TYPE)
            .reason(UPDATED_REASON)
            .actionDate(UPDATED_ACTION_DATE)
            .durationDays(UPDATED_DURATION_DAYS);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedModerationAction.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedModerationAction))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ModerationAction in the database
        List<ModerationAction> moderationActionList = moderationActionRepository.findAll().collectList().block();
        assertThat(moderationActionList).hasSize(databaseSizeBeforeUpdate);
        ModerationAction testModerationAction = moderationActionList.get(moderationActionList.size() - 1);
        assertThat(testModerationAction.getActionType()).isEqualTo(UPDATED_ACTION_TYPE);
        assertThat(testModerationAction.getReason()).isEqualTo(UPDATED_REASON);
        assertThat(testModerationAction.getActionDate()).isEqualTo(UPDATED_ACTION_DATE);
        assertThat(testModerationAction.getDurationDays()).isEqualTo(UPDATED_DURATION_DAYS);
    }

    @Test
    void patchNonExistingModerationAction() throws Exception {
        int databaseSizeBeforeUpdate = moderationActionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(moderationActionSearchRepository.findAll().collectList().block());
        moderationAction.setId(longCount.incrementAndGet());

        // Create the ModerationAction
        ModerationActionDTO moderationActionDTO = moderationActionMapper.toDto(moderationAction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, moderationActionDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(moderationActionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ModerationAction in the database
        List<ModerationAction> moderationActionList = moderationActionRepository.findAll().collectList().block();
        assertThat(moderationActionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(moderationActionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchModerationAction() throws Exception {
        int databaseSizeBeforeUpdate = moderationActionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(moderationActionSearchRepository.findAll().collectList().block());
        moderationAction.setId(longCount.incrementAndGet());

        // Create the ModerationAction
        ModerationActionDTO moderationActionDTO = moderationActionMapper.toDto(moderationAction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(moderationActionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ModerationAction in the database
        List<ModerationAction> moderationActionList = moderationActionRepository.findAll().collectList().block();
        assertThat(moderationActionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(moderationActionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamModerationAction() throws Exception {
        int databaseSizeBeforeUpdate = moderationActionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(moderationActionSearchRepository.findAll().collectList().block());
        moderationAction.setId(longCount.incrementAndGet());

        // Create the ModerationAction
        ModerationActionDTO moderationActionDTO = moderationActionMapper.toDto(moderationAction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(moderationActionDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ModerationAction in the database
        List<ModerationAction> moderationActionList = moderationActionRepository.findAll().collectList().block();
        assertThat(moderationActionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(moderationActionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteModerationAction() {
        // Initialize the database
        moderationActionRepository.save(moderationAction).block();
        moderationActionRepository.save(moderationAction).block();
        moderationActionSearchRepository.save(moderationAction).block();

        int databaseSizeBeforeDelete = moderationActionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(moderationActionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the moderationAction
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, moderationAction.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<ModerationAction> moderationActionList = moderationActionRepository.findAll().collectList().block();
        assertThat(moderationActionList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(moderationActionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchModerationAction() {
        // Initialize the database
        moderationAction = moderationActionRepository.save(moderationAction).block();
        moderationActionSearchRepository.save(moderationAction).block();

        // Search the moderationAction
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + moderationAction.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(moderationAction.getId().intValue()))
            .jsonPath("$.[*].actionType")
            .value(hasItem(DEFAULT_ACTION_TYPE.toString()))
            .jsonPath("$.[*].reason")
            .value(hasItem(DEFAULT_REASON))
            .jsonPath("$.[*].actionDate")
            .value(hasItem(DEFAULT_ACTION_DATE.toString()))
            .jsonPath("$.[*].durationDays")
            .value(hasItem(DEFAULT_DURATION_DAYS.toString()));
    }
}
