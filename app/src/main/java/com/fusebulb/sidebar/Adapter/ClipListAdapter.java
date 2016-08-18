package com.fusebulb.sidebar.Adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fusebulb.sidebar.Helpers.Downloader;
import com.fusebulb.sidebar.Models.Clip;
import com.fusebulb.sidebar.R;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by amiteshmaheshwari on 24/07/16.
 */

public class ClipListAdapter  extends BaseAdapter {

    private ArrayList<Clip> clips;
    private LayoutInflater clipInf;
    private String APP_FOLDER;

    public ClipListAdapter(Context c, ArrayList<Clip> theClips) {
        clips = theClips;
        clipInf = LayoutInflater.from(c);
        APP_FOLDER = Downloader.getAppFolder(c);

    }

    public int getCount() {
        return clips.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = (RelativeLayout) clipInf.inflate(R.layout.clip, parent, false);
        }
        TextView clipTitleView = (TextView) convertView.findViewById(R.id.playlist_clip_title);
        ImageView thumbnilView = (ImageView) convertView.findViewById(R.id.playlist_clip_thumbnil);
        Clip currClip= clips.get(position);
        clipTitleView .setText(currClip.getName());
        File thumbnilFile = new File(APP_FOLDER, currClip.getThumbnil());
        //thumbnilView.setImageBitmap(BitmapFactory.decodeFile(currClip.getThumbnil()));
        thumbnilView.setImageURI(Uri.fromFile(thumbnilFile));
        convertView.setTag(position);
        return convertView;
    }

}