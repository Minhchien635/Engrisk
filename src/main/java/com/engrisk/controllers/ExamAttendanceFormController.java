package com.engrisk.controllers;

import com.engrisk.api.Api;
import com.engrisk.dto.Attendance.CreateAttendanceDTO;
import com.engrisk.dto.Attendance.ResponseAttendanceDTO;
import com.engrisk.dto.Candidate.ResponseCandidateDTO;
import com.engrisk.dto.Exam.ResponseAttendanceRef;
import com.engrisk.utils.AlertUtils;
import com.engrisk.utils.DateUtils;
import com.engrisk.utils.EnumUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.exceptions.UnirestException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import lombok.SneakyThrows;
import org.json.JSONObject;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ExamAttendanceFormController extends BaseFormController {
    public ExamFormController examFormController;

    @FXML
    TableView<ResponseCandidateDTO> candidateTableView;

    ObservableList<ResponseCandidateDTO> data = FXCollections.observableArrayList();

    @FXML
    public TableColumn<ResponseCandidateDTO, String> candidateNameCol,
            candidatePhoneCol,
            candidateEmailCol,
            candidateGenderCol,
            candidateBirthDateCol,
            candidateBirthPlaceCol,
            candidateCitizenIdNumberCol,
            candidateCitizenIdDateCol,
            candidateCitizenIdPlaceCol;

    @Override
    public void onSaveClick(ActionEvent event) throws JsonProcessingException, UnirestException {
        ResponseCandidateDTO selectedCandidate = candidateTableView.getSelectionModel().getSelectedItem();

        if (selectedCandidate == null) {
            AlertUtils.showWarning("Hãy chọn một thí sinh để thêm vào khóa thi.");
            return;
        }

        // Call api
        CreateAttendanceDTO dto = new CreateAttendanceDTO();
        dto.setExamId(examFormController.exam.getId());
        dto.setCandidateId(selectedCandidate.getId());

        String requestBody = new ObjectMapper().writeValueAsString(dto);
        JSONObject response = Api.post("attendance", requestBody);

        if (response.has("error")) {
            AlertUtils.showWarning("Đã có lỗi xảy ra");
            System.out.println(response.get("error"));
            return;
        }

        ResponseAttendanceDTO responseDTO = new ObjectMapper().readValue(response.toString(), ResponseAttendanceDTO.class);
        ResponseAttendanceRef examAttendance = new ResponseAttendanceRef();
        examAttendance.setId(responseDTO.getId());
        examAttendance.setCandidate(responseDTO.getCandidate());
        examAttendance.setCode(responseDTO.getCode());
        examAttendance.setListening(responseDTO.getListening());
        examAttendance.setReading(responseDTO.getReading());
        examAttendance.setSpeaking(responseDTO.getWriting());
        examAttendance.setWriting(responseDTO.getWriting());

        // Add new attendance to exam form
        examFormController.attendances.add(examAttendance);

        // Close window
        closeWindow(event);
    }

    @Override
    public void initFormValues() {
    }

    public void loadData() throws UnirestException, JsonProcessingException {
        // Candidates that are already in this exam
        List<Long> existedCandidateIds = examFormController.exam.getAttendances()
                                                                .stream()
                                                                .map(x -> x.getCandidate().getId())
                                                                .collect(Collectors.toList());

        // Get all candidates
        String response = Api.get("candidate");
        ResponseCandidateDTO[] candidates = new ObjectMapper().readValue(response, ResponseCandidateDTO[].class);

        // Only show candidates that aren't in this exam
        data.setAll(Arrays.stream(candidates)
                          .filter(x -> !existedCandidateIds.contains(x.getId()))
                          .collect(Collectors.toList()));
    }

    public void initCandidateTable() {
        candidateTableView.setItems(data);

        candidateNameCol.setCellValueFactory(data -> {
            SimpleStringProperty property = new SimpleStringProperty();
            property.setValue(data.getValue().getName());
            return property;
        });

        candidatePhoneCol.setCellValueFactory(data -> {
            SimpleStringProperty property = new SimpleStringProperty();
            property.setValue(data.getValue().getPhone());
            return property;
        });

        candidateEmailCol.setCellValueFactory(data -> {
            SimpleStringProperty property = new SimpleStringProperty();
            property.setValue(data.getValue().getEmail());
            return property;
        });

        candidateGenderCol.setCellValueFactory(data -> {
            SimpleStringProperty property = new SimpleStringProperty();
            property.setValue(EnumUtils.toString(data.getValue().getSex()));
            return property;
        });

        candidateBirthDateCol.setCellValueFactory(data -> {
            SimpleStringProperty property = new SimpleStringProperty();
            property.setValue(DateUtils.format(data.getValue().getBirthDate()));
            return property;
        });

        candidateBirthPlaceCol.setCellValueFactory(data -> {
            SimpleStringProperty property = new SimpleStringProperty();
            property.setValue(data.getValue().getBirthPlace());
            return property;
        });

        candidateCitizenIdNumberCol.setCellValueFactory(data -> {
            SimpleStringProperty property = new SimpleStringProperty();
            property.setValue(data.getValue().getCitizenId());
            return property;
        });

        candidateCitizenIdDateCol.setCellValueFactory(data -> {
            SimpleStringProperty property = new SimpleStringProperty();
            property.setValue(DateUtils.format(data.getValue().getCitizenIdDate()));
            return property;
        });

        candidateCitizenIdPlaceCol.setCellValueFactory(data -> {
            SimpleStringProperty property = new SimpleStringProperty();
            property.setValue(data.getValue().getCitizenIdPlace());
            return property;
        });
    }

    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initCandidateTable();
        loadData();
    }
}
