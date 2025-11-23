package com.fanflip.admin.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.fanflip.admin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TaxInfoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TaxInfoDTO.class);
        TaxInfoDTO taxInfoDTO1 = new TaxInfoDTO();
        taxInfoDTO1.setId(1L);
        TaxInfoDTO taxInfoDTO2 = new TaxInfoDTO();
        assertThat(taxInfoDTO1).isNotEqualTo(taxInfoDTO2);
        taxInfoDTO2.setId(taxInfoDTO1.getId());
        assertThat(taxInfoDTO1).isEqualTo(taxInfoDTO2);
        taxInfoDTO2.setId(2L);
        assertThat(taxInfoDTO1).isNotEqualTo(taxInfoDTO2);
        taxInfoDTO1.setId(null);
        assertThat(taxInfoDTO1).isNotEqualTo(taxInfoDTO2);
    }
}
