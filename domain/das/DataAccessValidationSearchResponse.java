package com.optum.micro.domain.das;

import com.optum.micro.commons.domain.fg.OptumRxDataResult;
import com.optum.micro.domain.member.das.DataAccessConfigDTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataAccessValidationSearchResponse extends OptumRxDataResult {

	@ApiModelProperty(notes = "dataAccessValidationConfig: response object containing rules defined for a requesting client", required = true)
	private DataAccessConfigDTO dataAccessValidationConfig;

}
