package com.monsterdam.interactions.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.monsterdam.interactions.IntegrationTest;
import com.monsterdam.interactions.domain.PollOption;
import com.monsterdam.interactions.domain.PostPoll;
import com.monsterdam.interactions.repository.PollOptionRepository;
import com.monsterdam.interactions.service.PollOptionService;
import com.monsterdam.interactions.service.dto.PollOptionDTO;
import com.monsterdam.interactions.service.mapper.PollOptionMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link PollOptionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PollOptionResourceIT {

    private static final String DEFAULT_OPTION_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_OPTION_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_VOTE_COUNT = 1;
    private static final Integer UPDATED_VOTE_COUNT = 2;

    private static final String ENTITY_API_URL = "/api/poll-options";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

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
    private EntityManager em;

    @Autowired
    private MockMvc restPollOptionMockMvc;

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
        if (TestUtil.findAll(em, PostPoll.class).isEmpty()) {
            postPoll = PostPollResourceIT.createEntity(em);
            em.persist(postPoll);
            em.flush();
        } else {
            postPoll = TestUtil.findAll(em, PostPoll.class).get(0);
        }
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
        if (TestUtil.findAll(em, PostPoll.class).isEmpty()) {
            postPoll = PostPollResourceIT.createUpdatedEntity(em);
            em.persist(postPoll);
            em.flush();
        } else {
            postPoll = TestUtil.findAll(em, PostPoll.class).get(0);
        }
        pollOption.setPoll(postPoll);
        return pollOption;
    }

    @BeforeEach
    public void initTest() {
        pollOption = createEntity(em);
    }

    @Test
    @Transactional
    void createPollOption() throws Exception {
        int databaseSizeBeforeCreate = pollOptionRepository.findAll().size();
        // Create the PollOption
        PollOptionDTO pollOptionDTO = pollOptionMapper.toDto(pollOption);
        restPollOptionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pollOptionDTO)))
            .andExpect(status().isCreated());

        // Validate the PollOption in the database
        List<PollOption> pollOptionList = pollOptionRepository.findAll();
        assertThat(pollOptionList).hasSize(databaseSizeBeforeCreate + 1);
        PollOption testPollOption = pollOptionList.get(pollOptionList.size() - 1);
        assertThat(testPollOption.getOptionDescription()).isEqualTo(DEFAULT_OPTION_DESCRIPTION);
        assertThat(testPollOption.getVoteCount()).isEqualTo(DEFAULT_VOTE_COUNT);
    }

    @Test
    @Transactional
    void createPollOptionWithExistingId() throws Exception {
        // Create the PollOption with an existing ID
        pollOption.setId(1L);
        PollOptionDTO pollOptionDTO = pollOptionMapper.toDto(pollOption);

        int databaseSizeBeforeCreate = pollOptionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPollOptionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pollOptionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PollOption in the database
        List<PollOption> pollOptionList = pollOptionRepository.findAll();
        assertThat(pollOptionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkVoteCountIsRequired() throws Exception {
        int databaseSizeBeforeTest = pollOptionRepository.findAll().size();
        // set the field null
        pollOption.setVoteCount(null);

        // Create the PollOption, which fails.
        PollOptionDTO pollOptionDTO = pollOptionMapper.toDto(pollOption);

        restPollOptionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pollOptionDTO)))
            .andExpect(status().isBadRequest());

        List<PollOption> pollOptionList = pollOptionRepository.findAll();
        assertThat(pollOptionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPollOptions() throws Exception {
        // Initialize the database
        pollOptionRepository.saveAndFlush(pollOption);

        // Get all the pollOptionList
        restPollOptionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pollOption.getId().intValue())))
            .andExpect(jsonPath("$.[*].optionDescription").value(hasItem(DEFAULT_OPTION_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].voteCount").value(hasItem(DEFAULT_VOTE_COUNT)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPollOptionsWithEagerRelationshipsIsEnabled() throws Exception {
        when(pollOptionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPollOptionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(pollOptionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPollOptionsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(pollOptionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPollOptionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(pollOptionRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPollOption() throws Exception {
        // Initialize the database
        pollOptionRepository.saveAndFlush(pollOption);

        // Get the pollOption
        restPollOptionMockMvc
            .perform(get(ENTITY_API_URL_ID, pollOption.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pollOption.getId().intValue()))
            .andExpect(jsonPath("$.optionDescription").value(DEFAULT_OPTION_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.voteCount").value(DEFAULT_VOTE_COUNT));
    }

    @Test
    @Transactional
    void getNonExistingPollOption() throws Exception {
        // Get the pollOption
        restPollOptionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPollOption() throws Exception {
        // Initialize the database
        pollOptionRepository.saveAndFlush(pollOption);

        int databaseSizeBeforeUpdate = pollOptionRepository.findAll().size();

        // Update the pollOption
        PollOption updatedPollOption = pollOptionRepository.findById(pollOption.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPollOption are not directly saved in db
        em.detach(updatedPollOption);
        updatedPollOption.optionDescription(UPDATED_OPTION_DESCRIPTION).voteCount(UPDATED_VOTE_COUNT);
        PollOptionDTO pollOptionDTO = pollOptionMapper.toDto(updatedPollOption);

        restPollOptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pollOptionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pollOptionDTO))
            )
            .andExpect(status().isOk());

        // Validate the PollOption in the database
        List<PollOption> pollOptionList = pollOptionRepository.findAll();
        assertThat(pollOptionList).hasSize(databaseSizeBeforeUpdate);
        PollOption testPollOption = pollOptionList.get(pollOptionList.size() - 1);
        assertThat(testPollOption.getOptionDescription()).isEqualTo(UPDATED_OPTION_DESCRIPTION);
        assertThat(testPollOption.getVoteCount()).isEqualTo(UPDATED_VOTE_COUNT);
    }

    @Test
    @Transactional
    void putNonExistingPollOption() throws Exception {
        int databaseSizeBeforeUpdate = pollOptionRepository.findAll().size();
        pollOption.setId(longCount.incrementAndGet());

        // Create the PollOption
        PollOptionDTO pollOptionDTO = pollOptionMapper.toDto(pollOption);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPollOptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pollOptionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pollOptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PollOption in the database
        List<PollOption> pollOptionList = pollOptionRepository.findAll();
        assertThat(pollOptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPollOption() throws Exception {
        int databaseSizeBeforeUpdate = pollOptionRepository.findAll().size();
        pollOption.setId(longCount.incrementAndGet());

        // Create the PollOption
        PollOptionDTO pollOptionDTO = pollOptionMapper.toDto(pollOption);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPollOptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pollOptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PollOption in the database
        List<PollOption> pollOptionList = pollOptionRepository.findAll();
        assertThat(pollOptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPollOption() throws Exception {
        int databaseSizeBeforeUpdate = pollOptionRepository.findAll().size();
        pollOption.setId(longCount.incrementAndGet());

        // Create the PollOption
        PollOptionDTO pollOptionDTO = pollOptionMapper.toDto(pollOption);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPollOptionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pollOptionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PollOption in the database
        List<PollOption> pollOptionList = pollOptionRepository.findAll();
        assertThat(pollOptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePollOptionWithPatch() throws Exception {
        // Initialize the database
        pollOptionRepository.saveAndFlush(pollOption);

        int databaseSizeBeforeUpdate = pollOptionRepository.findAll().size();

        // Update the pollOption using partial update
        PollOption partialUpdatedPollOption = new PollOption();
        partialUpdatedPollOption.setId(pollOption.getId());

        partialUpdatedPollOption.optionDescription(UPDATED_OPTION_DESCRIPTION);

        restPollOptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPollOption.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPollOption))
            )
            .andExpect(status().isOk());

        // Validate the PollOption in the database
        List<PollOption> pollOptionList = pollOptionRepository.findAll();
        assertThat(pollOptionList).hasSize(databaseSizeBeforeUpdate);
        PollOption testPollOption = pollOptionList.get(pollOptionList.size() - 1);
        assertThat(testPollOption.getOptionDescription()).isEqualTo(UPDATED_OPTION_DESCRIPTION);
        assertThat(testPollOption.getVoteCount()).isEqualTo(DEFAULT_VOTE_COUNT);
    }

    @Test
    @Transactional
    void fullUpdatePollOptionWithPatch() throws Exception {
        // Initialize the database
        pollOptionRepository.saveAndFlush(pollOption);

        int databaseSizeBeforeUpdate = pollOptionRepository.findAll().size();

        // Update the pollOption using partial update
        PollOption partialUpdatedPollOption = new PollOption();
        partialUpdatedPollOption.setId(pollOption.getId());

        partialUpdatedPollOption.optionDescription(UPDATED_OPTION_DESCRIPTION).voteCount(UPDATED_VOTE_COUNT);

        restPollOptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPollOption.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPollOption))
            )
            .andExpect(status().isOk());

        // Validate the PollOption in the database
        List<PollOption> pollOptionList = pollOptionRepository.findAll();
        assertThat(pollOptionList).hasSize(databaseSizeBeforeUpdate);
        PollOption testPollOption = pollOptionList.get(pollOptionList.size() - 1);
        assertThat(testPollOption.getOptionDescription()).isEqualTo(UPDATED_OPTION_DESCRIPTION);
        assertThat(testPollOption.getVoteCount()).isEqualTo(UPDATED_VOTE_COUNT);
    }

    @Test
    @Transactional
    void patchNonExistingPollOption() throws Exception {
        int databaseSizeBeforeUpdate = pollOptionRepository.findAll().size();
        pollOption.setId(longCount.incrementAndGet());

        // Create the PollOption
        PollOptionDTO pollOptionDTO = pollOptionMapper.toDto(pollOption);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPollOptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pollOptionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pollOptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PollOption in the database
        List<PollOption> pollOptionList = pollOptionRepository.findAll();
        assertThat(pollOptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPollOption() throws Exception {
        int databaseSizeBeforeUpdate = pollOptionRepository.findAll().size();
        pollOption.setId(longCount.incrementAndGet());

        // Create the PollOption
        PollOptionDTO pollOptionDTO = pollOptionMapper.toDto(pollOption);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPollOptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pollOptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PollOption in the database
        List<PollOption> pollOptionList = pollOptionRepository.findAll();
        assertThat(pollOptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPollOption() throws Exception {
        int databaseSizeBeforeUpdate = pollOptionRepository.findAll().size();
        pollOption.setId(longCount.incrementAndGet());

        // Create the PollOption
        PollOptionDTO pollOptionDTO = pollOptionMapper.toDto(pollOption);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPollOptionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(pollOptionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PollOption in the database
        List<PollOption> pollOptionList = pollOptionRepository.findAll();
        assertThat(pollOptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePollOption() throws Exception {
        // Initialize the database
        pollOptionRepository.saveAndFlush(pollOption);

        int databaseSizeBeforeDelete = pollOptionRepository.findAll().size();

        // Delete the pollOption
        restPollOptionMockMvc
            .perform(delete(ENTITY_API_URL_ID, pollOption.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PollOption> pollOptionList = pollOptionRepository.findAll();
        assertThat(pollOptionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
