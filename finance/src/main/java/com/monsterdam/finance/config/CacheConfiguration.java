package com.monsterdam.finance.config;

import java.net.URI;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.redisson.Redisson;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.redisson.jcache.configuration.RedissonConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.support.SimpleCacheManager;

@Configuration
@EnableCaching
public class CacheConfiguration {

    // esto se comentara cuando se quiera activar redis
    private static final List<String> CACHE_NAMES = List.of(
        com.monsterdam.finance.domain.MoneyPayout.class.getName(),
        com.monsterdam.finance.domain.CreatorEarning.class.getName(),
        com.monsterdam.finance.domain.SubscriptionBundle.class.getName(),
        com.monsterdam.finance.domain.SubscriptionBundle.class.getName() + ".selledSubscriptions",
        com.monsterdam.finance.domain.WalletTransaction.class.getName(),
        com.monsterdam.finance.domain.PaymentTransaction.class.getName(),
        com.monsterdam.finance.domain.PurchasedTip.class.getName(),
        com.monsterdam.finance.domain.OfferPromotion.class.getName(),
        com.monsterdam.finance.domain.OfferPromotion.class.getName() + ".purchasedSubscriptions",
        com.monsterdam.finance.domain.PurchasedContent.class.getName(),
        com.monsterdam.finance.domain.PurchasedSubscription.class.getName()
        // jhipster-needle-redis-add-entry
    ); // esto se comentara cuando se quiera activar redis

    private GitProperties gitProperties;
    private BuildProperties buildProperties;


    /*
     * Redis-backed caching was disabled to avoid the external dependency. The application now uses
     * simple in-memory caches, so there is no need to run a Redis server for local development or tests.
     */
    /*@Bean
    public javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration(JHipsterProperties jHipsterProperties) {
        MutableConfiguration<Object, Object> jcacheConfig = new MutableConfiguration<>();

        URI redisUri = URI.create(jHipsterProperties.getCache().getRedis().getServer()[0]);

        Config config = new Config();
        if (jHipsterProperties.getCache().getRedis().isCluster()) {
            ClusterServersConfig clusterServersConfig = config
                .useClusterServers()
                .setMasterConnectionPoolSize(jHipsterProperties.getCache().getRedis().getConnectionPoolSize())
                .setMasterConnectionMinimumIdleSize(jHipsterProperties.getCache().getRedis().getConnectionMinimumIdleSize())
                .setSubscriptionConnectionPoolSize(jHipsterProperties.getCache().getRedis().getSubscriptionConnectionPoolSize())
                .addNodeAddress(jHipsterProperties.getCache().getRedis().getServer());

            if (redisUri.getUserInfo() != null) {
                clusterServersConfig.setPassword(redisUri.getUserInfo().substring(redisUri.getUserInfo().indexOf(':') + 1));
            }
        } else {
            SingleServerConfig singleServerConfig = config
                .useSingleServer()
                .setConnectionPoolSize(jHipsterProperties.getCache().getRedis().getConnectionPoolSize())
                .setConnectionMinimumIdleSize(jHipsterProperties.getCache().getRedis().getConnectionMinimumIdleSize())
                .setSubscriptionConnectionPoolSize(jHipsterProperties.getCache().getRedis().getSubscriptionConnectionPoolSize())
                .setAddress(jHipsterProperties.getCache().getRedis().getServer()[0]);

            if (redisUri.getUserInfo() != null) {
                singleServerConfig.setPassword(redisUri.getUserInfo().substring(redisUri.getUserInfo().indexOf(':') + 1));
            }
        }
        jcacheConfig.setStatisticsEnabled(true);
        jcacheConfig.setExpiryPolicyFactory(
            CreatedExpiryPolicy.factoryOf(new Duration(TimeUnit.SECONDS, jHipsterProperties.getCache().getRedis().getExpiration()))
        );
        return RedissonConfiguration.fromInstance(Redisson.create(config), jcacheConfig);
    }*/
    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(CACHE_NAMES.stream().map(ConcurrentMapCache::new).toList());
        return cacheManager;
    }
/*
    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cm) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cm);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer(javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration) {
        return cm -> {
            createCache(cm, com.monsterdam.finance.domain.MoneyPayout.class.getName(), jcacheConfiguration);
            createCache(cm, com.monsterdam.finance.domain.CreatorEarning.class.getName(), jcacheConfiguration);
            createCache(cm, com.monsterdam.finance.domain.SubscriptionBundle.class.getName(), jcacheConfiguration);
            createCache(cm, com.monsterdam.finance.domain.SubscriptionBundle.class.getName() + ".selledSubscriptions", jcacheConfiguration);
            createCache(cm, com.monsterdam.finance.domain.WalletTransaction.class.getName(), jcacheConfiguration);
            createCache(cm, com.monsterdam.finance.domain.PaymentTransaction.class.getName(), jcacheConfiguration);
            createCache(cm, com.monsterdam.finance.domain.PurchasedTip.class.getName(), jcacheConfiguration);
            createCache(cm, com.monsterdam.finance.domain.OfferPromotion.class.getName(), jcacheConfiguration);
            createCache(cm, com.monsterdam.finance.domain.OfferPromotion.class.getName() + ".purchasedSubscriptions", jcacheConfiguration);
            createCache(cm, com.monsterdam.finance.domain.PurchasedContent.class.getName(), jcacheConfiguration);
            createCache(cm, com.monsterdam.finance.domain.PurchasedSubscription.class.getName(), jcacheConfiguration);
            // jhipster-needle-redis-add-entry
        };
    }

    private void createCache(
        javax.cache.CacheManager cm,
        String cacheName,
        javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration
    ) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }*/

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
