package com.monsterdam.admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.monsterdam.admin.IntegrationTest;
import com.monsterdam.admin.domain.PollOption;
import com.monsterdam.admin.domain.PostPoll;
import com.monsterdam.admin.repository.EntityManager;
import com.monsterdam.admin.repository.PollOptionRepository;
import com.monsterdam.admin.repository.search.PollOptionSearchRepository;
import com.monsterdam.admin.service.PollOptionService;
import com.monsterdam.admin.service.dto.PollOptionDTO;
import com.monsterdam.admin.service.mapper.PollOptionMapper;
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
 * Integration tests for the {@link PollOptionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class PollOptionResourceIT {

    private static final String DEFAULT_OPTION_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_OPTION_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_VOTE_COUNT = 1;
    private static final Integer UPDATED_VOTE_COUNT = 2;

    private static final String ENTITY_API_URL = "/api/poll-options";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/poll-options/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PollOptionRepository pollOptionRepository;

    @Mock
    private PollOptionRepository pollOptionRepositoryMock;

    @Autowired
    private PollOptionMapper pollOptionMapper;

    @Mock
    private PollOptionService pollOptionServiceMock;

    @Autowired
    private PollOptionSearchRepository pollOptionSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private PollOption pollOption;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PollOption createEntity(EntityManager em) {
        PollOption pollOption = new PollOption().optionDescription(DEFAULT_OPTION_DESCRIPTION).voteCount(DEFAULT_VOTE_COUNT);
        // Add required entity
        PostPoll postPoll;
        postPoll = em.insert(PostPollResourceIT.createEntity(em)).block();
        pollOption.setPoll(postPoll);
        return pollOption;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PollOption createUpdatedEntity(EntityManager em) {
        PollOption pollOption = new PollOption().optionDescription(UPDATED_OPTION_DESCRIPTION).voteCount(UPDATED_VOTE_COUNT);
        // Add required entity
        PostPoll postPoll;
        postPoll = em.insert(PostPollResourceIT.createUpdatedEntity(em)).block();
        pollOption.setPoll(postPoll);
        return pollOption;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(PollOption.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
        PostPollResourceIT.deleteEntities(em);
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        pollOptionSearchRepository.deleteAll().block();
        assertThat(pollOptionSearchRepository.count().block()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        pollOption = createEntity(em);
    }

    @Test
    void createPollOption() throws Exception {
        int databaseSizeBeforeCreate = pollOptionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(pollOptionSearchRepository.findAll().collectList().block());
        // Create the PollOption
        PollOptionDTO pollOptionDTO = pollOptionMapper.toDto(pollOption);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(pollOptionDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the PollOption in the database
        List<PollOption> pollOptionList = pollOptionRepository.findAll().collectList().block();
        assertThat(pollOptionList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(pollOptionSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        PollOption testPollOption = pollOptionList.get(pollOptionList.size() - 1);
        assertThat(testPollOption.getOptionDescription()).isEqualTo(DEFAULT_OPTION_DESCRIPTION);
        assertThat(testPollOption.getVoteCount()).isEqualTo(DEFAULT_VOTE_COUNT);
    }

    @Test
    void createPollOptionWithExistingId() throws Exception {
        // Create the PollOption with an existing ID
        pollOption.setId(1L);
        PollOptionDTO pollOptionDTO = pollOptionMapper.toDto(pollOption);

        int databaseSizeBeforeCreate = pollOptionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(pollOptionSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(pollOptionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PollOption in the database
        List<PollOption> pollOptionList = pollOptionRepository.findAll().collectList().block();
        assertThat(pollOptionList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(pollOptionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkVoteCountIsRequired() throws Exception {
        int databaseSizeBeforeTest = pollOptionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(pollOptionSearchRepository.findAll().collectList().block());
        // set the field null
        pollOption.setVoteCount(null);

        // Create the PollOption, which fails.
        PollOptionDTO pollOptionDTO = pollOptionMapper.toDto(pollOption);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(pollOptionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<PollOption> pollOptionList = pollOptionRepository.findAll().collectList().block();
        assertThat(pollOptionList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(pollOptionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllPollOptions() {
        // Initialize the database
        pollOptionRepository.save(pollOption).block();

        // Get all the pollOptionList
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
            .value(hasItem(pollOption.getId().intValue()))
            .jsonPath("$.[*].optionDescription")
            .value(hasItem(DEFAULT_OPTION_DESCRIPTION.toString()))
            .jsonPath("$.[*].voteCount")
            .value(hasItem(DEFAULT_VOTE_COUNT));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPollOptionsWithEagerRelationshipsIsEnabled() {
        when(pollOptionServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(pollOptionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPollOptionsWithEagerRelationshipsIsNotEnabled() {
        when(pollOptionServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(pollOptionRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getPollOption() {
        // Initialize the database
        pollOptionRepository.save(pollOption).block();

        // Get the pollOption
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, pollOption.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(pollOption.getId().intValue()))
            .jsonPath("$.optionDescription")
            .value(is(DEFAULT_OPTION_DESCRIPTION.toString()))
            .jsonPath("$.voteCount")
            .value(is(DEFAULT_VOTE_COUNT));
    }

    @Test
    void getNonExistingPollOption() {
        // Get the pollOption
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingPollOption() throws Exception {
        // Initialize the database
        pollOptionRepository.save(pollOption).block();

        int databaseSizeBeforeUpdate = pollOptionRepository.findAll().collectList().block().size();
        pollOptionSearchRepository.save(pollOption).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(pollOptionSearchRepository.findAll().collectList().block());

        // Update the pollOption
        PollOption updatedPollOption = pollOptionRepository.findById(pollOption.getId()).block();
        updatedPollOption.optionDescription(UPDATED_OPTION_DESCRIPTION).voteCount(UPDATED_VOTE_COUNT);
        PollOptionDTO pollOptionDTO = pollOptionMapper.toDto(updatedPollOption);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, pollOptionDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(pollOptionDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the PollOption in the database
        List<PollOption> pollOptionList = pollOptionRepository.findAll().collectList().block();
        assertThat(pollOptionList).hasSize(databaseSizeBeforeUpdate);
        PollOption testPollOption = pollOptionList.get(pollOptionList.size() - 1);
        assertThat(testPollOption.getOptionDescription()).isEqualTo(UPDATED_OPTION_DESCRIPTION);
        assertThat(testPollOption.getVoteCount()).isEqualTo(UPDATED_VOTE_COUNT);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(pollOptionSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<PollOption> pollOptionSearchList = IterableUtils.toList(pollOptionSearchRepository.findAll().collectList().block());
                PollOption testPollOptionSearch = pollOptionSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testPollOptionSearch.getOptionDescription()).isEqualTo(UPDATED_OPTION_DESCRIPTION);
                assertThat(testPollOptionSearch.getVoteCount()).isEqualTo(UPDATED_VOTE_COUNT);
            });
    }

    @Test
    void putNonExistingPollOption() throws Exception {
        int databaseSizeBeforeUpdate = pollOptionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(pollOptionSearchRepository.findAll().collectList().block());
        pollOption.setId(longCount.incrementAndGet());

        // Create the PollOption
        PollOptionDTO pollOptionDTO = pollOptionMapper.toDto(pollOption);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, pollOptionDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(pollOptionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PollOption in the database
        List<PollOption> pollOptionList = pollOptionRepository.findAll().collectList().block();
        assertThat(pollOptionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(pollOptionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchPollOption() throws Exception {
        int databaseSizeBeforeUpdate = pollOptionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(pollOptionSearchRepository.findAll().collectList().block());
        pollOption.setId(longCount.incrementAndGet());

        // Create the PollOption
        PollOptionDTO pollOptionDTO = pollOptionMapper.toDto(pollOption);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(pollOptionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PollOption in the database
        List<PollOption> pollOptionList = pollOptionRepository.findAll().collectList().block();
        assertThat(pollOptionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(pollOptionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamPollOption() throws Exception {
        int databaseSizeBeforeUpdate = pollOptionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(pollOptionSearchRepository.findAll().collectList().block());
        pollOption.setId(longCount.incrementAndGet());

        // Create the PollOption
        PollOptionDTO pollOptionDTO = pollOptionMapper.toDto(pollOption);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(pollOptionDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the PollOption in the database
        List<PollOption> pollOptionList = pollOptionRepository.findAll().collectList().block();
        assertThat(pollOptionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(pollOptionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdatePollOptionWithPatch() throws Exception {
        // Initialize the database
        pollOptionRepository.save(pollOption).block();

        int databaseSizeBeforeUpdate = pollOptionRepository.findAll().collectList().block().size();

        // Update the pollOption using partial update
        PollOption partialUpdatedPollOption = new PollOption();
        partialUpdatedPollOption.setId(pollOption.getId());

        partialUpdatedPollOption.voteCount(UPDATED_VOTE_COUNT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPollOption.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPollOption))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the PollOption in the database
        List<PollOption> pollOptionList = pollOptionRepository.findAll().collectList().block();
        assertThat(pollOptionList).hasSize(databaseSizeBeforeUpdate);
        PollOption testPollOption = pollOptionList.get(pollOptionList.size() - 1);
        assertThat(testPollOption.getOptionDescription()).isEqualTo(DEFAULT_OPTION_DESCRIPTION);
        assertThat(testPollOption.getVoteCount()).isEqualTo(UPDATED_VOTE_COUNT);
    }

    @Test
    void fullUpdatePollOptionWithPatch() throws Exception {
        // Initialize the database
        pollOptionRepository.save(pollOption).block();

        int databaseSizeBeforeUpdate = pollOptionRepository.findAll().collectList().block().size();

        // Update the pollOption using partial update
        PollOption partialUpdatedPollOption = new PollOption();
        partialUpdatedPollOption.setId(pollOption.getId());

        partialUpdatedPollOption.optionDescription(UPDATED_OPTION_DESCRIPTION).voteCount(UPDATED_VOTE_COUNT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPollOption.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPollOption))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the PollOption in the database
        List<PollOption> pollOptionList = pollOptionRepository.findAll().collectList().block();
        assertThat(pollOptionList).hasSize(databaseSizeBeforeUpdate);
        PollOption testPollOption = pollOptionList.get(pollOptionList.size() - 1);
        assertThat(testPollOption.getOptionDescription()).isEqualTo(UPDATED_OPTION_DESCRIPTION);
        assertThat(testPollOption.getVoteCount()).isEqualTo(UPDATED_VOTE_COUNT);
    }

    @Test
    void patchNonExistingPollOption() throws Exception {
        int databaseSizeBeforeUpdate = pollOptionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(pollOptionSearchRepository.findAll().collectList().block());
        pollOption.setId(longCount.incrementAndGet());

        // Create the PollOption
        PollOptionDTO pollOptionDTO = pollOptionMapper.toDto(pollOption);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, pollOptionDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(pollOptionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PollOption in the database
        List<PollOption> pollOptionList = pollOptionRepository.findAll().collectList().block();
        assertThat(pollOptionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(pollOptionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchPollOption() throws Exception {
        int databaseSizeBeforeUpdate = pollOptionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(pollOptionSearchRepository.findAll().collectList().block());
        pollOption.setId(longCount.incrementAndGet());

        // Create the PollOption
        PollOptionDTO pollOptionDTO = pollOptionMapper.toDto(pollOption);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(pollOptionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PollOption in the database
        List<PollOption> pollOptionList = pollOptionRepository.findAll().collectList().block();
        assertThat(pollOptionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(pollOptionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamPollOption() throws Exception {
        int databaseSizeBeforeUpdate = pollOptionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(pollOptionSearchRepository.findAll().collectList().block());
        pollOption.setId(longCount.incrementAndGet());

        // Create the PollOption
        PollOptionDTO pollOptionDTO = pollOptionMapper.toDto(pollOption);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(pollOptionDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the PollOption in the database
        List<PollOption> pollOptionList = pollOptionRepository.findAll().collectList().block();
        assertThat(pollOptionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(pollOptionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deletePollOption() {
        // Initialize the database
        pollOptionRepository.save(pollOption).block();
        pollOptionRepository.save(pollOption).block();
        pollOptionSearchRepository.save(pollOption).block();

        int databaseSizeBeforeDelete = pollOptionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(pollOptionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the pollOption
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, pollOption.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<PollOption> pollOptionList = pollOptionRepository.findAll().collectList().block();
        assertThat(pollOptionList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(pollOptionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchPollOption() {
        // Initialize the database
        pollOption = pollOptionRepository.save(pollOption).block();
        pollOptionSearchRepository.save(pollOption).block();

        // Search the pollOption
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + pollOption.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(pollOption.getId().intValue()))
            .jsonPath("$.[*].optionDescription")
            .value(hasItem(DEFAULT_OPTION_DESCRIPTION.toString()))
            .jsonPath("$.[*].voteCount")
            .value(hasItem(DEFAULT_VOTE_COUNT));
    }
}
