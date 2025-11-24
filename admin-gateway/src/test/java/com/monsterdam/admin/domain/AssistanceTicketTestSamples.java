package com.monsterdam.admin.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AssistanceTicketTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AssistanceTicket getAssistanceTicketSample1() {
        return new AssistanceTicket()
            .id(1L)
            .subject("subject1")
            .description("description1")
            .comments("comments1")
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1")
            .userId(1L);
    }

    public static AssistanceTicket getAssistanceTicketSample2() {
        return new AssistanceTicket()
            .id(2L)
            .subject("subject2")
            .description("description2")
            .comments("comments2")
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2")
            .userId(2L);
    }

    public static AssistanceTicket getAssistanceTicketRandomSampleGenerator() {
        return new AssistanceTicket()
            .id(longCount.incrementAndGet())
            .subject(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .comments(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString())
            .userId(longCount.incrementAndGet());
    }
}
