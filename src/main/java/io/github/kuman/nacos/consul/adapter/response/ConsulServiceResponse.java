package io.github.kuman.nacos.consul.adapter.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

/**
 * consul协议格式定义-Service
 * <a href="https://developer.hashicorp.com/consul/api-docs/catalog#list-services">service协议参考</a>
 * /v1/catalog/service/web?ns=default
 *
 * @author manco
 * @since 1.0, 2023/03/25 18:01
 */
@Getter
@Builder
@ToString
public class ConsulServiceResponse {

    @JsonProperty("ID")
    private String id;

    @JsonProperty("Node")
    private String node;

    @JsonProperty("Address")
    private String address;

    @JsonProperty("Datacenter")
    private String dataCenter;

    @JsonProperty("ServiceID")
    private String serviceId;

    @JsonProperty("ServiceName")
    private String serviceName;

    @JsonProperty("ServicePort")
    private Integer servicePort;

    @JsonProperty("ServiceAddress")
    private String serviceAddress;

    @JsonProperty("NodeMeta")
    private Map<String, String> nodeMeta;
}
