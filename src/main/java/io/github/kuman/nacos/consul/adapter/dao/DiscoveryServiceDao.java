package io.github.kuman.nacos.consul.adapter.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 服务发现任务
 *
 * @author kuman
 * @since 1.0, 2023/03/23 20:03
 */
@Slf4j
@Component
public class DiscoveryServiceDao {
    @Resource
    private DiscoveryClient discoveryClient;

    /**
     * 获取服务注册中心服务名列表
     * @return 服务名称列表
     */
    public Set<String> getServiceIdList() {
        Set<String> serviceSet = new HashSet<>(16);
        try {
            List<String> serviceList = discoveryClient.getServices();
            if(serviceList == null) {
                serviceList = Collections.emptyList();
            }
            serviceSet.addAll(serviceList);
        } catch (Exception e) {
            log.error("从服务注册中心获取服务列表失败:", e);
        }
        return serviceSet;
    }

    /**
     * 获取服务实例列表
     * 报文样例：
     * {
     * 	"host": "127.0.0.1",
     * 	"metadata": {
     * 		"nacos.instanceId": "127.0.0.1#8085#DEFAULT#DEFAULT_GROUP@@dubbo-provider3",
     * 		"nacos.weight": "1.0",
     * 		"nacos.cluster": "DEFAULT",
     * 		"nacos.ephemeral": "true",
     * 		"nacos.healthy": "true",
     * 		"preserved.register.source": "SPRING_CLOUD"
     *        },
     * 	"port": 8085,
     * 	"secure": false,
     * 	"serviceId": "dubbo-provider3",
     * 	"uri": "http://127.0.0.1:8085"
     * }
     * @param serviceId 服务名称
     * @return 服务实例列表
     */
    public List<ServiceInstance> getInstances(String serviceId) {
        try {
            List<ServiceInstance> serviceInstanceList = discoveryClient.getInstances(serviceId);
            if(serviceInstanceList == null) {
                serviceInstanceList = Collections.emptyList();
            }
            return serviceInstanceList;
        } catch (Exception e) {
            log.error("从服务注册中心获取服务实例列表失败:", e);
        }
        return Collections.emptyList();
    }
}
