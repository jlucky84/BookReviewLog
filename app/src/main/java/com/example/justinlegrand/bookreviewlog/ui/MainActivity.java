package com.example.justinlegrand.bookreviewlog.ui;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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

    SqliteDB mSqliteDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                queryDatabase(mSqliteDB);
            }
        });

        ButterKnife.bind(this);

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

    private void queryDatabase(SqliteDB sqliteDB) {
        SQLiteDatabase db = mSqliteDB.getReadableDatabase();
        updateButtonData(db);
    }


    private void updateButtonData(SQLiteDatabase db) {
        int recordCount = 0;
        db.execSQL(getString(R.string.select_reviewed_books_sql));

        mLoadReviews.setText(String.format(getString(R.string.review_text), recordCount));
    }

    public void showWishlist(){
        //Intent intent = new Intent(this,BookWishlist.class);
    }

    public void showReviewlist(){
        Intent intent = new Intent(this,BookListActivity.class);
        startActivity(intent);
    }

    public void showSettings(){
        //Intent intent = new Intent(this,Settings.class);
    }
}
