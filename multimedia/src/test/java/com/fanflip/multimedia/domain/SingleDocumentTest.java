package com.monsterdam.multimedia.domain;

import static com.monsterdam.multimedia.domain.SingleDocumentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.multimedia.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SingleDocumentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SingleDocument.class);
        SingleDocument singleDocument1 = getSingleDocumentSample1();
        SingleDocument singleDocument2 = new SingleDocument();
        assertThat(singleDocument1).isNotEqualTo(singleDocument2);

        singleDocument2.setId(singleDocument1.getId());
        assertThat(singleDocument1).isEqualTo(singleDocument2);

        singleDocument2 = getSingleDocumentSample2();
        assertThat(singleDocument1).isNotEqualTo(singleDocument2);
    }
}
