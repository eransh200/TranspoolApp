package UI.TripOfferScreen;

import Manager.TransPoolManager;
import SystemEngine.Route;
import SystemEngine.Stop;
import SystemLogic.ModelLogic;
import UI.StartScreen.MyController;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalTime;

public class TripOfferController {

    @FXML
    private JFXTextField LastNameTextField;

    @FXML
    private JFXComboBox<String> CurrentStopComboBox;

    @FXML
    private JFXTextField FirstNameTextField;

    @FXML
    private JFXComboBox<Integer> DayComboBox;

    @FXML
    private JFXComboBox<Integer> HourComboBox;

    @FXML
    private JFXComboBox<Integer> MinutesComboBox;


    @FXML
    private JFXComboBox<Integer> PriceCombobox;

    @FXML
    private JFXComboBox<String> SchedulingComboBox;

    @FXML
    private JFXComboBox<Integer> CapacityCombobox;



    @FXML
    private Label Routelable;


    ObservableList<Integer> options = FXCollections.observableArrayList(1,2,3,4,5);


    TransPoolManager manager = MyController.getTransPoolManager();

    private String prop =  "";

    @FXML
    private void initialize() {
        HourComboBox.getItems().addAll(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23);
        MinutesComboBox.getItems().addAll(0,5,10,15,20, 25,30,35,40,45,50,55);
        for(int i = 1; i <= 365; i++)
            DayComboBox.getItems().add(i);
        for(int i = 1; i <= 100; i++)
            PriceCombobox.getItems().add(i);
        CapacityCombobox.getItems().addAll(1,2,3,4,5,6,7);
        SchedulingComboBox.getItems().addAll("Daily Travel", "Two-day Travel", "Weekly Travel", "Monthly Travel");
        CurrentStopComboBox.getItems().add("Clear Your Route");
        for(Stop stop :manager.getModelLogic().getStops())
        {
            CurrentStopComboBox.getItems().add(stop.getName());
        }

        CurrentStopComboBox.setTooltip(new Tooltip("please choose your route by stop by stop in the exactly order \n" +
                "You can see it in the route label under"));


        // Create action event
        EventHandler<ActionEvent> event =
                new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent e)
                    {
                        if(CurrentStopComboBox.getValue().equals("Clear Your Route") || CurrentStopComboBox.getValue().equals("Route"))
                        {
                            prop= "Your Route: ";
                        }
                        else {
                            prop += CurrentStopComboBox.getValue() + ",";
                        }
                        Routelable.setText(prop);

                    }
                };

        // Set on action
        CurrentStopComboBox.setOnAction(event);


    }


    @FXML
    void OnRouteClicked(ActionEvent event) {


    }


    @FXML
    void OnAddTripOfferClicked(ActionEvent event) {

        String FirstName = FirstNameTextField.getText();
        String LastName = LastNameTextField.getText();
        String Route =Routelable.getText();
        Integer Day = DayComboBox.getValue();
        Integer Hour = HourComboBox.getValue();
        Integer Minutes = MinutesComboBox.getValue();
        String SchedulingTravel = SchedulingComboBox.getValue();
        Integer ppk = PriceCombobox.getValue();
        Integer Capacity = CapacityCombobox.getValue();


        String Msg = manager.getModelLogic().NewOfferInputCheck(FirstName,LastName,Route,Day,Hour,Minutes,ppk,Capacity);
        if(!Msg.equals(""))
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Input Error");
            alert.setHeaderText("Input Error");

            TextArea area = new TextArea(Msg);
            alert.getDialogPane().setExpandableContent(area);
            alert.showAndWait();

        }
        else {
            LocalTime Time = LocalTime.of(HourComboBox.getValue(),MinutesComboBox.getValue());
            manager.getModelLogic().CreateNewTranspoolTrip(
                    FirstName, LastName, Route, Day, Time.getHour(),Time.getMinute(), ppk, Capacity,SchedulingTravel);
            Alert al = new Alert(Alert.AlertType.INFORMATION);
            al.setTitle("Success");
            al.setContentText("Request as been added");
            al.setHeaderText("Thank you!");
            al.showAndWait();

        }

       FirstNameTextField.clear();
       LastNameTextField.clear();
       CurrentStopComboBox.setValue("Route");
       DayComboBox.getSelectionModel().clearSelection();
       HourComboBox.getSelectionModel().clearSelection();
       MinutesComboBox.getSelectionModel().clearSelection();
       SchedulingComboBox.getSelectionModel().clearSelection();
       PriceCombobox.getSelectionModel().clearSelection();
       CapacityCombobox.getSelectionModel().clearSelection();


    }






}
