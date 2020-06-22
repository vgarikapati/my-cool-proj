package com.optum.micro.repositories;

import com.microsoft.azure.spring.data.cosmosdb.repository.CosmosRepository;
import com.optum.micro.domain.crossreferenceid.CrossReferenceIdDTO;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
@RepositoryRestResource(collectionResourceRel = "cross_ref", path = "cross_ref")
public interface CrossReferenceIdRepository extends CosmosRepository<CrossReferenceIdDTO, String> {
    List<CrossReferenceIdDTO> findByMxrMemberId(String mxrMemberId);
    List<CrossReferenceIdDTO> findBySourceSystemInstanceAndMxrMemberId(String sourceSystemInstance, String mxrMemberId);
}
