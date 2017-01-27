package com.knolskape.foodninja.foodninja;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.knolskape.foodninja.foodninja.data.FoodNinjaContract;
import com.knolskape.foodninja.foodninja.zomato.GeocodeSearchResponse;
import com.knolskape.foodninja.foodninja.zomato.ZomatoRestAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.knolskape.foodninja.foodninja.data.FoodNinjaContract.RestaurantEntry.COLUMN_RES_LAT;
import static com.knolskape.foodninja.foodninja.data.FoodNinjaContract.RestaurantEntry.COLUMN_RES_LON;

/**
 * Created by omkar on 19/1/17.
 */

public class RestaurantListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private RestaurantAdapter mRestaurantAdapter;
    private Location mLocation;
    private static final int RESTAURANT_LOADER = 0;

    private static final String[] RES_COLUMNS = {
            FoodNinjaContract.RestaurantEntry.TABLE_NAME + "." + FoodNinjaContract.RestaurantEntry.COLUMN_RES_ID,
            FoodNinjaContract.RestaurantEntry.COLUMN_RES_NAME,
            FoodNinjaContract.RestaurantEntry.COLUMN_RES_URL
    };



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(RESTAURANT_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);

        LoaderManager.enableDebugLogging(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLocation = getArguments().getParcelable("CURRENT_LOC");

        ZomatoRestAdapter adapter = new ZomatoRestAdapter();

        Callback<GeocodeSearchResponse> geocodeDataAdapter = new Callback<GeocodeSearchResponse>() {

            @Override
            public void onResponse(Call<GeocodeSearchResponse> call, Response<GeocodeSearchResponse> response) {
                insertRestaurantData(response.body());
            }

            @Override
            public void onFailure(Call<GeocodeSearchResponse> call, Throwable t) {

            }
        };

        Location currLoc = mLocation;

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
                        insertRestaurantData(geocodeSearchResponse);
                    }
                });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        return new CursorLoader(getActivity(),
                FoodNinjaContract.RestaurantEntry.CONTENT_URI,
                RES_COLUMNS,
                COLUMN_RES_LAT+"=?" +" AND "+COLUMN_RES_LON+"=?",
                new String[] {String.valueOf(mLocation.getLatitude()), String.valueOf(mLocation.getLongitude())},
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {

        mRestaurantAdapter.changeCursor(cursor);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mRestaurantAdapter.swapCursor(null);
    }





//    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRestaurantAdapter = new RestaurantAdapter(getActivity(), null, 0, mLocation);


        View rootView = inflater.inflate(R.layout.list_fragment, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.fragment_list_view);
        listView.setAdapter(mRestaurantAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                if (cursor != null) {
                    Uri dataUri = FoodNinjaContract.RestaurantEntry.CONTENT_URI;
                    Intent intent = new Intent(getActivity(), DetailActivity.class)
                            .putExtra("url", dataUri.toString())
                            .putExtra("pos", position);

                    startActivity(intent);
                }
            }
        });

        getLoaderManager().restartLoader(RESTAURANT_LOADER, null, this);

        return rootView;
    }

    private void insertRestaurantData(GeocodeSearchResponse response) {
        ContentResolver resolver = getContext().getContentResolver();
        ArrayList<ContentValues> insertList = response.getContentValues();
        for( ContentValues values : insertList){
            resolver.insert(FoodNinjaContract.RestaurantEntry.CONTENT_URI, values);
            Log.v(RestaurantListFragment.class.toString(), "Inserting in DB");
            resolver.notifyChange(FoodNinjaContract.RestaurantEntry.CONTENT_URI, null);
        }
    }


}
