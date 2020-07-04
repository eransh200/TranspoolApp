package SystemEngine;

import CustomClasses.TripRequest;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OneCapacity {
    String StationName;
    int capacityNum;
    int OriginalCapacity;
    List<TripRequest> passengerGettingUp =new ArrayList<TripRequest>();
    List<TripRequest> passengerGettingDown =new ArrayList<TripRequest>();

    LocalTime localTime;



    public LocalTime getLocalTime() {
        return localTime;
    }

    public void setLocalTime(LocalTime localTime) {
        this.localTime = localTime;
    }
    public int getOriginalCapacity() {
        return OriginalCapacity;
    }

    public void setOriginalCapacity(int originalCapacity) {
        OriginalCapacity = originalCapacity;
    }

    public void AddPassengerGettingUp(TripRequest tripRequest) {passengerGettingUp.add(tripRequest);}

    public void AddPassengerGettingDown(TripRequest tripRequest) {passengerGettingDown.add(tripRequest);}
    public String getStationName() {
        return StationName;
    }
    public OneCapacity() { }
    public OneCapacity(OneCapacity oneCapacity)
    {
        setStationName(oneCapacity.getStationName());
        setOriginalCapacity(oneCapacity.getOriginalCapacity());
        setCapacityNum(oneCapacity.getOriginalCapacity());
        setLocalTime(oneCapacity.getLocalTime());
        passengerGettingUp = new ArrayList<TripRequest>();
        passengerGettingDown = new ArrayList<TripRequest>();
    }

    public void setStationName(String stationName) {
        StationName = stationName;
    }

    public int getCapacityNum() {
        return capacityNum;
    }

    public void setCapacityNum(int capacityNum) {
        this.capacityNum = capacityNum;
    }

    public List<TripRequest> getPassengerGettingUp() {
        return passengerGettingUp;
    }

    public void setPassengerGettingUp(List<TripRequest> passengerGettingUp) {
        this.passengerGettingUp = passengerGettingUp;
    }

    public List<TripRequest> getPassengerGettingDown() {
        return passengerGettingDown;
    }

    public void setPassengerGettingDown(List<TripRequest> passengerGettingDown) {
        this.passengerGettingDown = passengerGettingDown;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OneCapacity)) return false;
        OneCapacity that = (OneCapacity) o;
        return Objects.equals(getStationName(), that.getStationName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStationName());
    }
}
