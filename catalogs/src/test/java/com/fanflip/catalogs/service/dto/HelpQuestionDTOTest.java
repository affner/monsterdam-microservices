package com.fanflip.catalogs.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.fanflip.catalogs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HelpQuestionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HelpQuestionDTO.class);
        HelpQuestionDTO helpQuestionDTO1 = new HelpQuestionDTO();
        helpQuestionDTO1.setId(1L);
        HelpQuestionDTO helpQuestionDTO2 = new HelpQuestionDTO();
        assertThat(helpQuestionDTO1).isNotEqualTo(helpQuestionDTO2);
        helpQuestionDTO2.setId(helpQuestionDTO1.getId());
        assertThat(helpQuestionDTO1).isEqualTo(helpQuestionDTO2);
        helpQuestionDTO2.setId(2L);
        assertThat(helpQuestionDTO1).isNotEqualTo(helpQuestionDTO2);
        helpQuestionDTO1.setId(null);
        assertThat(helpQuestionDTO1).isNotEqualTo(helpQuestionDTO2);
    }
}
