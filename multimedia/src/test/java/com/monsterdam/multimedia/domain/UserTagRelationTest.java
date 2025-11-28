package com.monsterdam.multimedia.domain;

import static com.monsterdam.multimedia.domain.ContentPackageTestSamples.*;
import static com.monsterdam.multimedia.domain.UserTagRelationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.multimedia.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserTagRelationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserTagRelation.class);
        UserTagRelation userTagRelation1 = getUserTagRelationSample1();
        UserTagRelation userTagRelation2 = new UserTagRelation();
        assertThat(userTagRelation1).isNotEqualTo(userTagRelation2);

        userTagRelation2.setId(userTagRelation1.getId());
        assertThat(userTagRelation1).isEqualTo(userTagRelation2);

        userTagRelation2 = getUserTagRelationSample2();
        assertThat(userTagRelation1).isNotEqualTo(userTagRelation2);
    }

    @Test
    void contentPackageTest() throws Exception {
        UserTagRelation userTagRelation = getUserTagRelationRandomSampleGenerator();
        ContentPackage contentPackageBack = getContentPackageRandomSampleGenerator();

        userTagRelation.setContentPackage(contentPackageBack);
        assertThat(userTagRelation.getContentPackage()).isEqualTo(contentPackageBack);

        userTagRelation.contentPackage(null);
        assertThat(userTagRelation.getContentPackage()).isNull();
    }
}
