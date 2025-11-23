package com.fanflip.admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.fanflip.admin.IntegrationTest;
import com.fanflip.admin.domain.AssistanceTicket;
import com.fanflip.admin.domain.enumeration.TicketStatus;
import com.fanflip.admin.domain.enumeration.TicketType;
import com.fanflip.admin.repository.AssistanceTicketRepository;
import com.fanflip.admin.repository.EntityManager;
import com.fanflip.admin.repository.search.AssistanceTicketSearchRepository;
import com.fanflip.admin.service.dto.AssistanceTicketDTO;
import com.fanflip.admin.service.mapper.AssistanceTicketMapper;
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
 * Integration tests for the {@link AssistanceTicketResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class AssistanceTicketResourceIT {

    private static final String DEFAULT_SUBJECT = "AAAAAAAAAA";
    private static final String UPDATED_SUBJECT = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final TicketStatus DEFAULT_STATUS = TicketStatus.OPEN;
    private static final TicketStatus UPDATED_STATUS = TicketStatus.ASSIGNED;

    private static final TicketType DEFAULT_TYPE = TicketType.ACCESS_ISSUE;
    private static final TicketType UPDATED_TYPE = TicketType.DOCUMENT_VERIFICATION;

    private static final Instant DEFAULT_OPENED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_OPENED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_CLOSED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CLOSED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_COMMENTS = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTS = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/assistance-tickets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/assistance-tickets/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AssistanceTicketRepository assistanceTicketRepository;

    @Autowired
    private AssistanceTicketMapper assistanceTicketMapper;

    @Autowired
    private AssistanceTicketSearchRepository assistanceTicketSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private AssistanceTicket assistanceTicket;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AssistanceTicket createEntity(EntityManager em) {
        AssistanceTicket assistanceTicket = new AssistanceTicket()
            .subject(DEFAULT_SUBJECT)
            .description(DEFAULT_DESCRIPTION)
            .status(DEFAULT_STATUS)
            .type(DEFAULT_TYPE)
            .openedAt(DEFAULT_OPENED_AT)
            .closedAt(DEFAULT_CLOSED_AT)
            .comments(DEFAULT_COMMENTS)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY);
        return assistanceTicket;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AssistanceTicket createUpdatedEntity(EntityManager em) {
        AssistanceTicket assistanceTicket = new AssistanceTicket()
            .subject(UPDATED_SUBJECT)
            .description(UPDATED_DESCRIPTION)
            .status(UPDATED_STATUS)
            .type(UPDATED_TYPE)
            .openedAt(UPDATED_OPENED_AT)
            .closedAt(UPDATED_CLOSED_AT)
            .comments(UPDATED_COMMENTS)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);
        return assistanceTicket;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(AssistanceTicket.class).block();
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
        assistanceTicketSearchRepository.deleteAll().block();
        assertThat(assistanceTicketSearchRepository.count().block()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        assistanceTicket = createEntity(em);
    }

    @Test
    void createAssistanceTicket() throws Exception {
        int databaseSizeBeforeCreate = assistanceTicketRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assistanceTicketSearchRepository.findAll().collectList().block());
        // Create the AssistanceTicket
        AssistanceTicketDTO assistanceTicketDTO = assistanceTicketMapper.toDto(assistanceTicket);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(assistanceTicketDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the AssistanceTicket in the database
        List<AssistanceTicket> assistanceTicketList = assistanceTicketRepository.findAll().collectList().block();
        assertThat(assistanceTicketList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(assistanceTicketSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        AssistanceTicket testAssistanceTicket = assistanceTicketList.get(assistanceTicketList.size() - 1);
        assertThat(testAssistanceTicket.getSubject()).isEqualTo(DEFAULT_SUBJECT);
        assertThat(testAssistanceTicket.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testAssistanceTicket.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testAssistanceTicket.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testAssistanceTicket.getOpenedAt()).isEqualTo(DEFAULT_OPENED_AT);
        assertThat(testAssistanceTicket.getClosedAt()).isEqualTo(DEFAULT_CLOSED_AT);
        assertThat(testAssistanceTicket.getComments()).isEqualTo(DEFAULT_COMMENTS);
        assertThat(testAssistanceTicket.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testAssistanceTicket.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testAssistanceTicket.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testAssistanceTicket.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
    }

    @Test
    void createAssistanceTicketWithExistingId() throws Exception {
        // Create the AssistanceTicket with an existing ID
        assistanceTicket.setId(1L);
        AssistanceTicketDTO assistanceTicketDTO = assistanceTicketMapper.toDto(assistanceTicket);

        int databaseSizeBeforeCreate = assistanceTicketRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assistanceTicketSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(assistanceTicketDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AssistanceTicket in the database
        List<AssistanceTicket> assistanceTicketList = assistanceTicketRepository.findAll().collectList().block();
        assertThat(assistanceTicketList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(assistanceTicketSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkSubjectIsRequired() throws Exception {
        int databaseSizeBeforeTest = assistanceTicketRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assistanceTicketSearchRepository.findAll().collectList().block());
        // set the field null
        assistanceTicket.setSubject(null);

        // Create the AssistanceTicket, which fails.
        AssistanceTicketDTO assistanceTicketDTO = assistanceTicketMapper.toDto(assistanceTicket);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(assistanceTicketDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<AssistanceTicket> assistanceTicketList = assistanceTicketRepository.findAll().collectList().block();
        assertThat(assistanceTicketList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(assistanceTicketSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = assistanceTicketRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assistanceTicketSearchRepository.findAll().collectList().block());
        // set the field null
        assistanceTicket.setDescription(null);

        // Create the AssistanceTicket, which fails.
        AssistanceTicketDTO assistanceTicketDTO = assistanceTicketMapper.toDto(assistanceTicket);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(assistanceTicketDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<AssistanceTicket> assistanceTicketList = assistanceTicketRepository.findAll().collectList().block();
        assertThat(assistanceTicketList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(assistanceTicketSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = assistanceTicketRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assistanceTicketSearchRepository.findAll().collectList().block());
        // set the field null
        assistanceTicket.setStatus(null);

        // Create the AssistanceTicket, which fails.
        AssistanceTicketDTO assistanceTicketDTO = assistanceTicketMapper.toDto(assistanceTicket);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(assistanceTicketDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<AssistanceTicket> assistanceTicketList = assistanceTicketRepository.findAll().collectList().block();
        assertThat(assistanceTicketList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(assistanceTicketSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = assistanceTicketRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assistanceTicketSearchRepository.findAll().collectList().block());
        // set the field null
        assistanceTicket.setType(null);

        // Create the AssistanceTicket, which fails.
        AssistanceTicketDTO assistanceTicketDTO = assistanceTicketMapper.toDto(assistanceTicket);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(assistanceTicketDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<AssistanceTicket> assistanceTicketList = assistanceTicketRepository.findAll().collectList().block();
        assertThat(assistanceTicketList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(assistanceTicketSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = assistanceTicketRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assistanceTicketSearchRepository.findAll().collectList().block());
        // set the field null
        assistanceTicket.setCreatedDate(null);

        // Create the AssistanceTicket, which fails.
        AssistanceTicketDTO assistanceTicketDTO = assistanceTicketMapper.toDto(assistanceTicket);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(assistanceTicketDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<AssistanceTicket> assistanceTicketList = assistanceTicketRepository.findAll().collectList().block();
        assertThat(assistanceTicketList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(assistanceTicketSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllAssistanceTickets() {
        // Initialize the database
        assistanceTicketRepository.save(assistanceTicket).block();

        // Get all the assistanceTicketList
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
            .value(hasItem(assistanceTicket.getId().intValue()))
            .jsonPath("$.[*].subject")
            .value(hasItem(DEFAULT_SUBJECT))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION))
            .jsonPath("$.[*].status")
            .value(hasItem(DEFAULT_STATUS.toString()))
            .jsonPath("$.[*].type")
            .value(hasItem(DEFAULT_TYPE.toString()))
            .jsonPath("$.[*].openedAt")
            .value(hasItem(DEFAULT_OPENED_AT.toString()))
            .jsonPath("$.[*].closedAt")
            .value(hasItem(DEFAULT_CLOSED_AT.toString()))
            .jsonPath("$.[*].comments")
            .value(hasItem(DEFAULT_COMMENTS))
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
    void getAssistanceTicket() {
        // Initialize the database
        assistanceTicketRepository.save(assistanceTicket).block();

        // Get the assistanceTicket
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, assistanceTicket.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(assistanceTicket.getId().intValue()))
            .jsonPath("$.subject")
            .value(is(DEFAULT_SUBJECT))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION))
            .jsonPath("$.status")
            .value(is(DEFAULT_STATUS.toString()))
            .jsonPath("$.type")
            .value(is(DEFAULT_TYPE.toString()))
            .jsonPath("$.openedAt")
            .value(is(DEFAULT_OPENED_AT.toString()))
            .jsonPath("$.closedAt")
            .value(is(DEFAULT_CLOSED_AT.toString()))
            .jsonPath("$.comments")
            .value(is(DEFAULT_COMMENTS))
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
    void getNonExistingAssistanceTicket() {
        // Get the assistanceTicket
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingAssistanceTicket() throws Exception {
        // Initialize the database
        assistanceTicketRepository.save(assistanceTicket).block();

        int databaseSizeBeforeUpdate = assistanceTicketRepository.findAll().collectList().block().size();
        assistanceTicketSearchRepository.save(assistanceTicket).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assistanceTicketSearchRepository.findAll().collectList().block());

        // Update the assistanceTicket
        AssistanceTicket updatedAssistanceTicket = assistanceTicketRepository.findById(assistanceTicket.getId()).block();
        updatedAssistanceTicket
            .subject(UPDATED_SUBJECT)
            .description(UPDATED_DESCRIPTION)
            .status(UPDATED_STATUS)
            .type(UPDATED_TYPE)
            .openedAt(UPDATED_OPENED_AT)
            .closedAt(UPDATED_CLOSED_AT)
            .comments(UPDATED_COMMENTS)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);
        AssistanceTicketDTO assistanceTicketDTO = assistanceTicketMapper.toDto(updatedAssistanceTicket);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, assistanceTicketDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(assistanceTicketDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the AssistanceTicket in the database
        List<AssistanceTicket> assistanceTicketList = assistanceTicketRepository.findAll().collectList().block();
        assertThat(assistanceTicketList).hasSize(databaseSizeBeforeUpdate);
        AssistanceTicket testAssistanceTicket = assistanceTicketList.get(assistanceTicketList.size() - 1);
        assertThat(testAssistanceTicket.getSubject()).isEqualTo(UPDATED_SUBJECT);
        assertThat(testAssistanceTicket.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAssistanceTicket.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testAssistanceTicket.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testAssistanceTicket.getOpenedAt()).isEqualTo(UPDATED_OPENED_AT);
        assertThat(testAssistanceTicket.getClosedAt()).isEqualTo(UPDATED_CLOSED_AT);
        assertThat(testAssistanceTicket.getComments()).isEqualTo(UPDATED_COMMENTS);
        assertThat(testAssistanceTicket.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testAssistanceTicket.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testAssistanceTicket.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testAssistanceTicket.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(assistanceTicketSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<AssistanceTicket> assistanceTicketSearchList = IterableUtils.toList(
                    assistanceTicketSearchRepository.findAll().collectList().block()
                );
                AssistanceTicket testAssistanceTicketSearch = assistanceTicketSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testAssistanceTicketSearch.getSubject()).isEqualTo(UPDATED_SUBJECT);
                assertThat(testAssistanceTicketSearch.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
                assertThat(testAssistanceTicketSearch.getStatus()).isEqualTo(UPDATED_STATUS);
                assertThat(testAssistanceTicketSearch.getType()).isEqualTo(UPDATED_TYPE);
                assertThat(testAssistanceTicketSearch.getOpenedAt()).isEqualTo(UPDATED_OPENED_AT);
                assertThat(testAssistanceTicketSearch.getClosedAt()).isEqualTo(UPDATED_CLOSED_AT);
                assertThat(testAssistanceTicketSearch.getComments()).isEqualTo(UPDATED_COMMENTS);
                assertThat(testAssistanceTicketSearch.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
                assertThat(testAssistanceTicketSearch.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
                assertThat(testAssistanceTicketSearch.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
                assertThat(testAssistanceTicketSearch.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
            });
    }

    @Test
    void putNonExistingAssistanceTicket() throws Exception {
        int databaseSizeBeforeUpdate = assistanceTicketRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assistanceTicketSearchRepository.findAll().collectList().block());
        assistanceTicket.setId(longCount.incrementAndGet());

        // Create the AssistanceTicket
        AssistanceTicketDTO assistanceTicketDTO = assistanceTicketMapper.toDto(assistanceTicket);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, assistanceTicketDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(assistanceTicketDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AssistanceTicket in the database
        List<AssistanceTicket> assistanceTicketList = assistanceTicketRepository.findAll().collectList().block();
        assertThat(assistanceTicketList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(assistanceTicketSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchAssistanceTicket() throws Exception {
        int databaseSizeBeforeUpdate = assistanceTicketRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assistanceTicketSearchRepository.findAll().collectList().block());
        assistanceTicket.setId(longCount.incrementAndGet());

        // Create the AssistanceTicket
        AssistanceTicketDTO assistanceTicketDTO = assistanceTicketMapper.toDto(assistanceTicket);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(assistanceTicketDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AssistanceTicket in the database
        List<AssistanceTicket> assistanceTicketList = assistanceTicketRepository.findAll().collectList().block();
        assertThat(assistanceTicketList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(assistanceTicketSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamAssistanceTicket() throws Exception {
        int databaseSizeBeforeUpdate = assistanceTicketRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assistanceTicketSearchRepository.findAll().collectList().block());
        assistanceTicket.setId(longCount.incrementAndGet());

        // Create the AssistanceTicket
        AssistanceTicketDTO assistanceTicketDTO = assistanceTicketMapper.toDto(assistanceTicket);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(assistanceTicketDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the AssistanceTicket in the database
        List<AssistanceTicket> assistanceTicketList = assistanceTicketRepository.findAll().collectList().block();
        assertThat(assistanceTicketList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(assistanceTicketSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateAssistanceTicketWithPatch() throws Exception {
        // Initialize the database
        assistanceTicketRepository.save(assistanceTicket).block();

        int databaseSizeBeforeUpdate = assistanceTicketRepository.findAll().collectList().block().size();

        // Update the assistanceTicket using partial update
        AssistanceTicket partialUpdatedAssistanceTicket = new AssistanceTicket();
        partialUpdatedAssistanceTicket.setId(assistanceTicket.getId());

        partialUpdatedAssistanceTicket
            .description(UPDATED_DESCRIPTION)
            .status(UPDATED_STATUS)
            .type(UPDATED_TYPE)
            .openedAt(UPDATED_OPENED_AT)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAssistanceTicket.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedAssistanceTicket))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the AssistanceTicket in the database
        List<AssistanceTicket> assistanceTicketList = assistanceTicketRepository.findAll().collectList().block();
        assertThat(assistanceTicketList).hasSize(databaseSizeBeforeUpdate);
        AssistanceTicket testAssistanceTicket = assistanceTicketList.get(assistanceTicketList.size() - 1);
        assertThat(testAssistanceTicket.getSubject()).isEqualTo(DEFAULT_SUBJECT);
        assertThat(testAssistanceTicket.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAssistanceTicket.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testAssistanceTicket.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testAssistanceTicket.getOpenedAt()).isEqualTo(UPDATED_OPENED_AT);
        assertThat(testAssistanceTicket.getClosedAt()).isEqualTo(DEFAULT_CLOSED_AT);
        assertThat(testAssistanceTicket.getComments()).isEqualTo(DEFAULT_COMMENTS);
        assertThat(testAssistanceTicket.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testAssistanceTicket.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testAssistanceTicket.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testAssistanceTicket.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    void fullUpdateAssistanceTicketWithPatch() throws Exception {
        // Initialize the database
        assistanceTicketRepository.save(assistanceTicket).block();

        int databaseSizeBeforeUpdate = assistanceTicketRepository.findAll().collectList().block().size();

        // Update the assistanceTicket using partial update
        AssistanceTicket partialUpdatedAssistanceTicket = new AssistanceTicket();
        partialUpdatedAssistanceTicket.setId(assistanceTicket.getId());

        partialUpdatedAssistanceTicket
            .subject(UPDATED_SUBJECT)
            .description(UPDATED_DESCRIPTION)
            .status(UPDATED_STATUS)
            .type(UPDATED_TYPE)
            .openedAt(UPDATED_OPENED_AT)
            .closedAt(UPDATED_CLOSED_AT)
            .comments(UPDATED_COMMENTS)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAssistanceTicket.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedAssistanceTicket))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the AssistanceTicket in the database
        List<AssistanceTicket> assistanceTicketList = assistanceTicketRepository.findAll().collectList().block();
        assertThat(assistanceTicketList).hasSize(databaseSizeBeforeUpdate);
        AssistanceTicket testAssistanceTicket = assistanceTicketList.get(assistanceTicketList.size() - 1);
        assertThat(testAssistanceTicket.getSubject()).isEqualTo(UPDATED_SUBJECT);
        assertThat(testAssistanceTicket.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAssistanceTicket.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testAssistanceTicket.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testAssistanceTicket.getOpenedAt()).isEqualTo(UPDATED_OPENED_AT);
        assertThat(testAssistanceTicket.getClosedAt()).isEqualTo(UPDATED_CLOSED_AT);
        assertThat(testAssistanceTicket.getComments()).isEqualTo(UPDATED_COMMENTS);
        assertThat(testAssistanceTicket.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testAssistanceTicket.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testAssistanceTicket.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testAssistanceTicket.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    void patchNonExistingAssistanceTicket() throws Exception {
        int databaseSizeBeforeUpdate = assistanceTicketRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assistanceTicketSearchRepository.findAll().collectList().block());
        assistanceTicket.setId(longCount.incrementAndGet());

        // Create the AssistanceTicket
        AssistanceTicketDTO assistanceTicketDTO = assistanceTicketMapper.toDto(assistanceTicket);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, assistanceTicketDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(assistanceTicketDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AssistanceTicket in the database
        List<AssistanceTicket> assistanceTicketList = assistanceTicketRepository.findAll().collectList().block();
        assertThat(assistanceTicketList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(assistanceTicketSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchAssistanceTicket() throws Exception {
        int databaseSizeBeforeUpdate = assistanceTicketRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assistanceTicketSearchRepository.findAll().collectList().block());
        assistanceTicket.setId(longCount.incrementAndGet());

        // Create the AssistanceTicket
        AssistanceTicketDTO assistanceTicketDTO = assistanceTicketMapper.toDto(assistanceTicket);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(assistanceTicketDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AssistanceTicket in the database
        List<AssistanceTicket> assistanceTicketList = assistanceTicketRepository.findAll().collectList().block();
        assertThat(assistanceTicketList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(assistanceTicketSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamAssistanceTicket() throws Exception {
        int databaseSizeBeforeUpdate = assistanceTicketRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assistanceTicketSearchRepository.findAll().collectList().block());
        assistanceTicket.setId(longCount.incrementAndGet());

        // Create the AssistanceTicket
        AssistanceTicketDTO assistanceTicketDTO = assistanceTicketMapper.toDto(assistanceTicket);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(assistanceTicketDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the AssistanceTicket in the database
        List<AssistanceTicket> assistanceTicketList = assistanceTicketRepository.findAll().collectList().block();
        assertThat(assistanceTicketList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(assistanceTicketSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteAssistanceTicket() {
        // Initialize the database
        assistanceTicketRepository.save(assistanceTicket).block();
        assistanceTicketRepository.save(assistanceTicket).block();
        assistanceTicketSearchRepository.save(assistanceTicket).block();

        int databaseSizeBeforeDelete = assistanceTicketRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assistanceTicketSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the assistanceTicket
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, assistanceTicket.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<AssistanceTicket> assistanceTicketList = assistanceTicketRepository.findAll().collectList().block();
        assertThat(assistanceTicketList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(assistanceTicketSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchAssistanceTicket() {
        // Initialize the database
        assistanceTicket = assistanceTicketRepository.save(assistanceTicket).block();
        assistanceTicketSearchRepository.save(assistanceTicket).block();

        // Search the assistanceTicket
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + assistanceTicket.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(assistanceTicket.getId().intValue()))
            .jsonPath("$.[*].subject")
            .value(hasItem(DEFAULT_SUBJECT))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION))
            .jsonPath("$.[*].status")
            .value(hasItem(DEFAULT_STATUS.toString()))
            .jsonPath("$.[*].type")
            .value(hasItem(DEFAULT_TYPE.toString()))
            .jsonPath("$.[*].openedAt")
            .value(hasItem(DEFAULT_OPENED_AT.toString()))
            .jsonPath("$.[*].closedAt")
            .value(hasItem(DEFAULT_CLOSED_AT.toString()))
            .jsonPath("$.[*].comments")
            .value(hasItem(DEFAULT_COMMENTS))
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
