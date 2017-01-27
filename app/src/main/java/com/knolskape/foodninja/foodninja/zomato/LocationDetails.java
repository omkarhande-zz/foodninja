package com.knolskape.foodninja.foodninja.zomato;

import android.content.ContentValues;

import com.knolskape.foodninja.foodninja.data.FoodNinjaContract;

/**
 * Created by omkar on 24/1/17.
 */

public class LocationDetails {
    public String address;
    public String locality;
    public String city;
    public int city_id;
    public double latitude;
    public double longitude;
    public String locality_verbose;

    public ContentValues getContentValues(){

        ContentValues contentValues = new ContentValues();

        contentValues.put(FoodNinjaContract.RestaurantEntry.COLUMN_RES_ADDRESS, this.address);
        contentValues.put(FoodNinjaContract.RestaurantEntry.COLUMN_RES_CITY, this.city);
        contentValues.put(FoodNinjaContract.RestaurantEntry.COLUMN_RES_CITY_ID, this.city_id);
        contentValues.put(FoodNinjaContract.RestaurantEntry.COLUMN_RES_LAT, this.latitude);
        contentValues.put(FoodNinjaContract.RestaurantEntry.COLUMN_RES_LON, this.longitude);
        contentValues.put(FoodNinjaContract.RestaurantEntry.COLUMN_RES_LOCALITY, this.locality);

        return contentValues;
    }

    @Override
    public String toString() {
        return address;
    }
}
