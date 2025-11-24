package com.monsterdam.admin.domain;

import static com.monsterdam.admin.domain.AccountingRecordTestSamples.*;
import static com.monsterdam.admin.domain.AssetTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.admin.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AssetTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Asset.class);
        Asset asset1 = getAssetSample1();
        Asset asset2 = new Asset();
        assertThat(asset1).isNotEqualTo(asset2);

        asset2.setId(asset1.getId());
        assertThat(asset1).isEqualTo(asset2);

        asset2 = getAssetSample2();
        assertThat(asset1).isNotEqualTo(asset2);
    }

    @Test
    void accountingRecordsTest() throws Exception {
        Asset asset = getAssetRandomSampleGenerator();
        AccountingRecord accountingRecordBack = getAccountingRecordRandomSampleGenerator();

        asset.addAccountingRecords(accountingRecordBack);
        assertThat(asset.getAccountingRecords()).containsOnly(accountingRecordBack);
        assertThat(accountingRecordBack.getAsset()).isEqualTo(asset);

        asset.removeAccountingRecords(accountingRecordBack);
        assertThat(asset.getAccountingRecords()).doesNotContain(accountingRecordBack);
        assertThat(accountingRecordBack.getAsset()).isNull();

        asset.accountingRecords(new HashSet<>(Set.of(accountingRecordBack)));
        assertThat(asset.getAccountingRecords()).containsOnly(accountingRecordBack);
        assertThat(accountingRecordBack.getAsset()).isEqualTo(asset);

        asset.setAccountingRecords(new HashSet<>());
        assertThat(asset.getAccountingRecords()).doesNotContain(accountingRecordBack);
        assertThat(accountingRecordBack.getAsset()).isNull();
    }
}
