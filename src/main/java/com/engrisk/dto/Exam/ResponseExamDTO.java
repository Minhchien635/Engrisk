package com.engrisk.dto.Exam;

import lombok.Data;

import java.util.List;

@Data
public class ResponseExamDTO extends UpdateExamDTO {
    private List<ResponseAttendanceRef> attendances;
    private List<ResponseRoomRef> rooms;
}
