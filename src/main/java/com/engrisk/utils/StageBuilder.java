package com.engrisk.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.util.EventObject;

public class StageBuilder {
    public static int DEFAULT_WIDTH = 800;
    public static int DEFAULT_HEIGHT = 600;

    // Required parameters
    private final String fxmlFileName;
    private final String title;
    private final Object controller;

    // Optional parameters
    private Stage stage = new Stage();
    private double width = DEFAULT_WIDTH;
    private double height = DEFAULT_HEIGHT;
    private Window modalOwner;

    public StageBuilder(String fxmlFileName, Object controller, String title) {
        this.fxmlFileName = fxmlFileName;
        this.controller = controller;
        this.title = title;
    }

    public StageBuilder setStage(Stage stage) {
        this.stage = stage;
        return this;
    }

    public StageBuilder setDimensions(int width, int height) {
        this.width = width;
        this.height = height;
        return this;
    }

    public StageBuilder setDimensionsAuto() {
        this.width = -1.0D;
        this.height = -1.0D;
        return this;
    }

    public StageBuilder setModalOwner(Window owner) {
        this.modalOwner = owner;
        return this;
    }

    public StageBuilder setModalOwner(EventObject event) {
        this.modalOwner = WindowUtils.getWindow(event);
        return this;
    }

    public Stage build() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/engrisk/fxml/" + fxmlFileName + ".fxml"));
        loader.setController(controller);

        Parent root = loader.load();

        Scene scene = new Scene(root, width, height);

        stage.setScene(scene);
        stage.setTitle(title);
        stage.setResizable(false);

        if (modalOwner != null) {
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(modalOwner);
        }

        return stage;
    }
}