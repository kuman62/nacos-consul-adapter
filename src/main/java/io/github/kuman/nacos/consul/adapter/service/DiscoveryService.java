package io.github.kuman.nacos.consul.adapter.service;


import io.github.kuman.nacos.consul.adapter.response.ConsulResponse;
import org.springframework.cloud.client.ServiceInstance;

import java.util.List;
import java.util.Map;

/**
 * 返回服务和上次更改的服务列表
 *
 * @author kuman
 * @since 1.0, 2023/03/23 20:03
 */
public interface DiscoveryService {

    /**
     * 获取所有服务名称
     *
     * @return 返回所有服务名称
     */
    ConsulResponse<Map<String, List<String>>> getServiceNames(Long index);

    /**
     * 获取指定服务所有实例（consul新版本协议）
     *
     * @param serviceId 服务名
     * @return 返回指定服务所有实例
     */
    ConsulResponse<List<ServiceInstance>> getServiceInstancesHealth(String serviceId, Long index);

    /**
     * 获取指定服务所有实例（consul老版本协议）
     * @param serviceId 服务名
     * @return 返回指定服务所有实例
     */
    ConsulResponse<List<ServiceInstance>> getServiceInstances(String serviceId, Long index);

}