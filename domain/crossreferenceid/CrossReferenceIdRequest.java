package com.optum.micro.domain.crossreferenceid;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class CrossReferenceIdRequest {
    private String sourceSystemInstance;
    private String mxrMemberId;

    public CrossReferenceIdRequest(String sourceSystemInstance,String mxrMemberId)
    {
        this.mxrMemberId = mxrMemberId;
        this.sourceSystemInstance = sourceSystemInstance;
    }
}
