package com.knolskape.foodninja.foodninja.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class FoodNinjaProvider extends ContentProvider {

  static final int WEATHER = 100;
  static final int WEATHER_WITH_LOCATION = 101;
  static final int WEATHER_WITH_LOCATION_AND_DATE = 102;
  static final int LOCATION = 300;
  private static final SQLiteQueryBuilder restautantByLocationQueryBuilder;

  static {
    restautantByLocationQueryBuilder = new SQLiteQueryBuilder();
    restautantByLocationQueryBuilder.setTables(FoodNinjaContract.RestaurantEntry.TABLE_NAME);
  }

  private FoodNinjaDBHelper mOpenHelper;

  private Cursor getRestaurantByLocation(Uri uri, String[] projection, String selection,
      String[] selectionArgs, String sortOrder) {

    return restautantByLocationQueryBuilder.query(mOpenHelper.getReadableDatabase(), projection,
        selection, selectionArgs, null, null, null);
  }

  @Override public boolean onCreate() {
    mOpenHelper = new FoodNinjaDBHelper(getContext());
    return true;
  }

  @Override public String getType(Uri uri) {

    return FoodNinjaContract.RestaurantEntry.CONTENT_TYPE;
  }

  @Override
  public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
      String sortOrder) {

    Cursor returnCursor =
        restautantByLocationQueryBuilder.query(mOpenHelper.getReadableDatabase(), null, null, null,
            null, null, null);

    returnCursor.setNotificationUri(getContext().getContentResolver(), uri);

    return returnCursor;
  }

  @Override public Uri insert(Uri uri, ContentValues values) {
    final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
    Uri returnUri;

    long _id = db.replace(FoodNinjaContract.RestaurantEntry.TABLE_NAME, null, values);
    if (_id > 0) {
      returnUri = FoodNinjaContract.RestaurantEntry.buildRestaurantUri(_id);
    } else {
      throw new android.database.SQLException("FAILED TO INSERT ROW " + uri);
    }

    getContext().getContentResolver().notifyChange(uri, null);
    return returnUri;
  }

  @Override
  public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
    final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

    int rowsUpdated;

    rowsUpdated =
        db.update(FoodNinjaContract.RestaurantEntry.TABLE_NAME, values, selection, selectionArgs);

    if (rowsUpdated != 0) {
      getContext().getContentResolver().notifyChange(uri, null);
    }

    return rowsUpdated;
  }

  @Override public int delete(Uri uri, String selection, String[] selectionArgs) {
    final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

    int rowsDeleted =
        db.delete(FoodNinjaContract.RestaurantEntry.TABLE_NAME, selection, selectionArgs);

    if (rowsDeleted != 0) {
      getContext().getContentResolver().notifyChange(uri, null);
    }
    return rowsDeleted;
  }

  @Override @TargetApi(11) public void shutdown() {
    mOpenHelper.close();
    super.shutdown();
  }
}