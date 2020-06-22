package com.optum.micro.integration.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.optum.micro.commons.config.YAMLConfig;
import com.optum.micro.commons.exception.CustomHttpServerErrorException;
import com.optum.micro.domain.das.DataAccessValidationSearchRequest;
import com.optum.micro.domain.das.DataAccessValidationSearchResponse;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class DataAccessService {

    private static final String DAS_URI = "dataaccessservice.url";
    private static final String FAILURE_MSG = "Failed to call Data Access Service via {} : {}";


    @Autowired
    private YAMLConfig yamlConfig;

    @Autowired
    private RestTemplate restTemplate;

    public DataAccessValidationSearchResponse getDataAccessValidationSearch(DataAccessValidationSearchRequest searchRequest) {
        String dataAccessServiceUri = yamlConfig.getProp().get(DAS_URI);
        ResponseEntity<DataAccessValidationSearchResponse> searchResponseEntity = null;
        DataAccessValidationSearchResponse searchResponse = null;
        HttpEntity<DataAccessValidationSearchRequest> requestProductHttpEntity = new HttpEntity<>(searchRequest, getHeaders());
        try {
        	searchResponseEntity = restTemplate.exchange(dataAccessServiceUri,HttpMethod.POST, requestProductHttpEntity, DataAccessValidationSearchResponse.class);
            if(HttpStatus.OK.equals(searchResponseEntity.getStatusCode())){
            	searchResponse = searchResponseEntity.getBody();
            }
        } catch (HttpClientErrorException ex) {
            String msg = ex.getMessage();
            if (msg.contains("404"))
            {
                log.info("Data Access service not found:" +dataAccessServiceUri + ex.getResponseBodyAsString());
                return new DataAccessValidationSearchResponse();
            }
            if (msg.contains("400"))
            {
                log.error(FAILURE_MSG, dataAccessServiceUri, ex.getResponseBodyAsString());
                throw new CustomHttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR,"Data Access service error:"+ex.getMessage());
            }
        }  catch (Exception ex) {
            log.error(FAILURE_MSG, dataAccessServiceUri, ex.getMessage(), ex);
            throw new CustomHttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR,"Data Access service error:"+ex.getMessage());
        }
        return searchResponse;
    }
    
    private MultiValueMap<String, String> getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        List<MediaType> mediaTypeList = new ArrayList<MediaType>();
        mediaTypeList.add(MediaType.APPLICATION_JSON);
        headers.setAccept(mediaTypeList);
        return headers;
    }


}
