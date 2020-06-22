package com.optum.micro.integration.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.optum.micro.domain.azuresearch.AzureSearchRequest;
import com.optum.micro.domain.azuresearch.AzureSearchResponse;
import com.optum.micro.service.azuresearch.AzureSearchServiceHelper;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class AzureSearchIntegrationService {

	private static final String FAILURE_MSG = "Failed to call Azure Search Service via {} : {}";

	private RestTemplate restTemplate;

	private AzureSearchServiceHelper azureSearchServiceHelper;

	@Autowired
	AzureSearchIntegrationService(@Qualifier("customRestTemplate") RestTemplate restTemplate, AzureSearchServiceHelper azureSearchServiceHelper) {
		this.restTemplate = restTemplate;
		this.azureSearchServiceHelper = azureSearchServiceHelper;
	}

	public AzureSearchResponse sendRequestToAzureSearch(AzureSearchRequest azureSearchRequest) {

		String uri = azureSearchServiceHelper.buildUrl();
		AzureSearchResponse azureSearchResponse = null;
		HttpEntity<AzureSearchRequest> httpRequestEntity = new HttpEntity<AzureSearchRequest>(azureSearchRequest,
				azureSearchServiceHelper.getHeaders());
		try {
			azureSearchResponse = restTemplate.postForObject(uri, httpRequestEntity, AzureSearchResponse.class);
		} catch (HttpClientErrorException ex) {
			log.error(FAILURE_MSG, uri, ex);
		} catch (RestClientException ex) {
			log.error(FAILURE_MSG, uri, ex);
		} catch (Exception ex) {
			log.error(FAILURE_MSG, uri, ex);
		}
		return azureSearchResponse;
	}
}
