package com.example.justinlegrand.bookreviewlog.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.sql.SQLClientInfoException;
import java.util.Arrays;

/**
 * Created by Justin.LeGrand on 12/4/2015.
 */
public class SqliteDB extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "Book.db";
    private static final int DATABASE_VERSION = 4;

    //BOOK_DATA table
    private static final String BOOK_TABLE_NAME = "BOOK_DATA";
    private static final String BOOKS_ID = "ID";
    private static final String AUTHOR = "AUTHOR";
    private static final String TITLE = "TITLE";
    private static final String COVER = "COVER";
    private static final String ISBN = "ISBN";
    private static final String IS_REVIEWED = "IS_REVIEWED";

    //REVIEW_DATA table
    private static final String REVIEW_TABLE_NAME = "REVIEW_DATA";
    private static final String REVIEW_ID = "ID";
    private static final String BOOK_ID = "BOOK_ID";
    private static final String DATE_READ = "DATE_READ";
    private static final String RATING = "RATING";
    private static final String REVIEW = "REVIEW";

    private static final int SUCCESSFUL_DB_CALL = 0;//SQLite returns 0 for successful, and -1 for unsuccessful calls
    private static final long INSERTED_ROW_ID = -1;



    public SqliteDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        SQLiteDatabase db = this.getWritableDatabase();

        //remove old version of table if
        if(db.getVersion() < DATABASE_VERSION)
            context.deleteDatabase(DATABASE_NAME);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createBooksTable = String.format("create table if not exists %s (%s, %s, %s, %s, %s, %s)",
                BOOK_TABLE_NAME,
                formatColumnInfo(BOOKS_ID, "INTEGER", true),
                formatColumnInfo(AUTHOR, "TEXT", false),
                formatColumnInfo(TITLE, "TEXT", false),
                formatColumnInfo(COVER, "BLOB", false),
                //sqlite3 accepts values up to 9223372036854775807, so ISBN can be INTEGER in DB, but must be type Long in code
                formatColumnInfo(ISBN, "INTEGER", false),
                formatColumnInfo(IS_REVIEWED, "INTEGER", false));

        String createReviewsTable = String.format("create table if not exists %s (%s, %s, %s, %s, %s)",
                REVIEW_TABLE_NAME,
                formatColumnInfo(REVIEW_ID, "INTEGER", true),
                formatColumnInfo(BOOK_ID, "INTEGER", false),
                formatColumnInfo(DATE_READ, "TEXT", false),//Dates will be stored in DB in YYYY-MM format
                formatColumnInfo(RATING, "REAL", false),
                formatColumnInfo(REVIEW, "TEXT", false));

            db.execSQL(createBooksTable);
            db.execSQL(createReviewsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + BOOK_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + REVIEW_TABLE_NAME);
        onCreate(db);
    }

    public int getDatabaseVersion(){
        SQLiteDatabase db = this.getReadableDatabase();
        return db.getVersion();
    }

    public long insertIntoBooks(String author, String title, int cover, long isbn, boolean isReviewed){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(this.AUTHOR,author);
        contentValues.put(this.TITLE,title);
        contentValues.put(this.COVER,cover);
        contentValues.put(this.ISBN, isbn);
        int reviewedInt = isReviewed ? 1:0;
        contentValues.put(this.IS_REVIEWED, reviewedInt);

        long insertedRow = db.insert(this.BOOK_TABLE_NAME, null, contentValues);

        return insertedRow;
    }

    public Cursor getAllData(){
        SQLiteDatabase dbRead = this.getReadableDatabase();
        Cursor res = dbRead.rawQuery(SELECT_ALL_DATA, null);
        return res;
    }

    public Cursor getBookData(){
        SQLiteDatabase dbRead = this.getReadableDatabase();
        Cursor res = dbRead.rawQuery(SELECT_BOOK_DATA,null);
        return res;
    }

    public Cursor selectBookIDByTitleAuthor(String title, String author){
        SQLiteDatabase dbRead = this.getReadableDatabase();
        String statement = String.format(SELECT_BOOK_ID_BY_TITLE_AUTHOR, title, author);
        Cursor res = dbRead.rawQuery(statement, null);

        return res;
    }

    public long insertIntoReviews(long bookID, float rating, String dateRead, String review){
        SQLiteDatabase dbWrite = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(this.BOOK_ID,bookID);
        contentValues.put(this.RATING,rating);
        contentValues.put(this.DATE_READ, dateRead);//Date values will be stored in YYYY-MM format
        contentValues.put(this.REVIEW,review);

        long newRowID =dbWrite.insert(REVIEW_TABLE_NAME, null, contentValues);
        return newRowID;
    }

    public int updateReview(long bookID, float rating, String dateRead, String review){
        SQLiteDatabase dbWrite = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(this.RATING,rating);
        contentValues.put(this.DATE_READ, dateRead);//Date values will be stored in YYYY-MM format
        contentValues.put(this.REVIEW,review);

        int updatedRows = dbWrite.update(
                REVIEW_TABLE_NAME,//update Table
                contentValues,//set Values
                "WHERE " + this.BOOK_ID + " = bookID",//where clause
                null );
        return updatedRows;
    }

   private String formatColumnInfo(String colName, String colType, boolean isPrimaryKey){
       String primaryKeyText = isPrimaryKey ? "PRIMARY KEY AUTOINCREMENT" : "";

       String formattedString = String.format("%s %s %s", colName,colType,primaryKeyText);

       return formattedString;
   }

    private static final String SELECT_MAX_BOOK_ID_SQL =
            "select max(" + BOOKS_ID + ")" +
            " from " + BOOK_TABLE_NAME;
    private static final String BOOK_DATA_INSERT_SQL =
            "insert into " + BOOK_TABLE_NAME +
            " values %s, %s, %d, %d";

    private static final String SELECT_WISHLIST_BOOKS_SQL =
            "select *" +
            " from " + BOOK_TABLE_NAME +
            " where " + IS_REVIEWED +" = 0";

    private static final String SELECT_REVIEWED_BOOKS_SQL =
            "select *" +
            " from " + BOOK_TABLE_NAME +
            " where " + IS_REVIEWED + " = 1";

    private static final String SELECT_BOOK_BY_TITLE =
            "select *" +
            " from " + BOOK_TABLE_NAME +
            " where " + TITLE + " = %s";

    private static final String SELECT_ALL_DATA =
            "select *, B.rowid as _id" +
            " from " + BOOK_TABLE_NAME + " as B" +
            " join " + REVIEW_TABLE_NAME + " as R" +
            " where B." + BOOKS_ID + "= R." + BOOK_ID;

    private static final String SELECT_BOOK_DATA =
            "select *" +
            " from " + BOOK_TABLE_NAME;

    private static final String SELECT_BOOK_ID_BY_TITLE_AUTHOR =
            "select " + BOOKS_ID +
            " from " + BOOK_TABLE_NAME +
            " where " + AUTHOR + "= %s" +
            " and " + TITLE + "= %s";
}
