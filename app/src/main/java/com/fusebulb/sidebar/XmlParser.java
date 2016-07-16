package com.fusebulb.sidebar;

/**
 * Created by amiteshmaheshwari on 13/07/16.
 */

import android.content.Context;
import android.os.AsyncTask;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;


import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.FileInputStream;
import java.io.InputStream;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.io.FileInputStream;

import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.util.ArrayList;


/**
 * Created by amiteshmaheshwari on 07/07/16.
 */


public class XmlParser extends AsyncTask<Void, Integer, Integer> {

    private static final String NS = null;
    private static final String TAG = "MainActivity";

    private static final String XML_CITY_ID = "id";
    private static final String XML_CITY_NAME = "name";
    private static final String XML_CITY_LON =  "lon";
    private static final String XML_CITY_LAT =  "lat";

    private static final String XML_TOUR_ID = "id";
    private static final String XML_TOUR_NAME = "name";
    private static final String XML_TOUR_TYPE= "type";
    private static final String XML_TOUR_LAT = "lat";
    private static final String XML_TOUR_LON = "lon";

    private static final String XML_CLIP_ID = "id";
    private static final String XML_CLIP_NAME = "name";
    private static final String XML_CLIP_ORDER = "order";

    private static String PARSE_CITY = "parseCity";


//    private static final String TAG = "Downloader";
//    public static final String HOST_NAME = "http://phitoor.com/fusebulb/";
//    public static String APP_FOLDER = "";
//
//    private Context context;
//    private String file_path;


    public XmlParser(){
        //mediaList = new ArrayList<String>();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
       // APP_FOLDER = context.getCacheDir().toString()+"/";
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected Integer doInBackground(Void... params) {
        Log.d(TAG, "Connected to network");
       // downloadFile(HOST_NAME, APP_FOLDER, file_path);
        return null;
    }

    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);
        //Remove the progress bar
        //mBuilder.setContentText("Download complete");
//        mBuilder.setProgress(0, 0, false);
//        mNotifierManager.notify(notificationId, mBuilder.build());
    }



//    public ArrayList<String> parse(InputStream in) throws XmlPullParserException, IOException {
//        try{
//            XmlPullParser parser = Xml.newPullParser();
//            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
//            parser.setInput(in, null);
//            parser.nextTag();
//            readCities(parser);
//        } finally {
//
//        }
//        return mediaList;
//    }

    public ArrayList<City> parseCities(Context context, InputStream in) throws XmlPullParserException, IOException {
        XmlPullParser parser = Xml.newPullParser();
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(in, null);
        parser.nextTag();
        return readCities(context, parser);
    }

    private ArrayList<City> readCities(Context context, XmlPullParser parser) throws XmlPullParserException, IOException {
        ArrayList<City> cityList = new ArrayList<City>();
        parser.require(XmlPullParser.START_TAG, NS, "tour_info");
        while (parser.next() != XmlPullParser.END_TAG) {

            if(parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if(name.equals("city")){
                cityList.add(readCity(context, parser));
            } else {
                skip(parser);
            }
        }
        return cityList;
    }


    public City readCity(Context context, XmlPullParser parser) throws XmlPullParserException, IOException {

        parser.require(XmlPullParser.START_TAG, NS, "city");
        City city = new City();
        city.setId(parser.getAttributeValue(NS, XML_CITY_ID));
        city.setName(parser.getAttributeValue(NS, XML_CITY_NAME));

        while (parser.next() != XmlPullParser.END_TAG){
            if(parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String tag = parser.getName();
            if(tag.equals("location")){
                city.setLocation(parser.getAttributeValue(NS, XML_CITY_LAT), parser.getAttributeValue(NS, XML_CITY_LON));
                parser.nextTag();
            } else if(tag.equals("picture")){
               // city.setPicture(readText(parser));
                //mediaList.add(city.getPicture());
//                Downloader downloader = new Downloader(context, city.getPicture());
//                downloader.execute();
                //Downloader.downloadFile(Downloader.HOST_NAME, Downloader.APP_FOLDER, city.getPicture());
            } else if (tag.equals("tours")){
                readCityTours(parser, city);
            } else {
                skip(parser);
            }
        }
        return city;
    }


    private void readCityTours(XmlPullParser parser, City city) throws XmlPullParserException, IOException {

        parser.require(XmlPullParser.START_TAG, NS, "tours");
        while (parser.next() != XmlPullParser.END_TAG) {

            if(parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if(name.equals("tour")){
                city.setCityTours(readText(parser));
            } else {
                skip(parser);
            }
        }
    }


//    private void readTours(XmlPullParser parser, DatabaseHelper dbHelper, City city) throws XmlPullParserException, IOException {
//
//        parser.require(XmlPullParser.START_TAG, NS, "tours");
//        while (parser.next() != XmlPullParser.END_TAG) {
//
//            if(parser.getEventType() != XmlPullParser.START_TAG) {
//                continue;
//            }
//            String name = parser.getName();
//            if(name.equals("tour")){
//                readTour(parser, dbHelper, city);
//            } else {
//                skip(parser);
//            }
//        }
//    }


//    private void readCityTour(XmlPullParser parser, City city, DatabaseHelper dbHelper) throws XmlPullParserException, IOException {
//        parser.require(XmlPullParser.START_TAG, NS, "tour");
//        Tour tour = new Tour();
//        tour.setId(parser.getAttributeValue(NS, XML_TOUR_ID));
//        tour.setName(parser.getAttributeValue(NS, XML_TOUR_NAME));
//        tour.setTourType(parser.getAttributeValue(NS, XML_TOUR_TYPE));
//        tour.setCity(city.getId());
//
//        while (parser.next() != XmlPullParser.END_TAG){
//            if(parser.getEventType() != XmlPullParser.START_TAG) {
//                continue;
//            }
//
//            String tag = parser.getName();
//            if(tag.equals("tour")){
//                tour.setLocation(parser.getAttributeValue(NS, XML_TOUR_LAT), parser.getAttributeValue(NS, XML_TOUR_LON));
//                parser.nextTag();
//            } else if(tag.equals("summary")){
//                tour.setSummary(readText(parser));
//                //parser.nextTag();
//            } else if(tag.equals("picture")){
//                tour.setPicture(readText(parser));
//                mediaList.add(tour.getPicture());
//                //MainActivity.downloadFile(MainActivity.HOST_NAME, MainActivity.APP_FOLDER, tour.getPicture());
//                // parser.nextTag();
//            } else if (tag.equals("clips")){
//                readClips(parser, dbHelper, tour);
//            }
//            else {
//                skip(parser);
//            }
//        }
//        dbHelper.createTour(tour);
//    }

//    private void readTour(XmlPullParser parser, DatabaseHelper dbHelper, City city) throws XmlPullParserException, IOException {
//        parser.require(XmlPullParser.START_TAG, NS, "tour");
//        Tour tour = new Tour();
//        tour.setId(parser.getAttributeValue(NS, XML_TOUR_ID));
//        tour.setName(parser.getAttributeValue(NS, XML_TOUR_NAME));
//        tour.setTourType(parser.getAttributeValue(NS, XML_TOUR_TYPE));
//        tour.setCity(city.getId());
//
//        while (parser.next() != XmlPullParser.END_TAG){
//            if(parser.getEventType() != XmlPullParser.START_TAG) {
//                continue;
//            }
//
//            String tag = parser.getName();
//            if(tag.equals("location")){
//                tour.setLocation(parser.getAttributeValue(NS, XML_TOUR_LAT), parser.getAttributeValue(NS, XML_TOUR_LON));
//                parser.nextTag();
//            } else if(tag.equals("summary")){
//                tour.setSummary(readText(parser));
//                //parser.nextTag();
//            } else if(tag.equals("picture")){
//                tour.setPicture(readText(parser));
//                mediaList.add(tour.getPicture());
//                //MainActivity.downloadFile(MainActivity.HOST_NAME, MainActivity.APP_FOLDER, tour.getPicture());
//                // parser.nextTag();
//            } else if (tag.equals("clips")){
//                readClips(parser, dbHelper, tour);
//            }
//            else {
//                skip(parser);
//            }
//        }
//        dbHelper.createTour(tour);
//    }

//    public void readClips(XmlPullParser parser, DatabaseHelper dbHelper, Tour tour) throws XmlPullParserException, IOException{
//        parser.require(XmlPullParser.START_TAG, NS, "clips");
//        while (parser.next() != XmlPullParser.END_TAG) {
//            if(parser.getEventType() != XmlPullParser.START_TAG) {
//                continue;
//            }
//            String name = parser.getName();
//            if(name.equals("clip")){
//                readClip(parser, dbHelper, tour);
//            } else {
//                skip(parser);
//            }
//        }
//
//    }

//    public void readClip(XmlPullParser parser, DatabaseHelper dbHelper, Tour tour) throws XmlPullParserException, IOException {
//        parser.require(XmlPullParser.START_TAG, NS, "clip");
//        Clip clip = new Clip();
//        clip.setId(parser.getAttributeValue(NS, XML_CLIP_ID));
//        clip.setName(parser.getAttributeValue(NS, XML_CLIP_NAME));
//        clip.setOrder(parser.getAttributeValue(NS, XML_CLIP_ORDER));
//        clip.setTourId(tour.getId());
//
//        while (parser.next() != XmlPullParser.END_TAG){
//            if(parser.getEventType() != XmlPullParser.START_TAG) {
//                continue;
//            }
//            String tag = parser.getName();
//            if(tag.equals("source")){
//                clip.setSource(readText(parser));
//                mediaList.add(clip.getSource());
//                //MainActivity.downloadFile(MainActivity.HOST_NAME, MainActivity.APP_FOLDER, clip.getSource());
//            } else {
//                skip(parser);
//            }
//        }
//        dbHelper.createClip(clip);
//    }






//    public void readCity(XmlPullParser parser, DatabaseHelper dbHelper) throws XmlPullParserException, IOException {
//
//        parser.require(XmlPullParser.START_TAG, NS, "city");
//        City city = new City();
//        city.setId(parser.getAttributeValue(NS, XML_CITY_ID));
//        city.setName(parser.getAttributeValue(NS, XML_CITY_NAME));
//
//        while (parser.next() != XmlPullParser.END_TAG){
//            if(parser.getEventType() != XmlPullParser.START_TAG) {
//                continue;
//            }
//
//            String tag = parser.getName();
//            if(tag.equals("location")){
//                city.setLocation(parser.getAttributeValue(NS, XML_CITY_LAT), parser.getAttributeValue(NS, XML_CITY_LON));
//                parser.nextTag();
//            } else if(tag.equals("picture")){
//                city.setPicture(readText(parser));
//                mediaList.add(city.getPicture());
//                //MainActivity.downloadFile(MainActivity.HOST_NAME, MainActivity.APP_FOLDER, city.getPicture());
//            } else if (tag.equals("tours")){
//                readTours(parser, dbHelper, city);
//            } else {
//                skip(parser);
//            }
//        }
//
//        dbHelper.createCity(city);
//
//    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }
}
