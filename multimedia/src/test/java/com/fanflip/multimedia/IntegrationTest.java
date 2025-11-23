package com.fanflip.multimedia;

import com.fanflip.multimedia.config.AsyncSyncConfiguration;
import com.fanflip.multimedia.config.EmbeddedElasticsearch;
import com.fanflip.multimedia.config.EmbeddedKafka;
import com.fanflip.multimedia.config.EmbeddedRedis;
import com.fanflip.multimedia.config.EmbeddedSQL;
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
@SpringBootTest(classes = { MultimediaApp.class, AsyncSyncConfiguration.class })
@EmbeddedRedis
@EmbeddedElasticsearch
@EmbeddedSQL
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@EmbeddedKafka
public @interface IntegrationTest {
}
