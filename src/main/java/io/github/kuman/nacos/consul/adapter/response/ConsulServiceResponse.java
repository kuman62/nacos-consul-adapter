package io.github.kuman.nacos.consul.adapter.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

/**
 * consul协议格式定义-Service
 * <a href="https://developer.hashicorp.com/consul/api-docs/catalog#list-services">service协议参考</a>
 * /v1/catalog/service/web?ns=default
 *
 * @author kuman
 * @since 1.0, 2023/03/25 18:01
 */
@Getter
@Builder
@ToString
public class ConsulServiceResponse {

    @JSONField(name = "ID")
    private String id;

    @JSONField(name = "Node")
    private String node;

    @JSONField(name = "Address")
    private String address;

    @JSONField(name = "Datacenter")
    private String dataCenter;

    @JSONField(name = "ServiceID")
    private String serviceId;

    @JSONField(name = "ServiceName")
    private String serviceName;

    @JSONField(name = "ServicePort")
    private Integer servicePort;

    @JSONField(name = "ServiceAddress")
    private String serviceAddress;

    @JSONField(name = "NodeMeta")
    private Map<String, String> nodeMeta;
}
