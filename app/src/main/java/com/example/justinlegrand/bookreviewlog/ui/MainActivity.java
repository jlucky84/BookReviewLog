package com.example.justinlegrand.bookreviewlog.ui;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import butterknife.Bind;
import butterknife.ButterKnife;

import com.example.justinlegrand.bookreviewlog.R;
import com.example.justinlegrand.bookreviewlog.model.SqliteDB;


public class MainActivity extends Activity {

    @Bind(R.id.btnReviews) Button mLoadReviews;
    @Bind(R.id.btnWishList) Button mLoadWishlist;
    @Bind(R.id.btnSettings) Button mLoadSettings;

    SqliteDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                db = new SqliteDB(MainActivity.this);
                Cursor res = getDBBookCount();
                updateButtonData(res);
            }
        });

        mLoadReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showReviewlist();
            }
        });

        mLoadWishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showWishlist();
            }
        });

        mLoadSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSettings();
            }
        });

    }

    private Cursor getDBBookCount() {
        db = new SqliteDB(this);
        Cursor res = db.getBookData();
        return res;
    }

    /**
     * updateButtonData()
     * This method updates the BOOK_REVIEW and WISH_LIST buttons
     * based on how many records were found in BOOK_DATA of books of each type
     */
    private void updateButtonData(Cursor res) {
        int recordCount = res.getCount();
        int reviewCount = 0;
        int wishlistCount = 0;
        while(res.moveToNext()){
            if(res.getInt(res.getColumnIndexOrThrow("IS_REVIEWED")) == 1)
                reviewCount++;
            else
                wishlistCount++;
        }
        mLoadReviews.setText(String.format(getString(R.string.review_text), reviewCount));
        mLoadWishlist.setText(String.format(getString(R.string.wishlist_text), wishlistCount));
    }

    public void showWishlist(){
        //Intent intent = new Intent(this,BookWishlist.class);
    }

    public void showReviewlist(){
        Intent intent = new Intent(this,BookListActivity.class);
        startActivity(intent);
    }

    public void showSettings(){
        Intent intent = new Intent(this,Settings.class);
        startActivity(intent);
    }
}
