package com.optum.micro.domain.member.das;

import javax.validation.constraints.NotEmpty;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientAppMetaData {

	@NotEmpty(message = "NULL_EMPTY")
	@ApiModelProperty(notes = "applicationId: applicationId of requesting client", required = true, example = "Portal")
	private String applicationId;

	@ApiModelProperty(notes = "applicationType: applicationType(Internal/External) of requesting client", required = true, example = "IA")
	private String applicationType;

	@NotEmpty(message = "NULL_EMPTY")
	@ApiModelProperty(notes = "applicationRole: Role defined for requesting client", required = true, example = "Member")
	private String applicationRole;

}
