package io.github.kuman.nacos.consul.adapter.service.polling;

import io.github.kuman.nacos.consul.adapter.common.ConsulAdapterConstant;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * 缓存管理器
 *
 * @author kuman
 * @since 1.0, 2023/03/30 10:39
 */
@Slf4j
@Configuration
@ConditionalOnBean(PollingDiscoveryServiceImpl.class)
public class PollingDiscoveryServiceCacheConfig {

    /**
     * 默认缓存管理器
     * @return 缓存管理器
     */
    @Primary
    @Bean(ConsulAdapterConstant.CACHE_MANAGER_DEFAULT)
    public CacheManager cacheManagerDefault() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder()
                // 初始的缓存空间大小
                .initialCapacity(300)
                .removalListener(((key, value, cause) -> log.debug("缓存[{}]key:{} 被移除, 原因:{}.", ConsulAdapterConstant.CACHE_MANAGER_DEFAULT, key, cause.name())))
                // 缓存的最大条数
                .maximumSize(5000));
        //允许缓存空值（默认为true）
        cacheManager.setAllowNullValues(true);
        return cacheManager;
    }
}
