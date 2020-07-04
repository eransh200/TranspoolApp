package UI.MainScreen;

import Manager.TransPoolManager;
import SystemEngine.TransPoolTrip;
import UI.StartScreen.MyController;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

import javax.swing.text.html.ListView;
import java.util.function.Function;

public class TranspoolTripCell extends ListCell<TransPoolTrip> {

    private TransPoolManager manager = MyController.getTransPoolManager();

        @Override
    public void updateItem(TransPoolTrip item, boolean empty) {
        super.updateItem(item, empty);

        int index = this.getIndex();
        String name = null;

        // Format name
        if (item == null || empty) {
        } else {
            if(manager.getModelLogic().CheckIfSingleTranspoolTripHappening(item,manager.getModelLogic().getTime())) {
                name = item.getOwner() + " travels from " + item.getDepartureStop() + " to " + item.getDestinationStop() + " at "
                + item.getScheduling().getHourStart() + " : " + item.getScheduling().getMinuteStart() ;
            }
        }

        this.setText(name);
        setGraphic(null);
    }
}


