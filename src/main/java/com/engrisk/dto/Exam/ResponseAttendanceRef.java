package com.engrisk.dto.Exam;

import com.engrisk.models.Attendance;
import com.engrisk.models.AttendanceID;
import lombok.Data;
import org.modelmapper.ModelMapper;

@Data
public class ResponseAttendanceRef {
    private AttendanceID id;

    private ResponseCandidateRef candidate;

    private Float listening;
    private Float speaking;
    private Float reading;
    private Float writing;
    private String code;

    public static ResponseAttendanceRef convert(Attendance attendance) {
        ModelMapper modelMapper = new ModelMapper();
        ResponseAttendanceRef res = modelMapper.map(attendance, ResponseAttendanceRef.class);
        res.setCandidate(ResponseCandidateRef.convert(attendance.getCandidate()));
        return res;
    }
}
