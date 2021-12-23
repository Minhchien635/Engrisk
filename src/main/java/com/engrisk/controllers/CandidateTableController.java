package com.engrisk.controllers;

import com.engrisk.models.Candidate;
import com.engrisk.utils.AlertUtils;
import com.engrisk.utils.DateUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class CandidateTableController implements Initializable {
    @FXML
    public TableView<Candidate> table;

    @FXML
    public TableColumn<Candidate, String> nameColumn,
            genderColumn,
            birthDateColumn,
            birthPlaceColumn,
            citizenIdColumn,
            citizenIdDateColumn,
            citizenIdPlaceColumn,
            phoneColumn,
            emailColumn;

    // Data got from server
    ObservableList<Candidate> data = FXCollections.observableArrayList();

    @FXML
    public void onCreateClick() {
        // Open create candidate view
    }

    @FXML
    public void onEditClick() {
        // Open edit candidate view
    }

    @FXML
    public void onDeleteClick() {
        Candidate selected = table.getSelectionModel().getSelectedItem();

        if (selected == null) {
            AlertUtils.showWarning("Hãy chọn thí sinh muốn xóa");
            return;
        }

        try {
            // Call delete api with candidate id
            Long id = selected.getId();
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

        genderColumn.setCellValueFactory(cell -> {
            SimpleStringProperty property = new SimpleStringProperty();
            property.setValue(switch (cell.getValue().getSex()) {
                case MALE -> "Nam";
                case FEMALE -> "Nữ";
                case OTHER -> "Khác";
            });

            return property;
        });

        birthDateColumn.setCellValueFactory(cell -> {
            SimpleStringProperty property = new SimpleStringProperty();
            property.setValue(DateUtils.format(cell.getValue().getBirthDate()));

            return property;
        });

        birthPlaceColumn.setCellValueFactory(cell -> {
            SimpleStringProperty property = new SimpleStringProperty();
            property.setValue(cell.getValue().getBirthPlace());

            return property;
        });

        citizenIdColumn.setCellValueFactory(cell -> {
            SimpleStringProperty property = new SimpleStringProperty();
            property.setValue(cell.getValue().getCitizenId());

            return property;
        });

        citizenIdDateColumn.setCellValueFactory(cell -> {
            SimpleStringProperty property = new SimpleStringProperty();
            property.setValue(DateUtils.format(cell.getValue().getCitizenIdDate()));

            return property;
        });

        citizenIdPlaceColumn.setCellValueFactory(cell -> {
            SimpleStringProperty property = new SimpleStringProperty();
            property.setValue(cell.getValue().getCitizenIdPlace());

            return property;
        });

        phoneColumn.setCellValueFactory(cell -> {
            SimpleStringProperty property = new SimpleStringProperty();
            property.setValue(cell.getValue().getPhone());

            return property;
        });

        emailColumn.setCellValueFactory(cell -> {
            SimpleStringProperty property = new SimpleStringProperty();
            property.setValue(cell.getValue().getEmail());

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