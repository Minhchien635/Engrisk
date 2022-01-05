package com.engrisk.controllers;

import com.engrisk.utils.WindowUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.exceptions.UnirestException;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.EventObject;
import java.util.ResourceBundle;

public abstract class BaseFormController implements Initializable {
    @FXML
    public Button saveButton;

    public void closeWindow(EventObject event) throws UnirestException, JsonProcessingException {
        WindowUtils.closeWindow(event);
    }

    public abstract void onSaveClick(Event event) throws JsonProcessingException, UnirestException;

    public abstract void initFormValues();

    public void onCancelClick(Event event) throws UnirestException, JsonProcessingException {
        closeWindow(event);
    }

    @Override
    public abstract void initialize(URL url, ResourceBundle resourceBundle);
}
