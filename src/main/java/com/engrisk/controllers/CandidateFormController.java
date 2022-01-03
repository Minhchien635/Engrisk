package com.engrisk.controllers;

import com.engrisk.api.Api;
import com.engrisk.dto.Candidate.CreateCandidateDTO;
import com.engrisk.dto.Candidate.ResponseCandidateDTO;
import com.engrisk.dto.Candidate.UpdateCandidateDTO;
import com.engrisk.enums.SexType;
import com.engrisk.utils.AlertUtils;
import com.engrisk.utils.DateUtils;
import com.engrisk.utils.NumberUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.exceptions.UnirestException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;

public class CandidateFormController extends BaseFormController {

    public ResponseCandidateDTO candidate = null;

    public CandidateTableController candidateTableController;

    ObservableList<SexType> sexTypes = FXCollections.observableArrayList();

    @FXML
    private TextField nameTextField, birthPlaceTextField, citizenIdTextField, citizenIdPlaceTextField, emailTextField, phoneTextField;

    @FXML
    private ComboBox<SexType> sexTypeComboBox;

    @FXML
    private DatePicker birthDateDatePicker, citizenIdDateDatePicker;

    @FXML
    private VBox formInfoVbox;

    @Override
    public void onSaveClick(Event event) throws JsonProcessingException, UnirestException {
        String name = nameTextField.getText();
        if (name.trim().isEmpty()) {
            AlertUtils.showWarning("Hãy nhập tên thí sinh");
            return;
        }

        LocalDate birthDate = birthDateDatePicker.getValue();
        if (birthDate == null) {
            AlertUtils.showWarning("Hãy chọn ngày sinh");
            return;
        }

        SexType sexType = sexTypeComboBox.getValue();
        if (sexType == null) {
            AlertUtils.showWarning("Hãy chọn giới tính");
            return;
        }

        String birthDatePlace = birthPlaceTextField.getText();
        if (birthDatePlace.trim().isEmpty()) {
            AlertUtils.showWarning("Hãy nhập nơi sinh");
            return;
        }

        Date dateCurr = new Date();
        if (DateUtils.parseDate(birthDate).after(dateCurr)) {
            AlertUtils.showWarning("Ngày sinh phải trước ngày hiện tại");
            return;
        }

        String citizenId = citizenIdTextField.getText();
        if (citizenId.trim().isEmpty()) {
            AlertUtils.showWarning("Hãy nhập số căn cước công dân");
            return;
        }

        LocalDate citizenIdDate = citizenIdDateDatePicker.getValue();
        if (citizenIdDate == null) {
            AlertUtils.showWarning("Hãy chọn ngày cấp căn cước công dân");
            return;
        }

        String citizenIdPlace = citizenIdPlaceTextField.getText();
        if (citizenIdPlace == null) {
            AlertUtils.showWarning("Hãy nhập nơi cấp căn cước công dân");
            return;
        }

        String email = emailTextField.getText();
        if (email.trim().isEmpty()) {
            AlertUtils.showWarning("Hãy nhập email");
            return;
        }

        String phoneNumber = phoneTextField.getText();
        if (phoneNumber.trim().isEmpty()) {
            AlertUtils.showWarning("Hãy nhập số điện thoại");
            return;
        }

        if (!NumberUtils.isLong(phoneNumber)) {
            AlertUtils.showWarning("Số điện thoại không hợp lệ");
            return;
        }

        if (candidate == null) {
            CreateCandidateDTO createDTO = new CreateCandidateDTO();
            createDTO.setName(name);
            createDTO.setBirthDate(DateUtils.parseDate(birthDate));
            createDTO.setSex(sexType);
            createDTO.setBirthPlace(birthDatePlace);
            createDTO.setCitizenId(citizenId);
            createDTO.setCitizenIdDate(DateUtils.parseDate(citizenIdDate));
            createDTO.setCitizenIdPlace(citizenIdPlace);
            createDTO.setEmail(email);
            createDTO.setPhone(phoneNumber);

            String requestBody = new ObjectMapper().writeValueAsString(createDTO);
            Api.post("candidate", requestBody);
        } else {
            UpdateCandidateDTO updateDTO = new UpdateCandidateDTO();
            updateDTO.setName(name);
            updateDTO.setBirthDate(DateUtils.parseDate(birthDate));
            updateDTO.setSex(sexType);
            updateDTO.setBirthPlace(birthDatePlace);
            updateDTO.setCitizenId(citizenId);
            updateDTO.setCitizenIdDate(DateUtils.parseDate(citizenIdDate));
            updateDTO.setCitizenIdPlace(citizenIdPlace);
            updateDTO.setEmail(email);
            updateDTO.setPhone(phoneNumber);

            String requestBody = new ObjectMapper().writeValueAsString(candidate);
            Api.put("candidate", requestBody);
        }

        candidateTableController.loadData();
        closeWindow(event);
    }

    public void initSexTypeComboBox() {
        Callback<ListView<SexType>, ListCell<SexType>> factory = (lv) -> new ListCell<>() {
            @Override
            protected void updateItem(SexType item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.name());
            }
        };
        sexTypeComboBox.setCellFactory(factory);

        sexTypeComboBox.setButtonCell(factory.call(null));

        sexTypes.setAll(SexType.MALE, SexType.FEMALE, SexType.OTHER);

        sexTypeComboBox.setItems(sexTypes);
    }

    @Override
    public void initFormValues() {
        nameTextField.setText(candidate.getName());
        birthDateDatePicker.setValue(DateUtils.parseLocalDate(candidate.getBirthDate()));
        sexTypeComboBox.setValue(candidate.getSex());
        birthPlaceTextField.setText(candidate.getBirthPlace());
        citizenIdTextField.setText(candidate.getCitizenId());
        citizenIdDateDatePicker.setValue(DateUtils.parseLocalDate(candidate.getCitizenIdDate()));
        citizenIdPlaceTextField.setText(candidate.getCitizenIdPlace());
        emailTextField.setText(candidate.getEmail());
        phoneTextField.setText(candidate.getPhone());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initSexTypeComboBox();

        if (candidate != null) {
            initFormValues();
        }
    }
}
