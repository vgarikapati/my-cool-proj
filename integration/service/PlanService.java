package com.optum.micro.integration.service;

import com.optum.micro.commons.config.YAMLConfig;
import com.optum.micro.commons.exception.CustomHttpServerErrorException;
import com.optum.micro.domain.plan.PlanSearchByKeyResult;
import com.optum.micro.domain.plan.PlanSearchByKeysRequest;
import com.optum.micro.util.MemberSearchConstants;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
public class PlanService {

    private static final String PLAN_URI = "plan.url";
    private static final String FAILURE_MSG = "Failed to call Plan Service via {} : {}";

    private YAMLConfig yamlConfig =null;

    private RestTemplate restTemplate=null;

    @Autowired
    PlanService(YAMLConfig yamlConfig, @Qualifier("customRestTemplate")RestTemplate restTemplate){
        this.yamlConfig=yamlConfig;
        this.restTemplate= restTemplate;
    }

    public PlanSearchByKeyResult getPlanByKeys(PlanSearchByKeysRequest planRequest) {
        String planUri = yamlConfig.getProp().get(PLAN_URI);
        ResponseEntity<PlanSearchByKeyResult> planResultResposneEntity = null;
        PlanSearchByKeyResult planResult = null;
        HttpEntity<PlanSearchByKeysRequest> requestProductHttpEntity = new HttpEntity<>(planRequest, getHeaders());
        try {
            planResultResposneEntity = restTemplate.exchange(planUri,HttpMethod.POST, requestProductHttpEntity, PlanSearchByKeyResult.class);
            if(HttpStatus.OK==planResultResposneEntity.getStatusCode()){
                planResult = planResultResposneEntity.getBody();
            }
        } catch (HttpClientErrorException ex) {
            String msg = ex.getMessage();
            if (msg.contains(MemberSearchConstants.RECORD_NOT_FOUND))
            {
                log.info("Plan not found:" +planUri + ex.getResponseBodyAsString());
                return new PlanSearchByKeyResult();
            }
            if (msg.contains(MemberSearchConstants.BAD_REQUEST))
            {
                log.error(FAILURE_MSG, planUri, ex.getResponseBodyAsString());
                throw new CustomHttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR,MemberSearchConstants.PLAN_SERVICE_ERROR +ex.getMessage());
            }
        }  catch (Exception ex) {
            log.error(FAILURE_MSG, planUri, ex.getMessage(), ex);
            throw new CustomHttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR,MemberSearchConstants.PLAN_SERVICE_ERROR+ex.getMessage());
        }
        return planResult;
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
