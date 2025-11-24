package com.monsterdam.admin.domain;

import static com.monsterdam.admin.domain.ContentPackageTestSamples.*;
import static com.monsterdam.admin.domain.SingleVideoTestSamples.*;
import static com.monsterdam.admin.domain.UserReportTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.admin.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class SingleVideoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SingleVideo.class);
        SingleVideo singleVideo1 = getSingleVideoSample1();
        SingleVideo singleVideo2 = new SingleVideo();
        assertThat(singleVideo1).isNotEqualTo(singleVideo2);

        singleVideo2.setId(singleVideo1.getId());
        assertThat(singleVideo1).isEqualTo(singleVideo2);

        singleVideo2 = getSingleVideoSample2();
        assertThat(singleVideo1).isNotEqualTo(singleVideo2);
    }

    @Test
    void reportsTest() throws Exception {
        SingleVideo singleVideo = getSingleVideoRandomSampleGenerator();
        UserReport userReportBack = getUserReportRandomSampleGenerator();

        singleVideo.addReports(userReportBack);
        assertThat(singleVideo.getReports()).containsOnly(userReportBack);
        assertThat(userReportBack.getVideo()).isEqualTo(singleVideo);

        singleVideo.removeReports(userReportBack);
        assertThat(singleVideo.getReports()).doesNotContain(userReportBack);
        assertThat(userReportBack.getVideo()).isNull();

        singleVideo.reports(new HashSet<>(Set.of(userReportBack)));
        assertThat(singleVideo.getReports()).containsOnly(userReportBack);
        assertThat(userReportBack.getVideo()).isEqualTo(singleVideo);

        singleVideo.setReports(new HashSet<>());
        assertThat(singleVideo.getReports()).doesNotContain(userReportBack);
        assertThat(userReportBack.getVideo()).isNull();
    }

    @Test
    void belongPackageTest() throws Exception {
        SingleVideo singleVideo = getSingleVideoRandomSampleGenerator();
        ContentPackage contentPackageBack = getContentPackageRandomSampleGenerator();

        singleVideo.setBelongPackage(contentPackageBack);
        assertThat(singleVideo.getBelongPackage()).isEqualTo(contentPackageBack);

        singleVideo.belongPackage(null);
        assertThat(singleVideo.getBelongPackage()).isNull();
    }
}
