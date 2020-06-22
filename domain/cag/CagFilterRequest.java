package com.optum.micro.domain.cag;

import com.optum.micro.domain.CagmiKey;
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
public class CagFilterRequest {
    private List<CagmiKey> cagmiKeys;
    private String cagList;
    private String cagListType; // C, A or G
    private String incExcListIdentifier; // I, E or R
}
