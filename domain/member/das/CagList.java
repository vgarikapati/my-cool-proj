package com.optum.micro.domain.member.das;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CagList {

	private String cagListName;
	private String cagListType;
	private String cagListIncludeExclude;
	private List<CagListValue> cagListValues;

}
