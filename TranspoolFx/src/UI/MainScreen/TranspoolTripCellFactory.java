package UI.MainScreen;

import SystemEngine.TransPoolTrip;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class TranspoolTripCellFactory implements Callback<ListView<TransPoolTrip>, ListCell<TransPoolTrip>> {

        @Override
        public ListCell<TransPoolTrip> call(ListView<TransPoolTrip> listview)
        {
            return new TranspoolTripCell();
        }
    }

