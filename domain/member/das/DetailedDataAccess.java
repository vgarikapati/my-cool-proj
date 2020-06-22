package com.optum.micro.domain.member.das;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DetailedDataAccess {

	private String instanceId;
	private List<CagList> cagLists;

}
