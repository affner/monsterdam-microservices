package com.monsterdam.admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.monsterdam.admin.IntegrationTest;
import com.monsterdam.admin.domain.Country;
import com.monsterdam.admin.repository.CountryRepository;
import com.monsterdam.admin.repository.EntityManager;
import com.monsterdam.admin.repository.search.CountrySearchRepository;
import com.monsterdam.admin.service.dto.CountryDTO;
import com.monsterdam.admin.service.mapper.CountryMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
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
 * Integration tests for the {@link CountryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class CountryResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ALPHA_2_CODE = "AA";
    private static final String UPDATED_ALPHA_2_CODE = "BB";

    private static final String DEFAULT_ALPHA_3_CODE = "AAA";
    private static final String UPDATED_ALPHA_3_CODE = "BBB";

    private static final String DEFAULT_PHONE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_CODE = "BBBBBBBBBB";

    private static final byte[] DEFAULT_THUMBNAIL_COUNTRY = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_THUMBNAIL_COUNTRY = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_THUMBNAIL_COUNTRY_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_THUMBNAIL_COUNTRY_CONTENT_TYPE = "image/png";

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

    private static final String ENTITY_API_URL = "/api/countries";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/countries/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private CountryMapper countryMapper;

    @Autowired
    private CountrySearchRepository countrySearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Country country;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Country createEntity(EntityManager em) {
        Country country = new Country()
            .name(DEFAULT_NAME)
            .alpha2Code(DEFAULT_ALPHA_2_CODE)
            .alpha3Code(DEFAULT_ALPHA_3_CODE)
            .phoneCode(DEFAULT_PHONE_CODE)
            .thumbnailCountry(DEFAULT_THUMBNAIL_COUNTRY)
            .thumbnailCountryContentType(DEFAULT_THUMBNAIL_COUNTRY_CONTENT_TYPE)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .isDeleted(DEFAULT_IS_DELETED);
        return country;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Country createUpdatedEntity(EntityManager em) {
        Country country = new Country()
            .name(UPDATED_NAME)
            .alpha2Code(UPDATED_ALPHA_2_CODE)
            .alpha3Code(UPDATED_ALPHA_3_CODE)
            .phoneCode(UPDATED_PHONE_CODE)
            .thumbnailCountry(UPDATED_THUMBNAIL_COUNTRY)
            .thumbnailCountryContentType(UPDATED_THUMBNAIL_COUNTRY_CONTENT_TYPE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        return country;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Country.class).block();
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
        countrySearchRepository.deleteAll().block();
        assertThat(countrySearchRepository.count().block()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        country = createEntity(em);
    }

    @Test
    void createCountry() throws Exception {
        int databaseSizeBeforeCreate = countryRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(countrySearchRepository.findAll().collectList().block());
        // Create the Country
        CountryDTO countryDTO = countryMapper.toDto(country);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(countryDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Country in the database
        List<Country> countryList = countryRepository.findAll().collectList().block();
        assertThat(countryList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(countrySearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        Country testCountry = countryList.get(countryList.size() - 1);
        assertThat(testCountry.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCountry.getAlpha2Code()).isEqualTo(DEFAULT_ALPHA_2_CODE);
        assertThat(testCountry.getAlpha3Code()).isEqualTo(DEFAULT_ALPHA_3_CODE);
        assertThat(testCountry.getPhoneCode()).isEqualTo(DEFAULT_PHONE_CODE);
        assertThat(testCountry.getThumbnailCountry()).isEqualTo(DEFAULT_THUMBNAIL_COUNTRY);
        assertThat(testCountry.getThumbnailCountryContentType()).isEqualTo(DEFAULT_THUMBNAIL_COUNTRY_CONTENT_TYPE);
        assertThat(testCountry.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testCountry.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testCountry.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testCountry.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testCountry.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    void createCountryWithExistingId() throws Exception {
        // Create the Country with an existing ID
        country.setId(1L);
        CountryDTO countryDTO = countryMapper.toDto(country);

        int databaseSizeBeforeCreate = countryRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(countrySearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(countryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Country in the database
        List<Country> countryList = countryRepository.findAll().collectList().block();
        assertThat(countryList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(countrySearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = countryRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(countrySearchRepository.findAll().collectList().block());
        // set the field null
        country.setName(null);

        // Create the Country, which fails.
        CountryDTO countryDTO = countryMapper.toDto(country);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(countryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Country> countryList = countryRepository.findAll().collectList().block();
        assertThat(countryList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(countrySearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkAlpha2CodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = countryRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(countrySearchRepository.findAll().collectList().block());
        // set the field null
        country.setAlpha2Code(null);

        // Create the Country, which fails.
        CountryDTO countryDTO = countryMapper.toDto(country);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(countryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Country> countryList = countryRepository.findAll().collectList().block();
        assertThat(countryList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(countrySearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkAlpha3CodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = countryRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(countrySearchRepository.findAll().collectList().block());
        // set the field null
        country.setAlpha3Code(null);

        // Create the Country, which fails.
        CountryDTO countryDTO = countryMapper.toDto(country);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(countryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Country> countryList = countryRepository.findAll().collectList().block();
        assertThat(countryList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(countrySearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = countryRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(countrySearchRepository.findAll().collectList().block());
        // set the field null
        country.setCreatedDate(null);

        // Create the Country, which fails.
        CountryDTO countryDTO = countryMapper.toDto(country);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(countryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Country> countryList = countryRepository.findAll().collectList().block();
        assertThat(countryList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(countrySearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = countryRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(countrySearchRepository.findAll().collectList().block());
        // set the field null
        country.setIsDeleted(null);

        // Create the Country, which fails.
        CountryDTO countryDTO = countryMapper.toDto(country);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(countryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Country> countryList = countryRepository.findAll().collectList().block();
        assertThat(countryList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(countrySearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllCountries() {
        // Initialize the database
        countryRepository.save(country).block();

        // Get all the countryList
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
            .value(hasItem(country.getId().intValue()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].alpha2Code")
            .value(hasItem(DEFAULT_ALPHA_2_CODE))
            .jsonPath("$.[*].alpha3Code")
            .value(hasItem(DEFAULT_ALPHA_3_CODE))
            .jsonPath("$.[*].phoneCode")
            .value(hasItem(DEFAULT_PHONE_CODE))
            .jsonPath("$.[*].thumbnailCountryContentType")
            .value(hasItem(DEFAULT_THUMBNAIL_COUNTRY_CONTENT_TYPE))
            .jsonPath("$.[*].thumbnailCountry")
            .value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_THUMBNAIL_COUNTRY)))
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

    @Test
    void getCountry() {
        // Initialize the database
        countryRepository.save(country).block();

        // Get the country
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, country.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(country.getId().intValue()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.alpha2Code")
            .value(is(DEFAULT_ALPHA_2_CODE))
            .jsonPath("$.alpha3Code")
            .value(is(DEFAULT_ALPHA_3_CODE))
            .jsonPath("$.phoneCode")
            .value(is(DEFAULT_PHONE_CODE))
            .jsonPath("$.thumbnailCountryContentType")
            .value(is(DEFAULT_THUMBNAIL_COUNTRY_CONTENT_TYPE))
            .jsonPath("$.thumbnailCountry")
            .value(is(Base64.getEncoder().encodeToString(DEFAULT_THUMBNAIL_COUNTRY)))
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
    void getNonExistingCountry() {
        // Get the country
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingCountry() throws Exception {
        // Initialize the database
        countryRepository.save(country).block();

        int databaseSizeBeforeUpdate = countryRepository.findAll().collectList().block().size();
        countrySearchRepository.save(country).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(countrySearchRepository.findAll().collectList().block());

        // Update the country
        Country updatedCountry = countryRepository.findById(country.getId()).block();
        updatedCountry
            .name(UPDATED_NAME)
            .alpha2Code(UPDATED_ALPHA_2_CODE)
            .alpha3Code(UPDATED_ALPHA_3_CODE)
            .phoneCode(UPDATED_PHONE_CODE)
            .thumbnailCountry(UPDATED_THUMBNAIL_COUNTRY)
            .thumbnailCountryContentType(UPDATED_THUMBNAIL_COUNTRY_CONTENT_TYPE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        CountryDTO countryDTO = countryMapper.toDto(updatedCountry);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, countryDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(countryDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Country in the database
        List<Country> countryList = countryRepository.findAll().collectList().block();
        assertThat(countryList).hasSize(databaseSizeBeforeUpdate);
        Country testCountry = countryList.get(countryList.size() - 1);
        assertThat(testCountry.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCountry.getAlpha2Code()).isEqualTo(UPDATED_ALPHA_2_CODE);
        assertThat(testCountry.getAlpha3Code()).isEqualTo(UPDATED_ALPHA_3_CODE);
        assertThat(testCountry.getPhoneCode()).isEqualTo(UPDATED_PHONE_CODE);
        assertThat(testCountry.getThumbnailCountry()).isEqualTo(UPDATED_THUMBNAIL_COUNTRY);
        assertThat(testCountry.getThumbnailCountryContentType()).isEqualTo(UPDATED_THUMBNAIL_COUNTRY_CONTENT_TYPE);
        assertThat(testCountry.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testCountry.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testCountry.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testCountry.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testCountry.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(countrySearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Country> countrySearchList = IterableUtils.toList(countrySearchRepository.findAll().collectList().block());
                Country testCountrySearch = countrySearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testCountrySearch.getName()).isEqualTo(UPDATED_NAME);
                assertThat(testCountrySearch.getAlpha2Code()).isEqualTo(UPDATED_ALPHA_2_CODE);
                assertThat(testCountrySearch.getAlpha3Code()).isEqualTo(UPDATED_ALPHA_3_CODE);
                assertThat(testCountrySearch.getPhoneCode()).isEqualTo(UPDATED_PHONE_CODE);
                assertThat(testCountrySearch.getThumbnailCountry()).isEqualTo(UPDATED_THUMBNAIL_COUNTRY);
                assertThat(testCountrySearch.getThumbnailCountryContentType()).isEqualTo(UPDATED_THUMBNAIL_COUNTRY_CONTENT_TYPE);
                assertThat(testCountrySearch.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
                assertThat(testCountrySearch.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
                assertThat(testCountrySearch.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
                assertThat(testCountrySearch.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
                assertThat(testCountrySearch.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
            });
    }

    @Test
    void putNonExistingCountry() throws Exception {
        int databaseSizeBeforeUpdate = countryRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(countrySearchRepository.findAll().collectList().block());
        country.setId(longCount.incrementAndGet());

        // Create the Country
        CountryDTO countryDTO = countryMapper.toDto(country);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, countryDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(countryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Country in the database
        List<Country> countryList = countryRepository.findAll().collectList().block();
        assertThat(countryList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(countrySearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchCountry() throws Exception {
        int databaseSizeBeforeUpdate = countryRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(countrySearchRepository.findAll().collectList().block());
        country.setId(longCount.incrementAndGet());

        // Create the Country
        CountryDTO countryDTO = countryMapper.toDto(country);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(countryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Country in the database
        List<Country> countryList = countryRepository.findAll().collectList().block();
        assertThat(countryList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(countrySearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamCountry() throws Exception {
        int databaseSizeBeforeUpdate = countryRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(countrySearchRepository.findAll().collectList().block());
        country.setId(longCount.incrementAndGet());

        // Create the Country
        CountryDTO countryDTO = countryMapper.toDto(country);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(countryDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Country in the database
        List<Country> countryList = countryRepository.findAll().collectList().block();
        assertThat(countryList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(countrySearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateCountryWithPatch() throws Exception {
        // Initialize the database
        countryRepository.save(country).block();

        int databaseSizeBeforeUpdate = countryRepository.findAll().collectList().block().size();

        // Update the country using partial update
        Country partialUpdatedCountry = new Country();
        partialUpdatedCountry.setId(country.getId());

        partialUpdatedCountry
            .name(UPDATED_NAME)
            .alpha3Code(UPDATED_ALPHA_3_CODE)
            .phoneCode(UPDATED_PHONE_CODE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCountry.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCountry))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Country in the database
        List<Country> countryList = countryRepository.findAll().collectList().block();
        assertThat(countryList).hasSize(databaseSizeBeforeUpdate);
        Country testCountry = countryList.get(countryList.size() - 1);
        assertThat(testCountry.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCountry.getAlpha2Code()).isEqualTo(DEFAULT_ALPHA_2_CODE);
        assertThat(testCountry.getAlpha3Code()).isEqualTo(UPDATED_ALPHA_3_CODE);
        assertThat(testCountry.getPhoneCode()).isEqualTo(UPDATED_PHONE_CODE);
        assertThat(testCountry.getThumbnailCountry()).isEqualTo(DEFAULT_THUMBNAIL_COUNTRY);
        assertThat(testCountry.getThumbnailCountryContentType()).isEqualTo(DEFAULT_THUMBNAIL_COUNTRY_CONTENT_TYPE);
        assertThat(testCountry.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testCountry.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testCountry.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testCountry.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testCountry.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    void fullUpdateCountryWithPatch() throws Exception {
        // Initialize the database
        countryRepository.save(country).block();

        int databaseSizeBeforeUpdate = countryRepository.findAll().collectList().block().size();

        // Update the country using partial update
        Country partialUpdatedCountry = new Country();
        partialUpdatedCountry.setId(country.getId());

        partialUpdatedCountry
            .name(UPDATED_NAME)
            .alpha2Code(UPDATED_ALPHA_2_CODE)
            .alpha3Code(UPDATED_ALPHA_3_CODE)
            .phoneCode(UPDATED_PHONE_CODE)
            .thumbnailCountry(UPDATED_THUMBNAIL_COUNTRY)
            .thumbnailCountryContentType(UPDATED_THUMBNAIL_COUNTRY_CONTENT_TYPE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCountry.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCountry))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Country in the database
        List<Country> countryList = countryRepository.findAll().collectList().block();
        assertThat(countryList).hasSize(databaseSizeBeforeUpdate);
        Country testCountry = countryList.get(countryList.size() - 1);
        assertThat(testCountry.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCountry.getAlpha2Code()).isEqualTo(UPDATED_ALPHA_2_CODE);
        assertThat(testCountry.getAlpha3Code()).isEqualTo(UPDATED_ALPHA_3_CODE);
        assertThat(testCountry.getPhoneCode()).isEqualTo(UPDATED_PHONE_CODE);
        assertThat(testCountry.getThumbnailCountry()).isEqualTo(UPDATED_THUMBNAIL_COUNTRY);
        assertThat(testCountry.getThumbnailCountryContentType()).isEqualTo(UPDATED_THUMBNAIL_COUNTRY_CONTENT_TYPE);
        assertThat(testCountry.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testCountry.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testCountry.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testCountry.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testCountry.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    void patchNonExistingCountry() throws Exception {
        int databaseSizeBeforeUpdate = countryRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(countrySearchRepository.findAll().collectList().block());
        country.setId(longCount.incrementAndGet());

        // Create the Country
        CountryDTO countryDTO = countryMapper.toDto(country);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, countryDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(countryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Country in the database
        List<Country> countryList = countryRepository.findAll().collectList().block();
        assertThat(countryList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(countrySearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchCountry() throws Exception {
        int databaseSizeBeforeUpdate = countryRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(countrySearchRepository.findAll().collectList().block());
        country.setId(longCount.incrementAndGet());

        // Create the Country
        CountryDTO countryDTO = countryMapper.toDto(country);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(countryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Country in the database
        List<Country> countryList = countryRepository.findAll().collectList().block();
        assertThat(countryList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(countrySearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamCountry() throws Exception {
        int databaseSizeBeforeUpdate = countryRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(countrySearchRepository.findAll().collectList().block());
        country.setId(longCount.incrementAndGet());

        // Create the Country
        CountryDTO countryDTO = countryMapper.toDto(country);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(countryDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Country in the database
        List<Country> countryList = countryRepository.findAll().collectList().block();
        assertThat(countryList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(countrySearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteCountry() {
        // Initialize the database
        countryRepository.save(country).block();
        countryRepository.save(country).block();
        countrySearchRepository.save(country).block();

        int databaseSizeBeforeDelete = countryRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(countrySearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the country
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, country.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Country> countryList = countryRepository.findAll().collectList().block();
        assertThat(countryList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(countrySearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchCountry() {
        // Initialize the database
        country = countryRepository.save(country).block();
        countrySearchRepository.save(country).block();

        // Search the country
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + country.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(country.getId().intValue()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].alpha2Code")
            .value(hasItem(DEFAULT_ALPHA_2_CODE))
            .jsonPath("$.[*].alpha3Code")
            .value(hasItem(DEFAULT_ALPHA_3_CODE))
            .jsonPath("$.[*].phoneCode")
            .value(hasItem(DEFAULT_PHONE_CODE))
            .jsonPath("$.[*].thumbnailCountryContentType")
            .value(hasItem(DEFAULT_THUMBNAIL_COUNTRY_CONTENT_TYPE))
            .jsonPath("$.[*].thumbnailCountry")
            .value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_THUMBNAIL_COUNTRY)))
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
