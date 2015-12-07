package com.example.justinlegrand.bookreviewlog.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Justin.LeGrand on 12/4/2015.
 */
public class SqliteDB extends SQLiteOpenHelper{

    //SQLiteDatabase mBookDB = SQLiteDatabase.openOrCreateDatabase(R.string.book_database_name,MODE_PRIVATE,null);
    public static final String DATABASE_NAME = "Book.db";

    public static final String BOOK_TABLE = "BOOK_DATA";
    public static final String BOOKS_ID = "ID";
    public static final String AUTHOR = "AUTHOR";
    public static final String TITLE = "TITLE";
    public static final String COVER = "COVER";
    public static final String ISBN = "ISBN";
    public static final String IS_REVIEWED = "IS_REVIEWED";

    public static final String REVIEW_TABLE = "REVIEW_DATA";
    public static final String REVIEW_ID = "ID";
    public static final String DATE_READ = "DATE_READ";
    public static final String RATING = "RATING";
    public static final String REVIEW = "REVIEW";



    public SqliteDB(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createBooksTable = String.format("create table %s (%s, %s, %s, %s, %s, %s)",
                BOOK_TABLE,
                formatColumnInfo(BOOKS_ID, "INTEGER", false),
                formatColumnInfo(AUTHOR, "TEXT", false),
                formatColumnInfo(TITLE, "TEXT", false),
                formatColumnInfo(COVER, "INTEGER", false),
                formatColumnInfo(ISBN, "INTEGER", false),
                formatColumnInfo(IS_REVIEWED, "INTEGER", false));

        db.execSQL(createBooksTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i2) {
        db.execSQL("DROP TABLE IF EXISTS " + BOOK_TABLE);
        onCreate(db);
    }


   private String formatColumnInfo(String colName, String colType, boolean isPrimaryKey){
       String primaryKey = isPrimaryKey ? " PRIMARY KEY AUTOINCREMENT" : "";

       String formattedString = String.format("%s %s %s", colName,colType,primaryKey);

       return formattedString;
   }
}
