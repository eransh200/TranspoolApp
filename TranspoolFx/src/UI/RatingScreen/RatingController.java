package UI.RatingScreen;

import CustomClasses.TripRequest;
import Manager.TransPoolManager;
import SystemEngine.TransPoolTrip;
import UI.MainScreen.ActionButtonTableCell;
import UI.StartScreen.MyController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class RatingController {

    @FXML
    private TableColumn<TransPoolTrip, Integer> OrderNumber;

    @FXML
    private TableColumn<TransPoolTrip, String> Name;

    @FXML
    private TableColumn<TransPoolTrip, Float>Rating;

    @FXML
    private TableView<TransPoolTrip> Table;

    @FXML
    private TableColumn<TransPoolTrip, Button>YourRating;



    TransPoolManager manager = MyController.getTransPoolManager();
    ObservableList<TransPoolTrip> Rides = FXCollections.observableArrayList();

    @FXML
    private void initialize() {

        Table.setEditable(true);
        List<TransPoolTrip> transPoolTrips = manager.findTripsToRating();
        OrderNumber.setCellValueFactory(new PropertyValueFactory<TransPoolTrip,Integer>("OrderNumber"));
        Name.setCellValueFactory(new PropertyValueFactory<TransPoolTrip,String>("Owner"));
        YourRating.setCellFactory(ActionButtonTableCell.<TransPoolTrip>forTableColumn("RATE", (TransPoolTrip s) -> {
            try {
                ButtonRatingCLicked(s);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return s;
        }));
        for(TransPoolTrip SingleTrip :transPoolTrips)
        {
            Rides.add(SingleTrip);
        }

      Table.setItems(Rides);
    }


    private void ButtonRatingCLicked(TransPoolTrip transPoolTrip) throws IOException {
        AnchorPane ChoosePane = FXMLLoader.load(getClass().getResource("/UI/RatingScreen/RateScreen.fxml"));
        Scene secondScene = new Scene(ChoosePane, 468, 250);
        manager.setTripToRate(transPoolTrip);
        Stage newWindow = new Stage();
        newWindow.setTitle("Rate");
        newWindow.setScene(secondScene);
        newWindow.initModality(Modality.WINDOW_MODAL);
        newWindow.setX(300);
        newWindow.setY(200);
        newWindow.showAndWait();
    }
}
