package com.optum.micro.config;


import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableScheduling
public class RestTemplateConfig {


    private HttpComponentsClientHttpRequestFactory clientHttpRequestFactoryTSLV12;

    private HttpComponentsClientHttpRequestFactory clientHttpRequestFactoryDisableCert;

    @Autowired
    RestTemplateConfig(@Qualifier("TSLV12")CloseableHttpClient httpClientTSLV12,
                       @Qualifier("DisableCert") CloseableHttpClient httpClientDisableCert ){
        this.clientHttpRequestFactoryTSLV12= new HttpComponentsClientHttpRequestFactory(httpClientTSLV12);
        this.clientHttpRequestFactoryDisableCert=new HttpComponentsClientHttpRequestFactory(httpClientDisableCert);
    }

    @Bean(name = "customRestTemplate")
    public RestTemplate customRestTemplate() {
        return new RestTemplate(clientHttpRequestFactoryTSLV12);
    }

    @Bean(name = "disableCertRestTemplate")
    public RestTemplate disableCertRestTemplate(){

        return  new RestTemplate(clientHttpRequestFactoryDisableCert);

    }


    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setThreadNamePrefix("poolScheduler");
        scheduler.setPoolSize(100);
        return scheduler;
    }

}
