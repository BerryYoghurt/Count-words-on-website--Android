package com.example.instabuginternship.network;

import android.util.Log;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.instabuginternship.ui.MainActivity;

public class MyWebViewClient extends WebViewClient {

    private static final String TAG = "MyWebViewClient";
    ValueCallback<String> callback;

    public MyWebViewClient(ValueCallback<String> callback){
        this.callback = callback;
    }

    @Override
    public void onPageFinished(WebView view, String url){
        super.onPageFinished(view, url);
        /*load all readable text from HTML and send to parser*/
        view.evaluateJavascript("(function(){return document.getElementsByTagName(\"body\")[0].innerText;})()", callback);
    }
}
