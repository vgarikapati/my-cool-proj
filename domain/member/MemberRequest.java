package com.optum.micro.domain.member;

import com.optum.micro.commons.domain.fg.SearchInputPagination;
import com.optum.micro.domain.Contact;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberRequest {
    private List<String> cagmiKeys;
    private String gender;
    private Contact contact;
    private SearchInputPagination pagination;
}
