package com.engrisk.dto.Room;

import com.engrisk.dto.Candidate.ResponseExamRef;
import lombok.Data;

@Data
public class ResponseRoomDTO {
    private String name;
    private ResponseExamRef exam;
}
