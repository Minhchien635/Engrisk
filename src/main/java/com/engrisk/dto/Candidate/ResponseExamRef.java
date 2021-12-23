package com.engrisk.dto.Candidate;

import com.engrisk.models.Exam;
import com.engrisk.enums.ExamType;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class ResponseExamRef {
    private Long id;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date examDate;

    private ExamType type;

    private String name;
    private Long price;

    public static ResponseExamRef convert(Exam exam) {
        ResponseExamRef res = (new ModelMapper()).map(exam, ResponseExamRef.class);
        return res;
    }
}
