package io.github.kuman.nacos.consul.adapter.service.direct;

import io.github.kuman.nacos.consul.adapter.service.DiscoveryService;
import io.github.kuman.nacos.consul.adapter.common.ConsulAdapterConstant;
import io.github.kuman.nacos.consul.adapter.dao.DiscoveryServiceDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

/**
 * 直接请求注册中心模式
 *
 * @author manco
 * @since 1.0, 2023/03/23 20:03
 */
@Slf4j
@Component
@ConditionalOnProperty(name= ConsulAdapterConstant.NACOS_MODE,havingValue = ConsulAdapterConstant.DIRECT_MODE)
public class DirectDiscoveryServiceImpl implements DiscoveryService {
    @Resource
    private DiscoveryServiceDao discoveryServiceDao;

    /**
     * 获取所有服务名称
     *
     * @return 返回所有服务名称
     */
    @Override
    public Map<String, List<String>> getServiceNames() {
        Set<String> services = discoveryServiceDao.getServiceIdList();
        Map<String, List<String>> result = new HashMap<>(services.size());
        for (String item : services) {
            result.put(item, Collections.emptyList());
        }
        return result;
    }

    /**
     * 获取指定服务所有实例（consul新版本协议）
     *
     * @param serviceId 服务名
     * @return 返回指定服务所有实例
     */
    @Override
    public List<ServiceInstance> getServiceInstancesHealth(String serviceId) {
        return discoveryServiceDao.getInstances(serviceId);
    }

    /**
     * 获取指定服务所有实例（consul老版本协议）
     * @param serviceId 服务名
     * @return 返回指定服务所有实例
     */
    @Override
    public List<ServiceInstance> getServiceInstances(String serviceId) {
        return discoveryServiceDao.getInstances(serviceId);
    }

}