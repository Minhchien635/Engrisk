package com.engrisk.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    public Button examButton, roomButton, candidateButton, attendanceButton;

    @FXML
    private VBox content;

    @FXML
    private VBox menu;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            // Load tour fxml into content pane
            loadView("exam_table");

            // Disable all other button active styles
            for (Node node : menu.getChildren()) {
                node.getStyleClass().remove("active");
            }

            // Set tour menu button as active
            examButton.getStyleClass().add("active");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Set onclick events for menu buttons
        setMenuButtonOnAction(examButton, "exam_table");
        setMenuButtonOnAction(roomButton, "room_table");
        setMenuButtonOnAction(candidateButton, "candidate_table");
        setMenuButtonOnAction(attendanceButton, "attendance_table");
    }

    public void setMenuButtonOnAction(Button button, String fxmlFileName) {
        button.setOnAction(actionEvent -> {
            try {
                // Load fxml into content pane
                loadView(fxmlFileName);

                // Disable all other button active styles
                for (Node node : menu.getChildren()) {
                    node.getStyleClass().remove("active");
                }

                // Add active class to clicked button
                button.getStyleClass().add("active");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void loadView(String fxmlFileName) throws IOException {
        // Load fxml file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/engrisk/fxml/" + fxmlFileName + ".fxml"));
        Parent fxml = loader.load();

        // Set fxml into content pane
        content.getChildren().setAll(fxml);
    }
}