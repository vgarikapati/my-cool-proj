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
public class PlanResult {
    private String planCode;
    private String planEffDate;
    private String planName;
    private String planTypeCode;
    private String planQualifier;
}
