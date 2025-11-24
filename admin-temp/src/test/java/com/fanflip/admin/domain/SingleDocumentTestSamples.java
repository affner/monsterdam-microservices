package com.monsterdam.admin.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SingleDocumentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static SingleDocument getSingleDocumentSample1() {
        return new SingleDocument()
            .id(1L)
            .title("title1")
            .description("description1")
            .documentFileS3Key("documentFileS3Key1")
            .documentType("documentType1")
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1");
    }

    public static SingleDocument getSingleDocumentSample2() {
        return new SingleDocument()
            .id(2L)
            .title("title2")
            .description("description2")
            .documentFileS3Key("documentFileS3Key2")
            .documentType("documentType2")
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2");
    }

    public static SingleDocument getSingleDocumentRandomSampleGenerator() {
        return new SingleDocument()
            .id(longCount.incrementAndGet())
            .title(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .documentFileS3Key(UUID.randomUUID().toString())
            .documentType(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
