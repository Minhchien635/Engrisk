package com.engrisk.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.exceptions.UnirestException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public abstract class BaseTableController implements Initializable {
    @FXML
    public TextField searchTextField;

    @FXML
    private ComboBox<String> optionComboBox;

    // Initialize how to render table columns and rows
    public abstract void initTable();

    // Load data for table
    public abstract void loadData() throws UnirestException, JsonProcessingException;

    public abstract void onSearchListener();

    // On create button click
    public abstract void onCreateClick(ActionEvent e) throws Exception;

    // On edit button click
    public abstract void onEditClick(ActionEvent e) throws Exception;

    // On delete button click
    public abstract void onDeleteClick(ActionEvent e);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initTable();
        try {
            loadData();
        } catch (UnirestException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        onSearchListener();
    }
}
