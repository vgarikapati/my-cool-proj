package com.optum.micro.domain;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Contact {

    @Size(max = 40, message ="INVALID_LENGTH_40")
    private String address1;
    @Size(max = 40, message ="INVALID_LENGTH_40")
    private String address2;
    @Size(max = 40, message ="INVALID_LENGTH_40")
    private String address3;
    @Size(max = 20, message ="INVALID_LENGTH_20")
    private String city;
    @Size(max = 2, message ="INVALID_LENGTH_2")
    private String state;
    @Size(max = 5, message ="INVALID_LENGTH_5")
    private String zip;
    @Size(max = 4, message ="INVALID_LENGTH_4")
    private String zip2;
    @Size(max = 2, message ="INVALID_LENGTH_2")
    private String zip3;
    @Size(max = 4, message ="INVALID_LENGTH_4")
    private String country;
    @Size(max = 10, message ="INVALID_LENGTH_10")
    private String phoneNumber;


}
