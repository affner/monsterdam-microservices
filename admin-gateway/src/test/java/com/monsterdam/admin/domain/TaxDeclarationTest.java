package com.monsterdam.admin.domain;

import static com.monsterdam.admin.domain.AccountingRecordTestSamples.*;
import static com.monsterdam.admin.domain.TaxDeclarationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.admin.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class TaxDeclarationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TaxDeclaration.class);
        TaxDeclaration taxDeclaration1 = getTaxDeclarationSample1();
        TaxDeclaration taxDeclaration2 = new TaxDeclaration();
        assertThat(taxDeclaration1).isNotEqualTo(taxDeclaration2);

        taxDeclaration2.setId(taxDeclaration1.getId());
        assertThat(taxDeclaration1).isEqualTo(taxDeclaration2);

        taxDeclaration2 = getTaxDeclarationSample2();
        assertThat(taxDeclaration1).isNotEqualTo(taxDeclaration2);
    }

    @Test
    void accountingRecordsTest() throws Exception {
        TaxDeclaration taxDeclaration = getTaxDeclarationRandomSampleGenerator();
        AccountingRecord accountingRecordBack = getAccountingRecordRandomSampleGenerator();

        taxDeclaration.addAccountingRecords(accountingRecordBack);
        assertThat(taxDeclaration.getAccountingRecords()).containsOnly(accountingRecordBack);

        taxDeclaration.removeAccountingRecords(accountingRecordBack);
        assertThat(taxDeclaration.getAccountingRecords()).doesNotContain(accountingRecordBack);

        taxDeclaration.accountingRecords(new HashSet<>(Set.of(accountingRecordBack)));
        assertThat(taxDeclaration.getAccountingRecords()).containsOnly(accountingRecordBack);

        taxDeclaration.setAccountingRecords(new HashSet<>());
        assertThat(taxDeclaration.getAccountingRecords()).doesNotContain(accountingRecordBack);
    }
}
