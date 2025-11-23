package com.fanflip.admin.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.fanflip.admin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IdentityDocumentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(IdentityDocumentDTO.class);
        IdentityDocumentDTO identityDocumentDTO1 = new IdentityDocumentDTO();
        identityDocumentDTO1.setId(1L);
        IdentityDocumentDTO identityDocumentDTO2 = new IdentityDocumentDTO();
        assertThat(identityDocumentDTO1).isNotEqualTo(identityDocumentDTO2);
        identityDocumentDTO2.setId(identityDocumentDTO1.getId());
        assertThat(identityDocumentDTO1).isEqualTo(identityDocumentDTO2);
        identityDocumentDTO2.setId(2L);
        assertThat(identityDocumentDTO1).isNotEqualTo(identityDocumentDTO2);
        identityDocumentDTO1.setId(null);
        assertThat(identityDocumentDTO1).isNotEqualTo(identityDocumentDTO2);
    }
}
