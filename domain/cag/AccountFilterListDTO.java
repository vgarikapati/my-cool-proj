package com.optum.micro.domain.cag;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper =true)
@AllArgsConstructor
@NoArgsConstructor
public class AccountFilterListDTO extends CagFilterListDTO{

	private String carrierId;
	private String accountId;
}
