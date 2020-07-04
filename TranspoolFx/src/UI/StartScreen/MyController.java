package UI.StartScreen;

import CustomClasses.TripRequest;
import Manager.TransPoolManager;
import SystemEngine.ObjectFactory;
import SystemEngine.Route;
import SystemEngine.Scheduling;
import SystemEngine.TransPoolTrip;
import Tasks.TaskFile;
import com.jfoenix.controls.JFXButton;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import javafx.scene.image.ImageView ;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class MyController {

    @FXML
    private ImageView EscapeImageView;

    @FXML
    private JFXButton MainButton;

    @FXML
    private JFXButton NewRideButton;

    @FXML
    private JFXButton NewRideButton1;

    @FXML
    private JFXButton NewRideButton11;

    @FXML
    private Button UploadXMlButton;

    @FXML
    private JFXButton NewRideButton111;

    @FXML
    private JFXButton NewRideButton1111;

    @FXML
    private AnchorPane DynamicPane;

    @FXML
    private BorderPane DynamicBorderPane;


    private static TransPoolManager transPoolManager;
    private Stage primaryStage;
    private SimpleStringProperty selectedFileProperty;
    private SimpleBooleanProperty isFileSelected;
    private boolean xmlUploaded;
    private ObjectFactory factory = new ObjectFactory();
    private BooleanProperty TransPoolUploadedProperty = new SimpleBooleanProperty(false);
    private BooleanProperty TransPoolLoad = new SimpleBooleanProperty(false);

    public MyController() {
        selectedFileProperty = new SimpleStringProperty();
        isFileSelected = new SimpleBooleanProperty(false);
        transPoolManager = new TransPoolManager();
    }


    @FXML
    private void initialize() throws IOException {
        AnchorPane OpenPane = FXMLLoader.load(getClass().getResource("/UI/OpenScreen/OpenScreen.fxml"));
        DynamicPane.getChildren().setAll(OpenPane);
        AnchorPane.setBottomAnchor(OpenPane,0.0);
        AnchorPane.setLeftAnchor(OpenPane,0.0);
        AnchorPane.setRightAnchor(OpenPane,0.0);
        AnchorPane.setTopAnchor(OpenPane,0.0);
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public static TransPoolManager getTransPoolManager() {
        return transPoolManager;
    }

    public void setTransPoolManager(TransPoolManager transPoolManager) {
        this.transPoolManager = transPoolManager;
    }

    @FXML
    void MainScreenAction(ActionEvent event) throws IOException {
        AnchorPane Mainpane = FXMLLoader.load(getClass().getResource("/UI/MainScreen/Mainpain.fxml"));
        DynamicPane.getChildren().setAll(Mainpane);
        AnchorPane.setBottomAnchor(Mainpane,0.0);
        AnchorPane.setLeftAnchor(Mainpane,0.0);
        AnchorPane.setRightAnchor(Mainpane,0.0);
        AnchorPane.setTopAnchor(Mainpane,0.0);
    }

    @FXML
    void NewRequestScreenAction(ActionEvent event) throws IOException {
        AnchorPane NewRidePane = FXMLLoader.load(getClass().getResource("/UI/NewRideScreen/NewrideScreen.fxml"));
        DynamicPane.getChildren().setAll(NewRidePane);
        AnchorPane.setBottomAnchor(NewRidePane,0.0);
        AnchorPane.setLeftAnchor(NewRidePane,0.0);
        AnchorPane.setRightAnchor(NewRidePane,0.0);
        AnchorPane.setTopAnchor(NewRidePane,0.0);
    }

    @FXML
    void NewOfferScreenAction(ActionEvent event) throws IOException {
        AnchorPane NewOfferPane = FXMLLoader.load(getClass().getResource("/UI/TripOfferScreen/TripOfferScreen.fxml"));
        DynamicPane.getChildren().setAll(NewOfferPane);
        AnchorPane.setBottomAnchor(NewOfferPane,0.0);
        AnchorPane.setLeftAnchor(NewOfferPane,0.0);
        AnchorPane.setRightAnchor(NewOfferPane,0.0);
        AnchorPane.setTopAnchor(NewOfferPane,0.0);
    }

    @FXML
    void FindMatchScreenAction(ActionEvent event) throws IOException {
        AnchorPane FindMatchPane = FXMLLoader.load(getClass().getResource("/UI/MatchScreen/MatchScreen.fxml"));
        DynamicPane.getChildren().setAll(FindMatchPane);
        AnchorPane.setBottomAnchor(FindMatchPane,0.0);
        AnchorPane.setLeftAnchor(FindMatchPane,0.0);
        AnchorPane.setRightAnchor(FindMatchPane,0.0);
        AnchorPane.setTopAnchor(FindMatchPane,0.0);


    }
    
    @FXML
    void AboutUsScreenAction(ActionEvent event) throws IOException {
        AnchorPane AboutUsPane = FXMLLoader.load(getClass().getResource("/UI/AboutUsScreen/AboutUsScreen.fxml"));
        DynamicPane.getChildren().setAll(AboutUsPane);
        AnchorPane.setBottomAnchor(AboutUsPane,0.0);
        AnchorPane.setLeftAnchor(AboutUsPane,0.0);
        AnchorPane.setRightAnchor(AboutUsPane,0.0);
        AnchorPane.setTopAnchor(AboutUsPane,0.0);

    }


    @FXML
    void OnEscape(MouseEvent event) {
       System.exit(0);
    }


    @FXML
    public void openFileButtonAction(ActionEvent actionEvent) {
        try {
            initialize();
        } catch (IOException e) {
            e.printStackTrace();
        }
       FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select XMl file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml files", "*.xml"));
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile != null) {
        //File selectedFile = new File("C:\\Users\\mishe\\Desktop\\vers319.6\\Transpool\\TranspoolLogic\\src\\testFile\\ex2-small.xml");
            this.StartLoadFromXml(selectedFile);
        }
    }

    public void StartLoadFromXml(File file) {
        TaskFile loadXmlFileTask = new TaskFile(this.transPoolManager, file);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Loading File");
        try {
            Thread loadXMLThread = new Thread(loadXmlFileTask);
            alert.contentTextProperty().bind(loadXmlFileTask.messageProperty());
            alert.show();
            loadXMLThread.start();
            loadXmlFileTask.setOnSucceeded((event) -> {
                if ((Boolean)loadXmlFileTask.getValue()) {
                    this.xmlUploaded = true;
                    this.MainButton.setDisable(false);
                    this.NewRideButton.setDisable(false);
                    this.NewRideButton1.setDisable(false);
                    this.NewRideButton11.setDisable(false);
                    if (!this.xmlUploaded) {
                        this.TransPoolUploadedProperty.setValue(true);
                    } else {
                        this.TransPoolUploadedProperty.setValue(false);
                        this.TransPoolUploadedProperty.setValue(true);
                        if (this.TransPoolUploadedProperty.getValue()) {
                            this.TransPoolUploadedProperty.setValue(false);
                        }
                    }
                } else {
                    this.xmlUploaded = false;
                    transPoolManager = new TransPoolManager();
                    this.MainButton.setDisable(true);
                    this.NewRideButton.setDisable(true);
                    this.NewRideButton1.setDisable(true);
                    this.NewRideButton11.setDisable(true);
                }

                this.TransPoolLoad.setValue((Boolean)loadXmlFileTask.getValue());
                alert.close();
            });


        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}
