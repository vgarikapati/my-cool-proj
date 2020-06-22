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
public class GroupInactiveEligibilityDoc {
    private String carrierId;
    private String accountId;
    private String groupId;
    private String cagiKey;
    private String sourceSystemInstance;
    private EligibilityWithGroupDoc eligibility;
}
