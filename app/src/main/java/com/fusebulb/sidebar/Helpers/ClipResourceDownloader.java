package com.fusebulb.sidebar.Helpers;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.fusebulb.sidebar.Models.Clip;
import com.fusebulb.sidebar.PlayTourActivity;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by amiteshmaheshwari on 19/08/16.
 */
public class ClipResourceDownloader extends AsyncTask<Void, Void, Void> {

    private Downloader downloader;
    private Clip clip;
    private Context context;
    private ProgressDialog progDailog;

    public ClipResourceDownloader(Context app_context, Clip mClip) {
        context = app_context;
        downloader = new Downloader(context);
        clip = mClip;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progDailog = new ProgressDialog(context);
        progDailog.setMessage("Loading...");
        progDailog.setIndeterminate(false);
        progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDailog.setCancelable(false);
        progDailog.show();
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        progDailog.dismiss();

    }

    @Override
    protected Void doInBackground(Void... voids) {
        downloader.getFile(clip.getClipSource());
        downloader.getFile(clip.getPictureSource());
        downloader.getFile(clip.getActionFileSource());
        ArrayList<String> clipResources = clip.getClipResources();
        if (clipResources != null) {
            for (String resource : clipResources) {
                downloader.getFile(resource);
            }
        }
        return null;
    }


}