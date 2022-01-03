package com.engrisk.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.exceptions.UnirestException;
import javafx.event.Event;
import javafx.fxml.Initializable;

public abstract class BaseTableController implements Initializable {
    // Initialize how to render table columns and rows
    public abstract void initTable();

    // Load data for table
    public abstract void loadData() throws JsonProcessingException, UnirestException;

    // On create button click
    public abstract void onCreateClick(Event e) throws Exception;

    // On edit button click
    public abstract void onEditClick(Event e) throws Exception;

    // On delete button click
    public abstract void onDeleteClick(Event e);
}
