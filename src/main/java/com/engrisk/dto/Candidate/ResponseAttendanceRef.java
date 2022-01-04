package com.engrisk.dto.Candidate;

import com.engrisk.dto.Exam.ResponseCandidateRef;
import lombok.Data;

@Data
public class ResponseAttendanceRef {
    private ResponseCandidateRef candidate;

    private Float listening;

    private Float speaking;

    private Float reading;

    private Float writing;

    private String code;
}
