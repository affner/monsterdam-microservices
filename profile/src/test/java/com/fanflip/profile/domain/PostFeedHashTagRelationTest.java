package com.fanflip.profile.domain;

import static com.fanflip.profile.domain.HashTagTestSamples.*;
import static com.fanflip.profile.domain.PostFeedHashTagRelationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.fanflip.profile.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PostFeedHashTagRelationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PostFeedHashTagRelation.class);
        PostFeedHashTagRelation postFeedHashTagRelation1 = getPostFeedHashTagRelationSample1();
        PostFeedHashTagRelation postFeedHashTagRelation2 = new PostFeedHashTagRelation();
        assertThat(postFeedHashTagRelation1).isNotEqualTo(postFeedHashTagRelation2);

        postFeedHashTagRelation2.setId(postFeedHashTagRelation1.getId());
        assertThat(postFeedHashTagRelation1).isEqualTo(postFeedHashTagRelation2);

        postFeedHashTagRelation2 = getPostFeedHashTagRelationSample2();
        assertThat(postFeedHashTagRelation1).isNotEqualTo(postFeedHashTagRelation2);
    }

    @Test
    void hashtagTest() throws Exception {
        PostFeedHashTagRelation postFeedHashTagRelation = getPostFeedHashTagRelationRandomSampleGenerator();
        HashTag hashTagBack = getHashTagRandomSampleGenerator();

        postFeedHashTagRelation.setHashtag(hashTagBack);
        assertThat(postFeedHashTagRelation.getHashtag()).isEqualTo(hashTagBack);

        postFeedHashTagRelation.hashtag(null);
        assertThat(postFeedHashTagRelation.getHashtag()).isNull();
    }
}
