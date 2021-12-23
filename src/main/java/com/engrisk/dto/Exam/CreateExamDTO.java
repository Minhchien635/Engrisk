package com.engrisk.dto.Exam;

import com.engrisk.utils.ExamType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateExamDTO {
    private Date examDate;

    private ExamType type;

    private String name;

    private Long price;
}
