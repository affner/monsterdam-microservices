package com.fanflip.admin.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class UserProfileTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static UserProfile getUserProfileSample1() {
        return new UserProfile()
            .id(1L)
            .emailContact("emailContact1")
            .profilePhotoS3Key("profilePhotoS3Key1")
            .coverPhotoS3Key("coverPhotoS3Key1")
            .mainContentUrl("mainContentUrl1")
            .mobilePhone("mobilePhone1")
            .websiteUrl("websiteUrl1")
            .amazonWishlistUrl("amazonWishlistUrl1")
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1");
    }

    public static UserProfile getUserProfileSample2() {
        return new UserProfile()
            .id(2L)
            .emailContact("emailContact2")
            .profilePhotoS3Key("profilePhotoS3Key2")
            .coverPhotoS3Key("coverPhotoS3Key2")
            .mainContentUrl("mainContentUrl2")
            .mobilePhone("mobilePhone2")
            .websiteUrl("websiteUrl2")
            .amazonWishlistUrl("amazonWishlistUrl2")
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2");
    }

    public static UserProfile getUserProfileRandomSampleGenerator() {
        return new UserProfile()
            .id(longCount.incrementAndGet())
            .emailContact(UUID.randomUUID().toString())
            .profilePhotoS3Key(UUID.randomUUID().toString())
            .coverPhotoS3Key(UUID.randomUUID().toString())
            .mainContentUrl(UUID.randomUUID().toString())
            .mobilePhone(UUID.randomUUID().toString())
            .websiteUrl(UUID.randomUUID().toString())
            .amazonWishlistUrl(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
