package com.fusebulb.sidebar;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.TimedText;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.MediaController.MediaPlayerControl;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;

import com.fusebulb.sidebar.Adapter.ClipListAdapter;
import com.fusebulb.sidebar.Fragments.AudioController;
import com.fusebulb.sidebar.Parser.TourClipParser;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


/**
 * Created by amiteshmaheshwari on 24/07/16.
 */
public class PlayTourActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, MediaPlayer.OnTimedTextListener {

    private static final String TAG = "PlayTourActivity";

    private static ArrayList<Clip> clipList;
    private PlayTourService playTourService;
    private boolean serviceBound;

    private Intent playIntent;
    private ImageButton showPlayListBtn;
    private DrawerLayout drawerLayoutPlayList;
    private ListView listViewDrawer;
    private AudioController audioController;
    private static Handler handler = new Handler();
    private TextView subtitlesView;
    private ImageView pictureInFocus;
    private ActionListener actionListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_tour);


        actionListener = new ActionListener();
        subtitlesView = (TextView) findViewById(R.id.play_tour_subtitle_box);
        subtitlesView.setVisibility(View.GONE);
        pictureInFocus = (ImageView) findViewById(R.id.play_tour_picture_in_focus);


        Bundle extras = getIntent().getExtras();
        String tourSourcePath = "";

        if (extras != null) {
            tourSourcePath = extras.getString("TOUR_SOURCE");
            // and get whatever type user account id is
        } else {
            Log.e(TAG, "No tour source passed.");
        }

        TourClipParser clipParser = new TourClipParser(this, tourSourcePath);
        clipParser.execute();

        try {
            clipList = clipParser.get();
            initializeView();
            openAudioControllerFragment();
            //songView.setAdapter(songAdpater);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void clipPickedInDrawer(View view) {
        setClipIndex(Integer.parseInt(view.getTag().toString()));
        playClip();

    }

    private void prepareClipActionFiles(Clip clip) {
        downloadFile(clip.getPictureSource());
        ArrayList<ClipAction> clipActions = clip.getClipActions();
        for (ClipAction clipAction : clipActions) {
            if (clipAction.getActionType().equals(ClipAction.MediaClipAction)) {
                File mediaFile = downloadFile(((MediaClipAction) clipAction).getMediaSource());
                ((MediaClipAction) clipAction).setMediaFile(mediaFile);
            }
        }
    }

    private File downloadFile(String file_path) {
        File file = null;
        try {
            file = new FileDownloader(this).execute(file_path).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return file;
    }

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

    private String getAppFolder() {
        return getFilesDir().toString() + "/";
    }

    private void setClipIndex(int clipIndex) {
        // download the clip file and action files
        // show loading & change the ply
        Clip clip = playTourService.setClipIndex(clipIndex);

        setPictureInFocus(clip.getPictureSource());

        playTourService.getMediaPlayer().setOnTimedTextListener(this);
        //setSubTitles(playTourService.getMediaPlayer(), actionFile);

    }

    private int findTrackIndexFor(int mediaTrackType, MediaPlayer.TrackInfo[] trackInfo) {
        int index = -1;
        for (int i = 0; i < trackInfo.length; i++) {
            if (trackInfo[i].getTrackType() == mediaTrackType) {
                return i;
            }
        }
        return index;
    }


    @Override
    public void onTimedText(final MediaPlayer mp, final TimedText timedText) {
        if (timedText != null) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    String text = timedText.getText();
                    String[] components = text.split("##");

                    subtitlesView.setText(text);
                    subtitlesView.setVisibility(View.VISIBLE);
                    pictureInFocus.setImageAlpha(50);
                }
            });
        }
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

    public void playClip() {
        audioController.setPlayClip();
        playTourService.playClip();
        //setClipDefaultPictures
    }

    public void stopClip() {
        playTourService.stopClip();
        onBackPressed();
    }


    private void setPicture(String source) {
        //todo
    }

    public void pauseClip() {
        audioController.setPauseClip();
        playTourService.pauseClip();
    }

    public void fastForwardClip() {
        playTourService.fastForwardPlayer();

    }

    public void rewindClip() {
        playTourService.rewindPlayer();
    }


    // PlayTour UI Elements

    private void initializeView() {
        showPlayListBtn = (ImageButton) findViewById(R.id.imageButton_playlist_play_tour);
        if (showPlayListBtn != null) {
            showPlayListBtn.setOnClickListener(this);
        }

        drawerLayoutPlayList = (DrawerLayout) findViewById(R.id.drawerLayout_playlist_right_play_tour);

        listViewDrawer = (ListView) findViewById(R.id.listView_rightDrawer);

        if (listViewDrawer != null) {

            ClipListAdapter clipListAdapter = new ClipListAdapter(this, clipList);
            listViewDrawer.setAdapter(clipListAdapter);
            listViewDrawer.setOnItemClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageButton_playlist_play_tour:
                if (drawerLayoutPlayList != null && !drawerLayoutPlayList.isDrawerOpen(GravityCompat.END)) {
                    drawerLayoutPlayList.openDrawer(GravityCompat.END);
                }
                break;
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

    private void openAudioControllerFragment() {
        audioController = new AudioController();
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_audioController, audioController).commit();
    }


    @Override
    protected void onDestroy() {
        unbindService(mediaPlayerConnection);
        //stopService(playIntent);
        playTourService = null;
        super.onDestroy();
    }

    private String getSubtitleFile(int resId) {
        String fileName = getResources().getResourceEntryName(resId);
        File subtitleFile = getFileStreamPath(fileName);


        if (subtitleFile.exists()) {
            Log.d(TAG, "Subtitle already exists");
            return subtitleFile.getAbsolutePath();
        }
        Log.d(TAG, "Subtitle does not exists, copy it from res/raw");

        // Copy the file from the res/raw folder to your app folder on the
        // device
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = getResources().openRawResource(resId);
            outputStream = new FileOutputStream(subtitleFile, false);
            copyFile(inputStream, outputStream);
            return subtitleFile.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeStreams(inputStream, outputStream);
        }
        return "";
    }

    private void copyFile(InputStream inputStream, OutputStream outputStream)
            throws IOException {
        final int BUFFER_SIZE = 1024;
        byte[] buffer = new byte[BUFFER_SIZE];
        int length = -1;
        while ((length = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, length);
        }
    }

    // A handy method I use to close all the streams
    private void closeStreams(Closeable... closeables) {
        if (closeables != null) {
            for (Closeable stream : closeables) {
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    public void setPictureInFocus(String picturePath) {
        File pictureFile = new File(getAppFolder(), picturePath);
        Bitmap pictureBitmap = BitmapFactory.decodeFile(pictureFile.getAbsolutePath());

        pictureInFocus.setImageBitmap(pictureBitmap);
    }
}
