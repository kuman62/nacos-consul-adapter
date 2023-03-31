package io.github.kuman.nacos.consul.adapter.controller;

import io.github.kuman.nacos.consul.adapter.response.ConsulServiceResponse;
import io.github.kuman.nacos.consul.adapter.common.ConsulAdapterCommon;
import io.github.kuman.nacos.consul.adapter.service.DiscoveryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * consul service 接口封装
 *
 * @author manco
 * @since 1.0, 2023/03/24 08:49
 */
@Slf4j
@RestController
public class ConsulServiceController {

    @Resource
    private DiscoveryService discoveryService;

    /**
     * 获取所有服务名称
     * @return 所有服务名称
     */
    @GetMapping(value = "/v1/catalog/services", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, List<String>>> getServiceNames() {
        Map<String, List<String>> response = discoveryService.getServiceNames();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 获取指定服务实例列表
     * @param serviceId 执行服务名称
     * @return 指定服务实例列表
     */
    @GetMapping(value = "/v1/catalog/service/{serviceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ConsulServiceResponse>> getService(@PathVariable("serviceId") String serviceId) {
        Assert.isTrue(StringUtils.hasLength(serviceId), "服务名不可为空");
        log.debug("请求服务名为[{}]的实例列表", serviceId);
        return new ResponseEntity<>(ConsulAdapterCommon.getServiceInstances(serviceId, discoveryService.getServiceInstances(serviceId)), HttpStatus.OK);
    }
}