package com.engrisk.controllers;

import com.engrisk.api.CallApi;
import com.engrisk.dto.Candidate.ResponseCandidateDTO;
import com.engrisk.utils.AlertUtils;
import com.engrisk.utils.DateUtils;
import com.engrisk.utils.StageBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.exceptions.UnirestException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CandidateTableController extends BaseTableController {
    @FXML
    public TableView<ResponseCandidateDTO> table;

    @FXML
    public TableColumn<ResponseCandidateDTO, String> nameColumn,
            genderColumn,
            birthDateColumn,
            birthPlaceColumn,
            citizenIdColumn,
            citizenIdDateColumn,
            citizenIdPlaceColumn,
            phoneColumn,
            emailColumn;

    // Data got from server
    ObservableList<ResponseCandidateDTO> data = FXCollections.observableArrayList();

    @Override
    public void onCreateClick(ActionEvent e) throws Exception {
        CandidateFormController controller = new CandidateFormController();
        controller.candidateTableController = this;

        new StageBuilder("candidate_form", controller, "Thêm thí sinh")
                .setModalOwner(e)
                .setDimensionsAuto()
                .build()
                .showAndWait();
    }

    @Override
    public void onEditClick(ActionEvent e) throws Exception {
    }

    @Override
    public void onDeleteClick(ActionEvent e) {
        ResponseCandidateDTO selected = table.getSelectionModel().getSelectedItem();

        if (selected == null) {
            AlertUtils.showWarning("Hãy chọn thí sinh muốn xóa");
            return;
        }

        try {
            // Call delete api with candidate id
            Long id = selected.getId();
            JSONObject response = CallApi.delete("candidate/{id}", String.valueOf(id));

            if (response.toMap().containsKey("error")) {
                AlertUtils.showWarning("Không thể xóa. Thí sinh đã có lịch thi");
                return;
            }

            loadData();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void initTable() {

        // On row double click
        table.setRowFactory(tv -> {
            TableRow<ResponseCandidateDTO> row = new TableRow<>();

            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    ResponseCandidateDTO candidate = row.getItem();

                    try {
                        // Init controller
                        CandidateFormController controller = new CandidateFormController();
                        controller.candidateTableController = this;
                        controller.candidate = candidate;

                        // Show modal
                        new StageBuilder("candidate_form", controller, "Chi tiết khóa thi")
                                .setModalOwner(event)
                                .setDimensionsAuto()
                                .build()
                                .showAndWait();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            return row;
        });

        nameColumn.setCellValueFactory(cell -> {
            SimpleStringProperty property = new SimpleStringProperty();
            property.setValue(cell.getValue().getName());

            return property;
        });

        genderColumn.setCellValueFactory(cell -> {
            SimpleStringProperty property = new SimpleStringProperty();
            property.setValue(switch (cell.getValue().getSex()) {
                case MALE -> "Nam";
                case FEMALE -> "Nữ";
                case OTHER -> "Khác";
            });

            return property;
        });

        birthDateColumn.setCellValueFactory(cell -> {
            SimpleStringProperty property = new SimpleStringProperty();
            property.setValue(DateUtils.format(cell.getValue().getBirthDate()));

            return property;
        });

        birthPlaceColumn.setCellValueFactory(cell -> {
            SimpleStringProperty property = new SimpleStringProperty();
            property.setValue(cell.getValue().getBirthPlace());

            return property;
        });

        citizenIdColumn.setCellValueFactory(cell -> {
            SimpleStringProperty property = new SimpleStringProperty();
            property.setValue(cell.getValue().getCitizenId());

            return property;
        });

        citizenIdDateColumn.setCellValueFactory(cell -> {
            SimpleStringProperty property = new SimpleStringProperty();
            property.setValue(DateUtils.format(cell.getValue().getCitizenIdDate()));

            return property;
        });

        citizenIdPlaceColumn.setCellValueFactory(cell -> {
            SimpleStringProperty property = new SimpleStringProperty();
            property.setValue(cell.getValue().getCitizenIdPlace());

            return property;
        });

        phoneColumn.setCellValueFactory(cell -> {
            SimpleStringProperty property = new SimpleStringProperty();
            property.setValue(cell.getValue().getPhone());

            return property;
        });

        emailColumn.setCellValueFactory(cell -> {
            SimpleStringProperty property = new SimpleStringProperty();
            property.setValue(cell.getValue().getEmail());

            return property;
        });

        table.setItems(data);
    }

    @Override
    public void loadData() throws UnirestException, JsonProcessingException {
        ResponseCandidateDTO[] responseCandidateDTOs = new ResponseCandidateDTO[0];
        String response = CallApi.get("candidate");

        if (!response.equals("[]")) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            responseCandidateDTOs = mapper.readValue(response, ResponseCandidateDTO[].class);
        }

        data.setAll(responseCandidateDTOs);
        table.refresh();
    }

    @Override
    public void onSearchListener() {

    }

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
    }
}