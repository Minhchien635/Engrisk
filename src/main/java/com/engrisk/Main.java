package com.engrisk;

import com.engrisk.utils.JacksonObjectMapper;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import kong.unirest.Unirest;

import java.io.IOException;

public class Main extends Application {
    public static final String BASE_URL = "http://localhost:8081/api/";

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        // Config unirest
        Unirest.config()
               .defaultBaseUrl(BASE_URL)
               .setDefaultHeader("accept", "application/json")
               .setDefaultHeader("content-type", "application/json")
               .setObjectMapper(new JacksonObjectMapper());

        // Open main view
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/engrisk/fxml/main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 720);
        stage.setTitle("Engrisk");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
}