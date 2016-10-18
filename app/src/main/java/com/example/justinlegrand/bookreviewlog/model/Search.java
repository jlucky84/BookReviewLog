package com.example.justinlegrand.bookreviewlog.model;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;

//import com.example.justinlegrand.bookreviewlog.R;

public class Search extends Activity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_search);

        //get the intent, verify the action and get the query
        Intent intent = getIntent();
        if(Intent.ACTION_SEARCH.equals(intent.getAction())){
            String query = intent.getStringExtra(SearchManager.QUERY);
            executeQuery(query);
        }
    }

    public void executeQuery(String query){
        //TODO create script to connect to DB
        //SQLiteDatabase db = new SQLiteDatabase();
    }
}
