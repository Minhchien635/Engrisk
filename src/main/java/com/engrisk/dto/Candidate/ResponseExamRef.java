package com.engrisk.dto.Candidate;

import com.engrisk.enums.ExamType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class ResponseExamRef {
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date examDate;

    private ExamType type;

    private String name;

    private Long price;
}
