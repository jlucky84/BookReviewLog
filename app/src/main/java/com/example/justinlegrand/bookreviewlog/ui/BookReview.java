package com.example.justinlegrand.bookreviewlog.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.justinlegrand.bookreviewlog.R;
import com.example.justinlegrand.bookreviewlog.model.Book;
import com.example.justinlegrand.bookreviewlog.model.SqliteDB;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BookReview extends Activity {

    private boolean isNewReview = false;
    private SqliteDB db;
    private static final long UNSUCCESSFUL_INSERT_RESULT = -1;
    private static int bookID = 0;
    private final String TAG = getClass().getSimpleName();

    @Bind(R.id.imgBookCover) ImageView mBookCover;
    @Bind(R.id.author) EditText mAuthor;
    @Bind(R.id.title) EditText mTitle;
    @Bind(R.id.dateRead) EditText mDateRead;
    @Bind(R.id.ratingBar) RatingBar mRatingBar;
    @Bind(R.id.ratingValue) EditText mRatingValue;
    @Bind(R.id.bookReview) EditText mBookReview;
    @Bind(R.id.updateButton)Button mUpdateButton;
    @Bind(R.id.returnButton) Button mReturnButton;
    @Bind(R.id.isbn) EditText mISBN;
    @Bind(R.id.bookDbId) TextView mBookDbId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_review);
        ButterKnife.bind(this);
        //get intent with which fields can be populated
        Intent intent = getIntent();
        //populate BookReview fields, which also determines whether the Activity is to create a new review, or display existing one
        populateFields(intent);

        setActivityListeners();
    }

    private void setActivityListeners() {
        //RatingBar and RatingValue are to be updated when the value of the other is changed.
        mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float value, boolean fromUser) {
                if(fromUser){
                    mRatingValue.setText(String.valueOf(value));
                }
            }
        });

        mRatingValue.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                float rating = Float.valueOf(mRatingValue.getText().toString());
                mRatingBar.setRating(rating);
            }
        });

        mUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Clicking to update the review will toggle fields enabled
                if (mUpdateButton.getText().equals(getString(R.string.update_button_text))) {
                    toggleFields(true);
                    mUpdateButton.setText(getString(R.string.done_editing));
                }
                //clicking Done Editing will update the database with the revised review.
                if (mUpdateButton.getText().equals(R.string.done_editing)) {
                    //verify all required fields are populated
                    String missingFields = verifyFields();
                    if(missingFields != "")
                        showMessage("The following field(s) are required before submitting:\n" + missingFields);
                    else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                int updatedRecords = updateRecord(db, Integer.valueOf(mBookDbId.getText().toString()));
                                showMessage(updatedRecords + " Records updated.");

                            }
                        });
                    }
                }
            }
        });

        mReturnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnToBookList();
            }
        });
    }

    private String verifyFields(){
        String missingFields = "";
        if(mTitle.getText() == null || mTitle.getText().toString() == "")
            missingFields += "Title\n";
        if(mAuthor.getText() == null || mAuthor.getText().toString() == "")
            missingFields += "Author\n";
        if(mDateRead.getText() == null || mDateRead.getText().toString() == "")
            missingFields += "Date Read\n";
        if(mISBN.getText() == null || mISBN.getText().toString() == "")
            missingFields += "ISBN\n";
        if(mBookReview.getText() == null || mBookReview.getText().toString() == "")
            missingFields += "Book Review";

        return missingFields;
    }

    private void insertRecord() {

        db = new SqliteDB(this);
        long bookDataRow, reviewDataRow = UNSUCCESSFUL_INSERT_RESULT;

        //insert new record into BOOK_DATA
        bookDataRow = insertBook(db,bookID);
        //If successful, insert record into REVIEW_DATA
        if(bookDataRow > UNSUCCESSFUL_INSERT_RESULT)
            reviewDataRow = insertReview(db,bookDataRow);

        if(reviewDataRow > UNSUCCESSFUL_INSERT_RESULT){
            showMessage(getString(R.string.db_insert_successful_message));
            Intent intent = new Intent(this,BookListActivity.class);
            startActivity(intent);
        }
        else
            showMessage(getString(R.string.db_insert_fail_message));
    }

    private void returnToBookList(){
        Intent intent = new Intent(this,BookListActivity.class);
        startActivity(intent);
    }

    private int updateRecord(SqliteDB db, long bookDbId){
        //Sqlite's update method returns the number of rows updated
        int updatedRows = db.updateReview(bookDbId,mRatingBar.getRating(),mDateRead.getText().toString(),mBookReview.getText().toString());
        return updatedRows;
    }

    private boolean selectFromReviews(SqliteDB db) {
        //TODO create select statement in SqliteDB class to select review, return false if record exists

        return false;
    }

    private long insertBook(SqliteDB db, int bookID) {
        //Sqlite's Insert statement returns the row ID of the inserted row, or -1 if unsuccessful
        long insertedRow = db.insertIntoBooks(
                mAuthor.getText().toString(),
                mTitle.getText().toString(),
                mBookCover.getId(),
                formatISBN(),
                true);
        return insertedRow;
    }

    private long insertReview(SqliteDB db, long bookDataID){

        long insertedRow = db.insertIntoReviews(
                    bookDataID,
                    mRatingBar.getRating(),
                    mDateRead.getText().toString(),
                    mBookReview.getText().toString());
        return insertedRow;
    }

    private void populateFields(Intent intent) {
        isNewReview = intent.getBooleanExtra("isNewReview", isNewReview);
        Log.d(TAG,"Is new Book Review? " + isNewReview);


        Book book = (Book) intent.getSerializableExtra("book");
        Log.d(TAG,"Book = " + book);
        boolean bookIsNull = book == null ? true : false;
        Log.d(TAG, "Book is null? " + bookIsNull);
        //bookID = intent.getIntExtra("bookID", bookID);

        //Intent provides setting value of isNewReview, based on viewing existing review or creating a new one
        //if calling intent had no title, only a new review can be created from this Activity
        if(isNewReview || bookIsNull){
            mUpdateButton.setText(getString(R.string.insert_button_text));
            toggleFields(true);
        }
        else{
            mUpdateButton.setText(getString(R.string.update_button_text));
            mAuthor.setText(book.getAuthor());
            mTitle.setText(book.getTitle());
            mDateRead.setText(String.valueOf(book.getDateRead()));
            mRatingBar.setRating(book.getRating());
            mRatingValue.setText(String.valueOf(book.getRating()));
            mBookReview.setText(book.getReview());
            mISBN.setText(String.valueOf(book.getISBN()));
            mBookDbId.setText(book.getDbID());
            toggleFields(false);
        }
    }

    private long formatISBN() {
        String isbn = mISBN.getText().toString();
        isbn = isbn.replace(" ","")
                .replace("-","");
        return Long.valueOf(isbn);
    }

    private void showMessage(String message) {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }

    private void toggleFields(boolean isEnabled) {
        mBookCover.setEnabled(isEnabled);
        mAuthor.setEnabled(isEnabled);
        mTitle.setEnabled(isEnabled);
        mDateRead.setEnabled(isEnabled);
        mRatingBar.setEnabled(isEnabled);
        mRatingValue.setEnabled(isEnabled);
        mISBN.setEnabled(isEnabled);
        mBookReview.setEnabled(isEnabled);
    }
}
