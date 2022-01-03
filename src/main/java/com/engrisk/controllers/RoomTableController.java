package com.engrisk.controllers;

import com.engrisk.api.CallApi;
import com.engrisk.dto.Room.ResponseRoomDTO;
import com.engrisk.utils.DateUtils;
import com.engrisk.utils.StageBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.exceptions.UnirestException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;

import java.io.IOException;
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

        // On row double click
        table.setRowFactory(tv -> {
            TableRow<ResponseRoomDTO> row = new TableRow<>();

            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    ResponseRoomDTO selected = row.getItem();

                    try {
                        // Init controller
                        RoomFormController controller = new RoomFormController();
                        controller.room = selected;

                        // Show modal
                        new StageBuilder("room_form", controller, "Chi tiết phòng thi")
                                .setModalOwner(event)
                                .setDimensionsAuto()
                                .build()
                                .showAndWait();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            return row;
        });

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

    public void initData() throws UnirestException, JsonProcessingException {
        ResponseRoomDTO[] responseRoomDTOs;
        String response = CallApi.get("room");
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        responseRoomDTOs = mapper.readValue(response, ResponseRoomDTO[].class);

        data.setAll(responseRoomDTOs);
        table.refresh();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initTable();
        try {
            initData();
        } catch (UnirestException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}