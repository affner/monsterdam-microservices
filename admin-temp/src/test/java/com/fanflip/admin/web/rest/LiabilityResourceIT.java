package com.fanflip.admin.web.rest;

import static com.fanflip.admin.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.fanflip.admin.IntegrationTest;
import com.fanflip.admin.domain.Liability;
import com.fanflip.admin.domain.enumeration.LiabilityType;
import com.fanflip.admin.repository.EntityManager;
import com.fanflip.admin.repository.LiabilityRepository;
import com.fanflip.admin.repository.search.LiabilitySearchRepository;
import com.fanflip.admin.service.dto.LiabilityDTO;
import com.fanflip.admin.service.mapper.LiabilityMapper;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link LiabilityResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class LiabilityResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);

    private static final LocalDate DEFAULT_DUE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DUE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LiabilityType DEFAULT_TYPE = LiabilityType.CURRENT;
    private static final LiabilityType UPDATED_TYPE = LiabilityType.LONG_TERM;

    private static final String ENTITY_API_URL = "/api/liabilities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/liabilities/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LiabilityRepository liabilityRepository;

    @Autowired
    private LiabilityMapper liabilityMapper;

    @Autowired
    private LiabilitySearchRepository liabilitySearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Liability liability;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Liability createEntity(EntityManager em) {
        Liability liability = new Liability().name(DEFAULT_NAME).amount(DEFAULT_AMOUNT).dueDate(DEFAULT_DUE_DATE).type(DEFAULT_TYPE);
        return liability;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Liability createUpdatedEntity(EntityManager em) {
        Liability liability = new Liability().name(UPDATED_NAME).amount(UPDATED_AMOUNT).dueDate(UPDATED_DUE_DATE).type(UPDATED_TYPE);
        return liability;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Liability.class).block();
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
        liabilitySearchRepository.deleteAll().block();
        assertThat(liabilitySearchRepository.count().block()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        liability = createEntity(em);
    }

    @Test
    void createLiability() throws Exception {
        int databaseSizeBeforeCreate = liabilityRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(liabilitySearchRepository.findAll().collectList().block());
        // Create the Liability
        LiabilityDTO liabilityDTO = liabilityMapper.toDto(liability);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(liabilityDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Liability in the database
        List<Liability> liabilityList = liabilityRepository.findAll().collectList().block();
        assertThat(liabilityList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(liabilitySearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        Liability testLiability = liabilityList.get(liabilityList.size() - 1);
        assertThat(testLiability.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testLiability.getAmount()).isEqualByComparingTo(DEFAULT_AMOUNT);
        assertThat(testLiability.getDueDate()).isEqualTo(DEFAULT_DUE_DATE);
        assertThat(testLiability.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    void createLiabilityWithExistingId() throws Exception {
        // Create the Liability with an existing ID
        liability.setId(1L);
        LiabilityDTO liabilityDTO = liabilityMapper.toDto(liability);

        int databaseSizeBeforeCreate = liabilityRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(liabilitySearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(liabilityDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Liability in the database
        List<Liability> liabilityList = liabilityRepository.findAll().collectList().block();
        assertThat(liabilityList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(liabilitySearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = liabilityRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(liabilitySearchRepository.findAll().collectList().block());
        // set the field null
        liability.setName(null);

        // Create the Liability, which fails.
        LiabilityDTO liabilityDTO = liabilityMapper.toDto(liability);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(liabilityDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Liability> liabilityList = liabilityRepository.findAll().collectList().block();
        assertThat(liabilityList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(liabilitySearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = liabilityRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(liabilitySearchRepository.findAll().collectList().block());
        // set the field null
        liability.setAmount(null);

        // Create the Liability, which fails.
        LiabilityDTO liabilityDTO = liabilityMapper.toDto(liability);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(liabilityDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Liability> liabilityList = liabilityRepository.findAll().collectList().block();
        assertThat(liabilityList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(liabilitySearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = liabilityRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(liabilitySearchRepository.findAll().collectList().block());
        // set the field null
        liability.setType(null);

        // Create the Liability, which fails.
        LiabilityDTO liabilityDTO = liabilityMapper.toDto(liability);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(liabilityDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Liability> liabilityList = liabilityRepository.findAll().collectList().block();
        assertThat(liabilityList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(liabilitySearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllLiabilities() {
        // Initialize the database
        liabilityRepository.save(liability).block();

        // Get all the liabilityList
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
            .value(hasItem(liability.getId().intValue()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].amount")
            .value(hasItem(sameNumber(DEFAULT_AMOUNT)))
            .jsonPath("$.[*].dueDate")
            .value(hasItem(DEFAULT_DUE_DATE.toString()))
            .jsonPath("$.[*].type")
            .value(hasItem(DEFAULT_TYPE.toString()));
    }

    @Test
    void getLiability() {
        // Initialize the database
        liabilityRepository.save(liability).block();

        // Get the liability
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, liability.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(liability.getId().intValue()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.amount")
            .value(is(sameNumber(DEFAULT_AMOUNT)))
            .jsonPath("$.dueDate")
            .value(is(DEFAULT_DUE_DATE.toString()))
            .jsonPath("$.type")
            .value(is(DEFAULT_TYPE.toString()));
    }

    @Test
    void getNonExistingLiability() {
        // Get the liability
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingLiability() throws Exception {
        // Initialize the database
        liabilityRepository.save(liability).block();

        int databaseSizeBeforeUpdate = liabilityRepository.findAll().collectList().block().size();
        liabilitySearchRepository.save(liability).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(liabilitySearchRepository.findAll().collectList().block());

        // Update the liability
        Liability updatedLiability = liabilityRepository.findById(liability.getId()).block();
        updatedLiability.name(UPDATED_NAME).amount(UPDATED_AMOUNT).dueDate(UPDATED_DUE_DATE).type(UPDATED_TYPE);
        LiabilityDTO liabilityDTO = liabilityMapper.toDto(updatedLiability);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, liabilityDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(liabilityDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Liability in the database
        List<Liability> liabilityList = liabilityRepository.findAll().collectList().block();
        assertThat(liabilityList).hasSize(databaseSizeBeforeUpdate);
        Liability testLiability = liabilityList.get(liabilityList.size() - 1);
        assertThat(testLiability.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testLiability.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
        assertThat(testLiability.getDueDate()).isEqualTo(UPDATED_DUE_DATE);
        assertThat(testLiability.getType()).isEqualTo(UPDATED_TYPE);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(liabilitySearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Liability> liabilitySearchList = IterableUtils.toList(liabilitySearchRepository.findAll().collectList().block());
                Liability testLiabilitySearch = liabilitySearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testLiabilitySearch.getName()).isEqualTo(UPDATED_NAME);
                assertThat(testLiabilitySearch.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
                assertThat(testLiabilitySearch.getDueDate()).isEqualTo(UPDATED_DUE_DATE);
                assertThat(testLiabilitySearch.getType()).isEqualTo(UPDATED_TYPE);
            });
    }

    @Test
    void putNonExistingLiability() throws Exception {
        int databaseSizeBeforeUpdate = liabilityRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(liabilitySearchRepository.findAll().collectList().block());
        liability.setId(longCount.incrementAndGet());

        // Create the Liability
        LiabilityDTO liabilityDTO = liabilityMapper.toDto(liability);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, liabilityDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(liabilityDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Liability in the database
        List<Liability> liabilityList = liabilityRepository.findAll().collectList().block();
        assertThat(liabilityList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(liabilitySearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchLiability() throws Exception {
        int databaseSizeBeforeUpdate = liabilityRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(liabilitySearchRepository.findAll().collectList().block());
        liability.setId(longCount.incrementAndGet());

        // Create the Liability
        LiabilityDTO liabilityDTO = liabilityMapper.toDto(liability);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(liabilityDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Liability in the database
        List<Liability> liabilityList = liabilityRepository.findAll().collectList().block();
        assertThat(liabilityList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(liabilitySearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamLiability() throws Exception {
        int databaseSizeBeforeUpdate = liabilityRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(liabilitySearchRepository.findAll().collectList().block());
        liability.setId(longCount.incrementAndGet());

        // Create the Liability
        LiabilityDTO liabilityDTO = liabilityMapper.toDto(liability);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(liabilityDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Liability in the database
        List<Liability> liabilityList = liabilityRepository.findAll().collectList().block();
        assertThat(liabilityList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(liabilitySearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateLiabilityWithPatch() throws Exception {
        // Initialize the database
        liabilityRepository.save(liability).block();

        int databaseSizeBeforeUpdate = liabilityRepository.findAll().collectList().block().size();

        // Update the liability using partial update
        Liability partialUpdatedLiability = new Liability();
        partialUpdatedLiability.setId(liability.getId());

        partialUpdatedLiability.name(UPDATED_NAME);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedLiability.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedLiability))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Liability in the database
        List<Liability> liabilityList = liabilityRepository.findAll().collectList().block();
        assertThat(liabilityList).hasSize(databaseSizeBeforeUpdate);
        Liability testLiability = liabilityList.get(liabilityList.size() - 1);
        assertThat(testLiability.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testLiability.getAmount()).isEqualByComparingTo(DEFAULT_AMOUNT);
        assertThat(testLiability.getDueDate()).isEqualTo(DEFAULT_DUE_DATE);
        assertThat(testLiability.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    void fullUpdateLiabilityWithPatch() throws Exception {
        // Initialize the database
        liabilityRepository.save(liability).block();

        int databaseSizeBeforeUpdate = liabilityRepository.findAll().collectList().block().size();

        // Update the liability using partial update
        Liability partialUpdatedLiability = new Liability();
        partialUpdatedLiability.setId(liability.getId());

        partialUpdatedLiability.name(UPDATED_NAME).amount(UPDATED_AMOUNT).dueDate(UPDATED_DUE_DATE).type(UPDATED_TYPE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedLiability.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedLiability))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Liability in the database
        List<Liability> liabilityList = liabilityRepository.findAll().collectList().block();
        assertThat(liabilityList).hasSize(databaseSizeBeforeUpdate);
        Liability testLiability = liabilityList.get(liabilityList.size() - 1);
        assertThat(testLiability.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testLiability.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
        assertThat(testLiability.getDueDate()).isEqualTo(UPDATED_DUE_DATE);
        assertThat(testLiability.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    void patchNonExistingLiability() throws Exception {
        int databaseSizeBeforeUpdate = liabilityRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(liabilitySearchRepository.findAll().collectList().block());
        liability.setId(longCount.incrementAndGet());

        // Create the Liability
        LiabilityDTO liabilityDTO = liabilityMapper.toDto(liability);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, liabilityDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(liabilityDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Liability in the database
        List<Liability> liabilityList = liabilityRepository.findAll().collectList().block();
        assertThat(liabilityList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(liabilitySearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchLiability() throws Exception {
        int databaseSizeBeforeUpdate = liabilityRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(liabilitySearchRepository.findAll().collectList().block());
        liability.setId(longCount.incrementAndGet());

        // Create the Liability
        LiabilityDTO liabilityDTO = liabilityMapper.toDto(liability);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(liabilityDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Liability in the database
        List<Liability> liabilityList = liabilityRepository.findAll().collectList().block();
        assertThat(liabilityList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(liabilitySearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamLiability() throws Exception {
        int databaseSizeBeforeUpdate = liabilityRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(liabilitySearchRepository.findAll().collectList().block());
        liability.setId(longCount.incrementAndGet());

        // Create the Liability
        LiabilityDTO liabilityDTO = liabilityMapper.toDto(liability);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(liabilityDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Liability in the database
        List<Liability> liabilityList = liabilityRepository.findAll().collectList().block();
        assertThat(liabilityList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(liabilitySearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteLiability() {
        // Initialize the database
        liabilityRepository.save(liability).block();
        liabilityRepository.save(liability).block();
        liabilitySearchRepository.save(liability).block();

        int databaseSizeBeforeDelete = liabilityRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(liabilitySearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the liability
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, liability.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Liability> liabilityList = liabilityRepository.findAll().collectList().block();
        assertThat(liabilityList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(liabilitySearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchLiability() {
        // Initialize the database
        liability = liabilityRepository.save(liability).block();
        liabilitySearchRepository.save(liability).block();

        // Search the liability
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + liability.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(liability.getId().intValue()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].amount")
            .value(hasItem(sameNumber(DEFAULT_AMOUNT)))
            .jsonPath("$.[*].dueDate")
            .value(hasItem(DEFAULT_DUE_DATE.toString()))
            .jsonPath("$.[*].type")
            .value(hasItem(DEFAULT_TYPE.toString()));
    }
}
