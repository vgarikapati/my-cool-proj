package com.optum.micro.service.crossreferenceid;

import com.optum.micro.domain.crossreferenceid.CrossReferenceIdRequest;
import com.optum.micro.domain.crossreferenceid.CrossReferenceIdResponse;

public interface CrossReferenceIdService {
    CrossReferenceIdResponse searchNewMemberIds(CrossReferenceIdRequest request);
}
