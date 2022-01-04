package com.engrisk.dto.Room;

import com.engrisk.dto.Candidate.ResponseAttendanceRef;
import com.engrisk.dto.Candidate.ResponseExamRef;
import lombok.Data;

import java.util.List;

@Data
public class ResponseRoomDTO {
    private String name;
    private ResponseExamRef exam;
    private List<ResponseAttendanceRef> attendances;
}
