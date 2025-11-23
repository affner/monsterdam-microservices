package com.fanflip.notifications;

import com.fanflip.notifications.config.AsyncSyncConfiguration;
import com.fanflip.notifications.config.EmbeddedElasticsearch;
import com.fanflip.notifications.config.EmbeddedKafka;
import com.fanflip.notifications.config.EmbeddedRedis;
import com.fanflip.notifications.config.EmbeddedSQL;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = { NotificationsApp.class, AsyncSyncConfiguration.class })
@EmbeddedRedis
@EmbeddedElasticsearch
@EmbeddedSQL
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@EmbeddedKafka
public @interface IntegrationTest {
}
