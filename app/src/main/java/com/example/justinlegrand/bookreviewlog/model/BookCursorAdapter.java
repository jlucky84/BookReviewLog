package com.example.justinlegrand.bookreviewlog.model;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.justinlegrand.bookreviewlog.R;

/**
 * Created by justin.legrand on 12/9/2015.
 */
public class BookCursorAdapter extends CursorAdapter{

    public BookCursorAdapter(Context context, Cursor cursor, int flags){
        super(context,cursor,flags);
    }

    /**
     * newView is used to inflate a new view and return it. No binding is done with this method.
     * @param context
     * @param cursor
     * @param parent
     * @return
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.book_summary,parent,false);
    }

    /**
     * the bindView method is used to bind cursor data to a given view (i.e. set text in a TextView)
     * @param view
     * @param context
     * @param cursor
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        //find fields to populate in inflated template
        TextView tvAuthor = (TextView) view.findViewById(R.id.author);
        TextView tvTitle = (TextView) view.findViewById(R.id.title);
        TextView tvDateRead = (TextView) view.findViewById(R.id.dateRead);
        RatingBar tvRatingBar = (RatingBar) view.findViewById(R.id.ratingBar);
        ImageView cover = (ImageView) view.findViewById(R.id.imgBookCover);

        //Extract values from Cursor
        String author = cursor.getString(cursor.getColumnIndexOrThrow("AUTHOR"));
        String title = cursor.getString(cursor.getColumnIndexOrThrow("TITLE"));
        float rating = cursor.getFloat(cursor.getColumnIndexOrThrow("RATING"));
        String dateString = cursor.getString(cursor.getColumnIndexOrThrow("DATE_READ"));
        String coverFile = cursor.getString(cursor.getColumnIndexOrThrow("COVER"));

        //populate fields with extracted data
        tvAuthor.setText(author);
        tvTitle.setText(title);
        tvRatingBar.setRating(rating);
        tvDateRead.setText(dateString);
        //TODO set ImageView for cover

    }
}
