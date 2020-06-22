package com.optum.micro.domain;

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
public class MemberEligibilityResult {
    private Integer eligibilitySequenceNumber;
    private String eligibilityStatus;
    private String eligibilityStartDate;
    private String eligibilityEndDate;
    private Double copayBrand;
    private Double copayGeneric;
    private Double copay3;
    private Double copay4;
    private String clientProductCode;
    private String clientRiderCode;
    private Double spendDownAmount;
    private PlanResult plan;
}
