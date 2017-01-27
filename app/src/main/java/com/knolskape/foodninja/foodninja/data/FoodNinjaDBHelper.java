package com.knolskape.foodninja.foodninja.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.knolskape.foodninja.foodninja.data.FoodNinjaContract.RestaurantEntry;

/**
 * Created by omkar on 19/1/17.
 */

public class FoodNinjaDBHelper extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 9;
    static String DATABASE_NAME = "foodninja.db";
    Context mContext;



    public FoodNinjaDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_RESTAURANT_TABLE = "CREATE TABLE " + RestaurantEntry.TABLE_NAME + " (" +
                RestaurantEntry._ID + " INTEGER PRIMARY KEY," +
                RestaurantEntry.COLUMN_RES_ID + " INTEGER NOT NULL, " +
                RestaurantEntry.COLUMN_RES_NAME + " TEXT UNIQUE NOT NULL, " +
                RestaurantEntry.COLUMN_RES_URL + " TEXT NOT NULL, " +
                RestaurantEntry.COLUMN_RES_ADDRESS + " TEXT NOT NULL, " +
                RestaurantEntry.COLUMN_RES_CITY + " TEXT NOT NULL, " +
                RestaurantEntry.COLUMN_RES_CITY_ID + " INTEGER NOT NULL, " +
                RestaurantEntry.COLUMN_RES_LAT + " REAL NOT NULL, " +
                RestaurantEntry.COLUMN_RES_LON + " REAL NOT NULL, " +
                RestaurantEntry.COLUMN_RES_LOCALITY + " TEXT NOT NULL, " +
                RestaurantEntry.COLUMN_USER_RATING + " REAL NOT NULL, " +
                RestaurantEntry.COLUMN_USER_RATING_COLOR + " TEXT NOT NULL " +
                " );";

        sqLiteDatabase.execSQL(SQL_CREATE_RESTAURANT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RestaurantEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
