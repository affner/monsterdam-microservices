package com.fanflip.admin.domain;

import static com.fanflip.admin.domain.PersonalSocialLinksTestSamples.*;
import static com.fanflip.admin.domain.SocialNetworkTestSamples.*;
import static com.fanflip.admin.domain.UserProfileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.fanflip.admin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PersonalSocialLinksTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PersonalSocialLinks.class);
        PersonalSocialLinks personalSocialLinks1 = getPersonalSocialLinksSample1();
        PersonalSocialLinks personalSocialLinks2 = new PersonalSocialLinks();
        assertThat(personalSocialLinks1).isNotEqualTo(personalSocialLinks2);

        personalSocialLinks2.setId(personalSocialLinks1.getId());
        assertThat(personalSocialLinks1).isEqualTo(personalSocialLinks2);

        personalSocialLinks2 = getPersonalSocialLinksSample2();
        assertThat(personalSocialLinks1).isNotEqualTo(personalSocialLinks2);
    }

    @Test
    void socialNetworkTest() throws Exception {
        PersonalSocialLinks personalSocialLinks = getPersonalSocialLinksRandomSampleGenerator();
        SocialNetwork socialNetworkBack = getSocialNetworkRandomSampleGenerator();

        personalSocialLinks.setSocialNetwork(socialNetworkBack);
        assertThat(personalSocialLinks.getSocialNetwork()).isEqualTo(socialNetworkBack);

        personalSocialLinks.socialNetwork(null);
        assertThat(personalSocialLinks.getSocialNetwork()).isNull();
    }

    @Test
    void userTest() throws Exception {
        PersonalSocialLinks personalSocialLinks = getPersonalSocialLinksRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        personalSocialLinks.setUser(userProfileBack);
        assertThat(personalSocialLinks.getUser()).isEqualTo(userProfileBack);

        personalSocialLinks.user(null);
        assertThat(personalSocialLinks.getUser()).isNull();
    }
}
