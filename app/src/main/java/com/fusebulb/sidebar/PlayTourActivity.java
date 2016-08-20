package com.fusebulb.sidebar;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.TimedText;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.MediaController;
import android.widget.TextView;
import android.os.Handler;

import com.fusebulb.sidebar.Adapter.ClipListAdapter;
import com.fusebulb.sidebar.Helpers.Downloader;
import com.fusebulb.sidebar.Models.Clip;
import com.fusebulb.sidebar.Parser.TourClipParser;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import android.widget.MediaController.MediaPlayerControl;


/**
 * Created by amiteshmaheshwari on 24/07/16.
 */
public class PlayTourActivity extends AppCompatActivity implements View.OnClickListener, MediaPlayer.OnTimedTextListener, MediaPlayerControl, AdapterView.OnItemClickListener {

    private static final String TAG = "PlayTourActivity";
    public static final String ACTION_SET_IMAGE_IN_FOCUS = "SET_IMAGE_IN_FOCUS";
    public static final String ACTION_SET_TIMED_LISTENER = "SET_TIMED_LISTENER";
    public static final String KEY_FILE_PATH_IMAGE_IN_FOCUS = "FILE_PATH";

    private static ArrayList<Clip> clipList;
    private PlayTourService playTourService;
    private boolean serviceBound;

    private DataUpdateReceiver dataUpdateReceiver;

    private Intent playIntent;
    private ImageButton showPlayListBtn;
    private MusicController clipController;
    private DrawerLayout drawerLayoutPlayList;
    private ListView listViewDrawer;
    private static Handler handler = new Handler();

    private TextView subtitlesView;
    private ImageView pictureInFocus, playBtn, exitBtn, showPlaylistBtn;
    private static final String ACTION_SPLITTER = "##";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_tour);

        Bundle extras = getIntent().getExtras();
        String tourSourcePath = extras.getString("TOUR_SOURCE");

        try {
            TourClipParser clipParser = new TourClipParser(this, tourSourcePath);
            clipParser.execute();
            clipList = clipParser.get();
            initializeView();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (dataUpdateReceiver == null) dataUpdateReceiver = new DataUpdateReceiver();
        IntentFilter intentFilter = new IntentFilter(ACTION_SET_IMAGE_IN_FOCUS);
        registerReceiver(dataUpdateReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (dataUpdateReceiver != null) unregisterReceiver(dataUpdateReceiver);
    }

    public void clipPickedInDrawer(View view) {
        setClipIndex(Integer.parseInt(view.getTag().toString()));
        start();
    }

//    private void setController(){
//        clipController = new MusicController(this);
//
//        //clipController.setVisibility(View.INVISIBLE);
//        clipController.setMediaPlayer(this);
//        clipController.setAnchorView(findViewById(R.id.play_tour_picture_in_focus));
//        clipController.setEnabled(true);
//    }

    private ServiceConnection mediaPlayerConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            PlayTourService.ClipPlayerBinder binder = (PlayTourService.ClipPlayerBinder) service;
            playTourService = binder.getService();
            playTourService.setClipList(clipList);
            playTourService.setAppFolder(getAppFolder());
            playTourService.setContext(PlayTourActivity.this);
            setClipIndex(0);
            serviceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            serviceBound = false;
        }
    };


    private void initializeView() {
        subtitlesView = (TextView) findViewById(R.id.play_tour_subtitle_box);
        subtitlesView.setVisibility(View.GONE);
        pictureInFocus = (ImageView) findViewById(R.id.play_tour_picture_in_focus);


        playBtn = (ImageView) findViewById(R.id.audioController_playBtn);
        playBtn.setOnClickListener(this);
        playBtn.setVisibility(View.VISIBLE);

        exitBtn = (ImageView) findViewById(R.id.audioController_exitBtn);
        exitBtn.setOnClickListener(this);
        exitBtn.setVisibility(View.VISIBLE);
        showPlaylistBtn = (ImageButton) findViewById(R.id.audioController_playlistBtn);

        showPlaylistBtn.setOnClickListener(this);
        drawerLayoutPlayList = (DrawerLayout) findViewById(R.id.drawerLayout_playlist_right_play_tour);
        listViewDrawer = (ListView) findViewById(R.id.listView_rightDrawer);
        if (listViewDrawer != null) {
            ClipListAdapter clipListAdapter = new ClipListAdapter(this, clipList);
            listViewDrawer.setAdapter(clipListAdapter);
            listViewDrawer.setOnItemClickListener(this);
        }

    }

    private String getAppFolder() {
        return Downloader.getAppFolder(this);
    }

    private void setClipIndex(int clipIndex) {
        // download the clip file and action files
        // show loading & change the ply
        playTourService.setClipIndex(clipIndex);
        //setSubTitles(playTourService.getMediaPlayer(), actionFile);
    }

    @Override
    public void onTimedText(final MediaPlayer mp, final TimedText timedText) {
        if (timedText != null) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    String text = timedText.getText();
                    String[] components = text.trim().split(ACTION_SPLITTER);

                    String action = components[0];
                    switch (action) {
                        case "Media":
                            setPictureInFocus(components[1]);
                            break;
                        case "Map":
                            setActionText(components[1]);
                            break;
                        case "Camera":
                            setActionText(components[1]);
                            break;

                    }

                }
            });
        }
    }

    public void setActionText(String text) {
        subtitlesView.setText(text);
        subtitlesView.setVisibility(View.VISIBLE);
        pictureInFocus.setImageAlpha(50);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (playIntent == null) {
            playIntent = new Intent(this, PlayTourService.class);
            bindService(playIntent, mediaPlayerConnection, Context.BIND_AUTO_CREATE);
            //startService(playIntent);
        }
    }

    public boolean isClipPlaying() {
        return playTourService.isPlayerPlaying();
    }

//    public void playClip() {
//        audioController.setPlayClip();
//        playTourService.playClip();
//        //setClipDefaultPictures
//    }

    public void stopClip() {
        playTourService.stopClip();
        onBackPressed();
    }


//    public void pauseClip() {
//        audioController.setPauseClip();
//        playTourService.pauseClip();
//    }

    public void fastForwardClip() {
        playTourService.fastForwardPlayer();

    }

    public void rewindClip() {
        playTourService.rewindPlayer();
    }


    public void openDrawer() {
        if (drawerLayoutPlayList != null && !drawerLayoutPlayList.isDrawerOpen(GravityCompat.END)) {
            drawerLayoutPlayList.openDrawer(GravityCompat.END);
        }
    }

    // PlayTour UI Elements

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.audioController_playlistBtn:

                if (isClipPlaying()) {
                    fastForwardClip();
                } else {
                    openDrawer();
                }
                break;

            case R.id.audioController_playBtn:
                if (isClipPlaying()) {
                    pause();
                } else {
                    start();
                }
                break;


            case R.id.audioController_exitBtn:
                if (isClipPlaying()) {
                    rewindClip();
                } else {
                    stopClip();
                }
                break;

//            case R.id.play_tour_openCamera_btn:
//                startActivity(new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE));
//                break;
//            case R.id.play_tour_openMap_btn:
//                Location loc = playTourService.getCurrentClipLocation();
//                String query= "http://maps.google.com/maps?daddr="+loc.getLongitude()+","+loc.getLatitude();
//                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
//                        Uri.parse(query));
//                startActivity(intent);
//
//                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {

            case R.id.listView_rightDrawer:

                if (drawerLayoutPlayList != null && drawerLayoutPlayList.isDrawerOpen(GravityCompat.END)) {
                    drawerLayoutPlayList.closeDrawer(GravityCompat.END);
                }

                switch (position) {
                    case 0:// X for close
                        break;
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayoutPlayList != null && drawerLayoutPlayList.isDrawerOpen(GravityCompat.END)) {
            drawerLayoutPlayList.closeDrawer(GravityCompat.END);
        } else
            super.onBackPressed();
    }


    @Override
    protected void onDestroy() {
        unbindService(mediaPlayerConnection);
        stopService(playIntent);
        playTourService = null;
        super.onDestroy();
    }

    public void setPictureInFocus(String picturePath) {
        File pictureFile = new File(getAppFolder(), picturePath);
        Drawable d = Drawable.createFromPath(pictureFile.getAbsolutePath());
        pictureInFocus.setBackground(d);
    }

    @Override
    public void start() {

        playBtn.setBackgroundResource(R.drawable.btn_paused_tour);
        exitBtn.setBackgroundResource(R.drawable.btn_rewind_tour);
        showPlaylistBtn.setBackgroundResource(R.drawable.btn_fforward_tour);

        playTourService.playClip();
        //clipController.show(0);
    }

    @Override
    public void pause() {
        //audioController.setPauseClip();

        playBtn.setBackgroundResource(R.drawable.btn_play_rounded);
        exitBtn.setBackgroundResource(R.drawable.btn_stop_tour);
        showPlaylistBtn.setBackgroundResource(R.drawable.btn_show_playlist);

        playTourService.pauseClip();
    }

    @Override
    public int getDuration() {
        return playTourService.getClipDuration();
    }

    @Override
    public int getCurrentPosition() {
        return playTourService.getPlayerCurrentPosition();
    }

    @Override
    public void seekTo(int i) {
        playTourService.seekPlayerTo(i);
    }

    @Override
    public boolean isPlaying() {
        return playTourService.isPlayerPlaying();
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return false;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }

    private class DataUpdateReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();

            switch (action) {
                case ACTION_SET_IMAGE_IN_FOCUS:
                    setPictureInFocus(intent.getStringExtra(KEY_FILE_PATH_IMAGE_IN_FOCUS));
                    playTourService.getMediaPlayer().setOnTimedTextListener(PlayTourActivity.this);
                    break;
            }

        }
    }
}
