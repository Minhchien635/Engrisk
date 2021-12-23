package com.engrisk.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class AttendanceID implements Serializable {
    private Long candidateId;

    private Long examId;

    public AttendanceID(Long candidateId, Long examId) {
        this.candidateId = candidateId;
        this.examId = examId;
    }

}
