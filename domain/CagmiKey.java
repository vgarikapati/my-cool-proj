package com.optum.micro.domain;

import com.optum.micro.util.EligibilityUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CagmiKey {

    private String carrierId;
    private String accountId;
    private String groupId;
    private String memberId;
    private String sourceSystemInstance;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private String cagmiKey;
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private String cagiKey;

    public String getCagiKey() {
        if (cagiKey==null)
        {
            cagiKey = EligibilityUtil.buildCagiKey(this);
        }
        return cagiKey;
    }

    public String getCagmiKey()
    {
        if (cagmiKey == null)
        {
            cagmiKey = EligibilityUtil.buildCagmiKey(this);
        }
        return cagmiKey;
    }

}
