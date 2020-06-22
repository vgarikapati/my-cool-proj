package com.optum.micro.domain.eligibility;

import com.optum.micro.commons.domain.fg.SearchOutputPagination;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MemberGroupEligibilityResponse {

    private Map<String, GroupEligibility> groupEligibilities;
    private Map<String, MemberEligibility> memberEligibilities;
    private SearchOutputPagination pagination;

}
