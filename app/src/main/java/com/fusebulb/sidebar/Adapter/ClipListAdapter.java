package com.fusebulb.sidebar.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fusebulb.sidebar.Clip;
import com.fusebulb.sidebar.R;

import java.util.ArrayList;

/**
 * Created by amiteshmaheshwari on 24/07/16.
 */

public class ClipListAdapter  extends BaseAdapter {

    private ArrayList<Clip> clips;
    private LayoutInflater clipInf;

    public ClipListAdapter(Context c, ArrayList<Clip> theClips) {
        clips = theClips;
        clipInf = LayoutInflater.from(c);
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
            convertView = (LinearLayout) clipInf.inflate(R.layout.clip, parent, false);
        }
        TextView songView = (TextView) convertView.findViewById(R.id.show_tour_drawer_clip_name);
        Clip currClip= clips.get(position);
        songView.setText(currClip.getName());
        convertView.setTag(position);
        return convertView;
    }

}