module com.engrisk.engrisk {
    requires javafx.controls;
    requires javafx.fxml;
    requires lombok;
    requires modelmapper;
    requires spring.context;


    opens com.engrisk to javafx.fxml;
    exports com.engrisk;
    exports com.engrisk.controllers;
    opens com.engrisk.controllers to javafx.fxml;
}