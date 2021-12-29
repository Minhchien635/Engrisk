package com.engrisk.utils;

import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.util.EventObject;

public class WindowUtils {
    public static Window getWindow(EventObject event) {
        Node source = (Node) event.getSource();
        return source.getScene().getWindow();
    }

    public static void closeWindow(EventObject event) {
        Stage stage = (Stage) getWindow(event);
        stage.close();
    }
}
