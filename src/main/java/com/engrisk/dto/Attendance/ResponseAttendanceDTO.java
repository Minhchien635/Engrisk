package com.engrisk.dto.Attendance;


import com.engrisk.dto.Candidate.ResponseExamRef;
import com.engrisk.dto.Exam.ResponseRoomRef;
import com.engrisk.models.Attendance;
import com.engrisk.models.AttendanceID;
import lombok.Data;
import org.modelmapper.ModelMapper;

@Data
public class ResponseAttendanceDTO {

    private AttendanceID id;

    private ResponseExamRef exam;
    private ResponseCandidateRef candidate;
    private ResponseRoomRef room;

    private Float listening;
    private Float speaking;
    private Float reading;
    private Float writing;
    private String code;

    public static ResponseAttendanceDTO convert(Attendance attendance) {
        ResponseAttendanceDTO res = (new ModelMapper()).map(attendance, ResponseAttendanceDTO.class);
        res.setCandidate(ResponseCandidateRef.convert(attendance.getCandidate()));
        res.setExam(ResponseExamRef.convert(attendance.getExam()));
        if (attendance.getRoom() != null)
            res.setRoom(ResponseRoomRef.convert(attendance.getRoom()));
        if (attendance.getRoom() != null)
            res.setRoom(ResponseRoomRef.convert(attendance.getRoom()));
        return res;
    }
}
