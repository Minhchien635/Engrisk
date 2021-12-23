package com.engrisk.dto.Candidate;

import com.engrisk.enums.SexType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCandidateDTO {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date citizenIdDate;

    private String citizenId;

    private String name;
    private String phone;
    private String email;
    private String birthPlace;
    private String citizenIdPlace;

    private SexType sex;
}
