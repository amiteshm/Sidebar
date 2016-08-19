package com.fusebulb.sidebar.Fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.fusebulb.sidebar.MainActivity;
import com.fusebulb.sidebar.PlayTourActivity;
import com.fusebulb.sidebar.R;

/**
 * Created by amiteshmaheshwari on 25/07/16.
 */

public class AudioController extends Fragment implements View.OnClickListener {

    private final String LOG_TAG = this.getClass().getSimpleName();
    private View rootView;
    private ImageView playBtn, exitBtn, showPlaylistBtn;
    private ImageView rewindBtn, fastForwardBtn;
    private PlayTourActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.audio_play_tour_fragment, container, false);
        Log.v(LOG_TAG, "in onCreate()");
        initializeView();
        return rootView;
    }

    private void initializeView() {
        activity = (PlayTourActivity) getActivity();
        playBtn = (ImageView) rootView.findViewById(R.id.audioController_playBtn);
        playBtn.setOnClickListener(this);
        playBtn.setVisibility(View.VISIBLE);

        exitBtn = (ImageView) rootView.findViewById(R.id.audioController_exitBtn);
        exitBtn.setOnClickListener(this);
        exitBtn.setVisibility(View.VISIBLE);

        showPlaylistBtn = (ImageButton) rootView.findViewById(R.id.audioController_playlistBtn);
        showPlaylistBtn.setOnClickListener(activity);



//        pauseBtn = (ImageView) rootView.findViewById(R.id.audioController_pauseBtn);
//        pauseBtn.setOnClickListener(this);
//        pauseBtn.setVisibility(View.GONE);

//        rewindBtn = (ImageView) rootView.findViewById(R.id.audioController_rewind);
//        rewindBtn.setOnClickListener(this);
//        rewindBtn.setVisibility(View.GONE);

//        fastForwardBtn = (ImageView) rootView.findViewById(R.id.audioController_fast_forward);
//        fastForwardBtn.setOnClickListener(this);
//        fastForwardBtn.setVisibility(View.GONE);

    }

    public void setPlayClip(){
        playBtn.setBackgroundResource(R.drawable.btn_paused_tour);
        //pauseBtn.setVisibility(View.VISIBLE);
    }

    public void setPauseClip(){
        //pauseBtn.setVisibility(View.GONE);
        playBtn.setBackgroundResource(R.drawable.btn_play_rounded);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

//            case R.id.audioController_fast_forward:
//                activity.fastForwardClip();
//                break;
//
//            case R.id.audioController_rewind:
//                activity.rewindClip();
//                break;

            case R.id.audioController_playBtn:
                if(activity.isClipPlaying()){
                    activity.pause();
                }else{
                    activity.start();
                }
                break;

            case R.id.audioController_playlistBtn:

//            case R.id.audioController_pauseBtn:
//                activity.pauseClip();
//                break;


//                if (exitBtn.getVisibility() == View.VISIBLE) {
//                    playBtn.setImageResource(R.drawable.stop_tour_btn);
//                    exitBtn.setVisibility(View.GONE);
//                    fastForwardBtn.setVisibility(View.VISIBLE);
//                    rewindBtn.setVisibility(View.VISIBLE);
//                    activity.start();
//                } else {
//                    playBtn.setImageResource(R.drawable.play_rounded_btn);
//                    exitBtn.setVisibility(View.VISIBLE);
//                    rewindBtn.setVisibility(View.GONE);
//                    fastForwardBtn.setVisibility(View.GONE);
//                    activity.pause();
//
//                }


                    case R.id.audioController_exitBtn:
                        activity.stopClip();
                break;
        }
    }
}
