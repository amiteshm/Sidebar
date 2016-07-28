package com.fusebulb.sidebar.Parser;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.util.Xml;

import com.fusebulb.sidebar.ClickClipAction;
import com.fusebulb.sidebar.Clip;
import com.fusebulb.sidebar.ClipAction;
import com.fusebulb.sidebar.Downloader;
import com.fusebulb.sidebar.MapClipAction;
import com.fusebulb.sidebar.MediaClipAction;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by amiteshmaheshwari on 22/07/16.
 */
public class TourClipParser extends Parser {

    private static final String XML_CLIP_ID = "id";
    private static final String XML_CLIP_NAME = "name";
    private static final String XML_CLIP_LAT = "lat";
    private static final String XML_CLIP_LON = "lon";

    private static final String XML_ACTION_TYPE = "type";
    private static final String XML_ACTION_ID = "id";
    private static final String XML_ACTION_START_TIME = "start_time";
    private static final String XML_ACTION_END_TIME = "end_time";
    private static final String XML_ACTION_LAT = "lat";
    private static final String XML_ACTION_LON = "lon";


    private static final String TAG = "TourClipParser";
    private Context context;
    //private String langPref;
    private Downloader downloader;
    private String tour_source;
    //private RecyclerView tourRecyclerView;
    private ProgressDialog progDailog;


    public TourClipParser(Context app_context, String tour_source_path) {
        context = app_context;
        downloader = new Downloader(context);
        tour_source = tour_source_path;
        //langPref = language;
    }


    @Override
    protected ArrayList doInBackground(Void... params) {
        Log.d(TAG, "Downloading file:" + tour_source);
        File tourClipsFile = downloader.getFile(tour_source);

        ArrayList<Clip> clipList = new ArrayList<Clip>();
        try {
            FileInputStream is = new FileInputStream(tourClipsFile);
            clipList = parseTourClips(context, is);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return clipList;
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
        //View.setAdapter(new TourCardAdapter(context, langPref, mediaPlayer, result));
    }

    public ArrayList<Clip> parseTourClips(Context context, InputStream in) throws XmlPullParserException, IOException {
        XmlPullParser parser = Xml.newPullParser();
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(in, null);
        parser.nextTag();
        return readClips(context, parser);
    }

    private ArrayList<Clip> readClips(Context context, XmlPullParser parser) throws XmlPullParserException, IOException {
        ArrayList<Clip> clipList = new ArrayList<Clip>();
        parser.require(XmlPullParser.START_TAG, NS, "clips");
        while (parser.next() != XmlPullParser.END_TAG) {

            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("clip")) {
                clipList.add(readClip(context, parser));
            } else {
                skip(parser);
            }
        }
        return clipList;
    }

    public Clip readClip(Context context, XmlPullParser parser) throws XmlPullParserException, IOException {

        parser.require(XmlPullParser.START_TAG, NS, "clip");
        Clip clip = new Clip();
        clip.setId(parser.getAttributeValue(NS, XML_CLIP_ID));
        clip.setName(parser.getAttributeValue(NS, XML_CLIP_NAME));

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String tag = parser.getName();
            if (tag.equals("source")) {
                clip.setClipSource(readText(parser));
                downloader.getFile(clip.getClipSource());
            } else if (tag.equals("location")) {
                clip.setLocation(parser.getAttributeValue(NS, XML_CLIP_LAT), parser.getAttributeValue(NS, XML_CLIP_LON));
                parser.nextTag();
            } else if (tag.equals("picture")) {
                clip.setPictureSource(readText(parser));
                downloader.getFile(clip.getPictureSource());
            } else if (tag.equals("actions")) {
                clip.setClipActions(parseClipActions(context, parser));
            } else if(tag.equals("caption")){
                clip.setActionFileSource(readText(parser));
                downloader.getFile(clip.getActionFileSource());
            }
            else {
                skip(parser);
            }
        }
        return clip;
    }

    public ArrayList<ClipAction> parseClipActions(Context context, XmlPullParser parser) throws XmlPullParserException, IOException {

        ArrayList<ClipAction> clipActions = new ArrayList<ClipAction>();
        parser.require(XmlPullParser.START_TAG, NS, "actions");
        while (parser.next() != XmlPullParser.END_TAG) {

            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("action")) {
                clipActions.add(readAction(context, parser));
            } else {
                skip(parser);
            }
        }
        return clipActions;
    }

    public ClipAction readAction(Context context, XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, NS, "action");
        String action_type = parser.getAttributeValue(NS, XML_ACTION_TYPE);
        ClipAction action;
        switch (action_type) {
            case ClipAction.MapClipAction:
                action = parseMapClipAction(context, parser);
                break;
            case ClipAction.ClickClipAction:
                action = parseClickClipAction(context, parser);
                break;
            case ClipAction.MediaClipAction:
                action = parseMediaClipAction(context, parser);
                break;
            default:
                action = new ClipAction();
                break;
        }
        action.setActionType(action_type);
        return action;
    }

    private ClickClipAction parseClickClipAction(Context context, XmlPullParser parser) throws XmlPullParserException, IOException {
        ClickClipAction action = new ClickClipAction();

        action.setId(parser.getAttributeValue(NS, XML_ACTION_ID));
        action.setStartTime(parser.getAttributeValue(NS, XML_ACTION_START_TIME));
        action.setEndTime(parser.getAttributeValue(NS, XML_ACTION_END_TIME));

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String tag = parser.getName();
            if (tag.equals("title")) {
                action.setTitle(readText(parser));
            } else {
                skip(parser);
            }
        }
        return action;
    }


    private MapClipAction parseMapClipAction(Context context, XmlPullParser parser) throws XmlPullParserException, IOException {
        MapClipAction action = new MapClipAction();

        action.setId(parser.getAttributeValue(NS, XML_ACTION_ID));
        action.setStartTime(parser.getAttributeValue(NS, XML_ACTION_START_TIME));
        action.setEndTime(parser.getAttributeValue(NS, XML_ACTION_END_TIME));

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String tag = parser.getName();
            if (tag.equals("title")) {
                action.setTitle(readText(parser));
            } else if (tag.equals("location")) {
                action.setLocation(parser.getAttributeValue(NS, XML_ACTION_LAT), parser.getAttributeValue(NS, XML_ACTION_LON));
                parser.nextTag();
            } else {
                skip(parser);
            }
        }
        return action;
    }

    private MediaClipAction parseMediaClipAction(Context context, XmlPullParser parser) throws XmlPullParserException, IOException {
        MediaClipAction action = new MediaClipAction();

        action.setId(parser.getAttributeValue(NS, XML_ACTION_ID));
        action.setStartTime(parser.getAttributeValue(NS, XML_ACTION_START_TIME));
        action.setEndTime(parser.getAttributeValue(NS, XML_ACTION_END_TIME));

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String tag = parser.getName();
            if (tag.equals("title")) {
                action.setTitle(readText(parser));
            } else if (tag.equals("source")) {
                action.setMediaSource(readText(parser));
                downloader.getFile(action.getMediaSource());
            } else {
                skip(parser);
            }
        }
        return action;
    }

}
