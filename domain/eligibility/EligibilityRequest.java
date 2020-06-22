package com.optum.micro.domain.eligibility;

import com.optum.micro.commons.domain.fg.SearchInputMetaData;
import com.optum.micro.commons.domain.fg.SearchInputPagination;
import com.optum.micro.domain.CagmiKey;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class EligibilityRequest {

    private String activeIndicator;
    private String effectiveDate;
    private List<CagmiKey> cagmiKeys;
    private SearchInputPagination pagination;
    private SearchInputMetaData metaData;

}
