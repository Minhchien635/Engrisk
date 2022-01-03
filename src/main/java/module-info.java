module com.engrisk.engrisk {
    requires javafx.controls;
    requires javafx.fxml;
    requires lombok;
    requires modelmapper;
    requires spring.context;
    requires unirest.java;
    requires com.google.gson;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;
    requires org.json;


    exports com.engrisk;
    opens com.engrisk to javafx.fxml;

    exports com.engrisk.controllers;
    opens com.engrisk.controllers to javafx.fxml;

    exports com.engrisk.models;
    opens com.engrisk.models to javafx.fxml;

    exports com.engrisk.dto.Exam;
    opens com.engrisk.dto.Exam;

    exports com.engrisk.dto.Attendance;
    opens com.engrisk.dto.Attendance;

    exports com.engrisk.dto.Candidate;
    opens com.engrisk.dto.Candidate;

    exports com.engrisk.dto.Room;
    opens com.engrisk.dto.Room;

    exports com.engrisk.utils;
    opens com.engrisk.utils to javafx.fxml;

    exports com.engrisk.enums;
    opens com.engrisk.enums to javafx.fxml;
}