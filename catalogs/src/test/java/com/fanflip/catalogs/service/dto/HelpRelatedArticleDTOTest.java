package com.fanflip.catalogs.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.fanflip.catalogs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HelpRelatedArticleDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HelpRelatedArticleDTO.class);
        HelpRelatedArticleDTO helpRelatedArticleDTO1 = new HelpRelatedArticleDTO();
        helpRelatedArticleDTO1.setId(1L);
        HelpRelatedArticleDTO helpRelatedArticleDTO2 = new HelpRelatedArticleDTO();
        assertThat(helpRelatedArticleDTO1).isNotEqualTo(helpRelatedArticleDTO2);
        helpRelatedArticleDTO2.setId(helpRelatedArticleDTO1.getId());
        assertThat(helpRelatedArticleDTO1).isEqualTo(helpRelatedArticleDTO2);
        helpRelatedArticleDTO2.setId(2L);
        assertThat(helpRelatedArticleDTO1).isNotEqualTo(helpRelatedArticleDTO2);
        helpRelatedArticleDTO1.setId(null);
        assertThat(helpRelatedArticleDTO1).isNotEqualTo(helpRelatedArticleDTO2);
    }
}
