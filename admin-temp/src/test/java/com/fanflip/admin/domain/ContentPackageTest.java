package com.monsterdam.admin.domain;

import static com.monsterdam.admin.domain.ContentPackageTestSamples.*;
import static com.monsterdam.admin.domain.DirectMessageTestSamples.*;
import static com.monsterdam.admin.domain.PostFeedTestSamples.*;
import static com.monsterdam.admin.domain.PurchasedContentTestSamples.*;
import static com.monsterdam.admin.domain.SingleAudioTestSamples.*;
import static com.monsterdam.admin.domain.SinglePhotoTestSamples.*;
import static com.monsterdam.admin.domain.SingleVideoTestSamples.*;
import static com.monsterdam.admin.domain.UserProfileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.admin.web.rest.TestUtil;
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
    void selledPackagesTest() throws Exception {
        ContentPackage contentPackage = getContentPackageRandomSampleGenerator();
        PurchasedContent purchasedContentBack = getPurchasedContentRandomSampleGenerator();

        contentPackage.addSelledPackages(purchasedContentBack);
        assertThat(contentPackage.getSelledPackages()).containsOnly(purchasedContentBack);
        assertThat(purchasedContentBack.getPurchasedContentPackage()).isEqualTo(contentPackage);

        contentPackage.removeSelledPackages(purchasedContentBack);
        assertThat(contentPackage.getSelledPackages()).doesNotContain(purchasedContentBack);
        assertThat(purchasedContentBack.getPurchasedContentPackage()).isNull();

        contentPackage.selledPackages(new HashSet<>(Set.of(purchasedContentBack)));
        assertThat(contentPackage.getSelledPackages()).containsOnly(purchasedContentBack);
        assertThat(purchasedContentBack.getPurchasedContentPackage()).isEqualTo(contentPackage);

        contentPackage.setSelledPackages(new HashSet<>());
        assertThat(contentPackage.getSelledPackages()).doesNotContain(purchasedContentBack);
        assertThat(purchasedContentBack.getPurchasedContentPackage()).isNull();
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
    void usersTaggedTest() throws Exception {
        ContentPackage contentPackage = getContentPackageRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        contentPackage.addUsersTagged(userProfileBack);
        assertThat(contentPackage.getUsersTaggeds()).containsOnly(userProfileBack);

        contentPackage.removeUsersTagged(userProfileBack);
        assertThat(contentPackage.getUsersTaggeds()).doesNotContain(userProfileBack);

        contentPackage.usersTaggeds(new HashSet<>(Set.of(userProfileBack)));
        assertThat(contentPackage.getUsersTaggeds()).containsOnly(userProfileBack);

        contentPackage.setUsersTaggeds(new HashSet<>());
        assertThat(contentPackage.getUsersTaggeds()).doesNotContain(userProfileBack);
    }

    @Test
    void messageTest() throws Exception {
        ContentPackage contentPackage = getContentPackageRandomSampleGenerator();
        DirectMessage directMessageBack = getDirectMessageRandomSampleGenerator();

        contentPackage.setMessage(directMessageBack);
        assertThat(contentPackage.getMessage()).isEqualTo(directMessageBack);
        assertThat(directMessageBack.getContentPackage()).isEqualTo(contentPackage);

        contentPackage.message(null);
        assertThat(contentPackage.getMessage()).isNull();
        assertThat(directMessageBack.getContentPackage()).isNull();
    }

    @Test
    void postTest() throws Exception {
        ContentPackage contentPackage = getContentPackageRandomSampleGenerator();
        PostFeed postFeedBack = getPostFeedRandomSampleGenerator();

        contentPackage.setPost(postFeedBack);
        assertThat(contentPackage.getPost()).isEqualTo(postFeedBack);
        assertThat(postFeedBack.getContentPackage()).isEqualTo(contentPackage);

        contentPackage.post(null);
        assertThat(contentPackage.getPost()).isNull();
        assertThat(postFeedBack.getContentPackage()).isNull();
    }
}
