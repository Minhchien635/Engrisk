package com.engrisk.dto.Candidate;

import lombok.Data;

@Data
public class ResponseAttendanceRef {
    private ResponseExamRef exam;

    private Float listening;

    private Float speaking;

    private Float reading;

    private Float writing;

    private String code;
}
