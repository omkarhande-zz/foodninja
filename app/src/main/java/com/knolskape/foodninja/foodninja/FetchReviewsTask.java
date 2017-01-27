package com.knolskape.foodninja.foodninja;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.knolskape.foodninja.foodninja.data.FoodNinjaContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by omkar on 20/1/17.
 */

public class FetchReviewsTask extends AsyncTask<String, Void, Void> {

    public String mResultJSON;
    public ListView mListView;
    public Context mContext;

    public FetchReviewsTask(Context c, ListView listView){
        mListView = listView;
        mContext = c;
    }

    @Override
    protected Void doInBackground(String... strings) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        try{
            final String FETCH_REVIEWS_BY_RESTAURANT = "https://developers.zomato.com/api/v2.1/reviews";
            final String RES_ID_PARAM = "res_id";
            final String USERID_HEADER_PARAM = "user-key";
            final String resId = strings[0];

            Uri builtUri = Uri.parse(FETCH_REVIEWS_BY_RESTAURANT).buildUpon()
                    .appendQueryParameter(RES_ID_PARAM, resId)
                    .build();

            URL url = new URL(builtUri.toString());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty(USERID_HEADER_PARAM, "f46a5bc72067fe9cf3429466857c858d");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            mResultJSON = buffer.toString();

        }catch (IOException e){
            Log.e("Error", e.getMessage(), e);
        }
        return null;
    }



    private void getWeatherDataFromJson(String responseStr) throws JSONException {


        try {
            JSONObject responseObject = new JSONObject(responseStr);
            JSONArray restaurants = responseObject.getJSONArray("nearby_restaurants");

            for(int i = 0; i < restaurants.length(); i++) {
                JSONObject restaurant = restaurants.getJSONObject(i).getJSONObject("restaurant");

                ContentValues contentValues = new ContentValues();
                JSONObject rating = restaurant.getJSONObject("user_rating");
                JSONObject location = restaurant.getJSONObject("location");


                contentValues.put(FoodNinjaContract.RestaurantEntry.COLUMN_RES_ID, restaurant.getInt("id"));
                contentValues.put(FoodNinjaContract.RestaurantEntry.COLUMN_RES_NAME, restaurant.getString("name"));
                contentValues.put(FoodNinjaContract.RestaurantEntry.COLUMN_RES_URL, restaurant.getString("thumb"));
                contentValues.put(FoodNinjaContract.RestaurantEntry.COLUMN_RES_ADDRESS, location.getString("address"));
                contentValues.put(FoodNinjaContract.RestaurantEntry.COLUMN_RES_CITY, location.getString("city"));
                contentValues.put(FoodNinjaContract.RestaurantEntry.COLUMN_RES_CITY_ID, location.getString("city_id"));
                contentValues.put(FoodNinjaContract.RestaurantEntry.COLUMN_RES_LAT, location.getString("latitude"));
                contentValues.put(FoodNinjaContract.RestaurantEntry.COLUMN_RES_LON, location.getString("longitude"));
                contentValues.put(FoodNinjaContract.RestaurantEntry.COLUMN_RES_LOCALITY, location.getString("locality"));
                contentValues.put(FoodNinjaContract.RestaurantEntry.COLUMN_USER_RATING, rating.getDouble("aggregate_rating"));
                contentValues.put(FoodNinjaContract.RestaurantEntry.COLUMN_USER_RATING_COLOR, rating.getString("rating_color"));

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onPostExecute(Void aVoid) {
        ArrayList<Review> reviews = new ArrayList<Review>();
        try{
            JSONObject resultJson = new JSONObject(mResultJSON);
            JSONArray reviewsArray = resultJson.getJSONArray("user_reviews");
            for (int i=0;i<reviewsArray.length();i++){
                JSONObject review = reviewsArray.getJSONObject(i).getJSONObject("review");
                reviews.add(new Review(
                        review.getLong("id"),
                        review.getJSONObject("user").getString("name"),
                        review.getString("review_text"),
                        review.getInt("rating"),
                        review.getLong("timestamp"),
                        review.getString("review_time_friendly"),
                        review.getString("rating_color")
                ));
            }

            mListView.setAdapter(new ReviewsArrayAdapter(mContext, reviews));

        }catch (JSONException e){
            Log.d("ERROR!", e.getMessage(), e);
        }


    }
}
