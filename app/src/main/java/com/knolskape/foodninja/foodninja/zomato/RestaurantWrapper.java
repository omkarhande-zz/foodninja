package com.knolskape.foodninja.foodninja.zomato;

import android.content.ContentValues;

import com.knolskape.foodninja.foodninja.data.FoodNinjaContract;

/**
 * Created by omkar on 25/1/17.
 */

public class RestaurantWrapper {
    Restaurant restaurant;
    public ContentValues getContentValues(){
        return restaurant.getContentValues();
    }

}
