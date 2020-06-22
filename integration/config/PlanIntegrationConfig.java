package com.optum.micro.integration.config;

import com.optum.micro.domain.plan.PlanSearchByKeyResult;
import com.optum.micro.domain.plan.PlanSearchByKeysRequest;
import com.optum.micro.integration.service.PlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.ExecutorChannel;
import org.springframework.messaging.MessageChannel;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.CompletableFuture;

@Configuration
public class PlanIntegrationConfig {

    @Value("${plan.si.thread.core.pool.size:10}")
    private int threadCorePoolSize;
    @Value("${plan.si.thread.max.pool.size:100}")
    private int threadMaxPoolSize;
    @Value("${plan.si.thread.alive.seconds:60}")
    private int threadAliveSeconds;
    @Value("${plan.si.thread.queue.capacity:100}")
    private int threadQueueCapacity;

    private static final String PLAN_EXECUTOR_PREFIX = "rxclaim-plan-si-exec-";


    private PlanService planService;

    @Autowired
    PlanIntegrationConfig(PlanService planService)
    {
        this.planService = planService;
    }

    public ThreadPoolTaskExecutor planThreadFactory() {
        ThreadPoolTaskExecutor threadFactory = new ThreadPoolTaskExecutor();
        threadFactory.setCorePoolSize(threadCorePoolSize);
        threadFactory.setMaxPoolSize(threadMaxPoolSize);
        threadFactory.setKeepAliveSeconds(threadAliveSeconds);
        threadFactory.setQueueCapacity(threadQueueCapacity);
        threadFactory.setThreadNamePrefix(PLAN_EXECUTOR_PREFIX);
        return threadFactory;
    }

    @Bean("planRequestChannel")
    public MessageChannel requestChannel() {
        SimpleAsyncTaskExecutor simpleAsyncTaskExecutor = new SimpleAsyncTaskExecutor(PLAN_EXECUTOR_PREFIX);
        simpleAsyncTaskExecutor.setThreadFactory(planThreadFactory());
        return new ExecutorChannel(simpleAsyncTaskExecutor);
    }

    @ServiceActivator(inputChannel = "planRequestChannel")
    public PlanSearchByKeyResult planService(PlanSearchByKeysRequest request) {
        return planService.getPlanByKeys(request);
    }

    @MessagingGateway(defaultReplyTimeout = "${plan.si.gateway.timeout.seconds:2}000")
    public interface PlanServiceGateway {
        @Gateway(requestChannel = "planRequestChannel")
        PlanSearchByKeyResult getPlanByKeys(PlanSearchByKeysRequest request);
    }


}
