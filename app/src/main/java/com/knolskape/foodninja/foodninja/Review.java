package com.knolskape.foodninja.foodninja;

/**
 * Created by omkar on 20/1/17.
 */

public class Review {
    public long reviewId;
    public String authorName;
    public String reviewText;
    public int reviewRating;
    public long timestamp;
    public String friendlyTime;
    public String reviewRatingColor;

    public Review(long reviewId,
                  String authorName,
                  String reviewText,
                  int reviewRating,
                  long timestamp,
                  String friendlyTime,
                  String reviewRatingColor){

        this.reviewId = reviewId;
        this.authorName = authorName;
        this.reviewText = reviewText;
        this.reviewRating = reviewRating;
        this.timestamp = timestamp;
        this.friendlyTime = friendlyTime;
        this.reviewRatingColor = reviewRatingColor;
    }
}
