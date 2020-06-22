package com.optum.micro.domain.cag;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CagFilterResponse {
    private Map<String, String> cagmiKeys; // cagmiKey -> listReferenceIndicator
}
