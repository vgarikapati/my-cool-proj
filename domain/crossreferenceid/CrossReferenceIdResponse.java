package com.optum.micro.domain.crossreferenceid;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.Set;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CrossReferenceIdResponse {
    private Set<CrossReferenceMember> newMembers;
}
