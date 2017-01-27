package com.knolskape.foodninja.foodninja.zomato;

import android.content.ContentValues;
import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.knolskape.foodninja.foodninja.data.FoodNinjaContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by omkar on 24/1/17.
 */

public class GeocodeSearchResponse {
    public List<RestaurantWrapper> nearby_restaurants;

    public ArrayList<ContentValues> getContentValues(){
        ArrayList<ContentValues> restaurantList = new ArrayList<ContentValues>();

        int length = 0;

        for( RestaurantWrapper restaurant: nearby_restaurants){
            restaurantList.add(restaurant.getContentValues());
            length++;
        }

        return  restaurantList;
    }

}
