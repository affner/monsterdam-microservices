package com.monsterdam.admin.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.admin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SingleDocumentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SingleDocumentDTO.class);
        SingleDocumentDTO singleDocumentDTO1 = new SingleDocumentDTO();
        singleDocumentDTO1.setId(1L);
        SingleDocumentDTO singleDocumentDTO2 = new SingleDocumentDTO();
        assertThat(singleDocumentDTO1).isNotEqualTo(singleDocumentDTO2);
        singleDocumentDTO2.setId(singleDocumentDTO1.getId());
        assertThat(singleDocumentDTO1).isEqualTo(singleDocumentDTO2);
        singleDocumentDTO2.setId(2L);
        assertThat(singleDocumentDTO1).isNotEqualTo(singleDocumentDTO2);
        singleDocumentDTO1.setId(null);
        assertThat(singleDocumentDTO1).isNotEqualTo(singleDocumentDTO2);
    }
}
