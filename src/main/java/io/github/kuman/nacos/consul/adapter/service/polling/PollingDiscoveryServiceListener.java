package io.github.kuman.nacos.consul.adapter.service.polling;

import com.alibaba.nacos.api.naming.listener.Event;
import com.alibaba.nacos.api.naming.listener.EventListener;
import com.alibaba.nacos.api.naming.listener.NamingEvent;
import com.alibaba.nacos.api.naming.utils.NamingUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;


/**
 * 服务变化监听
 *
 * @author kuman
 * @since 1.0, 2023/03/30 17:32
 */
@Slf4j
public class PollingDiscoveryServiceListener implements EventListener {

    private final PollingDiscoveryServiceCache pollingDiscoveryServiceCache;

    public PollingDiscoveryServiceListener(PollingDiscoveryServiceCache pollingDiscoveryServiceCache) {
        this.pollingDiscoveryServiceCache = pollingDiscoveryServiceCache;
    }

    /**
     * 监听服务的变更
     * @param event 事件
     */
    @Override
    public void onEvent(Event event) {
        if (event instanceof NamingEvent) {
            NamingEvent namingEvent = (NamingEvent) event;
            String serviceName = NamingUtils.getServiceName(namingEvent.getServiceName());
            log.debug("接收服务[{}]到变动事件,服务名[{}]", namingEvent.getServiceName(), serviceName);
            if(StringUtils.hasLength(serviceName)) {
                pollingDiscoveryServiceCache.deleteServiceInstances(serviceName);
            }
        }
    }
}
