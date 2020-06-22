package com.optum.micro.domain.crossreferenceid;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.AccessLevel;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor
public class CrossReferenceMember {
    private String sourceSystemInstance;
    private String newMemberId;
}
