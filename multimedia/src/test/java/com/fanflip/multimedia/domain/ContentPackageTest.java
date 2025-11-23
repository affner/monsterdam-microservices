package com.fanflip.multimedia.domain;

import static com.fanflip.multimedia.domain.ContentPackageTestSamples.*;
import static com.fanflip.multimedia.domain.SingleAudioTestSamples.*;
import static com.fanflip.multimedia.domain.SingleLiveStreamTestSamples.getSingleLiveStreamRandomSampleGenerator;
import static com.fanflip.multimedia.domain.SinglePhotoTestSamples.*;
import static com.fanflip.multimedia.domain.SingleVideoTestSamples.*;
import static com.fanflip.multimedia.domain.SpecialRewardTestSamples.*;
import static com.fanflip.multimedia.domain.UserTagRelationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.fanflip.multimedia.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ContentPackageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContentPackage.class);
        ContentPackage contentPackage1 = getContentPackageSample1();
        ContentPackage contentPackage2 = new ContentPackage();
        assertThat(contentPackage1).isNotEqualTo(contentPackage2);

        contentPackage2.setId(contentPackage1.getId());
        assertThat(contentPackage1).isEqualTo(contentPackage2);

        contentPackage2 = getContentPackageSample2();
        assertThat(contentPackage1).isNotEqualTo(contentPackage2);
    }

    @Test
    void audioTest() throws Exception {
        ContentPackage contentPackage = getContentPackageRandomSampleGenerator();
        SingleAudio singleAudioBack = getSingleAudioRandomSampleGenerator();

        contentPackage.setAudio(singleAudioBack);
        assertThat(contentPackage.getAudio()).isEqualTo(singleAudioBack);

        contentPackage.audio(null);
        assertThat(contentPackage.getAudio()).isNull();
    }

    @Test
    void liveStreamTest() throws Exception {
        ContentPackage contentPackage = getContentPackageRandomSampleGenerator();
        SingleLiveStream singleLiveStreamBack = getSingleLiveStreamRandomSampleGenerator();

        contentPackage.setLiveStream(singleLiveStreamBack);
        assertThat(contentPackage.getLiveStream()).isEqualTo(singleLiveStreamBack);

        contentPackage.liveStream(null);
        assertThat(contentPackage.getLiveStream()).isNull();
    }

    @Test
    void videosTest() throws Exception {
        ContentPackage contentPackage = getContentPackageRandomSampleGenerator();
        SingleVideo singleVideoBack = getSingleVideoRandomSampleGenerator();

        contentPackage.addVideos(singleVideoBack);
        assertThat(contentPackage.getVideos()).containsOnly(singleVideoBack);
        assertThat(singleVideoBack.getBelongPackage()).isEqualTo(contentPackage);

        contentPackage.removeVideos(singleVideoBack);
        assertThat(contentPackage.getVideos()).doesNotContain(singleVideoBack);
        assertThat(singleVideoBack.getBelongPackage()).isNull();

        contentPackage.videos(new HashSet<>(Set.of(singleVideoBack)));
        assertThat(contentPackage.getVideos()).containsOnly(singleVideoBack);
        assertThat(singleVideoBack.getBelongPackage()).isEqualTo(contentPackage);

        contentPackage.setVideos(new HashSet<>());
        assertThat(contentPackage.getVideos()).doesNotContain(singleVideoBack);
        assertThat(singleVideoBack.getBelongPackage()).isNull();
    }

    @Test
    void photosTest() throws Exception {
        ContentPackage contentPackage = getContentPackageRandomSampleGenerator();
        SinglePhoto singlePhotoBack = getSinglePhotoRandomSampleGenerator();

        contentPackage.addPhotos(singlePhotoBack);
        assertThat(contentPackage.getPhotos()).containsOnly(singlePhotoBack);
        assertThat(singlePhotoBack.getBelongPackage()).isEqualTo(contentPackage);

        contentPackage.removePhotos(singlePhotoBack);
        assertThat(contentPackage.getPhotos()).doesNotContain(singlePhotoBack);
        assertThat(singlePhotoBack.getBelongPackage()).isNull();

        contentPackage.photos(new HashSet<>(Set.of(singlePhotoBack)));
        assertThat(contentPackage.getPhotos()).containsOnly(singlePhotoBack);
        assertThat(singlePhotoBack.getBelongPackage()).isEqualTo(contentPackage);

        contentPackage.setPhotos(new HashSet<>());
        assertThat(contentPackage.getPhotos()).doesNotContain(singlePhotoBack);
        assertThat(singlePhotoBack.getBelongPackage()).isNull();
    }

    @Test
    void tagsTest() throws Exception {
        ContentPackage contentPackage = getContentPackageRandomSampleGenerator();
        UserTagRelation userTagRelationBack = getUserTagRelationRandomSampleGenerator();

        contentPackage.addTags(userTagRelationBack);
        assertThat(contentPackage.getTags()).containsOnly(userTagRelationBack);
        assertThat(userTagRelationBack.getContentPackage()).isEqualTo(contentPackage);

        contentPackage.removeTags(userTagRelationBack);
        assertThat(contentPackage.getTags()).doesNotContain(userTagRelationBack);
        assertThat(userTagRelationBack.getContentPackage()).isNull();

        contentPackage.tags(new HashSet<>(Set.of(userTagRelationBack)));
        assertThat(contentPackage.getTags()).containsOnly(userTagRelationBack);
        assertThat(userTagRelationBack.getContentPackage()).isEqualTo(contentPackage);

        contentPackage.setTags(new HashSet<>());
        assertThat(contentPackage.getTags()).doesNotContain(userTagRelationBack);
        assertThat(userTagRelationBack.getContentPackage()).isNull();
    }

    @Test
    void specialRewardTest() throws Exception {
        ContentPackage contentPackage = getContentPackageRandomSampleGenerator();
        SpecialReward specialRewardBack = getSpecialRewardRandomSampleGenerator();

        contentPackage.setSpecialReward(specialRewardBack);
        assertThat(contentPackage.getSpecialReward()).isEqualTo(specialRewardBack);
        assertThat(specialRewardBack.getContentPackage()).isEqualTo(contentPackage);

        contentPackage.specialReward(null);
        assertThat(contentPackage.getSpecialReward()).isNull();
        assertThat(specialRewardBack.getContentPackage()).isNull();
    }
}
