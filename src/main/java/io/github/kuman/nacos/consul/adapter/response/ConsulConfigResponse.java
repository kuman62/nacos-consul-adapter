package io.github.kuman.nacos.consul.adapter.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * consul协议格式定义-Agent
 * <a href="https://developer.hashicorp.com/consul/api-docs/agent">agent协议</a>
 * @author kuman
 * @since 1.0, 2023/03/25 18:01
 */
@Builder
@Getter
@ToString
public class ConsulConfigResponse {

    @JSONField(name = "Config")
    private Config config;

    @Getter
    @Builder
    public static class Config {
        @JSONField(name = "Datacenter")
        private String datacenter;
    }

}
