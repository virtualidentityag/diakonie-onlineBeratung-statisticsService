package de.caritas.cob.statisticsservice.config.cache;

import net.sf.ehcache.config.CacheConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class SessionCacheManagerConfig {

  public static final String SESSION_CACHE = "sessionCache";

  @Value("${cache.session.configuration.maxEntriesLocalHeap}")
  private long agenciesMaxEntriesLocalHeap;

  @Value("${cache.session.configuration.eternal}")
  private boolean agenciesEternal;

  @Value("${cache.session.configuration.timeToIdleSeconds}")
  private long agenciesTimeToIdleSeconds;

  @Value("${cache.session.configuration.timeToLiveSeconds}")
  private long agenciesTimeToLiveSeconds;

  @Bean
  public CacheManager cacheManager() {
    return new EhCacheCacheManager(ehCacheManager());
  }

  @Bean(destroyMethod = "shutdown")
  public net.sf.ehcache.CacheManager ehCacheManager() {
    var config = new net.sf.ehcache.config.Configuration();
    config.addCache(buildSessionCacheConfiguration());
    return net.sf.ehcache.CacheManager.newInstance(config);
  }

  private CacheConfiguration buildSessionCacheConfiguration() {
    var agencyCacheConfiguration = new CacheConfiguration();
    agencyCacheConfiguration.setName(SESSION_CACHE);
    agencyCacheConfiguration.setMaxEntriesLocalHeap(agenciesMaxEntriesLocalHeap);
    agencyCacheConfiguration.setEternal(agenciesEternal);
    agencyCacheConfiguration.setTimeToIdleSeconds(agenciesTimeToIdleSeconds);
    agencyCacheConfiguration.setTimeToLiveSeconds(agenciesTimeToLiveSeconds);
    return agencyCacheConfiguration;
  }
}
