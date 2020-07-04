package SystemLogic;

import CustomClasses.*;
import SystemEngine.*;
import com.sun.javafx.collections.MappingChange;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.awt.*;
import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class ModelLogic {

    static final int MINUTES_PER_HOUR = 60;
    static final int SECONDS_PER_MINUTE = 60;
    static final int SECONDS_PER_HOUR = SECONDS_PER_MINUTE * MINUTES_PER_HOUR;
    public static final int BOUNDARIES_MAXIMUM_SIZE = 100;
    public static final int BOUNDARIES_MINIMUM_SIZE = 6;
    public static final int MINIMUM_COORDINATE_VALUE = 1;
    public static final int MINIMUM_START_DAY = 1;
    private final String EXCEPTION_MESSAGE_3_2 = "Exception 3.2: The given %s of value %d isn't in the range of 6 to 100";
    private final String EXCEPTION_MESSAGE_3_3 = "Exception 3.3: The name '%s' is given to more than one Stop";
    private final String EXCEPTION_MESSAGE_3_4 = "Exception 3.4: The point (%s,%s) isn't in the map boundaries of (%s,%s) range";
    private final String EXCEPTION_MESSAGE_3_5 = "Exception 3.5: The point (%d,%d)  appears more than one time in the Stops ";
    private final String EXCEPTION_MESSAGE_3_6 = "Exception 3.6: The path  %s -> %s %s";
    private final String EXCEPTION_MESSAGE_3_7 = "Exception 3.7: The '%s' in the path '%s' %s";
    private final String EXCEPTION_MESSAGE_4_1 = "Exception 4.1: The trip offer by %s doesn't contain day-start field";
    private final String EXCEPTION_MESSAGE_4_2 = "Exception 4.2: The day-start value of %d is smaller than the minimum of 1 ";

    public ArrayList<String> errorList;
    public TransPool transPool;
    private ObjectFactory factory = new ObjectFactory();
    private TripRequests tripRequests = factory.createTripRequests();
    private Time time = new Time(1,0,0);


    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public TransPool getTransPool() {
        return transPool;
    }


    public PlannedTrips getPlannedTrips()
    {
        return  transPool.getPlannedTrips();
    }

    public TripRequests getTripRequests() {
        return tripRequests;
    }
    public void setTripRequests(TripRequests value) {
        tripRequests = value;
    }

    public List<Stop> getStops()
    {
        return transPool.getMapDescriptor().getStops().getStop();
    }

    public Stop  GetStopFromString(String stop)
    {
        Stops stops = transPool.getMapDescriptor().getStops();

        for(Stop SingleStop : stops.getStop())
        {
            if(SingleStop.getName().equals(stop))
            {
                return SingleStop;
            }
        }

        return null;
    }

    public  String NewRideInputCheck(String passengerFirstName, String passengerLastName, String CurrentLocation,
                                     String Destination, Integer Hour, Integer minutes
                                    , Integer Day, Boolean IsFutureTime) {

        String Msg;
        Msg = IOCheck.CheckName(passengerFirstName, " First Name") +
                IOCheck.CheckName(passengerLastName, " Last Name") +
                IOCheck.StringNullCheck(CurrentLocation,"Current Location") +
                IOCheck.StringNullCheck(Destination,"Destination") +
                IOCheck.IntegerNullCheck(Day,"Day") +
                IOCheck.IntegerNullCheck(Hour, "Hour") +
                IOCheck.IntegerNullCheck(minutes, "Minutes") ;




        return Msg;
    }

    public  String NewOfferInputCheck(String FirstName ,String LastName ,String Route,Integer DayStart,
                                      Integer Hour, Integer minutes,  Integer ppk , Integer capacity) {

        String Msg;
        Msg = IOCheck.CheckName(FirstName, " First Name") +
                IOCheck.CheckName(LastName, " Last Name") +
                IOCheck.StringNullCheck(Route,"Route") +
                IOCheck.IntegerNullCheck(DayStart,"Day") +
                IOCheck.IntegerNullCheck(Hour, "Hour") +
                IOCheck.IntegerNullCheck(minutes, "Minutes") +
                IOCheck.IntegerNullCheck(ppk,"Price per kilometer") +
                IOCheck.IntegerNullCheck(capacity,"Capacity") +
                 checkRoute(Route) +
                NewOfferRouteCheck(Route);


        return Msg;
    }

    public String checkRoute(String Route)
    {
        List<String> Stops = createRouteStopsStringsList(Route);
        if(Stops.size() <= 1)
        {
            return "You Have Enter only one Stop, pleas pick up Several stations";
        }
        else
        {

            return "";
        }



    }


    public String CreateOfferInformationAlertMsg(SingleValidMatchedOffers singleValidMatchedOffers)
    {
        String text = "";
        if(singleValidMatchedOffers.getOfferType().equals("Direct Ride"))
            text = MakeSingleRideInfo(singleValidMatchedOffers);
        else
            text = MakeConnectionRideInfo(singleValidMatchedOffers);

        return text;
    }

    public void FindRelevantTripsByTime()
    {
       for(TransPoolTrip trip : transPool.getPlannedTrips().getTransPoolTrip())
       {


       }

    }


     public List<TransPoolTrip> CheckIfTranspoolTripHappening()
     {
         List<TransPoolTrip> list = new ArrayList<TransPoolTrip>();
         for(TransPoolTrip SingleTrip : transPool.getPlannedTrips().getTransPoolTrip())
         {
             if(CheckIfSingleTranspoolTripHappening(SingleTrip,time)) {
                 list.add(SingleTrip);
             }
         }
         return list;
     }

    public boolean CheckIfSingleTranspoolTripHappening(TransPoolTrip Singletrip, Time time)
    {
        Optional<CapacityPerTrip> capacityPerTrip = Singletrip.getCapacityPerTripList().stream().filter(capacityPerTrip1 -> capacityPerTrip1.getDayNumber().equals(time.getDay())).findFirst();

        if(capacityPerTrip.isPresent())
        {
            List<OneCapacity> CapacityList = capacityPerTrip.get().getCapacityList();
            LocalTime startTripTime = CapacityList.get(0).getLocalTime();
            LocalTime endTripTime = CapacityList.get(CapacityList.size() -1).getLocalTime();
            LocalTime timeToCheck = LocalTime.of(time.getHour(),time.getMinutes());
            if(timeToCheck.equals(startTripTime) || timeToCheck.equals(endTripTime))
                return true;
            else if ( timeToCheck.isAfter(startTripTime) &&  timeToCheck.isBefore(endTripTime))
                return true;
        }
        return false;

    }
    public String getJaxbErrorsInString() {
        StringBuilder xmlErrors = new StringBuilder();
        for (String error: this.errorList)
        {
            xmlErrors.append(error + "\n");
        }
        return xmlErrors.toString();
    }
    public ModelLogic() {
    }



    private void checkTransPoolAfterCreation(TransPool transPool) {

        checkMapBoundries(transPool.getMapDescriptor().getMapBoundries().getLength(), transPool.getMapDescriptor().getMapBoundries().getWidth());
        checkStops(transPool.getMapDescriptor());
        checkPaths(transPool.getMapDescriptor().getStops().getStop(), transPool.getMapDescriptor().getPaths().getPath());
        if(transPool.getPlannedTrips() != null)
        {
            checkTransPoolTrips(transPool.getPlannedTrips().getTransPoolTrip(),transPool.getMapDescriptor().getStops().getStop(),transPool.getMapDescriptor().getPaths().getPath());
        }
    }

    public void  checkTransPoolTrips(List<TransPoolTrip> transPoolTrip, List<Stop> stop ,List<Path> path) {
        /**check trips routes*/
        checkRoutes(transPoolTrip, stop, path);
        /**check day-start'*/
        checkDayStart(transPoolTrip);


    }

    public void checkDayStart(List<TransPoolTrip> transPoolTrip)
    {
        for(TransPoolTrip singleTransPoolTrip: transPoolTrip) {
            if(singleTransPoolTrip.getScheduling().getDayStart() == null)
                errorList.add(String.format(EXCEPTION_MESSAGE_4_1, singleTransPoolTrip.getOwner()));
            else
            {
                if(singleTransPoolTrip.getScheduling().getDayStart() < MINIMUM_START_DAY)
                    errorList.add(String.format(EXCEPTION_MESSAGE_4_2,singleTransPoolTrip.getScheduling().getDayStart()));
            }
        }
    }

    public void  checkRoutes(List<TransPoolTrip> transPoolTrip, List<Stop> stop, List<Path> path){
        for(TransPoolTrip singleTransPoolTrip: transPoolTrip) {
            String pathString = singleTransPoolTrip.getRoute().getPath();
            List<String> route = Arrays.asList(pathString.trim()
                    .split(",")).stream().map(w-> w.trim()).collect(Collectors.toList());
            checkPathStation(route, pathString, stop);
            checkPathExistence(route,pathString, path);
            duplicationStationInPath(route,pathString);
        }
    }

    public String NewOfferRouteCheck(String Route)
    {
        String Msg = "";
        List<String> routeStringList = Arrays.asList(Route.trim()
                .split(",")).stream().map(w-> w.trim()).collect(Collectors.toList());

        List<Path> pathList = transPool.getMapDescriptor().getPaths().getPath();

        /**check Path Existence*/
        for(int i = 0; i < routeStringList.size()-1; i++) {
            if(FindPath(routeStringList.get(i),routeStringList.get(i+1), pathList) == null)
                Msg = Msg + String.format(EXCEPTION_MESSAGE_3_7, routeStringList.get(i), routeStringList.get(i+1),"doesn't exist in the system \n");
        }
        /**check duplication Station In Path*/
        Optional<String> duplicatedFoundedStation = routeStringList.stream()
                .filter(s -> Collections.frequency(routeStringList ,s) >1).findFirst();
        if(duplicatedFoundedStation.isPresent())
            Msg = Msg + String.format(EXCEPTION_MESSAGE_3_7, duplicatedFoundedStation.get(),Route, "appears more than ones");
        return Msg;

    }


    private void  checkPathStation (List<String> paths, String pathString, List<Stop> stop)  {
        Optional<String> unFoundedStation = paths.stream()
                .filter(station -> findStopByName (station, stop) == false).findFirst();
        if((unFoundedStation.isPresent()))
            errorList.add(String.format(EXCEPTION_MESSAGE_3_7, unFoundedStation.get(),pathString, "doesn't exists in the system"));
    }

    private void duplicationStationInPath (List<String> paths, String pathString) {
        Optional<String> duplicatedFoundedStation = paths.stream()
                .filter(s -> Collections.frequency(paths,s) >1).findFirst();
        if(duplicatedFoundedStation.isPresent())
            errorList.add(String.format(EXCEPTION_MESSAGE_3_7, duplicatedFoundedStation.get(),pathString, "appears more than ones"));
    }
    private void checkPathExistence (List<String> paths, String pathString, List<Path> path){
        for(int i = 0; i < paths.size()-1; i++) {
            if(FindPath(paths.get(i),paths.get(i+1), path) == null)
                errorList.add(String.format(EXCEPTION_MESSAGE_3_7, paths.get(i),paths.get(i+1),"doesn't exist in the system"));

        }
    }

    public Path FindPath(String fromStation, String toStation,List<Path> path) {
        Optional<Path> FoundPath = path.stream()
                .filter(singlePath -> (singlePath.getFrom().equals(fromStation) && singlePath.getTo().equals(toStation))
                        || (singlePath.getFrom().equals(toStation) && singlePath.getTo().equals(fromStation) && singlePath.isOneWay() == false) )
                .findFirst();
        if(FoundPath.isPresent())
            return FoundPath.get();
        return null;
    }

    public void checkMapBoundries(int length, int width) {
        if (length < BOUNDARIES_MINIMUM_SIZE || length > BOUNDARIES_MAXIMUM_SIZE)
            errorList.add(String.format(EXCEPTION_MESSAGE_3_2, "length" ,length));
        if (width < BOUNDARIES_MINIMUM_SIZE || width > BOUNDARIES_MAXIMUM_SIZE)
            errorList.add(String.format(EXCEPTION_MESSAGE_3_2, "width", width));

    }

    public void checkPaths(List<Stop> stop, List<Path> path) {
        for(Path singlePath : path) {
            if(findStopByName(singlePath.getFrom(), stop) == false || findStopByName(singlePath.getTo(),stop) == false)
                errorList.add(String.format(EXCEPTION_MESSAGE_3_6, singlePath.getFrom(),singlePath.getTo(),"doesn't exist in the System"));
            if (singlePath.getFrom().equals(singlePath.getTo()))
                errorList.add(String.format(EXCEPTION_MESSAGE_3_6, singlePath.getFrom(),singlePath.getTo(), "has the same station names"));
            if(Collections.frequency(path,singlePath) > 1 )
                errorList.add(String.format(EXCEPTION_MESSAGE_3_6, singlePath.getFrom(),singlePath.getTo(), "appears more than one time in the Paths"));
        }

    }

    public void checkStops (MapDescriptor mapDescriptor) {
        ArrayList<Point>  points = createPointsListFromStops(mapDescriptor.getStops());
        nameDuplicatesCheck(mapDescriptor.getStops().getStop());
        outOfMapBoundriesStopPointCheck(mapDescriptor.getStops().getStop(),mapDescriptor.getMapBoundries().getWidth(),mapDescriptor.getMapBoundries().getLength()  );
        pointDuplicateCheck(mapDescriptor.getStops().getStop());
    }


    private void nameDuplicatesCheck(List<Stop> stop) {
        Optional<Stop> duplicatedStopByName = stop.stream()
                .filter(i -> Collections.frequency(stop, i) > 1).findFirst();
        if(duplicatedStopByName.isPresent())
            errorList.add(String.format(EXCEPTION_MESSAGE_3_3,duplicatedStopByName.get().getName()));
    }


    private void outOfMapBoundriesStopPointCheck(List<Stop> stop, int width, int length){
        Optional<Stop> outOfBoundriesStop = stop.stream().filter(s-> checkCordinates(s.getX(),s.getY(),width,length)).findFirst();
        if(outOfBoundriesStop.isPresent())
            errorList.add(String.format(EXCEPTION_MESSAGE_3_4, outOfBoundriesStop.get().getX(),outOfBoundriesStop.get().getY(), width, length));
    }

    private void pointDuplicateCheck(List<Stop> stop) {
        List<Point> pointList = stop.stream().map(s-> new Point(s.getX(),s.getY())).collect(Collectors.toList());
        Optional<Point> duplicatedPoint = pointList.stream().filter(p -> Collections.frequency(pointList,p) > 1).findFirst();
        if(duplicatedPoint.isPresent())
            errorList.add(String.format(EXCEPTION_MESSAGE_3_5, duplicatedPoint.get().x,duplicatedPoint.get().y));
    }

    public boolean checkCordinates(int x, int y ,int width, int length) {
        if(x > width || x < MINIMUM_COORDINATE_VALUE || y > length || y < MINIMUM_COORDINATE_VALUE)
            return true;
        return false;
    }

    public ArrayList<Point> createPointsListFromStops(Stops stops) {
        ArrayList<Point> points = new ArrayList<Point>();
        for(Stop singleStop : stops.getStop()) {
            points.add(new Point(singleStop.getX(), singleStop.getY()));
        }
        return points;
    }


    public boolean findStopByName (String nameToFind, List<Stop> stop) {
        for (Stop singleStop : stop) {
            if(singleStop.getName().equals(nameToFind))
                return true;
        }
        return false;
    }





    public void setTransPool(TransPool transPool) {
        this.transPool = transPool;
    }

    public void CreateNewRequest(String passengerFirstName,String passengerLastName,
                                 String CurrentLocation, String Destination,
                                 Integer Hour , Integer Minutes , Integer Day, Boolean IsFutureTime) {

        TripRequest NewTrip = factory.createTripRequest();
        NewTrip.setOrderNumber(tripRequests.get().size());
        NewTrip.setFirstName(passengerFirstName);
        NewTrip.setLastName(passengerLastName);
        NewTrip.setCurrentLocation(CurrentLocation);
        NewTrip.setDestination(Destination);
        NewTrip.setIsFutureTime(IsFutureTime);
        NewTrip.setHour(Hour);
        NewTrip.setMinutes(Minutes);
        NewTrip.setDay(Day);
        tripRequests.AddNewRequest(NewTrip);
    }

    public ValidMatchedOffers FindRideMatches(Integer numberOffers, TripRequest tripRequest)
    {
        LinkedList<SingleValidMatchedOffers> singleValidMatchedOffersLinkedList = new LinkedList<SingleValidMatchedOffers>();
        ValidMatchedOffers validMatchedOffers = new ValidMatchedOffers();
        /**Create offer according to departure time */
        if(tripRequest.getIsFutureTime() == false)
            FindMatchesOffersFutureDeparture(tripRequest,singleValidMatchedOffersLinkedList, numberOffers);
        else    /**Create offer according to arrival time */
            FindMatchesOffersFutureArrival(tripRequest,singleValidMatchedOffersLinkedList, numberOffers);
        validMatchedOffers.setValidOffers(singleValidMatchedOffersLinkedList);
        return  validMatchedOffers;
    }



    public boolean isLocalTimeMatch( LocalDateTime requestDateTime,RideSingleStation rideSingleStation)
    {
        LocalDateTime localDateTimePlus5 = rideSingleStation.getLocalDateTime().plusMinutes(5);
        LocalDateTime localDateTimeMinus5 = rideSingleStation.getLocalDateTime().minusMinutes(5);
        if(requestDateTime.isEqual(rideSingleStation.getLocalDateTime()) || requestDateTime.isEqual(localDateTimeMinus5) || requestDateTime.isEqual(localDateTimePlus5))
            return true;
        else if(requestDateTime.toLocalTime().equals(rideSingleStation.getLocalDateTime().toLocalTime()) || requestDateTime.toLocalTime().equals(localDateTimeMinus5.toLocalTime()) || requestDateTime.toLocalTime().equals(localDateTimePlus5.toLocalTime()))
        {
            if(rideSingleStation.getLocalDateTime().isBefore(requestDateTime) && !rideSingleStation.getRecurrences().equals("OneTime"))
            {
                int valueToAdd = findAddValue(rideSingleStation.getRecurrences());
                LocalDateTime localDateTime = rideSingleStation.getLocalDateTime();
                while (localDateTime.isBefore(requestDateTime))
                {
                    localDateTime = localDateTime.plusDays(valueToAdd);
                }
                if(localDateTime.isEqual(requestDateTime))
                {
                    rideSingleStation.setLocalDateTime(localDateTime);
                    return true;
                }
            }
        }
        return false;
    }
    private void FindMatchesOffersFutureArrival(TripRequest tripRequest, LinkedList<SingleValidMatchedOffers> singleValidMatchedOffersLinkedList, Integer numberOffers)
    {
        /**Deceleration of variables */
        LinkedList<LinkedList<RideSingleStation>>  matchOffers = new LinkedList<LinkedList<RideSingleStation>>();
        Set<RideSingleStation> rideSingleStationSet = new  HashSet<RideSingleStation>();

        /**Create request arrival date time */
        LocalDateTime requestDateTime = createRequestDateTime(tripRequest.getDay(), tripRequest.getHour(),tripRequest.getMinutes());
        /**Create graph for finding all path algorithm */
        GraphForMatch graphForMatch = createGraphfromOffersArrival(rideSingleStationSet);
        /**find wanted arrival station */
        List<RideSingleStation>  rideSingleStationList = findWantedStartStation(rideSingleStationSet,tripRequest.getDestination());
        /**find wanted departure station with correctTime*/
        List<RideSingleStation> timeValidRideSingleStationList = rideSingleStationList.stream().filter(rideSingleStation ->isLocalTimeMatch(requestDateTime,rideSingleStation)).collect(Collectors.toList());

        for(RideSingleStation rideSingleStation: timeValidRideSingleStationList)
        {
            LinkedList<RideSingleStation> visited = new LinkedList();
            visited.add(rideSingleStation);
            depthFirst(graphForMatch, visited,  matchOffers, tripRequest.getCurrentLocation(), tripRequest.getIsFutureTime());
        }
        matchOffers = reverseRouteForArrivalOffers(matchOffers);
        modifyMatchesDateTimeArrival( matchOffers);
        modifyMatchesPerCapacity( matchOffers);
        setAllFieldsSingleValidMatchedOffer(matchOffers, singleValidMatchedOffersLinkedList, tripRequest, numberOffers);
    }

    private LocalDateTime createRequestDateTime(int day, int hour, int minutes)
    {
        LocalDateTime requestDateTime = LocalDateTime.of(0,1,  1, hour,minutes);
        requestDateTime = requestDateTime.plusDays(day-1);
        return requestDateTime;
    }
    private void FindMatchesOffersFutureDeparture(TripRequest tripRequest, LinkedList<SingleValidMatchedOffers> singleValidMatchedOffersLinkedList, Integer numberOffers)
    {
        /**Deceleration of variables */
        LinkedList<LinkedList<RideSingleStation>>  matchOffers = new LinkedList<LinkedList<RideSingleStation>>();
        Set<RideSingleStation> rideSingleStationSet = new  HashSet<RideSingleStation>();

        /**Create request departure date time */
        LocalDateTime requestDateTime = createRequestDateTime(tripRequest.getDay(), tripRequest.getHour(),tripRequest.getMinutes());
        /**Create graph for finding all path algorithm */
        GraphForMatch graphForMatch = createGraphfromOffersDeparture(rideSingleStationSet);
        /**find wanted departure station */
        List<RideSingleStation>  rideSingleStationList = findWantedStartStation(rideSingleStationSet,tripRequest.getCurrentLocation());
        /**find wanted departure station with correctTime*/
        List<RideSingleStation> timeValidRideSingleStationList = rideSingleStationList.stream().filter(rideSingleStation ->isLocalTimeMatch(requestDateTime,rideSingleStation) == true).collect(Collectors.toList());


        for(RideSingleStation rideSingleStation: timeValidRideSingleStationList)
        {
            LinkedList<RideSingleStation> visited = new LinkedList();
            visited.add(rideSingleStation);
            depthFirst(graphForMatch, visited,  matchOffers, tripRequest.getDestination(), tripRequest.getIsFutureTime());

        }
        modifyMatchesDateTimeDeparture( matchOffers);
        modifyMatchesPerCapacity( matchOffers);
        setAllFieldsSingleValidMatchedOffer(matchOffers, singleValidMatchedOffersLinkedList, tripRequest, numberOffers);
    }

    public  LinkedList<LinkedList<RideSingleStation>> reverseRouteForArrivalOffers(LinkedList<LinkedList<RideSingleStation>>  matchOffers)
    {
        LinkedList<LinkedList<RideSingleStation>> linkedList  =  new LinkedList<LinkedList<RideSingleStation>>();
        for(LinkedList<RideSingleStation> rideSingleStations: matchOffers)
        {
            LinkedList<RideSingleStation> newList = new LinkedList<RideSingleStation>();

            for(RideSingleStation rideSingleStation: rideSingleStations)
            {
                newList.addFirst(rideSingleStation);
            }
            linkedList.add(newList);
        }
        return linkedList;
    }
    public void setAllFieldsSingleValidMatchedOffer (LinkedList<LinkedList<RideSingleStation>>  matchOffers, LinkedList<SingleValidMatchedOffers> singleValidMatchedOffersLinkedList, TripRequest tripRequest, Integer numberOffers)
    {
        for(LinkedList<RideSingleStation> rideSingleStations : matchOffers)
        {
            if(numberOffers.equals(0))
                break;
            if(CapacityMatchValid(rideSingleStations))
            {
                SingleValidMatchedOffers singleValidMatchedOffers = new SingleValidMatchedOffers();
                singleValidMatchedOffers.setRideSingleStationLinkedList(rideSingleStations);
                singleValidMatchedOffers.setOfferNumber(singleValidMatchedOffersLinkedList.size());
                singleValidMatchedOffers.setOfferType(isOfferDirect(rideSingleStations));
                singleValidMatchedOffers.setEstimatedTime(findEstimatedTime(tripRequest.getIsFutureTime(),rideSingleStations).toLocalTime());
                singleValidMatchedOffers.setEstimatedTimeString(singleValidMatchedOffers.getEstimatedTime().toString());
                singleValidMatchedOffers.setOfferCost(calculateRideCost(rideSingleStations));
                singleValidMatchedOffers.setPassengerAvgFuelUtilization(getFuelAvgUtilization(rideSingleStations));
                singleValidMatchedOffersLinkedList.add(singleValidMatchedOffers);
                numberOffers --;
            }
        }
    }

    private LocalDateTime findEstimatedTime(boolean IsArrival, LinkedList<RideSingleStation> rideSingleStations)
    {
        if(IsArrival == false)
            return rideSingleStations.get(0).getLocalDateTime();
        else
            return rideSingleStations.get(rideSingleStations.size() - 1).getLocalDateTime();

    }
    public boolean CapacityMatchValid(LinkedList<RideSingleStation> rideSingleStations)
    {
        boolean answer = true;
        for(RideSingleStation rideSingleStation: rideSingleStations)
        {
            if(rideSingleStation.isCapacity() == false)
            {
                answer = false;
                break;
            }
        }
        return answer;
    }
    public boolean isOfferDirect(LinkedList<RideSingleStation> rideSingleStations)
    {
        boolean answer = true;
        for(int i = 0; i < rideSingleStations.size() -1; i++)
        {
            if(rideSingleStations.get(i).getTripOfferOrderNum() != rideSingleStations.get(i+1).getTripOfferOrderNum())
            {
                answer = false;
                break;
            }
        }
        return answer;
    }
    public boolean AddPassengerReview(TripRequest tripRequest,TransPoolTrip transPoolTrip, Integer rate, String comment)
    {
        boolean didRatingAccepted = false;
        OneRating rating = new OneRating();
        rating.setRate(rate);
        if(comment != null)
            rating.setComment(comment);
        rating.setTripRequest(new TripRequest(tripRequest));
        if(transPoolTrip.getRatingDriver().isReviewFound(rating) == false)
        {
            transPoolTrip.getRatingDriver().addOneRating(rating);
            transPoolTrip.getRatingDriver().setAmountOfReview(transPoolTrip.getRatingDriver().getRatingList().size());
            float sum = 0;
            for(OneRating oneRating: transPoolTrip.getRatingDriver().getRatingList())
            {
                sum = sum + oneRating.getRate();
            }
            float newRate = sum / transPoolTrip.getRatingDriver().getAmountOfReview();
            transPoolTrip.getRatingDriver().setRatingNumber(newRate);
            didRatingAccepted = true;
        }
        return didRatingAccepted;
    }
    public  List<RideSingleStation> findWantedStartStation( Set<RideSingleStation> rideSingleStationSet,String station)
    {
        List<RideSingleStation>  rideSingleStationList = rideSingleStationSet.stream()
                .filter(rideSingleStation -> rideSingleStation.name.equals(station)).collect(Collectors.toList());
        return rideSingleStationList;
    }
    public void modifyMatchesPerCapacity(LinkedList<LinkedList<RideSingleStation>> matchedLists)
    {
        for(LinkedList<RideSingleStation> rideSingleStations: matchedLists)
        {

            for(RideSingleStation rideSingleStation: rideSingleStations)
            {
                rideSingleStation.setCapacity(setCapacityMatch(rideSingleStation));
            }
        }
    }




    public boolean setCapacityMatch(RideSingleStation rideSingleStation)
    {
        Optional<TransPoolTrip> transPoolTrip1 = transPool.getPlannedTrips().getTransPoolTrip().stream().filter(transPoolTrip -> transPoolTrip.getOrderNumber() == rideSingleStation.getTripOfferOrderNum()).findFirst();
        if(transPoolTrip1.isPresent()) {
            Optional<CapacityPerTrip> capacityPerTrip = transPoolTrip1.get().getCapacityPerTripList()
                    .stream()
                    .filter(capacityPerTrip1 -> capacityPerTrip1.getDayNumber().equals(rideSingleStation.getLocalDateTime().getDayOfYear())).findFirst();
            if (capacityPerTrip.isPresent())
            {
                Optional<OneCapacity> oneCapacity = capacityPerTrip.get().getCapacityList().stream().filter(c -> c.equals(rideSingleStation.getName())).findFirst();
                if (oneCapacity.isPresent())
                {
                    if(oneCapacity.get().getCapacityNum() == 0)
                        return false;
                }
            }
        }
        return true;
    }
    public void modifyMatchesDateTimeDeparture( LinkedList<LinkedList<RideSingleStation>> matchedLists)
    {
        for(LinkedList<RideSingleStation> rideSingleStations: matchedLists)
        {
            for(int i = 0; i < rideSingleStations.size() - 1; i ++)
            {
                ModifyDateTimeDeparture(rideSingleStations.get(i), rideSingleStations.get(i+1));
            }
        }
    }
    public void modifyMatchesDateTimeArrival( LinkedList<LinkedList<RideSingleStation>> matchedLists) {
        for (LinkedList<RideSingleStation> rideSingleStations : matchedLists) {
            for (int i = rideSingleStations.size() - 1; i > 0; i--) {
                ModifyDateTimeDeArrival(rideSingleStations.get(i), rideSingleStations.get(i - 1));
            }
        }
    }
    public void ModifyDateTimeDeArrival(RideSingleStation lastVisited, RideSingleStation rideSingleStation)
    {
        LocalDateTime localDateTime1 = lastVisited.getLocalDateTime();
        LocalDateTime localDateTime2 = rideSingleStation.getLocalDateTime();
        int dayOfYear1 = localDateTime1.getDayOfYear();
        int dayOfYear2 = localDateTime2.getDayOfYear();
        if(lastVisited.getTripOfferOrderNum() == rideSingleStation.getTripOfferOrderNum())
        {
            int minusMinutes = calculateTripDurationInMinutes( rideSingleStation.name  + "," + lastVisited.name);
            rideSingleStation.setLocalDateTime(localDateTime1.minusMinutes(minusMinutes));
        }
        else
        {
            int valueToSub = findAddValue(rideSingleStation.getRecurrences());
            while (localDateTime1.isAfter(localDateTime2) == false)
            {
                if(localDateTime2.getDayOfYear() > 1)
                    localDateTime2 = localDateTime2.minusDays(valueToSub);
            }
            rideSingleStation.setLocalDateTime(localDateTime2);
        }

    }
    public void ModifyDateTimeDeparture(RideSingleStation lastVisited, RideSingleStation rideSingleStation)
    {
        LocalDateTime localDateTime1 = lastVisited.getLocalDateTime();
        LocalDateTime localDateTime2 = rideSingleStation.getLocalDateTime();
        int dayOfYear1 = localDateTime1.getDayOfYear();
        int dayOfYear2 = localDateTime2.getDayOfYear();
        if(lastVisited.getTripOfferOrderNum() == rideSingleStation.getTripOfferOrderNum())
        {
            int addMinutes = calculateTripDurationInMinutes(lastVisited.name + "," + rideSingleStation.name);
            rideSingleStation.setLocalDateTime(localDateTime1.plusMinutes(addMinutes));
        }
        else
        {
            int valueToAdd = findAddValue(rideSingleStation.getRecurrences());
            while (localDateTime1.isBefore(localDateTime2) == false)
            {
                localDateTime2 = localDateTime2.plusDays(valueToAdd);
            }
            rideSingleStation.setLocalDateTime(localDateTime2);
        }

    }
    private  int findAddValue(String recurrences)
    {
        int value = 0;
        switch (recurrences) {
            case "Daily":
                value = 1;
                break;
            case "BiDaily":
                value = 2;
                break;
            case "Weekly":
                value = 7;
                break;
            case "Monthly":
                value = 30;
                break;
        }
        return value;
    }

    public  Optional<String> findStopInRoute( List<String> stopsList, String stopToFind) {
        return stopsList.stream().filter(s -> s.equals(stopToFind)).findFirst();
    }


    public Stop FindStopFromString(String Stop)
    {
        for(Stop s :transPool.getMapDescriptor().getStops().getStop())
        {
            if(s.getName().equals(Stop))
            {
                return s;
            }
        }
        return null;
    }
    public GraphForMatch createGraphfromOffersArrival( Set<RideSingleStation> rideSingleStationSet)
    {
        GraphForMatch graph = new GraphForMatch();
        for(TransPoolTrip transPoolTrip: transPool.getPlannedTrips().getTransPoolTrip())
        {
            List<String> stopsList = createRouteStopsStringsList(transPoolTrip.getRoute().getPath());
            for(int i = stopsList.size() - 1; i > 0; i--)
            {
                /**add path to graph */
                createGraphElem(rideSingleStationSet,graph,transPoolTrip,stopsList.get(0),stopsList.get(i),stopsList.get(i-1), i, i-1);
            }
        }
        return graph;
    }

    public void createGraphElem(Set<RideSingleStation> rideSingleStationSet, GraphForMatch graph,TransPoolTrip transPoolTrip, String startStation, String station1, String station2,int index1,int index2)
    {
        String route1, route2;
        String tripPath = transPoolTrip.getRoute().getPath();
        /**first elem */
        route1 =  createSubRoute (tripPath, startStation, station1);
        LocalDateTime localDateTime1 = getTripTimeInStationTrip(transPoolTrip.getScheduling().getHourStart(),transPoolTrip.getScheduling().getMinuteStart(),transPoolTrip.getScheduling().getDayStart(),route1);
        RideSingleStation rideSingleStation1 = new RideSingleStation(station1,transPoolTrip.getOrderNumber(),localDateTime1,transPoolTrip.getScheduling().getRecurrences());
        rideSingleStation1.setIndex(index1);
        rideSingleStation1.setPkk(transPoolTrip.getPPK());
        rideSingleStation1.setOwner(transPoolTrip.getOwner());
        rideSingleStation1.setTransPoolTrip(transPoolTrip);
        /**second elem  */
        route2 =  createSubRoute (tripPath,startStation, station2);
        LocalDateTime localDateTime2 = getTripTimeInStationTrip(transPoolTrip.getScheduling().getHourStart(),transPoolTrip.getScheduling().getMinuteStart(),transPoolTrip.getScheduling().getDayStart(),route2);
        RideSingleStation rideSingleStation2 = new RideSingleStation(station2,transPoolTrip.getOrderNumber(),localDateTime2, transPoolTrip.getScheduling().getRecurrences());
        rideSingleStation2.setIndex(index2);
        rideSingleStation2.setPkk(transPoolTrip.getPPK());
        rideSingleStation2.setOwner(transPoolTrip.getOwner());
        rideSingleStation2.setTransPoolTrip(transPoolTrip);
        /**add to graph */
        graph.addEdge(rideSingleStation1,rideSingleStation2);
        /**add to graph all same stations */
        for(RideSingleStation rideSingleStation: rideSingleStationSet)
        {
            if(rideSingleStation.name.equals(rideSingleStation1.name) && rideSingleStation.equals(rideSingleStation1) == false)
                graph.addTwoWayVertex(rideSingleStation1,rideSingleStation);
            if(rideSingleStation.name.equals(rideSingleStation2.name) && rideSingleStation.equals(rideSingleStation2) == false)
                graph.addTwoWayVertex(rideSingleStation2,rideSingleStation);
        }
        rideSingleStationSet.add(rideSingleStation1);
        rideSingleStationSet.add(rideSingleStation2);


    }
    public GraphForMatch createGraphfromOffersDeparture( Set<RideSingleStation> rideSingleStationSet)
    {
        GraphForMatch graph = new GraphForMatch();
        for(TransPoolTrip transPoolTrip: transPool.getPlannedTrips().getTransPoolTrip())
        {
            List<String> stopsList = createRouteStopsStringsList(transPoolTrip.getRoute().getPath());
            for(int i = 0; i < stopsList.size() - 1; i++)
            {
                /**add path to graph */
                createGraphElem(rideSingleStationSet,graph,transPoolTrip,stopsList.get(0),stopsList.get(i),stopsList.get(i+1), i, i+1);
            }
        }
        return graph;
    }

    private boolean isDateTimeValid(RideSingleStation lastVisited, RideSingleStation rideSingleStation, boolean isArrival)
    {
        LocalDateTime localDateTime = lastVisited.getLocalDateTime();
        LocalDateTime localDateTime1 = rideSingleStation.getLocalDateTime();
        if(lastVisited.getTripOfferOrderNum() == rideSingleStation.getTripOfferOrderNum())
            return true;
        else if(isArrival == false)
        {
            if(localDateTime.isBefore(localDateTime1))
                return true;
            else if(rideSingleStation.getRecurrences().equals("Daily") || rideSingleStation.getRecurrences().equals("BiDaily") ||rideSingleStation.getRecurrences().equals("Weekly") || rideSingleStation.getRecurrences().equals("Monthly"))
                return true;
        }
        else if(isArrival)
        {
            if(localDateTime.isAfter(localDateTime1))
                return true;
            else if(rideSingleStation.getRecurrences().equals("OneTime") == false && localDateTime1.getDayOfYear() > 1)
            {
                int valueToSub = findAddValue(rideSingleStation.getRecurrences());
                while (localDateTime.isAfter(localDateTime1) == false)
                {
                    if(localDateTime1.getDayOfYear() - valueToSub < 1)
                        break;
                    localDateTime1= localDateTime1.minusDays(valueToSub);
                }
                if(localDateTime.isAfter(localDateTime1))
                {
                    return true;
                }
            }
        }
        return false;

    }
    private void depthFirst(GraphForMatch graph, LinkedList<RideSingleStation> visited, LinkedList<LinkedList<RideSingleStation>> visited1, String Destination, boolean isArrival) {
        LinkedList<RideSingleStation> nodes = graph.adjacentNodes(visited.getLast());
        LocalDateTime localDateTime = visited.getLast().getLocalDateTime();
        // examine adjacent nodes
        for (RideSingleStation node : nodes) {
            if (visited.contains(node) || isDateTimeValid(visited.getLast(), node, isArrival) == false) {
                continue;
            }
            if (node.name.equals(Destination)) {
                LinkedList <RideSingleStation> rideSingleStations = new LinkedList<RideSingleStation>();
                visited.add(node);
                for(RideSingleStation rideSingleStation: visited)
                {
                    rideSingleStations.add(new RideSingleStation(rideSingleStation));
                }
                visited1.add(rideSingleStations);
                visited.removeLast();
                break;
            }
        }
        {
            for (RideSingleStation node : nodes) {
                if (visited.contains(node) || node.name.equals(Destination) || isDateTimeValid(visited.getLast(), node, isArrival) == false) {
                    continue;
                }
                visited.addLast(node);
                depthFirst(graph, visited, visited1, Destination, isArrival);
                visited.removeLast();
            }
        }
    }

    public float getFuelAvgUtilization(LinkedList<RideSingleStation> rideSingleStations) {
        float sumFuelUtilization = 0;
        int numberOfPaths = 0;
        for(int i = 0; i < rideSingleStations.size()-1; i++) {
            if(rideSingleStations.get(i).getName().equals(rideSingleStations.get(i+1).getName()) == false)
            {
                Path foundedPath = FindPath(rideSingleStations.get(i).getName(),rideSingleStations.get(i+1).getName(), transPool.getMapDescriptor().getPaths().getPath());
                numberOfPaths++;
                sumFuelUtilization += foundedPath.getFuelConsumption();
            }
        }
        return sumFuelUtilization/ numberOfPaths;
    }


    public int calculateRideCost(LinkedList<RideSingleStation> rideSingleStations){
        int result = 0;
        int pathsSum = 0;
        int lastPkk = 0;
        for(int i = 0; i < rideSingleStations.size() -1; i++) {
            lastPkk = rideSingleStations.get(i+1).getPkk();
            if(rideSingleStations.get(i).getTripOfferOrderNum() == rideSingleStations.get(i+1).getTripOfferOrderNum())
                pathsSum += FindPath(rideSingleStations.get(i).getName(),rideSingleStations.get(i+1).getName(), transPool.getMapDescriptor().getPaths().getPath()).getLength();
            else
            {
                result = result + (pathsSum*rideSingleStations.get(i).getPkk());
            }
        }
        result = result + (pathsSum * lastPkk);
        return result;
    }

    public LocalDateTime getTripTimeInStationTrip(int departTimeHours, int departTimeMinutes, int day, String route){
        int rideDurationMinutes = calculateTripDurationInMinutes(route);
        LocalDateTime localDateTime =   LocalDateTime.of(0,1,1,departTimeHours,departTimeMinutes);
        localDateTime = localDateTime.plusDays(day -1);
        localDateTime = localDateTime.plusMinutes(rideDurationMinutes);

        return localDateTime;
    }
    private int calculateTripDurationInMinutes(String route){
        int rideDurationMinutes = 0;
        List<String> routesList = createRouteStopsStringsList(route);

        for(int i = 0; i < routesList.size()-1; i++) {
            Path foundedPath = FindPath(routesList.get(i),routesList.get(i+1),transPool.getMapDescriptor().getPaths().getPath());
            rideDurationMinutes += (foundedPath.getLength() * 60)/foundedPath.getSpeedLimit();
        }

        return rideDurationMinutes;
    }


    private String createSubRoute (String route, String departureStation, String arrivalStation) {
        if(departureStation.equals(arrivalStation))
            return departureStation;
        List<String> routesList = createRouteStopsStringsList(route);
        int departIndex = routesList.indexOf(departureStation);
        int arrivalIndex = routesList.indexOf(arrivalStation);
        routesList = routesList.subList(departIndex ,arrivalIndex + 1);
        return String.join(",",routesList);
    }

    public  List<String> createRouteStopsStringsList (String stopStringsList) {
        return Arrays.asList(stopStringsList.trim().split(",")).stream()
                .map(w -> w.trim())
                .collect(Collectors.toList());
    }

    public void CreateNewTranspoolTrip(String FirstName ,String LastName ,String Route,Integer DayStart,  Integer HourStart ,
                                       Integer Mintues,  Integer ppk , Integer capacity, String recurrences) {
        TransPoolTrip newTranspoolTrip = factory.createTransPoolTrip();
        Route route = factory.createRoute();
        Scheduling scheduling = factory.createScheduling();

        route.setPath(Route.substring(0,Route.length()-1));
        scheduling.setHourStart(HourStart);
        scheduling.setDayStart(DayStart);
        scheduling.setMinuteStart(Mintues);
        scheduling.setRecurrences(recurrences);

        newTranspoolTrip.setOrderNumber(transPool.getPlannedTrips().getTransPoolTrip().size());
        newTranspoolTrip.setOwner(FirstName + " " + LastName);
        newTranspoolTrip.setRoute(route);
        newTranspoolTrip.setScheduling(scheduling);
        newTranspoolTrip.setPPK(ppk);
        newTranspoolTrip.setCapacity(capacity);
        newTranspoolTrip.setCapacityPerTripList(transPool.getPlannedTrips().createCapacityList(newTranspoolTrip, transPool.getMapDescriptor().getPaths().getPath()));
        newTranspoolTrip.setRatingDriver(new RatingDriver());
        transPool.getPlannedTrips().AddNewTranspoolTrip(newTranspoolTrip);

    }

    public List<TransPoolTrip> TripsToRating(TripRequest tripRequest)
    {
        SingleValidMatchedOffers m_MatchTrip = tripRequest.getMatchTrip();
        List<TransPoolTrip> transPoolTrips = new ArrayList<TransPoolTrip>();
        if(m_MatchTrip != null)
        {
            for(RideSingleStation rideSingleStation: m_MatchTrip.getRideSingleStationLinkedList())
            {
                if(transPoolTrips.contains(rideSingleStation.getTransPoolTrip()) == false)
                    transPoolTrips.add(rideSingleStation.getTransPoolTrip());
            }
        }
        return transPoolTrips;
    }
    public void updateSingleRideCapacity(LinkedList<RideSingleStation> Information,TripRequest tripRequest)
    {
        Optional<TransPoolTrip> transPoolTrip = transPool.getPlannedTrips().getTransPoolTrip().stream().filter(transPoolTrip1 -> transPoolTrip1.getOrderNumber() == Information.getLast().getTripOfferOrderNum()).findFirst();
        Optional<CapacityPerTrip> capacityPerTrip = transPoolTrip.get().getCapacityPerTripList().stream().filter(capacityPerTrip1 -> capacityPerTrip1.getDayNumber().equals(Information.getFirst().getLocalDateTime().getDayOfYear())).findFirst();
        if(capacityPerTrip.isPresent()) {
            capacityPerTrip.get().setMatched(true);
            InsertDataToCapacity(Information, capacityPerTrip.get(), tripRequest);
        }
        else
        {
            CapacityPerTrip capacityPerTrip1 = new CapacityPerTrip(transPoolTrip.get().getCapacityPerTripList().get(0));
            capacityPerTrip1.setDayNumber(Information.getFirst().getLocalDateTime().getDayOfYear());
            capacityPerTrip1.setMatched(true);
            transPoolTrip.get().getCapacityPerTripList().add(capacityPerTrip1);
            InsertDataToCapacity(Information, capacityPerTrip1,tripRequest);
        }
    }

    public void InsertDataToCapacity(LinkedList<RideSingleStation> Information, CapacityPerTrip capacityPerTrip,TripRequest tripRequest)
    {
        for(RideSingleStation rideSingleStation: Information)
        {
            int capacityN = capacityPerTrip.getCapacityList().get(rideSingleStation.getIndex()).getCapacityNum();
            capacityPerTrip.getCapacityList().get(rideSingleStation.getIndex()).setCapacityNum(capacityN - 1);
        }
        capacityPerTrip.getCapacityList().get(Information.getFirst().getIndex()).AddPassengerGettingUp(tripRequest);
        capacityPerTrip.getCapacityList().get(Information.getLast().getIndex()).AddPassengerGettingDown(tripRequest);
    }

    public void updateCapacityAfterMatch(SingleValidMatchedOffers singleValidMatchedOffers, TripRequest tripRequest) {
        LinkedList<RideSingleStation> Information = singleValidMatchedOffers.getRideSingleStationLinkedList();
        LinkedList<RideSingleStation> oneRide = new LinkedList<RideSingleStation>();
        int orderNumber = Information.getFirst().getTripOfferOrderNum();
        for(RideSingleStation rideSingleStation : Information)
        {
            if(orderNumber != rideSingleStation.getTripOfferOrderNum())
            {
                updateSingleRideCapacity(oneRide,tripRequest);
                oneRide.clear();
            }
            oneRide.add(rideSingleStation);
            orderNumber = rideSingleStation.getTripOfferOrderNum();
        }
        updateSingleRideCapacity(oneRide,tripRequest);
    }

    public String MakeConnectionRideInfo(SingleValidMatchedOffers singleValidMatchedOffers)
    {
        String text = "";
        LinkedList<RideSingleStation> rideSingleStations = singleValidMatchedOffers.getRideSingleStationLinkedList();
        int rideNum = rideSingleStations.get(0).getTripOfferOrderNum();
        LocalDateTime localDateTime = rideSingleStations.get(0).getLocalDateTime();
        LinkedList<RideSingleStation> sameRide = new LinkedList<RideSingleStation>();
        for(RideSingleStation rideSingleStation: rideSingleStations)
        {
            if(rideNum != rideSingleStation.getTripOfferOrderNum())
            {
                SingleValidMatchedOffers singleValidMatchedOffers1 = new SingleValidMatchedOffers();
                singleValidMatchedOffers1.setRideSingleStationLinkedList(sameRide);
                text = text + MakeSingleRideInfo(singleValidMatchedOffers1) + "\n";
                LocalDateTime localDateTimeLast = sameRide.getLast().getLocalDateTime();
                LocalDateTime localDateTimeNext = rideSingleStation.getLocalDateTime();
                text = text + createWaitString(localDateTimeLast, localDateTimeNext);
                sameRide.clear();
            }
            sameRide.add(new RideSingleStation(rideSingleStation));
            rideNum = rideSingleStation.getTripOfferOrderNum();
            localDateTime = rideSingleStation.getLocalDateTime();
        }
        SingleValidMatchedOffers singleValidMatchedOffers1 = new SingleValidMatchedOffers();
        singleValidMatchedOffers1.setRideSingleStationLinkedList(sameRide);
        text = text + MakeSingleRideInfo(singleValidMatchedOffers1) + "\n";
        return text;
    }
    public String createWaitString(LocalDateTime fromDateTime, LocalDateTime toDateTime)
    {
        LocalDateTime tempDateTime = LocalDateTime.from( fromDateTime );
        long months = tempDateTime.until( toDateTime, ChronoUnit.MONTHS );
        tempDateTime = tempDateTime.plusMonths( months );
        long days = tempDateTime.until( toDateTime, ChronoUnit.DAYS );
        tempDateTime = tempDateTime.plusDays( days );
        long hours = tempDateTime.until( toDateTime, ChronoUnit.HOURS );
        tempDateTime = tempDateTime.plusHours( hours );
        long minutes = tempDateTime.until( toDateTime, ChronoUnit.MINUTES );
        tempDateTime = tempDateTime.plusMinutes( minutes );

        String month = " Months: " + months;
        if(months == 0)
            month = "";
        String day = " Days: " + days;
        if(days == 0)
            day = "";
        String hour = " Hours: " + hours;
        if(hours == 0)
            hour = "";
        String minute = " Minutes: " + minutes;
        return  "Wait -" + month + day + hour + minute + "\n"+ "\n";

    }
    public String MakeSingleRideInfo(SingleValidMatchedOffers singleValidMatchedOffers)
    {
        String text = "Ride Number: " + singleValidMatchedOffers.getRideSingleStationLinkedList().get(0).getTripOfferOrderNum() +"\n"
                +  "Driver's Name: " + singleValidMatchedOffers.getRideSingleStationLinkedList().get(0).getOwner() + "\n"
                + "Departure from Station: " + singleValidMatchedOffers.getRideSingleStationLinkedList().get(0).getName()
                +  " at Day: " + singleValidMatchedOffers.getRideSingleStationLinkedList().get(0).getLocalDateTime().getDayOfYear()
                + " Time: " + singleValidMatchedOffers.getRideSingleStationLinkedList().get(0).getLocalDateTime().toLocalTime().toString() + "\n"
                + "Arrival to Station: " + singleValidMatchedOffers.getRideSingleStationLinkedList().get(singleValidMatchedOffers.getRideSingleStationLinkedList().size() -1).getName()
                + " at Day: " + singleValidMatchedOffers.getRideSingleStationLinkedList().get(singleValidMatchedOffers.getRideSingleStationLinkedList().size() -1).getLocalDateTime().getDayOfYear()
                + " Time: " + singleValidMatchedOffers.getRideSingleStationLinkedList().get(singleValidMatchedOffers.getRideSingleStationLinkedList().size() -1).getLocalDateTime().toLocalTime().toString() + "\n"
                + "Details: \n";

        for(RideSingleStation rideSingleStation: singleValidMatchedOffers.getRideSingleStationLinkedList())
        {
            text = text + rideSingleStation.getName() + " - " + "Day: " + rideSingleStation.getLocalDateTime().getDayOfYear()
                    + " Time: " + rideSingleStation.getLocalDateTime().toLocalTime().toString() + "\n";
        }
        return text;
    }
    public String CreateInformationAlertMsg(SingleValidMatchedOffers tripOfferInformation)
    {
        return CreateOfferInformationAlertMsg(tripOfferInformation);
    }

    public String CreateInformationTripAlertMsg(TransPoolTrip transPoolTrip)
    {
        String text = "";
       List<CapacityPerTrip> capacityPerTripList = transPoolTrip.getCapacityPerTripList();
       for(CapacityPerTrip capacityPerTrip: capacityPerTripList)
       {
           if(capacityPerTrip.isMatched())
           {
               text =  text + "\n" + "Trip Day: " + capacityPerTrip.getDayNumber() + "\n"
                       + "Passengers Order Numbers: " + "\n";
               for(OneCapacity oneCapacity: capacityPerTrip.getCapacityList())
               {
                   String passenger = "";
                   for(TripRequest tripRequest: oneCapacity.getPassengerGettingDown())
                   {
                       passenger = passenger + tripRequest.getOrderNumber() + "\n";
                   }
                   text = text + passenger;
               }
               text = text + "Details: " + "\n";
               for(OneCapacity oneCapacity: capacityPerTrip.getCapacityList())
               {
                   if(oneCapacity.getPassengerGettingUp().size() > 0  || oneCapacity.getPassengerGettingDown().size() > 0)
                   {
                       text = text + "  Station Name: " + oneCapacity.getStationName() + "\n"
                               + "  Time: " + oneCapacity.getLocalTime().toString() + "\n";
                       if(oneCapacity.getPassengerGettingUp().size() > 0)
                       {
                           String gettingUp = "  Passenger getting Up: " + "\n";
                           for(TripRequest tripRequest: oneCapacity.getPassengerGettingUp())
                           {
                               gettingUp = gettingUp + "  " + tripRequest.getFirstName() + " " + tripRequest.getLastName() + "\n";
                           }
                           text = text + gettingUp;
                       }
                       if(oneCapacity.getPassengerGettingDown().size() > 0)
                       {
                           String gettingDown = "  Passenger getting Down: " + "\n";
                           for(TripRequest tripRequest: oneCapacity.getPassengerGettingDown())
                           {
                               gettingDown = gettingDown + "  " + tripRequest.getFirstName() + " " + tripRequest.getLastName() + "\n";
                           }
                           text = text + gettingDown;
                       }
                   }
               }
           }
       }
        return text;
    }

    public String CreateRatingTripAlertMsg(TransPoolTrip transPoolTrip)
    {
        RatingDriver ratingDriver = transPoolTrip.getRatingDriver();
        String text = "Current Rate Number: " + ratingDriver.getRatingNumber() + "\n"
                + "Number of Review: " + ratingDriver.getAmountOfReview() + "\n"
                + "Details: " + "\n";
        for(OneRating oneRating: ratingDriver.getRatingList())
        {
            TripRequest tripRequest = oneRating.getTripRequest();
            text = text + "Number of Request: " + tripRequest.getOrderNumber() + "\n"
                    + "Full Name: " + tripRequest.getFirstName() + " " + tripRequest.getLastName() + "\n"
                    + "Rate Number: " + oneRating.getRate() + "\n";
            if(oneRating.getComment().isEmpty() == false)
                text = text + "Comment: " + oneRating.getComment() + "\n" + "\n";
        }
       return  text;
    }
    public TransPool fromXmlFileToObject(File file) throws Exception {
        errorList = new ArrayList<String>();
        try {

            /**Creates TransPool system from given xml file*/
            JAXBContext jaxbContext = JAXBContext.newInstance(TransPool.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            TransPool transPoolToAdd = (TransPool) jaxbUnmarshaller.unmarshal(file);

            /**modify stops of any extra space in the stop's name*/
            transPoolToAdd.getMapDescriptor().getStops().getStop().stream().forEach(stop -> stop.setName(stop.getName().trim()));

            /**Checks if the given TransPool system after creation*/
            checkTransPoolAfterCreation(transPoolToAdd);
            if (errorList.isEmpty())
            {
                if(transPoolToAdd.getPlannedTrips() != null)
                {
                    transPoolToAdd.getPlannedTrips().SetOrderNumber();
                    transPoolToAdd.getPlannedTrips().SetCapacityList(transPoolToAdd.getMapDescriptor().getPaths().getPath());
                    transPoolToAdd.getPlannedTrips().createRatingForEachTrip();
                }
                return transPoolToAdd;
            }

            else {
                throw new Exception("");
            }
        } catch (JAXBException jaxBException) {
            throw jaxBException;
        }
        catch (Exception exception) {
            throw exception;
        }
    }






}
