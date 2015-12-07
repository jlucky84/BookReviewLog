package com.example.justinlegrand.bookreviewlog.model;

import android.view.View;

import java.util.Date;

/**
 * Created by justin.legrand on 9/16/2015.
 */
public class BookList {

    private View mBookView;
    private String mTitle;
    private String mAuthor;
    private Date mDateRead;
    private double mRating;


    public View getBookView() {
        return mBookView;
    }

    public void setBookView(View bookView) {
        mBookView = bookView;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String author) {
        mAuthor = author;
    }

    public Date getDateRead() {
        return mDateRead;
    }

    public void setDateRead(Date dateRead) {
        mDateRead = dateRead;
    }

    public double getRating() {
        return mRating;
    }

    public void setRating(double rating) {
        mRating = rating;
    }


    //public void setAdapter(ListAdapter adapter) {
    //    mAdapter = adapter;
    //}
}
