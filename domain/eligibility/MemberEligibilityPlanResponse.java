package com.optum.micro.domain.eligibility;

import java.util.Map;

import com.optum.micro.commons.domain.fg.SearchOutputPagination;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MemberEligibilityPlanResponse {
    private Map<String,MemberEligibilityPlan> memberEligibilityPlanMap;
    private SearchOutputPagination pagination;
}
