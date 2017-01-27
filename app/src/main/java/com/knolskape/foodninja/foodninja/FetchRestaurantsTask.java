package com.knolskape.foodninja.foodninja;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.format.Time;
import android.util.Log;

import com.knolskape.foodninja.foodninja.data.FoodNinjaContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.knolskape.foodninja.foodninja.data.FoodNinjaContract.RestaurantEntry;
import com.knolskape.foodninja.foodninja.zomato.GeocodeSearchResponse;
import com.knolskape.foodninja.foodninja.zomato.Restaurant;
import com.knolskape.foodninja.foodninja.zomato.ZomatoRestAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by omkar on 19/1/17.
 */

public class FetchRestaurantsTask extends AsyncTask<Location, Void, Void> {

    private final Context mContext;

    public FetchRestaurantsTask(Context context) {
        mContext = context;
    }
    private static final String[] RES_COLUMNS = {
            // In this case the id needs to be fully qualified with a table name, since
            // the content provider joins the location & weather tables in the background
            // (both have an _id column)
            // On the one hand, that's annoying.  On the other, you can search the weather table
            // using the location set by the user, which is only in the Location table.
            // So the convenience is worth it.
            FoodNinjaContract.RestaurantEntry.TABLE_NAME + "." + FoodNinjaContract.RestaurantEntry.COLUMN_RES_ID,
            FoodNinjaContract.RestaurantEntry.COLUMN_RES_NAME
    };

    @Override
    protected Void doInBackground(Location... locations) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        ZomatoRestAdapter adapter = new ZomatoRestAdapter();

        Callback<GeocodeSearchResponse> geocodeDataAdapter = new Callback<GeocodeSearchResponse>() {

            @Override
            public void onResponse(Call<GeocodeSearchResponse> call, Response<GeocodeSearchResponse> response) {;
                insertRestaurantData(response.body());
            }

            @Override
            public void onFailure(Call<GeocodeSearchResponse> call, Throwable t) {

            }
        };

        Location currLoc = locations[0];

//        Call<GeocodeSearchResponse> call = adapter.getRestaurantsByGeocode("f46a5bc72067fe9cf3429466857c858d", currLoc, geocodeDataAdapter);
//        call.enqueue(geocodeDataAdapter);

        adapter.getRestaurantsByGeocode("f46a5bc72067fe9cf3429466857c858d", currLoc, geocodeDataAdapter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GeocodeSearchResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(GeocodeSearchResponse geocodeSearchResponse) {

                    }


                });



//        try{
//            final String FETCH_RESTAURANTS_BY_LOCATION = "https://developers.zomato.com/api/v2.1/geocode";
//            final String LAT_PARAM = "lat";
//            final String LON_PARAM = "lon";
//            final String USERID_HEADER_PARAM = "user-key";
//            final Location userLocation = locations[0];
//
//            Uri builtUri = Uri.parse(FETCH_RESTAURANTS_BY_LOCATION).buildUpon()
//                    .appendQueryParameter(LAT_PARAM, Double.toString(userLocation.getLatitude()))
//                    .appendQueryParameter(LON_PARAM, Double.toString(userLocation.getLongitude()))
//                    .build();
//
//            URL url = new URL(builtUri.toString());
//
//            // Create the request to OpenWeatherMap, and open the connection
//            urlConnection = (HttpURLConnection) url.openConnection();
//            urlConnection.setRequestMethod("GET");
//            urlConnection.setRequestProperty(USERID_HEADER_PARAM, "f46a5bc72067fe9cf3429466857c858d");
//            urlConnection.connect();
//
//            InputStream inputStream = urlConnection.getInputStream();
//            StringBuffer buffer = new StringBuffer();
//            if (inputStream == null) {
//                // Nothing to do.
//                return null;
//            }
//            reader = new BufferedReader(new InputStreamReader(inputStream));
//
//            String line;
//            while ((line = reader.readLine()) != null) {
//                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
//                // But it does make debugging a *lot* easier if you print out the completed
//                // buffer for debugging.
//                buffer.append(line + "\n");
//            }
//
//            if (buffer.length() == 0) {
//                // Stream was empty.  No point in parsing.
//                return null;
//            }
//            getWeatherDataFromJson(buffer.toString());
//            Log.v("FETCHED", buffer.toString());
//
//        }catch (IOException e){
//            Log.e("Error", e.getMessage(), e);
//        } catch (JSONException e) {
//            Log.e("JSON ECCEPTOIN", e.getMessage(), e);
//            e.printStackTrace();
//        } finally {
//            if (urlConnection != null) {
//                urlConnection.disconnect();
//            }
//            if (reader != null) {
//                try {
//                    reader.close();
//                } catch (final IOException e) {
//                    Log.e("Error", "Error closing stream", e);
//                }
//            }
//        }
        return null;
    }


    private void insertRestaurantData(GeocodeSearchResponse response) {
        ContentResolver resolver = mContext.getContentResolver();
        ArrayList<ContentValues> insertList = response.getContentValues();
        for( ContentValues values : insertList){
            resolver.insert(RestaurantEntry.CONTENT_URI, values);
        }
    }

//    private void getWeatherDataFromJson(String responseStr) throws JSONException {
//
//
//        try {
//            JSONObject responseObject = new JSONObject(responseStr);
//            JSONArray restaurants = responseObject.getJSONArray("nearby_restaurants");
//
//            for(int i = 0; i < restaurants.length(); i++) {
//                JSONObject restaurant = restaurants.getJSONObject(i).getJSONObject("restaurant");
//
//                ContentValues contentValues = new ContentValues();
//                JSONObject rating = restaurant.getJSONObject("user_rating");
//                JSONObject location = restaurant.getJSONObject("location");
//
////                Log.v("YOOOOO", rating.toString());
//
//                contentValues.put(RestaurantEntry.COLUMN_RES_ID, restaurant.getInt("id"));
//                contentValues.put(RestaurantEntry.COLUMN_RES_NAME, restaurant.getString("name"));
//                contentValues.put(RestaurantEntry.COLUMN_RES_URL, restaurant.getString("thumb"));
//                contentValues.put(RestaurantEntry.COLUMN_RES_ADDRESS, location.getString("address"));
//                contentValues.put(RestaurantEntry.COLUMN_RES_CITY, location.getString("city"));
//                contentValues.put(RestaurantEntry.COLUMN_RES_CITY_ID, location.getString("city_id"));
//                contentValues.put(RestaurantEntry.COLUMN_RES_LAT, location.getString("latitude"));
//                contentValues.put(RestaurantEntry.COLUMN_RES_LON, location.getString("longitude"));
//                contentValues.put(RestaurantEntry.COLUMN_RES_LOCALITY, location.getString("locality"));
//                contentValues.put(RestaurantEntry.COLUMN_USER_RATING, rating.getDouble("aggregate_rating"));
//                contentValues.put(RestaurantEntry.COLUMN_USER_RATING_COLOR, rating.getString("rating_color"));
//
//                Log.v("INSERT DATA", contentValues.toString());
//
//                mContext.getContentResolver().insert(RestaurantEntry.CONTENT_URI, contentValues);
//
//            }
//
//        } catch (JSONException e) {
//            Log.e("ERROR Asynctask", e.getMessage(), e);
//            e.printStackTrace();
//        } finally {
//            Cursor resultCursor =  mContext.getContentResolver().query(RestaurantEntry.CONTENT_URI,null, null, null, null);
//            if(resultCursor.moveToFirst()){
//                String name = resultCursor.getString(2);
//                Log.v("RESULT", name);
//            }else{
//                Log.v("RESULT", "FAILURE" );
//            }
//
//        }
//    }
}

