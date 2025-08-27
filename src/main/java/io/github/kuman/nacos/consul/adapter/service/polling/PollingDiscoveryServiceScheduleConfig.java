package io.github.kuman.nacos.consul.adapter.service.polling;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * spring-boot 多线程  @Scheduled注解 并发定时任务的解决方案
 *
 * @author kuman
 * @since 1.0, 2023/03/23 20:03
 */
@Slf4j
@Configuration
@EnableScheduling
@ConditionalOnBean(PollingDiscoveryServiceImpl.class)
public class PollingDiscoveryServiceScheduleConfig implements SchedulingConfigurer {

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(taskExecutor());
    }

    public static final String EXECUTOR_SERVICE = "scheduledExecutor";

    @Bean(EXECUTOR_SERVICE)
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskScheduler executor = new ThreadPoolTaskScheduler();
        // 设置线程数:返回可用处理器的Java虚拟机的数量。
        executor.setPoolSize(Runtime.getRuntime().availableProcessors() * 2);
        // 设置默认线程名称
        executor.setThreadNamePrefix("scheduled2-");
        // 设置拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 等待所有任务结束后再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);
        // 是否将取消后的任务，从队列中删除
        executor.setRemoveOnCancelPolicy(true);

        executor.initialize();
        log.debug("自定义@Scheduled注解线程池配置完成");
        return executor;
    }
}
