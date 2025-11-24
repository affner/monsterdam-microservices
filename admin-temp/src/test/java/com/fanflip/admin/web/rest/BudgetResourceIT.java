package com.monsterdam.admin.web.rest;

import static com.monsterdam.admin.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.monsterdam.admin.IntegrationTest;
import com.monsterdam.admin.domain.Budget;
import com.monsterdam.admin.repository.BudgetRepository;
import com.monsterdam.admin.repository.EntityManager;
import com.monsterdam.admin.repository.search.BudgetSearchRepository;
import com.monsterdam.admin.service.dto.BudgetDTO;
import com.monsterdam.admin.service.mapper.BudgetMapper;
import java.math.BigDecimal;
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
 * Integration tests for the {@link BudgetResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class BudgetResourceIT {

    private static final Integer DEFAULT_YEAR = 1;
    private static final Integer UPDATED_YEAR = 2;

    private static final BigDecimal DEFAULT_TOTAL_BUDGET = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_BUDGET = new BigDecimal(2);

    private static final BigDecimal DEFAULT_SPENT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_SPENT_AMOUNT = new BigDecimal(2);

    private static final BigDecimal DEFAULT_REMAINING_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_REMAINING_AMOUNT = new BigDecimal(2);

    private static final String DEFAULT_BUDGET_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_BUDGET_DETAILS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/budgets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/budgets/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private BudgetMapper budgetMapper;

    @Autowired
    private BudgetSearchRepository budgetSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Budget budget;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Budget createEntity(EntityManager em) {
        Budget budget = new Budget()
            .year(DEFAULT_YEAR)
            .totalBudget(DEFAULT_TOTAL_BUDGET)
            .spentAmount(DEFAULT_SPENT_AMOUNT)
            .remainingAmount(DEFAULT_REMAINING_AMOUNT)
            .budgetDetails(DEFAULT_BUDGET_DETAILS);
        return budget;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Budget createUpdatedEntity(EntityManager em) {
        Budget budget = new Budget()
            .year(UPDATED_YEAR)
            .totalBudget(UPDATED_TOTAL_BUDGET)
            .spentAmount(UPDATED_SPENT_AMOUNT)
            .remainingAmount(UPDATED_REMAINING_AMOUNT)
            .budgetDetails(UPDATED_BUDGET_DETAILS);
        return budget;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Budget.class).block();
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
        budgetSearchRepository.deleteAll().block();
        assertThat(budgetSearchRepository.count().block()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        budget = createEntity(em);
    }

    @Test
    void createBudget() throws Exception {
        int databaseSizeBeforeCreate = budgetRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(budgetSearchRepository.findAll().collectList().block());
        // Create the Budget
        BudgetDTO budgetDTO = budgetMapper.toDto(budget);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(budgetDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Budget in the database
        List<Budget> budgetList = budgetRepository.findAll().collectList().block();
        assertThat(budgetList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(budgetSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        Budget testBudget = budgetList.get(budgetList.size() - 1);
        assertThat(testBudget.getYear()).isEqualTo(DEFAULT_YEAR);
        assertThat(testBudget.getTotalBudget()).isEqualByComparingTo(DEFAULT_TOTAL_BUDGET);
        assertThat(testBudget.getSpentAmount()).isEqualByComparingTo(DEFAULT_SPENT_AMOUNT);
        assertThat(testBudget.getRemainingAmount()).isEqualByComparingTo(DEFAULT_REMAINING_AMOUNT);
        assertThat(testBudget.getBudgetDetails()).isEqualTo(DEFAULT_BUDGET_DETAILS);
    }

    @Test
    void createBudgetWithExistingId() throws Exception {
        // Create the Budget with an existing ID
        budget.setId(1L);
        BudgetDTO budgetDTO = budgetMapper.toDto(budget);

        int databaseSizeBeforeCreate = budgetRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(budgetSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(budgetDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Budget in the database
        List<Budget> budgetList = budgetRepository.findAll().collectList().block();
        assertThat(budgetList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(budgetSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkYearIsRequired() throws Exception {
        int databaseSizeBeforeTest = budgetRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(budgetSearchRepository.findAll().collectList().block());
        // set the field null
        budget.setYear(null);

        // Create the Budget, which fails.
        BudgetDTO budgetDTO = budgetMapper.toDto(budget);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(budgetDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Budget> budgetList = budgetRepository.findAll().collectList().block();
        assertThat(budgetList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(budgetSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkTotalBudgetIsRequired() throws Exception {
        int databaseSizeBeforeTest = budgetRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(budgetSearchRepository.findAll().collectList().block());
        // set the field null
        budget.setTotalBudget(null);

        // Create the Budget, which fails.
        BudgetDTO budgetDTO = budgetMapper.toDto(budget);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(budgetDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Budget> budgetList = budgetRepository.findAll().collectList().block();
        assertThat(budgetList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(budgetSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllBudgets() {
        // Initialize the database
        budgetRepository.save(budget).block();

        // Get all the budgetList
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
            .value(hasItem(budget.getId().intValue()))
            .jsonPath("$.[*].year")
            .value(hasItem(DEFAULT_YEAR))
            .jsonPath("$.[*].totalBudget")
            .value(hasItem(sameNumber(DEFAULT_TOTAL_BUDGET)))
            .jsonPath("$.[*].spentAmount")
            .value(hasItem(sameNumber(DEFAULT_SPENT_AMOUNT)))
            .jsonPath("$.[*].remainingAmount")
            .value(hasItem(sameNumber(DEFAULT_REMAINING_AMOUNT)))
            .jsonPath("$.[*].budgetDetails")
            .value(hasItem(DEFAULT_BUDGET_DETAILS));
    }

    @Test
    void getBudget() {
        // Initialize the database
        budgetRepository.save(budget).block();

        // Get the budget
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, budget.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(budget.getId().intValue()))
            .jsonPath("$.year")
            .value(is(DEFAULT_YEAR))
            .jsonPath("$.totalBudget")
            .value(is(sameNumber(DEFAULT_TOTAL_BUDGET)))
            .jsonPath("$.spentAmount")
            .value(is(sameNumber(DEFAULT_SPENT_AMOUNT)))
            .jsonPath("$.remainingAmount")
            .value(is(sameNumber(DEFAULT_REMAINING_AMOUNT)))
            .jsonPath("$.budgetDetails")
            .value(is(DEFAULT_BUDGET_DETAILS));
    }

    @Test
    void getNonExistingBudget() {
        // Get the budget
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingBudget() throws Exception {
        // Initialize the database
        budgetRepository.save(budget).block();

        int databaseSizeBeforeUpdate = budgetRepository.findAll().collectList().block().size();
        budgetSearchRepository.save(budget).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(budgetSearchRepository.findAll().collectList().block());

        // Update the budget
        Budget updatedBudget = budgetRepository.findById(budget.getId()).block();
        updatedBudget
            .year(UPDATED_YEAR)
            .totalBudget(UPDATED_TOTAL_BUDGET)
            .spentAmount(UPDATED_SPENT_AMOUNT)
            .remainingAmount(UPDATED_REMAINING_AMOUNT)
            .budgetDetails(UPDATED_BUDGET_DETAILS);
        BudgetDTO budgetDTO = budgetMapper.toDto(updatedBudget);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, budgetDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(budgetDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Budget in the database
        List<Budget> budgetList = budgetRepository.findAll().collectList().block();
        assertThat(budgetList).hasSize(databaseSizeBeforeUpdate);
        Budget testBudget = budgetList.get(budgetList.size() - 1);
        assertThat(testBudget.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testBudget.getTotalBudget()).isEqualByComparingTo(UPDATED_TOTAL_BUDGET);
        assertThat(testBudget.getSpentAmount()).isEqualByComparingTo(UPDATED_SPENT_AMOUNT);
        assertThat(testBudget.getRemainingAmount()).isEqualByComparingTo(UPDATED_REMAINING_AMOUNT);
        assertThat(testBudget.getBudgetDetails()).isEqualTo(UPDATED_BUDGET_DETAILS);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(budgetSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Budget> budgetSearchList = IterableUtils.toList(budgetSearchRepository.findAll().collectList().block());
                Budget testBudgetSearch = budgetSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testBudgetSearch.getYear()).isEqualTo(UPDATED_YEAR);
                assertThat(testBudgetSearch.getTotalBudget()).isEqualByComparingTo(UPDATED_TOTAL_BUDGET);
                assertThat(testBudgetSearch.getSpentAmount()).isEqualByComparingTo(UPDATED_SPENT_AMOUNT);
                assertThat(testBudgetSearch.getRemainingAmount()).isEqualByComparingTo(UPDATED_REMAINING_AMOUNT);
                assertThat(testBudgetSearch.getBudgetDetails()).isEqualTo(UPDATED_BUDGET_DETAILS);
            });
    }

    @Test
    void putNonExistingBudget() throws Exception {
        int databaseSizeBeforeUpdate = budgetRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(budgetSearchRepository.findAll().collectList().block());
        budget.setId(longCount.incrementAndGet());

        // Create the Budget
        BudgetDTO budgetDTO = budgetMapper.toDto(budget);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, budgetDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(budgetDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Budget in the database
        List<Budget> budgetList = budgetRepository.findAll().collectList().block();
        assertThat(budgetList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(budgetSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchBudget() throws Exception {
        int databaseSizeBeforeUpdate = budgetRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(budgetSearchRepository.findAll().collectList().block());
        budget.setId(longCount.incrementAndGet());

        // Create the Budget
        BudgetDTO budgetDTO = budgetMapper.toDto(budget);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(budgetDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Budget in the database
        List<Budget> budgetList = budgetRepository.findAll().collectList().block();
        assertThat(budgetList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(budgetSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamBudget() throws Exception {
        int databaseSizeBeforeUpdate = budgetRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(budgetSearchRepository.findAll().collectList().block());
        budget.setId(longCount.incrementAndGet());

        // Create the Budget
        BudgetDTO budgetDTO = budgetMapper.toDto(budget);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(budgetDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Budget in the database
        List<Budget> budgetList = budgetRepository.findAll().collectList().block();
        assertThat(budgetList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(budgetSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateBudgetWithPatch() throws Exception {
        // Initialize the database
        budgetRepository.save(budget).block();

        int databaseSizeBeforeUpdate = budgetRepository.findAll().collectList().block().size();

        // Update the budget using partial update
        Budget partialUpdatedBudget = new Budget();
        partialUpdatedBudget.setId(budget.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedBudget.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedBudget))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Budget in the database
        List<Budget> budgetList = budgetRepository.findAll().collectList().block();
        assertThat(budgetList).hasSize(databaseSizeBeforeUpdate);
        Budget testBudget = budgetList.get(budgetList.size() - 1);
        assertThat(testBudget.getYear()).isEqualTo(DEFAULT_YEAR);
        assertThat(testBudget.getTotalBudget()).isEqualByComparingTo(DEFAULT_TOTAL_BUDGET);
        assertThat(testBudget.getSpentAmount()).isEqualByComparingTo(DEFAULT_SPENT_AMOUNT);
        assertThat(testBudget.getRemainingAmount()).isEqualByComparingTo(DEFAULT_REMAINING_AMOUNT);
        assertThat(testBudget.getBudgetDetails()).isEqualTo(DEFAULT_BUDGET_DETAILS);
    }

    @Test
    void fullUpdateBudgetWithPatch() throws Exception {
        // Initialize the database
        budgetRepository.save(budget).block();

        int databaseSizeBeforeUpdate = budgetRepository.findAll().collectList().block().size();

        // Update the budget using partial update
        Budget partialUpdatedBudget = new Budget();
        partialUpdatedBudget.setId(budget.getId());

        partialUpdatedBudget
            .year(UPDATED_YEAR)
            .totalBudget(UPDATED_TOTAL_BUDGET)
            .spentAmount(UPDATED_SPENT_AMOUNT)
            .remainingAmount(UPDATED_REMAINING_AMOUNT)
            .budgetDetails(UPDATED_BUDGET_DETAILS);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedBudget.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedBudget))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Budget in the database
        List<Budget> budgetList = budgetRepository.findAll().collectList().block();
        assertThat(budgetList).hasSize(databaseSizeBeforeUpdate);
        Budget testBudget = budgetList.get(budgetList.size() - 1);
        assertThat(testBudget.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testBudget.getTotalBudget()).isEqualByComparingTo(UPDATED_TOTAL_BUDGET);
        assertThat(testBudget.getSpentAmount()).isEqualByComparingTo(UPDATED_SPENT_AMOUNT);
        assertThat(testBudget.getRemainingAmount()).isEqualByComparingTo(UPDATED_REMAINING_AMOUNT);
        assertThat(testBudget.getBudgetDetails()).isEqualTo(UPDATED_BUDGET_DETAILS);
    }

    @Test
    void patchNonExistingBudget() throws Exception {
        int databaseSizeBeforeUpdate = budgetRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(budgetSearchRepository.findAll().collectList().block());
        budget.setId(longCount.incrementAndGet());

        // Create the Budget
        BudgetDTO budgetDTO = budgetMapper.toDto(budget);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, budgetDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(budgetDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Budget in the database
        List<Budget> budgetList = budgetRepository.findAll().collectList().block();
        assertThat(budgetList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(budgetSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchBudget() throws Exception {
        int databaseSizeBeforeUpdate = budgetRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(budgetSearchRepository.findAll().collectList().block());
        budget.setId(longCount.incrementAndGet());

        // Create the Budget
        BudgetDTO budgetDTO = budgetMapper.toDto(budget);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(budgetDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Budget in the database
        List<Budget> budgetList = budgetRepository.findAll().collectList().block();
        assertThat(budgetList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(budgetSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamBudget() throws Exception {
        int databaseSizeBeforeUpdate = budgetRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(budgetSearchRepository.findAll().collectList().block());
        budget.setId(longCount.incrementAndGet());

        // Create the Budget
        BudgetDTO budgetDTO = budgetMapper.toDto(budget);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(budgetDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Budget in the database
        List<Budget> budgetList = budgetRepository.findAll().collectList().block();
        assertThat(budgetList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(budgetSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteBudget() {
        // Initialize the database
        budgetRepository.save(budget).block();
        budgetRepository.save(budget).block();
        budgetSearchRepository.save(budget).block();

        int databaseSizeBeforeDelete = budgetRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(budgetSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the budget
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, budget.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Budget> budgetList = budgetRepository.findAll().collectList().block();
        assertThat(budgetList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(budgetSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchBudget() {
        // Initialize the database
        budget = budgetRepository.save(budget).block();
        budgetSearchRepository.save(budget).block();

        // Search the budget
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + budget.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(budget.getId().intValue()))
            .jsonPath("$.[*].year")
            .value(hasItem(DEFAULT_YEAR))
            .jsonPath("$.[*].totalBudget")
            .value(hasItem(sameNumber(DEFAULT_TOTAL_BUDGET)))
            .jsonPath("$.[*].spentAmount")
            .value(hasItem(sameNumber(DEFAULT_SPENT_AMOUNT)))
            .jsonPath("$.[*].remainingAmount")
            .value(hasItem(sameNumber(DEFAULT_REMAINING_AMOUNT)))
            .jsonPath("$.[*].budgetDetails")
            .value(hasItem(DEFAULT_BUDGET_DETAILS));
    }
}
