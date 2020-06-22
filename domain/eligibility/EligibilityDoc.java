package com.optum.micro.domain.eligibility;

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
public class EligibilityDoc {

    private Integer eligibilitySequenceNumber;
    private String eligibilityStatus;
    private String eligibilityStartDate;
    private String eligibilityEndDate;
    private Double copayBrand;
    private Double copayGeneric;
    private Double copay3;
    private Double copay4;
    private String planCode;
    private String planEffectiveDate;

}
