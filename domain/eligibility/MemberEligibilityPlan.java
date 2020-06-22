package com.optum.micro.domain.eligibility;

import com.optum.micro.domain.plan.Plan;
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
public class MemberEligibilityPlan {

    private String activeInd;
    private String cagmiKey;
    private MemberEligibility memberEligibility;
    private GroupEligibility groupEligibility;
    private Plan memberEligibilityPlan;
    private Plan groupEligibilityPlan;
    private Plan plan;
}
