package com.example.instabuginternship.ui;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.instabuginternship.network.NetworkHandler;
import com.example.instabuginternship.R;

import java.util.Map;

public class SearchResultActivity extends AppCompatActivity {

    private Map<String,Integer> map;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result_activity);
        map = NetworkHandler.getMap();
        Log.d("SearchResultActivity","onCreate");
        doSearch(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent){
        super.onNewIntent(intent);
        doSearch(intent);
    }

    private void doSearch(Intent intent){
        if(intent.getAction().equals(Intent.ACTION_SEARCH)){
            String query = intent.getStringExtra(SearchManager.QUERY);
            Integer count = map.get(query);
            if(count == null)
                count = 0;
            TextView textView = findViewById(R.id.tv_searchResult);
            textView.setText(getResources().getString(R.string.search_result, query, count));
        }
    }
}
