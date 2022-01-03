package com.engrisk.dto.Attendance;


import com.engrisk.dto.Candidate.ResponseExamRef;
import com.engrisk.dto.Exam.ResponseRoomRef;
import lombok.Data;

@Data
public class ResponseAttendanceDTO {
    private ResponseExamRef exam;

    private ResponseCandidateRef candidate;

    private ResponseRoomRef room;

    private Float listening;

    private Float speaking;

    private Float reading;

    private Float writing;

    private String code;
}
