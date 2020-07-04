package SystemLogic;

import java.util.LinkedList;

public class ValidMatchedOffers {
    private LinkedList<SingleValidMatchedOffers> validOffers = new LinkedList<SingleValidMatchedOffers>();

    public LinkedList<SingleValidMatchedOffers> getValidOffers() {
        return validOffers;
    }

    public void setValidOffers(LinkedList<SingleValidMatchedOffers> validOffers) {
        this.validOffers = validOffers;
    }

    public void AddOffer(SingleValidMatchedOffers validOffer)
    {
        validOffers.add(validOffer);
    }
}
