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

import com.optum.micro.domain.azuresearch.AzureSearchRequest;
import com.optum.micro.domain.azuresearch.AzureSearchResponse;
import com.optum.micro.integration.service.AzureSearchIntegrationService;
@Configuration
public class AzureSearchIntegrationConfig {

    @Value("${azuresearch.si.thread.core.pool.size:10}")
    private int threadCorePoolSize;
    @Value("${azuresearch.si.thread.max.pool.size:100}")
    private int threadMaxPoolSize;
    @Value("${azuresearch.si.thread.alive.seconds:60}")
    private int threadAliveSeconds;
    @Value("${azuresearch.si.thread.queue.capacity:100}")
    private int threadQueueCapacity;

    private static final String AZURE_SEARCH_EXECUTOR_PREFIX = "rxclaim-azuresearch-si-exec-";


    private AzureSearchIntegrationService azureSearchIntegrationService;

    @Autowired
    AzureSearchIntegrationConfig(AzureSearchIntegrationService azureSearchIntegrationService)
    {
        this.azureSearchIntegrationService = azureSearchIntegrationService;
    }

    
    @Bean("azureSearchThreadFactory")
    public ThreadPoolTaskExecutor azureSearchThreadFactory() {
        ThreadPoolTaskExecutor threadFactory = new ThreadPoolTaskExecutor();
        threadFactory.setCorePoolSize(threadCorePoolSize);
        threadFactory.setMaxPoolSize(threadMaxPoolSize);
        threadFactory.setKeepAliveSeconds(threadAliveSeconds);
        threadFactory.setQueueCapacity(threadQueueCapacity);
        threadFactory.setThreadNamePrefix(AZURE_SEARCH_EXECUTOR_PREFIX);
        return threadFactory;
    }

    @Bean("azureSearchRequestChannel")
    public MessageChannel requestChannel() {
        SimpleAsyncTaskExecutor simpleAsyncTaskExecutor = new SimpleAsyncTaskExecutor(AZURE_SEARCH_EXECUTOR_PREFIX);
        simpleAsyncTaskExecutor.setThreadFactory(azureSearchThreadFactory());
        return new ExecutorChannel(simpleAsyncTaskExecutor);
    }

    @ServiceActivator(inputChannel = "azureSearchRequestChannel")
    public AzureSearchResponse azureSearchService(AzureSearchRequest azureSearchRequest) {
        return azureSearchIntegrationService.sendRequestToAzureSearch(azureSearchRequest);
    }
    
    @MessagingGateway(asyncExecutor = "azureSearchThreadFactory", defaultReplyTimeout = "${azuresearch.si.gateway.timeout.seconds:5}000")
    public interface AzureSearchServiceGateway {
        @Gateway(requestChannel = "azureSearchRequestChannel")
        AzureSearchResponse sendRequestToAzureSearch(AzureSearchRequest azureSearchRequest);
    }


}
