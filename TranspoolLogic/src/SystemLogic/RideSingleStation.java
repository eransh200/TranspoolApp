package SystemLogic;

import SystemEngine.TransPoolTrip;

import java.time.LocalDateTime;
import java.util.Objects;

public class RideSingleStation {
    String name;
    int TripOfferOrderNum;
    LocalDateTime localDateTime;
    String recurrences;
    boolean capacity;
    int index;
    int pkk;
    String owner;
    TransPoolTrip transPoolTrip;

    public TransPoolTrip getTransPoolTrip() {
        return transPoolTrip;
    }

    public void setTransPoolTrip(TransPoolTrip transPoolTrip) {
        this.transPoolTrip = transPoolTrip;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getPkk() {
        return pkk;
    }

    public void setPkk(int pkk) {
        this.pkk = pkk;
    }

    public RideSingleStation(String name, int tripOfferOrderNum, LocalDateTime localDateTime, String recurrences) {
        this.name = name;
        TripOfferOrderNum = tripOfferOrderNum;
        this.localDateTime = localDateTime;
        this.recurrences = recurrences;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isCapacity() {
        return capacity;
    }

    public void setCapacity(boolean capacity) {
        this.capacity = capacity;
    }

    public  RideSingleStation(RideSingleStation someObject) {
        setName(someObject.getName());
        setLocalDateTime(someObject.getLocalDateTime());
        setTripOfferOrderNum(someObject.getTripOfferOrderNum());
        setRecurrences(someObject.getRecurrences());
        setIndex(someObject.getIndex());
        setCapacity(someObject.isCapacity());
        setPkk(someObject.getPkk());
        setOwner(someObject.getOwner());
        setTransPoolTrip(someObject.getTransPoolTrip());

    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTripOfferOrderNum() {
        return TripOfferOrderNum;
    }

    public void setTripOfferOrderNum(int tripOfferOrderNum) {
        TripOfferOrderNum = tripOfferOrderNum;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public String getRecurrences() {
        return recurrences;
    }

    public void setRecurrences(String recurrences) {
        this.recurrences = recurrences;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RideSingleStation)) return false;
        RideSingleStation that = (RideSingleStation) o;
        return TripOfferOrderNum == that.TripOfferOrderNum &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

}
