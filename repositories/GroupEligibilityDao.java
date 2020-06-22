package com.optum.micro.repositories;


import com.azure.data.cosmos.CosmosClient;
import com.azure.data.cosmos.SqlQuerySpec;
import com.optum.micro.commons.cosmosdb.dao.CommonCosmosDao;
import com.optum.micro.commons.cosmosdb.util.QueryUtility;
import com.optum.micro.domain.eligibility.EligibilitySeqKey;
import com.optum.micro.domain.eligibility.GroupEligibility;
import com.optum.micro.domain.eligibility.GroupEligibilityDoc;
import com.optum.micro.domain.eligibility.EligibilityWithGroupDoc;
import com.optum.micro.domain.eligibility.GroupInactiveEligibilityDoc;
import com.optum.micro.util.MemberSearchConstants;
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
public class GroupEligibilityDao extends CommonCosmosDao {

    private String collectionId;

    private String databaseId;

    public static final String EFF_DATE = "<effDate>";
    public static final String CAGI_KEY = "<cagiKey>";
    public static final String RXCLAIM_GROUP_ELIGIBILITY_DAO ="-RxClaimGroupEligibilityDao";

    public GroupEligibilityDao(@Value("${azure.cosmosdb.collection.eligibility.group}") final String collectionId,
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


    public List<GroupEligibilityDoc> searchActiveGroupEligibilities(List<String> cagiKeys) {
        SqlQuerySpec querySpec = QueryUtility.getQuerySpecWithKeys(QUERY_FOR_ACTIVE_BY_CAGI,cagiKeys);
        return queryForList(querySpec,GroupEligibilityDoc.class );
    }

    public List<GroupEligibility> searchActiveGroupEligibilitiesWithEffectiveDate(List<String> cagiKeys, String effDate) {
        String formattedQuery = QueryUtility.formatQueryWithKeys(QUERY_FOR_ACTIVE_ELIGIBILITY,cagiKeys);
        formattedQuery = StringUtils.replace(formattedQuery, EFF_DATE, ("'"+effDate+"'"));
        SqlQuerySpec querySpec = new SqlQuerySpec(formattedQuery);
        return queryForList(querySpec,GroupEligibility.class );
    }



    public List<GroupEligibilityDoc> searchInActiveGroupEligibilities (List<String> inactiveEligSeqKeys) {
        SqlQuerySpec querySpec = QueryUtility.getQuerySpecWithKeys(QUERY_FOR_INACTIVE_BY_CAGISEQ,inactiveEligSeqKeys);
        List<GroupInactiveEligibilityDoc> inactiveGelList = queryForList(querySpec,GroupInactiveEligibilityDoc.class );
        return inactiveGelList.stream().map(gel -> convertToEligibilityDoc(gel)).collect(Collectors.toList());
    }


    @Async("eligibilityThreadPoolTaskExecutor")
    public CompletableFuture<String> getInactiveEligiblitySeqKey(String cagiKey) {
        String formattedQuery = StringUtils.replace(QUERY_FOR_SINGLE_LATEST_SEQ, CAGI_KEY, ("'"+cagiKey+"'"));
        SqlQuerySpec querySpec = new SqlQuerySpec(formattedQuery);
        CompletableFuture<String> eligSeqStringKey =new CompletableFuture<>();
        List<EligibilitySeqKey> inActiveEligSeqKeys= queryForList(querySpec,EligibilitySeqKey.class );
        if (!inActiveEligSeqKeys.isEmpty()) {
            eligSeqStringKey.complete(buildStringSeqKey(inActiveEligSeqKeys.get(0)));
        }
        else {
            eligSeqStringKey.complete(MemberSearchConstants.EMPTY_STRING);
        }
        return eligSeqStringKey;
    }

    private GroupEligibilityDoc convertToEligibilityDoc (GroupInactiveEligibilityDoc groupInactiveEligibilityDoc) {
        GroupEligibilityDoc doc = new GroupEligibilityDoc();
        BeanUtils.copyProperties(groupInactiveEligibilityDoc,doc);
        EligibilityWithGroupDoc gelDoc = new EligibilityWithGroupDoc();
        if(groupInactiveEligibilityDoc.getEligibility()!=null){
            BeanUtils.copyProperties(groupInactiveEligibilityDoc.getEligibility(),gelDoc);
        }
        doc.setEligibility(Collections.singletonList(gelDoc));
        return doc;
    }

    private String buildStringSeqKey (EligibilitySeqKey objectKey) {
        if (objectKey!=null) {
            return objectKey.getCagiKey() + MemberSearchConstants.KEY_FIELD_DELIMITER + objectKey.getEligibilitySequenceNumber();
        }
        else{
            return null;
        }
    }

    protected  String getUserAgentSuffix() { return RXCLAIM_GROUP_ELIGIBILITY_DAO; }

    //use this....in parallel
    private static final String QUERY_FOR_SINGLE_LATEST_SEQ =  "SELECT top 1 c.cagiKey, c.eligibilitySequenceNumber  FROM c  WHERE c.cagiKey = <cagiKey> and c.documentType ='Inactive' order by c.eligibilitySequenceNumber";


    private static final String QUERY_FOR_ACTIVE_BY_CAGI = "select * from root r where r.cagiKey in <INPUT_KEYS> and r.documentType ='Active' order by r.cagiKey";

    ////use this......not parallel call
    private static final String QUERY_FOR_INACTIVE_BY_CAGISEQ = "select * from root r where r.cagiSeqKey in <INPUT_KEYS> and r.documentType ='Inactive' order by r.cagiKey";

    private static final String QUERY_FOR_ACTIVE_ELIGIBILITY = "SELECT \n" +
            "       c.carrierId, \n" +
            "       c.accountId,\n" +
            "       c.groupId, \n" +
            "       c.sourceSystemInstance,\n" +
            "       c.cagiKey, \n" +
            "       gel.eligibilitySequenceNumber,\n" +
            "       gel.eligibilityStatus,\n" +
            "       gel.eligibilityStartDate,\n" +
            "       gel.eligibilityEndDate,\n" +
            "       gel.copayBrand,\n" +
            "       gel.copayGeneric,\n" +
            "       gel.copay3,\n" +
            "       gel.copay4,\n" +
            "       gel.copay5,\n" +
            "       gel.copay6,\n" +
            "       gel.copay7,\n" +
            "       gel.copay8,\n" +
            "       gel.benefitCode,\n" +
            "       gel.createUser,\n" +
            "       gel.createDate,\n" +
            "       gel.createTime,\n" +
            "       gel.updateUser,\n" +
            "       gel.updateDate,\n" +
            "       gel.updateTime,\n" +
            "       gel.planCode,\n" +
            "       gel.planEffectiveDate\n" +
            "FROM c Join gel IN c.eligibility \n" +
            "WHERE c.cagiKey in <INPUT_KEYS> \n" +
            "and c.documentType='Active' \n" +
            "and gel.eligibilityStatus = 'A' \n" +
            "and ((gel.eligibilityStartDate <= <effDate> and gel.eligibilityEndDate >= <effDate>) \n" +
            "or (gel.eligibilityStartDate <= <effDate> and gel.eligibilityEndDate = '0'))\n" +
            "order by c.cagiKey";
}