package com.optum.micro.domain.member.das;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataProtectionAccess {

	private boolean phiDataAccessAllowed;
	private boolean piiDataAccessAllowed;
	private boolean detokenizeData;
}
