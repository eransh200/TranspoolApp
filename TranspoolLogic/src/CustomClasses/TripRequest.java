package CustomClasses;


import SystemLogic.SingleValidMatchedOffers;

public class TripRequest
{

  private Integer OrderNumber;
  private String FirstName;
  private String LastName;
  private String CurrentLocation;
  private String Destination;
  private Boolean IsFutureTime;
  private Integer Hour;
  private Integer Minutes;
  private  Integer Day;
  private SingleValidMatchedOffers m_MatchTrip;




  public TripRequest(Integer OrderNumber,String m_Name, String m_CurrentLocation,String m_Destination)
  {
    this.OrderNumber = OrderNumber;
    this.FirstName = m_Name;
    this.CurrentLocation = m_CurrentLocation;
    this.Destination = m_Destination;
  }

  public TripRequest() {

  }

  public Integer getMinutes() {
    return Minutes;
  }

  public void setMinutes(Integer m_Minutes) {
    this.Minutes = m_Minutes;
  }

  public Integer getDay() {
    return Day;
  }

  public void setDay(Integer Day) {
    this.Day = Day;
  }

  public String getFirstName() {
    return FirstName;
  }
  public void setFirstName(String value) {
    FirstName = value;
  }

  public String getLastName() {
    return LastName;
  }
  public void setLastName(String value) {
    LastName = value;
  }

  public String getCurrentLocation() {
    return CurrentLocation;
  }
  public void setCurrentLocation(String value) {
    CurrentLocation = value;
  }

  public String getDestination() {
    return Destination;
  }
  public void setDestination(String value) {
    Destination = value;
  }

  public Integer getHour() {
    return Hour;
  }

  public void setHour(Integer m_Hour) {
    this.Hour = m_Hour;
  }

  public Boolean getIsFutureTime() {
    return IsFutureTime;
  }

  public void setIsFutureTime(Boolean m_Hour) {
    this.IsFutureTime = m_Hour;
  }



  public SingleValidMatchedOffers getMatchTrip() {
    return m_MatchTrip;
  }

  public  TripRequest(TripRequest someObject) {
    setOrderNumber(someObject.getOrderNumber());
    setFirstName(someObject.getFirstName());
    setLastName(someObject.getLastName());
    setCurrentLocation(someObject.getCurrentLocation());
    setDestination(someObject.getDestination());
    setIsFutureTime(someObject.getIsFutureTime());
    setDay(someObject.getDay());
    setHour(someObject.getHour());
    setMinutes(someObject.getMinutes());
    setMatchTrip(someObject.getMatchTrip());
  }

  public void setMatchTrip(SingleValidMatchedOffers m_MatchTrip) {
    this.m_MatchTrip = m_MatchTrip;
  }

  public Integer getOrderNumber() {
    return OrderNumber;
  }

  public void setOrderNumber(Integer orderNumber) {
    OrderNumber = orderNumber;
  }

}