package com.fanflip.profile.domain;

import static com.fanflip.profile.domain.HashTagTestSamples.*;
import static com.fanflip.profile.domain.PostFeedHashTagRelationTestSamples.*;
import static com.fanflip.profile.domain.UserProfileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.fanflip.profile.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class HashTagTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HashTag.class);
        HashTag hashTag1 = getHashTagSample1();
        HashTag hashTag2 = new HashTag();
        assertThat(hashTag1).isNotEqualTo(hashTag2);

        hashTag2.setId(hashTag1.getId());
        assertThat(hashTag1).isEqualTo(hashTag2);

        hashTag2 = getHashTagSample2();
        assertThat(hashTag1).isNotEqualTo(hashTag2);
    }

    @Test
    void postFeedsTest() throws Exception {
        HashTag hashTag = getHashTagRandomSampleGenerator();
        PostFeedHashTagRelation postFeedHashTagRelationBack = getPostFeedHashTagRelationRandomSampleGenerator();

        hashTag.addPostFeeds(postFeedHashTagRelationBack);
        assertThat(hashTag.getPostFeeds()).containsOnly(postFeedHashTagRelationBack);
        assertThat(postFeedHashTagRelationBack.getHashtag()).isEqualTo(hashTag);

        hashTag.removePostFeeds(postFeedHashTagRelationBack);
        assertThat(hashTag.getPostFeeds()).doesNotContain(postFeedHashTagRelationBack);
        assertThat(postFeedHashTagRelationBack.getHashtag()).isNull();

        hashTag.postFeeds(new HashSet<>(Set.of(postFeedHashTagRelationBack)));
        assertThat(hashTag.getPostFeeds()).containsOnly(postFeedHashTagRelationBack);
        assertThat(postFeedHashTagRelationBack.getHashtag()).isEqualTo(hashTag);

        hashTag.setPostFeeds(new HashSet<>());
        assertThat(hashTag.getPostFeeds()).doesNotContain(postFeedHashTagRelationBack);
        assertThat(postFeedHashTagRelationBack.getHashtag()).isNull();
    }

    @Test
    void userTest() throws Exception {
        HashTag hashTag = getHashTagRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        hashTag.addUser(userProfileBack);
        assertThat(hashTag.getUsers()).containsOnly(userProfileBack);
        assertThat(userProfileBack.getHashtags()).containsOnly(hashTag);

        hashTag.removeUser(userProfileBack);
        assertThat(hashTag.getUsers()).doesNotContain(userProfileBack);
        assertThat(userProfileBack.getHashtags()).doesNotContain(hashTag);

        hashTag.users(new HashSet<>(Set.of(userProfileBack)));
        assertThat(hashTag.getUsers()).containsOnly(userProfileBack);
        assertThat(userProfileBack.getHashtags()).containsOnly(hashTag);

        hashTag.setUsers(new HashSet<>());
        assertThat(hashTag.getUsers()).doesNotContain(userProfileBack);
        assertThat(userProfileBack.getHashtags()).doesNotContain(hashTag);
    }
}
