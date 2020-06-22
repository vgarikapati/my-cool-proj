package com.optum.micro.redis.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.optum.micro.domain.member.das.DataAccessConfig;

@RepositoryRestResource(collectionResourceRel = "cache", path = "cache")
public interface DataAccessValidationCacheRepository  extends CrudRepository<DataAccessConfig,String>{

}
