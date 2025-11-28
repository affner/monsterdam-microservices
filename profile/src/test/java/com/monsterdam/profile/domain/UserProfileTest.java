package com.monsterdam.profile.domain;

import static com.monsterdam.profile.domain.HashTagTestSamples.*;
import static com.monsterdam.profile.domain.PersonalSocialLinksTestSamples.*;
import static com.monsterdam.profile.domain.StateUserRelationTestSamples.*;
import static com.monsterdam.profile.domain.UserEventTestSamples.*;
import static com.monsterdam.profile.domain.UserProfileTestSamples.*;
import static com.monsterdam.profile.domain.UserSettingsTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.profile.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class UserProfileTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserProfile.class);
        UserProfile userProfile1 = getUserProfileSample1();
        UserProfile userProfile2 = new UserProfile();
        assertThat(userProfile1).isNotEqualTo(userProfile2);

        userProfile2.setId(userProfile1.getId());
        assertThat(userProfile1).isEqualTo(userProfile2);

        userProfile2 = getUserProfileSample2();
        assertThat(userProfile1).isNotEqualTo(userProfile2);
    }

    @Test
    void settingsTest() throws Exception {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        UserSettings userSettingsBack = getUserSettingsRandomSampleGenerator();

        userProfile.setSettings(userSettingsBack);
        assertThat(userProfile.getSettings()).isEqualTo(userSettingsBack);

        userProfile.settings(null);
        assertThat(userProfile.getSettings()).isNull();
    }

    @Test
    void socialNetworksTest() throws Exception {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        PersonalSocialLinks personalSocialLinksBack = getPersonalSocialLinksRandomSampleGenerator();

        userProfile.addSocialNetworks(personalSocialLinksBack);
        assertThat(userProfile.getSocialNetworks()).containsOnly(personalSocialLinksBack);
        assertThat(personalSocialLinksBack.getUser()).isEqualTo(userProfile);

        userProfile.removeSocialNetworks(personalSocialLinksBack);
        assertThat(userProfile.getSocialNetworks()).doesNotContain(personalSocialLinksBack);
        assertThat(personalSocialLinksBack.getUser()).isNull();

        userProfile.socialNetworks(new HashSet<>(Set.of(personalSocialLinksBack)));
        assertThat(userProfile.getSocialNetworks()).containsOnly(personalSocialLinksBack);
        assertThat(personalSocialLinksBack.getUser()).isEqualTo(userProfile);

        userProfile.setSocialNetworks(new HashSet<>());
        assertThat(userProfile.getSocialNetworks()).doesNotContain(personalSocialLinksBack);
        assertThat(personalSocialLinksBack.getUser()).isNull();
    }

    @Test
    void blockedUbicationsTest() throws Exception {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        StateUserRelation stateUserRelationBack = getStateUserRelationRandomSampleGenerator();

        userProfile.addBlockedUbications(stateUserRelationBack);
        assertThat(userProfile.getBlockedUbications()).containsOnly(stateUserRelationBack);
        assertThat(stateUserRelationBack.getUser()).isEqualTo(userProfile);

        userProfile.removeBlockedUbications(stateUserRelationBack);
        assertThat(userProfile.getBlockedUbications()).doesNotContain(stateUserRelationBack);
        assertThat(stateUserRelationBack.getUser()).isNull();

        userProfile.blockedUbications(new HashSet<>(Set.of(stateUserRelationBack)));
        assertThat(userProfile.getBlockedUbications()).containsOnly(stateUserRelationBack);
        assertThat(stateUserRelationBack.getUser()).isEqualTo(userProfile);

        userProfile.setBlockedUbications(new HashSet<>());
        assertThat(userProfile.getBlockedUbications()).doesNotContain(stateUserRelationBack);
        assertThat(stateUserRelationBack.getUser()).isNull();
    }

    @Test
    void followedTest() throws Exception {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        userProfile.addFollowed(userProfileBack);
        assertThat(userProfile.getFolloweds()).containsOnly(userProfileBack);

        userProfile.removeFollowed(userProfileBack);
        assertThat(userProfile.getFolloweds()).doesNotContain(userProfileBack);

        userProfile.followeds(new HashSet<>(Set.of(userProfileBack)));
        assertThat(userProfile.getFolloweds()).containsOnly(userProfileBack);

        userProfile.setFolloweds(new HashSet<>());
        assertThat(userProfile.getFolloweds()).doesNotContain(userProfileBack);
    }

    @Test
    void blockedListTest() throws Exception {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        userProfile.addBlockedList(userProfileBack);
        assertThat(userProfile.getBlockedLists()).containsOnly(userProfileBack);

        userProfile.removeBlockedList(userProfileBack);
        assertThat(userProfile.getBlockedLists()).doesNotContain(userProfileBack);

        userProfile.blockedLists(new HashSet<>(Set.of(userProfileBack)));
        assertThat(userProfile.getBlockedLists()).containsOnly(userProfileBack);

        userProfile.setBlockedLists(new HashSet<>());
        assertThat(userProfile.getBlockedLists()).doesNotContain(userProfileBack);
    }

    @Test
    void loyaListsTest() throws Exception {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        userProfile.addLoyaLists(userProfileBack);
        assertThat(userProfile.getLoyaLists()).containsOnly(userProfileBack);

        userProfile.removeLoyaLists(userProfileBack);
        assertThat(userProfile.getLoyaLists()).doesNotContain(userProfileBack);

        userProfile.loyaLists(new HashSet<>(Set.of(userProfileBack)));
        assertThat(userProfile.getLoyaLists()).containsOnly(userProfileBack);

        userProfile.setLoyaLists(new HashSet<>());
        assertThat(userProfile.getLoyaLists()).doesNotContain(userProfileBack);
    }

    @Test
    void subscribedTest() throws Exception {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        userProfile.addSubscribed(userProfileBack);
        assertThat(userProfile.getSubscribeds()).containsOnly(userProfileBack);

        userProfile.removeSubscribed(userProfileBack);
        assertThat(userProfile.getSubscribeds()).doesNotContain(userProfileBack);

        userProfile.subscribeds(new HashSet<>(Set.of(userProfileBack)));
        assertThat(userProfile.getSubscribeds()).containsOnly(userProfileBack);

        userProfile.setSubscribeds(new HashSet<>());
        assertThat(userProfile.getSubscribeds()).doesNotContain(userProfileBack);
    }

    @Test
    void joinedEventsTest() throws Exception {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        UserEvent userEventBack = getUserEventRandomSampleGenerator();

        userProfile.addJoinedEvents(userEventBack);
        assertThat(userProfile.getJoinedEvents()).containsOnly(userEventBack);

        userProfile.removeJoinedEvents(userEventBack);
        assertThat(userProfile.getJoinedEvents()).doesNotContain(userEventBack);

        userProfile.joinedEvents(new HashSet<>(Set.of(userEventBack)));
        assertThat(userProfile.getJoinedEvents()).containsOnly(userEventBack);

        userProfile.setJoinedEvents(new HashSet<>());
        assertThat(userProfile.getJoinedEvents()).doesNotContain(userEventBack);
    }

    @Test
    void hashtagsTest() throws Exception {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        HashTag hashTagBack = getHashTagRandomSampleGenerator();

        userProfile.addHashtags(hashTagBack);
        assertThat(userProfile.getHashtags()).containsOnly(hashTagBack);

        userProfile.removeHashtags(hashTagBack);
        assertThat(userProfile.getHashtags()).doesNotContain(hashTagBack);

        userProfile.hashtags(new HashSet<>(Set.of(hashTagBack)));
        assertThat(userProfile.getHashtags()).containsOnly(hashTagBack);

        userProfile.setHashtags(new HashSet<>());
        assertThat(userProfile.getHashtags()).doesNotContain(hashTagBack);
    }

    @Test
    void followersTest() throws Exception {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        userProfile.addFollowers(userProfileBack);
        assertThat(userProfile.getFollowers()).containsOnly(userProfileBack);
        assertThat(userProfileBack.getFolloweds()).containsOnly(userProfile);

        userProfile.removeFollowers(userProfileBack);
        assertThat(userProfile.getFollowers()).doesNotContain(userProfileBack);
        assertThat(userProfileBack.getFolloweds()).doesNotContain(userProfile);

        userProfile.followers(new HashSet<>(Set.of(userProfileBack)));
        assertThat(userProfile.getFollowers()).containsOnly(userProfileBack);
        assertThat(userProfileBack.getFolloweds()).containsOnly(userProfile);

        userProfile.setFollowers(new HashSet<>());
        assertThat(userProfile.getFollowers()).doesNotContain(userProfileBack);
        assertThat(userProfileBack.getFolloweds()).doesNotContain(userProfile);
    }

    @Test
    void blockersTest() throws Exception {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        userProfile.addBlockers(userProfileBack);
        assertThat(userProfile.getBlockers()).containsOnly(userProfileBack);
        assertThat(userProfileBack.getBlockedLists()).containsOnly(userProfile);

        userProfile.removeBlockers(userProfileBack);
        assertThat(userProfile.getBlockers()).doesNotContain(userProfileBack);
        assertThat(userProfileBack.getBlockedLists()).doesNotContain(userProfile);

        userProfile.blockers(new HashSet<>(Set.of(userProfileBack)));
        assertThat(userProfile.getBlockers()).containsOnly(userProfileBack);
        assertThat(userProfileBack.getBlockedLists()).containsOnly(userProfile);

        userProfile.setBlockers(new HashSet<>());
        assertThat(userProfile.getBlockers()).doesNotContain(userProfileBack);
        assertThat(userProfileBack.getBlockedLists()).doesNotContain(userProfile);
    }

    @Test
    void awardsTest() throws Exception {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        userProfile.addAwards(userProfileBack);
        assertThat(userProfile.getAwards()).containsOnly(userProfileBack);
        assertThat(userProfileBack.getLoyaLists()).containsOnly(userProfile);

        userProfile.removeAwards(userProfileBack);
        assertThat(userProfile.getAwards()).doesNotContain(userProfileBack);
        assertThat(userProfileBack.getLoyaLists()).doesNotContain(userProfile);

        userProfile.awards(new HashSet<>(Set.of(userProfileBack)));
        assertThat(userProfile.getAwards()).containsOnly(userProfileBack);
        assertThat(userProfileBack.getLoyaLists()).containsOnly(userProfile);

        userProfile.setAwards(new HashSet<>());
        assertThat(userProfile.getAwards()).doesNotContain(userProfileBack);
        assertThat(userProfileBack.getLoyaLists()).doesNotContain(userProfile);
    }

    @Test
    void subscriptionsTest() throws Exception {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        userProfile.addSubscriptions(userProfileBack);
        assertThat(userProfile.getSubscriptions()).containsOnly(userProfileBack);
        assertThat(userProfileBack.getSubscribeds()).containsOnly(userProfile);

        userProfile.removeSubscriptions(userProfileBack);
        assertThat(userProfile.getSubscriptions()).doesNotContain(userProfileBack);
        assertThat(userProfileBack.getSubscribeds()).doesNotContain(userProfile);

        userProfile.subscriptions(new HashSet<>(Set.of(userProfileBack)));
        assertThat(userProfile.getSubscriptions()).containsOnly(userProfileBack);
        assertThat(userProfileBack.getSubscribeds()).containsOnly(userProfile);

        userProfile.setSubscriptions(new HashSet<>());
        assertThat(userProfile.getSubscriptions()).doesNotContain(userProfileBack);
        assertThat(userProfileBack.getSubscribeds()).doesNotContain(userProfile);
    }
}
