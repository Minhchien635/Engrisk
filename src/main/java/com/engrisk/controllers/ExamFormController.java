package com.engrisk.controllers;

import com.engrisk.api.CallApi;
import com.engrisk.dto.Exam.*;
import com.engrisk.enums.ExamType;
import com.engrisk.enums.SexType;
import com.engrisk.utils.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;

public class ExamFormController extends BaseFormController {

    public ResponseExamDTO exam = null;

    public ExamTableController examTableController;

    ObservableList<ExamType> examTypes = FXCollections.observableArrayList();

    ObservableList<ResponseAttendanceRef> attendances = FXCollections.observableArrayList();

    @FXML
    public VBox formBody;

    @FXML
    public TextField nameTextField, priceTextField;

    @FXML
    public ComboBox<ExamType> typeComboBox;

    @FXML
    public DatePicker examDatePicker;

    @FXML
    public VBox attendanceContainer;

    @FXML
    public TableView<ResponseAttendanceRef> attendanceTableView;

    @FXML
    public TableColumn<ResponseAttendanceRef, String> attendanceNameCol,
            attendancePhoneCol,
            attendanceEmailCol,
            attendanceGenderCol,
            attendanceBirthDateCol,
            attendanceBirthPlaceCol,
            attendanceCitizenIdNumberCol,
            attendanceCitizenIdDateCol,
            attendanceCitizenIdPlaceCol;

    @Override
    public void onSaveClick(ActionEvent event) throws JsonProcessingException {
        String name = nameTextField.getText();
        if (name.trim().isEmpty()) {
            AlertUtils.showWarning("Hãy nhập tên khóa thi");
            return;
        }

        ExamType type = typeComboBox.getValue();
        if (type == null) {
            AlertUtils.showWarning("Hãy chọn trình độ cho khóa thi");
            return;
        }

        String price = priceTextField.getText();
        if (price.trim().isEmpty()) {
            AlertUtils.showWarning("Hãy nhập phí dự thi");
            return;
        }

        if (!NumberUtils.isLong(price)) {
            AlertUtils.showWarning("Phí dự thi không hợp lệ");
            return;
        }

        LocalDate dateExam = examDatePicker.getValue();
        if (dateExam == null) {
            AlertUtils.showWarning("Hãy chọn ngày thi");
            return;
        }

        Date dateCurr = new Date();
        if (DateUtils.parseDate(dateExam).before(dateCurr)) {
            AlertUtils.showWarning("Ngày thi phải sau ngày tạo khóa thi");
            return;
        }

        if (exam != null) {
            UpdateExamDTO updateDTO = new UpdateExamDTO();
            updateDTO.setId(exam.getId());
            updateDTO.setName(name);
            updateDTO.setType(type);
            updateDTO.setPrice(Long.valueOf(price));
            updateDTO.setExamDate(DateUtils.parseDate(examDatePicker.getValue()));

            ObjectMapper mapper = new ObjectMapper();
            String request = mapper.writeValueAsString(updateDTO);
            CallApi.put("exam", request);
        } else {
            CreateExamDTO createDTO = new CreateExamDTO();
            createDTO.setName(name);
            createDTO.setType(type);
            createDTO.setPrice(Long.valueOf(price));
            createDTO.setExamDate(DateUtils.parseDate(examDatePicker.getValue()));

            CallApi.post("exam", createDTO);
        }

        examTableController.loadData();

        closeWindow(event);
    }

    public void initTypeComboBox() {
        examTypes.setAll(ExamType.A2, ExamType.B1);

        typeComboBox.setItems(examTypes);

        typeComboBox.setCellFactory(data -> new ListCell<>() {
            @Override
            protected void updateItem(ExamType item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.name());
            }
        });
    }

    public void initAttendanceTable() {
        attendanceTableView.setItems(attendances);

        attendanceNameCol.setCellValueFactory(data -> {
            SimpleStringProperty property = new SimpleStringProperty();
            property.setValue(data.getValue().getCandidate().getName());
            return property;
        });

        attendancePhoneCol.setCellValueFactory(data -> {
            SimpleStringProperty property = new SimpleStringProperty();
            property.setValue(data.getValue().getCandidate().getPhone());
            return property;
        });

        attendanceEmailCol.setCellValueFactory(data -> {
            SimpleStringProperty property = new SimpleStringProperty();
            property.setValue(data.getValue().getCandidate().getEmail());
            return property;
        });

        attendanceGenderCol.setCellValueFactory(data -> {
            SimpleStringProperty property = new SimpleStringProperty();
            property.setValue(EnumUtils.toString(data.getValue().getCandidate().getSex()));
            return property;
        });

        attendanceBirthDateCol.setCellValueFactory(data -> {
            SimpleStringProperty property = new SimpleStringProperty();
            property.setValue(DateUtils.format(data.getValue().getCandidate().getBirthDate()));
            return property;
        });

        attendanceBirthPlaceCol.setCellValueFactory(data -> {
            SimpleStringProperty property = new SimpleStringProperty();
            property.setValue(data.getValue().getCandidate().getBirthPlace());
            return property;
        });

        attendanceCitizenIdNumberCol.setCellValueFactory(data -> {
            SimpleStringProperty property = new SimpleStringProperty();
            property.setValue(data.getValue().getCandidate().getCitizenId());
            return property;
        });

        attendanceCitizenIdDateCol.setCellValueFactory(data -> {
            SimpleStringProperty property = new SimpleStringProperty();
            property.setValue(DateUtils.format(data.getValue().getCandidate().getCitizenIdDate()));
            return property;
        });

        attendanceCitizenIdPlaceCol.setCellValueFactory(data -> {
            SimpleStringProperty property = new SimpleStringProperty();
            property.setValue(data.getValue().getCandidate().getCitizenIdPlace());
            return property;
        });
    }

    public void onAttendanceAddClick(ActionEvent event) {
        try {
            // Init controller
            ExamAttendanceFormController controller = new ExamAttendanceFormController();
            controller.examFormController = this;

            // Show dialog
            new StageBuilder("exam_attendance_form", controller, "Thêm thí sinh dự thi")
                    .setModalOwner(event)
                    .setDimensionsAuto()
                    .build()
                    .showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initFormValues() {
        nameTextField.setText(exam.getName());
        typeComboBox.setValue(exam.type);
        priceTextField.setText(exam.getPrice().toString());
        examDatePicker.setValue(DateUtils.parseLocalDate(exam.getExamDate()));
        attendances.setAll(exam.getAttendances());
    }

    public void loadData() {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initTypeComboBox();

        // If edit
        if (exam != null) {
            initAttendanceTable();
            initFormValues();
        }
        // Else create
        else {
            // Remove attendance table
            formBody.getChildren().remove(attendanceContainer);
        }
    }
}
