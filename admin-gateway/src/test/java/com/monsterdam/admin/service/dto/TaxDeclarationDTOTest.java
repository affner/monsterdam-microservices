package com.monsterdam.admin.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.admin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TaxDeclarationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TaxDeclarationDTO.class);
        TaxDeclarationDTO taxDeclarationDTO1 = new TaxDeclarationDTO();
        taxDeclarationDTO1.setId(1L);
        TaxDeclarationDTO taxDeclarationDTO2 = new TaxDeclarationDTO();
        assertThat(taxDeclarationDTO1).isNotEqualTo(taxDeclarationDTO2);
        taxDeclarationDTO2.setId(taxDeclarationDTO1.getId());
        assertThat(taxDeclarationDTO1).isEqualTo(taxDeclarationDTO2);
        taxDeclarationDTO2.setId(2L);
        assertThat(taxDeclarationDTO1).isNotEqualTo(taxDeclarationDTO2);
        taxDeclarationDTO1.setId(null);
        assertThat(taxDeclarationDTO1).isNotEqualTo(taxDeclarationDTO2);
    }
}
