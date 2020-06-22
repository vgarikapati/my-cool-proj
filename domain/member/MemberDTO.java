package com.optum.micro.domain.member;

import com.optum.micro.domain.Contact;
import com.optum.micro.domain.Demographics;
import com.optum.micro.domain.HicIdentifier;
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
public class MemberDTO {
    private String cagmiKey;
    private String sourceSystemInstance;
    private String memberId;
    private String carrierId;
    private String accountId;
    private String groupId;
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
    private Contact contact;
    private Demographics demographics;
    private HicIdentifier hicIdentifier;
}
