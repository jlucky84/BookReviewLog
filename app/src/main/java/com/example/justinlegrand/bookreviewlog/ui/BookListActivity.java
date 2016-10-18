package com.example.justinlegrand.bookreviewlog.ui;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.justinlegrand.bookreviewlog.R;
import com.example.justinlegrand.bookreviewlog.model.Book;
import com.example.justinlegrand.bookreviewlog.model.BookCursorAdapter;
import com.example.justinlegrand.bookreviewlog.model.SqliteDB;

import butterknife.Bind;
import butterknife.ButterKnife;

import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class BookListActivity extends Activity {

    @Bind(R.id.bookList) ListView mBookListView;
    @Bind(R.id.btnNewReview) Button mNewReview;
    @Bind(R.id.listSort) Spinner mSpinner;
    ArrayList<Book> books;
    private final String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);
        ButterKnife.bind(this);

        //create an array adapter to use the spinner
        final ArrayAdapter adapter = ArrayAdapter.createFromResource(
                this, R.array.booklist_sort_options, android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);

        setActivityListeners();

        Cursor res = getBookDBCursor();
        if (res.getCount() == 0) {//empty db
            showMessage(getString(R.string.book_review_list_empty));
        } else {
            buildBookList(res);
        }
    }

    private void setActivityListeners() {
        //Create onItemSelected listener to sort the booklist by the selected value
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //TODO sortBookList(view);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mBookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) mBookListView.getAdapter().getItem(position);

                Log.d(TAG, "Position = " + position);
                Log.d(TAG, "Cursor = " + cursor);

                Book selectedBook = generateBookFromCursor(cursor);
                if(selectedBook != null){
                    Log.d(TAG,"Title = " + selectedBook.getTitle());
                    Log.d(TAG,"Author = " + selectedBook.getAuthor());
                }
                else
                    Log.d(TAG,"BOOK OBJECT IS NULL");

                loadBookReview(selectedBook);
            }
        });

        mNewReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadBookReview(new Book());
            }
        });
    }

    private Cursor getBookDBCursor() {
        SqliteDB db = new SqliteDB(this);
        return  db.getAllData();
    }

    private void buildBookList(Cursor res) {
        books = new ArrayList<Book>();
        while (res.moveToNext()) {
            Book book = generateBookFromCursor(res);
            books.add(book);
        }

        //build the Book Review List within the ListView
        BookCursorAdapter cursorAdapter = new BookCursorAdapter(this, res, 0);
        mBookListView.setAdapter(cursorAdapter);
    }

    private void loadBookReview(Book book) {
        Intent intent = new Intent(this, BookReview.class);
//        intent.putExtra("title", book.getTitle());
//        intent.putExtra("author", book.getAuthor());
//        intent.putExtra("dateRead", book.getDateRead());
//        intent.putExtra("rating", book.getRating());
//        intent.putExtra("isbn", book.getISBN());
//        intent.putExtra("bookReview", book.getReview());
//        intent.putExtra("bookID", book.getDbID());

        intent.putExtra("book", book);

        if (book.getTitle() == null)//new review
            intent.putExtra("isNewReview", true);
        else//existing review
            intent.putExtra("isNewReview", false);

        startActivity(intent);
    }

    private Book generateBookFromCursor(Cursor res){
        Book book = new Book();
        book.setDbID(res.getInt(res.getColumnIndex("ID")));
        book.setTitle(res.getString(res.getColumnIndexOrThrow("TITLE")));
        book.setAuthor(res.getString(res.getColumnIndexOrThrow("AUTHOR")));
        book.setISBN((long) res.getLong(res.getColumnIndexOrThrow("ISBN")));
        book.setDateRead(res.getString(res.getColumnIndexOrThrow("DATE_READ")));
        book.setRating(res.getFloat(res.getColumnIndexOrThrow("RATING")));
        book.setReview(res.getString(res.getColumnIndexOrThrow("REVIEW")));

        return book;
    }

    private void sortBookList(View view) {
        TextView textView = (TextView) view;
        String[] sortOptions = getResources().getStringArray(R.array.booklist_sort_options);
        String selected = (String) textView.getText();
        switch (Arrays.asList(sortOptions).indexOf(selected)) {
            case 1://Title asc.
                //TODO Sort by title, ascending
                break;
            case 2://Title desc.
                //TODO Sort by title descending
                break;
            case 3://Author asc.
                //TODO Sort by author ascending
                break;
            case 4://Author desc.
                //TODO Sort by author descending
                break;
            case 5://Date Read asc.
                //TODO Sort by Date Read ascending
                break;
            case 6://Date Read desc.
                //TODO Sort by Date Read descending
                break;
            case 7://Rating asc.
                //TODO Sort by Rating ascending
                break;
            case 8://Rating desc.
                //TODO Sort by Rating descending
                break;
            default://invalid selection
                showMessage(getString(R.string.invalid_sort_selection));
                break;
        }
    }

    public enum BookComparator implements Comparator<Book> {
        AUTHOR_SORT {
            public int compare(Book b1, Book b2) {
                return b1.getAuthor().compareTo(b2.getAuthor());
            }
        },
        TITLE_SORT {
            public int compare(Book b1, Book b2) {
                return b1.getTitle().compareTo(b2.getTitle());
            }
        },
        DATE_READ_SORT {
            public int compare(Book b1, Book b2) {
                return b1.getDateRead().compareTo(b2.getDateRead());
            }
        },
        RATING_SORT {
            public int compare(Book b1, Book b2) {
                return Float.compare(b1.getRating(), b2.getRating());
            }
        };


        public static Comparator<Book> descending(final Comparator<Book> other) {
            return new Comparator<Book>() {
                @Override
                public int compare(Book book, Book book2) {
                    return -1 * other.compare(book, book2);
                }
            };
        }

        //public static Comparator<Book> getComparator(final bookComparator)


        @Override
        public int compare(Book book, Book book2) {
            return 0;
        }
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
