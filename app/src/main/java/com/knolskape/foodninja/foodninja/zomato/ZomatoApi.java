package com.knolskape.foodninja.foodninja.zomato;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by omkar on 24/1/17.
 */

public interface ZomatoApi {
    @GET("geocode")
    Observable<GeocodeSearchResponse> getRestaurantList(@Header("user-key") String user_key, @Query("lat") double lat, @Query("lon") double lon);
}
