package com.fusebulb.sidebar.Helpers;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by amiteshmaheshwari on 13/07/16.
 */

public class Downloader {

    private static final String TAG = "Downloader";
    public static final String HOST_NAME = "http://phitoor.com/fusebulb/";
    public static String APP_FOLDER = "";
    private static Context context;
    private static NetworkHelper networkHelper;


    public Downloader(Context mContext){
        this.context = mContext;
        this.APP_FOLDER = getAppFolder(context);
        this.networkHelper = new NetworkHelper(context);
    }

    public static String getAppFolder(Context context){
        return context.getFilesDir().toString()+"/fusebulb/";
    }

    public static File getFile(String file_path) {
        File file = new File(APP_FOLDER, file_path);

        try {

            if (!file.exists()) {

                networkHelper.checkForInternetConnectivity();

                URL url = new URL(HOST_NAME + file_path);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoOutput(true);
                urlConnection.setInstanceFollowRedirects(false);
                urlConnection.connect();

                File mydir = file.getParentFile();

                if (!mydir.exists()) {
                    mydir.mkdirs();
                }

                FileOutputStream fileOutput = new FileOutputStream(file);
                //Stream used for reading the data from the internet
                InputStream inputStream = urlConnection.getInputStream();

                byte[] buffer = new byte[1024];
                int bufferLength = 0;
                int downloadedSize = 0;

                while ((bufferLength = inputStream.read(buffer)) > 0) {
                    fileOutput.write(buffer, 0, bufferLength);
                    downloadedSize += bufferLength;
                }
                fileOutput.close();
            }

        } catch (final Exception e) {
            //showError("Error in downloading the file. Please try again");
            e.printStackTrace();
        }
        return file;
    }

}

