package com.engrisk.dto.Candidate;

import com.engrisk.enums.ExamType;
import com.engrisk.models.Exam;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class ResponseExamRef {
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date examDate;

    private ExamType type;

    private String name;

    private Long price;

    public static ResponseExamRef convert(Exam exam) {
        ResponseExamRef res = (new ModelMapper()).map(exam, ResponseExamRef.class);
        return res;
    }
}
