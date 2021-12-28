package org.but.feec.esport.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.but.feec.esport.api.AdminBasicView;
import org.but.feec.esport.api.AdminEditView;
import org.but.feec.esport.data.AdminRepository;
import org.but.feec.esport.service.AdminService;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;


public class AdminsEditController {

    private static final Logger logger = LoggerFactory.getLogger(AdminsEditController.class);

    @FXML
    public Button editPersonButton;
    @FXML
    public TextField idTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField givenNameTextField;
    @FXML
    private TextField familyNameTextField;
    @FXML
    private TextField nicknameTextField;

    private AdminService admineService;
    private AdminRepository adminRepository;
    private ValidationSupport validation;

    public Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void initialize() {
        adminRepository = new AdminRepository();
        admineService = new AdminService(adminRepository);

        validation = new ValidationSupport();
        validation.registerValidator(idTextField, Validator.createEmptyValidator("The id must not be empty."));
        idTextField.setEditable(false);
        validation.registerValidator(emailTextField, Validator.createEmptyValidator("The email must not be empty."));
        validation.registerValidator(givenNameTextField, Validator.createEmptyValidator("The first name must not be empty."));
        validation.registerValidator(familyNameTextField, Validator.createEmptyValidator("The last name must not be empty."));
        validation.registerValidator(nicknameTextField, Validator.createEmptyValidator("The nickname must not be empty."));

        editPersonButton.disableProperty().bind(validation.invalidProperty());

        loadAdminsData();

        logger.info("AdminsEditController initialized");
    }

    /**
     * Load passed data from Admins controller. Check this tutorial explaining how to pass the data between controllers: https://dev.to/devtony101/javafx-3-ways-of-passing-information-between-scenes-1bm8
     */
    private void loadAdminsData() {
        Stage stage = this.stage;
        if (stage.getUserData() instanceof AdminBasicView) {
            AdminBasicView adminBasicView = (AdminBasicView) stage.getUserData();
            idTextField.setText(String.valueOf(adminBasicView.getId()));
            emailTextField.setText(adminBasicView.getEmail());
            givenNameTextField.setText(adminBasicView.getGivenName());
            familyNameTextField.setText(adminBasicView.getFamilyName());
            nicknameTextField.setText(adminBasicView.getNickname());
        }
    }

    @FXML
    public void handleEditPersonButton(ActionEvent event) {
        Long id = Long.valueOf(idTextField.getText());
        String email = emailTextField.getText();
        String firstName = givenNameTextField.getText();
        String lastName = familyNameTextField.getText();
        String nickname = nicknameTextField.getText();

        AdminEditView adminEditView = new AdminEditView();
        adminEditView.setId(id);
        adminEditView.setEmail(email);
        adminEditView.setGivenName(firstName);
        adminEditView.setFamilyName(lastName);
        adminEditView.setNickname(nickname);

        admineService.editAdmin(adminEditView);

        adminEditedConfirmationDialog();
    }

    private void adminEditedConfirmationDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Admin Edited Confirmation");
        alert.setHeaderText("Your admin was successfully edited.");

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
