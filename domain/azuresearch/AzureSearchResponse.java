package com.optum.micro.domain.azuresearch;

import java.util.List;

import com.optum.micro.domain.CagmiKey;

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
public class AzureSearchResponse {
	private List<CagmiKey> value;
}
