package com.optum.micro.domain.azuresearch;

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
public class AzureSearchObject {

	private String sourceSystemInstance;
	private String memberId;
	private String carrierId;
	private String groupId;
	private String accountId;
	private String cagmiKey;
	private String firstName;
	private String lastName;
	private String familyId;
	private String dateOfBirth;
	private String zip;
}