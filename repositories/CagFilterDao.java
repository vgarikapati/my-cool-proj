package com.optum.micro.repositories;

import java.util.List;

import javax.annotation.PostConstruct;

import com.optum.micro.domain.cag.AccountFilterListDTO;
import com.optum.micro.domain.cag.CarrierFilterListDTO;
import com.optum.micro.domain.cag.GroupFilterListDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.azure.data.cosmos.SqlQuerySpec;
import com.optum.micro.commons.cosmosdb.dao.CommonCosmosDao;
import com.optum.micro.commons.cosmosdb.util.QueryUtility;

@Component
public class CagFilterDao extends CommonCosmosDao {

	private String databaseId;

	private String collectionId;
	
	private String carrierListCollection;

	private String accountListCollection;

	private String groupListCollection;

	@Autowired
	public CagFilterDao(@Value("${azure.cosmosdb.database}") final String databaseId,
			@Value("${azure.cosmosdb.collection.carrierList}") final String carrierListCollection,
			@Value("${azure.cosmosdb.collection.accountList}") final String accountListCollection,
			@Value("${azure.cosmosdb.collection.groupList}") final String groupListCollection) {
		this.collectionId = null;
		this.databaseId = databaseId;
		this.carrierListCollection = carrierListCollection;
		this.accountListCollection = accountListCollection;
		this.groupListCollection = groupListCollection;
	}

	@PostConstruct
	@Override
	public void setup() {
		super.setup();
	}

	@Override
	protected String getCollectionId() {
		return this.collectionId;
	}

	@Override
	protected String getDatabaseId() {
		return this.databaseId;
	}



	public  List<CarrierFilterListDTO> queryForCarrierList(List<String> listNameInsKeys) {
		SqlQuerySpec querySpec = QueryUtility.getQuerySpecWithKeys(QUERY_FOR_CARRIER_LISTNAME_INSTANCE_KEYS, listNameInsKeys);
		return queryForList(querySpec, carrierListCollection, CarrierFilterListDTO.class);

	}

	public List<AccountFilterListDTO> queryForAccountList(List<String> listNameInsKeys) {
		SqlQuerySpec querySpec = QueryUtility.getQuerySpecWithKeys(QUERY_FOR_ACCOUNT_LISTNAME_INSTANCE_KEYS, listNameInsKeys);
		return queryForList(querySpec, accountListCollection, AccountFilterListDTO.class);

	}

	public  List<GroupFilterListDTO> queryForGroupList(List<String> listNameInsKeys) {
		SqlQuerySpec querySpec = QueryUtility.getQuerySpecWithKeys(QUERY_FOR_GROUP_LISTNAME_INSTANCE_KEYS, listNameInsKeys);
		return queryForList(querySpec, groupListCollection, GroupFilterListDTO.class);

	}
	
	private static final String QUERY_FOR_CARRIER_LISTNAME_INSTANCE_KEYS = "select r.sourceSystemInstance, r.carrierId, r.listNameIdInsKey from root r where r.listNameIdInsKey in <INPUT_KEYS>";

	private static final String QUERY_FOR_ACCOUNT_LISTNAME_INSTANCE_KEYS = "select r.sourceSystemInstance, r.carrierId, r.accountId, r.listNameIdInsKey from root r where r.listNameIdInsKey in <INPUT_KEYS>";

	private static final String QUERY_FOR_GROUP_LISTNAME_INSTANCE_KEYS = "select r.sourceSystemInstance, r.carrierId, r.accountId, r.groupId, r.listNameIdInsKey from root r where r.listNameIdInsKey in <INPUT_KEYS>";



}
