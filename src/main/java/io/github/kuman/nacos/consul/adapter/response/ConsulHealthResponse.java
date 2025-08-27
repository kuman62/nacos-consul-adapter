package io.github.kuman.nacos.consul.adapter.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

/**
 * consul协议格式定义-Health
 * <a href="https://developer.hashicorp.com/consul/api-docs/health">health协议参考</a>
 * /v1/health/service/my-service?ns=default
 *
 * @author kuman
 * @since 1.0, 2023/03/25 18:01
 */
@Getter
@Builder
@ToString
public class ConsulHealthResponse {

    @JSONField(name = "Node")
    private Node node;

    @JSONField(name = "Service")
    private Service service;

    @JSONField(name = "Checks")
    private List<Check> checks;

    @Getter
    @Builder
    public static class Node {

        @JSONField(name = "ID")
        private String id;

        @JSONField(name = "Address")
        private String address;

        @JSONField(name = "Datacenter")
        private String dataCenter;
    }

    @Getter
    @Builder
    public static class Service {

        @JSONField(name = "ID")
        private String id;

        @JSONField(name = "Service")
        private String service;

        @JSONField(name = "Port")
        private int port;

        @JSONField(name = "Meta")
        private Map<String, String> meta;
    }

    @Getter
    @Builder
    public static class Check {

        @JSONField(name = "Node")
        private String node;

        @JSONField(name = "CheckID")
        private String checkID;

        @JSONField(name = "Name")
        private String name;

        @JSONField(name = "Status")
        private String status;
    }
}
