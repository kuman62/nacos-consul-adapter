package io.github.kuman.nacos.consul.adapter.service.polling;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.NacosServiceManager;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.listener.EventListener;
import com.alibaba.nacos.client.naming.NacosNamingService;
import io.github.kuman.nacos.consul.adapter.dao.DiscoveryServiceDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.mutable.MutableBoolean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * 服务发现任务
 *
 * @author kuman
 * @since 1.0, 2023/03/23 20:03
 */
@Slf4j
@Component
@ConditionalOnBean(PollingDiscoveryServiceImpl.class)
public class PollingDiscoveryServiceTask {
    @Resource
    private PollingDiscoveryServiceCache pollingDiscoveryServiceCache;
    @Resource
    private DiscoveryServiceDao discoveryServiceDao;
    @Resource
    private NacosDiscoveryProperties nacosDiscoveryProperties;
    @Resource
    private NacosServiceManager nacosServiceManager;
    /**
     * 第一次注册事件
     */
    private static final AtomicBoolean SUBSCRIBE = new AtomicBoolean(false);
    /**
     * 定时任务
     */
    @Scheduled(cron = "${nacos.consul.adapter.mode.polling.cron:0/5 * * * * ?}")
    public void run() {
        try {
            Set<String> listenerList = new HashSet<>(16);
            MutableBoolean isCache = new MutableBoolean(true);
            Set<String> cacheServiceNameList = pollingDiscoveryServiceCache.getServicesFlagChannel(isCache);
            if(isCache.isTrue() && SUBSCRIBE.get()) {
                Set<String> serviceNameList = discoveryServiceDao.getServiceIdList();
                boolean isNotChange = cacheServiceNameList.containsAll(serviceNameList) && serviceNameList.containsAll(cacheServiceNameList);
                log.debug("从服务注册中心获取到服务数量为:{},本地缓存记录数量为:{},明细{}", serviceNameList.size(), cacheServiceNameList.size(),isNotChange ? "没有变化,无需更新本地缓存" : "有变化,需要更新本地缓存");
                if (!isNotChange) {
                    pollingDiscoveryServiceCache.putServices(new HashSet<>(serviceNameList));
                }
                listenerList.addAll(serviceNameList.stream().filter(e -> !cacheServiceNameList.contains(e)).collect(Collectors.toSet()));
            } else {
                log.debug("从服务注册中心获取到服务数量为:{}",cacheServiceNameList.size());
                listenerList.addAll(cacheServiceNameList);
            }
            if(!listenerList.isEmpty()) {
                subscribe(listenerList);
                SUBSCRIBE.compareAndSet(false, true);
            }
        } catch (Exception e) {
            log.error("定时任务执行出错", e);
        }
    }



    /**
     * 增加服务监听事件
     * @param serviceIdList 服务名称列表
     */
    private void subscribe(Set<String> serviceIdList) {
        serviceIdList.forEach(serviceName -> subscribe(serviceName, new PollingDiscoveryServiceListener(pollingDiscoveryServiceCache)));
    }

    /**
     * 增加服务监听事件
     * @param serviceId 服务名称
     * @param listener 事件监听
     */
    private void subscribe(String serviceId, EventListener listener) {
        try {
            NacosNamingService nacosNamingService = (NacosNamingService) nacosServiceManager.getNamingService(nacosDiscoveryProperties.getNacosProperties());
            nacosNamingService.subscribe(serviceId, nacosDiscoveryProperties.getGroup(), listener);
            log.debug("新增服务实例监听:[{}],组:[{}]", serviceId, nacosDiscoveryProperties.getGroup());
        } catch (NacosException e) {
            log.debug("新增服务实例监听:[{}],组:[{}],失败:{}", serviceId, nacosDiscoveryProperties.getGroup(), e.getErrMsg());
        }
    }
}
