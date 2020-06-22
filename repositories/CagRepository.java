package com.optum.micro.repositories;

import com.azure.data.cosmos.SqlQuerySpec;
import com.optum.micro.commons.cosmosdb.dao.CommonCosmosDao;
import com.optum.micro.commons.cosmosdb.util.QueryUtility;
import com.optum.micro.domain.cag.CagDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
@Log4j2
public class CagRepository extends CommonCosmosDao {

    private String collectionId;

    private String databaseId;

    private static final String RXCLAIM_CAG_DAO="-RxClaimCAGDao";

    public CagRepository(@Value("${azure.cosmosdb.collection.cag}") final String collectionId,
                     @Value("${azure.cosmosdb.database}") final String databaseId){
        this.collectionId = collectionId;
        this.databaseId = databaseId;
    }

    @PostConstruct
    @Override
    public void setup() { super.setup(); }

    @Override
    protected  String getDatabaseId() { return databaseId; }

    @Override
    protected  String getCollectionId() { return collectionId; }

    public List<CagDTO> findByCagiKeyIn(List<String> cagiKeys){
        SqlQuerySpec querySpec = QueryUtility.getQuerySpecWithKeys(QUERY_FOR_CAGS, cagiKeys);
        return queryForList(querySpec, CagDTO.class);
    }
    protected  String getUserAgentSuffix()
    {
        return RXCLAIM_CAG_DAO;
    }

    private static final String QUERY_FOR_CAGS= "select r.cagiKey,r.carrierName,r.accountName,r.groupName from root r where r.cagiKey in <INPUT_KEYS>";

}
