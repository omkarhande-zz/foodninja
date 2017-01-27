package com.knolskape.foodninja.foodninja;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

public class DetailActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor> {

  private Uri mUri;
  private int mPos = 0;
  private TextView titleTV;
  private TextView localityTV;
  private TextView ratingTV;
  private ImageView thumbImg;
  private ListView reviewList;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Bundle bundle = getIntent().getExtras();
    mUri = Uri.parse(bundle.getString("url"));
    Log.v("URL", mUri.toString());
    getLoaderManager().restartLoader(0, null, this);
    mPos = bundle.getInt("pos");
    setContentView(R.layout.activity_detail);
    titleTV = (TextView) findViewById(R.id.detail_title);
    localityTV = (TextView) findViewById(R.id.detail_locality);
    ratingTV = (TextView) findViewById(R.id.detail_rating);
    thumbImg = (ImageView) findViewById(R.id.detail_img);
    reviewList = (ListView) findViewById(R.id.review_list);
  }

  @Override public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

    if (mUri != null) {
      // Now create and return a CursorLoader that will take care of
      // creating a Cursor for the data being displayed.
      Log.v("CURSOR", mUri.toString());
      return new CursorLoader(getApplicationContext(), mUri, null, null, null, null);
    } else {

    }
    return null;
  }

  @Override public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
    if (cursor != null && cursor.moveToFirst()) {
      cursor.moveToPosition(mPos);
      String name = cursor.getString(2);
      String locality = cursor.getString(9);
      String rating = cursor.getString(10);
      String ratingColor = cursor.getString(11);

      titleTV.setText(name);
      localityTV.setText(locality);
      ratingTV.setText(rating + "/5");
      ratingTV.setBackgroundColor(Color.parseColor('#' + ratingColor));
      Picasso.with(getApplicationContext()).load(cursor.getString(3)).into(thumbImg);
      FetchReviewsTask reviewsTask = new FetchReviewsTask(getApplication(), reviewList);
      reviewsTask.execute(cursor.getString(1));
    } else {
      Log.d("DEAILALDLS", "EMPTY CURSOR");
    }
  }

  @Override public void onLoaderReset(Loader<Cursor> loader) {

  }
}
