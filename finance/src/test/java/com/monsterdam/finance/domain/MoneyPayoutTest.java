package com.monsterdam.finance.domain;

import static com.monsterdam.finance.domain.CreatorEarningTestSamples.*;
import static com.monsterdam.finance.domain.MoneyPayoutTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.finance.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MoneyPayoutTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MoneyPayout.class);
        MoneyPayout moneyPayout1 = getMoneyPayoutSample1();
        MoneyPayout moneyPayout2 = new MoneyPayout();
        assertThat(moneyPayout1).isNotEqualTo(moneyPayout2);

        moneyPayout2.setId(moneyPayout1.getId());
        assertThat(moneyPayout1).isEqualTo(moneyPayout2);

        moneyPayout2 = getMoneyPayoutSample2();
        assertThat(moneyPayout1).isNotEqualTo(moneyPayout2);
    }

    @Test
    void creatorEarningTest() throws Exception {
        MoneyPayout moneyPayout = getMoneyPayoutRandomSampleGenerator();
        CreatorEarning creatorEarningBack = getCreatorEarningRandomSampleGenerator();

        moneyPayout.setCreatorEarning(creatorEarningBack);
        assertThat(moneyPayout.getCreatorEarning()).isEqualTo(creatorEarningBack);

        moneyPayout.creatorEarning(null);
        assertThat(moneyPayout.getCreatorEarning()).isNull();
    }
}
