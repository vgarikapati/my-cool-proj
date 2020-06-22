package com.optum.micro.repositories;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.azure.data.cosmos.ConnectionPolicy;
import com.microsoft.azure.spring.data.cosmosdb.config.CosmosDBConfig;
import com.optum.micro.commons.config.CosmosDbProperties;
import com.optum.micro.commons.cosmosdb.config.CosmosDBRepositoryConfig;
import com.optum.micro.commons.exception.BaseException;

import lombok.extern.log4j.Log4j2;

@Configuration
@EnableConfigurationProperties(CosmosDbProperties.class)
@Log4j2
public class MemberDBRepositoryConfig extends CosmosDBRepositoryConfig {

    @Bean
    @Override
    public CosmosDBConfig getConfig(CosmosDbProperties properties) {
        return super.getConfig(properties);
    }

    @Override
    public ConnectionPolicy getConnectionPolicy() {
        try {
            return super.getConnectionPolicy();
        } catch (Exception e) {
            log.error("get DBConnectionPolicy for RxClaim Member Service error:",e);
            throw new BaseException(e.getMessage());
        }

    }
}
