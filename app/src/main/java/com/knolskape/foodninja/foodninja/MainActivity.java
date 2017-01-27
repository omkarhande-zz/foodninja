package com.knolskape.foodninja.foodninja;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

  private final String TAG = "FOODNINJA";
  private final LocationListener mLocationListener = new FNLocationListener();
  private final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 10001;
  private final int MIN_TIME = 300;
  private final int MIN_DISTANCE = 500;
  LocationManager mLocationManager;
  TextView fnTv;
  private Location mCurrentLocation;
  private boolean isInitialized = false;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    setupLocationListener();
  }

  void setupLocationListener() {

    mLocationManager = (LocationManager) this.getSystemService(getApplication().LOCATION_SERVICE);

    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(this,
          new String[] { android.Manifest.permission.ACCESS_FINE_LOCATION },
          MY_PERMISSIONS_REQUEST_READ_CONTACTS);
    } else {
      mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE,
          mLocationListener);
      mCurrentLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
      updateLocation(mCurrentLocation);
      loadRestaurants();
    }
  }

  private void loadRestaurants() {

    if (isInitialized) {
      return;
    }

    isInitialized = true;

    if (mCurrentLocation == null) {
      Toast.makeText(this, "Could not fetch location :(", Toast.LENGTH_LONG).show();
      return;
    }

    RestaurantListFragment fragment = new RestaurantListFragment();
    Bundle bundle = new Bundle();
    bundle.putParcelable("CURRENT_LOC", mCurrentLocation);
    fragment.setArguments(bundle);

    getFragmentManager().beginTransaction()
        .replace(R.id.fragment_list_view_2, fragment, "HI")
        .commit();
  }

  @Override public void onRequestPermissionsResult(int requestCode, String permissions[],
      int[] grantResults) {
    switch (requestCode) {
      case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
          //                    mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER , 300000, 500, mLocationListener);
          //                    mCurrentLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
          //                    updateLocation(mCurrentLocation);
          //                    loadRestaurants();
          setupLocationListener();
        } else {
          Toast.makeText(this, "To continue, enable permissions from settings", Toast.LENGTH_LONG)
              .show();
        }
        return;
      }

      // other 'case' lines to check for other
      // permissions this app might request
    }
  }

  void updateLocation(Location location) {
    mCurrentLocation = location;
  }

  class FNLocationListener implements LocationListener {

    @Override public void onLocationChanged(final Location location) {
      updateLocation(location);
    }

    @Override public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    public void onProviderEnabled(String provider) {
    }

    public void onProviderDisabled(String provider) {
    }
  }
}
