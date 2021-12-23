package com.engrisk.dto.Exam;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateExamDTO extends CreateExamDTO {
    private Long id;
}
