package com.example.instabuginternship.network;

import android.util.Log;

import com.example.instabuginternship.ui.MainActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class NetworkHandler implements Runnable{

    private final URL url;
    private static Map<String,Integer> map;
    /*private Consumer<Map<String,Integer>> callback; consumer requires a higher API level*/
    private final MainActivity activity;

    public NetworkHandler(String url, MainActivity activity) throws MalformedURLException {
        this.url = new URL(url);
        this.activity = activity;
    }


    @Override
    public void run() {
        if(false){ //TODO I cannot parse HTML for now, so ignore the real parsing work
            HttpURLConnection httpsURLConnection = null;
            InputStream in = null;
            try{
                httpsURLConnection = (HttpsURLConnection) url.openConnection();
                in = httpsURLConnection.getInputStream();
                HTMLParser parser = new HTMLParser(in);
                map = parser.getMap();
                activity.runOnUiThread(() -> activity.loadedCallback(map));
            }catch (IOException e){
                Log.d("HTTPS Connection",Log.getStackTraceString(e));
            }finally {
                if(httpsURLConnection != null)
                    httpsURLConnection.disconnect();
            }
        }else{
            try {
                Thread.sleep(5000);
                createDummyMap();
                activity.runOnUiThread(()->activity.loadedCallback(map));
            } catch (InterruptedException e) {
                Log.d("SLEEP interrupted", Log.getStackTraceString(e));
            }

        }


    }

    private void createDummyMap(){
        map = new HashMap<>();
        String[] names = {"alpha", "beta", "gamma", "delta"};
        for(int i = 0; i < names.length; i++){
            map.put(names[i], i*10);
        }
    }

    public static Map<String, Integer> getMap(){
        return map;
    }
}
