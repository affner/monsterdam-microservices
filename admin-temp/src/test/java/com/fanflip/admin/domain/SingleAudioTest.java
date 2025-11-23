package com.fanflip.admin.domain;

import static com.fanflip.admin.domain.ContentPackageTestSamples.*;
import static com.fanflip.admin.domain.SingleAudioTestSamples.*;
import static com.fanflip.admin.domain.UserReportTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.fanflip.admin.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class SingleAudioTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SingleAudio.class);
        SingleAudio singleAudio1 = getSingleAudioSample1();
        SingleAudio singleAudio2 = new SingleAudio();
        assertThat(singleAudio1).isNotEqualTo(singleAudio2);

        singleAudio2.setId(singleAudio1.getId());
        assertThat(singleAudio1).isEqualTo(singleAudio2);

        singleAudio2 = getSingleAudioSample2();
        assertThat(singleAudio1).isNotEqualTo(singleAudio2);
    }

    @Test
    void reportsTest() throws Exception {
        SingleAudio singleAudio = getSingleAudioRandomSampleGenerator();
        UserReport userReportBack = getUserReportRandomSampleGenerator();

        singleAudio.addReports(userReportBack);
        assertThat(singleAudio.getReports()).containsOnly(userReportBack);
        assertThat(userReportBack.getAudio()).isEqualTo(singleAudio);

        singleAudio.removeReports(userReportBack);
        assertThat(singleAudio.getReports()).doesNotContain(userReportBack);
        assertThat(userReportBack.getAudio()).isNull();

        singleAudio.reports(new HashSet<>(Set.of(userReportBack)));
        assertThat(singleAudio.getReports()).containsOnly(userReportBack);
        assertThat(userReportBack.getAudio()).isEqualTo(singleAudio);

        singleAudio.setReports(new HashSet<>());
        assertThat(singleAudio.getReports()).doesNotContain(userReportBack);
        assertThat(userReportBack.getAudio()).isNull();
    }

    @Test
    void contentPackageTest() throws Exception {
        SingleAudio singleAudio = getSingleAudioRandomSampleGenerator();
        ContentPackage contentPackageBack = getContentPackageRandomSampleGenerator();

        singleAudio.setContentPackage(contentPackageBack);
        assertThat(singleAudio.getContentPackage()).isEqualTo(contentPackageBack);
        assertThat(contentPackageBack.getAudio()).isEqualTo(singleAudio);

        singleAudio.contentPackage(null);
        assertThat(singleAudio.getContentPackage()).isNull();
        assertThat(contentPackageBack.getAudio()).isNull();
    }
}
