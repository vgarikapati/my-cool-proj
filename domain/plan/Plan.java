package com.optum.micro.domain.plan;

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
public class Plan {

    private String sourceSystemInstance;
    private String planCode;
    private String planEffDate;
    private String planCodeEffDateInsKey;
    private String planTermDate;
    private String planVerifyMemberInd;
    private String planName;
    private String planQualifier;
    private String planTypeCode;
}
