package UI.MainScreen;



import CustomClasses.TripRequest;
import CustomClasses.TripRequests;
import Manager.TransPoolManager;
import SystemEngine.*;
import UI.StartScreen.MyController;
import com.fxgraph.graph.Graph;
import com.fxgraph.graph.PannableCanvas;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXSlider;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import LiveMap.Graph.Logic.MapLogic;
import CustomClasses.Time;

import java.util.ArrayList;
import java.util.List;


public class MainController {

    @FXML
    private Tab MapTab;

    @FXML
    private Tab RidesTab;

    @FXML
    private Tab OffersTab;

    @FXML
    private HBox HboxItem;

    @FXML
    private VBox VboxList;

    @FXML
    private TableColumn<TripRequest, Integer> OrderNumber;

    @FXML
    private TableColumn<TripRequest, String> FirstName;

    @FXML
    private TableColumn<TripRequest, String> LastName;

    @FXML
    private TableColumn<TripRequest, String> CurrentStop;

    @FXML
    private TableColumn<TripRequest, Button> MatchCol;

    @FXML
    private TableColumn<TripRequest, String> DestinitionStop;

    @FXML
    private TableColumn<TripRequest, Integer> HourTableCol;

    @FXML
    private TableColumn<TripRequest, Integer> MinutesTableCol;

    @FXML
    private TableColumn<TripRequest, Integer> DayTableCol;

    @FXML
    private TableView<TransPoolTrip> OfferTable;

    @FXML
    private TableColumn<TransPoolTrip, Integer> OrderNumber1;

    @FXML
    private TableColumn<TransPoolTrip, Button> RatingCol;


    @FXML
    private TableColumn<TransPoolTrip, String> owner;

    @FXML
    private TableColumn<TransPoolTrip, Integer> capacity;

    @FXML
    private TableColumn<TransPoolTrip, Integer> ppk;

    @FXML
    private TableColumn<TransPoolTrip, Route> route;

    @FXML
    private TableColumn<TransPoolTrip, String> SchedulingTimeCol;

    @FXML
    private TableColumn<TransPoolTrip, Integer> dayStart;

    @FXML
    private TableColumn<TransPoolTrip, Integer> hourStart;

    @FXML
    private TableColumn<TransPoolTrip, Integer> minuteStart;

    @FXML
    private TableColumn<TransPoolTrip, Button> InformationCol;

    @FXML
    private Button EmphasizedButton;

    @FXML
    private TableView<TripRequest> Table;

    @FXML
    private ScrollPane MapScrollPane;

    @FXML
    private JFXComboBox<String> SpinnerTimeCombobox;

    @FXML
    private JFXSlider MapSlider;

    @FXML
    private Button NextButton;

    @FXML
    private Button PrevButton;

    @FXML
    private Label DayLabel;

    @FXML
    private Label HourLabel;

    @FXML
    private Label MinutesLabel;

    @FXML
    private JFXListView<TransPoolTrip> TravelListView;


    private Integer StartDay = 1;
    private Integer StartHour = 0;
    private Integer StartMinutes = 0;




    private MapLogic MapManager= new MapLogic();
    private TransPoolManager manager = MyController.getTransPoolManager();
    private ObservableList<TransPoolTrip> list = FXCollections.observableArrayList();
    private ObservableList<TripRequest> Ridelist = FXCollections.observableArrayList();
    private Time time = manager.getModelLogic().getTime();

    @FXML
    private void initialize() {
        time.setDay(1);
        time.setHour(0);
        time.setMinutes(0);
        SpinnerTimeCombobox.getItems().addAll("5 Minutes","30 Minutes", "1 Hour", "2 Hours", "1 Day");
        DayLabel.setText("1     :");
        HourLabel.setText("0   :");
        MinutesLabel.setText(" 0   ");
        SpinnerTimeCombobox.getSelectionModel().selectedItemProperty()
                .addListener(new ChangeListener<String>() {
                    public void changed(ObservableValue<? extends String> observable,
                                        String oldValue, String newValue) {
                        ChangeSlider();
                    }
                });



        MapSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                List<TransPoolTrip> list = new ArrayList<TransPoolTrip>();

                time.setDay(StartDay);
                time.setHour(StartHour);
                time.setMinutes(StartMinutes);
                ChangeTime(MapSlider.getValue());
                DayLabel.setText(time.getDay().toString() +"     :");
                HourLabel.setText(time.getHour().toString()+"   :");
                if(time.getMinutes() ==0) {
                    MinutesLabel.setText(time.getMinutes().toString());
                }
                else
                {
                    MinutesLabel.setText(time.getMinutes().toString());
                }

                list = manager.getModelLogic().CheckIfTranspoolTripHappening();
                if(list.size() == 0) {
                    TravelListView.getItems().clear();
                }
                else
                {
                    TravelListView.getItems().setAll(list);
                }

                SetMap();

            }
        });


        SetOfferTable();
        SetRidesTable();
        SetTravelListView();
        SetMap();
    }


    public void ChangeTime(Double value)
    {

        if(SpinnerTimeCombobox.getSelectionModel().getSelectedItem().equals("5 Minutes"))
        {
                time.AddMinutes(value);
        }

        if(SpinnerTimeCombobox.getSelectionModel().getSelectedItem().equals("30 Minutes"))
        {
            time.AddMinutes(value * 60);
        }

        if(SpinnerTimeCombobox.getSelectionModel().getSelectedItem().equals("1 Hour") ||
                SpinnerTimeCombobox.getSelectionModel().getSelectedItem().equals("2 Hours"))
        {
            time.AddHour(value);
        }

        if(SpinnerTimeCombobox.getSelectionModel().getSelectedItem().equals("1 Day"))
        {
            time.AddDay(value);
        }
    }

    public  void ChangeSlider()
    {
        if(SpinnerTimeCombobox.getSelectionModel().getSelectedItem().equals("5 Minutes")) {
            MapSlider.setMin(0);
            MapSlider.setMax(100);
            MapSlider.setBlockIncrement(5);
            MapSlider.setValue(0);
            MapSlider.setMajorTickUnit(5);

        }

        if(SpinnerTimeCombobox.getSelectionModel().getSelectedItem().equals("30 Minutes")) {
            MapSlider.setMin(0);
            MapSlider.setMax(10);
            MapSlider.setBlockIncrement(0.5);
            MapSlider.setValue(0);
            MapSlider.setMajorTickUnit(0.5);
        }

        if(SpinnerTimeCombobox.getSelectionModel().getSelectedItem().equals("1 Hour")) {
            MapSlider.setMin(0);
            MapSlider.setMax(20);
            MapSlider.setBlockIncrement(1);
            MapSlider.setValue(0);
            MapSlider.setMajorTickUnit(1);
        }

        if(SpinnerTimeCombobox.getSelectionModel().getSelectedItem().equals("2 Hours")) {
            MapSlider.setMin(0);
            MapSlider.setMax(20);
            MapSlider.setBlockIncrement(2);
            MapSlider.setValue(0);
            MapSlider.setMajorTickUnit(2);
        }

        if(SpinnerTimeCombobox.getSelectionModel().getSelectedItem().equals("2 Hours")) {
            MapSlider.setMin(0);
            MapSlider.setMax(20);
            MapSlider.setBlockIncrement(2);
            MapSlider.setValue(0);
            MapSlider.setMajorTickUnit(2);
        }

        if(SpinnerTimeCombobox.getSelectionModel().getSelectedItem().equals("1 Day")) {
            MapSlider.setMin(0);
            MapSlider.setMax(10);
            MapSlider.setBlockIncrement(1);
            MapSlider.setValue(0);
            MapSlider.setMajorTickUnit(1);
        }

    }

    @FXML
    void OnSpinnerComboBoxClicked(MouseEvent event) {


      NextButton.setDisable(false);
      PrevButton.setDisable(false);
      StartDay = time.getDay();
      StartHour = time.getHour();
      StartMinutes = time.getMinutes();

    }

    @FXML
    void OnNextClicked(ActionEvent event) {

        MapSlider.increment();
    }

    @FXML
    void OnItemClicked(MouseEvent event) {
     TransPoolTrip SelectedTrip = TravelListView.getSelectionModel().getSelectedItem();

    }

    @FXML
    void OnPrevClicked(ActionEvent event) {
        MapSlider.decrement();
    }
    @FXML
    void OnTabClicked(MouseEvent event) {
     if(RidesTab.isSelected())
     {

     }

     if(OffersTab.isSelected()) {


     }

    }


    public void SetTravelListView()
    {
       PlannedTrips plannedTrips = manager.getModelLogic().getPlannedTrips();
       if(plannedTrips != null)
       {
           String trip = null;
           //List<TransPoolTrip> list = manager.getModelLogic().CheckIfTranspoolTripHappening();
           //if(list != null) {
           TravelListView.getItems().addAll(plannedTrips.getTransPoolTrip());
           //}
           TravelListView.setCellFactory(new TranspoolTripCellFactory());
       }

    }

    public void SetOfferTable()
    {
        PlannedTrips plannedTrips = manager.getModelLogic().getPlannedTrips();
        if(plannedTrips != null)
        {
            OrderNumber1.setCellValueFactory(new PropertyValueFactory<TransPoolTrip,Integer>("OrderNumber"));
            owner.setCellValueFactory(new PropertyValueFactory<TransPoolTrip,String>("Owner"));
            capacity.setCellValueFactory(new PropertyValueFactory<TransPoolTrip,Integer>("Capacity"));
            ppk.setCellValueFactory(new PropertyValueFactory<TransPoolTrip,Integer>("PPK"));
            route.setCellValueFactory(new PropertyValueFactory<TransPoolTrip, Route>("Route"));


            dayStart.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TransPoolTrip, Integer>, ObservableValue<Integer>>() {
                @Override
                public ObservableValue<Integer> call(TableColumn.CellDataFeatures<TransPoolTrip, Integer> p) {
                    return new ReadOnlyObjectWrapper(p.getValue().getScheduling().getDayStart());
                }

            });

            hourStart.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TransPoolTrip, Integer>, ObservableValue<Integer>>() {
                @Override
                public ObservableValue<Integer> call(TableColumn.CellDataFeatures<TransPoolTrip, Integer> p) {
                    return new ReadOnlyObjectWrapper(p.getValue().getScheduling().getHourStart());
                }

            });

            minuteStart.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TransPoolTrip, Integer>, ObservableValue<Integer>>() {
                @Override
                public ObservableValue<Integer> call(TableColumn.CellDataFeatures<TransPoolTrip, Integer> p) {
                    return new ReadOnlyObjectWrapper(p.getValue().getScheduling().getMinuteStart());
                }

            });

            SchedulingTimeCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TransPoolTrip, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<TransPoolTrip, String> p) {
                    return new ReadOnlyObjectWrapper(p.getValue().getScheduling().getRecurrences());
                }

            });

            RatingCol.setCellFactory(ActionButtonTableCell.<TransPoolTrip>forTableColumn("Show", (TransPoolTrip p) -> {
                ButtonShowRate(p);
                return p;
            }));

            InformationCol.setCellFactory(ActionButtonTableCell.<TransPoolTrip>forTableColumn("Show", (TransPoolTrip p) -> {
                ButtonShowOfferInfo(p);
                return p;
            }));
            for(TransPoolTrip SingleTrip :plannedTrips.getTransPoolTrip())
            {
                list.add(SingleTrip);
            }

            OfferTable.setItems(list);

        }

    }


    private void ButtonShowOfferInfo(TransPoolTrip trip)
    {
        String text = manager.getModelLogic().CreateInformationTripAlertMsg(trip);
        Alert InformationAlert = new Alert(Alert.AlertType.INFORMATION);
        if(text.isEmpty() == false)
        {
            InformationAlert.setTitle("Offer Information");
            InformationAlert.setHeaderText("Information:");
            InformationAlert.setContentText(text);
        }
        else
            InformationAlert.setContentText("This Trip wasn't matched yet");
        InformationAlert.showAndWait();
    }

    private void ButtonShowRate(TransPoolTrip trip)
    {
        Alert InformationAlert = new Alert(Alert.AlertType.INFORMATION);
        if(trip.getRatingDriver().getRatingNumber() != 0) {
            InformationAlert.setTitle("Rate Information");
            InformationAlert.setHeaderText("Rate Information");
            InformationAlert.setContentText(manager.getModelLogic().CreateRatingTripAlertMsg(trip));
        }
        else
        {
            InformationAlert.setContentText("This Trip wasn't rated yet");
        }
        InformationAlert.showAndWait();
    }

    private void ButtonInformationCLicked(TripRequest request)
    {
        Alert InformationAlert = new Alert(Alert.AlertType.INFORMATION);
        if(request.getMatchTrip() != null) {
            InformationAlert.setContentText(manager.getModelLogic().CreateInformationAlertMsg(request.getMatchTrip()));
            InformationAlert.setTitle("Trip Match Information");
            InformationAlert.setHeaderText("Match Information");
            InformationAlert.showAndWait();
        }
        else
        {
            InformationAlert.setContentText("The request didn't match yet");
            InformationAlert.showAndWait();
        }


    }

    @FXML
    void OnEmphsizedButtonClicked(ActionEvent event) {
    if(!TravelListView.getSelectionModel().isEmpty()) {
        TransPoolTrip trip = TravelListView.getSelectionModel().getSelectedItem();
        MapManager.EmphasizePath(trip);
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
        MatchCol.setCellFactory(ActionButtonTableCell.<TripRequest>forTableColumn("Information", (TripRequest p) -> {
            ButtonInformationCLicked(p);
            return p;
        }));

        for(TripRequest SingleRequest : tripRequests.getTripRequests())
        {
            Ridelist.add(SingleRequest);
        }

        Table.setItems(Ridelist);
    }


    public void SetMap()
    {

        Graph graphMap = new Graph();
        MapManager.createMap(graphMap);


        PannableCanvas canvas = graphMap.getCanvas();
        MapScrollPane.setContent(canvas);

        Platform.runLater(() -> {
            graphMap.getUseViewportGestures().set(false);
            graphMap.getUseNodeGestures().set(false);
        });

    }

}
