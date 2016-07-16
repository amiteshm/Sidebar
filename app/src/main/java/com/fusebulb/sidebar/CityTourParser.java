package com.fusebulb.sidebar;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by amiteshmaheshwari on 15/07/16.
 */
public class CityTourParser  extends  Parser{

    private static final String XML_TOUR_ID = "id";
    private static final String XML_TOUR_NAME = "name";
    private static final String XML_TOUR_LON =  "lon";
    private static final String XML_TOUR_LAT =  "lat";
    private static final String XML_TOUR_TYPE = "type";
    private static final String XML_TOUR_PRICE = "rate";
    private static final String XML_TOUR_SIZE = "size";

    private static final String ATTRACTION_TOUR = "Attraction";
    private static final String CITY_TOUR = "City Tour";

    private static final String TAG = "CityParser";
    private Context context;
    private Downloader downloader;
    private String tour_source;
    private RecyclerView tourRecyclerView;
    private ProgressDialog progDailog;


    public CityTourParser(Context app_context, String tour_source_path, RecyclerView recyclerView){
        context = app_context;
        downloader = new Downloader(context);
        tour_source = tour_source_path;
        tourRecyclerView = recyclerView;
    }


    @Override
    protected ArrayList doInBackground(Void... params) {
        Log.d(TAG, "Downloading file:" + tour_source);
        File cityToursFile = downloader.getFile(tour_source);

        ArrayList<Tour> tourList = new ArrayList<Tour>();
        try {
            FileInputStream is = new FileInputStream(cityToursFile);
            tourList = parseCityTours(context, is);

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return tourList;
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
        tourRecyclerView.setAdapter(new TourCardAdapter(context, result));
    }

    public ArrayList<Tour> parseCityTours(Context context, InputStream in) throws XmlPullParserException, IOException {
        XmlPullParser parser = Xml.newPullParser();
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(in, null);
        parser.nextTag();
        return readTours(context, parser);
    }

    private ArrayList<Tour> readTours(Context context, XmlPullParser parser) throws XmlPullParserException, IOException {
        ArrayList<Tour> tourList = new ArrayList<Tour>();
        parser.require(XmlPullParser.START_TAG, NS, "tours");
        while (parser.next() != XmlPullParser.END_TAG) {

            if(parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if(name.equals("tour")){
                tourList.add(readTour(context, parser));
            } else {
                skip(parser);
            }
        }
        return tourList;
    }

    public Tour readTour(Context context, XmlPullParser parser) throws XmlPullParserException, IOException {

        parser.require(XmlPullParser.START_TAG, NS, "tour");
        Tour tour= new Tour();
        tour.setId(parser.getAttributeValue(NS, XML_TOUR_ID));
        tour.setName(parser.getAttributeValue(NS, XML_TOUR_NAME));

        while (parser.next() != XmlPullParser.END_TAG){
            if(parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String tag = parser.getName();
            if(tag.equals("location")){
                tour.setLocation(parser.getAttributeValue(NS, XML_TOUR_LAT), parser.getAttributeValue(NS, XML_TOUR_LON));
                parser.nextTag();
            } else if(tag.equals("picture")){
                String file_path = readText(parser);
                tour.setPicture(downloader.getFile(file_path));
            } else if (tag.equals("summary")){
                tour.setSummary(readText(parser));
            } else if(tag.equals("price")){
                tour.setPrice(Integer.parseInt(parser.getAttributeValue(NS, XML_TOUR_PRICE)));
                parser.nextTag();
            }
            else if(tag.equals("clips")){
                tour.setClipSource(readText(parser));
            }
            else {
                skip(parser);
            }
        }
        return tour;
    }
}
