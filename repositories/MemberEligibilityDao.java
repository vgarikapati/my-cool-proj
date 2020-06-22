package com.optum.micro.repositories;

import com.azure.data.cosmos.SqlQuerySpec;
import com.optum.micro.commons.cosmosdb.dao.CommonCosmosDao;
import com.optum.micro.commons.cosmosdb.util.QueryUtility;
import com.optum.micro.util.MemberSearchConstants;
import com.optum.micro.domain.eligibility.MemberEligibilityDoc;
import com.optum.micro.domain.eligibility.MemberEligibility;
import com.optum.micro.domain.eligibility.MemberInactiveEligibilityDoc;
import com.optum.micro.domain.eligibility.MemberEligibilitySeqKey;
import com.optum.micro.domain.eligibility.EligibilityWithMemberDoc;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Component
@RefreshScope
@Log4j2
public class MemberEligibilityDao extends CommonCosmosDao {

    private String collectionId;

    private String databaseId;

    public static final String EFF_DATE = "<effDate>";
    public static final String CAGMI_KEY = "<cagmiKey>";
    public static final String MEMBER_ELIGIBILITY_DAO="-RxClaimMemberEligibilityDao";


    public MemberEligibilityDao(@Value("${azure.cosmosdb.collection.eligibility.member}") final String collectionId,
                               @Value("${azure.cosmosdb.database}") final String databaseId){
        this.collectionId = collectionId;
        this.databaseId = databaseId;
    }

    @PostConstruct
    @Override
    public void setup() {
        super.setup();
    }

    protected  String getDatabaseId()
    {
        return databaseId;
    }

    protected  String getCollectionId()
    {
        return collectionId;
    }

    public List<MemberEligibilityDoc> searchActiveMemberEligibilities(List<String> cagmiKeys, int maxResult) {
        SqlQuerySpec querySpec = QueryUtility.getQuerySpecWithKeysAndTopRecords(QUERY_FOR_ACTIVE_BY_CAGMI,cagmiKeys,(maxResult + 1));
        return queryForList(querySpec,MemberEligibilityDoc.class );
    }

    public List<MemberEligibility> searchActiveMemberEligibilitiesWithEffectiveDate(List<String> cagmiKeys, String effDate) {
        String formattedQuery = QueryUtility.formatQueryWithKeys(QUERY_FOR_ACTIVE_ELIGIBILITY,cagmiKeys);
        formattedQuery = StringUtils.replace(formattedQuery, EFF_DATE, ("'"+effDate+"'"));
        SqlQuerySpec querySpec = new SqlQuerySpec(formattedQuery);
        return queryForList(querySpec,MemberEligibility.class );
    }


    public List<MemberEligibilityDoc> searchInActiveMemberEligibilities (List<String> inactiveMemEligSeqKeys, Integer maxResult) {
        SqlQuerySpec querySpec = QueryUtility.getQuerySpecWithKeysAndTopRecords(QUERY_FOR_INACTIVE_BY_CAGMISEQ,inactiveMemEligSeqKeys,(maxResult + 1));
        List<MemberInactiveEligibilityDoc> inactiveMelList = queryForList(querySpec,MemberInactiveEligibilityDoc.class );
        return inactiveMelList.stream().map(mel -> convertToEligibilityDoc(mel)).collect(Collectors.toList());
    }


    @Async("eligibilityThreadPoolTaskExecutor")
    public CompletableFuture<String> getInactiveMemberEligiblitySeqKey(String cagmiKey) {
        String formattedQuery = StringUtils.replace(QUERY_FOR_SINGLE_LATEST_SEQ, CAGMI_KEY, ("'"+cagmiKey+"'"));
        SqlQuerySpec querySpec = new SqlQuerySpec(formattedQuery);
        CompletableFuture<String> memEligSeqStringKey =new CompletableFuture<>();
        List<MemberEligibilitySeqKey> memberEligibilitySeqKeys = queryForList(querySpec,MemberEligibilitySeqKey.class );
        if (!memberEligibilitySeqKeys.isEmpty()) {
            memEligSeqStringKey.complete(buildStringSeqKey(memberEligibilitySeqKeys.get(0)));
        }
        else {
            memEligSeqStringKey.complete(MemberSearchConstants.EMPTY_STRING);
        }
        return memEligSeqStringKey;
    }


    private String buildStringSeqKey (MemberEligibilitySeqKey objectKey){
        return objectKey.getCagmiKey() + MemberSearchConstants.KEY_FIELD_DELIMITER + objectKey.getEligibilitySequenceNumber();
    }



    protected  String getUserAgentSuffix()
    {
        return MEMBER_ELIGIBILITY_DAO;
    }


    private static final String QUERY_FOR_SINGLE_LATEST_SEQ =  "SELECT top 1 c.cagmiKey, c.eligibilitySequenceNumber  FROM c  WHERE c.cagmiKey = <cagmiKey> and c.documentType ='Inactive' order by c.eligibilitySequenceNumber";


    private static final String QUERY_FOR_ACTIVE_BY_CAGMI = "select <TOP_RECORDS> * from root r where r.cagmiKey in <INPUT_KEYS> and r.documentType ='Active' order by r.cagmiKey";

    private static final String QUERY_FOR_INACTIVE_BY_CAGMISEQ = "select <TOP_RECORDS> * from root r where r.cagmiSeqKey in <INPUT_KEYS> and r.documentType ='Inactive' order by r.cagmiSeqKey";

    private static final String QUERY_FOR_ACTIVE_ELIGIBILITY = "SELECT \n" +
            "       c.carrierId, \n" +
            "       c.accountId,\n" +
            "       c.groupId,\n" +
            "       c.memberId, \n" +
            "       c.sourceSystemInstance,\n" +
            "       c.cagmiKey, \n" +
            "       mel.eligibilitySequenceNumber,\n" +
            "       mel.eligibilityStatus,\n" +
            "       mel.eligibilityStartDate,\n" +
            "       mel.eligibilityEndDate,\n" +
            "       mel.copayBrand,\n" +
            "       mel.copayGeneric,\n" +
            "       mel.copay3,\n" +
            "       mel.copay4,\n" +
            "       mel.clientProductCode,\n" +
            "       mel.clientRiderCode,\n" +
            "       mel.spendDownAmount,\n" +
            "       mel.planCode,\n" +
            "       mel.planEffectiveDate\n" +
            "FROM c Join mel IN c.eligibility \n" +
            "WHERE c.cagmiKey in <INPUT_KEYS> \n" +
            "and c.documentType='Active' \n" +
            "and mel.eligibilityStatus = 'A' \n" +
            "and ((mel.eligibilityStartDate <= <effDate> and mel.eligibilityEndDate >= <effDate> ) \n" +
            "or(mel.eligibilityStartDate <= <effDate> and mel.eligibilityEndDate = '0'))\n" +
            "order by c.cagmiKey";


    private MemberEligibilityDoc convertToEligibilityDoc (MemberInactiveEligibilityDoc memberInactiveEligibilityDoc) {
        MemberEligibilityDoc doc = new MemberEligibilityDoc();
        BeanUtils.copyProperties(memberInactiveEligibilityDoc,doc);
        EligibilityWithMemberDoc melDoc = new EligibilityWithMemberDoc();
        if(memberInactiveEligibilityDoc.getEligibility()!=null) {
            BeanUtils.copyProperties(memberInactiveEligibilityDoc.getEligibility(), melDoc);
        }
        doc.setEligibility(Collections.singletonList(melDoc));
        return doc;
    }

}