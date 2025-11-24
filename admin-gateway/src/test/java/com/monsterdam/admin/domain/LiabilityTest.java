package com.monsterdam.admin.domain;

import static com.monsterdam.admin.domain.AccountingRecordTestSamples.*;
import static com.monsterdam.admin.domain.LiabilityTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.admin.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class LiabilityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Liability.class);
        Liability liability1 = getLiabilitySample1();
        Liability liability2 = new Liability();
        assertThat(liability1).isNotEqualTo(liability2);

        liability2.setId(liability1.getId());
        assertThat(liability1).isEqualTo(liability2);

        liability2 = getLiabilitySample2();
        assertThat(liability1).isNotEqualTo(liability2);
    }

    @Test
    void accountingRecordsTest() throws Exception {
        Liability liability = getLiabilityRandomSampleGenerator();
        AccountingRecord accountingRecordBack = getAccountingRecordRandomSampleGenerator();

        liability.addAccountingRecords(accountingRecordBack);
        assertThat(liability.getAccountingRecords()).containsOnly(accountingRecordBack);
        assertThat(accountingRecordBack.getLiability()).isEqualTo(liability);

        liability.removeAccountingRecords(accountingRecordBack);
        assertThat(liability.getAccountingRecords()).doesNotContain(accountingRecordBack);
        assertThat(accountingRecordBack.getLiability()).isNull();

        liability.accountingRecords(new HashSet<>(Set.of(accountingRecordBack)));
        assertThat(liability.getAccountingRecords()).containsOnly(accountingRecordBack);
        assertThat(accountingRecordBack.getLiability()).isEqualTo(liability);

        liability.setAccountingRecords(new HashSet<>());
        assertThat(liability.getAccountingRecords()).doesNotContain(accountingRecordBack);
        assertThat(accountingRecordBack.getLiability()).isNull();
    }
}
