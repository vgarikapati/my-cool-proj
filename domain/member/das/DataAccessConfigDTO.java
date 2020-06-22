package com.optum.micro.domain.member.das;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;

import com.optum.micro.domain.member.das.DataAccessRules;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataAccessConfigDTO {
	@Id
	@ApiModelProperty(notes = "appIdRoleKey: combination of applicationId and applicationRole delimitted by hyphen, unique identifier of a record", required = true, example = "Portal-Member")
	private String appIdRoleKey;

	@ApiModelProperty(notes = "clientAppMetaData: contains applicationId and applicationRole, combination of these two uniquely identifies a client", required = true)
	@Valid
	@NotNull(message = "NULL_EMPTY")
	private ClientAppMetaData clientAppMetaData;

	@ApiModelProperty(notes = "dataAccessRules", required = true)
	private DataAccessRules dataAccessRules;

	@ApiModelProperty(notes = "createdUser: creator of the record", required = true, example = "testuser")
	private String configCreatedBy;

	@ApiModelProperty(notes = "lastUpdatedUser: last updated by the user", required = true, example = "testuser")
	private String configLastUpdatedBy;

	@ApiModelProperty(notes = "azureCreateDate: created date and time in db", required = true, example = "2019-11-07T08:45:37.190Z")
	private String configCreateDateTime;

	@ApiModelProperty(notes = "azureUpdateDate: last update date and time in db", required = true, example = "2019-11-07T08:45:37.190Z")
	private String configUpdateDateTime;
}
