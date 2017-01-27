package com.knolskape.foodninja.foodninja.zomato;

import android.location.Location;
import android.util.Log;


import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

/**
 * Created by omkar on 24/1/17.
 */

public class ZomatoRestAdapter {
    private final String ZOMATO_BASE_URL = "https://developers.zomato.com/api/v2.1/";

    private ZomatoApi service;

    public ZomatoRestAdapter(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ZOMATO_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        service = retrofit.create(ZomatoApi.class);

    }

    public Observable<GeocodeSearchResponse> getRestaurantsByGeocode(String userKey, Location location, Callback<GeocodeSearchResponse> callback){

        double lat = location.getLatitude();
        double lon = location.getLongitude();

        return service.getRestaurantList(userKey, lat, lon);

    }


}
