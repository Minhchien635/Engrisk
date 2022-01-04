package com.engrisk.controllers;

import com.engrisk.api.Api;
import com.engrisk.dto.Attendance.UpdateAttendanceResultDTO;
import com.engrisk.dto.Candidate.ResponseAttendanceRef;
import com.engrisk.dto.Room.ResponseRoomDTO;
import com.engrisk.utils.AlertUtils;
import com.engrisk.utils.Mapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.exceptions.UnirestException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import lombok.SneakyThrows;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class RoomFormController extends BaseFormController {
    public ResponseRoomDTO room = new ResponseRoomDTO();
    @FXML
    public Label roomLabel;
    @FXML
    public TableView<ResponseAttendanceRef> attendanceTableView;
    @FXML
    public TableColumn<ResponseAttendanceRef, String> idColumn, nameColumn, listenColumn, speakColumn, readColumn, writeColumn;
    ObservableList<ResponseAttendanceRef> attendances = FXCollections.observableArrayList();

    public void onSaveClick(Event event) throws JsonProcessingException, UnirestException {
        ArrayList<UpdateAttendanceResultDTO> updateAttendanceResultDTOS = new ArrayList<>();
        for (ResponseAttendanceRef attendanceDTO : attendances) {
            UpdateAttendanceResultDTO dto = new UpdateAttendanceResultDTO();
            dto.setCandidateId(attendanceDTO.getCandidate().getId());
            dto.setListening(attendanceDTO.getListening());
            dto.setSpeaking(attendanceDTO.getSpeaking());
            dto.setReading(attendanceDTO.getReading());
            dto.setWriting(attendanceDTO.getWriting());

            updateAttendanceResultDTOS.add(dto);
        }

        String request = Mapper.create().writeValueAsString(updateAttendanceResultDTOS);
        Api.put("room/{id}/updateResults", request, room.getId());
        closeWindow(event);
    }

    @Override
    public void initFormValues() {
        roomLabel.setText("Phòng: " + room.getName());
        attendances.addAll(room.getAttendances());
    }

    public void initTable() {
        attendanceTableView.setItems(attendances);

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
        listenColumn.setOnEditCommit((TableColumn.CellEditEvent<ResponseAttendanceRef, String> event) -> {
            try {
                // Parse input to float
                float floatValue = Float.parseFloat(event.getNewValue().trim());

                if (floatValue < 0 || floatValue > 10) {
                    AlertUtils.showWarning("Điểm phải nằm trong khoảng 0 tới 10.");
                    attendanceTableView.refresh();
                    return;
                }

                // Update table data
                ResponseAttendanceRef row = event.getRowValue();
                row.setListening(floatValue);
                attendanceTableView.refresh();
            } catch (NumberFormatException e) {
                // Alert
                AlertUtils.showWarning("Chỉ được phép nhập số thập phân trong khoảng 0 tới 10.");
                attendanceTableView.refresh();
            }
        });

        speakColumn.setCellValueFactory(cell -> {
            SimpleStringProperty property = new SimpleStringProperty();
            property.setValue(cell.getValue().getSpeaking() != null ? cell.getValue().getSpeaking().toString() : "");
            return property;
        });
        speakColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        speakColumn.setMinWidth(50);
        speakColumn.setOnEditCommit((TableColumn.CellEditEvent<ResponseAttendanceRef, String> event) -> {
            try {
                // Parse input to float
                float floatValue = Float.parseFloat(event.getNewValue().trim());

                if (floatValue < 0 || floatValue > 10) {
                    AlertUtils.showWarning("Điểm phải nằm trong khoảng 0 tới 10.");
                    attendanceTableView.refresh();
                    return;
                }

                // Update table data
                ResponseAttendanceRef row = event.getRowValue();
                row.setSpeaking(floatValue);
                attendanceTableView.refresh();
            } catch (NumberFormatException e) {
                // Alert
                AlertUtils.showWarning("Chỉ được phép nhập số thập phân trong khoảng 0 tới 10.");
                attendanceTableView.refresh();
            }
        });

        readColumn.setCellValueFactory(cell -> {
            SimpleStringProperty property = new SimpleStringProperty();
            property.setValue(cell.getValue().getReading() != null ? cell.getValue().getReading().toString() : "");
            return property;
        });
        readColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        readColumn.setMinWidth(50);
        readColumn.setOnEditCommit((TableColumn.CellEditEvent<ResponseAttendanceRef, String> event) -> {
            try {
                // Parse input to float
                float floatValue = Float.parseFloat(event.getNewValue().trim());

                if (floatValue < 0 || floatValue > 10) {
                    AlertUtils.showWarning("Điểm phải nằm trong khoảng 0 tới 10.");
                    attendanceTableView.refresh();
                    return;
                }

                // Update table data
                ResponseAttendanceRef row = event.getRowValue();
                row.setReading(floatValue);
                attendanceTableView.refresh();
            } catch (NumberFormatException e) {
                // Alert
                AlertUtils.showWarning("Chỉ được phép nhập số thập phân trong khoảng 0 tới 10.");
                attendanceTableView.refresh();
            }
        });

        writeColumn.setCellValueFactory(cell -> {
            SimpleStringProperty property = new SimpleStringProperty();
            property.setValue(cell.getValue().getWriting() != null ? cell.getValue().getWriting().toString() : "");
            return property;
        });
        writeColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        writeColumn.setMinWidth(50);
        writeColumn.setOnEditCommit((TableColumn.CellEditEvent<ResponseAttendanceRef, String> event) -> {
            try {
                // Parse input to float
                float floatValue = Float.parseFloat(event.getNewValue().trim());

                if (floatValue < 0 || floatValue > 10) {
                    AlertUtils.showWarning("Điểm phải nằm trong khoảng 0 tới 10.");
                    attendanceTableView.refresh();
                    return;
                }

                // Update table data
                ResponseAttendanceRef row = event.getRowValue();
                row.setWriting(floatValue);
                attendanceTableView.refresh();
            } catch (NumberFormatException e) {
                // Alert
                AlertUtils.showWarning("Chỉ được phép nhập số thập phân trong khoảng 0 tới 10.");
                attendanceTableView.refresh();
            }
        });
    }

    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initTable();
        initFormValues();
    }
}
