package com.fusebulb.sidebar.Parser;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Xml;

import com.fusebulb.sidebar.City;
import com.fusebulb.sidebar.Adapter.CityCardAdapter;
import com.fusebulb.sidebar.Downloader;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by amiteshmaheshwari on 14/07/16.
 */

public class CityParser extends Parser {

    private static final String XML_CITY_ID = "id";
    private static final String XML_CITY_NAME = "name";
    private static final String XML_CITY_LON =  "lon";
    private static final String XML_CITY_LAT =  "lat";
    private static final String XML_CITY_SIZE = "size";

    private static final String TAG = "CityParser";
    private Context context;
    private Downloader downloader;
    private String language_pref;
    RecyclerView cityRecyclerView;
    ProgressDialog progDailog;

    public CityParser(Context app_context, String language, RecyclerView recyclerView){
        context = app_context;
        downloader = new Downloader(context);
        language_pref = language;
        cityRecyclerView = recyclerView;
    }


    @Override
    protected ArrayList doInBackground(Void... params) {
        String file_path = language_pref +"_tour_info.xml";
        Log.d(TAG, "Downloading file:" + file_path);
        File cityFile = downloader.getFile(file_path);

        ArrayList<City> cityList = new ArrayList<City>();
        try {
            FileInputStream is = new FileInputStream(cityFile);
            cityList = parseCities(context, is);

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return cityList;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progDailog = new ProgressDialog(context);
        progDailog.setMessage("Loading...");
        progDailog.setIndeterminate(false);
        progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDailog.setCancelable(true);
        progDailog.show();
    }

    @Override
    protected void onPostExecute(ArrayList result) {
        super.onPostExecute(result);
        progDailog.dismiss();
        cityRecyclerView.setAdapter(new CityCardAdapter(context, language_pref, result));
    }

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
                cityList.add(readCity(parser));
            } else {
                skip(parser);
            }
        }
        return cityList;
    }

    public City readCity(XmlPullParser parser) throws XmlPullParserException, IOException {
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
                String file_path = readText(parser);
                city.setPicture(downloader.getFile(file_path));
            } else if (tag.equals("tours")){
                city.setTourSize(Integer.parseInt(parser.getAttributeValue(NS, XML_CITY_SIZE)));
                city.setCityTours(readText(parser));
            } else {
                skip(parser);
            }
        }
        return city;
    }

}
