package com.optum.micro.service.cag;

import com.optum.micro.commons.exception.CustomHttpServerErrorException;
import com.optum.micro.commons.exception.DataAccessException;
import com.optum.micro.domain.cag.CagDTO;
import com.optum.micro.domain.cag.CagName;
import com.optum.micro.domain.cag.CagNameRequest;
import com.optum.micro.domain.cag.CagNameResponse;
import com.optum.micro.repositories.CagRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Log4j2
public class CagDetailServiceImpl  implements CagDetailService {

    private CagRepository cagRepository;


    @Autowired
    CagDetailServiceImpl(CagRepository cagRepository)
    {
        this.cagRepository = cagRepository;
    }

    public CagNameResponse getCagNames(CagNameRequest request)
    {
        log.info(" Enter into CagNameService with CagNameRequest: {}",request);
        CagNameResponse response = new CagNameResponse();
        List<CagDTO> cagDTOS= null;
        try {
            cagDTOS=cagRepository.findByCagiKeyIn(request.getCagiKeys());
        }catch (DataAccessException e) {
            log.error("Exception occured in Service call exception:", e);
            throw new CustomHttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR,"CAG data access error");
        }
        Map<String,CagName> cagNameMap= cagDTOS.stream().map(cag-> {return toCagName(cag);})
                .collect(Collectors.toMap(CagName::getCagiKey,Function.identity()));
        response.setCagNameMap(cagNameMap);
        log.info(" Exit CagNameService with CagNameResponse : {}",response);
        return response;
    }

    private CagName toCagName(CagDTO cagDTO)
    {
        CagName cagName = new CagName();
        BeanUtils.copyProperties(cagDTO,cagName);
        return cagName;
    }


}
