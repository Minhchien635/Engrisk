package com.engrisk.dto.Exam;

import com.engrisk.models.AttendanceID;
import lombok.Data;

@Data
public class ResponseAttendanceRef {
    private AttendanceID id;

    private ResponseCandidateRef candidate;

    private Float listening;
    private Float speaking;
    private Float reading;
    private Float writing;
    private String code;
}
