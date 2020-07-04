package UI.NewRideScreen;

import Manager.TransPoolManager;
import SystemEngine.Stop;
import SystemLogic.ModelLogic;
import UI.StartScreen.MyController;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.sun.deploy.security.SelectableSecurityManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;

import java.time.LocalDate;
import java.time.LocalTime;

public class NewRideController {

    @FXML
    private JFXComboBox<Integer> DayComboBox;

    @FXML
    private JFXComboBox<Integer> HourComboBox;

    @FXML
    private JFXComboBox<Integer> MinutesComboBox;

    @FXML
    private JFXComboBox<String> TimeCombobox;

    @FXML
    private JFXTextField FirstNameTextField;

    @FXML
    private JFXTextField LastNameTextField;

    @FXML
    private JFXComboBox<String> CurrentStopComboBox;

    @FXML
    private JFXComboBox<String> DestinitionStopComboBox;

    TransPoolManager manager = MyController.getTransPoolManager();


    @FXML
    private void initialize() {
        HourComboBox.getItems().addAll(0,1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23);
        MinutesComboBox.getItems().addAll(0, 5,  10, 15,20, 25, 30,  35, 40,  45,  50, 55);
        for(int i = 1; i <= 365; i++)
            DayComboBox.getItems().add(i);

        TimeCombobox.getItems().addAll("Future departure time", "Desired arrival time");

        for (Stop stop : manager.getModelLogic().getStops()) {
            CurrentStopComboBox.getItems().add(stop.getName());
            DestinitionStopComboBox.getItems().add(stop.getName());
        }

    }


    @FXML
    void OnAddRequestClicked(ActionEvent event) {
        String FirstName = FirstNameTextField.getText();
        String LastName = LastNameTextField.getText();
        String CurrentStop = CurrentStopComboBox.getSelectionModel().getSelectedItem();
        String DestinationStop = DestinitionStopComboBox.getSelectionModel().getSelectedItem();
        Boolean IsFutureArrivalTime = null;
        if(TimeCombobox.getValue() != null) {
            if (TimeCombobox.getSelectionModel().getSelectedItem().equals("Future departure time")) {
                IsFutureArrivalTime = false;
            } else {
                IsFutureArrivalTime = true;
            }
        }
        Integer Hour = HourComboBox.getValue();
        Integer Minutes = MinutesComboBox.getValue();

        Integer Day = DayComboBox.getValue();

        String Msg = manager.getModelLogic().NewRideInputCheck(FirstName,LastName,CurrentStop
                ,DestinationStop,Hour, Minutes ,Day,IsFutureArrivalTime);
       if(!Msg.equals(""))
       {
           Alert alert = new Alert(Alert.AlertType.INFORMATION);
           alert.setTitle("Input Error");
           alert.setHeaderText("Input Error");

           TextArea area = new TextArea(Msg);
           alert.getDialogPane().setExpandableContent(area);
           alert.showAndWait();

       }
       else {
           LocalTime Time = LocalTime.of(HourComboBox.getValue(),MinutesComboBox.getValue());
           manager.getModelLogic().CreateNewRequest(FirstName, LastName, CurrentStop, DestinationStop,
                   Time.getHour(), Time.getMinute(), Day, IsFutureArrivalTime);
           Alert al = new Alert(Alert.AlertType.INFORMATION);
           al.setTitle("Success");
           al.setContentText("Request as been added");
           al.setHeaderText("Thank you!");
           al.showAndWait();
       }

        FirstNameTextField.clear();
        LastNameTextField.clear();
        CurrentStopComboBox.getSelectionModel().clearSelection();
        DestinitionStopComboBox.getSelectionModel().clearSelection();
        DayComboBox.getSelectionModel().clearSelection();
        HourComboBox.getSelectionModel().clearSelection();
        MinutesComboBox.getSelectionModel().clearSelection();
        TimeCombobox.getSelectionModel().getSelectedItem();
    }

}
