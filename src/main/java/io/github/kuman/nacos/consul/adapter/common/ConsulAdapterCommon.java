package io.github.kuman.nacos.consul.adapter.common;

import io.github.kuman.nacos.consul.adapter.response.ConsulHealthResponse;
import io.github.kuman.nacos.consul.adapter.response.ConsulServiceResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * 协议格式转换
 *
 * @author kuman
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

            String servicePort = metadataMap.get("management.port");
            if(!StringUtils.hasLength(servicePort)) {
                servicePort = String.valueOf(item.getPort());
            }

            String instanceId = metadataMap.get("nacos.instanceId");
            if(!StringUtils.hasLength(instanceId)) {
                instanceId = item.getHost() + "#" + servicePort;
            } else {
                if(!servicePort.equals(String.valueOf(item.getPort()))) {
                    instanceId = instanceId.replace(String.valueOf(item.getPort()), servicePort);
                }
            }

            ConsulHealthResponse.Node node = ConsulHealthResponse.Node.builder()
                    .id(instanceId)
                    .address(item.getHost())
                    .dataCenter(ConsulAdapterConstant.DATA_CENTER)
                    .build();

            ConsulHealthResponse.Service service = ConsulHealthResponse.Service.builder()
                    .service(serviceId)
                    .id(serviceId + "-" + servicePort)
                    .port(Integer.parseInt(servicePort))
                    .meta(metadataMap)
                    .build();

            ConsulHealthResponse.Check check = ConsulHealthResponse.Check.builder()
                    .node(item.getServiceId())
                    .checkID(item.getHost() + ":" + servicePort)
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

            String servicePort = metadataMap.get("management.port");
            if(!StringUtils.hasLength(servicePort)) {
                servicePort = String.valueOf(item.getPort());
            }

            //nacos.instanceId : "127.0.0.1#8085#DEFAULT#DEFAULT_GROUP@@dubbo-provider3",
            String instanceId = metadataMap.get("nacos.instanceId");
            if(!StringUtils.hasLength(instanceId)) {
                instanceId = item.getHost() + "#" + servicePort;
            } else {
                if(!servicePort.equals(String.valueOf(item.getPort()))) {
                    instanceId = instanceId.replace(String.valueOf(item.getPort()), servicePort);
                }
            }

            ConsulServiceResponse response = ConsulServiceResponse.builder()
                    .id(instanceId)
                    .node("nacos")
                    .address(item.getHost())
                    .dataCenter(ConsulAdapterConstant.DATA_CENTER)
                    .nodeMeta(metadataMap)
                    .serviceId(serviceId + "-" + servicePort)
                    .serviceName(serviceId)
                    .serviceAddress(item.getHost())
                    .servicePort(Integer.parseInt(servicePort)).build();

            consulServiceResponseList.add(response);
        }
        return consulServiceResponseList;
    }


    /**
     * 创建带头的响应报文
     * @param response 响应报文
     * @param index 编号
     * @return 响应报文
     */
    public static <T> ResponseEntity<T> createResponseEntity(T response, Long index) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(ConsulAdapterConstant.CONSUL_IDX_HEADER, "" + (Objects.isNull(index) ? 0 : index ));
        return new ResponseEntity<>(response, httpHeaders, HttpStatus.OK);
    }
}
