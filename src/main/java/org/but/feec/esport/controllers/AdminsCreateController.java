package org.but.feec.esport.controllers;

import org.but.feec.esport.api.AdminCreateView;
import org.but.feec.esport.data.AdminRepository;
import org.but.feec.esport.service.AdminService;

import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.util.Duration;

public class AdminsCreateController {

    private static final Logger logger = LoggerFactory.getLogger(AdminsCreateController.class);

    @FXML
    public Button newAdminCreatePerson;
    @FXML
    private TextField newAdminEmail;

    @FXML
    private TextField newAdminGivenName;

    @FXML
    private TextField newAdminFamilyName;

    @FXML
    private TextField newAdminNickname;

    @FXML
    private TextField newAdminPassword;

    private AdminService adminService;
    private AdminRepository adminRepository;
    private ValidationSupport validation;

    @FXML
    public void initialize() {
        adminRepository = new AdminRepository();
        adminService = new AdminService(adminRepository);

        validation = new ValidationSupport();
        validation.registerValidator(newAdminEmail, Validator.createEmptyValidator("The email must not be empty."));
        validation.registerValidator(newAdminGivenName, Validator.createEmptyValidator("The first name must not be empty."));
        validation.registerValidator(newAdminFamilyName, Validator.createEmptyValidator("The last name must not be empty."));
        validation.registerValidator(newAdminNickname, Validator.createEmptyValidator("The nickname must not be empty."));
        validation.registerValidator(newAdminPassword, Validator.createEmptyValidator("The password must not be empty."));

        newAdminCreatePerson.disableProperty().bind(validation.invalidProperty());

        logger.info("AdminCreateController initialized");
    }

    @FXML
    void handleCreateNewAdmin(ActionEvent event) {
        // can be written easier, its just for better explanation here on so many lines
        String email = newAdminEmail.getText();
        String givenName = newAdminGivenName.getText();
        String familyName = newAdminFamilyName.getText();
        String nickname = newAdminNickname.getText();
        String password = newAdminPassword.getText();

        AdminCreateView adminCreateView = new AdminCreateView();
        adminCreateView.setPassword (password.toCharArray());
        adminCreateView.setEmail(email);
        adminCreateView.setGivenName(givenName);
        adminCreateView.setFamilyName(familyName);
        adminCreateView.setNickname(nickname);

        adminService.createAdmin(adminCreateView);

        adminCreatedConfirmationDialog();
    }

    private void adminCreatedConfirmationDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Admin Created Confirmation");
        alert.setHeaderText("Your admin was successfully created.");

        Timeline idlestage = new Timeline(new KeyFrame(Duration.seconds(3), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                alert.setResult(ButtonType.CANCEL);
                alert.hide();
            }
        }));
        idlestage.setCycleCount(1);
        idlestage.play();
        Optional<ButtonType> result = alert.showAndWait();
    }

}
