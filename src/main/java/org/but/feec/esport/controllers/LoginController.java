package org.but.feec.esport.controllers;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.but.feec.esport.App;
import org.but.feec.esport.data.AdminRepository;
import org.but.feec.esport.exceptions.DataAccessException;
import org.but.feec.esport.exceptions.ExceptionHandler;
import org.but.feec.esport.exceptions.ResourceNotFoundException;
import org.but.feec.esport.service.AdminAuthService;
import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;

public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @FXML
    public Label usernameLabel;
    @FXML
    public Label passwordLabel;
    @FXML
    private Button signInButton;
    @FXML
    private Label mainLogo;
    @FXML
    private TextField usernameTextfield;
    @FXML
    private PasswordField passwordTextField;

    private AdminRepository adminRepository;
    private AdminAuthService authService;

    private ValidationSupport validation;

    public LoginController() {
    }

    @FXML
    private void initialize() {
        GlyphsDude.setIcon(signInButton, FontAwesomeIcon.SIGN_IN, "1em");
        GlyphsDude.setIcon(usernameLabel, FontAwesomeIcon.USER, "2em");
        GlyphsDude.setIcon(passwordLabel, FontAwesomeIcon.USER_SECRET, "2em");
        usernameTextfield.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleSignIn();
            }
        });
        passwordTextField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleSignIn();
            }
        });

        initializeLogos();
        initializeServices();
        initializeValidations();

        logger.info("LoginController initialized");
    }

    private void initializeValidations() {
        validation = new ValidationSupport();
        validation.registerValidator(usernameTextfield, Validator.createEmptyValidator("The username must not be empty."));
        validation.registerValidator(passwordTextField, Validator.createEmptyValidator("The password must not be empty."));
        signInButton.disableProperty().bind(validation.invalidProperty());
    }

    private void initializeServices() {
        adminRepository = new AdminRepository();
        authService = new AdminAuthService(adminRepository);
    }

    private void initializeLogos() {
        Image mainlogo = new Image(App.class.getResourceAsStream("/org.but.feec.esport/logos/loko.png"));
        ImageView mainLogoImage = new ImageView(mainlogo);
        mainLogoImage.setFitHeight(135);
        mainLogoImage.setFitWidth(500);
        mainLogoImage.setPreserveRatio(true);
        mainLogo.setGraphic(mainLogoImage);
    }

    public void signInActionHandler(ActionEvent event) {
        handleSignIn();
    }

    private void handleSignIn() {
        String username = usernameTextfield.getText();
        String password = passwordTextField.getText();

        try {
            boolean authenticated = authService.authenticate(username, password);
            if (authenticated) {
                showAdminsView();
            } else {
                showInvalidPaswordDialog();
            }
        } catch (ResourceNotFoundException | DataAccessException e) {
            showInvalidPaswordDialogs();
        }
    }

    private void showAdminsView() {
        try {
            System.out.println("Ahoj");
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(App.class.getResource("/org.but.feec.esport/fxml/Persons.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1050, 600);
            Stage stage = new Stage();
            stage.setTitle("BDS Esport Demo App");
            stage.setScene(scene);

            Stage stageOld = (Stage) signInButton.getScene().getWindow();
            stageOld.close();

            stage.getIcons().add(new Image(App.class.getResourceAsStream("/org.but.feec.esport/logos/lollogo.png")));
            authConfirmDialog();

            stage.show();
        } catch (IOException ex) {
            ExceptionHandler.handleException(ex);
        }
    }

    private void showInvalidPaswordDialog() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Unauthenticated");
        alert.setHeaderText("The user is not authenticated");
        alert.setContentText("Please provide a valid username and password");//ww  w . j  a  va2s  .  co  m

        alert.showAndWait();
    }

    private void showInvalidPaswordDialogs() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Eror");
        alert.setHeaderText("The user is not authenticated");
        alert.setContentText("Please provide a valid username and password");//ww  w . j  a  va2s  .  co  m

        alert.showAndWait();
    }


    private void authConfirmDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logging confirmation");
        alert.setHeaderText("You were successfully logged in.");

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

        if (result.get() == ButtonType.OK) {
            System.out.println("ok clicked");
        } else if (result.get() == ButtonType.CANCEL) {
            System.out.println("cancel clicked");
        }
    }

    public void handleOnEnterActionPassword(ActionEvent dragEvent) {
        handleSignIn();
    }
}

