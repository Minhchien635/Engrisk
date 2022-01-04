package com.engrisk.controllers;

import com.engrisk.api.Api;
import com.engrisk.dto.Candidate.ResponseCandidateDTO;
import com.engrisk.utils.AlertUtils;
import com.engrisk.utils.DateUtils;
import com.engrisk.utils.Mapper;
import com.engrisk.utils.StageBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.exceptions.UnirestException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CandidateTableController extends BaseTableController {
    @FXML
    public TableView<ResponseCandidateDTO> table;

    @FXML
    public TableColumn<ResponseCandidateDTO, String> nameCol,
            genderCol,
            birthDateCol,
            birthPlaceCol,
            citizenIdCol,
            citizenIdDateCol,
            citizenIdPlaceCol,
            phoneCol,
            emailCol;

    // Data got from server
    ObservableList<ResponseCandidateDTO> data = FXCollections.observableArrayList();

    @Override
    public void onCreateClick(Event event) throws Exception {
        CandidateFormController controller = new CandidateFormController();
        controller.candidateTableController = this;

        new StageBuilder("candidate_form", controller, "Thêm thí sinh")
                .setModalOwner(event)
                .setDimensionsAuto()
                .build()
                .showAndWait();
    }

    @Override
    public void onEditClick(Event event) throws Exception {
        ResponseCandidateDTO candidate = table.getSelectionModel().getSelectedItem();

        if (candidate == null) {
            AlertUtils.showWarning("Hãy chọn khóa thi để sửa");
            return;
        }

        CandidateFormController controller = new CandidateFormController();
        controller.candidateTableController = this;
        controller.candidate = candidate;

        new StageBuilder("candidate_form", controller, "Sửa khóa thi")
                .setModalOwner(event)
                .setDimensionsAuto()
                .build()
                .showAndWait();
    }

    @Override
    public void onDeleteClick(Event event) {
        ResponseCandidateDTO selected = table.getSelectionModel().getSelectedItem();

        if (selected == null) {
            AlertUtils.showWarning("Hãy chọn thí sinh muốn xóa");
            return;
        }

        try {
            // Call delete api with candidate id
            Long id = selected.getId();
            JSONObject response = Api.delete("candidate/{id}", String.valueOf(id));

            if (response.has("error")) {
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
                    try {
                        onEditClick(event);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            return row;
        });

        nameCol.setCellValueFactory(cell -> {
            SimpleStringProperty property = new SimpleStringProperty();
            property.setValue(cell.getValue().getName());

            return property;
        });

        genderCol.setCellValueFactory(cell -> {
            SimpleStringProperty property = new SimpleStringProperty();
            property.setValue(switch (cell.getValue().getSex()) {
                case MALE -> "Nam";
                case FEMALE -> "Nữ";
                case OTHER -> "Khác";
            });

            return property;
        });

        birthDateCol.setCellValueFactory(cell -> {
            SimpleStringProperty property = new SimpleStringProperty();
            property.setValue(DateUtils.format(cell.getValue().getBirthDate()));

            return property;
        });

        birthPlaceCol.setCellValueFactory(cell -> {
            SimpleStringProperty property = new SimpleStringProperty();
            property.setValue(cell.getValue().getBirthPlace());

            return property;
        });

        citizenIdCol.setCellValueFactory(cell -> {
            SimpleStringProperty property = new SimpleStringProperty();
            property.setValue(cell.getValue().getCitizenId());

            return property;
        });

        citizenIdDateCol.setCellValueFactory(cell -> {
            SimpleStringProperty property = new SimpleStringProperty();
            property.setValue(DateUtils.format(cell.getValue().getCitizenIdDate()));

            return property;
        });

        citizenIdPlaceCol.setCellValueFactory(cell -> {
            SimpleStringProperty property = new SimpleStringProperty();
            property.setValue(cell.getValue().getCitizenIdPlace());

            return property;
        });

        phoneCol.setCellValueFactory(cell -> {
            SimpleStringProperty property = new SimpleStringProperty();
            property.setValue(cell.getValue().getPhone());

            return property;
        });

        emailCol.setCellValueFactory(cell -> {
            SimpleStringProperty property = new SimpleStringProperty();
            property.setValue(cell.getValue().getEmail());

            return property;
        });

        table.setItems(data);
    }

    @Override
    public void loadData() throws JsonProcessingException, UnirestException {
        String response = Api.get("candidate");
        ArrayList<ResponseCandidateDTO> candidates = new ArrayList<>();

        if (!response.equals("[]")) {
            ResponseCandidateDTO[] responseCandidateDTOs = Mapper.create()
                                                                 .readValue(response, ResponseCandidateDTO[].class);

            for (ResponseCandidateDTO candidateDto : responseCandidateDTOs) {
                ResponseCandidateDTO candidate = new ResponseCandidateDTO();

                candidate.setId(candidateDto.getId());
                candidate.setName(candidateDto.getName());
                candidate.setBirthDate(candidateDto.getBirthDate());
                candidate.setSex(candidateDto.getSex());
                candidate.setBirthPlace(candidateDto.getBirthPlace());
                candidate.setCitizenId(candidateDto.getCitizenId());
                candidate.setCitizenIdDate(candidateDto.getCitizenIdDate());
                candidate.setCitizenIdPlace(candidateDto.getCitizenIdPlace());
                candidate.setEmail(candidateDto.getEmail());
                candidate.setPhone(candidateDto.getPhone());

                candidates.add(candidate);
            }
        }

        data.setAll(candidates);
        table.refresh();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initTable();

        try {
            loadData();
        } catch (JsonProcessingException | UnirestException e) {
            e.printStackTrace();
        }
    }
}