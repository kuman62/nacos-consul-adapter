package io.github.kuman.nacos.consul.adapter.service;


import org.springframework.cloud.client.ServiceInstance;

import java.util.List;
import java.util.Map;

/**
 * 返回服务和上次更改的服务列表
 *
 * @author manco
 * @since 1.0, 2023/03/23 20:03
 */
public interface DiscoveryService {

    /**
     * 获取所有服务名称
     *
     * @return 返回所有服务名称
     */
    Map<String, List<String>> getServiceNames();

    /**
     * 获取指定服务所有实例（consul新版本协议）
     *
     * @param serviceId 服务名
     * @return 返回指定服务所有实例
     */
    List<ServiceInstance> getServiceInstancesHealth(String serviceId);

    /**
     * 获取指定服务所有实例（consul老版本协议）
     * @param serviceId 服务名
     * @return 返回指定服务所有实例
     */
    List<ServiceInstance> getServiceInstances(String serviceId);

}