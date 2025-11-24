package com.monsterdam.admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.monsterdam.admin.IntegrationTest;
import com.monsterdam.admin.domain.ChatRoom;
import com.monsterdam.admin.domain.UserProfile;
import com.monsterdam.admin.repository.ChatRoomRepository;
import com.monsterdam.admin.repository.EntityManager;
import com.monsterdam.admin.repository.search.ChatRoomSearchRepository;
import com.monsterdam.admin.service.ChatRoomService;
import com.monsterdam.admin.service.dto.ChatRoomDTO;
import com.monsterdam.admin.service.mapper.ChatRoomMapper;
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
 * Integration tests for the {@link ChatRoomResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ChatRoomResourceIT {

    private static final String DEFAULT_LAST_ACTION = "AAAAAAAAAA";
    private static final String UPDATED_LAST_ACTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_CONNECTION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_CONNECTION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_MUTED = false;
    private static final Boolean UPDATED_MUTED = true;

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

    private static final String ENTITY_API_URL = "/api/chat-rooms";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/chat-rooms/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Mock
    private ChatRoomRepository chatRoomRepositoryMock;

    @Autowired
    private ChatRoomMapper chatRoomMapper;

    @Mock
    private ChatRoomService chatRoomServiceMock;

    @Autowired
    private ChatRoomSearchRepository chatRoomSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private ChatRoom chatRoom;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChatRoom createEntity(EntityManager em) {
        ChatRoom chatRoom = new ChatRoom()
            .lastAction(DEFAULT_LAST_ACTION)
            .lastConnectionDate(DEFAULT_LAST_CONNECTION_DATE)
            .muted(DEFAULT_MUTED)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .isDeleted(DEFAULT_IS_DELETED);
        // Add required entity
        UserProfile userProfile;
        userProfile = em.insert(UserProfileResourceIT.createEntity(em)).block();
        chatRoom.setUser(userProfile);
        return chatRoom;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChatRoom createUpdatedEntity(EntityManager em) {
        ChatRoom chatRoom = new ChatRoom()
            .lastAction(UPDATED_LAST_ACTION)
            .lastConnectionDate(UPDATED_LAST_CONNECTION_DATE)
            .muted(UPDATED_MUTED)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        // Add required entity
        UserProfile userProfile;
        userProfile = em.insert(UserProfileResourceIT.createUpdatedEntity(em)).block();
        chatRoom.setUser(userProfile);
        return chatRoom;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll("rel_chat_room__sent_messages").block();
            em.deleteAll(ChatRoom.class).block();
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
        chatRoomSearchRepository.deleteAll().block();
        assertThat(chatRoomSearchRepository.count().block()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        chatRoom = createEntity(em);
    }

    @Test
    void createChatRoom() throws Exception {
        int databaseSizeBeforeCreate = chatRoomRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(chatRoomSearchRepository.findAll().collectList().block());
        // Create the ChatRoom
        ChatRoomDTO chatRoomDTO = chatRoomMapper.toDto(chatRoom);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(chatRoomDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the ChatRoom in the database
        List<ChatRoom> chatRoomList = chatRoomRepository.findAll().collectList().block();
        assertThat(chatRoomList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(chatRoomSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        ChatRoom testChatRoom = chatRoomList.get(chatRoomList.size() - 1);
        assertThat(testChatRoom.getLastAction()).isEqualTo(DEFAULT_LAST_ACTION);
        assertThat(testChatRoom.getLastConnectionDate()).isEqualTo(DEFAULT_LAST_CONNECTION_DATE);
        assertThat(testChatRoom.getMuted()).isEqualTo(DEFAULT_MUTED);
        assertThat(testChatRoom.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testChatRoom.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testChatRoom.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testChatRoom.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testChatRoom.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    void createChatRoomWithExistingId() throws Exception {
        // Create the ChatRoom with an existing ID
        chatRoom.setId(1L);
        ChatRoomDTO chatRoomDTO = chatRoomMapper.toDto(chatRoom);

        int databaseSizeBeforeCreate = chatRoomRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(chatRoomSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(chatRoomDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ChatRoom in the database
        List<ChatRoom> chatRoomList = chatRoomRepository.findAll().collectList().block();
        assertThat(chatRoomList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(chatRoomSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = chatRoomRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(chatRoomSearchRepository.findAll().collectList().block());
        // set the field null
        chatRoom.setCreatedDate(null);

        // Create the ChatRoom, which fails.
        ChatRoomDTO chatRoomDTO = chatRoomMapper.toDto(chatRoom);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(chatRoomDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ChatRoom> chatRoomList = chatRoomRepository.findAll().collectList().block();
        assertThat(chatRoomList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(chatRoomSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = chatRoomRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(chatRoomSearchRepository.findAll().collectList().block());
        // set the field null
        chatRoom.setIsDeleted(null);

        // Create the ChatRoom, which fails.
        ChatRoomDTO chatRoomDTO = chatRoomMapper.toDto(chatRoom);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(chatRoomDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ChatRoom> chatRoomList = chatRoomRepository.findAll().collectList().block();
        assertThat(chatRoomList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(chatRoomSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllChatRooms() {
        // Initialize the database
        chatRoomRepository.save(chatRoom).block();

        // Get all the chatRoomList
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
            .value(hasItem(chatRoom.getId().intValue()))
            .jsonPath("$.[*].lastAction")
            .value(hasItem(DEFAULT_LAST_ACTION))
            .jsonPath("$.[*].lastConnectionDate")
            .value(hasItem(DEFAULT_LAST_CONNECTION_DATE.toString()))
            .jsonPath("$.[*].muted")
            .value(hasItem(DEFAULT_MUTED.booleanValue()))
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
    void getAllChatRoomsWithEagerRelationshipsIsEnabled() {
        when(chatRoomServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(chatRoomServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllChatRoomsWithEagerRelationshipsIsNotEnabled() {
        when(chatRoomServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(chatRoomRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getChatRoom() {
        // Initialize the database
        chatRoomRepository.save(chatRoom).block();

        // Get the chatRoom
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, chatRoom.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(chatRoom.getId().intValue()))
            .jsonPath("$.lastAction")
            .value(is(DEFAULT_LAST_ACTION))
            .jsonPath("$.lastConnectionDate")
            .value(is(DEFAULT_LAST_CONNECTION_DATE.toString()))
            .jsonPath("$.muted")
            .value(is(DEFAULT_MUTED.booleanValue()))
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
    void getNonExistingChatRoom() {
        // Get the chatRoom
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingChatRoom() throws Exception {
        // Initialize the database
        chatRoomRepository.save(chatRoom).block();

        int databaseSizeBeforeUpdate = chatRoomRepository.findAll().collectList().block().size();
        chatRoomSearchRepository.save(chatRoom).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(chatRoomSearchRepository.findAll().collectList().block());

        // Update the chatRoom
        ChatRoom updatedChatRoom = chatRoomRepository.findById(chatRoom.getId()).block();
        updatedChatRoom
            .lastAction(UPDATED_LAST_ACTION)
            .lastConnectionDate(UPDATED_LAST_CONNECTION_DATE)
            .muted(UPDATED_MUTED)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        ChatRoomDTO chatRoomDTO = chatRoomMapper.toDto(updatedChatRoom);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, chatRoomDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(chatRoomDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ChatRoom in the database
        List<ChatRoom> chatRoomList = chatRoomRepository.findAll().collectList().block();
        assertThat(chatRoomList).hasSize(databaseSizeBeforeUpdate);
        ChatRoom testChatRoom = chatRoomList.get(chatRoomList.size() - 1);
        assertThat(testChatRoom.getLastAction()).isEqualTo(UPDATED_LAST_ACTION);
        assertThat(testChatRoom.getLastConnectionDate()).isEqualTo(UPDATED_LAST_CONNECTION_DATE);
        assertThat(testChatRoom.getMuted()).isEqualTo(UPDATED_MUTED);
        assertThat(testChatRoom.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testChatRoom.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testChatRoom.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testChatRoom.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testChatRoom.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(chatRoomSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<ChatRoom> chatRoomSearchList = IterableUtils.toList(chatRoomSearchRepository.findAll().collectList().block());
                ChatRoom testChatRoomSearch = chatRoomSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testChatRoomSearch.getLastAction()).isEqualTo(UPDATED_LAST_ACTION);
                assertThat(testChatRoomSearch.getLastConnectionDate()).isEqualTo(UPDATED_LAST_CONNECTION_DATE);
                assertThat(testChatRoomSearch.getMuted()).isEqualTo(UPDATED_MUTED);
                assertThat(testChatRoomSearch.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
                assertThat(testChatRoomSearch.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
                assertThat(testChatRoomSearch.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
                assertThat(testChatRoomSearch.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
                assertThat(testChatRoomSearch.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
            });
    }

    @Test
    void putNonExistingChatRoom() throws Exception {
        int databaseSizeBeforeUpdate = chatRoomRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(chatRoomSearchRepository.findAll().collectList().block());
        chatRoom.setId(longCount.incrementAndGet());

        // Create the ChatRoom
        ChatRoomDTO chatRoomDTO = chatRoomMapper.toDto(chatRoom);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, chatRoomDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(chatRoomDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ChatRoom in the database
        List<ChatRoom> chatRoomList = chatRoomRepository.findAll().collectList().block();
        assertThat(chatRoomList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(chatRoomSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchChatRoom() throws Exception {
        int databaseSizeBeforeUpdate = chatRoomRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(chatRoomSearchRepository.findAll().collectList().block());
        chatRoom.setId(longCount.incrementAndGet());

        // Create the ChatRoom
        ChatRoomDTO chatRoomDTO = chatRoomMapper.toDto(chatRoom);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(chatRoomDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ChatRoom in the database
        List<ChatRoom> chatRoomList = chatRoomRepository.findAll().collectList().block();
        assertThat(chatRoomList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(chatRoomSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamChatRoom() throws Exception {
        int databaseSizeBeforeUpdate = chatRoomRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(chatRoomSearchRepository.findAll().collectList().block());
        chatRoom.setId(longCount.incrementAndGet());

        // Create the ChatRoom
        ChatRoomDTO chatRoomDTO = chatRoomMapper.toDto(chatRoom);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(chatRoomDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ChatRoom in the database
        List<ChatRoom> chatRoomList = chatRoomRepository.findAll().collectList().block();
        assertThat(chatRoomList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(chatRoomSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateChatRoomWithPatch() throws Exception {
        // Initialize the database
        chatRoomRepository.save(chatRoom).block();

        int databaseSizeBeforeUpdate = chatRoomRepository.findAll().collectList().block().size();

        // Update the chatRoom using partial update
        ChatRoom partialUpdatedChatRoom = new ChatRoom();
        partialUpdatedChatRoom.setId(chatRoom.getId());

        partialUpdatedChatRoom
            .lastAction(UPDATED_LAST_ACTION)
            .lastConnectionDate(UPDATED_LAST_CONNECTION_DATE)
            .createdDate(UPDATED_CREATED_DATE)
            .isDeleted(UPDATED_IS_DELETED);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedChatRoom.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedChatRoom))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ChatRoom in the database
        List<ChatRoom> chatRoomList = chatRoomRepository.findAll().collectList().block();
        assertThat(chatRoomList).hasSize(databaseSizeBeforeUpdate);
        ChatRoom testChatRoom = chatRoomList.get(chatRoomList.size() - 1);
        assertThat(testChatRoom.getLastAction()).isEqualTo(UPDATED_LAST_ACTION);
        assertThat(testChatRoom.getLastConnectionDate()).isEqualTo(UPDATED_LAST_CONNECTION_DATE);
        assertThat(testChatRoom.getMuted()).isEqualTo(DEFAULT_MUTED);
        assertThat(testChatRoom.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testChatRoom.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testChatRoom.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testChatRoom.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testChatRoom.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    void fullUpdateChatRoomWithPatch() throws Exception {
        // Initialize the database
        chatRoomRepository.save(chatRoom).block();

        int databaseSizeBeforeUpdate = chatRoomRepository.findAll().collectList().block().size();

        // Update the chatRoom using partial update
        ChatRoom partialUpdatedChatRoom = new ChatRoom();
        partialUpdatedChatRoom.setId(chatRoom.getId());

        partialUpdatedChatRoom
            .lastAction(UPDATED_LAST_ACTION)
            .lastConnectionDate(UPDATED_LAST_CONNECTION_DATE)
            .muted(UPDATED_MUTED)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedChatRoom.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedChatRoom))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ChatRoom in the database
        List<ChatRoom> chatRoomList = chatRoomRepository.findAll().collectList().block();
        assertThat(chatRoomList).hasSize(databaseSizeBeforeUpdate);
        ChatRoom testChatRoom = chatRoomList.get(chatRoomList.size() - 1);
        assertThat(testChatRoom.getLastAction()).isEqualTo(UPDATED_LAST_ACTION);
        assertThat(testChatRoom.getLastConnectionDate()).isEqualTo(UPDATED_LAST_CONNECTION_DATE);
        assertThat(testChatRoom.getMuted()).isEqualTo(UPDATED_MUTED);
        assertThat(testChatRoom.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testChatRoom.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testChatRoom.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testChatRoom.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testChatRoom.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    void patchNonExistingChatRoom() throws Exception {
        int databaseSizeBeforeUpdate = chatRoomRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(chatRoomSearchRepository.findAll().collectList().block());
        chatRoom.setId(longCount.incrementAndGet());

        // Create the ChatRoom
        ChatRoomDTO chatRoomDTO = chatRoomMapper.toDto(chatRoom);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, chatRoomDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(chatRoomDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ChatRoom in the database
        List<ChatRoom> chatRoomList = chatRoomRepository.findAll().collectList().block();
        assertThat(chatRoomList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(chatRoomSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchChatRoom() throws Exception {
        int databaseSizeBeforeUpdate = chatRoomRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(chatRoomSearchRepository.findAll().collectList().block());
        chatRoom.setId(longCount.incrementAndGet());

        // Create the ChatRoom
        ChatRoomDTO chatRoomDTO = chatRoomMapper.toDto(chatRoom);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(chatRoomDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ChatRoom in the database
        List<ChatRoom> chatRoomList = chatRoomRepository.findAll().collectList().block();
        assertThat(chatRoomList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(chatRoomSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamChatRoom() throws Exception {
        int databaseSizeBeforeUpdate = chatRoomRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(chatRoomSearchRepository.findAll().collectList().block());
        chatRoom.setId(longCount.incrementAndGet());

        // Create the ChatRoom
        ChatRoomDTO chatRoomDTO = chatRoomMapper.toDto(chatRoom);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(chatRoomDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ChatRoom in the database
        List<ChatRoom> chatRoomList = chatRoomRepository.findAll().collectList().block();
        assertThat(chatRoomList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(chatRoomSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteChatRoom() {
        // Initialize the database
        chatRoomRepository.save(chatRoom).block();
        chatRoomRepository.save(chatRoom).block();
        chatRoomSearchRepository.save(chatRoom).block();

        int databaseSizeBeforeDelete = chatRoomRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(chatRoomSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the chatRoom
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, chatRoom.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<ChatRoom> chatRoomList = chatRoomRepository.findAll().collectList().block();
        assertThat(chatRoomList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(chatRoomSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchChatRoom() {
        // Initialize the database
        chatRoom = chatRoomRepository.save(chatRoom).block();
        chatRoomSearchRepository.save(chatRoom).block();

        // Search the chatRoom
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + chatRoom.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(chatRoom.getId().intValue()))
            .jsonPath("$.[*].lastAction")
            .value(hasItem(DEFAULT_LAST_ACTION))
            .jsonPath("$.[*].lastConnectionDate")
            .value(hasItem(DEFAULT_LAST_CONNECTION_DATE.toString()))
            .jsonPath("$.[*].muted")
            .value(hasItem(DEFAULT_MUTED.booleanValue()))
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
