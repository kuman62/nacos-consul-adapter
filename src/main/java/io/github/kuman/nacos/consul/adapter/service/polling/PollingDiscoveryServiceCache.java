package io.github.kuman.nacos.consul.adapter.service.polling;

import com.alibaba.fastjson.JSON;
import io.github.kuman.nacos.consul.adapter.common.ConsulAdapterConstant;
import io.github.kuman.nacos.consul.adapter.dao.DiscoveryServiceDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.mutable.MutableBoolean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

/**
 * 服务发现任务
 *
 * @author kuman
 * @since 1.0, 2023/03/23 20:03
 */
@Slf4j
@Component
@ConditionalOnBean(PollingDiscoveryServiceImpl.class)
public class PollingDiscoveryServiceCache {
    @Resource
    private DiscoveryServiceDao discoveryServiceDao;

    private static final String NACOS_SERVICES = "services";

    private static final String NACOS_INSTANCES = "instances";

    @Cacheable(cacheManager= ConsulAdapterConstant.CACHE_MANAGER_DEFAULT, cacheNames = NACOS_SERVICES, key = "'nacosServices'")
    public Set<String> getServices() {
        Set<String> services = discoveryServiceDao.getServiceIdList();
        if(log.isDebugEnabled()) {
            log.debug("从注册中心获取,并更新本地缓存,服务明细:{}", JSON.toJSONString(services));
        }
        return services;
    }

    @Cacheable(cacheManager= ConsulAdapterConstant.CACHE_MANAGER_DEFAULT, cacheNames = NACOS_SERVICES, key = "'nacosServices'")
    public Set<String> getServicesFlagChannel(MutableBoolean isCache) {
        isCache.setValue(false);
        return discoveryServiceDao.getServiceIdList();
    }

    @CachePut(cacheManager= ConsulAdapterConstant.CACHE_MANAGER_DEFAULT, cacheNames = NACOS_SERVICES, key = "'nacosServices'")
    public Set<String> putServices(Set<String> serviceList) {
        if(log.isDebugEnabled()) {
            log.debug("强制更新本地缓存,服务明细:{}", JSON.toJSONString(serviceList));
        }
        return serviceList;
    }


    @Cacheable(cacheManager= ConsulAdapterConstant.CACHE_MANAGER_DEFAULT, cacheNames = NACOS_INSTANCES, key = "#serviceId")
    public List<ServiceInstance> getServiceInstances(String serviceId) {
        log.debug("从注册中心获取服务{}的实例信息,并更新本地缓存", serviceId);
        return discoveryServiceDao.getInstances(serviceId);
    }

    @CacheEvict(cacheManager= ConsulAdapterConstant.CACHE_MANAGER_DEFAULT, cacheNames = NACOS_INSTANCES, key = "#serviceId")
    public void deleteServiceInstances(String serviceId) {
        log.debug("从本地缓存删除服务[{}]的实例信息", serviceId);
    }
}
