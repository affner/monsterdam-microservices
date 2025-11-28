package com.monsterdam.profile.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.monsterdam.profile.IntegrationTest;
import com.monsterdam.profile.domain.BookMark;
import com.monsterdam.profile.repository.BookMarkRepository;
import com.monsterdam.profile.service.dto.BookMarkDTO;
import com.monsterdam.profile.service.mapper.BookMarkMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link BookMarkResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BookMarkResourceIT {

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

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;

    private static final Long DEFAULT_POST_ID = 1L;
    private static final Long UPDATED_POST_ID = 2L;

    private static final Long DEFAULT_MESSAGE_ID = 1L;
    private static final Long UPDATED_MESSAGE_ID = 2L;

    private static final String ENTITY_API_URL = "/api/book-marks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BookMarkRepository bookMarkRepository;

    @Autowired
    private BookMarkMapper bookMarkMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBookMarkMockMvc;

    private BookMark bookMark;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BookMark createEntity(EntityManager em) {
        BookMark bookMark = new BookMark()
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .isDeleted(DEFAULT_IS_DELETED)
            .userId(DEFAULT_USER_ID)
            .postId(DEFAULT_POST_ID)
            .messageId(DEFAULT_MESSAGE_ID);
        return bookMark;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BookMark createUpdatedEntity(EntityManager em) {
        BookMark bookMark = new BookMark()
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .userId(UPDATED_USER_ID)
            .postId(UPDATED_POST_ID)
            .messageId(UPDATED_MESSAGE_ID);
        return bookMark;
    }

    @BeforeEach
    public void initTest() {
        bookMark = createEntity(em);
    }

    @Test
    @Transactional
    void createBookMark() throws Exception {
        int databaseSizeBeforeCreate = bookMarkRepository.findAll().size();
        // Create the BookMark
        BookMarkDTO bookMarkDTO = bookMarkMapper.toDto(bookMark);
        restBookMarkMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bookMarkDTO)))
            .andExpect(status().isCreated());

        // Validate the BookMark in the database
        List<BookMark> bookMarkList = bookMarkRepository.findAll();
        assertThat(bookMarkList).hasSize(databaseSizeBeforeCreate + 1);
        BookMark testBookMark = bookMarkList.get(bookMarkList.size() - 1);
        assertThat(testBookMark.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testBookMark.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testBookMark.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testBookMark.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testBookMark.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
        assertThat(testBookMark.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testBookMark.getPostId()).isEqualTo(DEFAULT_POST_ID);
        assertThat(testBookMark.getMessageId()).isEqualTo(DEFAULT_MESSAGE_ID);
    }

    @Test
    @Transactional
    void createBookMarkWithExistingId() throws Exception {
        // Create the BookMark with an existing ID
        bookMark.setId(1L);
        BookMarkDTO bookMarkDTO = bookMarkMapper.toDto(bookMark);

        int databaseSizeBeforeCreate = bookMarkRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBookMarkMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bookMarkDTO)))
            .andExpect(status().isBadRequest());

        // Validate the BookMark in the database
        List<BookMark> bookMarkList = bookMarkRepository.findAll();
        assertThat(bookMarkList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookMarkRepository.findAll().size();
        // set the field null
        bookMark.setCreatedDate(null);

        // Create the BookMark, which fails.
        BookMarkDTO bookMarkDTO = bookMarkMapper.toDto(bookMark);

        restBookMarkMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bookMarkDTO)))
            .andExpect(status().isBadRequest());

        List<BookMark> bookMarkList = bookMarkRepository.findAll();
        assertThat(bookMarkList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookMarkRepository.findAll().size();
        // set the field null
        bookMark.setIsDeleted(null);

        // Create the BookMark, which fails.
        BookMarkDTO bookMarkDTO = bookMarkMapper.toDto(bookMark);

        restBookMarkMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bookMarkDTO)))
            .andExpect(status().isBadRequest());

        List<BookMark> bookMarkList = bookMarkRepository.findAll();
        assertThat(bookMarkList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBookMarks() throws Exception {
        // Initialize the database
        bookMarkRepository.saveAndFlush(bookMark);

        // Get all the bookMarkList
        restBookMarkMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bookMark.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].postId").value(hasItem(DEFAULT_POST_ID.intValue())))
            .andExpect(jsonPath("$.[*].messageId").value(hasItem(DEFAULT_MESSAGE_ID.intValue())));
    }

    @Test
    @Transactional
    void getBookMark() throws Exception {
        // Initialize the database
        bookMarkRepository.saveAndFlush(bookMark);

        // Get the bookMark
        restBookMarkMockMvc
            .perform(get(ENTITY_API_URL_ID, bookMark.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bookMark.getId().intValue()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()))
            .andExpect(jsonPath("$.postId").value(DEFAULT_POST_ID.intValue()))
            .andExpect(jsonPath("$.messageId").value(DEFAULT_MESSAGE_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingBookMark() throws Exception {
        // Get the bookMark
        restBookMarkMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBookMark() throws Exception {
        // Initialize the database
        bookMarkRepository.saveAndFlush(bookMark);

        int databaseSizeBeforeUpdate = bookMarkRepository.findAll().size();

        // Update the bookMark
        BookMark updatedBookMark = bookMarkRepository.findById(bookMark.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBookMark are not directly saved in db
        em.detach(updatedBookMark);
        updatedBookMark
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .userId(UPDATED_USER_ID)
            .postId(UPDATED_POST_ID)
            .messageId(UPDATED_MESSAGE_ID);
        BookMarkDTO bookMarkDTO = bookMarkMapper.toDto(updatedBookMark);

        restBookMarkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bookMarkDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bookMarkDTO))
            )
            .andExpect(status().isOk());

        // Validate the BookMark in the database
        List<BookMark> bookMarkList = bookMarkRepository.findAll();
        assertThat(bookMarkList).hasSize(databaseSizeBeforeUpdate);
        BookMark testBookMark = bookMarkList.get(bookMarkList.size() - 1);
        assertThat(testBookMark.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testBookMark.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testBookMark.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testBookMark.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testBookMark.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testBookMark.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testBookMark.getPostId()).isEqualTo(UPDATED_POST_ID);
        assertThat(testBookMark.getMessageId()).isEqualTo(UPDATED_MESSAGE_ID);
    }

    @Test
    @Transactional
    void putNonExistingBookMark() throws Exception {
        int databaseSizeBeforeUpdate = bookMarkRepository.findAll().size();
        bookMark.setId(longCount.incrementAndGet());

        // Create the BookMark
        BookMarkDTO bookMarkDTO = bookMarkMapper.toDto(bookMark);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBookMarkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bookMarkDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bookMarkDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookMark in the database
        List<BookMark> bookMarkList = bookMarkRepository.findAll();
        assertThat(bookMarkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBookMark() throws Exception {
        int databaseSizeBeforeUpdate = bookMarkRepository.findAll().size();
        bookMark.setId(longCount.incrementAndGet());

        // Create the BookMark
        BookMarkDTO bookMarkDTO = bookMarkMapper.toDto(bookMark);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookMarkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bookMarkDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookMark in the database
        List<BookMark> bookMarkList = bookMarkRepository.findAll();
        assertThat(bookMarkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBookMark() throws Exception {
        int databaseSizeBeforeUpdate = bookMarkRepository.findAll().size();
        bookMark.setId(longCount.incrementAndGet());

        // Create the BookMark
        BookMarkDTO bookMarkDTO = bookMarkMapper.toDto(bookMark);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookMarkMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bookMarkDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BookMark in the database
        List<BookMark> bookMarkList = bookMarkRepository.findAll();
        assertThat(bookMarkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBookMarkWithPatch() throws Exception {
        // Initialize the database
        bookMarkRepository.saveAndFlush(bookMark);

        int databaseSizeBeforeUpdate = bookMarkRepository.findAll().size();

        // Update the bookMark using partial update
        BookMark partialUpdatedBookMark = new BookMark();
        partialUpdatedBookMark.setId(bookMark.getId());

        partialUpdatedBookMark
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .userId(UPDATED_USER_ID)
            .postId(UPDATED_POST_ID)
            .messageId(UPDATED_MESSAGE_ID);

        restBookMarkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBookMark.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBookMark))
            )
            .andExpect(status().isOk());

        // Validate the BookMark in the database
        List<BookMark> bookMarkList = bookMarkRepository.findAll();
        assertThat(bookMarkList).hasSize(databaseSizeBeforeUpdate);
        BookMark testBookMark = bookMarkList.get(bookMarkList.size() - 1);
        assertThat(testBookMark.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testBookMark.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testBookMark.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testBookMark.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testBookMark.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
        assertThat(testBookMark.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testBookMark.getPostId()).isEqualTo(UPDATED_POST_ID);
        assertThat(testBookMark.getMessageId()).isEqualTo(UPDATED_MESSAGE_ID);
    }

    @Test
    @Transactional
    void fullUpdateBookMarkWithPatch() throws Exception {
        // Initialize the database
        bookMarkRepository.saveAndFlush(bookMark);

        int databaseSizeBeforeUpdate = bookMarkRepository.findAll().size();

        // Update the bookMark using partial update
        BookMark partialUpdatedBookMark = new BookMark();
        partialUpdatedBookMark.setId(bookMark.getId());

        partialUpdatedBookMark
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .userId(UPDATED_USER_ID)
            .postId(UPDATED_POST_ID)
            .messageId(UPDATED_MESSAGE_ID);

        restBookMarkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBookMark.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBookMark))
            )
            .andExpect(status().isOk());

        // Validate the BookMark in the database
        List<BookMark> bookMarkList = bookMarkRepository.findAll();
        assertThat(bookMarkList).hasSize(databaseSizeBeforeUpdate);
        BookMark testBookMark = bookMarkList.get(bookMarkList.size() - 1);
        assertThat(testBookMark.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testBookMark.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testBookMark.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testBookMark.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testBookMark.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testBookMark.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testBookMark.getPostId()).isEqualTo(UPDATED_POST_ID);
        assertThat(testBookMark.getMessageId()).isEqualTo(UPDATED_MESSAGE_ID);
    }

    @Test
    @Transactional
    void patchNonExistingBookMark() throws Exception {
        int databaseSizeBeforeUpdate = bookMarkRepository.findAll().size();
        bookMark.setId(longCount.incrementAndGet());

        // Create the BookMark
        BookMarkDTO bookMarkDTO = bookMarkMapper.toDto(bookMark);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBookMarkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, bookMarkDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bookMarkDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookMark in the database
        List<BookMark> bookMarkList = bookMarkRepository.findAll();
        assertThat(bookMarkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBookMark() throws Exception {
        int databaseSizeBeforeUpdate = bookMarkRepository.findAll().size();
        bookMark.setId(longCount.incrementAndGet());

        // Create the BookMark
        BookMarkDTO bookMarkDTO = bookMarkMapper.toDto(bookMark);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookMarkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bookMarkDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookMark in the database
        List<BookMark> bookMarkList = bookMarkRepository.findAll();
        assertThat(bookMarkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBookMark() throws Exception {
        int databaseSizeBeforeUpdate = bookMarkRepository.findAll().size();
        bookMark.setId(longCount.incrementAndGet());

        // Create the BookMark
        BookMarkDTO bookMarkDTO = bookMarkMapper.toDto(bookMark);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookMarkMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(bookMarkDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BookMark in the database
        List<BookMark> bookMarkList = bookMarkRepository.findAll();
        assertThat(bookMarkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBookMark() throws Exception {
        // Initialize the database
        bookMarkRepository.saveAndFlush(bookMark);

        int databaseSizeBeforeDelete = bookMarkRepository.findAll().size();

        // Delete the bookMark
        restBookMarkMockMvc
            .perform(delete(ENTITY_API_URL_ID, bookMark.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BookMark> bookMarkList = bookMarkRepository.findAll();
        assertThat(bookMarkList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
