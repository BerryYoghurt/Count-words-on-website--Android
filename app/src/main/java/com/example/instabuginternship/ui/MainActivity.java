package com.example.instabuginternship.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SearchView;

import com.example.instabuginternship.network.NetworkHandler;
import com.example.instabuginternship.R;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rv;
    private RelativeLayout loadingPanel;
    //private Map<String,Integer> map; //beware to check it is not null before searching or sorting
    private ArrayList<Entry> list;
    private boolean ascending = false;

    protected static class Entry{
        private final String key;
        private final Integer value;

        Entry(String key, Integer value){
            this.key = key;
            this.value = value;
        }

        public String getKey(){
            return key;
        }

        public Integer getValue(){
            return value;
        }
    }

    public void loadedCallback(@NonNull Map<String,Integer> map){
        for(Map.Entry<String,Integer> o : map.entrySet()){
            list.add(new Entry(o.getKey(),o.getValue()));
        }
        Log.d("LIST","loaded list");
        loadingPanel.setVisibility(View.GONE);
        Log.d("LIST",rv.getAdapter() == null? "Adapter is null":"Everything fine");
        rv.getAdapter().notifyItemRangeInserted(0,map.size());
        //rv.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadingPanel = findViewById(R.id.loadingPanel);
        list = new ArrayList<>();
        //list.add(new Entry("Testing",100));
        rv = findViewById(R.id.rv_Words);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(new WordsAdapter(list));

        try {
            Thread networkingThread = new Thread(new NetworkHandler("https://instabug.com", this));
            networkingThread.start();
        } catch (MalformedURLException e) {
            Log.d("URL",Log.getStackTraceString(e));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar, menu);

        SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        searchView.setSearchableInfo(manager.getSearchableInfo(getComponentName())); //get search activity from xml
        Log.d("SEARCH", manager.getSearchableInfo(getComponentName()) == null ? "NULL searchableinfo":"searchableinfo not null");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.app_bar_search:
                Log.d("SEARCH","this should not appear");
                return true;
            case R.id.app_bar_sort:
                if(!ascending){
                    ascending = true;
                    list.sort((o1, o2) -> Integer.compare(o1.getValue(),o2.getValue()));
                }else{
                    ascending = false;
                    list.sort((o1, o2) -> -Integer.compare(o1.getValue(),o2.getValue()));
                }
                Log.d("MainActivity","sorting should be done");
                rv.getAdapter().notifyDataSetChanged();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}