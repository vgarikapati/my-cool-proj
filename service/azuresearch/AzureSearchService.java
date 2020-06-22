package com.optum.micro.service.azuresearch;

import java.util.List;

import com.optum.micro.domain.CagmiKey;
import com.optum.micro.domain.MemberSearchRequest;
import com.optum.micro.domain.crossreferenceid.CrossReferenceIdResponse;

public interface AzureSearchService {
	
	 List<CagmiKey> getListOfCagmiKeys(MemberSearchRequest memberSearchRequest, CrossReferenceIdResponse crossReferenceIdResponse);
}
