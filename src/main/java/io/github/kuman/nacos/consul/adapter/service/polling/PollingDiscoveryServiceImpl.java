package io.github.kuman.nacos.consul.adapter.service.polling;

import io.github.kuman.nacos.consul.adapter.response.ConsulResponse;
import io.github.kuman.nacos.consul.adapter.service.DiscoveryService;
import io.github.kuman.nacos.consul.adapter.common.ConsulAdapterConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

/**
 * 长轮询模式
 *
 * @author kuman
 * @since 1.0, 2023/03/23 20:05
 */
@Slf4j
@Component
@ConditionalOnProperty(name= ConsulAdapterConstant.NACOS_MODE,havingValue = ConsulAdapterConstant.LONG_POLLING_MODE, matchIfMissing = true)
public class PollingDiscoveryServiceImpl implements DiscoveryService {
    @Resource
    private PollingDiscoveryServiceCache pollingDiscoveryServiceCache;

    /**
     * 获取所有服务名称
     *
     * @return 返回所有服务名称
     */
    @Override
    public ConsulResponse<Map<String, List<String>>> getServiceNames(Long index) {
        Set<String> services = pollingDiscoveryServiceCache.getServices();
        Map<String, List<String>> result = new HashMap<>(services.size());
        for (String item : services) {
            result.put(item, Collections.emptyList());
        }
        return new ConsulResponse<>(result, System.currentTimeMillis());
    }

    /**
     * 获取指定服务所有实例（consul新版本协议）
     *
     * @param serviceId 服务名
     * @return 返回指定服务所有实例
     */
    @Override
    public ConsulResponse<List<ServiceInstance>> getServiceInstancesHealth(String serviceId, Long index) {
        return new ConsulResponse<>(pollingDiscoveryServiceCache.getServiceInstances(serviceId), System.currentTimeMillis());
    }

    /**
     * 获取指定服务所有实例（consul老版本协议）
     * @param serviceId 服务名
     * @return 返回指定服务所有实例
     */
    @Override
    public ConsulResponse<List<ServiceInstance>> getServiceInstances(String serviceId, Long index) {
        return new ConsulResponse<>(pollingDiscoveryServiceCache.getServiceInstances(serviceId), System.currentTimeMillis());
    }

}
