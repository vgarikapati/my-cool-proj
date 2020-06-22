package com.optum.micro.controller;

import brave.Tracer;
import com.optum.micro.commons.util.HEMIUtil;
import com.optum.micro.domain.MemberSearchRequest;
import com.optum.micro.domain.MemberSearchResponse;
import com.optum.micro.service.MemberSearchService;
import com.optum.micro.validator.MemberSearchValidator;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@Log4j2
public class MemberSearchController {

	private Tracer tracer;

	private MemberSearchValidator validator;

	private MemberSearchService memberSearchService;

	@Autowired
	MemberSearchController(Tracer tracer,
						   MemberSearchValidator validator,
						   MemberSearchService memberSearchService)
	{
		this.tracer = tracer;
		this.validator = validator;
		this.memberSearchService = memberSearchService;
	}

	// Binds the input request with a validator
	@InitBinder("memberSearchRequest")
	public void initMemberSearchRequestBinder(WebDataBinder binder) {
		binder.setValidator(validator);
	}

	@PostMapping(value = "/v1.0/rxclaim/member/search", produces = {
			MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<MemberSearchResponse> searchMember(@RequestBody @Validated MemberSearchRequest memberSearchRequest, BindingResult bindingResult, @RequestHeader("Authorization") String varRawJWToken) {
		long startTime = System.currentTimeMillis();
		log.info("------- Enter into MemberSearchController at {}" , startTime);
		String transId = HEMIUtil.updateInputMetadataTransId(memberSearchRequest,
				tracer.currentSpan().context().traceIdString());//???
		log.info("MemberSearchController Request: {} " , memberSearchRequest.toString());
		MemberSearchResponse response = memberSearchService.searchMember(memberSearchRequest,bindingResult, varRawJWToken, transId);
		HEMIUtil.updateOutputMetadata(response, transId);
		log.info("MemberSearchController Response: {} " , response);
		log.info("MemberSearchController Member Search completed in {} ms", (System.currentTimeMillis() - startTime));
		return new ResponseEntity<>(response,HttpStatus.OK);

	}

}
