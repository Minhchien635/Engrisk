package com.engrisk.dto.Room;

import com.engrisk.dto.Candidate.ResponseExamRef;
import com.engrisk.models.Room;
import lombok.Data;
import org.modelmapper.ModelMapper;

@Data
public class ResponseRoomDTO {
    private String name;
    private ResponseExamRef exam;

    public static ResponseRoomDTO convert(Room room) {
        ResponseRoomDTO res = (new ModelMapper()).map(room, ResponseRoomDTO.class);
        res.setExam(ResponseExamRef.convert(room.getExam()));
        return res;
    }
}
