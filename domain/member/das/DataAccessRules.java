package com.optum.micro.domain.member.das;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataAccessRules {
	private DefaultAccess defaultAccess;
	private DataProtectionAccess dataProtectionAccess;
	private List<DetailedDataAccess> detailedDataAccess;

}
