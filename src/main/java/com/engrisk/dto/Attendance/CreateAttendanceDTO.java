package com.engrisk.dto.Attendance;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAttendanceDTO {
    private Long examId;
    private Long candidateId;
}
