package com.engrisk.controllers;

import com.engrisk.models.Room;
import com.engrisk.utils.DateUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.ResourceBundle;

public class RoomTableController implements Initializable {
    @FXML
    public TableView<Room> table;

    @FXML
    public TableColumn<Room, String> examNameColumn, examDateColumn, examTypeColumn, roomNameColumn;

    // Data got from server
    ObservableList<Room> data = FXCollections.observableArrayList();

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

    public void initData() {
        // Get data from api
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initTable();
        initData();
    }
}