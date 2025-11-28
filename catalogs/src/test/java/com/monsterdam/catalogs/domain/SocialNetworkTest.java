package com.monsterdam.catalogs.domain;

import static com.monsterdam.catalogs.domain.SocialNetworkTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.catalogs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SocialNetworkTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SocialNetwork.class);
        SocialNetwork socialNetwork1 = getSocialNetworkSample1();
        SocialNetwork socialNetwork2 = new SocialNetwork();
        assertThat(socialNetwork1).isNotEqualTo(socialNetwork2);

        socialNetwork2.setId(socialNetwork1.getId());
        assertThat(socialNetwork1).isEqualTo(socialNetwork2);

        socialNetwork2 = getSocialNetworkSample2();
        assertThat(socialNetwork1).isNotEqualTo(socialNetwork2);
    }
}
