package com.fanflip.catalogs;

import com.fanflip.catalogs.config.AsyncSyncConfiguration;
import com.fanflip.catalogs.config.EmbeddedElasticsearch;
import com.fanflip.catalogs.config.EmbeddedKafka;
import com.fanflip.catalogs.config.EmbeddedRedis;
import com.fanflip.catalogs.config.EmbeddedSQL;
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
@SpringBootTest(classes = { CatalogsApp.class, AsyncSyncConfiguration.class })
@EmbeddedRedis
@EmbeddedElasticsearch
@EmbeddedSQL
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@EmbeddedKafka
public @interface IntegrationTest {
}
