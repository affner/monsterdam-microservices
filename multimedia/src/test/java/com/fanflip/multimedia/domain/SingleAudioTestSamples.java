package com.fanflip.multimedia.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SingleAudioTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static SingleAudio getSingleAudioSample1() {
        return new SingleAudio()
            .id(1L)
            .thumbnailS3Key("thumbnailS3Key1")
            .contentS3Key("contentS3Key1")
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1");
    }

    public static SingleAudio getSingleAudioSample2() {
        return new SingleAudio()
            .id(2L)
            .thumbnailS3Key("thumbnailS3Key2")
            .contentS3Key("contentS3Key2")
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2");
    }

    public static SingleAudio getSingleAudioRandomSampleGenerator() {
        return new SingleAudio()
            .id(longCount.incrementAndGet())
            .thumbnailS3Key(UUID.randomUUID().toString())
            .contentS3Key(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
