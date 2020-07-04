package UI.MatchScreen;

import CustomClasses.TripRequest;
import Manager.TransPoolManager;
import SystemLogic.SingleValidMatchedOffers;
import SystemLogic.ValidMatchedOffers;
import UI.MainScreen.ActionButtonTableCell;
import UI.StartScreen.MyController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

public class ChooseScreenController {


    @FXML
    private AnchorPane ChoosePane;
    @FXML
    private TableColumn<SingleValidMatchedOffers, Integer>OfferNumber;
    @FXML
    private TableColumn<SingleValidMatchedOffers, String>offerType;

    @FXML
    private TableColumn<SingleValidMatchedOffers, String>estimatedTime;

    @FXML
    private TableColumn<SingleValidMatchedOffers, Float>passengerAvgFuelUtilization;

    @FXML
    private TableColumn<SingleValidMatchedOffers, Integer> offerCost;

    @FXML
    private TableColumn<SingleValidMatchedOffers, Button>InformationList;
    @FXML
    private TableColumn<SingleValidMatchedOffers, Button>OfferMatchClick;

    @FXML
    private TableView<SingleValidMatchedOffers> Table;

    TransPoolManager manager = MyController.getTransPoolManager();
    ObservableList<SingleValidMatchedOffers> Offers = FXCollections.observableArrayList();


    @FXML
    private void initialize() {

        SetTimeText();
        SetOffersTable();
    }

    public void SetTimeText() {
        if(manager.getTripRequest().getIsFutureTime() == false)
            estimatedTime.setText("Departure Time");
        else
            estimatedTime.setText("Arrival Time");
    }

    private void ButtonInformationCLicked(SingleValidMatchedOffers singleValidMatchedOffers)
    {
        Alert InformationAlert = new Alert(Alert.AlertType.INFORMATION);

            InformationAlert.setContentText(manager.getModelLogic().CreateOfferInformationAlertMsg(singleValidMatchedOffers));
            InformationAlert.setTitle("Information");
            InformationAlert.setHeaderText("Offer in Details");
            InformationAlert.showAndWait();


    }

    private void ButtonMatchCLicked(SingleValidMatchedOffers singleValidMatchedOffers) throws IOException {
      Integer requestOrderNum = manager.getTripRequest().getOrderNumber();
        Optional<TripRequest> tripRequestMatch = manager.getModelLogic().getTripRequests().getTripRequests().stream().filter(tripRequest -> tripRequest.getOrderNumber().equals(requestOrderNum)).findFirst();
        if(tripRequestMatch.isPresent())
        {
            tripRequestMatch.get().setMatchTrip(singleValidMatchedOffers);
            manager.getModelLogic().updateCapacityAfterMatch(singleValidMatchedOffers, tripRequestMatch.get());
            Alert InformationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            InformationAlert.setContentText("The Request was matched successfully");
            InformationAlert.setTitle("Information");
            InformationAlert.setHeaderText("This is a Match");
            Table.setVisible(false);
            AnchorPane RatingPane = FXMLLoader.load(getClass().getResource("/UI/RatingScreen/RatingScreen.fxml"));
            ChoosePane.getChildren().setAll(RatingPane);
            AnchorPane.setBottomAnchor(RatingPane,0.0);
            AnchorPane.setLeftAnchor(RatingPane,0.0);
            AnchorPane.setRightAnchor(RatingPane,0.0);
            AnchorPane.setTopAnchor(RatingPane,0.0);
            InformationAlert.showAndWait();
        }

    }

    public void  SetOffersTable() {
        ValidMatchedOffers validMatchedOffers =  manager.createRideOffersForMatch(manager.getNumOffersToShow(), manager.getTripRequest());
        OfferNumber.setCellValueFactory(new PropertyValueFactory<SingleValidMatchedOffers,Integer>("offerNumber"));
        offerType.setCellValueFactory(new PropertyValueFactory<SingleValidMatchedOffers,String>("offerType"));
        passengerAvgFuelUtilization.setCellValueFactory(new PropertyValueFactory<SingleValidMatchedOffers,Float>("passengerAvgFuelUtilization"));
        offerCost.setCellValueFactory(new PropertyValueFactory<SingleValidMatchedOffers,Integer>("offerCost"));
       estimatedTime.setCellValueFactory(new PropertyValueFactory<SingleValidMatchedOffers,String>("estimatedTimeString"));
        InformationList.setCellFactory(ActionButtonTableCell.<SingleValidMatchedOffers>forTableColumn("Information", (SingleValidMatchedOffers s) -> {
            ButtonInformationCLicked(s);
            return s;
       }));
        OfferMatchClick.setCellFactory(ActionButtonTableCell.<SingleValidMatchedOffers>forTableColumn("Match", (SingleValidMatchedOffers s) -> {
            try {
                ButtonMatchCLicked(s);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return s;
        }));
        for(SingleValidMatchedOffers singleValidMatchedOffers:validMatchedOffers.getValidOffers())
            Offers.add(singleValidMatchedOffers);

        Table.setItems(Offers);
    }
}
