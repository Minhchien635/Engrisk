package com.engrisk.dto.Exam;

import com.engrisk.enums.SexType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class ResponseCandidateRef {
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date citizenIdDate;

    private SexType sex;

    private String name;

    private String phone;

    private String email;

    private String birthPlace;

    private String citizenId;

    private String citizenIdPlace;
}
