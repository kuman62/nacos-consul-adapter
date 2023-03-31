package io.github.kuman.nacos.consul.adapter.common;

import io.github.kuman.nacos.consul.adapter.response.ConsulHealthResponse;
import io.github.kuman.nacos.consul.adapter.response.ConsulServiceResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 协议格式转换
 *
 * @author manco
 * @since 1.0, 2023/03/30 20:00
 */
@Slf4j
public class ConsulAdapterCommon {

    private ConsulAdapterCommon() {}
    /**
     * 自定义标签
     */
    private static final String NACOS_APPLICATION_NAME = "nacos_application_name";

    /**
     * nacos协议转consul协议
     *
     * @param serviceId 服务名称
     * @param serviceInstanceList 实例列表
     * @return consul协议
     */
    public static List<ConsulHealthResponse> getServiceInstancesHealth(String serviceId, List<ServiceInstance> serviceInstanceList) {
        List<ConsulHealthResponse> consulHealthResponseList = new ArrayList<>(serviceInstanceList.size());
        for (ServiceInstance item : serviceInstanceList) {
            Map<String, String> metadataMap = item.getMetadata();
            metadataMap.put(NACOS_APPLICATION_NAME, serviceId);
            String instanceId = metadataMap.get("nacos.instanceId");
            if(!StringUtils.hasLength(instanceId)) {
                instanceId = item.getHost() + "#" + item.getPort();
            }
            ConsulHealthResponse.Node node = ConsulHealthResponse.Node.builder()
                    .id(instanceId)
                    .address(item.getHost())
                    .dataCenter(ConsulAdapterConstant.DATA_CENTER)
                    .build();

            ConsulHealthResponse.Service service = ConsulHealthResponse.Service.builder()
                    .service(serviceId)
                    .id(serviceId + "-" + item.getPort())
                    .port(item.getPort())
                    .meta(metadataMap)
                    .build();

            ConsulHealthResponse.Check check = ConsulHealthResponse.Check.builder()
                    .node(item.getServiceId())
                    .checkID(item.getHost() + ":" + item.getPort())
                    .name(item.getServiceId())
                    .status("UP")
                    .build();

            consulHealthResponseList.add(ConsulHealthResponse.builder().node(node).service(service).checks(Collections.singletonList(check)).build());
        }
        return consulHealthResponseList;
    }

    /**
     * nacos协议转consul协议
     *
     * @param serviceId 服务名称
     * @param serviceInstanceList 实例列表
     * @return consul协议
     */
    public static List<ConsulServiceResponse> getServiceInstances(String serviceId, List<ServiceInstance> serviceInstanceList) {
        List<ConsulServiceResponse> consulServiceResponseList = new ArrayList<>(serviceInstanceList.size());
        for (ServiceInstance item : serviceInstanceList) {
            Map<String, String> metadataMap = item.getMetadata();
            metadataMap.put(NACOS_APPLICATION_NAME, serviceId);
            //nacos.instanceId : "10.20.16.44#8085#DEFAULT#DEFAULT_GROUP@@dubbo-provider3",
            String instanceId = metadataMap.get("nacos.instanceId");
            if(!StringUtils.hasLength(instanceId)) {
                instanceId = item.getHost() + "#" + item.getPort();
            }
            ConsulServiceResponse response = ConsulServiceResponse.builder()
                    .id(instanceId)
                    .node("nacos")
                    .address(item.getHost())
                    .dataCenter(ConsulAdapterConstant.DATA_CENTER)
                    .nodeMeta(metadataMap)
                    .serviceId(serviceId + "-" + item.getPort())
                    .serviceName(serviceId)
                    .serviceAddress(item.getHost())
                    .servicePort(item.getPort()).build();

            consulServiceResponseList.add(response);
        }
        return consulServiceResponseList;
    }
}
