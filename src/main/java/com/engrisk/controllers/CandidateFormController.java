package com.engrisk.controllers;

import com.engrisk.api.CallApi;
import com.engrisk.dto.Candidate.CreateCandidateDTO;
import com.engrisk.dto.Candidate.ResponseCandidateDTO;
import com.engrisk.enums.SexType;
import com.engrisk.models.Candidate;
import com.engrisk.utils.AlertUtils;
import com.engrisk.utils.DateUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.exceptions.UnirestException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import org.json.JSONObject;

import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;

public class CandidateFormController extends BaseFormController {

    public Candidate candidate = new Candidate();

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
    public void onSaveClick(ActionEvent event) throws JsonProcessingException, UnirestException {
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

        if (!phoneNumber.matches("^[+]*[(]{0,1}[0-9]{1,4}[)]{0,1}[-\\s\\./0-9]*$")) {
            AlertUtils.showWarning("Số điện thoại không hợp lệ");
            return;
        }

        JSONObject response;
        if (candidate.getId() == null) {
            CreateCandidateDTO candidateDto = new CreateCandidateDTO();
            candidateDto.setName(name);
            candidateDto.setBirthDate(DateUtils.parseDate(birthDate));
            candidateDto.setSex(sexType);
            candidateDto.setBirthPlace(birthDatePlace);
            candidateDto.setCitizenId(citizenId);
            candidateDto.setCitizenIdDate(DateUtils.parseDate(citizenIdDate));
            candidateDto.setCitizenIdPlace(citizenIdPlace);
            candidateDto.setEmail(email);
            candidateDto.setPhone(phoneNumber);

            ObjectMapper mapper = new ObjectMapper();
            String request = mapper.writeValueAsString(candidateDto);
           response = CallApi.post("candidate", request);

        } else {
            candidate.setName(name);
            candidate.setBirthDate(DateUtils.parseDate(birthDate));
            candidate.setSex(sexType);
            candidate.setBirthPlace(birthDatePlace);
            candidate.setCitizenId(citizenId);
            candidate.setCitizenIdDate(DateUtils.parseDate(citizenIdDate));
            candidate.setCitizenIdPlace(citizenIdPlace);
            candidate.setEmail(email);
            candidate.setPhone(phoneNumber);

            ObjectMapper mapper = new ObjectMapper();
            String request = mapper.writeValueAsString(candidate);
            response = CallApi.put("candidate", request);
        }

        if(response.toMap().containsKey("error")){
            AlertUtils.showWarning("Số căn cước công dân đã tồn tại trên hệ thống");
            return;
        }

        candidateTableController.loadData();
        closeWindow(event);
    }

    public void initSexTypeComboBox() {
        Callback<ListView<SexType>, ListCell<SexType>> factory = (lv) -> new ListCell<>() {
            @Override
            protected void updateItem(SexType item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : switch (item.name()) {
                    case "MALE" -> "Nam";
                    case "FEMALE" -> "Nữ";
                    case "OTHER" -> "Khác";
                    default -> "";
                });
            }
        };
        sexTypeComboBox.setCellFactory(factory);

        sexTypeComboBox.setButtonCell(factory.call(null));

        sexTypes.addAll(SexType.MALE, SexType.FEMALE, SexType.OTHER);

        sexTypeComboBox.setItems(sexTypes);
    }

    @Override
    public void initReadOnly() {
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

        if (candidate.getId() != null) {
            initFormValues();
        }
    }
}
