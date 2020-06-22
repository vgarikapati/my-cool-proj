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
public class CagNameResponse {
    private Map<String,CagName> cagNameMap; //cagiKey -> CagName
}
