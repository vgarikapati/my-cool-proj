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
public class GroupEligibilityResult {
    private Integer eligibilitySequenceNumber;
    private String eligibilityStatus;
    private String eligibilityStartDate;
    private String eligibilityEndDate;
    private Double copayBrand;
    private Double copayGeneric;
    private Double copay3;
    private Double copay4;
    private Double copay5;
    private Double copay6;
    private Double copay7;
    private Double copay8;
    private String benefitCode;
    private String createUser;
    private String createDate;
    private String createTime;
    private String updateUser;
    private String updateDate;
    private String updateTime;
    private PlanResult plan;
}
