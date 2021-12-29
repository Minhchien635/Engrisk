package com.engrisk.controllers;

import com.engrisk.utils.WindowUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.exceptions.UnirestException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.text.ParseException;
import java.util.EventObject;
import java.util.ResourceBundle;

public abstract class BaseFormController implements Initializable {
    @FXML
    public Button saveButton;

    public boolean read_only;

    public void closeWindow(EventObject event) {
        WindowUtils.closeWindow(event);
    }

    public abstract void onSaveClick(ActionEvent event) throws UnirestException, ParseException, JsonProcessingException;

    public abstract void initReadOnly();

    public abstract void initFormValues();

    public void onCancelClick(ActionEvent event) {
        closeWindow(event);
    }

    @Override
    public abstract void initialize(URL url, ResourceBundle resourceBundle);
}
