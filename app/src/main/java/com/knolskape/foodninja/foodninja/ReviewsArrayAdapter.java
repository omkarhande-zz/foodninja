package com.knolskape.foodninja.foodninja;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by omkar on 20/1/17.
 */

public class ReviewsArrayAdapter extends ArrayAdapter {
    public ReviewsArrayAdapter(Context context, ArrayList<Review> reviews) {
        super(context, 0, reviews);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Review review = (Review) getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_review_item, parent, false);
        }

        TextView tvAuthor = (TextView) convertView.findViewById(R.id.review_author);
        TextView tvReviewText = (TextView) convertView.findViewById(R.id.review_text);
        TextView tvReviewRating = (TextView) convertView.findViewById(R.id.review_rating_number);

        tvAuthor.setText(review.authorName);
        tvReviewText.setText(review.reviewText);
        tvReviewRating.setText(String.valueOf(review.reviewRating)+"/5");
        tvReviewRating.setBackgroundColor(Color.parseColor('#'+review.reviewRatingColor));

        return convertView;
    }
}
