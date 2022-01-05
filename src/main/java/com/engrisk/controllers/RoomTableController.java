package com.engrisk.controllers;

import com.engrisk.api.Api;
import com.engrisk.dto.Room.ResponseRoomDTO;
import com.engrisk.utils.AlertUtils;
import com.engrisk.utils.DateUtils;
import com.engrisk.utils.Mapper;
import com.engrisk.utils.StageBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.exceptions.UnirestException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.stage.WindowEvent;
import lombok.SneakyThrows;

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

    public void onEditClick(Event event) throws IOException, UnirestException {
        ResponseRoomDTO selectedRoom = table.getSelectionModel().getSelectedItem();

        if (selectedRoom == null) {
            AlertUtils.showWarning("Hãy chọn phòng thi để nhập điểm");
            return;
        }

        // Init controller
        RoomFormController controller = new RoomFormController();
        controller.room = selectedRoom;
        controller.roomTableController = this;

        // Show modal
        StageBuilder stageBuilder = new StageBuilder("room_form", controller, "Nhập điểm");
        stageBuilder.getStage().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, windowEvent -> {
            try {
                loadData();
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            } catch (UnirestException e) {
                e.printStackTrace();
            }
        });
        stageBuilder.setModalOwner(event)
                .setDimensionsAuto()
                .build()
                .showAndWait();
    }

    public void initTable() {

        // On row double click
        table.setRowFactory(tv -> {
            TableRow<ResponseRoomDTO> row = new TableRow<>();

            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    try {
                        onEditClick(event);
                    } catch (IOException | UnirestException e) {
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

    public void loadData() throws JsonProcessingException, UnirestException {
        String response = Api.get("room");
        ResponseRoomDTO[] rooms = Mapper.create()
                .readValue(response, ResponseRoomDTO[].class);

        data.clear();
        data.setAll(rooms);
        table.refresh();
    }

    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initTable();
        loadData();
    }
}