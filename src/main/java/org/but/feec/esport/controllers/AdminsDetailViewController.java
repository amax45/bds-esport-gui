package org.but.feec.esport.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.but.feec.esport.api.AdminDetailView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdminsDetailViewController {

    private static final Logger logger = LoggerFactory.getLogger(AdminsDetailViewController.class);

    @FXML
    private TextField idTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField givenNameTextField;

    @FXML
    private TextField familyNameTextField;

    @FXML
    private TextField nicknameTextField;


    // used to reference the stage and to get passed data through it
    public Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void initialize() {
        idTextField.setEditable(false);
        emailTextField.setEditable(false);
        givenNameTextField.setEditable(false);
        familyNameTextField.setEditable(false);
        nicknameTextField.setEditable(false);


        loadAdminsData();

        logger.info("AdminsDetailViewController initialized");
    }

    private void loadAdminsData() {
        Stage stage = this.stage;
        if (stage.getUserData() instanceof AdminDetailView) {
            AdminDetailView adminBasicView = (AdminDetailView) stage.getUserData();
            idTextField.setText(String.valueOf(adminBasicView.getId()));
            emailTextField.setText(adminBasicView.getEmail());
            givenNameTextField.setText(adminBasicView.getGivenName());
            familyNameTextField.setText(adminBasicView.getFamilyName());
            nicknameTextField.setText(adminBasicView.getNickname());
        }
    }

}
