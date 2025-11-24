package com.monsterdam.admin.domain;

import static com.monsterdam.admin.domain.CountryTestSamples.*;
import static com.monsterdam.admin.domain.TaxInfoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.admin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TaxInfoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TaxInfo.class);
        TaxInfo taxInfo1 = getTaxInfoSample1();
        TaxInfo taxInfo2 = new TaxInfo();
        assertThat(taxInfo1).isNotEqualTo(taxInfo2);

        taxInfo2.setId(taxInfo1.getId());
        assertThat(taxInfo1).isEqualTo(taxInfo2);

        taxInfo2 = getTaxInfoSample2();
        assertThat(taxInfo1).isNotEqualTo(taxInfo2);
    }

    @Test
    void countryTest() throws Exception {
        TaxInfo taxInfo = getTaxInfoRandomSampleGenerator();
        Country countryBack = getCountryRandomSampleGenerator();

        taxInfo.setCountry(countryBack);
        assertThat(taxInfo.getCountry()).isEqualTo(countryBack);

        taxInfo.country(null);
        assertThat(taxInfo.getCountry()).isNull();
    }
}
