package com.monsterdam.interactions.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.monsterdam.interactions.IntegrationTest;
import com.monsterdam.interactions.domain.DirectMessage;
import com.monsterdam.interactions.repository.DirectMessageRepository;
import com.monsterdam.interactions.repository.search.DirectMessageSearchRepository;
import com.monsterdam.interactions.service.DirectMessageService;
import com.monsterdam.interactions.service.dto.DirectMessageDTO;
import com.monsterdam.interactions.service.mapper.DirectMessageMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link DirectMessageResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DirectMessageResourceIT {

    private static final String DEFAULT_MESSAGE_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_MESSAGE_CONTENT = "BBBBBBBBBB";

    private static final Instant DEFAULT_READ_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_READ_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_LIKE_COUNT = 1;
    private static final Integer UPDATED_LIKE_COUNT = 2;

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

    private static final Long DEFAULT_REPLIED_STORY_ID = 1L;
    private static final Long UPDATED_REPLIED_STORY_ID = 2L;

    private static final Long DEFAULT_SENDER_USER_ID = 1L;
    private static final Long UPDATED_SENDER_USER_ID = 2L;

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
    private MockMvc restDirectMessageMockMvc;

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
            .isDeleted(DEFAULT_IS_DELETED)
            .repliedStoryId(DEFAULT_REPLIED_STORY_ID)
            .senderUserId(DEFAULT_SENDER_USER_ID);
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
            .isDeleted(UPDATED_IS_DELETED)
            .repliedStoryId(UPDATED_REPLIED_STORY_ID)
            .senderUserId(UPDATED_SENDER_USER_ID);
        return directMessage;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        directMessageSearchRepository.deleteAll();
        assertThat(directMessageSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        directMessage = createEntity(em);
    }

    @Test
    @Transactional
    void createDirectMessage() throws Exception {
        int databaseSizeBeforeCreate = directMessageRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(directMessageSearchRepository.findAll());
        // Create the DirectMessage
        DirectMessageDTO directMessageDTO = directMessageMapper.toDto(directMessage);
        restDirectMessageMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(directMessageDTO))
            )
            .andExpect(status().isCreated());

        // Validate the DirectMessage in the database
        List<DirectMessage> directMessageList = directMessageRepository.findAll();
        assertThat(directMessageList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(directMessageSearchRepository.findAll());
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
        assertThat(testDirectMessage.getRepliedStoryId()).isEqualTo(DEFAULT_REPLIED_STORY_ID);
        assertThat(testDirectMessage.getSenderUserId()).isEqualTo(DEFAULT_SENDER_USER_ID);
    }

    @Test
    @Transactional
    void createDirectMessageWithExistingId() throws Exception {
        // Create the DirectMessage with an existing ID
        directMessage.setId(1L);
        DirectMessageDTO directMessageDTO = directMessageMapper.toDto(directMessage);

        int databaseSizeBeforeCreate = directMessageRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(directMessageSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restDirectMessageMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(directMessageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DirectMessage in the database
        List<DirectMessage> directMessageList = directMessageRepository.findAll();
        assertThat(directMessageList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(directMessageSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = directMessageRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(directMessageSearchRepository.findAll());
        // set the field null
        directMessage.setCreatedDate(null);

        // Create the DirectMessage, which fails.
        DirectMessageDTO directMessageDTO = directMessageMapper.toDto(directMessage);

        restDirectMessageMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(directMessageDTO))
            )
            .andExpect(status().isBadRequest());

        List<DirectMessage> directMessageList = directMessageRepository.findAll();
        assertThat(directMessageList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(directMessageSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = directMessageRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(directMessageSearchRepository.findAll());
        // set the field null
        directMessage.setIsDeleted(null);

        // Create the DirectMessage, which fails.
        DirectMessageDTO directMessageDTO = directMessageMapper.toDto(directMessage);

        restDirectMessageMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(directMessageDTO))
            )
            .andExpect(status().isBadRequest());

        List<DirectMessage> directMessageList = directMessageRepository.findAll();
        assertThat(directMessageList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(directMessageSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkSenderUserIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = directMessageRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(directMessageSearchRepository.findAll());
        // set the field null
        directMessage.setSenderUserId(null);

        // Create the DirectMessage, which fails.
        DirectMessageDTO directMessageDTO = directMessageMapper.toDto(directMessage);

        restDirectMessageMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(directMessageDTO))
            )
            .andExpect(status().isBadRequest());

        List<DirectMessage> directMessageList = directMessageRepository.findAll();
        assertThat(directMessageList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(directMessageSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllDirectMessages() throws Exception {
        // Initialize the database
        directMessageRepository.saveAndFlush(directMessage);

        // Get all the directMessageList
        restDirectMessageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(directMessage.getId().intValue())))
            .andExpect(jsonPath("$.[*].messageContent").value(hasItem(DEFAULT_MESSAGE_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].readDate").value(hasItem(DEFAULT_READ_DATE.toString())))
            .andExpect(jsonPath("$.[*].likeCount").value(hasItem(DEFAULT_LIKE_COUNT)))
            .andExpect(jsonPath("$.[*].isHidden").value(hasItem(DEFAULT_IS_HIDDEN.booleanValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())))
            .andExpect(jsonPath("$.[*].repliedStoryId").value(hasItem(DEFAULT_REPLIED_STORY_ID.intValue())))
            .andExpect(jsonPath("$.[*].senderUserId").value(hasItem(DEFAULT_SENDER_USER_ID.intValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDirectMessagesWithEagerRelationshipsIsEnabled() throws Exception {
        when(directMessageServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDirectMessageMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(directMessageServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDirectMessagesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(directMessageServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDirectMessageMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(directMessageRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getDirectMessage() throws Exception {
        // Initialize the database
        directMessageRepository.saveAndFlush(directMessage);

        // Get the directMessage
        restDirectMessageMockMvc
            .perform(get(ENTITY_API_URL_ID, directMessage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(directMessage.getId().intValue()))
            .andExpect(jsonPath("$.messageContent").value(DEFAULT_MESSAGE_CONTENT.toString()))
            .andExpect(jsonPath("$.readDate").value(DEFAULT_READ_DATE.toString()))
            .andExpect(jsonPath("$.likeCount").value(DEFAULT_LIKE_COUNT))
            .andExpect(jsonPath("$.isHidden").value(DEFAULT_IS_HIDDEN.booleanValue()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()))
            .andExpect(jsonPath("$.repliedStoryId").value(DEFAULT_REPLIED_STORY_ID.intValue()))
            .andExpect(jsonPath("$.senderUserId").value(DEFAULT_SENDER_USER_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingDirectMessage() throws Exception {
        // Get the directMessage
        restDirectMessageMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDirectMessage() throws Exception {
        // Initialize the database
        directMessageRepository.saveAndFlush(directMessage);

        int databaseSizeBeforeUpdate = directMessageRepository.findAll().size();
        directMessageSearchRepository.save(directMessage);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(directMessageSearchRepository.findAll());

        // Update the directMessage
        DirectMessage updatedDirectMessage = directMessageRepository.findById(directMessage.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDirectMessage are not directly saved in db
        em.detach(updatedDirectMessage);
        updatedDirectMessage
            .messageContent(UPDATED_MESSAGE_CONTENT)
            .readDate(UPDATED_READ_DATE)
            .likeCount(UPDATED_LIKE_COUNT)
            .isHidden(UPDATED_IS_HIDDEN)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .repliedStoryId(UPDATED_REPLIED_STORY_ID)
            .senderUserId(UPDATED_SENDER_USER_ID);
        DirectMessageDTO directMessageDTO = directMessageMapper.toDto(updatedDirectMessage);

        restDirectMessageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, directMessageDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(directMessageDTO))
            )
            .andExpect(status().isOk());

        // Validate the DirectMessage in the database
        List<DirectMessage> directMessageList = directMessageRepository.findAll();
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
        assertThat(testDirectMessage.getRepliedStoryId()).isEqualTo(UPDATED_REPLIED_STORY_ID);
        assertThat(testDirectMessage.getSenderUserId()).isEqualTo(UPDATED_SENDER_USER_ID);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(directMessageSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<DirectMessage> directMessageSearchList = IterableUtils.toList(directMessageSearchRepository.findAll());
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
                assertThat(testDirectMessageSearch.getRepliedStoryId()).isEqualTo(UPDATED_REPLIED_STORY_ID);
                assertThat(testDirectMessageSearch.getSenderUserId()).isEqualTo(UPDATED_SENDER_USER_ID);
            });
    }

    @Test
    @Transactional
    void putNonExistingDirectMessage() throws Exception {
        int databaseSizeBeforeUpdate = directMessageRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(directMessageSearchRepository.findAll());
        directMessage.setId(longCount.incrementAndGet());

        // Create the DirectMessage
        DirectMessageDTO directMessageDTO = directMessageMapper.toDto(directMessage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDirectMessageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, directMessageDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(directMessageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DirectMessage in the database
        List<DirectMessage> directMessageList = directMessageRepository.findAll();
        assertThat(directMessageList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(directMessageSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchDirectMessage() throws Exception {
        int databaseSizeBeforeUpdate = directMessageRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(directMessageSearchRepository.findAll());
        directMessage.setId(longCount.incrementAndGet());

        // Create the DirectMessage
        DirectMessageDTO directMessageDTO = directMessageMapper.toDto(directMessage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDirectMessageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(directMessageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DirectMessage in the database
        List<DirectMessage> directMessageList = directMessageRepository.findAll();
        assertThat(directMessageList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(directMessageSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDirectMessage() throws Exception {
        int databaseSizeBeforeUpdate = directMessageRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(directMessageSearchRepository.findAll());
        directMessage.setId(longCount.incrementAndGet());

        // Create the DirectMessage
        DirectMessageDTO directMessageDTO = directMessageMapper.toDto(directMessage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDirectMessageMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(directMessageDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DirectMessage in the database
        List<DirectMessage> directMessageList = directMessageRepository.findAll();
        assertThat(directMessageList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(directMessageSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateDirectMessageWithPatch() throws Exception {
        // Initialize the database
        directMessageRepository.saveAndFlush(directMessage);

        int databaseSizeBeforeUpdate = directMessageRepository.findAll().size();

        // Update the directMessage using partial update
        DirectMessage partialUpdatedDirectMessage = new DirectMessage();
        partialUpdatedDirectMessage.setId(directMessage.getId());

        partialUpdatedDirectMessage
            .likeCount(UPDATED_LIKE_COUNT)
            .isHidden(UPDATED_IS_HIDDEN)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        restDirectMessageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDirectMessage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDirectMessage))
            )
            .andExpect(status().isOk());

        // Validate the DirectMessage in the database
        List<DirectMessage> directMessageList = directMessageRepository.findAll();
        assertThat(directMessageList).hasSize(databaseSizeBeforeUpdate);
        DirectMessage testDirectMessage = directMessageList.get(directMessageList.size() - 1);
        assertThat(testDirectMessage.getMessageContent()).isEqualTo(DEFAULT_MESSAGE_CONTENT);
        assertThat(testDirectMessage.getReadDate()).isEqualTo(DEFAULT_READ_DATE);
        assertThat(testDirectMessage.getLikeCount()).isEqualTo(UPDATED_LIKE_COUNT);
        assertThat(testDirectMessage.getIsHidden()).isEqualTo(UPDATED_IS_HIDDEN);
        assertThat(testDirectMessage.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testDirectMessage.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testDirectMessage.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testDirectMessage.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testDirectMessage.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
        assertThat(testDirectMessage.getRepliedStoryId()).isEqualTo(DEFAULT_REPLIED_STORY_ID);
        assertThat(testDirectMessage.getSenderUserId()).isEqualTo(DEFAULT_SENDER_USER_ID);
    }

    @Test
    @Transactional
    void fullUpdateDirectMessageWithPatch() throws Exception {
        // Initialize the database
        directMessageRepository.saveAndFlush(directMessage);

        int databaseSizeBeforeUpdate = directMessageRepository.findAll().size();

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
            .isDeleted(UPDATED_IS_DELETED)
            .repliedStoryId(UPDATED_REPLIED_STORY_ID)
            .senderUserId(UPDATED_SENDER_USER_ID);

        restDirectMessageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDirectMessage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDirectMessage))
            )
            .andExpect(status().isOk());

        // Validate the DirectMessage in the database
        List<DirectMessage> directMessageList = directMessageRepository.findAll();
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
        assertThat(testDirectMessage.getRepliedStoryId()).isEqualTo(UPDATED_REPLIED_STORY_ID);
        assertThat(testDirectMessage.getSenderUserId()).isEqualTo(UPDATED_SENDER_USER_ID);
    }

    @Test
    @Transactional
    void patchNonExistingDirectMessage() throws Exception {
        int databaseSizeBeforeUpdate = directMessageRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(directMessageSearchRepository.findAll());
        directMessage.setId(longCount.incrementAndGet());

        // Create the DirectMessage
        DirectMessageDTO directMessageDTO = directMessageMapper.toDto(directMessage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDirectMessageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, directMessageDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(directMessageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DirectMessage in the database
        List<DirectMessage> directMessageList = directMessageRepository.findAll();
        assertThat(directMessageList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(directMessageSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDirectMessage() throws Exception {
        int databaseSizeBeforeUpdate = directMessageRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(directMessageSearchRepository.findAll());
        directMessage.setId(longCount.incrementAndGet());

        // Create the DirectMessage
        DirectMessageDTO directMessageDTO = directMessageMapper.toDto(directMessage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDirectMessageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(directMessageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DirectMessage in the database
        List<DirectMessage> directMessageList = directMessageRepository.findAll();
        assertThat(directMessageList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(directMessageSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDirectMessage() throws Exception {
        int databaseSizeBeforeUpdate = directMessageRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(directMessageSearchRepository.findAll());
        directMessage.setId(longCount.incrementAndGet());

        // Create the DirectMessage
        DirectMessageDTO directMessageDTO = directMessageMapper.toDto(directMessage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDirectMessageMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(directMessageDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DirectMessage in the database
        List<DirectMessage> directMessageList = directMessageRepository.findAll();
        assertThat(directMessageList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(directMessageSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteDirectMessage() throws Exception {
        // Initialize the database
        directMessageRepository.saveAndFlush(directMessage);
        directMessageRepository.save(directMessage);
        directMessageSearchRepository.save(directMessage);

        int databaseSizeBeforeDelete = directMessageRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(directMessageSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the directMessage
        restDirectMessageMockMvc
            .perform(delete(ENTITY_API_URL_ID, directMessage.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DirectMessage> directMessageList = directMessageRepository.findAll();
        assertThat(directMessageList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(directMessageSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchDirectMessage() throws Exception {
        // Initialize the database
        directMessage = directMessageRepository.saveAndFlush(directMessage);
        directMessageSearchRepository.save(directMessage);

        // Search the directMessage
        restDirectMessageMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + directMessage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(directMessage.getId().intValue())))
            .andExpect(jsonPath("$.[*].messageContent").value(hasItem(DEFAULT_MESSAGE_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].readDate").value(hasItem(DEFAULT_READ_DATE.toString())))
            .andExpect(jsonPath("$.[*].likeCount").value(hasItem(DEFAULT_LIKE_COUNT)))
            .andExpect(jsonPath("$.[*].isHidden").value(hasItem(DEFAULT_IS_HIDDEN.booleanValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())))
            .andExpect(jsonPath("$.[*].repliedStoryId").value(hasItem(DEFAULT_REPLIED_STORY_ID.intValue())))
            .andExpect(jsonPath("$.[*].senderUserId").value(hasItem(DEFAULT_SENDER_USER_ID.intValue())));
    }
}
