package com.monsterdam.admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.monsterdam.admin.IntegrationTest;
import com.monsterdam.admin.domain.PollOption;
import com.monsterdam.admin.domain.PollVote;
import com.monsterdam.admin.repository.EntityManager;
import com.monsterdam.admin.repository.PollVoteRepository;
import com.monsterdam.admin.repository.search.PollVoteSearchRepository;
import com.monsterdam.admin.service.PollVoteService;
import com.monsterdam.admin.service.dto.PollVoteDTO;
import com.monsterdam.admin.service.mapper.PollVoteMapper;
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
 * Integration tests for the {@link PollVoteResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class PollVoteResourceIT {

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/poll-votes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/poll-votes/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PollVoteRepository pollVoteRepository;

    @Mock
    private PollVoteRepository pollVoteRepositoryMock;

    @Autowired
    private PollVoteMapper pollVoteMapper;

    @Mock
    private PollVoteService pollVoteServiceMock;

    @Autowired
    private PollVoteSearchRepository pollVoteSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private PollVote pollVote;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PollVote createEntity(EntityManager em) {
        PollVote pollVote = new PollVote().createdDate(DEFAULT_CREATED_DATE);
        // Add required entity
        PollOption pollOption;
        pollOption = em.insert(PollOptionResourceIT.createEntity(em)).block();
        pollVote.setPollOption(pollOption);
        return pollVote;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PollVote createUpdatedEntity(EntityManager em) {
        PollVote pollVote = new PollVote().createdDate(UPDATED_CREATED_DATE);
        // Add required entity
        PollOption pollOption;
        pollOption = em.insert(PollOptionResourceIT.createUpdatedEntity(em)).block();
        pollVote.setPollOption(pollOption);
        return pollVote;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(PollVote.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
        PollOptionResourceIT.deleteEntities(em);
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        pollVoteSearchRepository.deleteAll().block();
        assertThat(pollVoteSearchRepository.count().block()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        pollVote = createEntity(em);
    }

    @Test
    void createPollVote() throws Exception {
        int databaseSizeBeforeCreate = pollVoteRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(pollVoteSearchRepository.findAll().collectList().block());
        // Create the PollVote
        PollVoteDTO pollVoteDTO = pollVoteMapper.toDto(pollVote);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(pollVoteDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the PollVote in the database
        List<PollVote> pollVoteList = pollVoteRepository.findAll().collectList().block();
        assertThat(pollVoteList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(pollVoteSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        PollVote testPollVote = pollVoteList.get(pollVoteList.size() - 1);
        assertThat(testPollVote.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
    }

    @Test
    void createPollVoteWithExistingId() throws Exception {
        // Create the PollVote with an existing ID
        pollVote.setId(1L);
        PollVoteDTO pollVoteDTO = pollVoteMapper.toDto(pollVote);

        int databaseSizeBeforeCreate = pollVoteRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(pollVoteSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(pollVoteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PollVote in the database
        List<PollVote> pollVoteList = pollVoteRepository.findAll().collectList().block();
        assertThat(pollVoteList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(pollVoteSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = pollVoteRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(pollVoteSearchRepository.findAll().collectList().block());
        // set the field null
        pollVote.setCreatedDate(null);

        // Create the PollVote, which fails.
        PollVoteDTO pollVoteDTO = pollVoteMapper.toDto(pollVote);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(pollVoteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<PollVote> pollVoteList = pollVoteRepository.findAll().collectList().block();
        assertThat(pollVoteList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(pollVoteSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllPollVotes() {
        // Initialize the database
        pollVoteRepository.save(pollVote).block();

        // Get all the pollVoteList
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
            .value(hasItem(pollVote.getId().intValue()))
            .jsonPath("$.[*].createdDate")
            .value(hasItem(DEFAULT_CREATED_DATE.toString()));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPollVotesWithEagerRelationshipsIsEnabled() {
        when(pollVoteServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(pollVoteServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPollVotesWithEagerRelationshipsIsNotEnabled() {
        when(pollVoteServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(pollVoteRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getPollVote() {
        // Initialize the database
        pollVoteRepository.save(pollVote).block();

        // Get the pollVote
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, pollVote.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(pollVote.getId().intValue()))
            .jsonPath("$.createdDate")
            .value(is(DEFAULT_CREATED_DATE.toString()));
    }

    @Test
    void getNonExistingPollVote() {
        // Get the pollVote
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingPollVote() throws Exception {
        // Initialize the database
        pollVoteRepository.save(pollVote).block();

        int databaseSizeBeforeUpdate = pollVoteRepository.findAll().collectList().block().size();
        pollVoteSearchRepository.save(pollVote).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(pollVoteSearchRepository.findAll().collectList().block());

        // Update the pollVote
        PollVote updatedPollVote = pollVoteRepository.findById(pollVote.getId()).block();
        updatedPollVote.createdDate(UPDATED_CREATED_DATE);
        PollVoteDTO pollVoteDTO = pollVoteMapper.toDto(updatedPollVote);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, pollVoteDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(pollVoteDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the PollVote in the database
        List<PollVote> pollVoteList = pollVoteRepository.findAll().collectList().block();
        assertThat(pollVoteList).hasSize(databaseSizeBeforeUpdate);
        PollVote testPollVote = pollVoteList.get(pollVoteList.size() - 1);
        assertThat(testPollVote.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(pollVoteSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<PollVote> pollVoteSearchList = IterableUtils.toList(pollVoteSearchRepository.findAll().collectList().block());
                PollVote testPollVoteSearch = pollVoteSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testPollVoteSearch.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
            });
    }

    @Test
    void putNonExistingPollVote() throws Exception {
        int databaseSizeBeforeUpdate = pollVoteRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(pollVoteSearchRepository.findAll().collectList().block());
        pollVote.setId(longCount.incrementAndGet());

        // Create the PollVote
        PollVoteDTO pollVoteDTO = pollVoteMapper.toDto(pollVote);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, pollVoteDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(pollVoteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PollVote in the database
        List<PollVote> pollVoteList = pollVoteRepository.findAll().collectList().block();
        assertThat(pollVoteList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(pollVoteSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchPollVote() throws Exception {
        int databaseSizeBeforeUpdate = pollVoteRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(pollVoteSearchRepository.findAll().collectList().block());
        pollVote.setId(longCount.incrementAndGet());

        // Create the PollVote
        PollVoteDTO pollVoteDTO = pollVoteMapper.toDto(pollVote);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(pollVoteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PollVote in the database
        List<PollVote> pollVoteList = pollVoteRepository.findAll().collectList().block();
        assertThat(pollVoteList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(pollVoteSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamPollVote() throws Exception {
        int databaseSizeBeforeUpdate = pollVoteRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(pollVoteSearchRepository.findAll().collectList().block());
        pollVote.setId(longCount.incrementAndGet());

        // Create the PollVote
        PollVoteDTO pollVoteDTO = pollVoteMapper.toDto(pollVote);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(pollVoteDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the PollVote in the database
        List<PollVote> pollVoteList = pollVoteRepository.findAll().collectList().block();
        assertThat(pollVoteList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(pollVoteSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdatePollVoteWithPatch() throws Exception {
        // Initialize the database
        pollVoteRepository.save(pollVote).block();

        int databaseSizeBeforeUpdate = pollVoteRepository.findAll().collectList().block().size();

        // Update the pollVote using partial update
        PollVote partialUpdatedPollVote = new PollVote();
        partialUpdatedPollVote.setId(pollVote.getId());

        partialUpdatedPollVote.createdDate(UPDATED_CREATED_DATE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPollVote.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPollVote))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the PollVote in the database
        List<PollVote> pollVoteList = pollVoteRepository.findAll().collectList().block();
        assertThat(pollVoteList).hasSize(databaseSizeBeforeUpdate);
        PollVote testPollVote = pollVoteList.get(pollVoteList.size() - 1);
        assertThat(testPollVote.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
    }

    @Test
    void fullUpdatePollVoteWithPatch() throws Exception {
        // Initialize the database
        pollVoteRepository.save(pollVote).block();

        int databaseSizeBeforeUpdate = pollVoteRepository.findAll().collectList().block().size();

        // Update the pollVote using partial update
        PollVote partialUpdatedPollVote = new PollVote();
        partialUpdatedPollVote.setId(pollVote.getId());

        partialUpdatedPollVote.createdDate(UPDATED_CREATED_DATE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPollVote.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPollVote))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the PollVote in the database
        List<PollVote> pollVoteList = pollVoteRepository.findAll().collectList().block();
        assertThat(pollVoteList).hasSize(databaseSizeBeforeUpdate);
        PollVote testPollVote = pollVoteList.get(pollVoteList.size() - 1);
        assertThat(testPollVote.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
    }

    @Test
    void patchNonExistingPollVote() throws Exception {
        int databaseSizeBeforeUpdate = pollVoteRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(pollVoteSearchRepository.findAll().collectList().block());
        pollVote.setId(longCount.incrementAndGet());

        // Create the PollVote
        PollVoteDTO pollVoteDTO = pollVoteMapper.toDto(pollVote);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, pollVoteDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(pollVoteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PollVote in the database
        List<PollVote> pollVoteList = pollVoteRepository.findAll().collectList().block();
        assertThat(pollVoteList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(pollVoteSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchPollVote() throws Exception {
        int databaseSizeBeforeUpdate = pollVoteRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(pollVoteSearchRepository.findAll().collectList().block());
        pollVote.setId(longCount.incrementAndGet());

        // Create the PollVote
        PollVoteDTO pollVoteDTO = pollVoteMapper.toDto(pollVote);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(pollVoteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PollVote in the database
        List<PollVote> pollVoteList = pollVoteRepository.findAll().collectList().block();
        assertThat(pollVoteList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(pollVoteSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamPollVote() throws Exception {
        int databaseSizeBeforeUpdate = pollVoteRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(pollVoteSearchRepository.findAll().collectList().block());
        pollVote.setId(longCount.incrementAndGet());

        // Create the PollVote
        PollVoteDTO pollVoteDTO = pollVoteMapper.toDto(pollVote);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(pollVoteDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the PollVote in the database
        List<PollVote> pollVoteList = pollVoteRepository.findAll().collectList().block();
        assertThat(pollVoteList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(pollVoteSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deletePollVote() {
        // Initialize the database
        pollVoteRepository.save(pollVote).block();
        pollVoteRepository.save(pollVote).block();
        pollVoteSearchRepository.save(pollVote).block();

        int databaseSizeBeforeDelete = pollVoteRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(pollVoteSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the pollVote
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, pollVote.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<PollVote> pollVoteList = pollVoteRepository.findAll().collectList().block();
        assertThat(pollVoteList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(pollVoteSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchPollVote() {
        // Initialize the database
        pollVote = pollVoteRepository.save(pollVote).block();
        pollVoteSearchRepository.save(pollVote).block();

        // Search the pollVote
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + pollVote.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(pollVote.getId().intValue()))
            .jsonPath("$.[*].createdDate")
            .value(hasItem(DEFAULT_CREATED_DATE.toString()));
    }
}
