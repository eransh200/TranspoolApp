package SystemEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CapacityPerTrip {

    private List<OneCapacity> CapacityList = new ArrayList<OneCapacity>();
    private Integer dayNumber;
    private boolean isMatched = false;

    public boolean isMatched() {
        return isMatched;
    }

    public void setMatched(boolean matched) {
        isMatched = matched;
    }

    public CapacityPerTrip() {
    }

    public CapacityPerTrip(CapacityPerTrip capacityPerTrip) {
        CapacityList = new ArrayList<OneCapacity>();
        for(OneCapacity oneCapacity: capacityPerTrip.getCapacityList()) {
            OneCapacity oneCapacity1 = new OneCapacity(oneCapacity);
            CapacityList.add(oneCapacity1);
        }
        setDayNumber(capacityPerTrip.getDayNumber());
        setMatched(capacityPerTrip.isMatched());
    }
    public List<OneCapacity> getCapacityList() {
        return CapacityList;
    }

    public void setCapacityList(List<OneCapacity> capacityList) {
        CapacityList = capacityList;
    }
    public void AddToCapacityList(OneCapacity capacity)
    {
        CapacityList.add(capacity);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CapacityPerTrip)) return false;
        CapacityPerTrip that = (CapacityPerTrip) o;
        return Objects.equals(getDayNumber(), that.getDayNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDayNumber());
    }

    public Integer getDayNumber() {
        return dayNumber;
    }

    public void setDayNumber(Integer dayNumber) {
        this.dayNumber = dayNumber;
    }
}
