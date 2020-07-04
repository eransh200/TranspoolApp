package UI.MatchScreen;

import CustomClasses.TripRequest;
import CustomClasses.TripRequests;
import Manager.TransPoolManager;
import SystemEngine.PlannedTrips;
import UI.StartScreen.MyController;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.Optional;

public class MatchController {

    @FXML
    private TableColumn<TripRequest, Integer> OrderNumber;

    @FXML
    private TableColumn<TripRequest, String> FirstName;

    @FXML
    private TableColumn<TripRequest, String> LastName;

    @FXML
    private TableColumn<TripRequest, String> CurrentStop;

    @FXML
    private TableColumn<TripRequest, String> DestinitionStop;

    @FXML
    private TableColumn<TripRequest, Integer> HourTableCol;

    @FXML
    private TableColumn<TripRequest, Integer> MinutesTableCol;

    @FXML
    private TableColumn<TripRequest, Integer> DayTableCol;

    @FXML
    private TableView<TripRequest> Table;

    @FXML
    private AnchorPane DynamicPaneMatch;

    @FXML
    private JFXComboBox<Integer> RideRequestNumCombobox =   new JFXComboBox<>();

    @FXML
    private JFXComboBox<Integer> NumOffersCombobox =   new JFXComboBox<>();

    @FXML
    private Button FindMatchButton;

    TransPoolManager manager = MyController.getTransPoolManager();
    ObservableList<TripRequest> Ridelist = FXCollections.observableArrayList();

    public MatchController() {
    }

    @FXML
    private void initialize() {

        SetRidesTable();
        PlannedTrips plannedTrips = manager.getModelLogic().transPool.getPlannedTrips();
        if(plannedTrips != null)
        {
            for(int i = 1; i <= 10; i++)
                NumOffersCombobox.getItems().add(i);
        }
    }

    public void SetRidesTable() {
        TripRequests tripRequests = manager.getModelLogic().getTripRequests();
        OrderNumber.setCellValueFactory(new PropertyValueFactory<TripRequest,Integer>("OrderNumber"));
        FirstName.setCellValueFactory(new PropertyValueFactory<TripRequest,String>("FirstName"));
        LastName.setCellValueFactory(new PropertyValueFactory<TripRequest, String>("LastName"));
        CurrentStop.setCellValueFactory(new PropertyValueFactory<TripRequest, String>("CurrentLocation"));
        DestinitionStop.setCellValueFactory(new PropertyValueFactory<TripRequest, String>("Destination"));
        HourTableCol.setCellValueFactory(new PropertyValueFactory<TripRequest, Integer>("Hour"));
        MinutesTableCol.setCellValueFactory(new PropertyValueFactory<TripRequest, Integer>("Minutes"));
        DayTableCol.setCellValueFactory(new PropertyValueFactory<TripRequest, Integer>("Day"));




        for(TripRequest SingleRequest : tripRequests.getTripRequests())
        {
            if(SingleRequest.getMatchTrip() == null) {
                Ridelist.add(SingleRequest);
                RideRequestNumCombobox.getItems().add(SingleRequest.getOrderNumber());
            }
        }

        Table.setItems(Ridelist);
    }


    public void FindMatchButtonClicked(ActionEvent actionEvent) throws IOException {

        Integer tripRequestIndex = RideRequestNumCombobox.getSelectionModel().getSelectedItem();
        Integer numOffers = NumOffersCombobox.getSelectionModel().getSelectedItem();
        Optional<TripRequest> tripRequest = Ridelist.stream().filter(t -> t.getOrderNumber() == tripRequestIndex).findFirst();
        manager.setNumOffersToShow(numOffers);
        manager.setTripRequest(tripRequest.get());
        AnchorPane ChoosePane = FXMLLoader.load(getClass().getResource("/UI/MatchScreen/ChooseScreen.fxml"));
        DynamicPaneMatch.getChildren().setAll(ChoosePane);
        AnchorPane.setBottomAnchor(ChoosePane,0.0);
        AnchorPane.setLeftAnchor(ChoosePane,0.0);
        AnchorPane.setRightAnchor(ChoosePane,0.0);
        AnchorPane.setTopAnchor(ChoosePane,0.0);

    }

    public void RideRequestNumAction(ActionEvent actionEvent) {
        if(isInputSelected())
            FindMatchButton.setDisable(false);
    }

    public void NumOffersAction(ActionEvent actionEvent) {
        if(isInputSelected())
            FindMatchButton.setDisable(false);
    }

    private  boolean isInputSelected()
    {
        if(NumOffersCombobox.getSelectionModel().getSelectedItem() != null && RideRequestNumCombobox.getSelectionModel() .getSelectedItem() != null)
            return true;
        return false;
    }

}
