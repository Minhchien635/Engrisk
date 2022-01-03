package com.engrisk.controllers;

import com.engrisk.api.Api;
import com.engrisk.dto.Room.ResponseRoomDTO;
import com.engrisk.utils.DateUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.exceptions.UnirestException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import lombok.SneakyThrows;

import java.net.URL;
import java.util.ResourceBundle;

public class RoomTableController implements Initializable {
    @FXML
    public TableView<ResponseRoomDTO> table;

    @FXML
    public TableColumn<ResponseRoomDTO, String> examNameColumn, examDateColumn, examTypeColumn, roomNameColumn;

    // Data got from server
    ObservableList<ResponseRoomDTO> data = FXCollections.observableArrayList();

    public void initTable() {

        examNameColumn.setCellValueFactory(cell -> {
            SimpleStringProperty property = new SimpleStringProperty();
            property.setValue(cell.getValue().getExam().getName());
            return property;
        });

        examDateColumn.setCellValueFactory(cell -> {
            SimpleStringProperty property = new SimpleStringProperty();
            property.setValue(DateUtils.format(cell.getValue().getExam().getExamDate()));
            return property;
        });

        examTypeColumn.setCellValueFactory(cell -> {
            SimpleStringProperty property = new SimpleStringProperty();
            property.setValue(cell.getValue().getExam().getType().name());
            return property;
        });

        roomNameColumn.setCellValueFactory(cell -> {
            SimpleStringProperty property = new SimpleStringProperty();
            property.setValue(cell.getValue().getName());
            return property;
        });

        table.setItems(data);
    }

    public void loadData() throws JsonProcessingException, UnirestException {
        ResponseRoomDTO[] responseRoomDTOs;
        String response = Api.get("room");
        ObjectMapper mapper = new ObjectMapper();
        responseRoomDTOs = mapper.readValue(response, ResponseRoomDTO[].class);

        data.setAll(responseRoomDTOs);
        table.refresh();
    }

    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initTable();

        loadData();
    }
}