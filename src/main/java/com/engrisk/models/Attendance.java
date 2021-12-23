package com.engrisk.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Attendance implements Serializable {
    private AttendanceID id;

    private Exam exam;

    private Candidate candidate;

    private Room room;

    private String code;

    private Float listening;

    private Float speaking;

    private Float reading;

    private Float writing;

    public Attendance(Exam exam, Candidate candidate) {
        this.id = new AttendanceID(candidate.getId(), exam.getId());
        this.exam = exam;
        this.candidate = candidate;
    }
}