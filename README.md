# Nacos Consul Adapter(for Prometheus)
Prometheus官方提供Consul为注册中心的配置方式，配置后可自动获取Consul中所有实例的信息并进行监控。  如果想使用Prometheus监控使用其他注册中心的服务就需要一些额外的适配，适配的目的是让项目提供Consul服务器相同接口。  
本项目是Nacos服务注册中心适配器，供Prometheus获取注册中心的所有实例信息。  

## 特性
项目一共只有四个接口:  
1. /v1/agent/self：返回数据中心的名称。这个接口暂时返回固定内容，未提供配置。
2. /v1/catalog/services：返回注册中心所有服务名称。
3. /v1/health/service/{serviceId}：返回指定名称服务的所有实例。
4. /v1/catalog/service/{serviceId}：返回指定名称服务的所有实例，兼容早期的Prometheus版本。
