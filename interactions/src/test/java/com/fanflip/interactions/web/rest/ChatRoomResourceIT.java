package com.fanflip.interactions.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fanflip.interactions.IntegrationTest;
import com.fanflip.interactions.domain.ChatRoom;
import com.fanflip.interactions.repository.ChatRoomRepository;
import com.fanflip.interactions.service.ChatRoomService;
import com.fanflip.interactions.service.dto.ChatRoomDTO;
import com.fanflip.interactions.service.mapper.ChatRoomMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
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
 * Integration tests for the {@link ChatRoomResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
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

    private static final Long DEFAULT_PARTICIPANT_USER_ID = 1L;
    private static final Long UPDATED_PARTICIPANT_USER_ID = 2L;

    private static final String ENTITY_API_URL = "/api/chat-rooms";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

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
    private EntityManager em;

    @Autowired
    private MockMvc restChatRoomMockMvc;

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
            .isDeleted(DEFAULT_IS_DELETED)
            .participantUserId(DEFAULT_PARTICIPANT_USER_ID);
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
            .isDeleted(UPDATED_IS_DELETED)
            .participantUserId(UPDATED_PARTICIPANT_USER_ID);
        return chatRoom;
    }

    @BeforeEach
    public void initTest() {
        chatRoom = createEntity(em);
    }

    @Test
    @Transactional
    void createChatRoom() throws Exception {
        int databaseSizeBeforeCreate = chatRoomRepository.findAll().size();
        // Create the ChatRoom
        ChatRoomDTO chatRoomDTO = chatRoomMapper.toDto(chatRoom);
        restChatRoomMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(chatRoomDTO)))
            .andExpect(status().isCreated());

        // Validate the ChatRoom in the database
        List<ChatRoom> chatRoomList = chatRoomRepository.findAll();
        assertThat(chatRoomList).hasSize(databaseSizeBeforeCreate + 1);
        ChatRoom testChatRoom = chatRoomList.get(chatRoomList.size() - 1);
        assertThat(testChatRoom.getLastAction()).isEqualTo(DEFAULT_LAST_ACTION);
        assertThat(testChatRoom.getLastConnectionDate()).isEqualTo(DEFAULT_LAST_CONNECTION_DATE);
        assertThat(testChatRoom.getMuted()).isEqualTo(DEFAULT_MUTED);
        assertThat(testChatRoom.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testChatRoom.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testChatRoom.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testChatRoom.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testChatRoom.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
        assertThat(testChatRoom.getParticipantUserId()).isEqualTo(DEFAULT_PARTICIPANT_USER_ID);
    }

    @Test
    @Transactional
    void createChatRoomWithExistingId() throws Exception {
        // Create the ChatRoom with an existing ID
        chatRoom.setId(1L);
        ChatRoomDTO chatRoomDTO = chatRoomMapper.toDto(chatRoom);

        int databaseSizeBeforeCreate = chatRoomRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restChatRoomMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(chatRoomDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ChatRoom in the database
        List<ChatRoom> chatRoomList = chatRoomRepository.findAll();
        assertThat(chatRoomList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = chatRoomRepository.findAll().size();
        // set the field null
        chatRoom.setCreatedDate(null);

        // Create the ChatRoom, which fails.
        ChatRoomDTO chatRoomDTO = chatRoomMapper.toDto(chatRoom);

        restChatRoomMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(chatRoomDTO)))
            .andExpect(status().isBadRequest());

        List<ChatRoom> chatRoomList = chatRoomRepository.findAll();
        assertThat(chatRoomList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = chatRoomRepository.findAll().size();
        // set the field null
        chatRoom.setIsDeleted(null);

        // Create the ChatRoom, which fails.
        ChatRoomDTO chatRoomDTO = chatRoomMapper.toDto(chatRoom);

        restChatRoomMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(chatRoomDTO)))
            .andExpect(status().isBadRequest());

        List<ChatRoom> chatRoomList = chatRoomRepository.findAll();
        assertThat(chatRoomList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllChatRooms() throws Exception {
        // Initialize the database
        chatRoomRepository.saveAndFlush(chatRoom);

        // Get all the chatRoomList
        restChatRoomMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(chatRoom.getId().intValue())))
            .andExpect(jsonPath("$.[*].lastAction").value(hasItem(DEFAULT_LAST_ACTION)))
            .andExpect(jsonPath("$.[*].lastConnectionDate").value(hasItem(DEFAULT_LAST_CONNECTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].muted").value(hasItem(DEFAULT_MUTED.booleanValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())))
            .andExpect(jsonPath("$.[*].participantUserId").value(hasItem(DEFAULT_PARTICIPANT_USER_ID.intValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllChatRoomsWithEagerRelationshipsIsEnabled() throws Exception {
        when(chatRoomServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restChatRoomMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(chatRoomServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllChatRoomsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(chatRoomServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restChatRoomMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(chatRoomRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getChatRoom() throws Exception {
        // Initialize the database
        chatRoomRepository.saveAndFlush(chatRoom);

        // Get the chatRoom
        restChatRoomMockMvc
            .perform(get(ENTITY_API_URL_ID, chatRoom.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(chatRoom.getId().intValue()))
            .andExpect(jsonPath("$.lastAction").value(DEFAULT_LAST_ACTION))
            .andExpect(jsonPath("$.lastConnectionDate").value(DEFAULT_LAST_CONNECTION_DATE.toString()))
            .andExpect(jsonPath("$.muted").value(DEFAULT_MUTED.booleanValue()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()))
            .andExpect(jsonPath("$.participantUserId").value(DEFAULT_PARTICIPANT_USER_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingChatRoom() throws Exception {
        // Get the chatRoom
        restChatRoomMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingChatRoom() throws Exception {
        // Initialize the database
        chatRoomRepository.saveAndFlush(chatRoom);

        int databaseSizeBeforeUpdate = chatRoomRepository.findAll().size();

        // Update the chatRoom
        ChatRoom updatedChatRoom = chatRoomRepository.findById(chatRoom.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedChatRoom are not directly saved in db
        em.detach(updatedChatRoom);
        updatedChatRoom
            .lastAction(UPDATED_LAST_ACTION)
            .lastConnectionDate(UPDATED_LAST_CONNECTION_DATE)
            .muted(UPDATED_MUTED)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .participantUserId(UPDATED_PARTICIPANT_USER_ID);
        ChatRoomDTO chatRoomDTO = chatRoomMapper.toDto(updatedChatRoom);

        restChatRoomMockMvc
            .perform(
                put(ENTITY_API_URL_ID, chatRoomDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(chatRoomDTO))
            )
            .andExpect(status().isOk());

        // Validate the ChatRoom in the database
        List<ChatRoom> chatRoomList = chatRoomRepository.findAll();
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
        assertThat(testChatRoom.getParticipantUserId()).isEqualTo(UPDATED_PARTICIPANT_USER_ID);
    }

    @Test
    @Transactional
    void putNonExistingChatRoom() throws Exception {
        int databaseSizeBeforeUpdate = chatRoomRepository.findAll().size();
        chatRoom.setId(longCount.incrementAndGet());

        // Create the ChatRoom
        ChatRoomDTO chatRoomDTO = chatRoomMapper.toDto(chatRoom);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChatRoomMockMvc
            .perform(
                put(ENTITY_API_URL_ID, chatRoomDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(chatRoomDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChatRoom in the database
        List<ChatRoom> chatRoomList = chatRoomRepository.findAll();
        assertThat(chatRoomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchChatRoom() throws Exception {
        int databaseSizeBeforeUpdate = chatRoomRepository.findAll().size();
        chatRoom.setId(longCount.incrementAndGet());

        // Create the ChatRoom
        ChatRoomDTO chatRoomDTO = chatRoomMapper.toDto(chatRoom);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChatRoomMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(chatRoomDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChatRoom in the database
        List<ChatRoom> chatRoomList = chatRoomRepository.findAll();
        assertThat(chatRoomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamChatRoom() throws Exception {
        int databaseSizeBeforeUpdate = chatRoomRepository.findAll().size();
        chatRoom.setId(longCount.incrementAndGet());

        // Create the ChatRoom
        ChatRoomDTO chatRoomDTO = chatRoomMapper.toDto(chatRoom);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChatRoomMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(chatRoomDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ChatRoom in the database
        List<ChatRoom> chatRoomList = chatRoomRepository.findAll();
        assertThat(chatRoomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateChatRoomWithPatch() throws Exception {
        // Initialize the database
        chatRoomRepository.saveAndFlush(chatRoom);

        int databaseSizeBeforeUpdate = chatRoomRepository.findAll().size();

        // Update the chatRoom using partial update
        ChatRoom partialUpdatedChatRoom = new ChatRoom();
        partialUpdatedChatRoom.setId(chatRoom.getId());

        partialUpdatedChatRoom
            .lastConnectionDate(UPDATED_LAST_CONNECTION_DATE)
            .muted(UPDATED_MUTED)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .participantUserId(UPDATED_PARTICIPANT_USER_ID);

        restChatRoomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedChatRoom.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedChatRoom))
            )
            .andExpect(status().isOk());

        // Validate the ChatRoom in the database
        List<ChatRoom> chatRoomList = chatRoomRepository.findAll();
        assertThat(chatRoomList).hasSize(databaseSizeBeforeUpdate);
        ChatRoom testChatRoom = chatRoomList.get(chatRoomList.size() - 1);
        assertThat(testChatRoom.getLastAction()).isEqualTo(DEFAULT_LAST_ACTION);
        assertThat(testChatRoom.getLastConnectionDate()).isEqualTo(UPDATED_LAST_CONNECTION_DATE);
        assertThat(testChatRoom.getMuted()).isEqualTo(UPDATED_MUTED);
        assertThat(testChatRoom.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testChatRoom.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testChatRoom.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testChatRoom.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testChatRoom.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
        assertThat(testChatRoom.getParticipantUserId()).isEqualTo(UPDATED_PARTICIPANT_USER_ID);
    }

    @Test
    @Transactional
    void fullUpdateChatRoomWithPatch() throws Exception {
        // Initialize the database
        chatRoomRepository.saveAndFlush(chatRoom);

        int databaseSizeBeforeUpdate = chatRoomRepository.findAll().size();

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
            .isDeleted(UPDATED_IS_DELETED)
            .participantUserId(UPDATED_PARTICIPANT_USER_ID);

        restChatRoomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedChatRoom.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedChatRoom))
            )
            .andExpect(status().isOk());

        // Validate the ChatRoom in the database
        List<ChatRoom> chatRoomList = chatRoomRepository.findAll();
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
        assertThat(testChatRoom.getParticipantUserId()).isEqualTo(UPDATED_PARTICIPANT_USER_ID);
    }

    @Test
    @Transactional
    void patchNonExistingChatRoom() throws Exception {
        int databaseSizeBeforeUpdate = chatRoomRepository.findAll().size();
        chatRoom.setId(longCount.incrementAndGet());

        // Create the ChatRoom
        ChatRoomDTO chatRoomDTO = chatRoomMapper.toDto(chatRoom);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChatRoomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, chatRoomDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(chatRoomDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChatRoom in the database
        List<ChatRoom> chatRoomList = chatRoomRepository.findAll();
        assertThat(chatRoomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchChatRoom() throws Exception {
        int databaseSizeBeforeUpdate = chatRoomRepository.findAll().size();
        chatRoom.setId(longCount.incrementAndGet());

        // Create the ChatRoom
        ChatRoomDTO chatRoomDTO = chatRoomMapper.toDto(chatRoom);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChatRoomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(chatRoomDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChatRoom in the database
        List<ChatRoom> chatRoomList = chatRoomRepository.findAll();
        assertThat(chatRoomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamChatRoom() throws Exception {
        int databaseSizeBeforeUpdate = chatRoomRepository.findAll().size();
        chatRoom.setId(longCount.incrementAndGet());

        // Create the ChatRoom
        ChatRoomDTO chatRoomDTO = chatRoomMapper.toDto(chatRoom);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChatRoomMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(chatRoomDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ChatRoom in the database
        List<ChatRoom> chatRoomList = chatRoomRepository.findAll();
        assertThat(chatRoomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteChatRoom() throws Exception {
        // Initialize the database
        chatRoomRepository.saveAndFlush(chatRoom);

        int databaseSizeBeforeDelete = chatRoomRepository.findAll().size();

        // Delete the chatRoom
        restChatRoomMockMvc
            .perform(delete(ENTITY_API_URL_ID, chatRoom.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ChatRoom> chatRoomList = chatRoomRepository.findAll();
        assertThat(chatRoomList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
