package com.optum.micro.domain.plan;

import com.optum.micro.commons.domain.fg.OptumRxDataRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class PlanSearchByKeysRequest extends OptumRxDataRequest {
    @NotEmpty(message = "NULL_EMPTY")
    private List <@NotEmpty(message = "NULL_EMPTY")String> planKeys;
}
