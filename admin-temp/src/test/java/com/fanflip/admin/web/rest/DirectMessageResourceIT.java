package com.monsterdam.admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.monsterdam.admin.IntegrationTest;
import com.monsterdam.admin.domain.DirectMessage;
import com.monsterdam.admin.domain.UserProfile;
import com.monsterdam.admin.repository.DirectMessageRepository;
import com.monsterdam.admin.repository.EntityManager;
import com.monsterdam.admin.repository.search.DirectMessageSearchRepository;
import com.monsterdam.admin.service.DirectMessageService;
import com.monsterdam.admin.service.dto.DirectMessageDTO;
import com.monsterdam.admin.service.mapper.DirectMessageMapper;
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
 * Integration tests for the {@link DirectMessageResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class DirectMessageResourceIT {

    private static final String DEFAULT_MESSAGE_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_MESSAGE_CONTENT = "BBBBBBBBBB";

    private static final Instant DEFAULT_READ_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_READ_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_LIKE_COUNT = 1L;
    private static final Long UPDATED_LIKE_COUNT = 2L;

    private static final Boolean DEFAULT_IS_HIDDEN = false;
    private static final Boolean UPDATED_IS_HIDDEN = true;

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

    private static final String ENTITY_API_URL = "/api/direct-messages";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/direct-messages/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DirectMessageRepository directMessageRepository;

    @Mock
    private DirectMessageRepository directMessageRepositoryMock;

    @Autowired
    private DirectMessageMapper directMessageMapper;

    @Mock
    private DirectMessageService directMessageServiceMock;

    @Autowired
    private DirectMessageSearchRepository directMessageSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private DirectMessage directMessage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DirectMessage createEntity(EntityManager em) {
        DirectMessage directMessage = new DirectMessage()
            .messageContent(DEFAULT_MESSAGE_CONTENT)
            .readDate(DEFAULT_READ_DATE)
            .likeCount(DEFAULT_LIKE_COUNT)
            .isHidden(DEFAULT_IS_HIDDEN)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .isDeleted(DEFAULT_IS_DELETED);
        // Add required entity
        UserProfile userProfile;
        userProfile = em.insert(UserProfileResourceIT.createEntity(em)).block();
        directMessage.setUser(userProfile);
        return directMessage;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DirectMessage createUpdatedEntity(EntityManager em) {
        DirectMessage directMessage = new DirectMessage()
            .messageContent(UPDATED_MESSAGE_CONTENT)
            .readDate(UPDATED_READ_DATE)
            .likeCount(UPDATED_LIKE_COUNT)
            .isHidden(UPDATED_IS_HIDDEN)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        // Add required entity
        UserProfile userProfile;
        userProfile = em.insert(UserProfileResourceIT.createUpdatedEntity(em)).block();
        directMessage.setUser(userProfile);
        return directMessage;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(DirectMessage.class).block();
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
        directMessageSearchRepository.deleteAll().block();
        assertThat(directMessageSearchRepository.count().block()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        directMessage = createEntity(em);
    }

    @Test
    void createDirectMessage() throws Exception {
        int databaseSizeBeforeCreate = directMessageRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(directMessageSearchRepository.findAll().collectList().block());
        // Create the DirectMessage
        DirectMessageDTO directMessageDTO = directMessageMapper.toDto(directMessage);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(directMessageDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the DirectMessage in the database
        List<DirectMessage> directMessageList = directMessageRepository.findAll().collectList().block();
        assertThat(directMessageList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(directMessageSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        DirectMessage testDirectMessage = directMessageList.get(directMessageList.size() - 1);
        assertThat(testDirectMessage.getMessageContent()).isEqualTo(DEFAULT_MESSAGE_CONTENT);
        assertThat(testDirectMessage.getReadDate()).isEqualTo(DEFAULT_READ_DATE);
        assertThat(testDirectMessage.getLikeCount()).isEqualTo(DEFAULT_LIKE_COUNT);
        assertThat(testDirectMessage.getIsHidden()).isEqualTo(DEFAULT_IS_HIDDEN);
        assertThat(testDirectMessage.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testDirectMessage.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testDirectMessage.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testDirectMessage.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testDirectMessage.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    void createDirectMessageWithExistingId() throws Exception {
        // Create the DirectMessage with an existing ID
        directMessage.setId(1L);
        DirectMessageDTO directMessageDTO = directMessageMapper.toDto(directMessage);

        int databaseSizeBeforeCreate = directMessageRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(directMessageSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(directMessageDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DirectMessage in the database
        List<DirectMessage> directMessageList = directMessageRepository.findAll().collectList().block();
        assertThat(directMessageList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(directMessageSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = directMessageRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(directMessageSearchRepository.findAll().collectList().block());
        // set the field null
        directMessage.setCreatedDate(null);

        // Create the DirectMessage, which fails.
        DirectMessageDTO directMessageDTO = directMessageMapper.toDto(directMessage);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(directMessageDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<DirectMessage> directMessageList = directMessageRepository.findAll().collectList().block();
        assertThat(directMessageList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(directMessageSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = directMessageRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(directMessageSearchRepository.findAll().collectList().block());
        // set the field null
        directMessage.setIsDeleted(null);

        // Create the DirectMessage, which fails.
        DirectMessageDTO directMessageDTO = directMessageMapper.toDto(directMessage);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(directMessageDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<DirectMessage> directMessageList = directMessageRepository.findAll().collectList().block();
        assertThat(directMessageList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(directMessageSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllDirectMessages() {
        // Initialize the database
        directMessageRepository.save(directMessage).block();

        // Get all the directMessageList
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
            .value(hasItem(directMessage.getId().intValue()))
            .jsonPath("$.[*].messageContent")
            .value(hasItem(DEFAULT_MESSAGE_CONTENT.toString()))
            .jsonPath("$.[*].readDate")
            .value(hasItem(DEFAULT_READ_DATE.toString()))
            .jsonPath("$.[*].likeCount")
            .value(hasItem(DEFAULT_LIKE_COUNT.intValue()))
            .jsonPath("$.[*].isHidden")
            .value(hasItem(DEFAULT_IS_HIDDEN.booleanValue()))
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
    void getAllDirectMessagesWithEagerRelationshipsIsEnabled() {
        when(directMessageServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(directMessageServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDirectMessagesWithEagerRelationshipsIsNotEnabled() {
        when(directMessageServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(directMessageRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getDirectMessage() {
        // Initialize the database
        directMessageRepository.save(directMessage).block();

        // Get the directMessage
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, directMessage.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(directMessage.getId().intValue()))
            .jsonPath("$.messageContent")
            .value(is(DEFAULT_MESSAGE_CONTENT.toString()))
            .jsonPath("$.readDate")
            .value(is(DEFAULT_READ_DATE.toString()))
            .jsonPath("$.likeCount")
            .value(is(DEFAULT_LIKE_COUNT.intValue()))
            .jsonPath("$.isHidden")
            .value(is(DEFAULT_IS_HIDDEN.booleanValue()))
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
    void getNonExistingDirectMessage() {
        // Get the directMessage
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingDirectMessage() throws Exception {
        // Initialize the database
        directMessageRepository.save(directMessage).block();

        int databaseSizeBeforeUpdate = directMessageRepository.findAll().collectList().block().size();
        directMessageSearchRepository.save(directMessage).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(directMessageSearchRepository.findAll().collectList().block());

        // Update the directMessage
        DirectMessage updatedDirectMessage = directMessageRepository.findById(directMessage.getId()).block();
        updatedDirectMessage
            .messageContent(UPDATED_MESSAGE_CONTENT)
            .readDate(UPDATED_READ_DATE)
            .likeCount(UPDATED_LIKE_COUNT)
            .isHidden(UPDATED_IS_HIDDEN)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        DirectMessageDTO directMessageDTO = directMessageMapper.toDto(updatedDirectMessage);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, directMessageDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(directMessageDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the DirectMessage in the database
        List<DirectMessage> directMessageList = directMessageRepository.findAll().collectList().block();
        assertThat(directMessageList).hasSize(databaseSizeBeforeUpdate);
        DirectMessage testDirectMessage = directMessageList.get(directMessageList.size() - 1);
        assertThat(testDirectMessage.getMessageContent()).isEqualTo(UPDATED_MESSAGE_CONTENT);
        assertThat(testDirectMessage.getReadDate()).isEqualTo(UPDATED_READ_DATE);
        assertThat(testDirectMessage.getLikeCount()).isEqualTo(UPDATED_LIKE_COUNT);
        assertThat(testDirectMessage.getIsHidden()).isEqualTo(UPDATED_IS_HIDDEN);
        assertThat(testDirectMessage.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testDirectMessage.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testDirectMessage.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testDirectMessage.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testDirectMessage.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(directMessageSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<DirectMessage> directMessageSearchList = IterableUtils.toList(
                    directMessageSearchRepository.findAll().collectList().block()
                );
                DirectMessage testDirectMessageSearch = directMessageSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testDirectMessageSearch.getMessageContent()).isEqualTo(UPDATED_MESSAGE_CONTENT);
                assertThat(testDirectMessageSearch.getReadDate()).isEqualTo(UPDATED_READ_DATE);
                assertThat(testDirectMessageSearch.getLikeCount()).isEqualTo(UPDATED_LIKE_COUNT);
                assertThat(testDirectMessageSearch.getIsHidden()).isEqualTo(UPDATED_IS_HIDDEN);
                assertThat(testDirectMessageSearch.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
                assertThat(testDirectMessageSearch.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
                assertThat(testDirectMessageSearch.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
                assertThat(testDirectMessageSearch.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
                assertThat(testDirectMessageSearch.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
            });
    }

    @Test
    void putNonExistingDirectMessage() throws Exception {
        int databaseSizeBeforeUpdate = directMessageRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(directMessageSearchRepository.findAll().collectList().block());
        directMessage.setId(longCount.incrementAndGet());

        // Create the DirectMessage
        DirectMessageDTO directMessageDTO = directMessageMapper.toDto(directMessage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, directMessageDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(directMessageDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DirectMessage in the database
        List<DirectMessage> directMessageList = directMessageRepository.findAll().collectList().block();
        assertThat(directMessageList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(directMessageSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchDirectMessage() throws Exception {
        int databaseSizeBeforeUpdate = directMessageRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(directMessageSearchRepository.findAll().collectList().block());
        directMessage.setId(longCount.incrementAndGet());

        // Create the DirectMessage
        DirectMessageDTO directMessageDTO = directMessageMapper.toDto(directMessage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(directMessageDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DirectMessage in the database
        List<DirectMessage> directMessageList = directMessageRepository.findAll().collectList().block();
        assertThat(directMessageList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(directMessageSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamDirectMessage() throws Exception {
        int databaseSizeBeforeUpdate = directMessageRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(directMessageSearchRepository.findAll().collectList().block());
        directMessage.setId(longCount.incrementAndGet());

        // Create the DirectMessage
        DirectMessageDTO directMessageDTO = directMessageMapper.toDto(directMessage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(directMessageDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the DirectMessage in the database
        List<DirectMessage> directMessageList = directMessageRepository.findAll().collectList().block();
        assertThat(directMessageList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(directMessageSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateDirectMessageWithPatch() throws Exception {
        // Initialize the database
        directMessageRepository.save(directMessage).block();

        int databaseSizeBeforeUpdate = directMessageRepository.findAll().collectList().block().size();

        // Update the directMessage using partial update
        DirectMessage partialUpdatedDirectMessage = new DirectMessage();
        partialUpdatedDirectMessage.setId(directMessage.getId());

        partialUpdatedDirectMessage
            .messageContent(UPDATED_MESSAGE_CONTENT)
            .likeCount(UPDATED_LIKE_COUNT)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedDirectMessage.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedDirectMessage))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the DirectMessage in the database
        List<DirectMessage> directMessageList = directMessageRepository.findAll().collectList().block();
        assertThat(directMessageList).hasSize(databaseSizeBeforeUpdate);
        DirectMessage testDirectMessage = directMessageList.get(directMessageList.size() - 1);
        assertThat(testDirectMessage.getMessageContent()).isEqualTo(UPDATED_MESSAGE_CONTENT);
        assertThat(testDirectMessage.getReadDate()).isEqualTo(DEFAULT_READ_DATE);
        assertThat(testDirectMessage.getLikeCount()).isEqualTo(UPDATED_LIKE_COUNT);
        assertThat(testDirectMessage.getIsHidden()).isEqualTo(DEFAULT_IS_HIDDEN);
        assertThat(testDirectMessage.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testDirectMessage.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testDirectMessage.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testDirectMessage.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testDirectMessage.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    void fullUpdateDirectMessageWithPatch() throws Exception {
        // Initialize the database
        directMessageRepository.save(directMessage).block();

        int databaseSizeBeforeUpdate = directMessageRepository.findAll().collectList().block().size();

        // Update the directMessage using partial update
        DirectMessage partialUpdatedDirectMessage = new DirectMessage();
        partialUpdatedDirectMessage.setId(directMessage.getId());

        partialUpdatedDirectMessage
            .messageContent(UPDATED_MESSAGE_CONTENT)
            .readDate(UPDATED_READ_DATE)
            .likeCount(UPDATED_LIKE_COUNT)
            .isHidden(UPDATED_IS_HIDDEN)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedDirectMessage.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedDirectMessage))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the DirectMessage in the database
        List<DirectMessage> directMessageList = directMessageRepository.findAll().collectList().block();
        assertThat(directMessageList).hasSize(databaseSizeBeforeUpdate);
        DirectMessage testDirectMessage = directMessageList.get(directMessageList.size() - 1);
        assertThat(testDirectMessage.getMessageContent()).isEqualTo(UPDATED_MESSAGE_CONTENT);
        assertThat(testDirectMessage.getReadDate()).isEqualTo(UPDATED_READ_DATE);
        assertThat(testDirectMessage.getLikeCount()).isEqualTo(UPDATED_LIKE_COUNT);
        assertThat(testDirectMessage.getIsHidden()).isEqualTo(UPDATED_IS_HIDDEN);
        assertThat(testDirectMessage.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testDirectMessage.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testDirectMessage.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testDirectMessage.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testDirectMessage.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    void patchNonExistingDirectMessage() throws Exception {
        int databaseSizeBeforeUpdate = directMessageRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(directMessageSearchRepository.findAll().collectList().block());
        directMessage.setId(longCount.incrementAndGet());

        // Create the DirectMessage
        DirectMessageDTO directMessageDTO = directMessageMapper.toDto(directMessage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, directMessageDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(directMessageDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DirectMessage in the database
        List<DirectMessage> directMessageList = directMessageRepository.findAll().collectList().block();
        assertThat(directMessageList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(directMessageSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchDirectMessage() throws Exception {
        int databaseSizeBeforeUpdate = directMessageRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(directMessageSearchRepository.findAll().collectList().block());
        directMessage.setId(longCount.incrementAndGet());

        // Create the DirectMessage
        DirectMessageDTO directMessageDTO = directMessageMapper.toDto(directMessage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(directMessageDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DirectMessage in the database
        List<DirectMessage> directMessageList = directMessageRepository.findAll().collectList().block();
        assertThat(directMessageList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(directMessageSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamDirectMessage() throws Exception {
        int databaseSizeBeforeUpdate = directMessageRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(directMessageSearchRepository.findAll().collectList().block());
        directMessage.setId(longCount.incrementAndGet());

        // Create the DirectMessage
        DirectMessageDTO directMessageDTO = directMessageMapper.toDto(directMessage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(directMessageDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the DirectMessage in the database
        List<DirectMessage> directMessageList = directMessageRepository.findAll().collectList().block();
        assertThat(directMessageList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(directMessageSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteDirectMessage() {
        // Initialize the database
        directMessageRepository.save(directMessage).block();
        directMessageRepository.save(directMessage).block();
        directMessageSearchRepository.save(directMessage).block();

        int databaseSizeBeforeDelete = directMessageRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(directMessageSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the directMessage
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, directMessage.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<DirectMessage> directMessageList = directMessageRepository.findAll().collectList().block();
        assertThat(directMessageList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(directMessageSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchDirectMessage() {
        // Initialize the database
        directMessage = directMessageRepository.save(directMessage).block();
        directMessageSearchRepository.save(directMessage).block();

        // Search the directMessage
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + directMessage.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(directMessage.getId().intValue()))
            .jsonPath("$.[*].messageContent")
            .value(hasItem(DEFAULT_MESSAGE_CONTENT.toString()))
            .jsonPath("$.[*].readDate")
            .value(hasItem(DEFAULT_READ_DATE.toString()))
            .jsonPath("$.[*].likeCount")
            .value(hasItem(DEFAULT_LIKE_COUNT.intValue()))
            .jsonPath("$.[*].isHidden")
            .value(hasItem(DEFAULT_IS_HIDDEN.booleanValue()))
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
