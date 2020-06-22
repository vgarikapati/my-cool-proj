package com.optum.micro.service.crossreferenceid;

import com.optum.micro.domain.crossreferenceid.CrossReferenceIdDTO;
import com.optum.micro.domain.crossreferenceid.CrossReferenceIdRequest;
import com.optum.micro.domain.crossreferenceid.CrossReferenceIdResponse;
import com.optum.micro.domain.crossreferenceid.CrossReferenceMember;
import com.optum.micro.repositories.CrossReferenceIdRepository;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Log4j2
public class CrossReferenceIdServiceImpl implements CrossReferenceIdService {


    private CrossReferenceIdRepository crossReferenceIdRepository;

    @Autowired
    CrossReferenceIdServiceImpl(CrossReferenceIdRepository crossReferenceIdRepository)
    {
        this.crossReferenceIdRepository = crossReferenceIdRepository;
    }


    public CrossReferenceIdResponse searchNewMemberIds(CrossReferenceIdRequest request) {
        CrossReferenceIdResponse response = new CrossReferenceIdResponse();
        List<CrossReferenceIdDTO> crossReferenceIdDTOS;
        if (StringUtils.isBlank(request.getSourceSystemInstance())) {
            log.info("Looking for new member id with mxrMemberId: " + request.getMxrMemberId());
            crossReferenceIdDTOS = crossReferenceIdRepository.findByMxrMemberId(request.getMxrMemberId());
        } else {
            log.info("Looking for new member id with sourceSystemInstance/mxrMemberId: {}/{}",
                    request.getSourceSystemInstance(), request.getMxrMemberId());
            crossReferenceIdDTOS = crossReferenceIdRepository.findBySourceSystemInstanceAndMxrMemberId(
                    request.getSourceSystemInstance(), request.getMxrMemberId());
        }
        Set<CrossReferenceMember> crossReferenceIds = crossReferenceIdDTOS.stream()
                .map(x -> new CrossReferenceMember(x.getSourceSystemInstance(), x.getNewMemberId()))
                .collect(Collectors.toSet());
        response.setNewMembers(crossReferenceIds);
        return response;
    }
}
