package com.engrisk.controllers;

import com.engrisk.dto.Attendance.ResponseAttendanceDTO;
import com.engrisk.dto.Exam.ResponseCandidateRef;
import com.engrisk.utils.AlertUtils;
import com.engrisk.utils.DateUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

import java.net.URL;
import java.util.ArrayList;
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
    public Button searchButton;
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
            property.setValue(cell.getValue().getListening().toString());
            return property;
        });

        speakingColumn.setCellValueFactory(cell -> {
            SimpleStringProperty property = new SimpleStringProperty();
            property.setValue(cell.getValue().getSpeaking().toString());
            return property;
        });

        readingColumn.setCellValueFactory(cell -> {
            SimpleStringProperty property = new SimpleStringProperty();
            property.setValue(cell.getValue().getReading().toString());
            return property;
        });

        writingColumn.setCellValueFactory(cell -> {
            SimpleStringProperty property = new SimpleStringProperty();
            property.setValue(cell.getValue().getWriting().toString());
            return property;
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

    public void initData() {
        // Get data from server and set to data array and filtered data
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initTable();
        initSearchTextFields();
        initData();
    }
}