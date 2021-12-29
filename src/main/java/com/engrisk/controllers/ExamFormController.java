package com.engrisk.controllers;

import com.engrisk.dto.Exam.CreateExamDTO;
import com.engrisk.enums.ExamType;
import com.engrisk.models.Exam;
import com.engrisk.utils.AlertUtils;
import com.engrisk.utils.DateUtils;
import com.engrisk.utils.NumberUtils;
import com.engrisk.utils.PriceFormatter;
import com.google.gson.Gson;
import com.mashape.unirest.http.exceptions.UnirestException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;
import org.json.simple.parser.ParseException;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;

public class ExamFormController extends BaseFormController {

    @FXML
    private TextField nameTextField, priceTextField;

    @FXML
    private ComboBox<ExamType> typeComboBox;

    @FXML
    private DatePicker examDatePicker;

    public Exam exam = new Exam();

    ObservableList<ExamType> examTypes = FXCollections.observableArrayList();

    @Override
    public void onSaveClick(ActionEvent event) throws UnirestException, ParseException {
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

        Date dateCurr = new Date();
        LocalDate dateExam = examDatePicker.getValue();
        if (DateUtils.parseDate(dateExam).before(dateCurr)) {
            AlertUtils.showWarning("Ngày thi phải sau ngày tạo khóa thi");
            return;
        }

        CreateExamDTO examDTO = new CreateExamDTO();
        examDTO.setName(name);
        examDTO.setType(type);
        examDTO.setPrice(Long.valueOf(price));
        examDTO.setExamDate(DateUtils.parseDate(examDatePicker.getValue()));

        //{
        //  "examDate": "2022-1-20 07:00",
        //  "name": "dfsdfsd",
        //  "price": 0,
        //  "type": "A2"
        //}
        System.out.println(examDTO.getExamDate());
        //"examDate":"Dec 29, 2021, 2:27:29 PM"
        Gson gson = new Gson();

        System.out.println(gson.toJson(examDTO));
    }

    public void initTypeComboBox() {
        Callback<ListView<ExamType>, ListCell<ExamType>> factory = (lv) ->
                new ListCell<>() {
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
        priceTextField.setText(PriceFormatter.format(exam.getPrice()));
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
