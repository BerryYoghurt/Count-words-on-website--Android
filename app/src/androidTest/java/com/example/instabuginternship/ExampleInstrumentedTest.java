package com.example.instabuginternship;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.instabuginternship.network.HTMLParser;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Objects;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.instabuginternship", appContext.getPackageName());
    }

    @Test
    public void testParser() throws IOException {
        try(InputStream in = Objects.requireNonNull(getClass().getClassLoader()).getResourceAsStream("test_response.html")){
            assertNotEquals(null,in);
            HTMLParser parser = new HTMLParser(in);
            Map<String, Integer> map = parser.getMap();
            assertFalse(map.isEmpty());
            assertTrue(map.containsKey("ship"));
            assertTrue(map.containsKey("that"));
            assertEquals((int) 1,(int)map.get("ship"));
            assertEquals((int) 3, (int)map.get("that"));
        }

    }

    /*@Test
    public void testUnderstandingOfParser() throws XmlPullParserException, IOException {
        try(InputStream in = Objects.requireNonNull(getClass().getClassLoader()).getResourceAsStream("simple_response.html")){
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES,false);
            parser.setInput(in,null);

            int eventType = parser.getEventType();
            boolean inHead = false;
            while(eventType != XmlPullParser.END_DOCUMENT){
                switch (eventType){
                    case XmlPullParser.START_TAG:
                        if(parser.getName().equals("head")){
                            inHead = true;
                        }
                        Log.i("TAG FOUND",parser.getName());
                        break;
                    case XmlPullParser.TEXT:
                        if(!inHead)
                            Log.i("TEXT FOUND",parser.getText());
                        break;
                    case XmlPullParser.END_TAG:
                        if(parser.getName().equals("head")){
                            inHead = false;
                        }
                    default:
                        break;
                }
                eventType = parser.next();
            }

        }
    }*/
}