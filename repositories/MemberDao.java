package com.optum.micro.repositories;

import com.azure.data.cosmos.SqlQuerySpec;
import com.optum.micro.commons.cosmosdb.dao.CommonCosmosDao;
import com.optum.micro.commons.cosmosdb.util.QueryUtility;
import com.optum.micro.domain.Contact;
import com.optum.micro.domain.member.MemberDTO;
import com.optum.micro.domain.member.MemberRequest;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
@Log4j2
public class MemberDao extends CommonCosmosDao {

    private String collectionId;

    private String databaseId;

    private static final String RXCLAIM_MEMBER_DAO="-RxClaimMemberDao";

    public MemberDao(@Value("${azure.cosmosdb.collection.member}") final String collectionId,
                                @Value("${azure.cosmosdb.database}") final String databaseId){
        this.collectionId = collectionId;
        this.databaseId = databaseId;
    }

    @PostConstruct
    @Override
    public void setup() { super.setup(); }

    protected  String getDatabaseId()
    {
        return databaseId;
    }

    protected  String getCollectionId()
    {
        return collectionId;
    }

    public List<MemberDTO> searchMembers(MemberRequest request) {
        String formattedQuery =  formatQueryWithConditions(request);
        SqlQuerySpec querySpec = QueryUtility.getQuerySpecWithKeysAndTopRecords(formattedQuery, request.getCagmiKeys(), request.getPagination().getMaxResults() * 3);
        return queryForList(querySpec,MemberDTO.class);
    }

    private String formatQueryWithConditions(MemberRequest request) {
        String formattedQueryCondition = "";
        if (request.getGender()!=null) {
            formattedQueryCondition += " and r.demographics.gender = " + "\'" + request.getGender().toUpperCase() + "\'";
        }
        Contact contact = request.getContact();
        if (contact != null) {
            if (contact.getAddress1()!=null) {
                formattedQueryCondition += " and r.contact.address1 = " + "\'" + contact.getAddress1().toUpperCase() + "\'";
            }
            if (contact.getAddress2()!=null) {
                formattedQueryCondition += " and r.contact.address2 = " + "\'" + contact.getAddress2().toUpperCase() + "\'";
            }
            if (contact.getAddress3()!=null) {
                formattedQueryCondition += " and r.contact.address3 = " + "\'" + contact.getAddress3().toUpperCase() + "\'";
            }
            if (contact.getCity()!=null) {
                formattedQueryCondition += " and r.contact.city = " + "\'" + contact.getCity().toUpperCase() + "\'";
            }
            if (contact.getState()!=null) {
                formattedQueryCondition += " and r.contact.state = " + "\'" + contact.getState().toUpperCase() + "\'";
            }
            if (contact.getZip2()!=null) {
                formattedQueryCondition += " and r.contact.zip2 = " + "\'" + contact.getZip2() + "\'";
            }
            if (contact.getZip3()!=null) {
                formattedQueryCondition += " and r.contact.zip3 = " + "\'" + contact.getZip3() + "\'";
            }
            if (contact.getCountry()!=null) {
                formattedQueryCondition += " and r.contact.country = " + "\'" + contact.getCountry().toUpperCase() + "\'";
            }
            if (contact.getPhoneNumber()!=null) {
                formattedQueryCondition += " and r.contact.phoneNumber = " + "\'" + contact.getPhoneNumber() + "\'";
            }
        }
        String formattedQuery = StringUtils.replace(QUERY_FOR_MEMBERS, "<MEMBER_QUERY_CONDITIONS>", formattedQueryCondition);
        log.debug("formattedQuery; {}",formattedQuery);
        return formattedQuery;
    }

    protected  String getUserAgentSuffix()
    {
        return RXCLAIM_MEMBER_DAO;
    }

    private static final String QUERY_FOR_MEMBERS = "" +
            "select <TOP_RECORDS> r.cagmiKey,\n" +
            "r.sourceSystemInstance,\n" +
            "r.memberId,\n" +
            "r.carrierId,\n" +
            "r.accountId,\n" +
            "r.groupId,\n" +
            "r.firstName,\n" +
            "r.lastName,\n" +
            "r.middleName,\n" +
            "r.familyType,\n" +
            "r.familyIndicator,\n" +
            "r.familyId,\n" +
            "r.personCode,\n" +
            "r.relationshipCode,\n" +
            "r.benefitResetDate,\n" +
            "r.languageCode,\n" +
            "r.durKey,\n" +
            "r.durProcessFlag,\n" +
            "r.multipleBirthCode,\n" +
            "r.originalEffectiveDate,\n" +
            "r.memberType,\n" +
            "r.altInsuranceMbrId,\n" +
            "r.altInsuranceFlag,\n" +
            "r.contact,\n" +
            "r.demographics,\n" +
            "r.hicIdentifier\n" +
            "from root r where r.cagmiKey in <INPUT_KEYS>\n" +
            " <MEMBER_QUERY_CONDITIONS>\n" +
            "order by r.cagmiKey";

}

