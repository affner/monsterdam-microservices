package com.monsterdam.admin.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class IdentityDocumentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static IdentityDocument getIdentityDocumentSample1() {
        return new IdentityDocument()
            .id(1L)
            .documentName("documentName1")
            .documentDescription("documentDescription1")
            .fileDocumentS3Key("fileDocumentS3Key1")
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1");
    }

    public static IdentityDocument getIdentityDocumentSample2() {
        return new IdentityDocument()
            .id(2L)
            .documentName("documentName2")
            .documentDescription("documentDescription2")
            .fileDocumentS3Key("fileDocumentS3Key2")
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2");
    }

    public static IdentityDocument getIdentityDocumentRandomSampleGenerator() {
        return new IdentityDocument()
            .id(longCount.incrementAndGet())
            .documentName(UUID.randomUUID().toString())
            .documentDescription(UUID.randomUUID().toString())
            .fileDocumentS3Key(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
