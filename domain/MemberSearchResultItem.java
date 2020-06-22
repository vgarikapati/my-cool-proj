package com.optum.micro.domain;

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
public class MemberSearchResultItem {

    private String sourceSystemInstance;
    private String memberId;
    private String carrierId;
    private String carrierName;
    private String accountId;
    private String accountName;
    private String groupId;
    private String groupName;
    @ToString.Exclude
    private String firstName;
    @ToString.Exclude
    private String lastName;
    @ToString.Exclude
    private String middleName;
    private String familyType;
    private String familyIndicator;
    private String familyId;
    private String personCode;
    private String relationshipCode;
    private String benefitResetDate;
    private String languageCode;
    private String durKey;
    private String durProcessFlag;
    private Integer multipleBirthCode;
    private String originalEffectiveDate;
    private String memberType;
    private String altInsuranceMbrId;
    private String altInsuranceFlag;
    private String listReferenceIndicator;
    private String activeIndicator;
    private Contact contact;
    private Demographics demographics;
    private HicIdentifier hicIdentifier;
    private PlanResult plan;
    private List<MemberEligibilityResult> memberEligibility;
    private List<GroupEligibilityResult> groupEligibility;

}
