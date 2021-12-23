package com.engrisk.dto.Attendance;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAttendanceResultDTO extends CreateAttendanceDTO {
    private Float listening;
    private Float speaking;
    private Float reading;
    private Float writing;
}
