package com.example.justinlegrand.bookreviewlog.model;

import com.example.justinlegrand.bookreviewlog.R;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by justin.legrand on 12/4/2015.
 */
public class Book implements Serializable{

    private String mAuthor;
    private String mTitle;
    private String mReview;
    private float mRating;
    private String mDateRead;
    private long mISBN;
    private int dbID;
    private static final long serialVersionUID = 23059124L;
    private static final String DATE_FORMAT = "M/YYYY";

    public String getDateRead() {
        return mDateRead;
    }

    public void setDateRead(String dateRead) {
        //SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
        //String mDateRead = formatter.parse(dateRead)

        mDateRead = dateRead;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String author) {
        mAuthor = author;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getReview() {
        return mReview;
    }

    public void setReview(String review) {
        mReview = review;
    }

    public float getRating() {
        return mRating;
    }

    public void setRating(float rating) {
        mRating = rating;
    }

    public long getISBN() {
        return mISBN;
    }

    public void setISBN(long ISBN) {
        mISBN = ISBN;
    }

    public int getDbID() {
        return this.dbID;
    }

    public void setDbID(int dbID) {
        this.dbID = dbID;
    }

}
