package com.monsterdam.admin.domain;

import static com.monsterdam.admin.domain.ContentPackageTestSamples.*;
import static com.monsterdam.admin.domain.SinglePhotoTestSamples.*;
import static com.monsterdam.admin.domain.UserReportTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.admin.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class SinglePhotoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SinglePhoto.class);
        SinglePhoto singlePhoto1 = getSinglePhotoSample1();
        SinglePhoto singlePhoto2 = new SinglePhoto();
        assertThat(singlePhoto1).isNotEqualTo(singlePhoto2);

        singlePhoto2.setId(singlePhoto1.getId());
        assertThat(singlePhoto1).isEqualTo(singlePhoto2);

        singlePhoto2 = getSinglePhotoSample2();
        assertThat(singlePhoto1).isNotEqualTo(singlePhoto2);
    }

    @Test
    void reportsTest() throws Exception {
        SinglePhoto singlePhoto = getSinglePhotoRandomSampleGenerator();
        UserReport userReportBack = getUserReportRandomSampleGenerator();

        singlePhoto.addReports(userReportBack);
        assertThat(singlePhoto.getReports()).containsOnly(userReportBack);
        assertThat(userReportBack.getPhoto()).isEqualTo(singlePhoto);

        singlePhoto.removeReports(userReportBack);
        assertThat(singlePhoto.getReports()).doesNotContain(userReportBack);
        assertThat(userReportBack.getPhoto()).isNull();

        singlePhoto.reports(new HashSet<>(Set.of(userReportBack)));
        assertThat(singlePhoto.getReports()).containsOnly(userReportBack);
        assertThat(userReportBack.getPhoto()).isEqualTo(singlePhoto);

        singlePhoto.setReports(new HashSet<>());
        assertThat(singlePhoto.getReports()).doesNotContain(userReportBack);
        assertThat(userReportBack.getPhoto()).isNull();
    }

    @Test
    void belongPackageTest() throws Exception {
        SinglePhoto singlePhoto = getSinglePhotoRandomSampleGenerator();
        ContentPackage contentPackageBack = getContentPackageRandomSampleGenerator();

        singlePhoto.setBelongPackage(contentPackageBack);
        assertThat(singlePhoto.getBelongPackage()).isEqualTo(contentPackageBack);

        singlePhoto.belongPackage(null);
        assertThat(singlePhoto.getBelongPackage()).isNull();
    }
}
