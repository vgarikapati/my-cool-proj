package com.optum.micro.config;

import com.optum.micro.util.MemberSearchConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class ThreadPoolTaskExecutorConfig {

    @Value("${eligibility.thread.pool.core.size:200}")
    private int eligibilityThreadPoolCoreSize;
    @Value("${eligibility.thread.pool.max.size:500}")
    private int eligibilityThreadPoolMaxSize;

    @Bean("eligibilityThreadPoolTaskExecutor")
    public TaskExecutor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(eligibilityThreadPoolCoreSize);
        executor.setMaxPoolSize(eligibilityThreadPoolMaxSize);
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setThreadNamePrefix(MemberSearchConstants.ELIGIBILITY_EXECUTOR);
        return executor;
    }
}
