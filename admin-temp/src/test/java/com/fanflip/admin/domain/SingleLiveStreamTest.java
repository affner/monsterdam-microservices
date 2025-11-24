package com.monsterdam.admin.domain;

import static com.monsterdam.admin.domain.SingleLiveStreamTestSamples.*;
import static com.monsterdam.admin.domain.UserReportTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.admin.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class SingleLiveStreamTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SingleLiveStream.class);
        SingleLiveStream singleLiveStream1 = getSingleLiveStreamSample1();
        SingleLiveStream singleLiveStream2 = new SingleLiveStream();
        assertThat(singleLiveStream1).isNotEqualTo(singleLiveStream2);

        singleLiveStream2.setId(singleLiveStream1.getId());
        assertThat(singleLiveStream1).isEqualTo(singleLiveStream2);

        singleLiveStream2 = getSingleLiveStreamSample2();
        assertThat(singleLiveStream1).isNotEqualTo(singleLiveStream2);
    }

    @Test
    void reportsTest() throws Exception {
        SingleLiveStream singleLiveStream = getSingleLiveStreamRandomSampleGenerator();
        UserReport userReportBack = getUserReportRandomSampleGenerator();

        singleLiveStream.addReports(userReportBack);
        assertThat(singleLiveStream.getReports()).containsOnly(userReportBack);
        assertThat(userReportBack.getLiveStream()).isEqualTo(singleLiveStream);

        singleLiveStream.removeReports(userReportBack);
        assertThat(singleLiveStream.getReports()).doesNotContain(userReportBack);
        assertThat(userReportBack.getLiveStream()).isNull();

        singleLiveStream.reports(new HashSet<>(Set.of(userReportBack)));
        assertThat(singleLiveStream.getReports()).containsOnly(userReportBack);
        assertThat(userReportBack.getLiveStream()).isEqualTo(singleLiveStream);

        singleLiveStream.setReports(new HashSet<>());
        assertThat(singleLiveStream.getReports()).doesNotContain(userReportBack);
        assertThat(userReportBack.getLiveStream()).isNull();
    }
}
