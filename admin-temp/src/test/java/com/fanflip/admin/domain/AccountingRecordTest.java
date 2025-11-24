package com.monsterdam.admin.domain;

import static com.monsterdam.admin.domain.AccountingRecordTestSamples.*;
import static com.monsterdam.admin.domain.AssetTestSamples.*;
import static com.monsterdam.admin.domain.BudgetTestSamples.*;
import static com.monsterdam.admin.domain.FinancialStatementTestSamples.*;
import static com.monsterdam.admin.domain.LiabilityTestSamples.*;
import static com.monsterdam.admin.domain.PaymentTransactionTestSamples.*;
import static com.monsterdam.admin.domain.TaxDeclarationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.admin.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AccountingRecordTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AccountingRecord.class);
        AccountingRecord accountingRecord1 = getAccountingRecordSample1();
        AccountingRecord accountingRecord2 = new AccountingRecord();
        assertThat(accountingRecord1).isNotEqualTo(accountingRecord2);

        accountingRecord2.setId(accountingRecord1.getId());
        assertThat(accountingRecord1).isEqualTo(accountingRecord2);

        accountingRecord2 = getAccountingRecordSample2();
        assertThat(accountingRecord1).isNotEqualTo(accountingRecord2);
    }

    @Test
    void paymentTest() throws Exception {
        AccountingRecord accountingRecord = getAccountingRecordRandomSampleGenerator();
        PaymentTransaction paymentTransactionBack = getPaymentTransactionRandomSampleGenerator();

        accountingRecord.setPayment(paymentTransactionBack);
        assertThat(accountingRecord.getPayment()).isEqualTo(paymentTransactionBack);

        accountingRecord.payment(null);
        assertThat(accountingRecord.getPayment()).isNull();
    }

    @Test
    void budgetTest() throws Exception {
        AccountingRecord accountingRecord = getAccountingRecordRandomSampleGenerator();
        Budget budgetBack = getBudgetRandomSampleGenerator();

        accountingRecord.setBudget(budgetBack);
        assertThat(accountingRecord.getBudget()).isEqualTo(budgetBack);

        accountingRecord.budget(null);
        assertThat(accountingRecord.getBudget()).isNull();
    }

    @Test
    void assetTest() throws Exception {
        AccountingRecord accountingRecord = getAccountingRecordRandomSampleGenerator();
        Asset assetBack = getAssetRandomSampleGenerator();

        accountingRecord.setAsset(assetBack);
        assertThat(accountingRecord.getAsset()).isEqualTo(assetBack);

        accountingRecord.asset(null);
        assertThat(accountingRecord.getAsset()).isNull();
    }

    @Test
    void liabilityTest() throws Exception {
        AccountingRecord accountingRecord = getAccountingRecordRandomSampleGenerator();
        Liability liabilityBack = getLiabilityRandomSampleGenerator();

        accountingRecord.setLiability(liabilityBack);
        assertThat(accountingRecord.getLiability()).isEqualTo(liabilityBack);

        accountingRecord.liability(null);
        assertThat(accountingRecord.getLiability()).isNull();
    }

    @Test
    void taxDeclarationsTest() throws Exception {
        AccountingRecord accountingRecord = getAccountingRecordRandomSampleGenerator();
        TaxDeclaration taxDeclarationBack = getTaxDeclarationRandomSampleGenerator();

        accountingRecord.addTaxDeclarations(taxDeclarationBack);
        assertThat(accountingRecord.getTaxDeclarations()).containsOnly(taxDeclarationBack);
        assertThat(taxDeclarationBack.getAccountingRecords()).containsOnly(accountingRecord);

        accountingRecord.removeTaxDeclarations(taxDeclarationBack);
        assertThat(accountingRecord.getTaxDeclarations()).doesNotContain(taxDeclarationBack);
        assertThat(taxDeclarationBack.getAccountingRecords()).doesNotContain(accountingRecord);

        accountingRecord.taxDeclarations(new HashSet<>(Set.of(taxDeclarationBack)));
        assertThat(accountingRecord.getTaxDeclarations()).containsOnly(taxDeclarationBack);
        assertThat(taxDeclarationBack.getAccountingRecords()).containsOnly(accountingRecord);

        accountingRecord.setTaxDeclarations(new HashSet<>());
        assertThat(accountingRecord.getTaxDeclarations()).doesNotContain(taxDeclarationBack);
        assertThat(taxDeclarationBack.getAccountingRecords()).doesNotContain(accountingRecord);
    }

    @Test
    void financialStatementsTest() throws Exception {
        AccountingRecord accountingRecord = getAccountingRecordRandomSampleGenerator();
        FinancialStatement financialStatementBack = getFinancialStatementRandomSampleGenerator();

        accountingRecord.addFinancialStatements(financialStatementBack);
        assertThat(accountingRecord.getFinancialStatements()).containsOnly(financialStatementBack);
        assertThat(financialStatementBack.getAccountingRecords()).containsOnly(accountingRecord);

        accountingRecord.removeFinancialStatements(financialStatementBack);
        assertThat(accountingRecord.getFinancialStatements()).doesNotContain(financialStatementBack);
        assertThat(financialStatementBack.getAccountingRecords()).doesNotContain(accountingRecord);

        accountingRecord.financialStatements(new HashSet<>(Set.of(financialStatementBack)));
        assertThat(accountingRecord.getFinancialStatements()).containsOnly(financialStatementBack);
        assertThat(financialStatementBack.getAccountingRecords()).containsOnly(accountingRecord);

        accountingRecord.setFinancialStatements(new HashSet<>());
        assertThat(accountingRecord.getFinancialStatements()).doesNotContain(financialStatementBack);
        assertThat(financialStatementBack.getAccountingRecords()).doesNotContain(accountingRecord);
    }
}
