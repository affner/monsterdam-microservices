package com.monsterdam.admin.domain;

import static com.monsterdam.admin.domain.LikeMarkTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.admin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LikeMarkTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LikeMark.class);
        LikeMark likeMark1 = getLikeMarkSample1();
        LikeMark likeMark2 = new LikeMark();
        assertThat(likeMark1).isNotEqualTo(likeMark2);

        likeMark2.setId(likeMark1.getId());
        assertThat(likeMark1).isEqualTo(likeMark2);

        likeMark2 = getLikeMarkSample2();
        assertThat(likeMark1).isNotEqualTo(likeMark2);
    }
}
