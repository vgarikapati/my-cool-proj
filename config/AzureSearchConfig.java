package com.optum.micro.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Configuration
@AllArgsConstructor
@NoArgsConstructor
public class AzureSearchConfig {
	
	@Value("${azure.search.servicename}")
	private String serviceName;
	@Value("${azure.search.apikey}")
	private String apiKey;
	@Value("${azure.search.indexname}")
	private String indexName;
	@Value("${azure.search.apiversion}")
	private String apiVersion;
	
}