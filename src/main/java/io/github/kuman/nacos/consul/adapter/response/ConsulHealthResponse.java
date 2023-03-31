package io.github.kuman.nacos.consul.adapter.response;

import com.fasterxml.jackson.annotation.JsonProperty;
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
 * @author manco
 * @since 1.0, 2023/03/25 18:01
 */
@Getter
@Builder
@ToString
public class ConsulHealthResponse {

    @JsonProperty("Node")
    private Node node;

    @JsonProperty("Service")
    private Service service;

    @JsonProperty("Checks")
    private List<Check> checks;

    @Getter
    @Builder
    public static class Node {

        @JsonProperty("ID")
        private String id;

        @JsonProperty("Address")
        private String address;

        @JsonProperty("Datacenter")
        private String dataCenter;
    }

    @Getter
    @Builder
    public static class Service {

        @JsonProperty("ID")
        private String id;

        @JsonProperty("Service")
        private String service;

        @JsonProperty("Port")
        private int port;

        @JsonProperty("Meta")
        private Map<String, String> meta;
    }

    @Getter
    @Builder
    public static class Check {

        @JsonProperty("Node")
        private String node;

        @JsonProperty("CheckID")
        private String checkID;

        @JsonProperty("Name")
        private String name;

        @JsonProperty("Status")
        private String status;
    }
}
