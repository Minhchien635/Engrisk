package com.engrisk.dto.Exam;

import com.engrisk.models.Exam;
import lombok.Data;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class ResponseExamDTO extends UpdateExamDTO {
    private List<ResponseAttendanceRef> attendances;
    private List<ResponseRoomRef> rooms;

    public static ResponseExamDTO convert(Exam exam) {
        ResponseExamDTO res = (new ModelMapper()).map(exam, ResponseExamDTO.class);
        if (exam.getAttendances() != null)
            res.setAttendances(exam.getAttendances().stream().map(a -> ResponseAttendanceRef.convert(a)).collect(Collectors.toList()));
        if (exam.getRooms() != null)
            res.setRooms(exam.getRooms().stream().map(r -> ResponseRoomRef.convert(r)).collect(Collectors.toList()));
        return res;
    }
}
