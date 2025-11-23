package com.fanflip.admin.domain;

import static com.fanflip.admin.domain.HashTagTestSamples.*;
import static com.fanflip.admin.domain.PostFeedTestSamples.*;
import static com.fanflip.admin.domain.UserProfileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.fanflip.admin.web.rest.TestUtil;
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
    void postsTest() throws Exception {
        HashTag hashTag = getHashTagRandomSampleGenerator();
        PostFeed postFeedBack = getPostFeedRandomSampleGenerator();

        hashTag.addPosts(postFeedBack);
        assertThat(hashTag.getPosts()).containsOnly(postFeedBack);
        assertThat(postFeedBack.getHashTags()).containsOnly(hashTag);

        hashTag.removePosts(postFeedBack);
        assertThat(hashTag.getPosts()).doesNotContain(postFeedBack);
        assertThat(postFeedBack.getHashTags()).doesNotContain(hashTag);

        hashTag.posts(new HashSet<>(Set.of(postFeedBack)));
        assertThat(hashTag.getPosts()).containsOnly(postFeedBack);
        assertThat(postFeedBack.getHashTags()).containsOnly(hashTag);

        hashTag.setPosts(new HashSet<>());
        assertThat(hashTag.getPosts()).doesNotContain(postFeedBack);
        assertThat(postFeedBack.getHashTags()).doesNotContain(hashTag);
    }

    @Test
    void profilesTest() throws Exception {
        HashTag hashTag = getHashTagRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        hashTag.addProfiles(userProfileBack);
        assertThat(hashTag.getProfiles()).containsOnly(userProfileBack);
        assertThat(userProfileBack.getHashTags()).containsOnly(hashTag);

        hashTag.removeProfiles(userProfileBack);
        assertThat(hashTag.getProfiles()).doesNotContain(userProfileBack);
        assertThat(userProfileBack.getHashTags()).doesNotContain(hashTag);

        hashTag.profiles(new HashSet<>(Set.of(userProfileBack)));
        assertThat(hashTag.getProfiles()).containsOnly(userProfileBack);
        assertThat(userProfileBack.getHashTags()).containsOnly(hashTag);

        hashTag.setProfiles(new HashSet<>());
        assertThat(hashTag.getProfiles()).doesNotContain(userProfileBack);
        assertThat(userProfileBack.getHashTags()).doesNotContain(hashTag);
    }
}
