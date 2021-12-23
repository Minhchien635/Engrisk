package com.engrisk.controllers;

import com.engrisk.models.Exam;
import com.engrisk.utils.AlertUtils;
import com.engrisk.utils.DateUtils;
import com.engrisk.utils.PriceFormatter;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.ResourceBundle;

public class ExamTableController implements Initializable {
    @FXML
    public TableView<Exam> table;

    @FXML
    public TableColumn<Exam, String> nameColumn, typeColumn, priceColumn, dateColumn;

    // Data got from server
    ObservableList<Exam> data = FXCollections.observableArrayList();

    @FXML
    public void onCreateClick() {
        // Open create exam view
    }

    @FXML
    public void onEditClick() {
        // Open edit exam view
    }

    @FXML
    public void onDeleteClick() {
        Exam selected = table.getSelectionModel().getSelectedItem();

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
    }
}