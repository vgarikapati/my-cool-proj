package com.optum.micro.domain.das;

import javax.validation.constraints.NotEmpty;

import com.optum.micro.commons.domain.fg.OptumRxDataRequest;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataAccessValidationSearchRequest extends OptumRxDataRequest {

	@NotEmpty(message = "NULL_EMPTY")
	@ApiModelProperty(notes = "applicationId: applicationId of requesting client", required = true, example = "Portal")
	private String applicationId;

	@ApiModelProperty(notes = "applicationType: applicationType(Internal/External) of requesting client", required = false, example = "IA")
	private String applicationType;

	@NotEmpty(message = "NULL_EMPTY")
	@ApiModelProperty(notes = "applicationRole: Role defined for requesting client", required = true, example = "Member")
	private String applicationRole;
}
