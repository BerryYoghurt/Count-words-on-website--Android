package com.example.instabuginternship.network;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;


import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**Assumes the webpage is well-formed and can be treated as XML
 * deprecated two days after its creation because making and HTML parser is almost impossible*/
@Deprecated
public class HTMLParser {
    private Map<String, Integer> map;
    XmlPullParser parser;

    private void parse() throws XmlPullParserException, IOException {
        int eventType = parser.getEventType();
        boolean inBody = false;
        String text;
        String[] words;
        while(eventType != XmlPullParser.END_DOCUMENT){
            switch (eventType){
                case XmlPullParser.START_DOCUMENT:
                    Log.d("EVENT","document start");
                    map = new HashMap<>();
                    break;
                case XmlPullParser.START_TAG:
                    Log.d("STARTING TAG NAME",parser.getName());
                    if(parser.getName().equals("body")){
                        inBody = true;
                    }
                    break;
                case XmlPullParser.TEXT:
                    Log.d("TEXT",parser.getText() == null || parser.getText().isEmpty() ? "empty" : parser.getText());
                    if(inBody) {
                        text = parser.getText();
                        words = text.split("[ \n\t]"); //split at whitespace
                        for (String word : words) {
                            if (!word.isEmpty()) {
                                if (map.containsKey(word.toLowerCase())) {
                                    map.put(word.toLowerCase(), map.get(word.toLowerCase()) + 1);
                                } else {
                                    map.put(word.toLowerCase(), 0);
                                }
                            }
                        }
                    }
                    break;
                case XmlPullParser.END_TAG:
                    Log.d("ENDING TAG NAME",parser.getName());
                    if(parser.getName().equals("body")){
                        inBody = false;
                    }
                    break;
                default:
                    break;
            }
            eventType = parser.next();
            if(eventType == XmlPullParser.END_DOCUMENT){
                Log.d("EVENT","end document");
            }
        }
    }

    public HTMLParser(InputStream in){
        parser = Xml.newPullParser();
        try {
            parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parse();
        }catch (XmlPullParserException | IOException e){
            Log.d("Parsing",Log.getStackTraceString(e));
        }
    }

    public Map<String,Integer> getMap(){
        return map;
    }
}
