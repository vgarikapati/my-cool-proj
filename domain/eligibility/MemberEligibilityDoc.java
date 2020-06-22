package com.optum.micro.domain.eligibility;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MemberEligibilityDoc {

    private String carrierId;
    private String accountId;
    private String groupId;
    private String memberId;
    private String sourceSystemInstance;
    private String cagmiKey;
    private List<EligibilityWithMemberDoc> eligibility;

}
