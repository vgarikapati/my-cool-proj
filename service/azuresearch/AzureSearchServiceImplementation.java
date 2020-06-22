package com.optum.micro.service.azuresearch;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.optum.micro.commons.exception.InputValidationException;
import com.optum.micro.commons.exception.ServiceUnavailableException;
import com.optum.micro.domain.CagmiKey;
import com.optum.micro.domain.MemberSearchRequest;
import com.optum.micro.domain.azuresearch.AzureSearchRequest;
import com.optum.micro.domain.azuresearch.AzureSearchResponse;
import com.optum.micro.domain.crossreferenceid.CrossReferenceIdResponse;
import com.optum.micro.domain.crossreferenceid.CrossReferenceMember;
import com.optum.micro.integration.config.AzureSearchIntegrationConfig;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class AzureSearchServiceImplementation implements AzureSearchService {

	private static final String MEMBER_ID = "M";
	private static final String FAMILY_ID = "F";
	private static final String SUPPLEMENTAL_ID = "S";
	private static final String XREF_ID = "X";
	private static final String FAMILY_ID_SCOPE_CARRIER_ID = "C";
	private static final String FAMILY_ID_SCOPE_ACCOUNT_ID = "A";
	private static final String FAMILY_ID_SCOPE_GROUP_ID = "G";
	private static final String ACTIVE_INDICATOR = "A";
	private static final String SUPPLEMENTAL_ID_TYPE = "07";
	private static final String SEARCH_MODE_ANY = "any";
	private static final String SEARCH_MODE_ALL = "all";
	private static final String QUERY_TYPE_FULL = "full";
	private static final String STARTS_WITH = "S";
	private static final String EQUAL = " eq ";
	private static final String AND = " and ";
	private static final String OR = " or ";
	private static final String BLANK = "";
	private static final String FORWARD_SLASH = "/";
	private static final String REGEX_ANYTHING_OPERATOR = ".";
	private static final String COMMA = ",";
	private static final String ASTERISK = "*";
	private static final String SINGLE_QUOTE = "'";
	private static final Integer DEFAULT_FACTOR_FOR_TOP = 3;
	private static final Integer DEFAULT_NUMBER_OF_TOP_RECORDS = 300;
	private AzureSearchIntegrationConfig.AzureSearchServiceGateway azureSearchServiceGateway;

	@Autowired
	AzureSearchServiceImplementation(AzureSearchServiceHelper azureSearchServiceHelper,
			AzureSearchIntegrationConfig.AzureSearchServiceGateway azureSearchServiceGateway) {
		this.azureSearchServiceGateway = azureSearchServiceGateway;
	}

	@Override
	public List<CagmiKey> getListOfCagmiKeys(MemberSearchRequest memberSearchRequest,
			CrossReferenceIdResponse crossReferenceIdResponse) {

		String id = memberSearchRequest.getId();
		String idType = memberSearchRequest.getIdType() != null ? memberSearchRequest.getIdType() : MEMBER_ID;
		String idSearchOperator = memberSearchRequest.getIdSearchOperator();
		String memberEffectiveDate = memberSearchRequest.getMemberEffectiveDate();
		StringBuilder sb = new StringBuilder();
		boolean isFirst = true;
		if (id != null && idType != null) {
			switch (idType) {
			case MEMBER_ID:
				sb.append(findByMemberId(id, idSearchOperator));
				isFirst = false;
				break;
			case FAMILY_ID:
				sb.append(findByFamilyId(id, idSearchOperator));
				isFirst = false;
				break;
			case SUPPLEMENTAL_ID:
				sb.append(findBySupplementalId(id, memberEffectiveDate));
				isFirst = false;
				break;
			case XREF_ID:
				return getAzureSearchResponseUsingCrossReferenceIds(memberSearchRequest, crossReferenceIdResponse);
			default:
				log.error("Invalid Id Type");
				throw new InputValidationException("AZURE_SEARCH", "IdType is Invalid");
			}
		}
		String queryStringForCIdAIdGId = formulateQueryForCarrierIdAccountIdGroupId(memberSearchRequest, isFirst);
		if (!queryStringForCIdAIdGId.isEmpty()) {
			sb.append(queryStringForCIdAIdGId);
			isFirst = false;
		}
		sb.append(formulateTheQueryForOtherMemberDetails(memberSearchRequest, isFirst));
		String completeQuery = sb.toString();
		if (!BLANK.equals(completeQuery)) {
			return getTheResponse(completeQuery, memberSearchRequest);
		}
		log.error("Request Doesn't contain the required input parameters");
		throw new InputValidationException("AZURE_SEARCH", "Input Request doesn't contain the required input parameters");

	}

	private List<CagmiKey> getAzureSearchResponseUsingCrossReferenceIds(MemberSearchRequest memberSearchRequest,
			CrossReferenceIdResponse crossReferenceIdResponse) {
		StringBuilder sb = new StringBuilder();
		boolean isFirst = true;
		if (crossReferenceIdResponse != null) {
			String queryStringForCIdAIdGId = formulateQueryForCarrierIdAccountIdGroupId(memberSearchRequest, isFirst);
			String queryStringForOtherMemberDetails = formulateTheQueryForOtherMemberDetails(memberSearchRequest,
					isFirst);
			String intermediateQueryString;
			if (!queryStringForCIdAIdGId.isEmpty() && queryStringForOtherMemberDetails.isEmpty()) {
				intermediateQueryString = queryStringForCIdAIdGId;
			} else if (queryStringForCIdAIdGId.isEmpty() && !queryStringForOtherMemberDetails.isEmpty()) {
				intermediateQueryString = queryStringForOtherMemberDetails;
			} else if (!queryStringForCIdAIdGId.isEmpty() && !queryStringForOtherMemberDetails.isEmpty()) {
				intermediateQueryString = queryStringForCIdAIdGId + AND + queryStringForOtherMemberDetails;
			} else {
				intermediateQueryString = BLANK;
			}
			for (CrossReferenceMember crossReferenceMember : crossReferenceIdResponse.getNewMembers()) {
				if (!isFirst) {
					sb.append(OR);
				}
				if (intermediateQueryString.isEmpty()) {
					sb.append(findBySourceSystemInstance(crossReferenceMember.getSourceSystemInstance()) + AND
							+ findByMemberId(crossReferenceMember.getNewMemberId(), STARTS_WITH));
				} else {
					sb.append(findBySourceSystemInstance(crossReferenceMember.getSourceSystemInstance()) + AND
							+ intermediateQueryString + AND
							+ findByMemberId(crossReferenceMember.getNewMemberId(), STARTS_WITH));
				}
				isFirst = false;
			}
			String completeQuery = sb.toString();
			if (!BLANK.equals(completeQuery)) {
				return getTheResponse(completeQuery, memberSearchRequest);
			}
			log.error(
					"crossReferenceIdResponse.getNewMembers() is empty/Request Doesn't contain the required input parameters");
			throw new InputValidationException("AZURE_SEARCH", "Id Type is CrossReference But crossReferenceIdResponse.getNewMembers() is empty");
		}
		log.error("Id Type is CrossReference But CrossReferenceIdResponse is null");
		throw new InputValidationException("AZURE_SEARCH", "Id Type is CrossReference But CrossReferenceIdResponse is null");
	}

	private String formulateQueryForCarrierIdAccountIdGroupId(MemberSearchRequest memberSearchRequest,
			boolean isFirst) {
		String id = memberSearchRequest.getId();
		String idType = memberSearchRequest.getIdType();
		String carrierId = memberSearchRequest.getCarrierId();
		String accountId = memberSearchRequest.getAccountId();
		String groupId = memberSearchRequest.getGroupId();
		String familyIdScope = memberSearchRequest.getFamilyIdScope() != null ? memberSearchRequest.getFamilyIdScope()
				: FAMILY_ID_SCOPE_GROUP_ID;

		StringBuilder sb = new StringBuilder();

		if (id != null && familyIdScope != null && FAMILY_ID.equals(idType)) {
			if (FAMILY_ID_SCOPE_GROUP_ID.equals(familyIdScope)) {
				if (carrierId != null && accountId != null && groupId != null) {
					if (!isFirst) {
						sb.append(AND);
					}
					sb.append(findByCarrierId(carrierId) + AND + findByAccountId(accountId) + AND
							+ findByGroupId(groupId));
				}

			} else if (FAMILY_ID_SCOPE_ACCOUNT_ID.equals(familyIdScope)) {
				if (carrierId != null && accountId != null) {
					if (!isFirst) {
						sb.append(AND);
					}
					sb.append(findByCarrierId(carrierId) + AND + findByAccountId(accountId));
				}

			} else if (FAMILY_ID_SCOPE_CARRIER_ID.equals(familyIdScope)) {
				if (carrierId != null) {
					if (!isFirst) {
						sb.append(AND);
					}
					sb.append(findByCarrierId(carrierId));
				}
			} else {
				log.error("Invalid FamilyIdScope");
			}
		} else {
			if (carrierId != null) {
				if (!isFirst) {
					sb.append(AND);
				}
				sb.append(findByCarrierId(carrierId));
				isFirst = false;
			}
			if (accountId != null) {
				if (!isFirst) {
					sb.append(AND);
				}
				sb.append(findByAccountId(accountId));
				isFirst = false;
			}
			if (groupId != null) {
				if (!isFirst) {
					sb.append(AND);
				}
				sb.append(findByGroupId(groupId));
			}
		}
		return sb.toString();
	}

	private String formulateTheQueryForOtherMemberDetails(MemberSearchRequest memberSearchRequest, boolean isFirst) {
		String firstName = memberSearchRequest.getFirstName();
		String firstNameSearchOperator = memberSearchRequest.getFirstNameSearchOperator();
		String lastName = memberSearchRequest.getLastName();
		String lastNameSearchOperator = memberSearchRequest.getLastNameSearchOperator();
		String dateOfBirth = memberSearchRequest.getDateOfBirth();
		String zip = memberSearchRequest.getContact() != null ? memberSearchRequest.getContact().getZip() : null;
		String sourceSystemInstance = memberSearchRequest.getSourceSystemInstance();

		StringBuilder sb = new StringBuilder();

		if (firstName != null) {
			if (!isFirst) {
				sb.append(AND);
			}
			sb.append(findByFirstName(firstName, firstNameSearchOperator));
			isFirst = false;

		}
		if (lastName != null) {
			if (!isFirst) {
				sb.append(AND);
			}
			sb.append(findByLastName(lastName, lastNameSearchOperator));
			isFirst = false;
		}
		if (dateOfBirth != null) {
			if (!isFirst) {
				sb.append(AND);
			}
			sb.append(findByDateOfBirth(dateOfBirth));
			isFirst = false;

		}
		if (zip != null) {
			if (!isFirst) {
				sb.append(AND);
			}
			sb.append(findByZip(zip));
			isFirst = false;
		}

		if (sourceSystemInstance != null) {
			if (!isFirst) {
				sb.append(AND);
			}
			sb.append(findBySourceSystemInstance(sourceSystemInstance));
		}

		return sb.toString();
	}

	private List<CagmiKey> getTheResponse(String completeQuery, MemberSearchRequest memberSearchRequest) {
		AzureSearchRequest azureSearchRequest = new AzureSearchRequest();
		azureSearchRequest.setFilter(completeQuery);
		Integer top = getTopValue(memberSearchRequest);
		azureSearchRequest.setTop(top);
		log.debug("Azure Search Request " + azureSearchRequest);
		AzureSearchResponse azureSearchResponse = azureSearchServiceGateway
				.sendRequestToAzureSearch(azureSearchRequest);
		if (azureSearchResponse != null) {
			return azureSearchResponse.getValue();
		} else {
			log.error("Azure Search Service Unavailable");
			throw new ServiceUnavailableException("AZURE_SEARCH","Azure Search Service is not Responding");
		}
	}

	private Integer getTopValue(MemberSearchRequest memberSearchRequest) {
		if (memberSearchRequest.getPagination() != null
				&& memberSearchRequest.getPagination().getMaxResults() != null) {
			return DEFAULT_FACTOR_FOR_TOP * memberSearchRequest.getPagination().getMaxResults();
		}
		return DEFAULT_NUMBER_OF_TOP_RECORDS;
	}

	private String findBySourceSystemInstance(String sourceSystemInstance) {
		return IndexedFields.SOURCE_SYSTEM_INSTANCE + EQUAL + SINGLE_QUOTE + sourceSystemInstance + SINGLE_QUOTE;
	}

	private String findByCarrierId(String carrierId) {
		return IndexedFields.CARRIER_ID + EQUAL + SINGLE_QUOTE + carrierId + SINGLE_QUOTE;
	}

	private String findByAccountId(String accountId) {
		return IndexedFields.ACCOUNT_ID + EQUAL + SINGLE_QUOTE + accountId + SINGLE_QUOTE;
	}

	private String findByGroupId(String groupId) {
		return IndexedFields.GROUP_ID + EQUAL + SINGLE_QUOTE + groupId + SINGLE_QUOTE;
	}

	private String findByDateOfBirth(String dateOfBirth) {
		return IndexedFields.DATE_OF_BIRTH + EQUAL + SINGLE_QUOTE + dateOfBirth + SINGLE_QUOTE;
	}

	private String findBySupplementalId(String supplementalId, String memberEffectiveDate) {
		return "supplementIdentifiers/any(object:object/supplementalId" + EQUAL + SINGLE_QUOTE + supplementalId
				+ SINGLE_QUOTE + AND + "object/supplementalStatus" + EQUAL + SINGLE_QUOTE + ACTIVE_INDICATOR
				+ SINGLE_QUOTE + AND + "object/supplementalIdType" + EQUAL + SINGLE_QUOTE + SUPPLEMENTAL_ID_TYPE
				+ SINGLE_QUOTE + AND + "object/fromDate lt '" + memberEffectiveDate + "' and object/thruDate gt '"
				+ memberEffectiveDate + "')";
	}

	private String findByZip(String zip) {
		return IndexedFields.ZIP + EQUAL + SINGLE_QUOTE + zip + SINGLE_QUOTE;
	}

	private String findByMemberId(String memberId, String operator) {
		if (STARTS_WITH.equals(operator)) {
			// If memberId contained /, it is possible that request may break,so
			// escaping the character
			if (memberId.contains("/")) {
				memberId = memberId.replace("/", "\\/");
			}
			return "search.ismatch(" + SINGLE_QUOTE + FORWARD_SLASH + memberId + REGEX_ANYTHING_OPERATOR + ASTERISK
					+ FORWARD_SLASH + SINGLE_QUOTE + COMMA + SINGLE_QUOTE + IndexedFields.MEMBER_ID + SINGLE_QUOTE
					+ COMMA + SINGLE_QUOTE + QUERY_TYPE_FULL + SINGLE_QUOTE + COMMA + SINGLE_QUOTE + SEARCH_MODE_ALL
					+ SINGLE_QUOTE + ")";
		} else {
			return IndexedFields.MEMBER_ID + EQUAL + SINGLE_QUOTE + memberId + SINGLE_QUOTE;
		}
	}

	private String findByLastName(String lastName, String operator) {
		if (STARTS_WITH.equals(operator)) {
			// If lastName contained /, it is possible that request may break,so
			// escaping the character
			if (lastName.contains("/")) {
				lastName = lastName.replace("/", "\\/");
			}
			return "search.ismatch(" + SINGLE_QUOTE + FORWARD_SLASH + lastName + REGEX_ANYTHING_OPERATOR + ASTERISK
					+ FORWARD_SLASH + SINGLE_QUOTE + COMMA + SINGLE_QUOTE + IndexedFields.LAST_NAME + SINGLE_QUOTE
					+ COMMA + SINGLE_QUOTE + QUERY_TYPE_FULL + SINGLE_QUOTE + COMMA + SINGLE_QUOTE + SEARCH_MODE_ALL
					+ SINGLE_QUOTE + ")";
		} else {
			return IndexedFields.LAST_NAME + EQUAL + SINGLE_QUOTE + lastName + SINGLE_QUOTE;
		}
	}

	private String findByFirstName(String firstName, String operator) {
		if (STARTS_WITH.equals(operator)) {
			// If firstName contained /, it is possible that request may
			// break,so escaping the character
			if (firstName.contains("/")) {
				firstName = firstName.replace("/", "\\/");
			}
			return "search.ismatch(" + SINGLE_QUOTE + FORWARD_SLASH + firstName + REGEX_ANYTHING_OPERATOR + ASTERISK
					+ FORWARD_SLASH + SINGLE_QUOTE + COMMA + SINGLE_QUOTE + IndexedFields.FIRST_NAME + SINGLE_QUOTE
					+ COMMA + SINGLE_QUOTE + QUERY_TYPE_FULL + SINGLE_QUOTE + COMMA + SINGLE_QUOTE + SEARCH_MODE_ALL
					+ SINGLE_QUOTE + ")";
		} else {
			return IndexedFields.FIRST_NAME + EQUAL + SINGLE_QUOTE + firstName + SINGLE_QUOTE;
		}

	}

	private String findByFamilyId(String familyId, String operator) {
		if (STARTS_WITH.equals(operator)) {
			// If familyId contained /, it is possible that request may break,
			// so escaping the character
			if (familyId.contains("/")) {
				familyId = familyId.replace("/", "\\/");
			}
			return "search.ismatch(" + SINGLE_QUOTE + FORWARD_SLASH + familyId + REGEX_ANYTHING_OPERATOR + ASTERISK
					+ FORWARD_SLASH + SINGLE_QUOTE + COMMA + SINGLE_QUOTE + IndexedFields.FAMILY_ID + SINGLE_QUOTE
					+ COMMA + SINGLE_QUOTE + QUERY_TYPE_FULL + SINGLE_QUOTE + COMMA + SINGLE_QUOTE + SEARCH_MODE_ALL
					+ SINGLE_QUOTE + ")";
		} else {
			return IndexedFields.FAMILY_ID + EQUAL + SINGLE_QUOTE + familyId + SINGLE_QUOTE;
		}

	}

}
