package com.fusebulb.sidebar.Helpers;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;

import com.fusebulb.sidebar.UserSettings;

/**
 * Created by amiteshmaheshwari on 10/08/16.
 */

public class NetworkHelper {

    private static Context context;
    private ConnectivityManager connMgr;
    private NetworkInfo networkInfo;
    //private String contentPref;

    public NetworkHelper(Context mContext) {
        context = mContext;
        //contentPref = pref;
    }

    public void checkForInternetConnectivity() {
       // if (contentPref.equals(UserSettings.pref_internet)) {
            connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo == null || !networkInfo.isConnected()){
                displayAlert();
            }
        //}
    }

    public static void displayAlert()
    {

        ((Activity) context).runOnUiThread(new Runnable() {
            public void run() {
                final AlertDialog dialog = new AlertDialog.Builder(context).setMessage("Please connect to the internet")
                        .setTitle("No Internet Connection")
                        .setCancelable(true)
                        .setNeutralButton(android.R.string.ok,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton){
                                        // finish(); Finish is closing the app
                                    }
                                })
                        .show();

            }
        });

    }

}

