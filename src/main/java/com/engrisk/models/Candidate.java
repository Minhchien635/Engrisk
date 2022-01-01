package com.engrisk.models;

import com.engrisk.enums.SexType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Candidate {
    private Long id;

    @ToString.Exclude
    private List<Attendance> attendances;

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

}