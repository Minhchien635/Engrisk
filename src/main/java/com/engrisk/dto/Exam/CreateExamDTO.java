package com.engrisk.dto.Exam;

import com.engrisk.enums.ExamType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateExamDTO {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    public Date examDate;

    public ExamType type;

    public String name;

    public Long price;
}
