package com.optum.micro.domain.cag;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CagFilterListDTO {
    private String listNameIdInsKey;
    private String sourceSystemInstance;
}
