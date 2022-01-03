package com.engrisk.dto.Candidate;

import lombok.Data;

import java.util.List;

@Data
public class ResponseCandidateDTO extends UpdateCandidateDTO {
    private List<ResponseAttendanceRef> attendances;
}
