package com.fanflip.multimedia.domain;

import static com.fanflip.multimedia.domain.ContentPackageTestSamples.*;
import static com.fanflip.multimedia.domain.SinglePhotoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.fanflip.multimedia.web.rest.TestUtil;
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
    void belongPackageTest() throws Exception {
        SinglePhoto singlePhoto = getSinglePhotoRandomSampleGenerator();
        ContentPackage contentPackageBack = getContentPackageRandomSampleGenerator();

        singlePhoto.setBelongPackage(contentPackageBack);
        assertThat(singlePhoto.getBelongPackage()).isEqualTo(contentPackageBack);

        singlePhoto.belongPackage(null);
        assertThat(singlePhoto.getBelongPackage()).isNull();
    }
}
