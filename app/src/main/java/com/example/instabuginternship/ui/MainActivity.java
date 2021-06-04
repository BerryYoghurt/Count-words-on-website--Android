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
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.SearchView;

import com.example.instabuginternship.network.BackgroundTasks;
import com.example.instabuginternship.network.MyWebViewClient;
import com.example.instabuginternship.network.NetworkHandler;
import com.example.instabuginternship.R;

import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private RecyclerView rv;
    private RelativeLayout loadingPanel;
    private WebView webView;
    private ArrayList<Entry> list;
    private boolean ascending = false;
    private BackgroundTasks backgroundTasks;

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
        Log.d(TAG,"loaded list");
        loadingPanel.setVisibility(View.GONE);
        Log.d(TAG,rv.getAdapter() == null? "Adapter is null":"Everything fine");
        rv.getAdapter().notifyItemRangeInserted(0,map.size());

        backgroundTasks = null;
        webView.getSettings().setJavaScriptEnabled(false);
    }

    public void renderHTML(String html){
        Log.d(TAG, "renderHTML: ");
        webView.loadDataWithBaseURL("https://instabug.com",html,"text/html", null, null);
        Log.d(TAG,"loaded webpage");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate:");
        setContentView(R.layout.activity_main);
        loadingPanel = findViewById(R.id.loadingPanel);
        list = new ArrayList<>();
        //list.add(new Entry("Testing",100));
        rv = findViewById(R.id.rv_Words);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(new WordsAdapter(list));

        webView = findViewById(R.id.headless_browser);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new MyWebViewClient(value -> {
            Log.d(TAG, value);
            backgroundTasks.getHandler().post(() -> backgroundTasks.parseHTML(value));
        }));

        try {
            backgroundTasks = new BackgroundTasks("https://instabug.com",this);
            Thread backgroundThread = new Thread(backgroundTasks);
            backgroundThread.start();
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
        Log.d(TAG, manager.getSearchableInfo(getComponentName()) == null ? "NULL searchableinfo":"searchableinfo not null");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.app_bar_search:
                Log.d(TAG,"this should not appear");
                return true;
            case R.id.app_bar_sort:
                if(!ascending){
                    ascending = true;
                    list.sort((o1, o2) -> Integer.compare(o1.getValue(),o2.getValue()));
                }else{
                    ascending = false;
                    list.sort((o1, o2) -> -Integer.compare(o1.getValue(),o2.getValue()));
                }
                Log.d(TAG,"sorting should be done");
                rv.getAdapter().notifyDataSetChanged();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}