package com.engrisk.dto.Candidate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCandidateDTO extends CreateCandidateDTO {
    private Long id;
}
