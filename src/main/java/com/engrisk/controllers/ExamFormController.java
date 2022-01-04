package com.engrisk.controllers;

import com.engrisk.api.Api;
import com.engrisk.dto.Attendance.ResponseAttendanceDTO;
import com.engrisk.dto.Exam.*;
import com.engrisk.enums.ExamType;
import com.engrisk.utils.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

public class ExamFormController extends BaseFormController {

    public ResponseExamDTO exam = null;

    public ExamTableController examTableController;
    @FXML
    public VBox formBody;
    @FXML
    public TextField nameTextField, priceTextField;
    @FXML
    public ComboBox<ExamType> typeComboBox;
    @FXML
    public DatePicker examDatePicker;
    @FXML
    public VBox attendanceContainer, roomContainer;
    @FXML
    public HBox attendanceActionButtons, roomActionButtons;
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
    @FXML
    public TableView<ResponseRoomRef> roomTableView;
    @FXML
    public TableColumn<ResponseRoomRef, String> roomNameCol;
    ObservableList<ExamType> examTypes = FXCollections.observableArrayList();
    ObservableList<ResponseAttendanceRef> attendances = FXCollections.observableArrayList();
    ObservableList<ResponseRoomRef> rooms = FXCollections.observableArrayList();

    @Override
    public void onSaveClick(Event event) throws JsonProcessingException, UnirestException {
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

            String body = Mapper.create().writeValueAsString(updateDTO);
            Api.put("exam", body);
        } else {
            CreateExamDTO dto = new CreateExamDTO();
            dto.setName(name);
            dto.setType(type);
            dto.setPrice(Long.valueOf(price));
            dto.setExamDate(DateUtils.parseDate(examDatePicker.getValue()));

            String body = Mapper.create().writeValueAsString(dto);
            Api.post("exam", body);
        }

        examTableController.loadData();

        closeWindow(event);
    }

    public void onCancelClick(Event event) throws UnirestException, JsonProcessingException {
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

    public void initRoomTable() {
        roomTableView.setItems(rooms);

        roomNameCol.setCellValueFactory(data -> {
            SimpleStringProperty property = new SimpleStringProperty();
            property.setValue(data.getValue().getName());
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

    public void onAttendanceDeleteClick(ActionEvent event) throws UnirestException, JsonProcessingException {
        ResponseAttendanceRef attendance = attendanceTableView.getSelectionModel().getSelectedItem();

        if (attendance == null) {
            AlertUtils.showWarning("Hãy chọn thí sinh dự thi để xóa.");
            return;
        }

        Alert confirmAlert = AlertUtils.createConfirmAlert("Bạn có chắc là muốn xóa thí sinh này khỏi khóa thi?");

        Optional<ButtonType> result = confirmAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            String response = Unirest.delete(Api.URL + "attendance/id")
                    .queryString("candidateId", attendance.getCandidate().getId().toString())
                    .queryString("examId", exam.getId().toString())
                    .asString()
                    .getBody();

            ResponseAttendanceDTO responseDTO = Mapper.create()
                    .readValue(response, ResponseAttendanceDTO.class);
            Long deletedCandidateId = responseDTO.getCandidate().getId();
            attendances.removeIf(x -> x.getCandidate().getId().equals(deletedCandidateId));
        }
    }

    public void onArrangeRoomClick(ActionEvent event) throws UnirestException, JsonProcessingException {
        if (attendances.isEmpty()) {
            AlertUtils.showWarning("Hãy thêm ít nhất một thí sinh để xếp phòng.");
            return;
        }

        // Call arrange room api
        String responseString = Unirest.put(Api.URL + "exam/{id}/rearrange")
                .routeParam("id", exam.getId().toString())
                .asString()
                .getBody();

        // Update data
        this.exam = Mapper.create()
                .readValue(responseString, ResponseExamDTO.class);
        this.attendances.setAll(exam.getAttendances());
        this.rooms.setAll(exam.getRooms());
        hideAttendanceActionButtons();
        hideRearrangeButton();
    }

    public void hideAttendanceActionButtons() {
        attendanceActionButtons.setManaged(false);
        attendanceActionButtons.setVisible(false);
    }

    public void hideRearrangeButton() {
        roomActionButtons.setManaged(false);
        roomActionButtons.setVisible(false);
    }

    @Override
    public void initFormValues() {
        nameTextField.setText(exam.getName());
        typeComboBox.setValue(exam.type);
        priceTextField.setText(exam.getPrice().toString());
        examDatePicker.setValue(DateUtils.parseLocalDate(exam.getExamDate()));
        attendances.setAll(exam.getAttendances());
        rooms.setAll(exam.getRooms());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initTypeComboBox();

        // If edit
        if (exam != null) {
            initAttendanceTable();
            initRoomTable();
            initFormValues();

            // Hide action buttons if rooms are arranged
            if (!exam.getRooms().isEmpty()) {
                hideAttendanceActionButtons();
                hideRearrangeButton();
            }

            // Disable adding/removing attendance 5 days before exam start date
            long differMilli = exam.examDate.getTime() - new Date().getTime();
            boolean closed = TimeUnit.MILLISECONDS.toDays(differMilli) < 5;
            if (closed) {
                hideAttendanceActionButtons();
            }
        }
        // Else create
        else {
            // Remove attendance table and room table
            formBody.getChildren().removeAll(attendanceContainer, roomContainer);
        }
    }
}
