package com.optum.micro.domain;


import com.optum.micro.commons.domain.fg.OptumRxDataPaginationRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import com.optum.micro.commons.validation.ValidDate;

@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class MemberSearchRequest extends OptumRxDataPaginationRequest {

    private String sourceSystemInstance;
    @Size(max = 9, message ="INVALID_LENGTH_9")
    private String carrierId;
    @Size(max = 15, message ="INVALID_LENGTH_15")
    private String accountId;
    @Size(max = 15, message ="INVALID_LENGTH_15")
    private String groupId;
    @Size(max = 10, message ="INVALID_LENGTH_10")
    private String cagList;
    @Pattern(regexp = "C|A|G|\\s*", message = "INVALID_CAG_TYPE")
    private String cagListType;
    @Pattern(regexp = "I|E|R|\\s*", message = "INVALID_EXCLISTID_IND")
    private String incExcListIdentifier;
    private String id;
    @Pattern(regexp = "M|F|S|X|\\s*", message = "INVALID_ID_TYPE")
    private String idType;
    @Pattern(regexp = "E|S|\\s*", message = "INVALID_SEARCH_OPER")
    private String idSearchOperator;
    @Pattern(regexp = "C|A|G|\\s*", message = "INVALID_CAG_TYPE")
    private String familyIdScope;
    @ValidDate (message = "INVALID_DATE_FORMAT")
    private String dateOfBirth;
    @Pattern(regexp = "M|F|\\s*", message = "INVALID_GENDER")
    private String gender;
    @Size(max = 15, message ="INVALID_LENGTH_15")
    @ToString.Exclude
    private String firstName;
    @Pattern(regexp = "E|S|\\s*", message = "INVALID_SEARCH_OPER")
    private String firstNameSearchOperator;
    @Size(max = 25, message ="INVALID_LENGTH_25")
    @ToString.Exclude
    private String lastName;
    @Pattern(regexp = "E|S|\\s*", message = "INVALID_SEARCH_OPER")
    private String lastNameSearchOperator;
    @ValidDate (message = "INVALID_DATE_FORMAT")
    private String memberEffectiveDate;
    @Pattern(regexp = "A|\\s*", message = "INVALID_ACTIVE_IND")
    private String activeIndicator;
    private Boolean includeCAGNames;
    @Valid
    private Contact contact;
    
    private boolean dasConfigDisabled;
    

}
