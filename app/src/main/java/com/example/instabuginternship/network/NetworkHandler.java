package com.example.instabuginternship.network;

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

/**No need for this class, moved everything to backgroundtasks to be more succinct*/
@Deprecated
public class NetworkHandler implements Runnable{

    private final URL url;
    private static Map<String,Integer> map;
    /*private Consumer<Map<String,Integer>> callback; consumer requires a higher API level*/
    private final MainActivity activity;

    public NetworkHandler(String url, MainActivity activity) throws MalformedURLException {
        this.url = new URL(url);
        this.activity = activity;
    }

    public void parse(String text){
        Map<String,Integer> map = new HashMap<>();
        String[] words = text.split("[ \t\n]");
        for(String word : words){
            if(!word.isEmpty()) {
                if (map.containsKey(word.toLowerCase())) {
                    map.put(word.toLowerCase(), map.get(word.toLowerCase()) + 1);
                } else {
                    map.put(word.toLowerCase(), 0);
                }
            }
        }
    }


    @Override
    public void run() {
        //if(false){ //TODO I cannot parse HTML for now, so ignore the real parsing work
        HttpURLConnection httpsURLConnection = null;
        InputStream in = null;
        try{
            /*get HTML*/
            httpsURLConnection = (HttpsURLConnection) url.openConnection();
            in = httpsURLConnection.getInputStream();
            String type = httpsURLConnection.getContentType();
            String encoding = httpsURLConnection.getContentEncoding();
            StringBuilder sb = new StringBuilder();
            try(BufferedReader br = new BufferedReader(new InputStreamReader(in))){
                String line;
                while((line = br.readLine()) != null){
                    sb.append(line);
                    sb.append(" ");
                }
            }

            activity.runOnUiThread(() -> activity.loadedCallback(map));
        }catch (IOException e){
            Log.d("HTTPS Connection",Log.getStackTraceString(e));
        }finally {
            if(httpsURLConnection != null)
                httpsURLConnection.disconnect();
        }
        /*}else{
            try {
                Thread.sleep(5000);
                createDummyMap();
                activity.runOnUiThread(()->activity.loadedCallback(map));
            } catch (InterruptedException e) {
                Log.d("SLEEP interrupted", Log.getStackTraceString(e));
            }

        }*/


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
