package com.monsterdam.admin.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.admin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EmojiTypeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmojiTypeDTO.class);
        EmojiTypeDTO emojiTypeDTO1 = new EmojiTypeDTO();
        emojiTypeDTO1.setId(1L);
        EmojiTypeDTO emojiTypeDTO2 = new EmojiTypeDTO();
        assertThat(emojiTypeDTO1).isNotEqualTo(emojiTypeDTO2);
        emojiTypeDTO2.setId(emojiTypeDTO1.getId());
        assertThat(emojiTypeDTO1).isEqualTo(emojiTypeDTO2);
        emojiTypeDTO2.setId(2L);
        assertThat(emojiTypeDTO1).isNotEqualTo(emojiTypeDTO2);
        emojiTypeDTO1.setId(null);
        assertThat(emojiTypeDTO1).isNotEqualTo(emojiTypeDTO2);
    }
}
