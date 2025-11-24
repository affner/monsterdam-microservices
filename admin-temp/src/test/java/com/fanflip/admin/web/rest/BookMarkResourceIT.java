package com.monsterdam.admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.monsterdam.admin.IntegrationTest;
import com.monsterdam.admin.domain.BookMark;
import com.monsterdam.admin.domain.DirectMessage;
import com.monsterdam.admin.domain.PostFeed;
import com.monsterdam.admin.domain.UserProfile;
import com.monsterdam.admin.repository.BookMarkRepository;
import com.monsterdam.admin.repository.EntityManager;
import com.monsterdam.admin.repository.search.BookMarkSearchRepository;
import com.monsterdam.admin.service.BookMarkService;
import com.monsterdam.admin.service.dto.BookMarkDTO;
import com.monsterdam.admin.service.mapper.BookMarkMapper;
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
 * Integration tests for the {@link BookMarkResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
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

    private static final String ENTITY_API_URL = "/api/book-marks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/book-marks/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BookMarkRepository bookMarkRepository;

    @Mock
    private BookMarkRepository bookMarkRepositoryMock;

    @Autowired
    private BookMarkMapper bookMarkMapper;

    @Mock
    private BookMarkService bookMarkServiceMock;

    @Autowired
    private BookMarkSearchRepository bookMarkSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

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
            .isDeleted(DEFAULT_IS_DELETED);
        // Add required entity
        PostFeed postFeed;
        postFeed = em.insert(PostFeedResourceIT.createEntity(em)).block();
        bookMark.setPost(postFeed);
        // Add required entity
        DirectMessage directMessage;
        directMessage = em.insert(DirectMessageResourceIT.createEntity(em)).block();
        bookMark.setMessage(directMessage);
        // Add required entity
        UserProfile userProfile;
        userProfile = em.insert(UserProfileResourceIT.createEntity(em)).block();
        bookMark.setUser(userProfile);
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
            .isDeleted(UPDATED_IS_DELETED);
        // Add required entity
        PostFeed postFeed;
        postFeed = em.insert(PostFeedResourceIT.createUpdatedEntity(em)).block();
        bookMark.setPost(postFeed);
        // Add required entity
        DirectMessage directMessage;
        directMessage = em.insert(DirectMessageResourceIT.createUpdatedEntity(em)).block();
        bookMark.setMessage(directMessage);
        // Add required entity
        UserProfile userProfile;
        userProfile = em.insert(UserProfileResourceIT.createUpdatedEntity(em)).block();
        bookMark.setUser(userProfile);
        return bookMark;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(BookMark.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
        PostFeedResourceIT.deleteEntities(em);
        DirectMessageResourceIT.deleteEntities(em);
        UserProfileResourceIT.deleteEntities(em);
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        bookMarkSearchRepository.deleteAll().block();
        assertThat(bookMarkSearchRepository.count().block()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        bookMark = createEntity(em);
    }

    @Test
    void createBookMark() throws Exception {
        int databaseSizeBeforeCreate = bookMarkRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(bookMarkSearchRepository.findAll().collectList().block());
        // Create the BookMark
        BookMarkDTO bookMarkDTO = bookMarkMapper.toDto(bookMark);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(bookMarkDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the BookMark in the database
        List<BookMark> bookMarkList = bookMarkRepository.findAll().collectList().block();
        assertThat(bookMarkList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(bookMarkSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        BookMark testBookMark = bookMarkList.get(bookMarkList.size() - 1);
        assertThat(testBookMark.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testBookMark.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testBookMark.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testBookMark.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testBookMark.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    void createBookMarkWithExistingId() throws Exception {
        // Create the BookMark with an existing ID
        bookMark.setId(1L);
        BookMarkDTO bookMarkDTO = bookMarkMapper.toDto(bookMark);

        int databaseSizeBeforeCreate = bookMarkRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(bookMarkSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(bookMarkDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the BookMark in the database
        List<BookMark> bookMarkList = bookMarkRepository.findAll().collectList().block();
        assertThat(bookMarkList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(bookMarkSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookMarkRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(bookMarkSearchRepository.findAll().collectList().block());
        // set the field null
        bookMark.setCreatedDate(null);

        // Create the BookMark, which fails.
        BookMarkDTO bookMarkDTO = bookMarkMapper.toDto(bookMark);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(bookMarkDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<BookMark> bookMarkList = bookMarkRepository.findAll().collectList().block();
        assertThat(bookMarkList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(bookMarkSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookMarkRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(bookMarkSearchRepository.findAll().collectList().block());
        // set the field null
        bookMark.setIsDeleted(null);

        // Create the BookMark, which fails.
        BookMarkDTO bookMarkDTO = bookMarkMapper.toDto(bookMark);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(bookMarkDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<BookMark> bookMarkList = bookMarkRepository.findAll().collectList().block();
        assertThat(bookMarkList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(bookMarkSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllBookMarks() {
        // Initialize the database
        bookMarkRepository.save(bookMark).block();

        // Get all the bookMarkList
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
            .value(hasItem(bookMark.getId().intValue()))
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
    void getAllBookMarksWithEagerRelationshipsIsEnabled() {
        when(bookMarkServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(bookMarkServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBookMarksWithEagerRelationshipsIsNotEnabled() {
        when(bookMarkServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(bookMarkRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getBookMark() {
        // Initialize the database
        bookMarkRepository.save(bookMark).block();

        // Get the bookMark
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, bookMark.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(bookMark.getId().intValue()))
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
    void getNonExistingBookMark() {
        // Get the bookMark
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingBookMark() throws Exception {
        // Initialize the database
        bookMarkRepository.save(bookMark).block();

        int databaseSizeBeforeUpdate = bookMarkRepository.findAll().collectList().block().size();
        bookMarkSearchRepository.save(bookMark).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(bookMarkSearchRepository.findAll().collectList().block());

        // Update the bookMark
        BookMark updatedBookMark = bookMarkRepository.findById(bookMark.getId()).block();
        updatedBookMark
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        BookMarkDTO bookMarkDTO = bookMarkMapper.toDto(updatedBookMark);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, bookMarkDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(bookMarkDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the BookMark in the database
        List<BookMark> bookMarkList = bookMarkRepository.findAll().collectList().block();
        assertThat(bookMarkList).hasSize(databaseSizeBeforeUpdate);
        BookMark testBookMark = bookMarkList.get(bookMarkList.size() - 1);
        assertThat(testBookMark.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testBookMark.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testBookMark.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testBookMark.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testBookMark.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(bookMarkSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<BookMark> bookMarkSearchList = IterableUtils.toList(bookMarkSearchRepository.findAll().collectList().block());
                BookMark testBookMarkSearch = bookMarkSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testBookMarkSearch.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
                assertThat(testBookMarkSearch.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
                assertThat(testBookMarkSearch.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
                assertThat(testBookMarkSearch.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
                assertThat(testBookMarkSearch.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
            });
    }

    @Test
    void putNonExistingBookMark() throws Exception {
        int databaseSizeBeforeUpdate = bookMarkRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(bookMarkSearchRepository.findAll().collectList().block());
        bookMark.setId(longCount.incrementAndGet());

        // Create the BookMark
        BookMarkDTO bookMarkDTO = bookMarkMapper.toDto(bookMark);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, bookMarkDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(bookMarkDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the BookMark in the database
        List<BookMark> bookMarkList = bookMarkRepository.findAll().collectList().block();
        assertThat(bookMarkList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(bookMarkSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchBookMark() throws Exception {
        int databaseSizeBeforeUpdate = bookMarkRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(bookMarkSearchRepository.findAll().collectList().block());
        bookMark.setId(longCount.incrementAndGet());

        // Create the BookMark
        BookMarkDTO bookMarkDTO = bookMarkMapper.toDto(bookMark);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(bookMarkDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the BookMark in the database
        List<BookMark> bookMarkList = bookMarkRepository.findAll().collectList().block();
        assertThat(bookMarkList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(bookMarkSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamBookMark() throws Exception {
        int databaseSizeBeforeUpdate = bookMarkRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(bookMarkSearchRepository.findAll().collectList().block());
        bookMark.setId(longCount.incrementAndGet());

        // Create the BookMark
        BookMarkDTO bookMarkDTO = bookMarkMapper.toDto(bookMark);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(bookMarkDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the BookMark in the database
        List<BookMark> bookMarkList = bookMarkRepository.findAll().collectList().block();
        assertThat(bookMarkList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(bookMarkSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateBookMarkWithPatch() throws Exception {
        // Initialize the database
        bookMarkRepository.save(bookMark).block();

        int databaseSizeBeforeUpdate = bookMarkRepository.findAll().collectList().block().size();

        // Update the bookMark using partial update
        BookMark partialUpdatedBookMark = new BookMark();
        partialUpdatedBookMark.setId(bookMark.getId());

        partialUpdatedBookMark
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .isDeleted(UPDATED_IS_DELETED);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedBookMark.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedBookMark))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the BookMark in the database
        List<BookMark> bookMarkList = bookMarkRepository.findAll().collectList().block();
        assertThat(bookMarkList).hasSize(databaseSizeBeforeUpdate);
        BookMark testBookMark = bookMarkList.get(bookMarkList.size() - 1);
        assertThat(testBookMark.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testBookMark.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testBookMark.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testBookMark.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testBookMark.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    void fullUpdateBookMarkWithPatch() throws Exception {
        // Initialize the database
        bookMarkRepository.save(bookMark).block();

        int databaseSizeBeforeUpdate = bookMarkRepository.findAll().collectList().block().size();

        // Update the bookMark using partial update
        BookMark partialUpdatedBookMark = new BookMark();
        partialUpdatedBookMark.setId(bookMark.getId());

        partialUpdatedBookMark
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedBookMark.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedBookMark))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the BookMark in the database
        List<BookMark> bookMarkList = bookMarkRepository.findAll().collectList().block();
        assertThat(bookMarkList).hasSize(databaseSizeBeforeUpdate);
        BookMark testBookMark = bookMarkList.get(bookMarkList.size() - 1);
        assertThat(testBookMark.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testBookMark.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testBookMark.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testBookMark.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testBookMark.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    void patchNonExistingBookMark() throws Exception {
        int databaseSizeBeforeUpdate = bookMarkRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(bookMarkSearchRepository.findAll().collectList().block());
        bookMark.setId(longCount.incrementAndGet());

        // Create the BookMark
        BookMarkDTO bookMarkDTO = bookMarkMapper.toDto(bookMark);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, bookMarkDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(bookMarkDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the BookMark in the database
        List<BookMark> bookMarkList = bookMarkRepository.findAll().collectList().block();
        assertThat(bookMarkList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(bookMarkSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchBookMark() throws Exception {
        int databaseSizeBeforeUpdate = bookMarkRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(bookMarkSearchRepository.findAll().collectList().block());
        bookMark.setId(longCount.incrementAndGet());

        // Create the BookMark
        BookMarkDTO bookMarkDTO = bookMarkMapper.toDto(bookMark);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(bookMarkDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the BookMark in the database
        List<BookMark> bookMarkList = bookMarkRepository.findAll().collectList().block();
        assertThat(bookMarkList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(bookMarkSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamBookMark() throws Exception {
        int databaseSizeBeforeUpdate = bookMarkRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(bookMarkSearchRepository.findAll().collectList().block());
        bookMark.setId(longCount.incrementAndGet());

        // Create the BookMark
        BookMarkDTO bookMarkDTO = bookMarkMapper.toDto(bookMark);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(bookMarkDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the BookMark in the database
        List<BookMark> bookMarkList = bookMarkRepository.findAll().collectList().block();
        assertThat(bookMarkList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(bookMarkSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteBookMark() {
        // Initialize the database
        bookMarkRepository.save(bookMark).block();
        bookMarkRepository.save(bookMark).block();
        bookMarkSearchRepository.save(bookMark).block();

        int databaseSizeBeforeDelete = bookMarkRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(bookMarkSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the bookMark
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, bookMark.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<BookMark> bookMarkList = bookMarkRepository.findAll().collectList().block();
        assertThat(bookMarkList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(bookMarkSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchBookMark() {
        // Initialize the database
        bookMark = bookMarkRepository.save(bookMark).block();
        bookMarkSearchRepository.save(bookMark).block();

        // Search the bookMark
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + bookMark.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(bookMark.getId().intValue()))
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
