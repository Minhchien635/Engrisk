package com.engrisk.dto.Exam;

import com.engrisk.models.Room;
import lombok.Data;
import org.modelmapper.ModelMapper;

@Data
public class ResponseRoomRef {
    private Long id;

    private String name;

    public static ResponseRoomRef convert(Room room) {
        return (new ModelMapper()).map(room, ResponseRoomRef.class);
    }
}
