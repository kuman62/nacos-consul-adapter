# Nacos Consul Adapter(for Prometheus)
Prometheus官方提供Consul为注册中心的配置方式，配置后可自动获取Consul中所有实例的信息并进行监控。  如果想使用Prometheus监控使用其他注册中心的服务就需要一些额外的适配，适配的目的是让项目提供Consul服务器相同接口。  
本项目是Nacos服务注册中心适配器，供Prometheus获取注册中心的所有实例信息。
项目主要为了获取端点信息，因management.endpoints.web.base-path和server.servlet.context-path可能设置不一致，导致需要开启management.server.port参数配置
management.server.port与server.port可能不一致，当不一致时，本工程优先使用management.server.port配置

## 特性
项目一共只有四个接口:  
1. /v1/agent/self：返回数据中心的名称。这个接口暂时返回固定内容，未提供配置。
2. /v1/catalog/services：返回注册中心所有服务名称。
3. /v1/health/service/{serviceId}：返回指定名称服务的所有实例。
4. /v1/catalog/service/{serviceId}：返回指定名称服务的所有实例，兼容早期的Prometheus版本。

## 配置选项
`nacos.consul.adapter.mode`：使用模式。`direct`：直接查询，`polling`：轮询。默认为轮询。  
`nacos.consul.adapter.mode.polling.cron`:在轮询模式下，请求服务名称的间隔时间。默认为：5秒

## 快速开始  
在Prometheus的配置文件prometheus.yml中添加如下配置:
```
  - job_name: 'nacos-consul-adapter'
    scrape_interval: 30s
    static_configs:
    consul_sd_configs:
      - server: '127.0.0.1:8083' #nacos-consul-adapter实例的ip+端口
    relabel_configs:
      - source_labels: ['__metrics_path__']
        regex:         '/metrics'
        target_label:  __metrics_path__
        replacement:   '/actuator/prometheus'
```
