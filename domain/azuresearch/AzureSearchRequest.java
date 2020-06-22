package com.optum.micro.domain.azuresearch;

import com.optum.micro.service.azuresearch.IndexedFields;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AzureSearchRequest {

	private String search;
	private String filter;
	private String orderby = IndexedFields.SOURCE_SYSTEM_INSTANCE + "," + IndexedFields.CARRIER_ID + "," + IndexedFields.ACCOUNT_ID + "," + IndexedFields.GROUP_ID + "," +IndexedFields.MEMBER_ID;
	private Integer top;
}