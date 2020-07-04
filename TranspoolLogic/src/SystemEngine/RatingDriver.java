package SystemEngine;

import java.util.ArrayList;
import java.util.List;

public class RatingDriver {

    List<OneRating> ratingList;
    private int amountOfReview;
    private float ratingNumber;

    public void addOneRating (OneRating oneRating)
    {
       ratingList.add(oneRating);
    }

    public RatingDriver() {
        this.ratingList = new ArrayList<OneRating>();
        this.amountOfReview = 0;
        this.ratingNumber = 0;
    }

    public List<OneRating> getRatingList() {
        return ratingList;
    }

    public void setRatingList(List<OneRating> ratingList) {
        this.ratingList = ratingList;
    }

    public int getAmountOfReview() {
        return amountOfReview;
    }

    public void setAmountOfReview(int amountOfReview) {
        this.amountOfReview = amountOfReview;
    }

    public float getRatingNumber() {
        return ratingNumber;
    }

    public void setRatingNumber(float ratingNumber) {
        this.ratingNumber = ratingNumber;
    }

    public boolean isReviewFound(OneRating rating)
    {
        for(OneRating oneRating: ratingList)
        {
            if(rating.equals(oneRating))
                return true;
        }
        return false;
    }
}
