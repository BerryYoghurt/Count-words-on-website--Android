package com.example.instabuginternship.network;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.instabuginternship.ui.MainActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class BackgroundTasks implements Runnable{

    private static final String TAG = "BackgroundTasks";
    private Handler handler;
    private static Map<String,Integer> map;
    private URL url;
    private MainActivity activity;

    public BackgroundTasks(String url, MainActivity activity) throws MalformedURLException {
        this.url = new URL(url);
        this.activity = activity;
    }

    @Override
    public void run() {
        Looper.prepare();
        handler = new Handler(Looper.myLooper());
        handler.post(this::fetchPage);
        Looper.loop();
    }

    public void fetchPage(){
        Log.d(TAG,"fetchPage:");
        HttpURLConnection httpsURLConnection = null;
        InputStream in = null;

        try{
            /*get HTML*/
            httpsURLConnection = (HttpsURLConnection) url.openConnection();
            in = httpsURLConnection.getInputStream();

            StringBuilder sb = new StringBuilder();//read the whole response to memory
            try(BufferedReader br = new BufferedReader(new InputStreamReader(in))){
                String line;
                while((line = br.readLine()) != null){
                    sb.append(line);
                    sb.append(" ");
                }
            }

            /*pass to webview*/
            activity.runOnUiThread(()->activity.renderHTML(sb.toString()));
        }catch (IOException e){
            Log.d("HTTPS Connection",Log.getStackTraceString(e));
        }finally {
            if(httpsURLConnection != null)
                httpsURLConnection.disconnect();
        }
    }

    public void parseHTML(String text){
        Log.d(TAG, "parseHTML:");

        map = new HashMap<>();

        /*text returned from javascript is JSON escaped, remove this escaping*/
        text = text.replaceAll("\\\\n", "\n");
        text = text.replaceAll("\\\\\"","\"");
        Log.d(TAG, text);
        String[] words = text.split("[ \t\n]");
        for(String word : words){
            if(!word.isEmpty()) {
                if (map.containsKey(word.toLowerCase())) {
                    map.put(word.toLowerCase(), map.get(word.toLowerCase()) + 1);
                } else {
                    map.put(word.toLowerCase(), 1);
                }
            }
        }

        /*notify UI thread that map was loaded*/
        activity.runOnUiThread(()->activity.loadedCallback(map));
        handler.getLooper().quitSafely(); //no need for this thread anymore
    }

    public Handler getHandler(){
        return handler;
    }

    public static Map<String, Integer> getMap() {
        return map;
    }
}
