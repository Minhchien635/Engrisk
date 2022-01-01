package com.engrisk.controllers;

import com.engrisk.api.CallApi;
import com.engrisk.dto.Exam.CreateExamDTO;
import com.engrisk.dto.Exam.ResponseExamDTO;
import com.engrisk.dto.Exam.UpdateExamDTO;
import com.engrisk.enums.ExamType;
import com.engrisk.utils.AlertUtils;
import com.engrisk.utils.DateUtils;
import com.engrisk.utils.NumberUtils;
import com.engrisk.utils.PriceFormatter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.exceptions.UnirestException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;

public class ExamFormController extends BaseFormController {

    public ResponseExamDTO exam = new ResponseExamDTO();

    public ExamTableController examTableController;

    ObservableList<ExamType> examTypes = FXCollections.observableArrayList();

    @FXML
    private TextField nameTextField, priceTextField;

    @FXML
    private ComboBox<ExamType> typeComboBox;

    @FXML
    private DatePicker examDatePicker;

    @Override
    public void onSaveClick(ActionEvent event) throws JsonProcessingException, UnirestException {
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

        if (exam.getId() != null) {
            UpdateExamDTO examDTO = new UpdateExamDTO();
            examDTO.setId(exam.getId());
            examDTO.setName(name);
            examDTO.setType(type);
            examDTO.setPrice(Long.valueOf(price));
            examDTO.setExamDate(DateUtils.parseDate(examDatePicker.getValue()));

            ObjectMapper mapper = new ObjectMapper();
            String request = mapper.writeValueAsString(examDTO);
            CallApi.put("exam", request);
            System.out.println(request);

            examTableController.loadData();
            closeWindow(event);

            return;
        }

        CreateExamDTO examDTO = new CreateExamDTO();
        examDTO.setName(name);
        examDTO.setType(type);
        examDTO.setPrice(Long.valueOf(price));
        examDTO.setExamDate(DateUtils.parseDate(examDatePicker.getValue()));

        ObjectMapper mapper = new ObjectMapper();
        String request = mapper.writeValueAsString(examDTO);
        CallApi.post("exam", request);

        examTableController.loadData();
        closeWindow(event);
    }

    public void initTypeComboBox() {
        Callback<ListView<ExamType>, ListCell<ExamType>> factory = (lv) -> new ListCell<>() {
            @Override
            protected void updateItem(ExamType item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.name());
            }
        };
        typeComboBox.setCellFactory(factory);

        typeComboBox.setButtonCell(factory.call(null));

        examTypes.setAll(ExamType.A2, ExamType.B1);

        typeComboBox.setItems(examTypes);
    }

    @Override
    public void initReadOnly() {
        nameTextField.setEditable(false);
        typeComboBox.setEditable(false);
        priceTextField.setEditable(false);
        examDatePicker.setEditable(false);
    }

    @Override
    public void initFormValues() {
        nameTextField.setText(exam.getName());
        typeComboBox.setItems(examTypes);
        typeComboBox.setValue(exam.type);
        priceTextField.setText(exam.getPrice().toString());
        examDatePicker.setValue(DateUtils.parseLocalDate(exam.getExamDate()));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initTypeComboBox();

        if (exam.getId() != null) {
            initFormValues();
        }

        if (read_only) {
            initReadOnly();
        }
    }
}
