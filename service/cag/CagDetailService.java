package com.optum.micro.service.cag;

import com.optum.micro.domain.cag.CagNameRequest;
import com.optum.micro.domain.cag.CagNameResponse;

public interface CagDetailService {

    public CagNameResponse getCagNames(CagNameRequest request);
}
