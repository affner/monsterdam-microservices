package com.fanflip.admin.domain;

import static com.fanflip.admin.domain.SingleDocumentTestSamples.*;
import static com.fanflip.admin.domain.UserProfileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.fanflip.admin.web.rest.TestUtil;
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

    @Test
    void userTest() throws Exception {
        SingleDocument singleDocument = getSingleDocumentRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        singleDocument.setUser(userProfileBack);
        assertThat(singleDocument.getUser()).isEqualTo(userProfileBack);

        singleDocument.user(null);
        assertThat(singleDocument.getUser()).isNull();
    }
}
