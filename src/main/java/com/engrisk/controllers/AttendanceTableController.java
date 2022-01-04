package com.engrisk.controllers;

import com.engrisk.api.Api;
import com.engrisk.dto.Attendance.ResponseAttendanceDTO;
import com.engrisk.dto.Attendance.UpdateAttendanceResultDTO;
import com.engrisk.dto.Exam.ResponseCandidateRef;
import com.engrisk.utils.AlertUtils;
import com.engrisk.utils.DateUtils;
import com.engrisk.utils.Mapper;
import com.engrisk.utils.NumberUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.exceptions.UnirestException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import lombok.SneakyThrows;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class AttendanceTableController implements Initializable {
    @FXML
    public TableView<ResponseAttendanceDTO> table;

    @FXML
    public TableColumn<ResponseAttendanceDTO, String> candidateNameColumn,
            candidatePhoneColumn,
            candidateCodeColumn,
            examNameColumn,
            examTypeColumn,
            examDateColumn,
            listeningColumn,
            speakingColumn,
            readingColumn,
            writingColumn;
    @FXML
    public Button searchButton, saveButton;

    @FXML
    TextField nameTextField, phoneTextField;

    // Data got from server
    ArrayList<ResponseAttendanceDTO> data = new ArrayList<>();

    // Data filtered by search bar
    ObservableList<ResponseAttendanceDTO> filteredData = FXCollections.observableArrayList();

    @FXML
    public void onSearchSubmit() {
        String nameSearch = nameTextField.getText().toLowerCase();
        String phoneSearch = phoneTextField.getText();

        if (nameSearch.isEmpty() && phoneSearch.isEmpty()) {
            filteredData.setAll(data);
            return;
        }

        Predicate<ResponseAttendanceDTO> predicate = attendance -> {
            ResponseCandidateRef candidate = attendance.getCandidate();
            String name = candidate.getName().toLowerCase();
            String phone = candidate.getPhone();

            if (phoneSearch.isEmpty()) {
                return name.contains(nameSearch);
            }

            if (nameSearch.isEmpty()) {
                return phone.contains(phoneSearch);
            }

            return name.contains(nameSearch) && phone.contains(phoneSearch);
        };

        filteredData.setAll(data.stream()
                                .filter(predicate)
                                .collect(Collectors.toList()));
    }

    @FXML
    public void onEditClick() {
        ResponseAttendanceDTO selected = table.getSelectionModel().getSelectedItem();

        if (selected == null) {
            AlertUtils.showWarning("Hãy chọn dòng muốn sửa");
            return;
        }

        // Open edit attendance view (only edit exam points)
    }

    public void initTable() {
        candidateCodeColumn.setCellValueFactory(cell -> {
            SimpleStringProperty property = new SimpleStringProperty();
            property.setValue(cell.getValue().getCode());
            return property;
        });

        candidateNameColumn.setCellValueFactory(cell -> {
            SimpleStringProperty property = new SimpleStringProperty();
            property.setValue(cell.getValue().getCandidate().getName());
            return property;
        });

        candidatePhoneColumn.setCellValueFactory(cell -> {
            SimpleStringProperty property = new SimpleStringProperty();
            property.setValue(cell.getValue().getCandidate().getPhone());
            return property;
        });

        examNameColumn.setCellValueFactory(cell -> {
            SimpleStringProperty property = new SimpleStringProperty();
            property.setValue(cell.getValue().getExam().getName());
            return property;
        });

        examTypeColumn.setCellValueFactory(cell -> {
            SimpleStringProperty property = new SimpleStringProperty();
            property.setValue(cell.getValue().getExam().getType().name());
            return property;
        });

        examDateColumn.setCellValueFactory(cell -> {
            SimpleStringProperty property = new SimpleStringProperty();
            property.setValue(DateUtils.format(cell.getValue().getExam().getExamDate()));
            return property;
        });

        listeningColumn.setCellValueFactory(cell -> {
            SimpleStringProperty property = new SimpleStringProperty();
            property.setValue(cell.getValue().getListening() != null ? cell.getValue().getListening().toString() : "");
            return property;
        });
        listeningColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        listeningColumn.setMinWidth(50);

        speakingColumn.setCellValueFactory(cell -> {
            SimpleStringProperty property = new SimpleStringProperty();
            property.setValue(cell.getValue().getSpeaking() != null ? cell.getValue().getSpeaking().toString() : "");
            return property;
        });
        speakingColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        speakingColumn.setMinWidth(50);

        readingColumn.setCellValueFactory(cell -> {
            SimpleStringProperty property = new SimpleStringProperty();
            property.setValue(cell.getValue().getReading() != null ? cell.getValue().getReading().toString() : "");
            return property;
        });
        readingColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        readingColumn.setMinWidth(50);

        writingColumn.setCellValueFactory(cell -> {
            SimpleStringProperty property = new SimpleStringProperty();
            property.setValue(cell.getValue().getWriting() != null ? cell.getValue().getWriting().toString() : "");
            return property;
        });
        writingColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        writingColumn.setMinWidth(50);

        listeningColumn.setOnEditCommit((TableColumn.CellEditEvent<ResponseAttendanceDTO, String> event) -> {
            TablePosition<ResponseAttendanceDTO, String> pos = event.getTablePosition();

            if (event.getOldValue().equals(event.getNewValue()) || (event.getOldValue().isEmpty() && event.getNewValue().isEmpty())) {
                table.refresh();
                return;
            }

            int row = pos.getRow();
            if (NumberUtils.isFloat(event.getNewValue())) {
                ResponseAttendanceDTO attendanceDTO = table.getItems().get(row);
                attendanceDTO.setListening(Float.valueOf(event.getNewValue()));

                for (ResponseAttendanceDTO responseAttendanceDTO : data) {
                    if (responseAttendanceDTO.getCandidate().getId().equals(attendanceDTO.getCandidate().getId())
                            && responseAttendanceDTO.getExam().getId().equals(attendanceDTO.getExam().getId())) {
                        data.remove(responseAttendanceDTO);
                        data.add(attendanceDTO);
                        return;
                    }
                }

                event.getTableView().getItems().get(row).setListening(Float.valueOf(event.getNewValue()));
                table.refresh();
                return;
            }

            event.getTableView().getItems().get(row).setListening(Float.valueOf(event.getOldValue()));
            table.refresh();
            AlertUtils.showWarning("Hãy nhập số");
        });

        speakingColumn.setOnEditCommit((TableColumn.CellEditEvent<ResponseAttendanceDTO, String> event) -> {
            TablePosition<ResponseAttendanceDTO, String> pos = event.getTablePosition();

            if (event.getOldValue().equals(event.getNewValue()) || (event.getOldValue().isEmpty() && event.getNewValue().isEmpty())) {
                table.refresh();
                return;
            }

            int row = pos.getRow();
            if (NumberUtils.isFloat(event.getNewValue())) {
                ResponseAttendanceDTO attendanceDTO = table.getItems().get(row);
                attendanceDTO.setSpeaking(Float.valueOf(event.getNewValue()));

                for (ResponseAttendanceDTO responseAttendanceDTO : data) {
                    if (responseAttendanceDTO.getCandidate().getId().equals(attendanceDTO.getCandidate().getId())
                            && responseAttendanceDTO.getExam().getId().equals(attendanceDTO.getExam().getId())) {
                        data.remove(responseAttendanceDTO);
                        data.add(attendanceDTO);
                        return;
                    }
                }

                event.getTableView().getItems().get(row).setSpeaking(Float.valueOf(event.getNewValue()));
                table.refresh();
                return;
            }

            event.getTableView().getItems().get(row).setSpeaking(Float.valueOf(event.getOldValue()));
            table.refresh();
            AlertUtils.showWarning("Hãy nhập số");
        });

        readingColumn.setOnEditCommit((TableColumn.CellEditEvent<ResponseAttendanceDTO, String> event) -> {
            TablePosition<ResponseAttendanceDTO, String> pos = event.getTablePosition();

            if (event.getOldValue().equals(event.getNewValue()) || (event.getOldValue().isEmpty() && event.getNewValue().isEmpty())) {
                table.refresh();
                return;
            }

            int row = pos.getRow();
            if (NumberUtils.isFloat(event.getNewValue())) {
                ResponseAttendanceDTO attendanceDTO = table.getItems().get(row);
                attendanceDTO.setReading(Float.valueOf(event.getNewValue()));

                for (ResponseAttendanceDTO responseAttendanceDTO : data) {
                    if (responseAttendanceDTO.getCandidate().getId().equals(attendanceDTO.getCandidate().getId())
                            && responseAttendanceDTO.getExam().getId().equals(attendanceDTO.getExam().getId())) {
                        data.remove(responseAttendanceDTO);
                        data.add(attendanceDTO);
                        return;
                    }
                }

                event.getTableView().getItems().get(row).setReading(Float.valueOf(event.getNewValue()));
                table.refresh();
                return;
            }

            event.getTableView().getItems().get(row).setReading(Float.valueOf(event.getOldValue()));
            table.refresh();
            AlertUtils.showWarning("Hãy nhập số");
        });

        writingColumn.setOnEditCommit((TableColumn.CellEditEvent<ResponseAttendanceDTO, String> event) -> {
            TablePosition<ResponseAttendanceDTO, String> pos = event.getTablePosition();

            if (event.getOldValue().equals(event.getNewValue()) || (event.getOldValue().isEmpty() && event.getNewValue().isEmpty())) {
                table.refresh();
                return;
            }

            int row = pos.getRow();
            if (NumberUtils.isFloat(event.getNewValue())) {
                ResponseAttendanceDTO attendanceDTO = table.getItems().get(row);
                attendanceDTO.setWriting(Float.valueOf(event.getNewValue()));

                for (ResponseAttendanceDTO responseAttendanceDTO : data) {
                    if (responseAttendanceDTO.getCandidate().getId().equals(attendanceDTO.getCandidate().getId())
                            && responseAttendanceDTO.getExam().getId().equals(attendanceDTO.getExam().getId())) {
                        data.remove(responseAttendanceDTO);
                        data.add(attendanceDTO);
                        return;
                    }
                }

                event.getTableView().getItems().get(row).setWriting(Float.valueOf(event.getNewValue()));
                table.refresh();
                return;
            }

            event.getTableView().getItems().get(row).setWriting((Float.valueOf(event.getOldValue())));
            table.refresh();
            AlertUtils.showWarning("Hãy nhập số");
        });

        table.setItems(filteredData);
    }

    // Submit on text field Enter
    public void initSearchTextFields() {
        nameTextField.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ENTER)) {
                onSearchSubmit();
            }
        });

        phoneTextField.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ENTER)) {
                onSearchSubmit();
            }
        });
    }

    public void onSaveClick() throws JsonProcessingException, UnirestException {
        for (ResponseAttendanceDTO attendanceDTO : data) {
            UpdateAttendanceResultDTO dto = new UpdateAttendanceResultDTO();
            dto.setCandidateId(attendanceDTO.getCandidate().getId());
            dto.setExamId(attendanceDTO.getExam().getId());
            dto.setListening(attendanceDTO.getListening());
            dto.setSpeaking(attendanceDTO.getSpeaking());
            dto.setReading(attendanceDTO.getReading());
            dto.setWriting(attendanceDTO.getWriting());

            String request = Mapper.create()
                                   .writeValueAsString(dto);
            Api.put("attendance", request);
        }

        loadData();
    }

    public void loadData() throws UnirestException, JsonProcessingException {
        // Get data from server and set to data array and filtered data
        ResponseAttendanceDTO[] responseAttendanceDTOs;
        String response = Api.get("attendance");
        responseAttendanceDTOs = Mapper.create()
                                       .readValue(response, ResponseAttendanceDTO[].class);

        data.clear();
        filteredData.clear();
        data.addAll(List.of(responseAttendanceDTOs));
        filteredData.addAll(data);
        table.setItems(filteredData);
        table.refresh();
    }

    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initTable();
        initSearchTextFields();
        loadData();
    }
}