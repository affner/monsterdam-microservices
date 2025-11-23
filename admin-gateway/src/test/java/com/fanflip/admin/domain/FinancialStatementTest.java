package com.fanflip.admin.domain;

import static com.fanflip.admin.domain.AccountingRecordTestSamples.*;
import static com.fanflip.admin.domain.FinancialStatementTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.fanflip.admin.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class FinancialStatementTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FinancialStatement.class);
        FinancialStatement financialStatement1 = getFinancialStatementSample1();
        FinancialStatement financialStatement2 = new FinancialStatement();
        assertThat(financialStatement1).isNotEqualTo(financialStatement2);

        financialStatement2.setId(financialStatement1.getId());
        assertThat(financialStatement1).isEqualTo(financialStatement2);

        financialStatement2 = getFinancialStatementSample2();
        assertThat(financialStatement1).isNotEqualTo(financialStatement2);
    }

    @Test
    void accountingRecordsTest() throws Exception {
        FinancialStatement financialStatement = getFinancialStatementRandomSampleGenerator();
        AccountingRecord accountingRecordBack = getAccountingRecordRandomSampleGenerator();

        financialStatement.addAccountingRecords(accountingRecordBack);
        assertThat(financialStatement.getAccountingRecords()).containsOnly(accountingRecordBack);

        financialStatement.removeAccountingRecords(accountingRecordBack);
        assertThat(financialStatement.getAccountingRecords()).doesNotContain(accountingRecordBack);

        financialStatement.accountingRecords(new HashSet<>(Set.of(accountingRecordBack)));
        assertThat(financialStatement.getAccountingRecords()).containsOnly(accountingRecordBack);

        financialStatement.setAccountingRecords(new HashSet<>());
        assertThat(financialStatement.getAccountingRecords()).doesNotContain(accountingRecordBack);
    }
}
