package SystemEngine;

import CustomClasses.TripRequest;

import java.util.Objects;

public class OneRating {
    TripRequest tripRequest;
    int rate;
    String comment;




    public TripRequest getTripRequest() {
        return tripRequest;
    }

    public void setTripRequest(TripRequest tripRequest) {
        this.tripRequest = tripRequest;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OneRating)) return false;
        OneRating oneRating = (OneRating) o;
        return Objects.equals(getTripRequest(), oneRating.getTripRequest());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTripRequest());
    }
}
