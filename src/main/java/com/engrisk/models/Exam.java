package com.engrisk.models;

import com.engrisk.utils.ExamType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Exam {
    private Long id;

    @ToString.Exclude
    private List<Room> rooms;

    @ToString.Exclude
    private List<Attendance> attendances;

    private Date examDate;

    private ExamType type;

    private String name;

    private Long price;

    public boolean isClose() {
        long differMiliSecond = this.examDate.getTime() - (new Date()).getTime();
        return TimeUnit.MILLISECONDS.toDays(differMiliSecond) < 5;
    }
}