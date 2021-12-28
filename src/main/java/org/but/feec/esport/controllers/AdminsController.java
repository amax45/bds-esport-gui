package org.but.feec.esport.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import javafx.util.Duration;

import org.but.feec.esport.api.AdminBasicView;
import org.but.feec.esport.api.AdminDetailView;
import org.but.feec.esport.data.AdminRepository;
import org.but.feec.esport.service.AdminService;
import org.but.feec.esport.App;
import org.but.feec.esport.exceptions.ExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public class AdminsController {

    private static final Logger logger = LoggerFactory.getLogger(AdminsController.class);

    @FXML
    public Button addPersonButton;
    @FXML
    public Button refreshButton;
    @FXML
    private TableColumn<AdminBasicView, Long> adminsId;
    @FXML
    private TableColumn<AdminBasicView, String> adminsEmail;
    @FXML
    private TableColumn<AdminBasicView, String> adminsFamilyName;
    @FXML
    private TableColumn<AdminBasicView, String> adminsGivenName;
    @FXML
    private TableColumn<AdminBasicView, String> adminsNickname;
    @FXML
    private TableColumn<AdminBasicView, Timestamp> adminsTimeOfMatch;
    @FXML
    private TableView<AdminBasicView> systemAdminsTableView;
    @FXML
    private TableView<Timestamp> systemAdminsTable2View;
    @FXML
    private TableView<Timestamp> systemAdminsTable3View;

//    @FXML
//    public MenuItem exitMenuItem;

    private AdminService adminService;
    private AdminRepository adminRepository;

    public AdminsController() {
    }

    @FXML
    private void initialize() {
        adminRepository = new AdminRepository();
        adminService = new AdminService(adminRepository);
//        GlyphsDude.setIcon(exitMenuItem, FontAwesomeIcon.CLOSE, "1em");

        adminsId.setCellValueFactory(new PropertyValueFactory<AdminBasicView, Long>("id"));
        adminsEmail.setCellValueFactory(new PropertyValueFactory<AdminBasicView, String>("email"));
        adminsFamilyName.setCellValueFactory(new PropertyValueFactory<AdminBasicView, String>("familyName"));
        adminsGivenName.setCellValueFactory(new PropertyValueFactory<AdminBasicView, String>("givenName"));
        adminsNickname.setCellValueFactory(new PropertyValueFactory<AdminBasicView, String>("nickname"));
        adminsTimeOfMatch.setCellValueFactory(new PropertyValueFactory<AdminBasicView, Timestamp>("timeOfMatch"));


       ObservableList<AdminBasicView> observableAdminsList = initializeAdminsData();
       systemAdminsTableView.setItems(observableAdminsList);

        systemAdminsTableView.getSortOrder().add(adminsId);

        initializeTableViewSelection();
        //loadIcons();

        logger.info("AdminsController initialized");
    }

    private void initializeTableViewSelection() {
        MenuItem edit = new MenuItem("Edit admin");
        MenuItem detailedView = new MenuItem("Detailed admin view");
        MenuItem delete = new MenuItem("Delete admin");

        systemAdminsTableView.setRowFactory(tv -> {
            TableRow<AdminBasicView> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (! row.isEmpty() && event.getButton()==MouseButton.PRIMARY
                        && event.getClickCount() == 2) {

                    AdminBasicView clickedRow = row.getItem();
                    printRow(clickedRow);
                }
            });
            return row ;
        });

        edit.setOnAction((ActionEvent event) -> {
            AdminBasicView personView = systemAdminsTableView.getSelectionModel().getSelectedItem();
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(App.class.getResource("/org.but.feec.esport/fxml/AdminEdit.fxml"));
                Stage stage = new Stage();
                stage.setUserData(personView);
                stage.setTitle("BDS Esport Edit Admin");

                AdminsEditController controller = new AdminsEditController();
                controller.setStage(stage);
                fxmlLoader.setController(controller);

                Scene scene = new Scene(fxmlLoader.load(), 600, 500);

                stage.setScene(scene);

                stage.show();
            } catch (IOException ex) {
                ExceptionHandler.handleException(ex);
            }
        });

        detailedView.setOnAction((ActionEvent event) -> {
            AdminBasicView personView = systemAdminsTableView.getSelectionModel().getSelectedItem();
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(App.class.getResource("/org.but.feec.esport/fxml/AdminsDetailView.fxml"));
                Stage stage = new Stage();

                Long adminId = personView.getId();
                AdminDetailView adminDetailView = adminService.getAdminDetailView(adminId);

                stage.setUserData(adminDetailView);
                stage.setTitle("BDS Esport Admins Detailed View");

                AdminsDetailViewController controller = new AdminsDetailViewController();
                controller.setStage(stage);
                fxmlLoader.setController(controller);

                Scene scene = new Scene(fxmlLoader.load(), 600, 500);

                stage.setScene(scene);

                stage.show();
            } catch (IOException ex) {
                ExceptionHandler.handleException(ex);
            }
        });

        delete.setOnAction((ActionEvent event) -> {
            AdminBasicView adminView = systemAdminsTableView.getSelectionModel().getSelectedItem();
            try {
                adminRepository.deleteAdmin(adminView);
            } catch (SQLException ex) {
                ExceptionHandler.handleException(ex);
            }
            adminDeletedConfirmationDialog();
        });

        ContextMenu menu = new ContextMenu();
        menu.getItems().add(edit);
        menu.getItems().addAll(detailedView);
        menu.getItems().addAll(delete);
        systemAdminsTableView.setContextMenu(menu);
    }

    private ObservableList<AdminBasicView> initializeAdminsData() {
        List<AdminBasicView> persons = adminService.getAdminsBasicView();
        return FXCollections.observableArrayList(persons);
    }

    private void loadIcons() {
        Image vutLogoImage = new Image(App.class.getResourceAsStream("logos/lollogo.png"));
        ImageView vutLogo = new ImageView(vutLogoImage);
        vutLogo.setFitWidth(150);
        vutLogo.setFitHeight(50);
    }

    public void handleExitMenuItem(ActionEvent event) {
        System.exit(0);
    }

    public void handleAddPersonButton(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(App.class.getResource("/org.but.feec.esport/fxml/AdminsCreate.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 500);
            Stage stage = new Stage();
            stage.setTitle("BDS Esport Create Admin");
            stage.setScene(scene);

//            Stage stageOld = (Stage) signInButton.getScene().getWindow();
//            stageOld.close();
//
//            stage.getIcons().add(new Image(App.class.getResourceAsStream("logos/vut.jpg")));
//            authConfirmDialog();

            stage.show();
        } catch (IOException ex) {
            ExceptionHandler.handleException(ex);
        }
    }

    public void handleRefreshButton(ActionEvent actionEvent) {
        ObservableList<AdminBasicView> observableAdminsList = initializeAdminsData();
        systemAdminsTableView.setItems(observableAdminsList);
        systemAdminsTableView.refresh();
        systemAdminsTableView.sort();
    }


    private void printRow(AdminBasicView item) {
        ObservableList<AdminBasicView> observableAdminsList = initializeAdminsData();

        systemAdminsTable3View.setItems(FXCollections.observableArrayList(item.getTime_of_match()));

/*        systemAdminsTable3View.setCellFactory();
        systemAdminsTable3www.google.com/search?q=how+to+add+observaView.setCellFactory(new Callback<AdminBasicView<String>, javafx.scene.control.ListCell<String>>()
        {
            @Override
            public ListCell<String> call(AdminBasicView<String> systemAdminsTable3View)
            {
                return new systemAdminsTable3ViewCell();
            }
        });
*/


        System.out.println(item.getTime_of_match());
    }

    private void adminDeletedConfirmationDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Admin Deleted Confirmation");
        alert.setHeaderText("Your admin was successfully deleted.");

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
