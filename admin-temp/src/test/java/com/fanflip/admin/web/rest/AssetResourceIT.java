package com.fanflip.admin.web.rest;

import static com.fanflip.admin.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.fanflip.admin.IntegrationTest;
import com.fanflip.admin.domain.Asset;
import com.fanflip.admin.domain.enumeration.AssetType;
import com.fanflip.admin.repository.AssetRepository;
import com.fanflip.admin.repository.EntityManager;
import com.fanflip.admin.repository.search.AssetSearchRepository;
import com.fanflip.admin.service.dto.AssetDTO;
import com.fanflip.admin.service.mapper.AssetMapper;
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
 * Integration tests for the {@link AssetResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class AssetResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_VALUE = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALUE = new BigDecimal(2);

    private static final LocalDate DEFAULT_ACQUISITION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ACQUISITION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final AssetType DEFAULT_TYPE = AssetType.CURRENT;
    private static final AssetType UPDATED_TYPE = AssetType.FIXED;

    private static final String ENTITY_API_URL = "/api/assets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/assets/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AssetRepository assetRepository;

    @Autowired
    private AssetMapper assetMapper;

    @Autowired
    private AssetSearchRepository assetSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Asset asset;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Asset createEntity(EntityManager em) {
        Asset asset = new Asset().name(DEFAULT_NAME).value(DEFAULT_VALUE).acquisitionDate(DEFAULT_ACQUISITION_DATE).type(DEFAULT_TYPE);
        return asset;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Asset createUpdatedEntity(EntityManager em) {
        Asset asset = new Asset().name(UPDATED_NAME).value(UPDATED_VALUE).acquisitionDate(UPDATED_ACQUISITION_DATE).type(UPDATED_TYPE);
        return asset;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Asset.class).block();
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
        assetSearchRepository.deleteAll().block();
        assertThat(assetSearchRepository.count().block()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        asset = createEntity(em);
    }

    @Test
    void createAsset() throws Exception {
        int databaseSizeBeforeCreate = assetRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assetSearchRepository.findAll().collectList().block());
        // Create the Asset
        AssetDTO assetDTO = assetMapper.toDto(asset);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(assetDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Asset in the database
        List<Asset> assetList = assetRepository.findAll().collectList().block();
        assertThat(assetList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(assetSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        Asset testAsset = assetList.get(assetList.size() - 1);
        assertThat(testAsset.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAsset.getValue()).isEqualByComparingTo(DEFAULT_VALUE);
        assertThat(testAsset.getAcquisitionDate()).isEqualTo(DEFAULT_ACQUISITION_DATE);
        assertThat(testAsset.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    void createAssetWithExistingId() throws Exception {
        // Create the Asset with an existing ID
        asset.setId(1L);
        AssetDTO assetDTO = assetMapper.toDto(asset);

        int databaseSizeBeforeCreate = assetRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assetSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(assetDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Asset in the database
        List<Asset> assetList = assetRepository.findAll().collectList().block();
        assertThat(assetList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(assetSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = assetRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assetSearchRepository.findAll().collectList().block());
        // set the field null
        asset.setName(null);

        // Create the Asset, which fails.
        AssetDTO assetDTO = assetMapper.toDto(asset);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(assetDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Asset> assetList = assetRepository.findAll().collectList().block();
        assertThat(assetList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(assetSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = assetRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assetSearchRepository.findAll().collectList().block());
        // set the field null
        asset.setValue(null);

        // Create the Asset, which fails.
        AssetDTO assetDTO = assetMapper.toDto(asset);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(assetDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Asset> assetList = assetRepository.findAll().collectList().block();
        assertThat(assetList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(assetSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = assetRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assetSearchRepository.findAll().collectList().block());
        // set the field null
        asset.setType(null);

        // Create the Asset, which fails.
        AssetDTO assetDTO = assetMapper.toDto(asset);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(assetDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Asset> assetList = assetRepository.findAll().collectList().block();
        assertThat(assetList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(assetSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllAssets() {
        // Initialize the database
        assetRepository.save(asset).block();

        // Get all the assetList
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
            .value(hasItem(asset.getId().intValue()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].value")
            .value(hasItem(sameNumber(DEFAULT_VALUE)))
            .jsonPath("$.[*].acquisitionDate")
            .value(hasItem(DEFAULT_ACQUISITION_DATE.toString()))
            .jsonPath("$.[*].type")
            .value(hasItem(DEFAULT_TYPE.toString()));
    }

    @Test
    void getAsset() {
        // Initialize the database
        assetRepository.save(asset).block();

        // Get the asset
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, asset.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(asset.getId().intValue()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.value")
            .value(is(sameNumber(DEFAULT_VALUE)))
            .jsonPath("$.acquisitionDate")
            .value(is(DEFAULT_ACQUISITION_DATE.toString()))
            .jsonPath("$.type")
            .value(is(DEFAULT_TYPE.toString()));
    }

    @Test
    void getNonExistingAsset() {
        // Get the asset
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingAsset() throws Exception {
        // Initialize the database
        assetRepository.save(asset).block();

        int databaseSizeBeforeUpdate = assetRepository.findAll().collectList().block().size();
        assetSearchRepository.save(asset).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assetSearchRepository.findAll().collectList().block());

        // Update the asset
        Asset updatedAsset = assetRepository.findById(asset.getId()).block();
        updatedAsset.name(UPDATED_NAME).value(UPDATED_VALUE).acquisitionDate(UPDATED_ACQUISITION_DATE).type(UPDATED_TYPE);
        AssetDTO assetDTO = assetMapper.toDto(updatedAsset);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, assetDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(assetDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Asset in the database
        List<Asset> assetList = assetRepository.findAll().collectList().block();
        assertThat(assetList).hasSize(databaseSizeBeforeUpdate);
        Asset testAsset = assetList.get(assetList.size() - 1);
        assertThat(testAsset.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAsset.getValue()).isEqualByComparingTo(UPDATED_VALUE);
        assertThat(testAsset.getAcquisitionDate()).isEqualTo(UPDATED_ACQUISITION_DATE);
        assertThat(testAsset.getType()).isEqualTo(UPDATED_TYPE);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(assetSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Asset> assetSearchList = IterableUtils.toList(assetSearchRepository.findAll().collectList().block());
                Asset testAssetSearch = assetSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testAssetSearch.getName()).isEqualTo(UPDATED_NAME);
                assertThat(testAssetSearch.getValue()).isEqualByComparingTo(UPDATED_VALUE);
                assertThat(testAssetSearch.getAcquisitionDate()).isEqualTo(UPDATED_ACQUISITION_DATE);
                assertThat(testAssetSearch.getType()).isEqualTo(UPDATED_TYPE);
            });
    }

    @Test
    void putNonExistingAsset() throws Exception {
        int databaseSizeBeforeUpdate = assetRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assetSearchRepository.findAll().collectList().block());
        asset.setId(longCount.incrementAndGet());

        // Create the Asset
        AssetDTO assetDTO = assetMapper.toDto(asset);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, assetDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(assetDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Asset in the database
        List<Asset> assetList = assetRepository.findAll().collectList().block();
        assertThat(assetList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(assetSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchAsset() throws Exception {
        int databaseSizeBeforeUpdate = assetRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assetSearchRepository.findAll().collectList().block());
        asset.setId(longCount.incrementAndGet());

        // Create the Asset
        AssetDTO assetDTO = assetMapper.toDto(asset);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(assetDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Asset in the database
        List<Asset> assetList = assetRepository.findAll().collectList().block();
        assertThat(assetList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(assetSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamAsset() throws Exception {
        int databaseSizeBeforeUpdate = assetRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assetSearchRepository.findAll().collectList().block());
        asset.setId(longCount.incrementAndGet());

        // Create the Asset
        AssetDTO assetDTO = assetMapper.toDto(asset);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(assetDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Asset in the database
        List<Asset> assetList = assetRepository.findAll().collectList().block();
        assertThat(assetList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(assetSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateAssetWithPatch() throws Exception {
        // Initialize the database
        assetRepository.save(asset).block();

        int databaseSizeBeforeUpdate = assetRepository.findAll().collectList().block().size();

        // Update the asset using partial update
        Asset partialUpdatedAsset = new Asset();
        partialUpdatedAsset.setId(asset.getId());

        partialUpdatedAsset.name(UPDATED_NAME).value(UPDATED_VALUE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAsset.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedAsset))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Asset in the database
        List<Asset> assetList = assetRepository.findAll().collectList().block();
        assertThat(assetList).hasSize(databaseSizeBeforeUpdate);
        Asset testAsset = assetList.get(assetList.size() - 1);
        assertThat(testAsset.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAsset.getValue()).isEqualByComparingTo(UPDATED_VALUE);
        assertThat(testAsset.getAcquisitionDate()).isEqualTo(DEFAULT_ACQUISITION_DATE);
        assertThat(testAsset.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    void fullUpdateAssetWithPatch() throws Exception {
        // Initialize the database
        assetRepository.save(asset).block();

        int databaseSizeBeforeUpdate = assetRepository.findAll().collectList().block().size();

        // Update the asset using partial update
        Asset partialUpdatedAsset = new Asset();
        partialUpdatedAsset.setId(asset.getId());

        partialUpdatedAsset.name(UPDATED_NAME).value(UPDATED_VALUE).acquisitionDate(UPDATED_ACQUISITION_DATE).type(UPDATED_TYPE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAsset.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedAsset))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Asset in the database
        List<Asset> assetList = assetRepository.findAll().collectList().block();
        assertThat(assetList).hasSize(databaseSizeBeforeUpdate);
        Asset testAsset = assetList.get(assetList.size() - 1);
        assertThat(testAsset.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAsset.getValue()).isEqualByComparingTo(UPDATED_VALUE);
        assertThat(testAsset.getAcquisitionDate()).isEqualTo(UPDATED_ACQUISITION_DATE);
        assertThat(testAsset.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    void patchNonExistingAsset() throws Exception {
        int databaseSizeBeforeUpdate = assetRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assetSearchRepository.findAll().collectList().block());
        asset.setId(longCount.incrementAndGet());

        // Create the Asset
        AssetDTO assetDTO = assetMapper.toDto(asset);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, assetDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(assetDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Asset in the database
        List<Asset> assetList = assetRepository.findAll().collectList().block();
        assertThat(assetList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(assetSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchAsset() throws Exception {
        int databaseSizeBeforeUpdate = assetRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assetSearchRepository.findAll().collectList().block());
        asset.setId(longCount.incrementAndGet());

        // Create the Asset
        AssetDTO assetDTO = assetMapper.toDto(asset);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(assetDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Asset in the database
        List<Asset> assetList = assetRepository.findAll().collectList().block();
        assertThat(assetList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(assetSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamAsset() throws Exception {
        int databaseSizeBeforeUpdate = assetRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assetSearchRepository.findAll().collectList().block());
        asset.setId(longCount.incrementAndGet());

        // Create the Asset
        AssetDTO assetDTO = assetMapper.toDto(asset);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(assetDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Asset in the database
        List<Asset> assetList = assetRepository.findAll().collectList().block();
        assertThat(assetList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(assetSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteAsset() {
        // Initialize the database
        assetRepository.save(asset).block();
        assetRepository.save(asset).block();
        assetSearchRepository.save(asset).block();

        int databaseSizeBeforeDelete = assetRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assetSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the asset
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, asset.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Asset> assetList = assetRepository.findAll().collectList().block();
        assertThat(assetList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(assetSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchAsset() {
        // Initialize the database
        asset = assetRepository.save(asset).block();
        assetSearchRepository.save(asset).block();

        // Search the asset
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + asset.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(asset.getId().intValue()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].value")
            .value(hasItem(sameNumber(DEFAULT_VALUE)))
            .jsonPath("$.[*].acquisitionDate")
            .value(hasItem(DEFAULT_ACQUISITION_DATE.toString()))
            .jsonPath("$.[*].type")
            .value(hasItem(DEFAULT_TYPE.toString()));
    }
}
