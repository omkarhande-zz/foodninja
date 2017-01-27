package com.knolskape.foodninja.foodninja;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by omkar on 19/1/17.
 */

public class RestaurantAdapter extends CursorAdapter {

  public Location mLocation;

  public RestaurantAdapter(Context context, Cursor c, int flags, Location location) {
    super(context, c, flags);
    mLocation = location;
  }

  private boolean isNearby(Cursor cursor) {
    float distance;
    Location resLocation;
    double resLat = cursor.getDouble(7);
    double resLon = cursor.getDouble(8);

    if (mLocation != null) {
      resLocation = new Location("");
      resLocation.setLatitude(resLat);
      resLocation.setLongitude(resLon);
      distance = mLocation.distanceTo(resLocation);

      //            Log.v("DISTANCE", String.valueOf(distance));
      return (distance < 1000000);
    } else {
      return false;
    }
  }

  @Override public View getView(int position, View convertView, ViewGroup parent) {
    return super.getView(position, convertView, parent);
  }

  @Override public View newView(Context context, Cursor cursor, ViewGroup parent) {

    View view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);

    String name = cursor.getString(2);
    ViewHolder viewHolder = new ViewHolder(view);
    view.setTag(viewHolder);

    return view;
  }

  @Override public void bindView(View view, Context context, Cursor cursor) {

    String name = cursor.getString(2);
    String rating = cursor.getString(10);
    String ratingColor = cursor.getString(11);

    ViewHolder viewHolder = (ViewHolder) view.getTag();

    if (!isNearby(cursor)) {
      viewHolder.itemView.setVisibility(View.INVISIBLE);
    }
    viewHolder.titleTextView.setText(name);
    viewHolder.ratingTextView.setText(rating + "/5");
    viewHolder.ratingTextView.setBackgroundColor(Color.parseColor("#" + ratingColor));
  }

  public static class ViewHolder {
    //        public final ImageView iconView;
    public final TextView titleTextView;
    public final TextView ratingTextView;
    public final View itemView;

    public ViewHolder(View view) {
      //            iconView = (ImageView) view.findViewById(R.id.list_item_icon);
      titleTextView = (TextView) view.findViewById(R.id.list_item_name_tv);
      ratingTextView = (TextView) view.findViewById(R.id.list_item_rating_tv);
      itemView = view.findViewById(R.id.list_item_ll);
    }
  }
}
