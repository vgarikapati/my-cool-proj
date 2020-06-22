package com.optum.micro.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Demographics {

    private String gender;
    private String dateOfBirth;
    private String preferredLanguage;
}
