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
public class MemberInactiveEligibilityDoc {

    private String carrierId;
    private String accountId;
    private String groupId;
    private String memberId;
    private String sourceSystemInstance;
    private String cagmiKey;
    private EligibilityWithMemberDoc eligibility;

}
