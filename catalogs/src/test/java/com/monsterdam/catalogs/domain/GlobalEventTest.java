package com.monsterdam.catalogs.domain;

import static com.monsterdam.catalogs.domain.GlobalEventTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.catalogs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GlobalEventTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GlobalEvent.class);
        GlobalEvent globalEvent1 = getGlobalEventSample1();
        GlobalEvent globalEvent2 = new GlobalEvent();
        assertThat(globalEvent1).isNotEqualTo(globalEvent2);

        globalEvent2.setId(globalEvent1.getId());
        assertThat(globalEvent1).isEqualTo(globalEvent2);

        globalEvent2 = getGlobalEventSample2();
        assertThat(globalEvent1).isNotEqualTo(globalEvent2);
    }
}
