package SystemLogic;

import java.time.LocalTime;
import java.util.LinkedList;

public class SingleValidMatchedOffers {
    private LinkedList<RideSingleStation> Information = new LinkedList<RideSingleStation>();
    private Integer offerCost;
    private LocalTime estimatedTime;
    private String estimatedTimeString;
    private Float passengerAvgFuelUtilization;
    private String offerType;
    private Integer OfferNumber;

    public LinkedList<RideSingleStation> getRideSingleStationLinkedList() {
        return Information;
    }

    public void setRideSingleStationLinkedList(LinkedList<RideSingleStation> rideSingleStationLinkedList) {
        this.Information = rideSingleStationLinkedList;
    }

    public Integer getOfferCost() {
        return offerCost;
    }

    public void setOfferCost(Integer offerCost) {
        this.offerCost = offerCost;
    }

    public LocalTime getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(LocalTime estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public String getEstimatedTimeString() {
        return estimatedTimeString;
    }

    public void setEstimatedTimeString(String estimatedTimeString) {
        this.estimatedTimeString = estimatedTimeString;
    }

    public Float getPassengerAvgFuelUtilization() {
        return passengerAvgFuelUtilization;
    }

    public void setPassengerAvgFuelUtilization(Float passengerAvgFuelUtilization) {
        this.passengerAvgFuelUtilization = passengerAvgFuelUtilization;
    }

    public String getOfferType() {
        return offerType;
    }

    public void setOfferType(boolean b) {

        if (b == false)
            offerType = "Connection Ride";
        else
            offerType = "Direct Ride";
    }

    public Integer getOfferNumber() {
        return OfferNumber;
    }

    public void setOfferNumber(Integer offerNumber) {
        OfferNumber = offerNumber;
    }
}
