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
public class GroupEligibilityDoc {

    private String carrierId;
    private String accountId;
    private String groupId;
    private String cagiKey;
    private String sourceSystemInstance;
    private List<EligibilityWithGroupDoc> eligibility;
}
