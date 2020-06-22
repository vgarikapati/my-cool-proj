package com.optum.micro.service.azuresearch;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.optum.micro.config.AzureSearchConfig;
import com.optum.micro.domain.azuresearch.AzureSearchRequest;
import com.optum.micro.domain.azuresearch.AzureSearchResponse;
@Component
@Log4j2
public class AzureSearchServiceHelper {

	private AzureSearchConfig azureSearchConfig;

	private  RestTemplate restTemplate;

	@Autowired
	AzureSearchServiceHelper (AzureSearchConfig azureSearchConfig,
							  RestTemplate restTemplate)
	{
		this.azureSearchConfig = azureSearchConfig;
		this.restTemplate = restTemplate;
	}

	public String buildUrl() {
		String url = String.format("https://%s.search.windows.net/indexes/%s/docs/search?api-version=%s",
				azureSearchConfig.getServiceName(), azureSearchConfig.getIndexName(), azureSearchConfig.getApiVersion());
		return url;
	}

	public AzureSearchResponse send(AzureSearchRequest azureSearchRequest) {
		AzureSearchResponse azureSearchResponse = null;
		restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
		HttpEntity<AzureSearchRequest> httpRequestEntity = new HttpEntity<AzureSearchRequest>(azureSearchRequest,
				getHeaders());
		try {
			azureSearchResponse = restTemplate.postForObject(buildUrl(), httpRequestEntity, AzureSearchResponse.class);
		} catch (Exception ex) {
			log.error("Exception occure in azure search call:",ex);
		}
		return azureSearchResponse;
	}

	public MultiValueMap<String, String> getHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
		headers.add("api-key", azureSearchConfig.getApiKey());
		List<MediaType> mediaTypeList = new ArrayList<MediaType>();
		mediaTypeList.add(MediaType.APPLICATION_JSON);
		headers.setAccept(mediaTypeList);
		return headers;
	}
}
