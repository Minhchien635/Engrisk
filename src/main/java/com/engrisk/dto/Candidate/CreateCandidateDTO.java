package com.engrisk.dto.Candidate;

import com.engrisk.enums.SexType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCandidateDTO {

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date citizenIdDate;

    private String citizenId;

    private String name;

    private String phone;

    private String email;

    private String birthPlace;

    private String citizenIdPlace;

    private SexType sex;
}
