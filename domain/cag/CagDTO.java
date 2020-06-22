package com.optum.micro.domain.cag;

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
public class CagDTO {


    private String cagiKey;
    private String carrierName;
    private String accountName;
    private String groupName;
}
