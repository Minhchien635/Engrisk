package com.engrisk.dto.Candidate;

import com.engrisk.models.Candidate;
import lombok.Data;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class ResponseCandidateDTO extends UpdateCandidateDTO {
    private List<ResponseAttendanceRef> attendances;

    public static ResponseCandidateDTO convert(Candidate candidate) {
        ResponseCandidateDTO res = (new ModelMapper()).map(candidate, ResponseCandidateDTO.class);
        if (candidate.getAttendances() != null)
            res.setAttendances(candidate.getAttendances().stream().map(a -> ResponseAttendanceRef.convert(a)).collect(Collectors.toList()));
        return res;
    }
}
