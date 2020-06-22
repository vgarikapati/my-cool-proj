package com.optum.micro.service.cag;

import static com.optum.micro.util.MemberSearchConstants.CAG_FILTER_ALL;
import static com.optum.micro.util.MemberSearchConstants.KEY_FIELD_DELIMITER;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.optum.micro.domain.CagmiKey;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.optum.micro.commons.constants.ExceptionConstants;
import com.optum.micro.commons.exception.InputValidationException;
import com.optum.micro.domain.cag.AccountFilterListDTO;
import com.optum.micro.domain.cag.CagFilterListDTO;
import com.optum.micro.domain.cag.CagFilterRequest;
import com.optum.micro.domain.cag.CagFilterResponse;
import com.optum.micro.domain.cag.CarrierFilterListDTO;
import com.optum.micro.domain.cag.GroupFilterListDTO;
import com.optum.micro.repositories.CagFilterDao;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class CagFilterServiceImpl implements CagFilterService {

	private CagFilterDao cagFilterDao;

	@Autowired
	CagFilterServiceImpl(CagFilterDao cagFilterDao) {
		this.cagFilterDao = cagFilterDao;
	}

	public CagFilterResponse filterByCagListName(CagFilterRequest cagFilterRequest) {
		String cagList = cagFilterRequest.getCagList();
		log.info("Enter CagFilter service with {}",cagFilterRequest);
		List<CagmiKey> cagmiKeys = cagFilterRequest.getCagmiKeys();
		Set<String> searchKeys;
		List<String> searchKeysList;
		List<CarrierFilterListDTO> carrierFilterList;
		List<AccountFilterListDTO> accountFilterList;
		List<GroupFilterListDTO> groupFilterList;
		Set<String> filters;
		log.info("Enter CagFilter service with {}",cagFilterRequest);
		switch (cagFilterRequest.getCagListType()) {
		case "C":
			searchKeys = buildSearchKeys(cagmiKeys, cagList, 2);
			searchKeysList = searchKeys.stream().collect(Collectors.toList());
			carrierFilterList= cagFilterDao.queryForCarrierList(searchKeysList);
			filters = carrierFilterList.stream().map(x -> StringUtils.join(ArrayUtils.toArray(x.getSourceSystemInstance(), x.getCarrierId()), KEY_FIELD_DELIMITER)).map(x -> x.replace("-*ALL", "")).collect(Collectors.toSet());
			break;
		case "A":
			searchKeys = buildSearchKeys(cagmiKeys, cagList, 3);
			searchKeysList = searchKeys.stream().collect(Collectors.toList());
			accountFilterList = cagFilterDao.queryForAccountList(searchKeysList);
			filters = accountFilterList.stream().map(x -> StringUtils.join(ArrayUtils.toArray(x.getSourceSystemInstance(), x.getCarrierId(), x.getAccountId()), KEY_FIELD_DELIMITER)).map(x -> x.replace("-*ALL", "")).collect(Collectors.toSet());
			break;
		case "G":
			searchKeys = buildSearchKeys(cagmiKeys, cagList, 4);
			searchKeysList = searchKeys.stream().collect(Collectors.toList());
			groupFilterList = cagFilterDao.queryForGroupList(searchKeysList);
			filters = groupFilterList.stream().map(x -> StringUtils.join(ArrayUtils.toArray(x.getSourceSystemInstance(),x.getCarrierId(), x.getAccountId(), x.getGroupId()), KEY_FIELD_DELIMITER)).map(x -> x.replace("-*ALL", "")).collect(Collectors.toSet());
			break;
		default:
			log.error("Invalid cagListType, should be 'C', 'A' or 'G'");
			throw new InputValidationException(ExceptionConstants.BAD_REQUEST,
					"Invalid cagListType in CAG Filter Request");
		}

		return doFilter(cagFilterRequest.getCagmiKeys(), filters, cagFilterRequest.getIncExcListIdentifier());
	}

	private Set<String> buildSearchKeys(List<CagmiKey> cagmiKeys, String cagList, int index) {
		Set<String> searchKeys = cagmiKeys.stream().map(cagmikey -> buildSearchKey(cagmikey,cagList,index)).collect(Collectors.toSet());
		Set<String> searchAllKeys = new HashSet<>();
		for (String searchKey : searchKeys) {
			for (int i = 0; i < index - 1; i++) {
				String wildCardSearchkey = searchKey.substring(0,
						StringUtils.ordinalIndexOf(searchKey, KEY_FIELD_DELIMITER, index - i))
						+ StringUtils.repeat(KEY_FIELD_DELIMITER + CAG_FILTER_ALL, i + 1);
				searchAllKeys.add(wildCardSearchkey);
			}
		}
		searchKeys.addAll(searchAllKeys);
		return searchKeys;
	}

	private String buildSearchKey (CagmiKey cagmiKey,String cagList,int index)
	{
		String searchKey = null;
		if (cagmiKey!=null)
		{
			switch (index)
			{
				case 2:
					searchKey=cagmiKey.getSourceSystemInstance()+KEY_FIELD_DELIMITER+cagList+KEY_FIELD_DELIMITER+cagmiKey.getCarrierId();
					break;
				case 3:
					searchKey=cagmiKey.getSourceSystemInstance()+KEY_FIELD_DELIMITER+cagList+KEY_FIELD_DELIMITER+cagmiKey.getCarrierId()
							+KEY_FIELD_DELIMITER+cagmiKey.getAccountId();
					break;
				case 4:
					searchKey=cagmiKey.getSourceSystemInstance()+KEY_FIELD_DELIMITER+cagList+KEY_FIELD_DELIMITER+cagmiKey.getCarrierId()
							+KEY_FIELD_DELIMITER+cagmiKey.getAccountId()+KEY_FIELD_DELIMITER+cagmiKey.getGroupId();
					break;
				default:
					log.error("Invalid Index");
			}
		}
		return searchKey;
	}


	private CagFilterResponse doFilter(List<CagmiKey> cagmiKeys, Set<String> filters, String filterType) {
		CagFilterResponse cagFilterResponse = new CagFilterResponse();
		Map<String, String> cagmiKeyMap = new HashMap<>();
		Set<String> matchCagmiKeys = new HashSet<>();
		for (CagmiKey cagmiKey : cagmiKeys) {
			if (cagmiKey==null)
			{
				continue;
			}
			String cagmiStringKey = cagmiKey.getCagmiKey();
			for (String filter : filters) {
				if (cagmiStringKey.startsWith(filter)) {
					matchCagmiKeys.add(cagmiStringKey);
					break;
				}
			}
		}
		Set<String> unmatchCagmiKeys = cagmiKeys.stream().filter(x -> !matchCagmiKeys.contains(x.getCagmiKey())).
			map(CagmiKey::getCagmiKey).collect(Collectors.toSet());
		switch (filterType) {
		case "I":
			for (String key : matchCagmiKeys) {
				cagmiKeyMap.put(key, "I");
			}
			break;
		case "E":
			for (String key : unmatchCagmiKeys) {
				cagmiKeyMap.put(key, "E");
			}
			break;
		case "R":
			for (String key : matchCagmiKeys) {
				cagmiKeyMap.put(key, "I");
			}
			for (String key : unmatchCagmiKeys) {
				cagmiKeyMap.put(key, "E");
			}
			break;
		default:
			log.error("Invalid filter type, should be 'I', 'E' or 'R'");
			throw new InputValidationException(ExceptionConstants.BAD_REQUEST,
					"Invalid filter type in CAG Filter Request");
		}
		cagFilterResponse.setCagmiKeys(cagmiKeyMap);
		log.info("Exit CagFilter service response with {}",cagFilterResponse);
		return cagFilterResponse;
	}
}
