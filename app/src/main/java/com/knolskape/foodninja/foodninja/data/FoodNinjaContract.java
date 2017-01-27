package com.knolskape.foodninja.foodninja.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by omkar on 19/1/17.
 */

public class FoodNinjaContract {

  public static final String CONTENT_AUTHORITY = "com.knolskape.foodninja.foodninja";

  public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

  public static final String PATH_RESTAURANT = "restaurant";

  public static final class RestaurantEntry implements BaseColumns {

    public static final Uri CONTENT_URI =
        BASE_CONTENT_URI.buildUpon().appendPath(PATH_RESTAURANT).build();

    public static final String CONTENT_TYPE =
        ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RESTAURANT;

    public static final String TABLE_NAME = "restaurant";
    public static final String COLUMN_RES_ID = "id";
    public static final String COLUMN_RES_NAME = "name";
    public static final String COLUMN_RES_URL = "image_url";
    public static final String COLUMN_RES_ADDRESS = "address";
    public static final String COLUMN_RES_CITY = "city";
    public static final String COLUMN_RES_CITY_ID = "city_id";
    public static final String COLUMN_RES_LAT = "latitude";
    public static final String COLUMN_RES_LON = "longitude";
    public static final String COLUMN_RES_LOCALITY = "locality";
    public static final String COLUMN_USER_RATING = "user_rating";
    public static final String COLUMN_USER_RATING_COLOR = "rating_color";

    public static Uri buildRestaurantUri(long id) {
      return ContentUris.withAppendedId(CONTENT_URI, id);
    }
  }
}

