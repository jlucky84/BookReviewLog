package com.example.justinlegrand.bookreviewlog.model;

import java.util.Date;

/**
 * Created by justin.legrand on 12/4/2015.
 */
public class Book {

    private String mAuthor;
    private String mTitle;
    private String mReview;
    private float mRating;
    private Date mDateRead;

    public Date getDateRead() {
        return mDateRead;
    }

    public void setDateRead(Date dateRead) {
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

}
