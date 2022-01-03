package com.engrisk.controllers;

import com.engrisk.api.Api;
import com.engrisk.dto.Exam.ResponseExamDTO;
import com.engrisk.utils.AlertUtils;
import com.engrisk.utils.DateUtils;
import com.engrisk.utils.PriceFormatter;
import com.engrisk.utils.StageBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.exceptions.UnirestException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ExamTableController extends BaseTableController {
    @FXML
    public TableView<ResponseExamDTO> table;

    @FXML
    public TableColumn<ResponseExamDTO, String> nameCol, typeCol, priceCol, dateCol;

    // Data got from server
    ObservableList<ResponseExamDTO> data = FXCollections.observableArrayList();

    @FXML
    public void onCreateClick(Event event) throws IOException {
        ExamFormController controller = new ExamFormController();
        controller.examTableController = this;

        new StageBuilder("exam_form", controller, "Thêm khóa thi")
                .setModalOwner(event)
                .setDimensions(400, -1)
                .build()
                .showAndWait();
    }

    @FXML
    public void onEditClick(Event event) throws IOException {
        ResponseExamDTO exam = table.getSelectionModel().getSelectedItem();

        if (exam == null) {
            AlertUtils.showWarning("Hãy chọn khóa thi để sửa");
            return;
        }

        ExamFormController controller = new ExamFormController();
        controller.examTableController = this;
        controller.exam = exam;

        new StageBuilder("exam_form", controller, "Sửa khóa thi")
                .setModalOwner(event)
                .setDimensions(-1, 720)
                .build()
                .showAndWait();
    }

    @FXML
    public void onDeleteClick(Event event) {
        ResponseExamDTO selected = table.getSelectionModel().getSelectedItem();

        if (selected == null) {
            AlertUtils.showWarning("Hãy chọn khóa thi để xóa");
            return;
        }

        try {
            // Call delete api with exam id
            long id = selected.getId();
            Api.delete("exam/{id}", String.valueOf(id));

            loadData();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void loadData() throws UnirestException, JsonProcessingException {
        String response = Api.get("exam");

        ResponseExamDTO[] exams = new ObjectMapper().readValue(response, ResponseExamDTO[].class);

        data.setAll(exams);
    }

    public void initTable() {
        // On row double click
        table.setRowFactory(tv -> {
            TableRow<ResponseExamDTO> row = new TableRow<>();

            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    try {
                        onEditClick(event);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            return row;
        });

        nameCol.setCellValueFactory(cell -> {
            SimpleStringProperty property = new SimpleStringProperty();
            property.setValue(cell.getValue().getName());
            return property;
        });

        typeCol.setCellValueFactory(cell -> {
            SimpleStringProperty property = new SimpleStringProperty();
            property.setValue(cell.getValue().getType().name());
            return property;
        });

        priceCol.setCellValueFactory(cell -> {
            SimpleStringProperty property = new SimpleStringProperty();
            property.setValue(PriceFormatter.format(cell.getValue().getPrice()));
            return property;
        });

        dateCol.setCellValueFactory(cell -> {
            SimpleStringProperty property = new SimpleStringProperty();
            property.setValue(DateUtils.format(cell.getValue().getExamDate()));
            return property;
        });

        table.setItems(data);
    }

    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initTable();

        loadData();
    }
}