package com.knolskape.foodninja.foodninja.zomato;


import android.content.ContentValues;
import android.util.Log;

import com.knolskape.foodninja.foodninja.data.FoodNinjaContract;

/**
 * Created by omkar on 24/1/17.
 */

public class Restaurant {
    public String name;
    public LocationDetails location;
    public String thumb;
    public Rating user_rating;
    public int id;

    public ContentValues getContentValues(){
        ContentValues contentValues = new ContentValues();

        contentValues.putAll(location.getContentValues());
        contentValues.putAll(user_rating.getContentValues());

        contentValues.put(FoodNinjaContract.RestaurantEntry.COLUMN_RES_ID, id);
        contentValues.put(FoodNinjaContract.RestaurantEntry.COLUMN_RES_NAME, this.name);
        contentValues.put(FoodNinjaContract.RestaurantEntry.COLUMN_RES_URL, this.thumb);

        return contentValues;

    }

}
