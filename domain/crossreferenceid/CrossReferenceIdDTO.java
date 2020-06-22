package com.optum.micro.domain.crossreferenceid;

import com.microsoft.azure.spring.data.cosmosdb.core.mapping.Document;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@ToString
@Document(collection = "consumer_member_cross_reference")
@AllArgsConstructor
@NoArgsConstructor
public class CrossReferenceIdDTO {
    @Id
    private String id;
    private String sourceSystemInstance;
    private String newMemberId;
    private String mxrMemberId;
}
