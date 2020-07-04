package CustomClasses;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class TripRequests
{
    private ArrayList<TripRequest> tripRequests = new ArrayList<TripRequest>();

    public ArrayList<TripRequest>  get(){return tripRequests;};

    public ArrayList<TripRequest> getTripRequests() { return tripRequests; }
    public void AddNewRequest(TripRequest request)
    {
        tripRequests.add(request);
    }

    public List<TripRequest>  getUnmatchedRTripRequests() {
      return this.get().stream()
                .filter(tripRequest -> tripRequest.getMatchTrip() == null).collect(Collectors.toList());
    }

}
