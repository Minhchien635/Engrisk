package com.engrisk.controllers;

import com.engrisk.api.CallApi;
import com.engrisk.dto.Attendance.ResponseAttendanceDTO;
import com.engrisk.dto.Attendance.UpdateAttendanceResultDTO;
import com.engrisk.dto.Room.ResponseRoomDTO;
import com.engrisk.utils.AlertUtils;
import com.engrisk.utils.NumberUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.exceptions.UnirestException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static com.engrisk.utils.WindowUtils.closeWindow;

public class RoomFormController implements Initializable {

    public ResponseRoomDTO room = new ResponseRoomDTO();

    ArrayList<ResponseAttendanceDTO> attendances = new ArrayList<>();

    ObservableList<ResponseAttendanceDTO> attendanceObList = FXCollections.observableArrayList();

    @FXML
    private TextField nameTextField;

    @FXML
    private TableView<ResponseAttendanceDTO> tableAttendance;

    @FXML
    private Button saveButton;

    @FXML
    private TableColumn<ResponseAttendanceDTO, String> idColumn, nameColumn, listenColumn, speakColumn, readColumn, writeColumn;

    public void onSaveClick(ActionEvent event) throws JsonProcessingException, UnirestException {
        for (ResponseAttendanceDTO attendanceDTO : attendances) {
            UpdateAttendanceResultDTO dto = new UpdateAttendanceResultDTO();
            dto.setCandidateId(attendanceDTO.getCandidate().getId());
            dto.setExamId(attendanceDTO.getExam().getId());
            dto.setListening(attendanceDTO.getListening());
            dto.setSpeaking(attendanceDTO.getSpeaking());
            dto.setReading(attendanceDTO.getReading());
            dto.setWriting(attendanceDTO.getWriting());

            ObjectMapper mapper = new ObjectMapper();
            String request = mapper.writeValueAsString(dto);
            CallApi.put("attendance", request);
        }
        closeWindow(event);
    }

    public void initReadOnly() {
        nameTextField.setEditable(false);
        tableAttendance.setEditable(false);
        idColumn.setEditable(false);
        nameColumn.setEditable(false);
        saveButton.setManaged(false);
    }

    public void onAddCoreClick(ActionEvent e) {
        tableAttendance.setEditable(true);
        saveButton.setManaged(true);
    }

    public void loadData() throws UnirestException, JsonProcessingException {
        ResponseAttendanceDTO[] responseAttendanceDTOs;
        String response = CallApi.get("attendance");
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        responseAttendanceDTOs = mapper.readValue(response, ResponseAttendanceDTO[].class);

        ArrayList<ResponseAttendanceDTO> attendancesOfRoom;
        attendancesOfRoom = (ArrayList<ResponseAttendanceDTO>) Arrays.stream(responseAttendanceDTOs)
                .filter(p -> p.getRoom().getName().equals(room.getName()))
                .collect(Collectors.toList());

        attendances.clear();
        attendanceObList.clear();
        attendances.addAll(attendancesOfRoom);
        attendanceObList.setAll(attendances);
        tableAttendance.refresh();
    }

    public void initTable() throws UnirestException, JsonProcessingException {
        idColumn.setCellValueFactory(cell -> {
            SimpleStringProperty property = new SimpleStringProperty();
            property.setValue(cell.getValue().getCode());
            return property;
        });

        nameColumn.setCellValueFactory(cell -> {
            SimpleStringProperty property = new SimpleStringProperty();
            property.setValue(cell.getValue().getCandidate().getName());
            return property;
        });

        listenColumn.setCellValueFactory(cell -> {
            SimpleStringProperty property = new SimpleStringProperty();
            property.setValue(cell.getValue().getListening() != null ? cell.getValue().getListening().toString() : "");
            return property;
        });
        listenColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        listenColumn.setMinWidth(50);

        speakColumn.setCellValueFactory(cell -> {
            SimpleStringProperty property = new SimpleStringProperty();
            property.setValue(cell.getValue().getSpeaking() != null ? cell.getValue().getSpeaking().toString() : "");
            return property;
        });
        speakColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        speakColumn.setMinWidth(50);

        readColumn.setCellValueFactory(cell -> {
            SimpleStringProperty property = new SimpleStringProperty();
            property.setValue(cell.getValue().getReading() != null ? cell.getValue().getReading().toString() : "");
            return property;
        });
        readColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        readColumn.setMinWidth(50);

        writeColumn.setCellValueFactory(cell -> {
            SimpleStringProperty property = new SimpleStringProperty();
            property.setValue(cell.getValue().getWriting() != null ? cell.getValue().getWriting().toString() : "");
            return property;
        });
        writeColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        writeColumn.setMinWidth(50);

        listenColumn.setOnEditCommit((TableColumn.CellEditEvent<ResponseAttendanceDTO, String> event) -> {
            TablePosition<ResponseAttendanceDTO, String> pos = event.getTablePosition();

            if (event.getOldValue() == event.getNewValue() || (event.getOldValue().isEmpty() && event.getNewValue().isEmpty())) {
                tableAttendance.refresh();
                return;
            }

            int row = pos.getRow();
            if (NumberUtils.isFloat(event.getNewValue())) {
                ResponseAttendanceDTO attendanceDTO = tableAttendance.getItems().get(row);
                attendanceDTO.setListening(Float.valueOf(event.getNewValue()));

                for (ResponseAttendanceDTO responseAttendanceDTO : attendances) {
                    if (responseAttendanceDTO.getCandidate().getId().equals(attendanceDTO.getCandidate().getId())
                            && responseAttendanceDTO.getExam().getId().equals(attendanceDTO.getExam().getId())) {
                        attendances.remove(responseAttendanceDTO);
                        attendances.add(attendanceDTO);
                        return;
                    }
                }

                event.getTableView().getItems().get(row).setListening(Float.valueOf(event.getNewValue()));
                tableAttendance.refresh();
                return;
            }

            event.getTableView().getItems().get(row).setListening(Float.valueOf(event.getOldValue()));
            tableAttendance.refresh();
            AlertUtils.showWarning("Hãy nhập số");
        });

        speakColumn.setOnEditCommit((TableColumn.CellEditEvent<ResponseAttendanceDTO, String> event) -> {
            TablePosition<ResponseAttendanceDTO, String> pos = event.getTablePosition();

            if (event.getOldValue() == event.getNewValue() || (event.getOldValue().isEmpty() && event.getNewValue().isEmpty())) {
                tableAttendance.refresh();
                return;
            }

            int row = pos.getRow();
            if (NumberUtils.isFloat(event.getNewValue())) {
                ResponseAttendanceDTO attendanceDTO = tableAttendance.getItems().get(row);
                attendanceDTO.setSpeaking(Float.valueOf(event.getNewValue()));

                for (ResponseAttendanceDTO responseAttendanceDTO : attendances) {
                    if (responseAttendanceDTO.getCandidate().getId().equals(attendanceDTO.getCandidate().getId())
                            && responseAttendanceDTO.getExam().getId().equals(attendanceDTO.getExam().getId())) {
                        attendances.remove(responseAttendanceDTO);
                        attendances.add(attendanceDTO);
                        return;
                    }
                }

                event.getTableView().getItems().get(row).setSpeaking(Float.valueOf(event.getNewValue()));
                tableAttendance.refresh();
                return;
            }

            event.getTableView().getItems().get(row).setSpeaking(Float.valueOf(event.getOldValue()));
            tableAttendance.refresh();
            AlertUtils.showWarning("Hãy nhập số");
        });

        readColumn.setOnEditCommit((TableColumn.CellEditEvent<ResponseAttendanceDTO, String> event) -> {
            TablePosition<ResponseAttendanceDTO, String> pos = event.getTablePosition();

            if (event.getOldValue() == event.getNewValue() || (event.getOldValue().isEmpty() && event.getNewValue().isEmpty())) {
                tableAttendance.refresh();
                return;
            }

            int row = pos.getRow();
            if (NumberUtils.isFloat(event.getNewValue())) {
                ResponseAttendanceDTO attendanceDTO = tableAttendance.getItems().get(row);
                attendanceDTO.setReading(Float.valueOf(event.getNewValue()));

                for (ResponseAttendanceDTO responseAttendanceDTO : attendances) {
                    if (responseAttendanceDTO.getCandidate().getId().equals(attendanceDTO.getCandidate().getId())
                            && responseAttendanceDTO.getExam().getId().equals(attendanceDTO.getExam().getId())) {
                        attendances.remove(responseAttendanceDTO);
                        attendances.add(attendanceDTO);
                        return;
                    }
                }

                event.getTableView().getItems().get(row).setReading(Float.valueOf(event.getNewValue()));
                tableAttendance.refresh();
                return;
            }

            event.getTableView().getItems().get(row).setReading(Float.valueOf(event.getOldValue()));
            tableAttendance.refresh();
            AlertUtils.showWarning("Hãy nhập số");
        });

        writeColumn.setOnEditCommit((TableColumn.CellEditEvent<ResponseAttendanceDTO, String> event) -> {
            TablePosition<ResponseAttendanceDTO, String> pos = event.getTablePosition();

            if (event.getOldValue() == event.getNewValue() || (event.getOldValue().isEmpty() && event.getNewValue().isEmpty())) {
                tableAttendance.refresh();
                return;
            }

            int row = pos.getRow();
            if (NumberUtils.isFloat(event.getNewValue())) {
                ResponseAttendanceDTO attendanceDTO = tableAttendance.getItems().get(row);
                attendanceDTO.setWriting(Float.valueOf(event.getNewValue()));

                for (ResponseAttendanceDTO responseAttendanceDTO : attendances) {
                    if (responseAttendanceDTO.getCandidate().getId().equals(attendanceDTO.getCandidate().getId())
                            && responseAttendanceDTO.getExam().getId().equals(attendanceDTO.getExam().getId())) {
                        attendances.remove(responseAttendanceDTO);
                        attendances.add(attendanceDTO);
                        return;
                    }
                }

                event.getTableView().getItems().get(row).setWriting(Float.valueOf(event.getNewValue()));
                tableAttendance.refresh();
                return;
            }

            event.getTableView().getItems().get(row).setWriting(Float.valueOf(event.getOldValue()));
            tableAttendance.refresh();
            AlertUtils.showWarning("Hãy nhập số");
        });

        loadData();
        tableAttendance.setItems(attendanceObList);
    }

    public void initFormValues() {
        nameTextField.setText(room.getName());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initFormValues();
        try {
            initTable();
        } catch (UnirestException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        initReadOnly();
    }
}
