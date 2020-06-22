package com.optum.micro.domain;

import com.optum.micro.commons.domain.fg.OptumRxDataPaginationResult;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;


@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class MemberSearchResponse extends OptumRxDataPaginationResult {

    private List<MemberSearchResultItem> memberItems;
}
