package com.fanflip.multimedia.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.fanflip.multimedia.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ContentPackageDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContentPackageDTO.class);
        ContentPackageDTO contentPackageDTO1 = new ContentPackageDTO();
        contentPackageDTO1.setId(1L);
        ContentPackageDTO contentPackageDTO2 = new ContentPackageDTO();
        assertThat(contentPackageDTO1).isNotEqualTo(contentPackageDTO2);
        contentPackageDTO2.setId(contentPackageDTO1.getId());
        assertThat(contentPackageDTO1).isEqualTo(contentPackageDTO2);
        contentPackageDTO2.setId(2L);
        assertThat(contentPackageDTO1).isNotEqualTo(contentPackageDTO2);
        contentPackageDTO1.setId(null);
        assertThat(contentPackageDTO1).isNotEqualTo(contentPackageDTO2);
    }
}
