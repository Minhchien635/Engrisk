package com.engrisk.controllers;

import com.engrisk.api.CallApi;
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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ExamTableController extends BaseTableController {
    @FXML
    public TableView<ResponseExamDTO> table;

    @FXML
    public TableColumn<ResponseExamDTO, String> nameColumn, typeColumn, priceColumn, dateColumn;

    // Data got from server
    ObservableList<ResponseExamDTO> data = FXCollections.observableArrayList();

    @FXML
    public void onCreateClick(ActionEvent event) throws IOException {
        ExamFormController controller = new ExamFormController();
        controller.examTableController = this;

        new StageBuilder("exam_form", controller, "Tạo khóa thi")
                .setModalOwner(event)
                .setDimensionsAuto()
                .build()
                .showAndWait();
    }

    @FXML
    public void onEditClick(ActionEvent event) {
        // Open edit exam view
    }

    @FXML
    public void onDeleteClick(ActionEvent event) {
        ResponseExamDTO selected = table.getSelectionModel().getSelectedItem();

        if (selected == null) {
            AlertUtils.showWarning("Hãy chọn khóa thi để xóa");
            return;
        }

        try {
            // Call delete api with exam id
            long id = selected.getId();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void loadData() throws UnirestException, JsonProcessingException {
        ResponseExamDTO[] responseExamDTOs;
        String response = CallApi.get("exam");
        ObjectMapper mapper = new ObjectMapper();
        responseExamDTOs = mapper.readValue(response, ResponseExamDTO[].class);

        data.setAll(responseExamDTOs);
        table.refresh();
    }

    @Override
    public void onSearchListener() {

    }

    public void initTable() {
        nameColumn.setCellValueFactory(cell -> {
            SimpleStringProperty property = new SimpleStringProperty();
            property.setValue(cell.getValue().getName());
            return property;
        });

        typeColumn.setCellValueFactory(cell -> {
            SimpleStringProperty property = new SimpleStringProperty();
            property.setValue(cell.getValue().getType().name());
            return property;
        });

        priceColumn.setCellValueFactory(cell -> {
            SimpleStringProperty property = new SimpleStringProperty();
            property.setValue(PriceFormatter.format(cell.getValue().getPrice()));
            return property;
        });

        dateColumn.setCellValueFactory(cell -> {
            SimpleStringProperty property = new SimpleStringProperty();
            property.setValue(DateUtils.format(cell.getValue().getExamDate()));
            return property;
        });

        table.setItems(data);
    }

    public void initData() {
        // Get data from server and set to observable list
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initTable();
        initData();
        try {
            loadData();
        } catch (UnirestException | JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}