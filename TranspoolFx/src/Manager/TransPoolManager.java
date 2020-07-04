package Manager;

import CustomClasses.TripRequest;
import SystemEngine.TransPool;
import SystemEngine.TransPoolTrip;
import SystemLogic.ModelLogic;
import SystemLogic.ValidMatchedOffers;
import UI.StartScreen.MyController;
import javafx.beans.property.SimpleStringProperty;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.util.List;

public class TransPoolManager {

    private SimpleStringProperty fileName;
    private MyController controller;
    private ModelLogic modelLogic = new ModelLogic();
    private TripRequest tripRequest = new TripRequest();
    private TransPoolTrip tripToRate = new TransPoolTrip();
    private Integer numOffersToShow;

    public TransPoolTrip getTripToRate() {
        return tripToRate;
    }

    public void setTripToRate(TransPoolTrip tripToRate) {
        this.tripToRate = tripToRate;
    }

    public Integer getNumOffersToShow() {
        return numOffersToShow;
    }

    public void setNumOffersToShow(Integer numOffersToShow) {
        this.numOffersToShow = numOffersToShow;
    }

    public TripRequest getTripRequest() {
        return tripRequest;
    }

    public void setTripRequest(TripRequest tripRequest) {
        this.tripRequest = tripRequest;
    }

    public ModelLogic getModelLogic() {
        return modelLogic;
    }

    public void setModelLogic(ModelLogic modelLogic) {
        this.modelLogic = modelLogic;
    }

    public String getFileName() {
        return fileName.get();
    }

    public void setFileName(String fileName) {
        this.fileName.set(fileName);
    }


    public boolean AddDriversRating (Integer rate, String comment)
    {
        return  modelLogic.AddPassengerReview(tripRequest,tripToRate,rate,comment);
    }

    public TransPoolManager() {
    }

    public ValidMatchedOffers createRideOffersForMatch(Integer numberOffers, TripRequest tripRequest)
    {
        ValidMatchedOffers validMatchedOffers =  modelLogic.FindRideMatches(numberOffers,tripRequest);
        return  validMatchedOffers;
    }

    public List<TransPoolTrip> findTripsToRating()
    {
        List<TransPoolTrip> transPoolTrips = modelLogic.TripsToRating(this.getTripRequest());
        return transPoolTrips;
    }

    public void createTransPoolSystem(File file) throws Exception {
        try {
            TransPool transPool = modelLogic.fromXmlFileToObject(file);
            modelLogic.setTransPool(transPool);
        }
        catch (JAXBException jAXBException) {
            throw jAXBException;
        }
        catch (Exception exception) {
            throw exception;
        }
    }

    public SimpleStringProperty fileNameProperty() {
        return fileName;
    }

    public TransPoolManager(MyController controller) {
        this.fileName = new SimpleStringProperty();
        this.controller = controller;
    }
}