package com.fusebulb.sidebar.Fragments;

import android.app.Fragment;
import android.media.MediaPlayer;
import android.media.TimedText;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fusebulb.sidebar.R;

/**
 * Created by amiteshmaheshwari on 29/07/16.
 */
public class ActionScreen extends Fragment implements MediaPlayer.OnTimedTextListener {

    @Override
    public void onTimedText(MediaPlayer mediaPlayer, TimedText timedText) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.audio_play_tour_fragment, container, false);
    }
}
