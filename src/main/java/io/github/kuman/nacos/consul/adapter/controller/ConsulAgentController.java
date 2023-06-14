package io.github.kuman.nacos.consul.adapter.controller;

import com.alibaba.fastjson.JSON;
import io.github.kuman.nacos.consul.adapter.response.ConsulConfigResponse;
import io.github.kuman.nacos.consul.adapter.common.ConsulAdapterConstant;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * consul agent 接口封装
 *
 * @author kuman
 * @since 1.0, 2023/03/24 08:49
 */
@RestController
public class ConsulAgentController {

    @GetMapping(value = "/v1/agent/self", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getNodes() {
        ConsulConfigResponse.Config config = ConsulConfigResponse.Config.builder().datacenter(ConsulAdapterConstant.DATA_CENTER).build();
        return JSON.toJSONString(ConsulConfigResponse.builder().config(config).build());
    }
}