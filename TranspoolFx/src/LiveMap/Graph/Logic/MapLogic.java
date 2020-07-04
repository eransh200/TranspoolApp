package LiveMap.Graph.Logic;

import CustomClasses.Time;
import LiveMap.Graph.component.details.StationDetailsDTO;
import LiveMap.Graph.component.road.ArrowedEdge;
import Manager.TransPoolManager;
import SystemEngine.*;
import UI.StartScreen.MyController;
import com.fxgraph.graph.Graph;
import com.fxgraph.graph.IEdge;
import com.fxgraph.graph.Model;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import LiveMap.Graph.component.coordinate.CoordinateNode;
import LiveMap.Graph.component.coordinate.CoordinatesManager;
import LiveMap.Graph.component.station.StationManager;
import LiveMap.Graph.component.station.StationNode;
import LiveMap.Graph.layout.MapGridLayout;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MapLogic {

    private TransPoolManager manager = MyController.getTransPoolManager();
    private List<ArrowedEdge> ArrowedEdgeList = new ArrayList<ArrowedEdge>();

    public void createMap(Graph graph) {

        final Model model = graph.getModel();
        graph.beginUpdate();

        StationManager sm = createStations(model);
        CoordinatesManager cm = createCoordinates(model);
        createEdges(model, cm);

        graph.endUpdate();

        graph.layout(new MapGridLayout(cm, sm));
    }

    private StationManager createStations(Model model) {
        StationManager sm = new StationManager(StationNode::new);
        List<Stop> stops = manager.getModelLogic().getStops();


        for (Stop SingleStop : stops) {

            StationNode station = sm.getOrCreate(SingleStop.getX(), SingleStop.getY());
            station.setName(SingleStop.getName());
            station.setDetailsSupplier(() -> new StationDetailsDTO(getStationDriversName(SingleStop.getName())));
            model.addCell(station);

        }


        return sm;
    }

    private List<String> getStationDriversName(String StopName) {
        List<String> trips = new ArrayList<>();
        Time time = manager.getModelLogic().getTime();
        for (TransPoolTrip SingleTrip : manager.getModelLogic().getPlannedTrips().getTransPoolTrip()) {

            Optional<CapacityPerTrip> capacityPerTrip = SingleTrip.getCapacityPerTripList().stream().filter(capacityPerTrip1 -> capacityPerTrip1.getDayNumber().equals(time.getDay())).findFirst();

            if (capacityPerTrip.isPresent()) {
                List<OneCapacity> CapacityList = capacityPerTrip.get().getCapacityList();
                for (int i = 0; i < CapacityList.size() - 1; i++) {
                    LocalTime FisrstStopTime = CapacityList.get(i).getLocalTime();
                    LocalTime SecondStoptStopTime = CapacityList.get(i + 1).getLocalTime();
                    LocalTime timeToCheck = LocalTime.of(time.getHour(), time.getMinutes());
                    if ((timeToCheck.equals(FisrstStopTime) ||
                            (timeToCheck.isAfter(FisrstStopTime) && timeToCheck.isBefore(SecondStoptStopTime)))
                    && StopName.equals(CapacityList.get(i).getStationName()) )
                    {
                        trips.add(SingleTrip.getOwner());
                    }

                    if(i + 1 == CapacityList.size() - 1)
                    {
                        if (timeToCheck.equals(SecondStoptStopTime) && StopName.equals(CapacityList.get(i + 1).getStationName()))
                        {
                            trips.add(SingleTrip.getOwner());
                        }
                    }
                }

            }

        }
        return  trips;
    }


    private CoordinatesManager createCoordinates(Model model) {

        Integer Width = manager.getModelLogic().getTransPool().getMapDescriptor().getMapBoundries().getWidth();
        Integer Height = manager.getModelLogic().getTransPool().getMapDescriptor().getMapBoundries().getLength();
        CoordinatesManager cm = new CoordinatesManager(CoordinateNode::new);

        for (int i=0; i<Width; i++) {
            for (int j = 0; j < Height; j++) {
                model.addCell(cm.getOrCreate(i+1, j+1));
            }
        }

        return cm;
    }

    private void createEdges(Model model, CoordinatesManager cm) {
        PlannedTrips plannedTrips = manager.getModelLogic().getPlannedTrips();
        Paths paths = manager.getModelLogic().getTransPool().getMapDescriptor().getPaths();

        for(Path SinglePath :paths.getPath()) {

            Stop Fromstop = manager.getModelLogic().GetStopFromString(SinglePath.getFrom());
            Stop ToStop = manager.getModelLogic().GetStopFromString(SinglePath.getTo());

            ArrowedEdge e13 = new ArrowedEdge(cm.getOrCreate(Fromstop.getX(), Fromstop.getY()),
                    cm.getOrCreate(ToStop.getX(), ToStop.getY()));
            e13.textProperty().set("L: " + SinglePath.getLength() + " FC: "+ SinglePath.getFuelConsumption());
            model.addEdge(e13); // 1-3
            ArrowedEdgeList.add(e13);
        }

        Platform.runLater(() -> {




            //moveAllEdgesToTheFront(graph);
        });

    }

    public void EmphasizePath(TransPoolTrip trip)
    {
        String path = trip.getRoute().getPath();
        List<String> Stoplist = manager.getModelLogic().createRouteStopsStringsList(trip.getRoute().getPath());

        for(int i=0; i < Stoplist.size() -1;i++)
        {
            Stop s1 = manager.getModelLogic().FindStopFromString(Stoplist.get(i));
            Stop s2 = manager.getModelLogic().FindStopFromString(Stoplist.get(i + 1));
            FindArrowageAndEmphasize(s1,s2);
        }
    }

    private void FindArrowageAndEmphasize(Stop From, Stop To)
    {
        List<ArrowedEdge> EmphasizedEdgesList = new ArrayList<ArrowedEdge>();
        for(ArrowedEdge edge : ArrowedEdgeList)
        {
            CoordinateNode nodeSource = (CoordinateNode) edge.getSource();
            CoordinateNode nodeTarget =(CoordinateNode) edge.getTarget();
            if(nodeSource.getX() == From.getX() && nodeSource.getY() == From.getY()
                    && nodeTarget.getX() == To.getX() && nodeTarget.getY() == To.getY())
            {
                EmphasizedEdgesList.add(edge);
            }
        }

        Platform.runLater(() -> {

            for(ArrowedEdge edge : EmphasizedEdgesList)
            {
                edge.getLine().getStyleClass().add("line1");
                edge.getText().getStyleClass().add("edge-text");
            }

        });

    }

    private void moveAllEdgesToTheFront(Graph graph) {

        List<Node> onlyEdges = new ArrayList<>();

        // finds all edge nodes and remove them from the beginning of list
        ObservableList<Node> nodes = graph.getCanvas().getChildren();
        while (nodes.get(0).getClass().getSimpleName().equals("EdgeGraphic")) {
            onlyEdges.add(nodes.remove(0));
        }

        // adds them as last ones
        nodes.addAll(onlyEdges);
    }
}
