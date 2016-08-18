package com.fusebulb.sidebar.Helpers;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.fusebulb.sidebar.Helpers.Downloader;

import java.io.File;

/**
 * Created by amiteshmaheshwari on 20/07/16.
 */
public class FileDownloader extends AsyncTask<String, Void, File> {

    ProgressDialog progDailog;

    private Context context;
    private Downloader downloader;

    public FileDownloader(Context mContext){
        context = mContext;
        downloader = new Downloader(context);
    }

    @Override
    protected File doInBackground(String... params) {
        return  downloader.getFile(params[0]);
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
    protected void onPostExecute(File file) {
        super.onPostExecute(file);
        progDailog.dismiss();
    }

}

