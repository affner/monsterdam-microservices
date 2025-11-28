package com.monsterdam.interactions;

import com.monsterdam.interactions.config.AsyncSyncConfiguration;
import com.monsterdam.interactions.config.EmbeddedElasticsearch;
import com.monsterdam.interactions.config.EmbeddedKafka;
import com.monsterdam.interactions.config.EmbeddedRedis;
import com.monsterdam.interactions.config.EmbeddedSQL;
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
@SpringBootTest(classes = { InteractionsApp.class, AsyncSyncConfiguration.class })
@EmbeddedRedis
@EmbeddedElasticsearch
@EmbeddedSQL
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@EmbeddedKafka
public @interface IntegrationTest {
}
