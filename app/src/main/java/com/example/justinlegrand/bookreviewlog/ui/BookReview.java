package com.example.justinlegrand.bookreviewlog.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.example.justinlegrand.bookreviewlog.R;
import butterknife.Bind;
import butterknife.ButterKnife;

public class BookReview extends Activity {

    @Bind(R.id.imgBookCover) ImageView mBookCover;
    @Bind(R.id.author) EditText mAuthor;
    @Bind(R.id.title) EditText mTitle;
    @Bind(R.id.dateRead) EditText mDateRead;
    @Bind(R.id.ratingBar) RatingBar mRatingBar;
    @Bind(R.id.bookReview) EditText mBookReview;
    @Bind(R.id.updateButton)Button mUpdateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_review);

        ButterKnife.bind(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.book_review, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
