package com.fanflip.admin.domain;

import static com.fanflip.admin.domain.SpecialTitleTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.fanflip.admin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SpecialTitleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SpecialTitle.class);
        SpecialTitle specialTitle1 = getSpecialTitleSample1();
        SpecialTitle specialTitle2 = new SpecialTitle();
        assertThat(specialTitle1).isNotEqualTo(specialTitle2);

        specialTitle2.setId(specialTitle1.getId());
        assertThat(specialTitle1).isEqualTo(specialTitle2);

        specialTitle2 = getSpecialTitleSample2();
        assertThat(specialTitle1).isNotEqualTo(specialTitle2);
    }
}
