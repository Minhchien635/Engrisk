package com.engrisk.dto.Attendance;

import com.engrisk.enums.SexType;
import com.engrisk.models.Candidate;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class ResponseCandidateRef {
    private Long id;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date citizenIdDate;

    private SexType sex;

    private String name;
    private String phone;
    private String email;
    private String birthPlace;
    private String citizenId;
    private String citizenIdPlace;

    public static ResponseCandidateRef convert(Candidate candidate) {
        ResponseCandidateRef res = (new ModelMapper()).map(candidate, ResponseCandidateRef.class);
        return res;
    }
}
