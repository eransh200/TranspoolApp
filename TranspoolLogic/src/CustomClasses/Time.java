package CustomClasses;

import static java.lang.Math.floor;

public class Time {

    private Integer Day;
    private Integer Hour;
    private Integer Minutes;



    public Time(int Day, int Hour, int Minutes)
    {
        this.Day = Day;
        this.Hour = Hour;
        this.Minutes= Minutes;
    }

    public void AddMinutes(Double MinutesToAdd)
    {

        if(MinutesToAdd + Minutes < 60)
        {
            Minutes = (int)(MinutesToAdd +Minutes);
        }
        else
        {

            while(MinutesToAdd + Minutes >= 60) {
                AddHour((double) 1);
                MinutesToAdd = MinutesToAdd - 60;
            }
            Minutes = (int)(MinutesToAdd + Minutes);

        }
    }

    public void AddHour(Double HourToAdd)
    {

        if(HourToAdd + Hour < 24)
        {
            Hour = (int)(HourToAdd + Hour);
        }
        else
        {
            Hour = (int)(HourToAdd + Hour - 24);
            AddDay((double) 1);
        }
    }

    public void AddDay(Double DayToAdd)
    {

        this.Day = (int)(Day + DayToAdd);
    }


    public Integer getDay() {
        return Day;
    }

    public Integer getHour() {
        return Hour;
    }

    public Integer getMinutes() {
        return Minutes;
    }

    public void setDay(Integer day) {
        Day = day;
    }

    public void setHour(Integer hour) {
        Hour = hour;
    }

    public void setMinutes(Integer minutes) {
        Minutes = minutes;
    }
}
