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
public class GroupEligibility extends CommonEligibility {

    private String cagiKey;
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


}
