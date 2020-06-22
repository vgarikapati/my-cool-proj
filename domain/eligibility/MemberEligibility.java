package com.optum.micro.domain.eligibility;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class MemberEligibility extends CommonEligibility implements Comparable{

    private String memberId;
    private String cagmiKey;
    private String clientProductCode;
    private String clientRiderCode;
    private Double spendDownAmount;

    @Override
    public int compareTo(Object o) {
        if (o instanceof MemberEligibility) {
            return this.cagmiKey.compareTo(((MemberEligibility) o).getCagmiKey());
        } else {
            throw new IllegalArgumentException("Only MemberEligibility object could be compared");
        }
    }
}
