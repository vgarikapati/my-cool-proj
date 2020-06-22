package com.optum.micro.domain.eligibility;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class EligibilityWithMemberDoc extends EligibilityDoc {
    private String clientProductCode;
    private String clientRiderCode;
    private Double spendDownAmount;
}
