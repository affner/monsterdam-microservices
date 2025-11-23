package com.fanflip.admin.domain;

import static com.fanflip.admin.domain.AccountingRecordTestSamples.*;
import static com.fanflip.admin.domain.BudgetTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.fanflip.admin.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class BudgetTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Budget.class);
        Budget budget1 = getBudgetSample1();
        Budget budget2 = new Budget();
        assertThat(budget1).isNotEqualTo(budget2);

        budget2.setId(budget1.getId());
        assertThat(budget1).isEqualTo(budget2);

        budget2 = getBudgetSample2();
        assertThat(budget1).isNotEqualTo(budget2);
    }

    @Test
    void accountingRecordsTest() throws Exception {
        Budget budget = getBudgetRandomSampleGenerator();
        AccountingRecord accountingRecordBack = getAccountingRecordRandomSampleGenerator();

        budget.addAccountingRecords(accountingRecordBack);
        assertThat(budget.getAccountingRecords()).containsOnly(accountingRecordBack);
        assertThat(accountingRecordBack.getBudget()).isEqualTo(budget);

        budget.removeAccountingRecords(accountingRecordBack);
        assertThat(budget.getAccountingRecords()).doesNotContain(accountingRecordBack);
        assertThat(accountingRecordBack.getBudget()).isNull();

        budget.accountingRecords(new HashSet<>(Set.of(accountingRecordBack)));
        assertThat(budget.getAccountingRecords()).containsOnly(accountingRecordBack);
        assertThat(accountingRecordBack.getBudget()).isEqualTo(budget);

        budget.setAccountingRecords(new HashSet<>());
        assertThat(budget.getAccountingRecords()).doesNotContain(accountingRecordBack);
        assertThat(accountingRecordBack.getBudget()).isNull();
    }
}
