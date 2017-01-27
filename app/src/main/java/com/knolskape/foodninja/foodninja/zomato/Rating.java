package com.knolskape.foodninja.foodninja.zomato;

import android.content.ContentValues;

import com.knolskape.foodninja.foodninja.data.FoodNinjaContract;

/**
 * Created by omkar on 24/1/17.
 */

public class Rating {
    public double aggregate_rating;
    public String rating_text;
    public String rating_color;
    public int votes;


    public ContentValues getContentValues(){
        ContentValues contentValues = new ContentValues();

        contentValues.put(FoodNinjaContract.RestaurantEntry.COLUMN_USER_RATING, this.aggregate_rating);
        contentValues.put(FoodNinjaContract.RestaurantEntry.COLUMN_USER_RATING_COLOR, this.rating_color);

        return contentValues;
    }


}
