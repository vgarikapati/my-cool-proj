package com.optum.micro.integration.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.ExecutorChannel;
import org.springframework.messaging.MessageChannel;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.optum.micro.domain.das.DataAccessValidationSearchRequest;
import com.optum.micro.domain.das.DataAccessValidationSearchResponse;
import com.optum.micro.integration.service.DataAccessService;

@Configuration
public class DataAccessServiceConfig {

    @Value("${plan.si.thread.core.pool.size:10}")
    private int threadCorePoolSize;
    @Value("${plan.si.thread.max.pool.size:100}")
    private int threadMaxPoolSize;
    @Value("${plan.si.thread.alive.seconds:60}")
    private int threadAliveSeconds;
    @Value("${plan.si.thread.queue.capacity:100}")
    private int threadQueueCapacity;

    private static final String DAS_EXECUTOR_PREFIX = "rxclaim-das-si-exec-";

    @Autowired
    private DataAccessService dataAccessService;

    public ThreadPoolTaskExecutor dasThreadFactory() {
        ThreadPoolTaskExecutor threadFactory = new ThreadPoolTaskExecutor();
        threadFactory.setCorePoolSize(threadCorePoolSize);
        threadFactory.setMaxPoolSize(threadMaxPoolSize);
        threadFactory.setKeepAliveSeconds(threadAliveSeconds);
        threadFactory.setQueueCapacity(threadQueueCapacity);
        threadFactory.setThreadNamePrefix(DAS_EXECUTOR_PREFIX);
        return threadFactory;
    }

    @Bean("dasRequestChannel")
    public MessageChannel requestChannel() {
        SimpleAsyncTaskExecutor simpleAsyncTaskExecutor = new SimpleAsyncTaskExecutor(DAS_EXECUTOR_PREFIX);
        simpleAsyncTaskExecutor.setThreadFactory(dasThreadFactory());
        return new ExecutorChannel(simpleAsyncTaskExecutor);
    }

    @ServiceActivator(inputChannel = "dasRequestChannel")
    public DataAccessValidationSearchResponse dataAccessService(DataAccessValidationSearchRequest request) {
        return dataAccessService.getDataAccessValidationSearch(request);
    }

    @MessagingGateway(defaultReplyTimeout = "${das.si.gateway.timeout.seconds:5}000")
    public interface DataAccessServiceGateway {
        @Gateway(requestChannel = "dasRequestChannel")
        DataAccessValidationSearchResponse getDataAccessValidationSearch(DataAccessValidationSearchRequest request);
    }


}
