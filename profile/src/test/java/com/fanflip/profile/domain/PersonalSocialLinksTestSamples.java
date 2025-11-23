package com.fanflip.profile.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PersonalSocialLinksTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static PersonalSocialLinks getPersonalSocialLinksSample1() {
        return new PersonalSocialLinks()
            .id(1L)
            .normalImageS3Key("normalImageS3Key1")
            .thumbnailIconS3Key("thumbnailIconS3Key1")
            .socialLink("socialLink1")
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1")
            .socialNetworkId(1L);
    }

    public static PersonalSocialLinks getPersonalSocialLinksSample2() {
        return new PersonalSocialLinks()
            .id(2L)
            .normalImageS3Key("normalImageS3Key2")
            .thumbnailIconS3Key("thumbnailIconS3Key2")
            .socialLink("socialLink2")
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2")
            .socialNetworkId(2L);
    }

    public static PersonalSocialLinks getPersonalSocialLinksRandomSampleGenerator() {
        return new PersonalSocialLinks()
            .id(longCount.incrementAndGet())
            .normalImageS3Key(UUID.randomUUID().toString())
            .thumbnailIconS3Key(UUID.randomUUID().toString())
            .socialLink(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString())
            .socialNetworkId(longCount.incrementAndGet());
    }
}
