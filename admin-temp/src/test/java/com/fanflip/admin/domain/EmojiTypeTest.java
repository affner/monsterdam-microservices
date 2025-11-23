package com.fanflip.admin.domain;

import static com.fanflip.admin.domain.EmojiTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.fanflip.admin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EmojiTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmojiType.class);
        EmojiType emojiType1 = getEmojiTypeSample1();
        EmojiType emojiType2 = new EmojiType();
        assertThat(emojiType1).isNotEqualTo(emojiType2);

        emojiType2.setId(emojiType1.getId());
        assertThat(emojiType1).isEqualTo(emojiType2);

        emojiType2 = getEmojiTypeSample2();
        assertThat(emojiType1).isNotEqualTo(emojiType2);
    }
}
