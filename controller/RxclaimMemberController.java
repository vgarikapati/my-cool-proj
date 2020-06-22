package com.optum.micro.controller;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.optum.micro.commons.constants.ExceptionConstants;
import com.optum.micro.commons.domain.fg.SearchOutputPagination;
import com.optum.micro.commons.exception.InputValidationException;
import com.optum.micro.commons.util.HEMIUtil;
import com.optum.micro.domain.CagmiKey;
import com.optum.micro.domain.MemberSearchRequest;
import com.optum.micro.domain.MemberSearchResultItem;
import com.optum.micro.domain.cag.CagFilterRequest;
import com.optum.micro.domain.cag.CagFilterResponse;
import com.optum.micro.domain.cag.CagNameRequest;
import com.optum.micro.domain.cag.CagNameResponse;
import com.optum.micro.domain.crossreferenceid.CrossReferenceIdRequest;
import com.optum.micro.domain.crossreferenceid.CrossReferenceIdResponse;
import com.optum.micro.domain.eligibility.EligibilityRequest;
import com.optum.micro.domain.eligibility.GroupEligibility;
import com.optum.micro.domain.eligibility.MemberEligibility;
import com.optum.micro.domain.eligibility.MemberEligibilityPlanResponse;
import com.optum.micro.domain.eligibility.MemberGroupEligibilityResponse;
import com.optum.micro.domain.member.MemberRequest;
import com.optum.micro.service.azuresearch.AzureSearchService;
import com.optum.micro.service.cag.CagDetailService;
import com.optum.micro.service.cag.CagFilterService;
import com.optum.micro.service.crossreferenceid.CrossReferenceIdService;
import com.optum.micro.service.eligibility.GroupEligibilityService;
import com.optum.micro.service.eligibility.GroupMemberEligibilityService;
import com.optum.micro.service.eligibility.MemberEligibilityPlanService;
import com.optum.micro.service.eligibility.MemberEligibilityService;
import com.optum.micro.service.member.MemberService;
import com.optum.micro.util.EligibilityUtil;
import com.optum.micro.validator.MemberSearchValidator;

import brave.Tracer;
import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
public class RxclaimMemberController {


    @Autowired
    private MemberSearchValidator validator;

    @Autowired
    private Tracer tracer;

    @Autowired
    private MemberEligibilityService memberEligibilityService;

    @Autowired
    private GroupEligibilityService groupEligibilityService;

    @Autowired
    private GroupMemberEligibilityService eligibilityService;

    @Autowired
    private MemberEligibilityPlanService eligibilityPlanService;

    @Autowired
    private CagFilterService cagFilterService;

    @Autowired
    private CrossReferenceIdService crossReferenceIdService;

    @Autowired
    private CagDetailService cagDetailService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private AzureSearchService azureSearchService;


    // Binds the input request with a validator
    @InitBinder("memberSearchRequest")
    protected void initMemberSearchRequestBinder(WebDataBinder binder) {
        binder.setValidator(validator);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/v1.0/rxclaim/member/searchRequestValidate", produces = {
            MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public MemberSearchResultItem validateInput(@RequestBody @Validated MemberSearchRequest memberSearchRequest, BindingResult bindingResult) {
        long startTime = System.currentTimeMillis();
        log.info("------- Enter into RxclaimMemberController at " + startTime);

        String correlationId = HEMIUtil.updateInputMetadata(memberSearchRequest,
                tracer.currentSpan().context().traceIdString());

        log.info("RxclaimMemberController Request: " + memberSearchRequest.toString());

        if (bindingResult.hasErrors()) {
            throw new InputValidationException(ExceptionConstants.BAD_REQUEST, ExceptionConstants.INVALID_REQUEST,
                    bindingResult);
        }
        MemberSearchResultItem result = new MemberSearchResultItem();
        //result.setValidInput(true);
        return result;
    }

    @PostMapping(value = "/group/eligibility/test",
            produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> getGroupEligibility(@RequestBody EligibilityRequest request) throws ExecutionException, InterruptedException {
        CompletableFuture<Map<String, GroupEligibility>> response = groupEligibilityService.searchGroupEligibilities(request);
        CompletableFuture.allOf(response).join();
        return new ResponseEntity<>(response.get(), HttpStatus.OK);
    }

    @PostMapping(value = "/member/eligibility/test",
            produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> getMemberEligibility(@RequestBody EligibilityRequest request) throws ExecutionException, InterruptedException {
        CompletableFuture<Map<String, MemberEligibility>> response = memberEligibilityService.searchMemberEligibilities(request, new SearchOutputPagination());
        CompletableFuture.allOf(response).join();
        return new ResponseEntity<>(response.get(), HttpStatus.OK);
    }

    @PostMapping(value = "/eligibility/test",
            produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> getEligibility(@RequestBody EligibilityRequest request) {
        long startTime = System.currentTimeMillis();
        // set effective date to current date if not set in request
        // for testing purpose, the effective date from caller must be set correctly
        if (StringUtils.isBlank(request.getEffectiveDate())) {
            request.setEffectiveDate(EligibilityUtil.formattedCurrentDate());
        }
        MemberGroupEligibilityResponse response = eligibilityService.searchMemberGroupEligibilities(request);
        log.info("Eligibility lookup completed in {} ms", (System.currentTimeMillis() - startTime));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.POST, value = "/member/elig-plan/test", produces = {
            MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> getMemberEligibilityPlan(@RequestBody EligibilityRequest request) {
        long startTime = System.currentTimeMillis();
        log.info("------- Enter into RxclaimMemberController-elig-plan at " + startTime);

        log.info("RxclaimMemberController-elig-plan Request: " + request.toString());

        MemberEligibilityPlanResponse eligPlanResponse = eligibilityPlanService.getMemberEligibilityPlan(request);

        log.info("RxclaimMemberController Elig-Plan lookup completed in {} ms", (System.currentTimeMillis() - startTime));
        return new ResponseEntity<>(eligPlanResponse, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/member/cag-filter/test", produces = {
            MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> getCagFilterResponse(@RequestBody CagFilterRequest request) {
        long startTime = System.currentTimeMillis();
        CagFilterResponse response = cagFilterService.filterByCagListName(request);
        log.info("RxclaimMemberController CAG Filter completed in {} ms", (System.currentTimeMillis() - startTime));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/member/crossreferenceid/test", produces = {
            MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> getCrossReferenceIdResponse(@RequestBody CrossReferenceIdRequest request) {
        long startTime = System.currentTimeMillis();
        CrossReferenceIdResponse response = crossReferenceIdService.searchNewMemberIds(request);
        log.info("RxclaimMemberController Cross Reference ID search completed in {} ms", (System.currentTimeMillis() - startTime));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/member/cagname/test", produces = {
            MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public CagNameResponse searchCagNames(@RequestBody CagNameRequest request) {
        long startTime = System.currentTimeMillis();
        CagNameResponse response = cagDetailService.getCagNames(request);
        log.info("RxclaimMemberController searchCagNames completed in {} ms", (System.currentTimeMillis() - startTime));
        return response;

    }

    @RequestMapping(method = RequestMethod.POST, value = "/member/lookup/test", produces = {
            MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> getMembers(@RequestBody MemberRequest request) {
        long startTime = System.currentTimeMillis();
        List<MemberSearchResultItem> response = memberService.searchMembers(request);
        log.info("RxclaimMemberController Member Collection lookup completed in {} ms", (System.currentTimeMillis() - startTime));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/member/azuresearch/test", produces = {
            MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> getListOfCagmiKeys(@RequestBody MemberSearchRequest request) {
        long startTime = System.currentTimeMillis();
        List<CagmiKey> response = azureSearchService.getListOfCagmiKeys(request, null);
        log.info("RxclaimMemberController Azure Search completed in {} ms", (System.currentTimeMillis() - startTime));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
