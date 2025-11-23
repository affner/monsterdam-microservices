package com.fanflip.interactions.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fanflip.interactions.IntegrationTest;
import com.fanflip.interactions.domain.PollOption;
import com.fanflip.interactions.domain.PollVote;
import com.fanflip.interactions.repository.PollVoteRepository;
import com.fanflip.interactions.service.PollVoteService;
import com.fanflip.interactions.service.dto.PollVoteDTO;
import com.fanflip.interactions.service.mapper.PollVoteMapper;
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
 * Integration tests for the {@link PollVoteResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PollVoteResourceIT {

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_VOTING_USER_ID = 1L;
    private static final Long UPDATED_VOTING_USER_ID = 2L;

    private static final String ENTITY_API_URL = "/api/poll-votes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

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
    private EntityManager em;

    @Autowired
    private MockMvc restPollVoteMockMvc;

    private PollVote pollVote;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PollVote createEntity(EntityManager em) {
        PollVote pollVote = new PollVote().createdDate(DEFAULT_CREATED_DATE).votingUserId(DEFAULT_VOTING_USER_ID);
        // Add required entity
        PollOption pollOption;
        if (TestUtil.findAll(em, PollOption.class).isEmpty()) {
            pollOption = PollOptionResourceIT.createEntity(em);
            em.persist(pollOption);
            em.flush();
        } else {
            pollOption = TestUtil.findAll(em, PollOption.class).get(0);
        }
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
        PollVote pollVote = new PollVote().createdDate(UPDATED_CREATED_DATE).votingUserId(UPDATED_VOTING_USER_ID);
        // Add required entity
        PollOption pollOption;
        if (TestUtil.findAll(em, PollOption.class).isEmpty()) {
            pollOption = PollOptionResourceIT.createUpdatedEntity(em);
            em.persist(pollOption);
            em.flush();
        } else {
            pollOption = TestUtil.findAll(em, PollOption.class).get(0);
        }
        pollVote.setPollOption(pollOption);
        return pollVote;
    }

    @BeforeEach
    public void initTest() {
        pollVote = createEntity(em);
    }

    @Test
    @Transactional
    void createPollVote() throws Exception {
        int databaseSizeBeforeCreate = pollVoteRepository.findAll().size();
        // Create the PollVote
        PollVoteDTO pollVoteDTO = pollVoteMapper.toDto(pollVote);
        restPollVoteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pollVoteDTO)))
            .andExpect(status().isCreated());

        // Validate the PollVote in the database
        List<PollVote> pollVoteList = pollVoteRepository.findAll();
        assertThat(pollVoteList).hasSize(databaseSizeBeforeCreate + 1);
        PollVote testPollVote = pollVoteList.get(pollVoteList.size() - 1);
        assertThat(testPollVote.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testPollVote.getVotingUserId()).isEqualTo(DEFAULT_VOTING_USER_ID);
    }

    @Test
    @Transactional
    void createPollVoteWithExistingId() throws Exception {
        // Create the PollVote with an existing ID
        pollVote.setId(1L);
        PollVoteDTO pollVoteDTO = pollVoteMapper.toDto(pollVote);

        int databaseSizeBeforeCreate = pollVoteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPollVoteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pollVoteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PollVote in the database
        List<PollVote> pollVoteList = pollVoteRepository.findAll();
        assertThat(pollVoteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = pollVoteRepository.findAll().size();
        // set the field null
        pollVote.setCreatedDate(null);

        // Create the PollVote, which fails.
        PollVoteDTO pollVoteDTO = pollVoteMapper.toDto(pollVote);

        restPollVoteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pollVoteDTO)))
            .andExpect(status().isBadRequest());

        List<PollVote> pollVoteList = pollVoteRepository.findAll();
        assertThat(pollVoteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkVotingUserIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = pollVoteRepository.findAll().size();
        // set the field null
        pollVote.setVotingUserId(null);

        // Create the PollVote, which fails.
        PollVoteDTO pollVoteDTO = pollVoteMapper.toDto(pollVote);

        restPollVoteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pollVoteDTO)))
            .andExpect(status().isBadRequest());

        List<PollVote> pollVoteList = pollVoteRepository.findAll();
        assertThat(pollVoteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPollVotes() throws Exception {
        // Initialize the database
        pollVoteRepository.saveAndFlush(pollVote);

        // Get all the pollVoteList
        restPollVoteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pollVote.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].votingUserId").value(hasItem(DEFAULT_VOTING_USER_ID.intValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPollVotesWithEagerRelationshipsIsEnabled() throws Exception {
        when(pollVoteServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPollVoteMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(pollVoteServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPollVotesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(pollVoteServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPollVoteMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(pollVoteRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPollVote() throws Exception {
        // Initialize the database
        pollVoteRepository.saveAndFlush(pollVote);

        // Get the pollVote
        restPollVoteMockMvc
            .perform(get(ENTITY_API_URL_ID, pollVote.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pollVote.getId().intValue()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.votingUserId").value(DEFAULT_VOTING_USER_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingPollVote() throws Exception {
        // Get the pollVote
        restPollVoteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPollVote() throws Exception {
        // Initialize the database
        pollVoteRepository.saveAndFlush(pollVote);

        int databaseSizeBeforeUpdate = pollVoteRepository.findAll().size();

        // Update the pollVote
        PollVote updatedPollVote = pollVoteRepository.findById(pollVote.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPollVote are not directly saved in db
        em.detach(updatedPollVote);
        updatedPollVote.createdDate(UPDATED_CREATED_DATE).votingUserId(UPDATED_VOTING_USER_ID);
        PollVoteDTO pollVoteDTO = pollVoteMapper.toDto(updatedPollVote);

        restPollVoteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pollVoteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pollVoteDTO))
            )
            .andExpect(status().isOk());

        // Validate the PollVote in the database
        List<PollVote> pollVoteList = pollVoteRepository.findAll();
        assertThat(pollVoteList).hasSize(databaseSizeBeforeUpdate);
        PollVote testPollVote = pollVoteList.get(pollVoteList.size() - 1);
        assertThat(testPollVote.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testPollVote.getVotingUserId()).isEqualTo(UPDATED_VOTING_USER_ID);
    }

    @Test
    @Transactional
    void putNonExistingPollVote() throws Exception {
        int databaseSizeBeforeUpdate = pollVoteRepository.findAll().size();
        pollVote.setId(longCount.incrementAndGet());

        // Create the PollVote
        PollVoteDTO pollVoteDTO = pollVoteMapper.toDto(pollVote);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPollVoteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pollVoteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pollVoteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PollVote in the database
        List<PollVote> pollVoteList = pollVoteRepository.findAll();
        assertThat(pollVoteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPollVote() throws Exception {
        int databaseSizeBeforeUpdate = pollVoteRepository.findAll().size();
        pollVote.setId(longCount.incrementAndGet());

        // Create the PollVote
        PollVoteDTO pollVoteDTO = pollVoteMapper.toDto(pollVote);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPollVoteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pollVoteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PollVote in the database
        List<PollVote> pollVoteList = pollVoteRepository.findAll();
        assertThat(pollVoteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPollVote() throws Exception {
        int databaseSizeBeforeUpdate = pollVoteRepository.findAll().size();
        pollVote.setId(longCount.incrementAndGet());

        // Create the PollVote
        PollVoteDTO pollVoteDTO = pollVoteMapper.toDto(pollVote);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPollVoteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pollVoteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PollVote in the database
        List<PollVote> pollVoteList = pollVoteRepository.findAll();
        assertThat(pollVoteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePollVoteWithPatch() throws Exception {
        // Initialize the database
        pollVoteRepository.saveAndFlush(pollVote);

        int databaseSizeBeforeUpdate = pollVoteRepository.findAll().size();

        // Update the pollVote using partial update
        PollVote partialUpdatedPollVote = new PollVote();
        partialUpdatedPollVote.setId(pollVote.getId());

        restPollVoteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPollVote.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPollVote))
            )
            .andExpect(status().isOk());

        // Validate the PollVote in the database
        List<PollVote> pollVoteList = pollVoteRepository.findAll();
        assertThat(pollVoteList).hasSize(databaseSizeBeforeUpdate);
        PollVote testPollVote = pollVoteList.get(pollVoteList.size() - 1);
        assertThat(testPollVote.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testPollVote.getVotingUserId()).isEqualTo(DEFAULT_VOTING_USER_ID);
    }

    @Test
    @Transactional
    void fullUpdatePollVoteWithPatch() throws Exception {
        // Initialize the database
        pollVoteRepository.saveAndFlush(pollVote);

        int databaseSizeBeforeUpdate = pollVoteRepository.findAll().size();

        // Update the pollVote using partial update
        PollVote partialUpdatedPollVote = new PollVote();
        partialUpdatedPollVote.setId(pollVote.getId());

        partialUpdatedPollVote.createdDate(UPDATED_CREATED_DATE).votingUserId(UPDATED_VOTING_USER_ID);

        restPollVoteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPollVote.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPollVote))
            )
            .andExpect(status().isOk());

        // Validate the PollVote in the database
        List<PollVote> pollVoteList = pollVoteRepository.findAll();
        assertThat(pollVoteList).hasSize(databaseSizeBeforeUpdate);
        PollVote testPollVote = pollVoteList.get(pollVoteList.size() - 1);
        assertThat(testPollVote.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testPollVote.getVotingUserId()).isEqualTo(UPDATED_VOTING_USER_ID);
    }

    @Test
    @Transactional
    void patchNonExistingPollVote() throws Exception {
        int databaseSizeBeforeUpdate = pollVoteRepository.findAll().size();
        pollVote.setId(longCount.incrementAndGet());

        // Create the PollVote
        PollVoteDTO pollVoteDTO = pollVoteMapper.toDto(pollVote);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPollVoteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pollVoteDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pollVoteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PollVote in the database
        List<PollVote> pollVoteList = pollVoteRepository.findAll();
        assertThat(pollVoteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPollVote() throws Exception {
        int databaseSizeBeforeUpdate = pollVoteRepository.findAll().size();
        pollVote.setId(longCount.incrementAndGet());

        // Create the PollVote
        PollVoteDTO pollVoteDTO = pollVoteMapper.toDto(pollVote);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPollVoteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pollVoteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PollVote in the database
        List<PollVote> pollVoteList = pollVoteRepository.findAll();
        assertThat(pollVoteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPollVote() throws Exception {
        int databaseSizeBeforeUpdate = pollVoteRepository.findAll().size();
        pollVote.setId(longCount.incrementAndGet());

        // Create the PollVote
        PollVoteDTO pollVoteDTO = pollVoteMapper.toDto(pollVote);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPollVoteMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(pollVoteDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PollVote in the database
        List<PollVote> pollVoteList = pollVoteRepository.findAll();
        assertThat(pollVoteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePollVote() throws Exception {
        // Initialize the database
        pollVoteRepository.saveAndFlush(pollVote);

        int databaseSizeBeforeDelete = pollVoteRepository.findAll().size();

        // Delete the pollVote
        restPollVoteMockMvc
            .perform(delete(ENTITY_API_URL_ID, pollVote.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PollVote> pollVoteList = pollVoteRepository.findAll();
        assertThat(pollVoteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
