package com.optum.micro.service.cag;

import com.optum.micro.domain.cag.CagFilterRequest;
import com.optum.micro.domain.cag.CagFilterResponse;

public interface CagFilterService {
    CagFilterResponse filterByCagListName(CagFilterRequest cagFilterRequest);
}
